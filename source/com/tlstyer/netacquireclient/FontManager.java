package com.tlstyer.netacquire;

import java.awt.*;

public class FontManager {
	private static Font fontNormal = new Font("Monospaced", Font.BOLD, 16);
	private static Font fontSmall = new Font("Monospaced", Font.PLAIN, 12);
	
	public FontManager() {
	}

	public Font getFont() {
		return fontNormal;
	}

	public Font getSmallFont() {
		return fontSmall;
	}
}
