import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;

public class CommunicationsDialog extends GameDialog implements ActionListener {
	private static final long serialVersionUID = -9110080591988857670L;
	
	private JComboBox cbNickname;
	private JButton buttonDeleteNickname;
	private JComboBox cbIPURLPort;
	private JButton buttonDeleteIPURLPort;
	private JButton buttonGo;

    private static final Pattern badNicknameChars = Pattern.compile(",|;|:|\"");

	public CommunicationsDialog() {
		setTitle("Communications");

		// "Nickname" panel
		JLabel labelNickname = new JLabel("Nickname:", JLabel.TRAILING);
		labelNickname.setDisplayedMnemonic(KeyEvent.VK_N);
		
		cbNickname = new JComboBox(SerializedData.getSerializedData().getNicknames());
		cbNickname.setEditable(true);
		labelNickname.setLabelFor(cbNickname);
		
		buttonDeleteNickname = new JButton("X");
		buttonDeleteNickname.addActionListener(this);
		
		JPanel panelNickname = new JPanel();
		panelNickname.setLayout(new BoxLayout(panelNickname, BoxLayout.X_AXIS));
		panelNickname.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panelNickname.add(Box.createHorizontalGlue());
		panelNickname.add(labelNickname);
		panelNickname.add(Box.createRigidArea(new Dimension(5, 0)));
		panelNickname.add(cbNickname);
		panelNickname.add(Box.createRigidArea(new Dimension(5, 0)));
		panelNickname.add(buttonDeleteNickname);

		// "IPURL:Port" panel
		JLabel labelIPURLPort = new JLabel("IP/URL:Port:", JLabel.TRAILING);
		labelIPURLPort.setDisplayedMnemonic(KeyEvent.VK_I);
		
		cbIPURLPort = new JComboBox(SerializedData.getSerializedData().getAddressesAndPorts());
		cbIPURLPort.setEditable(true);
		labelIPURLPort.setLabelFor(cbIPURLPort);
		
		buttonDeleteIPURLPort = new JButton("X");
		buttonDeleteIPURLPort.addActionListener(this);
		
		JPanel panelIPURLPort = new JPanel();
		panelIPURLPort.setLayout(new BoxLayout(panelIPURLPort, BoxLayout.X_AXIS));
		panelIPURLPort.setAlignmentX(Component.RIGHT_ALIGNMENT);
		panelIPURLPort.add(Box.createHorizontalGlue());
		panelIPURLPort.add(labelIPURLPort);
		panelIPURLPort.add(Box.createRigidArea(new Dimension(5, 0)));
		panelIPURLPort.add(cbIPURLPort);
		panelIPURLPort.add(Box.createRigidArea(new Dimension(5, 0)));
		panelIPURLPort.add(buttonDeleteIPURLPort);

		// "Go" button
		buttonGo = new JButton("Go");
		buttonGo.setMnemonic(KeyEvent.VK_G);
		buttonGo.addActionListener(this);
		Dimension dimensionButtonGo = buttonGo.getPreferredSize();
		dimensionButtonGo.height *= 2;
		buttonGo.setAlignmentX(Component.RIGHT_ALIGNMENT);
		buttonGo.setMinimumSize(dimensionButtonGo);
		buttonGo.setPreferredSize(dimensionButtonGo);
		buttonGo.setMaximumSize(dimensionButtonGo);
		
		// make the combo boxes the same width
		int width = labelIPURLPort.getPreferredSize().width * 3;
		Dimension dimension;
		
		dimension = cbNickname.getPreferredSize();
		dimension.width = width;
		cbNickname.setMinimumSize(dimension);
		cbNickname.setPreferredSize(dimension);
		cbNickname.setMaximumSize(dimension);
		
		dimension = cbIPURLPort.getPreferredSize();
		dimension.width = width;
		cbIPURLPort.setMinimumSize(dimension);
		cbIPURLPort.setPreferredSize(dimension);
		cbIPURLPort.setMaximumSize(dimension);
		
		// put them all together
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panelNickname);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(panelIPURLPort);
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(buttonGo);

		getRootPane().setDefaultButton(buttonGo);
		
		showGameDialog(GameDialog.POSITION_0_0);
	}

	private void ShowErrorMessage(String title, String message) {
		JOptionPane.showMessageDialog(Main.getMainFrame(), message, title, JOptionPane.ERROR_MESSAGE);
		toFront();
	}
	
	private void processButtonDelete(Vector<String> strings, JComboBox comboBox) {
		String nickname = ((String)comboBox.getSelectedItem());
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

	private void buttonGoPressed() {
		// figure out input and check for bad input
		String[] ipurlAndPort = ((String)cbIPURLPort.getSelectedItem()).split(":", -1);

		if (ipurlAndPort.length != 2) {
			ShowErrorMessage("Bad IP/URL:Port format", "IP/URL:Port format must be <IP/URL>:<Port>");
			return;
		}

		String nickname = ((String)cbNickname.getSelectedItem()).trim();
		String ipurl = ipurlAndPort[0].trim();
		String port = ipurlAndPort[1].trim();

		if (nickname.equals("")) {
			ShowErrorMessage("Bad Nickname", "Nickname must have at least one visible charater in it");
			return;
		}

		if (ipurl.equals("")) {
			ShowErrorMessage("Bad IP/URL", "The IP/URL must have at least one visible charater in it");
			return;
		}

		if (port.equals("")) {
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
		} catch (NumberFormatException nfe) {
			portInt = 0;
		}
		if (portInt < 1 || portInt > 65535) {
			ShowErrorMessage("Bad Port", "Port must be a positive integer between 1 and 65535");
			return;
		}
		
		// tell SerializedData about the changes
		Vector<String> nicknames = SerializedData.getSerializedData().getNicknames();
		nicknames.remove(nickname);
		nicknames.add(0, nickname);
		
		Vector<String> addressesAndPorts = SerializedData.getSerializedData().getAddressesAndPorts();
		String addressAndPort = ipurl + ":" + portInt;
		addressesAndPorts.remove(addressAndPort);
		addressesAndPorts.add(0, addressAndPort);
		
		// input accepted, so leave this dialog
		Main.getMainFrame().setConnectionParams(nickname, ipurl, portInt);
		hideGameDialog();
	}

	public void actionPerformed(ActionEvent e) {
		Object object = e.getSource();
		if (object == buttonDeleteNickname) {
			processButtonDelete(SerializedData.getSerializedData().getNicknames(), cbNickname);
		} else if (object == buttonDeleteIPURLPort) {
			processButtonDelete(SerializedData.getSerializedData().getAddressesAndPorts(), cbIPURLPort);
		} else if (object == buttonGo) {
			buttonGoPressed();
		}
	}
}
