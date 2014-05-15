package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

public class ModeDialog extends GameDialog {

	// "Nickname" panel
	private JComboBox cbNickname;
	private JButton buttonDeleteNickname;

	// "IPURL:Port" panel
	private JComboBox cbIPURLPort;
	private JButton buttonDeleteIPURLPort;

	// "Play" panel
	private JButton buttonPlay;

	// "Review" panel
	private JButton buttonReview;

	private static final Pattern badNicknameChars = Pattern.compile(",|;|:|\"");

	public ModeDialog() {
		super(DO_NOT_ALLOW_EXTERNAL_HIDE_REQUEST);

		setTitle("Choose Mode");

		// "Nickname" panel
		JLabel labelNickname = new JLabel("Nickname:", JLabel.TRAILING);
		labelNickname.setDisplayedMnemonic(KeyEvent.VK_N);

		cbNickname = new JComboBox(Main.getUserPreferences().getStringArrayList(UserPreferences.NICKNAMES).toArray());
		cbNickname.setEditable(true);
		labelNickname.setLabelFor(cbNickname);

		buttonDeleteNickname = new JButton("X");
		buttonDeleteNickname.addActionListener(this);

		JPanel panelNickname = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelNickname.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panelNickname.add(labelNickname);
		panelNickname.add(cbNickname);
		panelNickname.add(buttonDeleteNickname);

		// "IPURL:Port" panel
		JLabel labelIPURLPort = new JLabel("IP/URL:Port:", JLabel.TRAILING);
		labelIPURLPort.setDisplayedMnemonic(KeyEvent.VK_I);

		cbIPURLPort = new JComboBox(Main.getUserPreferences().getStringArrayList(UserPreferences.ADDRESSES_AND_PORTS).toArray());
		cbIPURLPort.setEditable(true);
		labelIPURLPort.setLabelFor(cbIPURLPort);

		buttonDeleteIPURLPort = new JButton("X");
		buttonDeleteIPURLPort.addActionListener(this);

		JPanel panelIPURLPort = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panelIPURLPort.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panelIPURLPort.add(labelIPURLPort);
		panelIPURLPort.add(cbIPURLPort);
		panelIPURLPort.add(buttonDeleteIPURLPort);

		// make the combo boxes the same width
		int width = labelIPURLPort.getPreferredSize().width * 3;
		Dimension dimension;

		dimension = cbNickname.getPreferredSize();
		dimension.width = width;
		Util.setOnlySize(cbNickname, dimension);

		dimension = cbIPURLPort.getPreferredSize();
		dimension.width = width;
		Util.setOnlySize(cbIPURLPort, dimension);

		// "Play" panel
		buttonPlay = Util.getButton3d2("Play", KeyEvent.VK_P);
		buttonPlay.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonPlay.addActionListener(this);

		JPanel panelGame = new JPanel();
		panelGame.setBorder(BorderFactory.createTitledBorder("Play"));
		panelGame.setLayout(new BoxLayout(panelGame, BoxLayout.Y_AXIS));
		panelGame.add(panelNickname);
		panelGame.add(panelIPURLPort);
		panelGame.add(buttonPlay);

		// "Review" button panel
		buttonReview = Util.getButton3d2("Review", KeyEvent.VK_R);
		buttonReview.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonReview.addActionListener(this);

		JPanel panelButtonReview = new JPanel();
		panelButtonReview.setLayout(new BoxLayout(panelButtonReview, BoxLayout.X_AXIS));
		panelButtonReview.add(Box.createHorizontalGlue());
		panelButtonReview.add(buttonReview);

		// "Review" panel
		JPanel panelReview = new JPanel();
		panelReview.setBorder(BorderFactory.createTitledBorder("Review"));
		panelReview.setLayout(new BoxLayout(panelReview, BoxLayout.Y_AXIS));
		panelReview.add(panelButtonReview);

		// put them all together
		panelGame.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelReview.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panelGame);
		panel.add(panelReview);

		getRootPane().setDefaultButton(buttonPlay);

