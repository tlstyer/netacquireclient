package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.font.*;

public class FontManager {

	private static final int LENGTH_TEXTCOMPONENTFONTDATAARRAY = 16;
	private final TextComponentFontData[] textComponentFontDataArray = new TextComponentFontData[LENGTH_TEXTCOMPONENTFONTDATAARRAY];
	private int classicTextComponentHeight = 0;

	private static final String fontNameTextComponents = "SansSerif";
	private static final String fontNameMessageWindows = "Monospaced";

	private static final Font fontBoldDialog = new Font(fontNameTextComponents, Font.BOLD, 16);
	private static final Font fontMessageWindow = new Font(fontNameMessageWindows, Font.PLAIN, 12);

	public FontManager() {
	}

	public void initializeTextComponentFontData(FontRenderContext fontRenderContext) {
		for (int index = 0; index < LENGTH_TEXTCOMPONENTFONTDATAARRAY; ++index) {
			textComponentFontDataArray[index] = new TextComponentFontData(fontRenderContext, (index + 1) * 2);
		}
	}

	public void setClassicTextComponentHeight(int classicTextComponentHeight) {
		this.classicTextComponentHeight = classicTextComponentHeight;
	}

	public TextComponentFontData getTextComponentFontData(int divisor) {
		int index = classicTextComponentHeight / divisor;
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

	public static String getFontNameTextComponents() {
		return fontNameTextComponents;
	}

	public static String getFontNameMessageWindows() {
		return fontNameMessageWindows;
	}
}
