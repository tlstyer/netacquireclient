public class GameBoardData {
	private int hotelTypes[][] = new int[9][12];
	private boolean dirty = true;
	
	public GameBoardData() {
		init();
	}
	
	public void init() {
    	for (int y=0; y<9; ++y) {
    		for (int x=0; x<12; ++x) {
    			hotelTypes[y][x] = BoardtypeEnum.BOARDTYPE_NONE.ordinal();
        	}
        }
    	dirty = true;
	}
	
	public int getHoteltype(int y, int x) {
		return hotelTypes[y][x];
	}
	
	public void setHoteltype(int y, int x, int hoteltype) {
		hotelTypes[y][x] = hoteltype;
		dirty = true;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public void clean() {
		dirty = false;
	}
}
