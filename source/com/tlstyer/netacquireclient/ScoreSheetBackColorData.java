public class ScoreSheetBackColorData {
	private int[][] backgroundColors = new int[10][10];
	private boolean dirty = true;
	
	private static final int colorvaluePlayer = Util.hoteltypeToColorvalueSwing(Hoteltype.NOT_MY_TURN);
	private static final int colorvalueStats = Util.hoteltypeToColorvalueSwing(Hoteltype.HCS);

	public ScoreSheetBackColorData() {
		init();
	}
	
	public void init() {
    	for (int y=0; y<10; ++y) {
    		for (int x=0; x<10; ++x) {
    			backgroundColors[y][x] = (y<7 ? colorvaluePlayer : colorvalueStats);
        	}
        }
    	dirty = true;
	}
	
	public int getBackColor(int y, int x) {
		return backgroundColors[y][x];
	}
	
	public void setBackColor(int y, int x, int backgroundColor) {
		backgroundColors[y][x] = backgroundColor;
		dirty = true;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public void clean() {
		dirty = false;
	}
}
