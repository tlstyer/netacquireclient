package com.tlstyer.netacquire;

import java.awt.*;

public class FontManager {
	private static Font fontBoldDialog = new Font("Monospaced", Font.BOLD, 16);
	private static Font fontMessageWindow = new Font("Monospaced", Font.PLAIN, 12);
	
	public FontManager() {
	}

	public Font getBoldDialogFont() {
		return fontBoldDialog;
	}

	public Font getMessageWindowFont() {
		return fontMessageWindow;
	}
}
