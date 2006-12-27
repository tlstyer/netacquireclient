package com.tlstyer.netacquireclient;

import java.awt.event.*;
import javax.swing.*;

public class PostMessageTextField extends JTextField implements ActionListener {
	private static final long serialVersionUID = 8604009977121656758L;
	
	String type = null;
	
	public PostMessageTextField (String type_) {
		type = type_;
		addActionListener(this);
	}
	
    public void actionPerformed(ActionEvent e) {
    	String message = getText().replace("\"", "\"\"");
    	setText("");
   		Main.getNetworkConnection().writeMessage("BM;" + type + ",\"" + message + "\"");
    }
}
