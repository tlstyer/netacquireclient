package com.tlstyer.netacquireclient;

import java.awt.*;
import javax.swing.text.*;

public class MessageWindowDocument extends DefaultStyledDocument {
	private static final Style styleNormal;
	private static final Style styleComment;
	private static final Style styleImportant;
	static {
		StyleContext defaultStyleContext = StyleContext.getDefaultStyleContext();
		Style style = defaultStyleContext.getStyle(StyleContext.DEFAULT_STYLE);

		styleNormal = defaultStyleContext.addStyle("normal", style);
		StyleConstants.setFontFamily(styleNormal, FontManager.getFontNameMessageWindows());
		StyleConstants.setFontSize(styleNormal, 12);
		StyleConstants.setForeground(styleNormal, Color.black);

		styleComment = defaultStyleContext.addStyle("comment", styleNormal);
		StyleConstants.setForeground(styleComment, Color.blue);

		styleImportant = defaultStyleContext.addStyle("important", styleComment);
		StyleConstants.setForeground(styleImportant, Color.red);
	}

	public MessageWindowDocument() {
		addStyle("normal", styleNormal);
		addStyle("comment", styleComment);
		addStyle("important", styleImportant);
	}

	public static final int APPEND_NORMAL = 0;
	public static final int APPEND_COMMENT = 1;
	public static final int APPEND_IMPORTANT = 2;

	public void append(String str, int type) {
		String style;
		switch (type) {
			case APPEND_NORMAL:
				style = "normal";
				break;
			case APPEND_COMMENT:
				style = "comment";
				break;
			case APPEND_IMPORTANT:
			default:
				style = "important";
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
