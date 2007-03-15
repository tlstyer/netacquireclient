package com.tlstyer.netacquireclient;

public class TileRackData {
	private String[] labels = new String[6];
	private int[] hoteltypes = new int[6];
	private boolean[] visibilities = new boolean[6];
	private boolean dirty = true;

	public TileRackData() {
		init();
	}

	public void init() {
		for (int index=0; index<6; ++index) {
			labels[index] = " ";
			hoteltypes[index] = Hoteltype.NONE;
			visibilities[index] = false;
		}
		dirty = true;
	}

	public String getLabel(int index) {
		return labels[index];
	}

	public void setLabel(int index, String label) {
		labels[index] = label;
		dirty = true;
	}

	public int getHoteltype(int index) {
		return hoteltypes[index];
	}

	public void setHoteltype(int index, int hoteltype) {
		hoteltypes[index] = hoteltype;
		dirty = true;
	}

	public boolean getVisibility(int index) {
		return visibilities[index];
	}

	public void setVisibility(int index, boolean visibility) {
		visibilities[index] = visibility;
		dirty = true;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void clean() {
		dirty = false;
	}
}
