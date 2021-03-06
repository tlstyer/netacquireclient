package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.font.*;

public class TextComponentFontData {

	private final Font font;
	private final double fontHeight;
	private final double fontY;

	public TextComponentFontData(FontRenderContext fontRenderContext, int size) {
		font = new Font(FontManager.getFontNameTextComponents(), Font.BOLD, size);
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
