package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AboutDialog extends GameDialog implements ActionListener {
	private static final long serialVersionUID = 6464857331964610658L;

	private static final String[] labelTextArray = new String[]{
		Main.getProgramName(),
		"Programmed by Tim Styer",
		"NetAcquireClient@tlstyer.com",
		"http://www.tlstyer.com/NetAcquireClient/",
	};
	
	private JButton buttonOk;
	
	private static Boolean aboutDialogShowing = false;
	
	public AboutDialog() {
		super(DO_NOT_ALLOW_EXTERNAL_HIDE_REQUEST);
		
		setTitle("About " + Main.getProgramName());
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		for (String labelText : labelTextArray) {
			JLabel label = new JLabel(labelText);
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.add(label);
		}
		
		buttonOk = Util.getButton3d2("Ok", KeyEvent.VK_O);
		buttonOk.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonOk.addActionListener(this);
		
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(buttonOk);
		
		getRootPane().setDefaultButton(buttonOk);
		
		showGameDialog(GameDialog.POSITION_CENTER_IN_MAIN_FRAME_PANEL);
	}

	public static void showAboutDialog() {
		synchronized (aboutDialogShowing) {
			if (!aboutDialogShowing) {
				new AboutDialog();
				aboutDialogShowing = true;
			}
		}
	}
	
	private void hideAboutDialog() {
		synchronized (aboutDialogShowing) {
			hideGameDialog();
			aboutDialogShowing = false;		
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if (object == buttonOk) {
			hideAboutDialog();
		}
	}
}
