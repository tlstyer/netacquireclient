package com.tlstyer.netacquire;

import java.awt.*;
import javax.swing.*;

public class TileRackTextComponents extends JPanel {
	private static final long serialVersionUID = 4472307616301298731L;
	
	private TextComponent[] textComponents = new TextComponent[6];
	private GridLayout gridLayout = new GridLayout(1, 6, TileRackButtons.spacing, TileRackButtons.spacing);
	private TileRackData tileRackData = new TileRackData();
	
	public TileRackTextComponents() {
		setLayout(gridLayout);
		for (int buttonIndex=0; buttonIndex<6; ++buttonIndex) {
			textComponents[buttonIndex] = new TextComponent();
			add(textComponents[buttonIndex]);
			textComponents[buttonIndex].setVisible(false);
		}
		setBackground(MainFrame.getTileRackBackgroundColor());
	}
	
	public void sync(TileRackData trd) {
		for (int index=0; index<6; ++index) {
			boolean repaint = false;

			String label = trd.getLabel(index);
			if (tileRackData.getLabel(index) != label) {
				tileRackData.setLabel(index, label);
				textComponents[index].setText(label);
				repaint = true;
			}
			
			int hoteltype = trd.getHoteltype(index);
			if (tileRackData.getHoteltype(index) != hoteltype) {
				tileRackData.setHoteltype(index, hoteltype);
				textComponents[index].setBackgroundColor(Util.hoteltypeToColor(hoteltype));
				repaint = true;
			}
			
			boolean visibility = trd.getVisibility(index);
			if (tileRackData.getVisibility(index) != visibility) {
				tileRackData.setVisibility(index, visibility);
				textComponents[index].setVisible(visibility);
				repaint = true;
			}
			
			if (repaint) {
				textComponents[index].repaint();
			}
		}
	}
}
