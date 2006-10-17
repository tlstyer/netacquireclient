import java.awt.*;
import javax.swing.*;

public class GameBoard {
	private TextComponent[][] board;
	private GridLayout gl;
	private JPanel panel;
	
	private Color color_def = new Color(HoteltypeToColorvalue.lookupSwing(BoardtypeEnum.BOARDTYPE_NONE.ordinal()));
	private String letters = "ABCDEFGHI";
    
    public GameBoard() {
    	board = new TextComponent[9][12];
        gl = new GridLayout(9, 12, 2, 2);
        panel = new JPanel(gl);
    	for (int y=0; y<9; ++y) {
    		for (int x=0; x<12; ++x) {
    			board[y][x] = new TextComponent();
    			board[y][x].setBackgroundColor(color_def);
    			board[y][x].setText(Integer.toString(x + 1) + letters.charAt(y));
    			board[y][x].setTextAlign(TextComponent.ALIGN_CENTER);
        		panel.add(board[y][x]);
        	}
        }
    }
    
    public JPanel getPanel() {
    	return panel;
    }
}
