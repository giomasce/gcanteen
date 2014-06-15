package gio.gcanteen.model;

import java.io.UnsupportedEncodingException;

import android.util.Log;
import gio.gcanteen.utils.Base64;

public class LoginCredentials {
	public String username;
	public String password;
	
	public LoginCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String toHTTPAuthValue() {
		String toEncode = this.username + ":" + this.password;
		try {
			return "basic " + Base64.encode(toEncode.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			Log.e("gCanteen", "Exception while encoding Base64", e);
			return null;
		}
	}
}