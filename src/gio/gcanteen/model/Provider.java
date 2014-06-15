package gio.gcanteen.model;

import java.util.Vector;

import android.util.Log;
import gio.gcanteen.R;

public class Provider {

	private String name;
	private String publicName;
	private String baseUrl;
	private int certResource;

	public static final Provider PROVIDERS[] = {
		new Provider("uz", "UZ", "https://uz.sns.it/mensa/api/", R.raw.uz_cacert)
	};

	public Provider(String name, String publicName, String baseUrl, int certResource) {
		this.name = name;
		this.publicName = publicName;
		this.baseUrl = baseUrl;
		this.certResource = certResource;
	}

	public String getName() {
		return this.name;
	}

	public String getPublicName() {
		return this.publicName;
	}

	public String getBaseUrl() {
		return this.baseUrl;
	}
	
	public int getCertResource() {
		return this.certResource;
	}
	
	public static Provider getFromName(String name) {
		for (Provider provider : PROVIDERS) {
			if (provider.getName().equals(name)) {
				return provider;
			}
		}
		Log.e("gCanteen", "Missing provider for name " + name);
		return null;
	}
	
	public static String[] getEntriesForListPreference() {
		Vector<String> entries = new Vector<String>();
		for (Provider provider : PROVIDERS) {
			entries.add(provider.getPublicName());
		}
		return entries.toArray(new String[0]);
	}
	
	public static String[] getEntryValuesForListPreference() {
		Vector<String> entryValues = new Vector<String>();
		for (Provider provider : PROVIDERS) {
			entryValues.add(provider.getName());
		}
		return entryValues.toArray(new String[0]);
	}

}
