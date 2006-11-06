import java.awt.*;
import javax.swing.*;

public class GameDialog extends JDialog {
	private static final long serialVersionUID = -9013637360065026393L;
	
	protected JPanel panel = new JPanel();
	
	public GameDialog() {
		super(Main.getMainFrame());
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setContentPane(panel);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
	}
	
	public static final int POSITION_0_0 = 1;
	public static final int POSITION_BELOW_SCORE_SHEET = 2;
	
	public void showGameDialog(int position) {
		pack();
		if (position == POSITION_0_0) {
			JPanel panel = Main.getMainFrame().panel;
			Point location = panel.getLocationOnScreen();
			setLocation(location);
		} else if (position == POSITION_BELOW_SCORE_SHEET) {
			ScoreSheet scoreSheet = Main.getMainFrame().scoreSheet;
			Point location = scoreSheet.getLocationOnScreen();
			location.translate(0, scoreSheet.getHeightOfAllRows());
			setLocation(location);
		}
		setVisible(true);
	}
}
