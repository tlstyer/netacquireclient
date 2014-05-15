package com.tlstyer.netacquireclient;

public final class GameBoardData {

	private int hoteltypes[][] = new int[9][12];
	private boolean dirty = true;

	public GameBoardData() {
		init();
	}

	public void init() {
		for (int y = 0; y < 9; ++y) {
			for (int x = 0; x < 12; ++x) {
				hoteltypes[y][x] = Hoteltype.NONE;
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