		showGameDialog(POSITION_0_0);
	}

	private void ShowErrorMessage(String title, String message) {
		JOptionPane.showMessageDialog(Main.getMainFrame(), message, title, JOptionPane.ERROR_MESSAGE);
		toFront();
	}

	private void processButtonDelete(ArrayList<String> strings, JComboBox comboBox) {
		String nickname = ((String) comboBox.getSelectedItem());
		strings.remove(nickname);

		int selectedIndex = comboBox.getSelectedIndex();
		if (selectedIndex >= 0) {
			comboBox.removeItemAt(selectedIndex);
			int itemCount = comboBox.getItemCount();
			if (itemCount > 0) {
				if (selectedIndex >= itemCount) {
					selectedIndex = itemCount - 1;
				}
				comboBox.setSelectedIndex(selectedIndex);
			} else {
				comboBox.setSelectedItem("");
			}
		} else {
			int itemCount = comboBox.getItemCount();
			if (itemCount > 0) {
				comboBox.setSelectedIndex(0);
			} else {
				comboBox.setSelectedItem("");
			}
		}
	}

	private void buttonPlayPressed() {
		// figure out input and check for bad input
		String[] ipurlAndPort = ((String) cbIPURLPort.getSelectedItem()).split(":", -1);

		if (ipurlAndPort.length != 2) {
			ShowErrorMessage("Bad IP/URL:Port format", "IP/URL:Port format must be <IP/URL>:<Port>");
			return;
		}

		String nickname = ((String) cbNickname.getSelectedItem()).trim();
		String ipurl = ipurlAndPort[0].trim();
		String port = ipurlAndPort[1].trim();

		if (nickname.length() == 0) {
			ShowErrorMessage("Bad Nickname", "Nickname must have at least one visible charater in it");
			return;
		}

		if (ipurl.length() == 0) {
			ShowErrorMessage("Bad IP/URL", "The IP/URL must have at least one visible charater in it");
			return;
		}

		if (port.length() == 0) {
			ShowErrorMessage("Bad Port", "Port must have at least one visible charater in it");
			return;
		}

		Matcher matcher = badNicknameChars.matcher(nickname);
		if (matcher.find()) {
			ShowErrorMessage("Bad Nickname", "Nickname cannot contain a comma, semi-colon, colon, or double-quote");
			return;
		}

		Integer portInt = 0;
		try {
			portInt = Integer.decode(port);
		} catch (NumberFormatException numberFormatException) {
			portInt = 0;
		}
		if (portInt < 1 || portInt > 65535) {
			ShowErrorMessage("Bad Port", "Port must be a positive integer between 1 and 65535");
			return;
		}

		// tell UserPreferences about the changes
		ArrayList<String> nicknames = Main.getUserPreferences().getStringArrayList(UserPreferences.NICKNAMES);
		nicknames.remove(nickname);
		nicknames.add(0, nickname);

		ArrayList<String> addressesAndPorts = Main.getUserPreferences().getStringArrayList(UserPreferences.ADDRESSES_AND_PORTS);
		String addressAndPort = ipurl + ":" + portInt;
		addressesAndPorts.remove(addressAndPort);
		addressesAndPorts.add(0, addressAndPort);

		// input accepted, so leave this dialog
		Main.getMain().setPlayModeInfo(nickname, ipurl, portInt);
		hideGameDialog();
	}

	private void buttonReviewPressed() {
		Main.getMain().setReviewModeInfo();
		hideGameDialog();
	}

	@Override
	public void DoAction(ActionEvent actionEvent) {
		Object object = actionEvent.getSource();
		if (object == buttonDeleteNickname) {
			processButtonDelete(Main.getUserPreferences().getStringArrayList(UserPreferences.NICKNAMES), cbNickname);
		} else if (object == buttonDeleteIPURLPort) {
			processButtonDelete(Main.getUserPreferences().getStringArrayList(UserPreferences.ADDRESSES_AND_PORTS), cbIPURLPort);
		} else if (object == buttonPlay) {
			buttonPlayPressed();
		} else if (object == buttonReview) {
			buttonReviewPressed();
		}
	}
}
