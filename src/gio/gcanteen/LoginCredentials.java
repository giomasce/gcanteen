package gio.gcanteen;

import android.util.Base64;

public class LoginCredentials {
	public String username;
	public String password;
	
	public LoginCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String toHTTPAuthValue() {
		return "basic " + Base64.encodeToString((this.username + ":" + this.password).getBytes(), Base64.DEFAULT);
	}
}