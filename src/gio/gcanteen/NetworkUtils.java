package gio.gcanteen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkUtils {
	
	public final static int READ_TIMEOUT = 10000;
	public final static int CONNECT_TIMEOUT = 1500;
	
	private Context context;

	public NetworkUtils(Context context) {
		this.context = context;
	}
	
	public SSLContext getTrustedSSLContext() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream certIS = this.context.getResources().openRawResource(R.raw.uz_cacert);
		Certificate ca;
		try {
			ca = cf.generateCertificate(certIS);
		} finally {
			certIS.close();
		}
		
		String keyStoreType = KeyStore.getDefaultType();
		KeyStore keyStore = KeyStore.getInstance(keyStoreType);
		keyStore.load(null, null);
		keyStore.setCertificateEntry("ca", ca);
		
		String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
		tmf.init(keyStore);
		
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, tmf.getTrustManagers(), null);
		
		return context;
	}
	
	public HttpURLConnection connectSSLURL(String url) throws MalformedURLException {
		return this.connectSSLURL(new URL(url));
	}
	
	public HttpURLConnection connectSSLURL(URL url) {
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) url.openConnection();
			try {
				((HttpsURLConnection) conn).setSSLSocketFactory(this.getTrustedSSLContext().getSocketFactory());
			} catch (ClassCastException e) {
				// If the protocol is not HTTPS, we just skip this phase
			}
		} catch (Exception e) {
			// We should never arrive here...
			Log.e(this.getClass().getName(), "Couldn't create SSL context", e);
			return null;
		}
		conn.setReadTimeout(READ_TIMEOUT);
		conn.setConnectTimeout(CONNECT_TIMEOUT);
		conn.setDoInput(true);
		return conn;
	}
	
	public HttpURLConnection connectAndCheck(String url) throws UnauthorizedException, IOException {
		return this.connectAndCheck(new URL(url));
	}
	
	public HttpURLConnection connectAndCheck(URL url) throws UnauthorizedException, IOException {
		HttpURLConnection conn = this.connectSSLURL(url);
		conn.connect();
		
		int response = conn.getResponseCode();
		if (response == 200) return conn;
		if (response == 401) throw new UnauthorizedException("Server replid with: " + response + conn.getResponseMessage());

		// If we arrive here, then the server replied with an unexpected code
		throw new IOException("Server replid with: " + response + conn.getResponseMessage());
	}
	
	public static JSONObject connToJSON(HttpURLConnection conn) throws IOException, JSONException {
		InputStream inputStream = conn.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 4096);
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line + "\n");
		}
		inputStream.close();
		return new JSONObject(stringBuilder.toString());
	}
	
	public JSONObject connectAndGetJSON(String url) throws UnauthorizedException, IOException {
		HttpURLConnection conn;
		try {
			conn = this.connectAndCheck(url);
		} catch (MalformedURLException e) {
			// We should never arrive here
			Log.e(this.getClass().getName(), "Internal error when creating the request URL", e);
			return null;
		}
		
		JSONObject json;
		try {
			json = NetworkUtils.connToJSON(conn);
		} catch (JSONException e) {
			Log.w(this.getClass().getName(), "Error while decoding JSON", e);
			return null;
		}
		
		return json;
	}
	
	public void setCredentials(final LoginCredentials loginCredentials) {
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(loginCredentials.username, loginCredentials.password.toCharArray());
			}
		});
	}
    
    boolean testConnectivity() {
    	ConnectivityManager connMgr = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo info = connMgr.getActiveNetworkInfo();
    	if (info != null && info.isConnected()) return true;
    	else return false;
    }

}
