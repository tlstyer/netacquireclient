package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AboutDialog extends GameDialog {

	private static final String[] labelTextArray = new String[]{
		Main.getProgramName(),
		"Programmed by Tim Styer",
		"tlstyer@gmail.com",
		"http://www.tlstyer.com/NetAcquireClient/",};

	private final JButton buttonOK;

	private static Boolean aboutDialogShowing = false;
	private static final Boolean aboutDialogShowingSynch = true;

	public AboutDialog() {
		super(DO_NOT_ALLOW_EXTERNAL_HIDE_REQUEST);

		setTitle("About " + Main.getProgramName());

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		for (String labelText : labelTextArray) {
			JLabel label = new JLabel(labelText);
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			panel.add(label);
		}

		buttonOK = Util.getButton3d2("OK", KeyEvent.VK_O);
		buttonOK.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonOK.addActionListener(this);

		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(buttonOK);

		getRootPane().setDefaultButton(buttonOK);

		showGameDialog(POSITION_CENTER_IN_MAIN_FRAME_PANEL);
	}

	public static void showAboutDialog() {
		synchronized (aboutDialogShowingSynch) {
			if (!aboutDialogShowing) {
				new AboutDialog();
				aboutDialogShowing = true;
			}
		}
	}

	private void hideAboutDialog() {
		synchronized (aboutDialogShowingSynch) {
			hideGameDialog();
			aboutDialogShowing = false;
		}
	}

	@Override
	public void DoAction(ActionEvent actionEvent) {
		Object object = actionEvent.getSource();
		if (object == buttonOK) {
			hideAboutDialog();
		}
	}
}
