package com.tlstyer.netacquire;

import java.awt.*;
import java.awt.font.*;

public class TextComponentFontData {
	private Font font;
    private double fontHeight;
    private double fontY;

	public TextComponentFontData(FontRenderContext fontRenderContext, int size) {
		font = new Font("Monospaced", Font.BOLD, size);
    	TextLayout textLayout = new TextLayout("0", font, fontRenderContext);
    	fontHeight = textLayout.getBounds().getHeight();
    	fontY = textLayout.getBounds().getY();
	}

	public Font getFont() {
		return font;
	}

	public double getFontHeight() {
		return fontHeight;
	}

	public double getFontY() {
		return fontY;
	}
}
