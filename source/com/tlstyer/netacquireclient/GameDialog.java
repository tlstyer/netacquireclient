import javax.swing.*;

public class GameDialog extends JDialog {
	private static final long serialVersionUID = -9013637360065026393L;
	
	protected JPanel panel;
	
	public GameDialog() {
		super(Main.getMainFrame());
	}
	
	public void showGameDialog() {
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setContentPane(panel);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		pack();
		setVisible(true);
	}
}
