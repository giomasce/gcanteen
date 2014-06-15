package gio.gcanteen.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppContext {

	private Context context;
	private ModelProxy modelProxy;
	private NetworkUtils networkUtils;
	private Provider provider;
	
	private static AppContext appContext;
	
	public AppContext(Context context) {
		this.context = context;
		this.networkUtils = new NetworkUtils(this.context);
		this.modelProxy = new ModelProxy();
		
		this.updateFromPreferences();
	}
	
	public static void newAppContext(Context context) {
		AppContext.appContext = new AppContext(context);
	}
	
	public static AppContext getAppContext() {
		if (AppContext.appContext == null) {
			throw new InternalError();
		}
		return AppContext.appContext;
	}
	
	public void updateFromPreferences() {
    	SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
		String username = sharedPreferences.getString("pref_username", null);
		String password = sharedPreferences.getString("pref_password", null);
		this.provider = Provider.getFromName(sharedPreferences.getString("pref_provider", null));
		this.networkUtils.setCredentials(new LoginCredentials(username, password));
	}

	public ModelProxy getModelProxy() {
		return this.modelProxy;
	}

	public NetworkUtils getNetworkUtils() {
		return this.networkUtils;
	}
	
	public Provider getProvider() {
		return this.provider;
	}

	public Context getContext() {
		return this.context;
	}

}
