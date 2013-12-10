package gio.gcanteen;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ModelProxy {
	
	public final static String BASE_URL = "https://uz.sns.it/mensa/api/";
	public final static String VERSION_URL = "version.json";
	public final static int SUPPORTED_VERSION = 1;
	
	public final static String MIN_VERSION_TAG = "min_version";
	public final static String MAX_VERSION_TAG = "max_version";
	
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
		
		JSONObject json;
		try {
			json = NetworkUtils.connToJSON(conn);
		} catch (JSONException e) {
			Log.w(this.getClass().getName(), "Error while decoding JSON", e);
			return false;
		}
		
		int min_version, max_version;
		try {
			min_version = json.getInt(MIN_VERSION_TAG);
			max_version = json.getInt(MAX_VERSION_TAG);
		} catch (JSONException e) {
			Log.w(this.getClass().getName(), "Malformed version object", e);
			return false;
		}
		
		return (min_version <= SUPPORTED_VERSION) && (SUPPORTED_VERSION <= max_version);
	}

}
