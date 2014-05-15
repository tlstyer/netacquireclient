package com.tlstyer.netacquireclient;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TileRackButtons extends JPanel implements ActionListener, ComponentListener {

	private Button[] buttons = new Button[6];
	private int[] hoteltypes = new int[6];
	private GridLayout gridLayout = new GridLayout(1, 6, spacing, spacing);
	private Boolean canPlayTile = false;
	private final Boolean canPlayTileSynch = true;

	public static final int spacing = 10;

	public TileRackButtons() {
		setLayout(gridLayout);
		for (int buttonIndex = 0; buttonIndex < 6; ++buttonIndex) {
			buttons[buttonIndex] = new Button();
			buttons[buttonIndex].addActionListener(this);
			buttons[buttonIndex].setActionCommand(((Integer) buttonIndex).toString());
			add(buttons[buttonIndex]);
		}
		setBackground(MainFrame.getTileRackBackgroundColor());

		addComponentListener(this);
	}

	public void setButton(int buttonIndex, String label, int hoteltype) {
		buttons[buttonIndex].setLabel(label);
		buttons[buttonIndex].setBackground(Util.hoteltypeToColor(hoteltype));
		hoteltypes[buttonIndex] = hoteltype;
		buttons[buttonIndex].setVisible(true);
	}

	public void setButtonVisible(int buttonIndex, boolean visible) {
		buttons[buttonIndex].setVisible(visible);
	}

	public void setButtonsVisible(boolean visible) {
		for (int buttonIndex = 0; buttonIndex < 6; ++buttonIndex) {
			buttons[buttonIndex].setVisible(visible);
		}
	}

	public void setCanPlayTile(boolean canPlayTile_) {
		synchronized (canPlayTileSynch) {
			canPlayTile = canPlayTile_;
		}
	}

	public void actionPerformed(ActionEvent actionEvent) {
		int buttonIndex = Integer.decode(actionEvent.getActionCommand());
		synchronized (canPlayTileSynch) {
			boolean canPlayTileNow = canPlayTile;
			canPlayTileNow &= (hoteltypes[buttonIndex] != Hoteltype.CANT_PLAY_EVER);
			canPlayTileNow &= (hoteltypes[buttonIndex] != Hoteltype.CANT_PLAY_NOW);
			if (canPlayTileNow) {
				Main.getNetworkConnection().writeMessage("PT;" + (buttonIndex + 1));
				setButtonVisible(buttonIndex, false);
				canPlayTile = false;
			}
		}
	}

	public void updateFonts() {
		TextComponentFontData textComponentFontData = Main.getFontManager().getTextComponentFontData(4);
		if (textComponentFontData == null) {
			return;
		}

		Font font = textComponentFontData.getFont();
		for (int buttonIndex = 0; buttonIndex < 6; ++buttonIndex) {
			buttons[buttonIndex].setFont(font);
		}
	}

	public void componentHidden(ComponentEvent componentEvent) {
	}

	public void componentMoved(ComponentEvent componentEvent) {
	}

	public void componentResized(ComponentEvent componentEvent) {
		updateFonts();
	}

	public void componentShown(ComponentEvent componentEvent) {
	}
}
