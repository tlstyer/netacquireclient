package com.tlstyer.netacquireclient;

import java.net.*;
import java.util.*;
import java.util.prefs.*;

class UserPreference {
	protected static final Preferences preferences = Preferences.userNodeForPackage(UserPreferences.class);
	protected static final String stringArraySeparator = "::";

	public void load() {
	}

	public void save() {
	}

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

class UserPreferenceNicknames extends UserPreference {
	private static final String key = "nicknames";

	private ArrayList<String> value = null;

	public void load() {
		String[] nicknamesArray = preferences.get(key, "").split(stringArraySeparator, -1);
		value = new ArrayList<String>();
		if (nicknamesArray.length == 1 && nicknamesArray[0].length() == 0) {
			String nickname;
			try {
				nickname = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException unknownHostException) {
				Random random = new Random();
				nickname = "" + random.nextInt();
			}
			value.add(nickname);
		} else {
			for (String nickname : nicknamesArray) {
				value.add(nickname);
			}
		}
	}

	public void save() {
		putString(key, Util.join(value.toArray(), stringArraySeparator));
	}

	public ArrayList<String> getStringArrayList() {
		return value;
	}

	public void setStringArrayList(ArrayList<String> stringArrayList_) {
		value = stringArrayList_;
	}
}

class UserPreferenceAddressesAndPorts extends UserPreference {
	private static final String key = "addresses and ports";

	private ArrayList<String> value = null;

	public void load() {
		String[] addressesAndPortsArray = preferences.get(key, "").split(stringArraySeparator, -1);
		value = new ArrayList<String>();
		if (addressesAndPortsArray.length == 1 && addressesAndPortsArray[0].length() == 0) {
			value.add("acquire.sbg.org:1001");
			value.add("acquire.sbg.org:1002");
			value.add("acquire.dynu.com:1001");
			value.add("localhost:1001");
		} else {
			for (String addressAndPort : addressesAndPortsArray) {
				value.add(addressAndPort);
			}
		}
	}

	public void save() {
		putString(key, Util.join(value.toArray(), stringArraySeparator));
	}

	public ArrayList<String> getStringArrayList() {
		return value;
	}

	public void setStringArrayList(ArrayList<String> stringArrayList_) {
		value = stringArrayList_;
	}
}

class UserPreferenceMaxPlayerCount extends UserPreference {
	private static final String key = "max player count";

	private Integer value = null;
	private static final Integer valueDefault = 4;

	public void load() {
		value = preferences.getInt(key, valueDefault);
		if (value < 2 || value > 6) {
			value = valueDefault;
		}
	}

	public void save() {
		preferences.putInt(key, value);
	}

	public Integer getInteger() {
		return value;
	}

	public void setInteger(Integer integer_) {
		value = integer_;
	}
}

class UserPreferenceUserListSortingMethod extends UserPreference {
	private static final String key = "user list sorting method";

	private Integer value = null;
	private static final Integer valueDefault = UserListPresenter.SORT_NONE;

	public void load() {
		value = preferences.getInt(key, valueDefault);
		if (value < 0 || value > UserListPresenter.SORT_END) {
			value = valueDefault;
		}
	}

	public void save() {
		preferences.putInt(key, value);
	}

	public Integer getInteger() {
		return value;
	}

	public void setInteger(Integer integer_) {
		value = integer_;
	}
}

class UserPreferencePlaySoundWhenWaitingForMe extends UserPreference {
	private static final String key = "play sound when waiting for me";

	private Boolean value = null;
	private static final Boolean valueDefault = false;

	public void load() {
		value = preferences.getBoolean(key, valueDefault);
	}

	public void save() {
		preferences.putBoolean(key, value);
	}

	public Boolean getBoolean() {
		return value;
	}

	public void setBoolean(Boolean boolean_) {
		value = boolean_;
	}
}

class UserPreferencePathToSound extends UserPreference {
	private static final String key = "path to sound";

	private String value = null;
	private static final String valueDefault = "";

	public void load() {
		value = preferences.get(key, valueDefault);
	}

	public void save() {
		putString(key, value);
	}

	public String getString() {
		return value;
	}

	public void setString(String string_) {
		value = string_;
	}
}

class UserPreferenceWriteToLogFiles extends UserPreference {
	private static final String key = "write to log files";

