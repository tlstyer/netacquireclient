package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public abstract class GameDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -9013637360065026393L;
	
	protected JPanel panel = new JPanel();
	
	private boolean hasBeenHidden = false;
	
	private static Set<GameDialog> setOfGameDialogs = new HashSet<GameDialog>();
	
	public static final int ALLOW_EXTERNAL_HIDE_REQUEST = 1;
	public static final int DO_NOT_ALLOW_EXTERNAL_HIDE_REQUEST = 2;
	
	public GameDialog(int allowExternalHideRequest) {
		super(Main.getMainFrame());
		panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setContentPane(panel);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
		if (allowExternalHideRequest == ALLOW_EXTERNAL_HIDE_REQUEST) {
			synchronized (setOfGameDialogs) {
				setOfGameDialogs.add(this);
			}
		}
	}
	
	public static final int POSITION_0_0 = 1;
	public static final int POSITION_BELOW_SCORE_SHEET = 2;
	public static final int POSITION_CENTER_IN_MAIN_FRAME_PANEL = 3;

	public void setLocation(int position) {
		if (position == POSITION_0_0) {
			JPanel panel = Main.getMainFrame().getPanel();
			Point location = panel.getLocationOnScreen();
			setLocation(location);
		} else if (position == POSITION_BELOW_SCORE_SHEET) {
			ScoreSheet scoreSheet = Main.getMainFrame().getScoreSheet();
			Point location = scoreSheet.getLocationOnScreen();
			location.translate(0, Main.getMainFrame().getScoreSheetHeight());
			setLocation(location);
		} else if (position == POSITION_CENTER_IN_MAIN_FRAME_PANEL) {
			JPanel mainFramePanel = Main.getMainFrame().getPanel();
			Point mainFrameLocation = mainFramePanel.getLocationOnScreen();
			Dimension mainFrameDimension = mainFramePanel.getSize();
			Dimension gameDialogDimension = this.getSize();
			Point gameDialogLocation = new Point();
			gameDialogLocation.x = mainFrameLocation.x + (mainFrameDimension.width - gameDialogDimension.width) / 2;
			gameDialogLocation.y = mainFrameLocation.y + (mainFrameDimension.height - gameDialogDimension.height) / 2;
			setLocation(gameDialogLocation);
		}
	}
		
	public void showGameDialog(int position) {
		pack();
		setLocation(position);
		setVisible(true);
	}
	
	public void hideGameDialog() {
		synchronized (setOfGameDialogs) {
			hasBeenHidden = true;
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

	public void actionPerformed(ActionEvent arg0) {
		if (!hasBeenHidden) {
			DoAction(arg0);
		}
	}
	
	public abstract void DoAction(ActionEvent arg0);
}
