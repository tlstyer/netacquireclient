import java.awt.*;
import javax.swing.*;

public class GameBoard extends JPanel {
	private TextComponent[][] board = new TextComponent[9][12];
	private GridLayout gridLayout = new GridLayout(9, 12, 2, 2);
	private GameBoardData gameBoardData = new GameBoardData();
	
	private static final Color color_def = new Color(HoteltypeToColorvalue.lookupSwing(BoardtypeEnum.BOARDTYPE_NONE.ordinal()));
    
    public GameBoard() {
        setLayout(gridLayout);
    	for (int y=0; y<9; ++y) {
    		for (int x=0; x<12; ++x) {
    			board[y][x] = new TextComponent();
        		add(board[y][x]);
        	}
        }
    	init();
    }
    
    public void init() {
    	for (int y=0; y<9; ++y) {
    		for (int x=0; x<12; ++x) {
    			board[y][x].setBackgroundColor(color_def);
    			board[y][x].setText(Util.coordsToNumberAndLetter(y, x));
    			board[y][x].setTextAlign(TextComponent.ALIGN_CENTER);
        	}
        }
    	gameBoardData.init();
    }
    
    public void sync(GameBoardData gbd) {
    	for (int y=0; y<9; ++y) {
    		for (int x=0; x<12; ++x) {
    			int hoteltype = gbd.getHoteltype(y, x);
    			if (gameBoardData.getHoteltype(y, x) != hoteltype) {
    				gameBoardData.setHoteltype(y, x, hoteltype);
    				board[y][x].setBackgroundColor(new Color(HoteltypeToColorvalue.lookupSwing(hoteltype)));
    				board[y][x].repaint();
    			}
        	}
        }
    }
}