	private Boolean value = null;
	private static final Boolean valueDefault = false;

	public void load() {
		value = preferences.getBoolean(key, valueDefault);
	}

	public void save() {
		preferences.putBoolean(key, value);
	}

	public Boolean getBoolean() {
		return value;
	}

	public void setBoolean(Boolean boolean_) {
		value = boolean_;
	}
}

class UserPreferencePathToLogFiles extends UserPreference {
	private static final String key = "path to log files";

	private String value = null;
	private static final String valueDefault = "";

	public void load() {
		value = preferences.get(key, valueDefault);
	}

	public void save() {
		putString(key, value);
	}

	public String getString() {
		return value;
	}

	public void setString(String string_) {
		value = string_;
	}
}

class UserPreferenceWhereToStartInReviewMode extends UserPreference {
	private static final String key = "where to start in review mode";

	private Integer value = null;
	private static final Integer valueDefault = Review.START_AT_BEGINNING_OF_GAME;

	public void load() {
		value = preferences.getInt(key, valueDefault);
		if (value < 0 || value > Review.START_END) {
			value = valueDefault;
		}
	}

	public void save() {
		preferences.putInt(key, value);
	}

	public Integer getInteger() {
		return value;
	}

	public void setInteger(Integer integer_) {
		value = integer_;
	}
}

class UserPreferenceShowModalMessageDialogBoxes extends UserPreference {
	private static final String key = "show modal message dialog boxes";

	private Boolean value = null;
	private static final Boolean valueDefault = false;

	public void load() {
		value = preferences.getBoolean(key, valueDefault);
	}

	public void save() {
		preferences.putBoolean(key, value);
	}

	public Boolean getBoolean() {
		return value;
	}

	public void setBoolean(Boolean boolean_) {
		value = boolean_;
	}
}

class UserPreferenceGameBoardLabelMode extends UserPreference {
	private static final String key = "game board label mode";

	private Integer value = null;
	private static final Integer valueDefault = GameBoard.LABEL_COORDINATES;

	public void load() {
		value = preferences.getInt(key, valueDefault);
		if (value < 0 || value > GameBoard.LABEL_END) {
			value = valueDefault;
		}
	}

	public void save() {
		preferences.putInt(key, value);
	}

	public Integer getInteger() {
		return value;
	}

	public void setInteger(Integer integer_) {
		value = integer_;
	}
}

class UserPreferenceShowMessagePrefixes extends UserPreference {
	private static final String key = "show message prefixes";

	private Boolean value = null;
	private static final Boolean valueDefault = true;

	public void load() {
		value = preferences.getBoolean(key, valueDefault);
	}

	public void save() {
		preferences.putBoolean(key, value);
	}

	public Boolean getBoolean() {
		return value;
	}

	public void setBoolean(Boolean boolean_) {
		value = boolean_;
	}
}

public class UserPreferences {
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
		userPreferenceArray[MAX_PLAYER_COUNT               ] = new UserPreferenceMaxPlayerCount();
		userPreferenceArray[USER_LIST_SORTING_METHOD       ] = new UserPreferenceUserListSortingMethod();
		userPreferenceArray[PLAY_SOUND_WHEN_WAITING_FOR_ME ] = new UserPreferencePlaySoundWhenWaitingForMe();
		userPreferenceArray[PATH_TO_SOUND                  ] = new UserPreferencePathToSound();
		userPreferenceArray[WRITE_TO_LOG_FILES             ] = new UserPreferenceWriteToLogFiles();
		userPreferenceArray[PATH_TO_LOG_FILES              ] = new UserPreferencePathToLogFiles();
		userPreferenceArray[WHERE_TO_START_IN_REVIEW_MODE  ] = new UserPreferenceWhereToStartInReviewMode();
		userPreferenceArray[SHOW_MODAL_MESSAGE_DIALOG_BOXES] = new UserPreferenceShowModalMessageDialogBoxes();
		userPreferenceArray[GAME_BOARD_LABEL_MODE          ] = new UserPreferenceGameBoardLabelMode();
		userPreferenceArray[SHOW_MESSAGE_PREFIXES          ] = new UserPreferenceShowMessagePrefixes();

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
