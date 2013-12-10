package gio.gcanteen;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.net.ssl.HttpsURLConnection;

import android.util.Log;

public class ModelProxy {
	
	public final static String BASE_URL = "https://uz.sns.it/mensa/api/";
	public final static String VERSION_URL = "version.json";
	public final static int SUPPORTED_VERSION = 1;
	
	private NetworkUtils networkUtils;
	
	public ModelProxy(NetworkUtils networkUtils) {
		this.networkUtils = networkUtils;
	}
	
	public boolean checkVersion() throws IOException, UnauthorizedException {
		HttpsURLConnection conn;
		try {
			conn = this.networkUtils.connectAndCheck(BASE_URL + VERSION_URL);
		} catch (MalformedURLException e) {
			// We should never arrive here
			Log.e(this.getClass().getName(), "Internal error when creating the request URL", e);
			return false;
		}
		
		return true;
	}

}
