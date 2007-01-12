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
		StyleConstants.setFontFamily(styleSystem, FontManager.getFontToUse());
		StyleConstants.setFontSize(styleSystem, 12);
        StyleConstants.setForeground(styleSystem, Color.black);
        
        styleUser = defaultStyleContext.addStyle("user", styleSystem);
        StyleConstants.setForeground(styleUser, Color.magenta);        
        
        styleError = defaultStyleContext.addStyle("error", styleUser);
        StyleConstants.setForeground(styleError, Color.red);
	}
	
	public MessageWindowDocument() {
		addStyle("system", styleSystem);
		addStyle("user", styleUser);
		addStyle("error", styleError);
	}
	
	public static final int APPEND_DEFAULT = 0;
	public static final int APPEND_ERROR = 1;

	public void append(String str, int type) {
		String style;
		if (type == APPEND_DEFAULT) {
			if (str.length() >= 2 && str.substring(0, 2).equals("->")) {
				style = "user";
			} else {
				style = "system";
			}			
		} else if (type == APPEND_ERROR) {
			style = "error";
		} else {
			style = "system";
		}
		
		try {
			insertString(getLength(), str + "\n", getStyle(style));
		} catch (BadLocationException e) {
		}
	}

	public void unAppend(String str) {
		int length = str.length() + 1;
		try {
			remove(getLength() - length, length);
		} catch (BadLocationException e) {
		}
	}
	
	public void clear() {
		try {
			remove(0, getLength());
		} catch (BadLocationException e) {
		}
	}
}
