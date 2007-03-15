package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SelectChainDialog extends GameDialog {
	private static final long serialVersionUID = -480008475260474107L;

	private ButtonGroup radioButtonGroup;
	private JRadioButton[] radioButtons;
	private JButton buttonOK;
	private int type;

	public SelectChainDialog(int type_, boolean[] hotelOptions) {
		super(ALLOW_EXTERNAL_HIDE_REQUEST);

		type = type_;

		String title;
		switch (type) {
		case 4:
			title = "Hotel Chain Selection";
			break;
		case 6:
			title = "Select Merger Survivor";
			break;
		case 8:
			title = "Select Chain To Dispose Of";
			break;
		default:
			title = "Unknown Title";
			break;
		}
		setTitle(title);

		// radio buttons panel
		JPanel panelRadioButtons = new JPanel();
		panelRadioButtons.setLayout(new BoxLayout(panelRadioButtons, BoxLayout.Y_AXIS));
		radioButtonGroup = new ButtonGroup();
		radioButtons = new JRadioButton[7];

		for (int index=0; index<7; ++index) {
			String name = Util.hoteltypeToName(index + 1);
			JRadioButton radioButton = new JRadioButton(name);
			radioButtons[index] = radioButton;
			radioButtonGroup.add(radioButton);
			radioButton.setMnemonic(Util.hoteltypeToMnemonic(index + 1));
			radioButton.setFont(Main.getFontManager().getBoldDialogFont());
			radioButton.setForeground(Util.hoteltypeToColor(index + 1));
			Dimension size = radioButton.getPreferredSize();
			size.height -= 7;
			radioButton.setPreferredSize(size);
			if (!hotelOptions[index]) {
				radioButton.setEnabled(false);
			}
			panelRadioButtons.add(radioButton);
			if (index == 1 || index == 4) {
				panelRadioButtons.add(Box.createRigidArea(new Dimension(0,10)));
			}
		}
		panelRadioButtons.add(Box.createRigidArea(new Dimension(0,10)));

		for (int index=0; index<7; ++index) {
			if (hotelOptions[index]) {
				radioButtons[index].setSelected(true);
				break;
			}
		}

		// "OK" button
		buttonOK = Util.getButton3d2("OK", KeyEvent.VK_O);
		buttonOK.addActionListener(this);

		// put them all together
		panelRadioButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonOK.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panelRadioButtons);
		panel.add(buttonOK);

		getRootPane().setDefaultButton(buttonOK);

		showGameDialog(POSITION_BELOW_SCORE_SHEET);
	}

	public void DoAction(ActionEvent actionEvent) {
		for (int index=0; index<7; ++index) {
			if (radioButtons[index].isSelected()) {
				int colorvalue = Util.hoteltypeToColorvalueNetwork(index + 1);
				Main.getNetworkConnection().writeMessage("CS;" + colorvalue + "," + type);
				hideGameDialog();
				return;
			}
		}
	}
}
