package com.tlstyer.netacquireclient;

import java.net.*;
import java.util.*;
import java.util.prefs.*;

abstract class UserPreference {
	protected static final Preferences preferences = Preferences.userNodeForPackage(UserPreferences.class);

	abstract public void load();

	abstract public void save();

	protected void putString(String key, String value) {
		if (value.length() > Preferences.MAX_VALUE_LENGTH) {
			value = value.substring(0, Preferences.MAX_VALUE_LENGTH);
		}
		preferences.put(key, value);
	}

	public ArrayList<String> getStringArrayList() {
		return null;
	}

	public void setStringArrayList(ArrayList<String> stringArrayList_) {
	}

	public String getString() {
		return null;
	}

	public void setString(String string_) {
	}

	public Integer getInteger() {
		return null;
	}

	public void setInteger(Integer integer_) {
	}

	public Boolean getBoolean() {
		return null;
	}

	public void setBoolean(Boolean boolean_) {
	}
}

abstract class UserPreferenceTypeStringArrayList extends UserPreference {
	protected ArrayList<String> value = null;

	private String key;

	private static final String stringArraySeparator = "::";

	public UserPreferenceTypeStringArrayList(String key) {
		this.key = key;
	}

	@Override
	public void load() {
		String[] itemsArray = preferences.get(key, "").split(stringArraySeparator, -1);
		value = new ArrayList<String>();
		if (itemsArray.length == 1 && itemsArray[0].length() == 0) {
			populateEmptyArrayList();
		} else {
			value.addAll(Arrays.asList(itemsArray));
		}
	}

	abstract protected void populateEmptyArrayList();

	@Override
	public void save() {
		putString(key, Util.join(value.toArray(), stringArraySeparator));
	}

	@Override
	public ArrayList<String> getStringArrayList() {
		return value;
	}

	@Override
	public void setStringArrayList(ArrayList<String> stringArrayList_) {
		value = stringArrayList_;
	}
}

class UserPreferenceTypeString extends UserPreference {
	private String value = null;

	private String key;
	private String valueDefault;

	public UserPreferenceTypeString(String key, String valueDefault) {
		this.key = key;
		this.valueDefault = valueDefault;
	}

	@Override
	public void load() {
		value = preferences.get(key, valueDefault);
	}

	@Override
	public void save() {
		putString(key, value);
	}

	@Override
	public String getString() {
		return value;
	}

	@Override
	public void setString(String string_) {
		value = string_;
	}
}

class UserPreferenceTypeInteger extends UserPreference {
	private Integer value = null;

	private String key;
	private int valueDefault;
	private int valueMin;
	private int valueMax;

	public UserPreferenceTypeInteger(String key, int valueDefault, int valueMin, int valueMax) {
		this.key = key;
		this.valueDefault = valueDefault;
		this.valueMin = valueMin;
		this.valueMax = valueMax;
	}

	@Override
	public void load() {
		value = preferences.getInt(key, valueDefault);
		if (value < valueMin || value > valueMax) {
			value = valueDefault;
		}
	}

	@Override
	public void save() {
		preferences.putInt(key, value);
	}

	@Override
	public Integer getInteger() {
		return value;
	}

	@Override
	public void setInteger(Integer integer_) {
		value = integer_;
	}
}

class UserPreferenceTypeBoolean extends UserPreference {
	private Boolean value = null;

	private String key;
	private boolean valueDefault;

	public UserPreferenceTypeBoolean(String key, boolean valueDefault) {
		this.key = key;
		this.valueDefault = valueDefault;
	}

	@Override
	public void load() {
		value = preferences.getBoolean(key, valueDefault);
	}

	@Override
	public void save() {
		preferences.putBoolean(key, value);
	}

	@Override
	public Boolean getBoolean() {
		return value;
	}

	@Override
	public void setBoolean(Boolean boolean_) {
		value = boolean_;
	}
}

class UserPreferenceNicknames extends UserPreferenceTypeStringArrayList {
	public UserPreferenceNicknames() {
		super("nicknames");
	}

	@Override
	protected void populateEmptyArrayList() {
		String nickname;
		try {
			nickname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException unknownHostException) {
			Random random = new Random();
			nickname = "" + random.nextInt();
		}
		value.add(nickname);
	}
}

