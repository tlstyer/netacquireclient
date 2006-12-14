package com.tlstyer.netacquire;

import java.awt.*;
import javax.swing.*;

public class MessageWindow extends JScrollPane {
	private static final long serialVersionUID = 89083236981415606L;

	private JTextPane textPane = new JTextPane();
	private MessageWindowDocument messageWindowDocument;
	private TransferHandler transferHandler;
	
	private static final Color colorBackground = new Color(192, 192, 255);
	
	public MessageWindow() {
		textPane.setEditable(false);
		textPane.setBackground(colorBackground);
		setViewportView(textPane);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		transferHandler = textPane.getTransferHandler();
		
		messageWindowDocument = new MessageWindowDocument();
		textPane.setStyledDocument(messageWindowDocument);
	}
	
	public void setMode(int mode) {
		if (mode == Main.MODE_REVIEW) {
			textPane.setFocusable(false);
			textPane.setTransferHandler(null);
		} else {
			textPane.setFocusable(true);
			textPane.setTransferHandler(transferHandler);
		}
	}
	
	public void append(String str, int type) {
		messageWindowDocument.append(str, type);
		textPane.setCaretPosition(messageWindowDocument.getLength());
	}

	public void unAppend(String str) {
		messageWindowDocument.unAppend(str);
		textPane.setCaretPosition(messageWindowDocument.getLength());
	}
	
	public void clear() {
		messageWindowDocument.clear();
	}
}
