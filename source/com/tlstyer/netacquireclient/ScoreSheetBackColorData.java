import java.awt.Color;

public class ScoreSheetBackColorData {
	private int[][] backcolors = new int[10][9];
	private boolean dirty = true;
	
	private static final int colorvaluePlayer = HoteltypeToColorvalue.lookupSwing(BoardtypeEnum.CSSCLASS_NOT_MY_TURN.ordinal());
	private static final int colorvalueStats = HoteltypeToColorvalue.lookupSwing(BoardtypeEnum.CSSCLASS_HCS.ordinal());

	public ScoreSheetBackColorData() {
		init();
	}
	
	public void init() {
    	for (int y=0; y<10; ++y) {
    		for (int x=0; x<9; ++x) {
    			backcolors[y][x] = (y<7 ? colorvaluePlayer : colorvalueStats);
        	}
        }
    	dirty = true;
	}
	
	public int getBackColor(int y, int x) {
		return backcolors[y][x];
	}
	
	public void setBackColor(int y, int x, int backcolor) {
		backcolors[y][x] = backcolor;
		dirty = true;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public void clean() {
		dirty = false;
	}
}
