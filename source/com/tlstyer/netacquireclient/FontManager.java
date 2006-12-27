package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.font.*;

public class FontManager {
	private static final int LENGTH_TEXTCOMPONENTFONTDATAARRAY = 16;
	private TextComponentFontData[] textComponentFontDataArray = new TextComponentFontData[LENGTH_TEXTCOMPONENTFONTDATAARRAY];
	
	private static final Font fontBoldDialog = new Font("Monospaced", Font.BOLD, 16);
	private static final Font fontMessageWindow = new Font("Monospaced", Font.PLAIN, 12);
	
	public FontManager() {
	}
	
	public void initializeTextComponentFontData(FontRenderContext fontRenderContext) {
		for (int index=0; index<LENGTH_TEXTCOMPONENTFONTDATAARRAY; ++index) {
			textComponentFontDataArray[index] = new TextComponentFontData(fontRenderContext, (index+1)*2);
		}
	}
	
	public TextComponentFontData getTextComponentFontData(int height) {
		int index = height / 4;
		if (index < 0) {
			index = 0;
		} else if (index >= LENGTH_TEXTCOMPONENTFONTDATAARRAY) {
			index = LENGTH_TEXTCOMPONENTFONTDATAARRAY - 1;
		}
		return textComponentFontDataArray[index];
	}

	public Font getBoldDialogFont() {
		return fontBoldDialog;
	}

	public Font getMessageWindowFont() {
		return fontMessageWindow;
	}
}
