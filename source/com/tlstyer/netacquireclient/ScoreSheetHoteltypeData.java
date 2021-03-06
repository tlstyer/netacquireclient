package com.tlstyer.netacquireclient;

public final class ScoreSheetHoteltypeData {

	private final int[][] hoteltypes = new int[10][10];
	private boolean dirty = true;

	public ScoreSheetHoteltypeData() {
		init();
	}

	public void init() {
		for (int y = 0; y < 10; ++y) {
			for (int x = 0; x < 10; ++x) {
				hoteltypes[y][x] = (y < 7 ? Hoteltype.NOT_MY_TURN : Hoteltype.HCS);
			}
		}
		dirty = true;
	}

	public int getHoteltype(int x, int y) {
		return hoteltypes[y][x];
	}

	public void setHoteltype(int x, int y, int hoteltype) {
		hoteltypes[y][x] = hoteltype;
		dirty = true;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void clean() {
		dirty = false;
	}
}
