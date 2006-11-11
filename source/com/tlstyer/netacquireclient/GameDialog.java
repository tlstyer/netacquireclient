import java.awt.*;
import java.util.*;
import javax.swing.*;

public class GameDialog extends JDialog {
	private static final long serialVersionUID = -9013637360065026393L;
	
	protected JPanel panel = new JPanel();
	
	private static Set<GameDialog> setOfGameDialogs = new HashSet<GameDialog>();
	
	public GameDialog(boolean allowExternalHideRequest) {
		super(Main.getMainFrame());
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setContentPane(panel);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		if (allowExternalHideRequest) {
			synchronized (setOfGameDialogs) {
				setOfGameDialogs.add(this);
			}
		}
	}
	
	public static final int POSITION_0_0 = 1;
	public static final int POSITION_BELOW_SCORE_SHEET = 2;

	public void setLocation(int position) {
		if (position == POSITION_0_0) {
			JPanel panel = Main.getMainFrame().panel;
			Point location = panel.getLocationOnScreen();
			setLocation(location);
		} else if (position == POSITION_BELOW_SCORE_SHEET) {
			ScoreSheet scoreSheet = Main.getMainFrame().scoreSheet;
			Point location = scoreSheet.getLocationOnScreen();
			location.translate(0, Main.getMainFrame().getScoreSheetHeight());
			setLocation(location);
		}
	}
		
	public void showGameDialog(int position) {
		pack();
		setLocation(position);
		setVisible(true);
	}
	
	public void hideGameDialog() {
		synchronized (setOfGameDialogs) {
			setVisible(false);
			setOfGameDialogs.remove(this);
		}
	}
	
	public static void hideGameDialogs() {
		synchronized (setOfGameDialogs) {
			for (GameDialog gameDialog : setOfGameDialogs) {
				gameDialog.setVisible(false);
			}
			setOfGameDialogs.clear();
		}
	}
}
