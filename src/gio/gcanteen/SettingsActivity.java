package gio.gcanteen;

import gio.gcanteen.model.AppContext;
import gio.gcanteen.model.Provider;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		ListPreference providerPreference = (ListPreference) findPreference("pref_provider");
		providerPreference.setEntries(Provider.getEntriesForListPreference());
		providerPreference.setEntryValues(Provider.getEntryValuesForListPreference());
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
	}
	
	@Override
	public void onDestroy() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		AppContext.getAppContext().updateFromPreferences();
	}

}