class UserPreferenceAddressesAndPorts extends UserPreferenceTypeStringArrayList {
	public UserPreferenceAddressesAndPorts() {
		super("addresses and ports");
	}

	@Override
	protected void populateEmptyArrayList() {
		value.add("server.netacquire.ca:1001");
		value.add("localhost:1001");
	}
}

public final class UserPreferences {
	private UserPreference[] userPreferenceArray = new UserPreference[COUNT];

	public static final int NICKNAMES                       =  0;
	public static final int ADDRESSES_AND_PORTS             =  1;
	public static final int MAX_PLAYER_COUNT                =  2;
	public static final int USER_LIST_SORTING_METHOD        =  3;
	public static final int PLAY_SOUND_WHEN_WAITING_FOR_ME  =  4;
	public static final int PATH_TO_SOUND                   =  5;
	public static final int WRITE_TO_LOG_FILES              =  6;
	public static final int PATH_TO_LOG_FILES               =  7;
	public static final int WHERE_TO_START_IN_REVIEW_MODE   =  8;
	public static final int SHOW_MODAL_MESSAGE_DIALOG_BOXES =  9;
	public static final int GAME_BOARD_LABEL_MODE           = 10;
	public static final int SHOW_MESSAGE_PREFIXES           = 11;
	public static final int COUNT                           = 12;

	public UserPreferences() {
		userPreferenceArray[NICKNAMES                      ] = new UserPreferenceNicknames();
		userPreferenceArray[ADDRESSES_AND_PORTS            ] = new UserPreferenceAddressesAndPorts();
		userPreferenceArray[MAX_PLAYER_COUNT               ] = new UserPreferenceTypeInteger("max player count", 4, 2, 6);
		userPreferenceArray[USER_LIST_SORTING_METHOD       ] = new UserPreferenceTypeInteger("user list sorting method", UserListPresenter.SORT_NONE, 0, UserListPresenter.SORT_END);
		userPreferenceArray[PLAY_SOUND_WHEN_WAITING_FOR_ME ] = new UserPreferenceTypeBoolean("play sound when waiting for me", false);
		userPreferenceArray[PATH_TO_SOUND                  ] = new UserPreferenceTypeString("path to sound", "");
		userPreferenceArray[WRITE_TO_LOG_FILES             ] = new UserPreferenceTypeBoolean("write to log files", false);
		userPreferenceArray[PATH_TO_LOG_FILES              ] = new UserPreferenceTypeString("path to log files", "");
		userPreferenceArray[WHERE_TO_START_IN_REVIEW_MODE  ] = new UserPreferenceTypeInteger("where to start in review mode", Review.START_AT_BEGINNING_OF_GAME, 0, Review.START_END);
		userPreferenceArray[SHOW_MODAL_MESSAGE_DIALOG_BOXES] = new UserPreferenceTypeBoolean("show modal message dialog boxes", false);
		userPreferenceArray[GAME_BOARD_LABEL_MODE          ] = new UserPreferenceTypeInteger("game board label mode", GameBoard.LABEL_COORDINATES, 0, GameBoard.LABEL_END);
		userPreferenceArray[SHOW_MESSAGE_PREFIXES          ] = new UserPreferenceTypeBoolean("show message prefixes", true);

		load();
	}

	public void load() {
		for (int index=0; index<COUNT; ++index) {
			userPreferenceArray[index].load();
		}
	}

	public void save() {
		for (int index=0; index<COUNT; ++index) {
			userPreferenceArray[index].save();
		}
	}

	public ArrayList<String> getStringArrayList(int index) {
		return userPreferenceArray[index].getStringArrayList();
	}

	public void setStringArrayList(int index, ArrayList<String> stringArrayList_) {
		userPreferenceArray[index].setStringArrayList(stringArrayList_);
	}

	public String getString(int index) {
		return userPreferenceArray[index].getString();
	}

	public void setString(int index, String string_) {
		userPreferenceArray[index].setString(string_);
	}

	public Integer getInteger(int index) {
		return userPreferenceArray[index].getInteger();
	}

	public void setInteger(int index, Integer integer_) {
		userPreferenceArray[index].setInteger(integer_);
	}

	public Boolean getBoolean(int index) {
		return userPreferenceArray[index].getBoolean();
	}

	public void setBoolean(int index, Boolean boolean_) {
		userPreferenceArray[index].setBoolean(boolean_);
	}
}
