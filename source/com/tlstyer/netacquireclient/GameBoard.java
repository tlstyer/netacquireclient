package com.tlstyer.netacquireclient;

import java.awt.*;
import javax.swing.*;

public class GameBoard extends JPanel {
	private static final long serialVersionUID = -520919107548745846L;
	
	private TextComponent[][] board = new TextComponent[9][12];
	private GridLayout gridLayout = new GridLayout(9, 12, 2, 2);
	private GameBoardData gameBoardData = new GameBoardData();
	
	private static final Color color_def = Util.hoteltypeToColor(Hoteltype.NONE);
    
    public GameBoard() {
        setLayout(gridLayout);
    	for (int y=0; y<9; ++y) {
    		for (int x=0; x<12; ++x) {
    			board[y][x] = new TextComponent();
    			board[y][x].setBackgroundColor(color_def);
    			board[y][x].setText(Util.pointToNumberAndLetter(x, y));
    			board[y][x].setTextAlign(TextComponent.ALIGN_CENTER);
        		add(board[y][x]);
        	}
        }
    }
    
    public static final int LABEL_COORDINATES = 0;
    public static final int LABEL_BLANK = 1;
    public static final int LABEL_HOTELTYPE = 2;
    
    private String getTextForTC(int x, int y) {
    	int defaultType = LABEL_HOTELTYPE;
    	
    	int hoteltype = gameBoardData.getHoteltype(x, y);
    	
    	int labelType = LABEL_COORDINATES;
    	if (Hoteltype.LUXOR <= hoteltype && hoteltype <= Hoteltype.IMPERIAL) {
    		labelType = defaultType;
    	}
    	
    	switch (labelType) {
    		case LABEL_COORDINATES:
    			return Util.pointToNumberAndLetter(x, y);
	    	case LABEL_BLANK:
	    		return " ";
	    	case LABEL_HOTELTYPE:
	    		return Util.hoteltypeToInitial(hoteltype);
    	}
    	return " ";
    }
    
    public void sync(GameBoardData gbd) {
    	for (int y=0; y<9; ++y) {
    		for (int x=0; x<12; ++x) {
    			int hoteltype = gbd.getHoteltype(x, y);
    			if (gameBoardData.getHoteltype(x, y) != hoteltype) {
    				gameBoardData.setHoteltype(x, y, hoteltype);
    				board[y][x].setBackgroundColor(Util.hoteltypeToColor(hoteltype));
    				board[y][x].setText(getTextForTC(x, y));
    				board[y][x].repaint();
    			}
        	}
        }
    }
}
