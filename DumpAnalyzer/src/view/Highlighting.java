package view;

import java.awt.Color;

public class Highlighting {
	private String token;
	private Color backgroundColor;
	
	public Highlighting(String token, Color backgroundColor) {
		this.token = token;
		this.backgroundColor = backgroundColor;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
