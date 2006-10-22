public class ScoreSheetBackColorData {
	private int[][] backcolors = new int[10][9];
	private boolean dirty = true;
	
	public ScoreSheetBackColorData() {
		init();
	}
	
	public void init() {
    	for (int y=0; y<10; ++y) {
    		for (int x=0; x<9; ++x) {
    			backcolors[y][x] = 0;
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
