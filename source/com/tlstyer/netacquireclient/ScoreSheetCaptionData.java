public class ScoreSheetCaptionData {
	private Object[][] captions = new Object[10][10];
	private boolean dirty = true;
	
	public ScoreSheetCaptionData() {
		init();
	}
	
	public void init() {
    	for (int y=0; y<10; ++y) {
    		for (int x=0; x<10; ++x) {
    			captions[y][x] = null;
        	}
        }
    	dirty = true;
	}
	
	public Object getCaption(int y, int x) {
		return captions[y][x];
	}
	
	public void setCaption(int y, int x, Object caption) {
		captions[y][x] = caption;
		dirty = true;
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public void clean() {
		dirty = false;
	}
}
