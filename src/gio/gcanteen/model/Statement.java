package gio.gcanteen.model;

import java.util.Date;

public class Statement {

	private String username;
	private String value;
	private Date timestamp;
	
	public Statement(String username, String value, Date timestamp) {
		super();
		this.username = username;
		this.value = value;
		this.timestamp = timestamp;
	}
	
	public String toFormattedString() {
		return "<b>@" + this.getUsername() +
				"</b> (<i>" + this.getTimestamp() +
				"</i>): " + this.getValue();
	}
	
	public String toString() {
		return "@" + this.getUsername() +
				" (" + this.getTimestamp() +
				"): " + this.getValue();
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
