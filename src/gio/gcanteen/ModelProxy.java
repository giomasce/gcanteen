package gio.gcanteen;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import org.json.JSONArray;
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
	
	public final static String STATEMENTS_TAG = "statements";
	public final static String STATEMENT_USERNAME_TAG = "username";
	public final static String STATEMENT_VALUE_TAG = "value";
	public final static String STATEMENT_TIMESTAMP_TAG = "timestamp";
	
	private NetworkUtils networkUtils;
	
	private Vector<Statement> statements = null;
	
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
		
		this.statements = new Vector<Statement>();
		try {
			JSONArray jsonStatements = json.getJSONArray(STATEMENTS_TAG);
			for (int i = 0; i < jsonStatements.length(); i++) {
				JSONObject jsonItem = jsonStatements.getJSONObject(i);
				String username = jsonItem.getString(STATEMENT_USERNAME_TAG);
				String value = jsonItem.getString(STATEMENT_VALUE_TAG);
				Date timestamp = new Date(1000 * jsonItem.getLong(STATEMENT_TIMESTAMP_TAG));
				Statement statement = new Statement(username, value, timestamp);
				this.statements.add(statement);
			}
		} catch (JSONException e) {
			Log.w(this.getClass().getName(), "Malformed version object", e);
			statements = null;
			return;
		}
	}
	
	public Collection<Statement> getStatements() {
		return this.statements;
	}

}
