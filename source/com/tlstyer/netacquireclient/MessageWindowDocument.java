package com.tlstyer.netacquireclient;

import java.awt.*;
import javax.swing.text.*;

public class MessageWindowDocument extends DefaultStyledDocument {
	private static final long serialVersionUID = -7707575450662821467L;

	private static Style styleSystem;
	private static Style styleUser;
	private static Style styleError;
	static {
		StyleContext defaultStyleContext = StyleContext.getDefaultStyleContext();
		Style style = defaultStyleContext.getStyle(StyleContext.DEFAULT_STYLE);

		styleSystem = defaultStyleContext.addStyle("system", style);
		StyleConstants.setFontFamily(styleSystem, FontManager.getFontNameMessageWindows());
		StyleConstants.setFontSize(styleSystem, 12);
		StyleConstants.setForeground(styleSystem, Color.black);

		styleUser = defaultStyleContext.addStyle("user", styleSystem);
		StyleConstants.setForeground(styleUser, Color.blue);

		styleError = defaultStyleContext.addStyle("error", styleUser);
		StyleConstants.setForeground(styleError, Color.red);
	}

	public MessageWindowDocument() {
		addStyle("system", styleSystem);
		addStyle("user", styleUser);
		addStyle("error", styleError);
	}

	public static final int APPEND_SYSTEM = 0;
	public static final int APPEND_USER = 1;
	public static final int APPEND_ERROR = 2;

	public void append(String str, int type) {
		String style;
		switch (type) {
			case APPEND_SYSTEM:
				style = "system";
				break;
			case APPEND_USER:
				style = "user";
				break;
			case APPEND_ERROR:
			default:
				style = "error";
				break;
		}

		try {
			insertString(getLength(), str + "\n", getStyle(style));
		} catch (BadLocationException badLocationException) {
		}
	}

	public void unAppend(String str) {
		int length = str.length() + 1;
		try {
			remove(getLength() - length, length);
		} catch (BadLocationException badLocationException) {
		}
	}

	public void clear() {
		try {
			remove(0, getLength());
		} catch (BadLocationException badLocationException) {
		}
	}
}
