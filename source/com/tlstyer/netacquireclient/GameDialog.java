import javax.swing.*;

public class GameDialog extends JDialog {
	protected JPanel panel;
	
	public GameDialog() {
		super(Main.getMainFrame());
	}
	
	public void showGameDialog() {
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setContentPane(panel);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setAlwaysOnTop(true);
		pack();
		setVisible(true);
	}
}
