import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;
import javax.swing.*;

public class CommunicationsDialog extends GameDialog implements ActionListener {
	private static final long serialVersionUID = -9110080591988857670L;
	
	private JTextField tfNickname;
	private JTextField tfIPURLPort;
	private JButton buttonGo;

    private static final Pattern badNicknameChars = Pattern.compile(",|;|:|\"");

	public CommunicationsDialog() {
		setTitle("Communications");

		// "Nickname" panel
		JPanel panelNickname = new JPanel(new FlowLayout());
		JLabel labelNickname = new JLabel("Nickname:", JLabel.TRAILING);
		labelNickname.setDisplayedMnemonic(KeyEvent.VK_N);
		tfNickname = new JTextField(SerializedData.getSerializedData().getNickname(), 30);
		labelNickname.setLabelFor(tfNickname);
		panelNickname.add(labelNickname);
		panelNickname.add(tfNickname);

		// "IPURL:Port" panel
		JPanel panelIPURLPort = new JPanel(new FlowLayout());
		JLabel labelIPURLPort = new JLabel("IP/URL:Port:", JLabel.TRAILING);
		labelIPURLPort.setDisplayedMnemonic(KeyEvent.VK_I);
		tfIPURLPort = new JTextField("localhost:1001", 30);
		labelIPURLPort.setLabelFor(tfIPURLPort);
		panelIPURLPort.add(labelIPURLPort);
		panelIPURLPort.add(tfIPURLPort);

		// "Go" button
		buttonGo = new JButton("Go");
		buttonGo.setMnemonic(KeyEvent.VK_G);
		buttonGo.addActionListener(this);
		Dimension dimensionButtonGo = buttonGo.getPreferredSize();
		dimensionButtonGo.width *= 2;
		dimensionButtonGo.height *= 2;
		buttonGo.setPreferredSize(dimensionButtonGo);

		// put them all together
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(panelNickname);
		panel.add(panelIPURLPort);
		panel.add(buttonGo);

		getRootPane().setDefaultButton(buttonGo);
		
		showGameDialog(GameDialog.POSITION_0_0);
	}

	private void ShowErrorMessage(String title, String message) {
		JOptionPane.showMessageDialog(Main.getMainFrame(), message, title, JOptionPane.ERROR_MESSAGE);
		toFront();
	}

	public void actionPerformed(ActionEvent e) {
		String[] ipurlAndPort = tfIPURLPort.getText().split(":", -1);

		if (ipurlAndPort.length != 2) {
			ShowErrorMessage("Bad IP/URL:Port format", "IP/URL:Port format must be <IP/URL>:<Port>");
			return;
		}

		String nickname = tfNickname.getText().trim();
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
		
		Main.getMainFrame().setConnectionParams(nickname, ipurl, portInt);
		hideGameDialog();
	}
}
