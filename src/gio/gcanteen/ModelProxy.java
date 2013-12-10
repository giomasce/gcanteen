package gio.gcanteen;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ModelProxy {
	
	public final static int SUPPORTED_VERSION = 1;
	public final static String BASE_URL = "https://uz.sns.it/mensa/api/";
	public final static String VERSION_URL = "version.json";
	public final static String VERSIONED_BASE_URL = BASE_URL + "v" + SUPPORTED_VERSION + "/"; 
	public final static String STATEMENTS_URL = "statements.json";
	
	public final static String MIN_VERSION_TAG = "min_version";
	public final static String MAX_VERSION_TAG = "max_version";
	
	private NetworkUtils networkUtils;
	
	public ModelProxy(NetworkUtils networkUtils) {
		this.networkUtils = networkUtils;
	}
	
	public boolean checkVersion() throws IOException, UnauthorizedException {
		JSONObject json = this.networkUtils.connectAndGetJSON(BASE_URL + VERSION_URL);
		if (json == null) return false;
		
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
	
	public void loadStatements() throws UnauthorizedException, IOException {
		JSONObject json = this.networkUtils.connectAndGetJSON(VERSIONED_BASE_URL + STATEMENTS_URL);
		if (json == null) return;
	}

}
