import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;
import javax.swing.*;

public class CommunicationsDialog extends GameDialog implements ActionListener {
	private static final long serialVersionUID = -9110080591988857670L;
	
	private JTextField tfNickname;
	private JTextField tfIPURL;
	private JTextField tfPort;
	private JButton buttonGo;

    private static final Pattern badNicknameChars = Pattern.compile(",|;|:|\"");

	public CommunicationsDialog() {
		setTitle("Communications");

		// "Nickname" panel
		JPanel panelNickname = new JPanel(new FlowLayout());
		JLabel labelNickname = new JLabel("Nickname:", JLabel.TRAILING);
		labelNickname.setDisplayedMnemonic(KeyEvent.VK_N);
		tfNickname = new JTextField("tlstyer", 30);
		labelNickname.setLabelFor(tfNickname);
		panelNickname.add(labelNickname);
		panelNickname.add(tfNickname);

		// "IPURL" panel
		JPanel panelIPURL = new JPanel(new FlowLayout());
		JLabel labelIPURL = new JLabel("IP/URL:", JLabel.TRAILING);
		labelIPURL.setDisplayedMnemonic(KeyEvent.VK_I);
		tfIPURL = new JTextField("localhost", 30);
		labelIPURL.setLabelFor(tfIPURL);
		panelIPURL.add(labelIPURL);
		panelIPURL.add(tfIPURL);

		// "Port" panel
		JPanel panelPort = new JPanel(new FlowLayout());
		JLabel labelPort = new JLabel("Port:", JLabel.TRAILING);
		labelPort.setDisplayedMnemonic(KeyEvent.VK_P);
		tfPort = new JTextField("1001", 6);
		labelPort.setLabelFor(tfPort);
		panelPort.add(labelPort);
		panelPort.add(tfPort);

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
		panel.add(panelIPURL);
		panel.add(panelPort);
		panel.add(buttonGo);

		getRootPane().setDefaultButton(buttonGo);
		
		showGameDialog(GameDialog.POSITION_0_0);
	}

	public void actionPerformed(ActionEvent e) {
		String nickname = tfNickname.getText().trim();
		String ipurl = tfIPURL.getText().trim();
		String port = tfPort.getText().trim();

		if (nickname.equals("")) {
			JOptionPane.showMessageDialog(Main.getMainFrame(),
										  "Nickname must have at least one visible charater in it.",
										  "Bad nickname",
										  JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (ipurl.equals("")) {
			JOptionPane.showMessageDialog(Main.getMainFrame(),
										  "The IP/URL must have at least one visible charater in it.",
										  "Bad IP/URL",
										  JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (port.equals("")) {
			JOptionPane.showMessageDialog(Main.getMainFrame(),
										  "Port must have at least one visible charater in it.",
										  "Bad port",
										  JOptionPane.ERROR_MESSAGE);
			return;
		}

		Matcher matcher = badNicknameChars.matcher(nickname);
		if (matcher.find()) {
			JOptionPane.showMessageDialog(Main.getMainFrame(),
										  "Nickname cannot contain a comma, semi-colon, colon, or double-quote.",
										  "Bad nickname",
										  JOptionPane.ERROR_MESSAGE);
			return;
		}

        Integer portInt = 0;
        try {
			portInt = Integer.decode(port);
		} catch (NumberFormatException nfe) {
			portInt = 0;
		}
		if (portInt < 1 || portInt > 65535) {
			JOptionPane.showMessageDialog(Main.getMainFrame(),
										  "Port must be a positive integer between 1 and 65535.",
										  "Bad port",
										  JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Main.getMainFrame().setConnectionParams(nickname, ipurl, portInt);
		GameDialog.hideGameDialog();
	}
}
