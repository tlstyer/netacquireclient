package com.tlstyer.netacquireclient;

import java.awt.event.*;
import javax.swing.*;

public class PostMessageTextField extends JTextField implements ActionListener {

	String type = null;

	public PostMessageTextField(String type_) {
		type = type_;
		addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent actionEvent) {
		String message = getText().replace("\"", "\"\"");
		setText("");
		Main.getNetworkConnection().writeMessage("BM;" + type + ",\"" + message + "\"");
	}
}
