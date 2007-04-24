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
	private static final String KEY_nicknames = "nicknames";

	private ArrayList<String> nicknames = null;

	public void load() {
		String[] nicknamesArray = preferences.get(KEY_nicknames, "").split(stringArraySeparator, -1);
		nicknames = new ArrayList<String>();
		if (nicknamesArray.length == 1 && nicknamesArray[0].length() == 0) {
			String nickname;
			try {
				nickname = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException unknownHostException) {
				Random random = new Random();
				nickname = "" + random.nextInt();
			}
			nicknames.add(nickname);
		} else {
			for (String nickname : nicknamesArray) {
				nicknames.add(nickname);
			}
		}
	}

	public void save() {
		putString(KEY_nicknames, Util.join(nicknames.toArray(), stringArraySeparator));
	}

	public ArrayList<String> getStringArrayList() {
		return nicknames;
	}

	public void setStringArrayList(ArrayList<String> stringArrayList_) {
		nicknames = stringArrayList_;
	}
}

class UserPreferenceAddressesAndPorts extends UserPreference {
	private static final String KEY_addressesAndPorts = "addresses and ports";

	private ArrayList<String> addressesAndPorts = null;

	public void load() {
		String[] addressesAndPortsArray = preferences.get(KEY_addressesAndPorts, "").split(stringArraySeparator, -1);
		addressesAndPorts = new ArrayList<String>();
		if (addressesAndPortsArray.length == 1 && addressesAndPortsArray[0].length() == 0) {
			addressesAndPorts.add("acquire.sbg.org:1001");
			addressesAndPorts.add("acquire.sbg.org:1002");
			addressesAndPorts.add("acquire.dynu.com:1001");
			addressesAndPorts.add("localhost:1001");
		} else {
			for (String addressAndPort : addressesAndPortsArray) {
				addressesAndPorts.add(addressAndPort);
			}
		}
	}

	public void save() {
		putString(KEY_addressesAndPorts, Util.join(addressesAndPorts.toArray(), stringArraySeparator));
	}

	public ArrayList<String> getStringArrayList() {
		return addressesAndPorts;
	}

	public void setStringArrayList(ArrayList<String> stringArrayList_) {
		addressesAndPorts = stringArrayList_;
	}
}

class UserPreferenceMaxPlayerCount extends UserPreference {
	private static final String KEY_maxPlayerCount = "max player count";

	private Integer maxPlayerCount = null;
	private static final Integer valueDefault = 4;

	public void load() {
		maxPlayerCount = preferences.getInt(KEY_maxPlayerCount, valueDefault);
		if (maxPlayerCount < 2 || maxPlayerCount > 6) {
			maxPlayerCount = valueDefault;
		}
	}

	public void save() {
		preferences.putInt(KEY_maxPlayerCount, maxPlayerCount);
	}

	public Integer getInteger() {
		return maxPlayerCount;
	}

	public void setInteger(Integer integer_) {
		maxPlayerCount = integer_;
	}
}

class UserPreferenceUserListSortingMethod extends UserPreference {
	private static final String KEY_userListSortingMethod = "user list sorting method";

	private Integer userListSortingMethod = null;
	private static final Integer showModalMessageDialogBoxesDefault = UserListPresenter.SORT_NONE;

	public void load() {
		userListSortingMethod = preferences.getInt(KEY_userListSortingMethod, showModalMessageDialogBoxesDefault);
		if (userListSortingMethod < 0 || userListSortingMethod > UserListPresenter.SORT_END) {
			userListSortingMethod = showModalMessageDialogBoxesDefault;
		}
	}

	public void save() {
		preferences.putInt(KEY_userListSortingMethod, userListSortingMethod);
	}

	public Integer getInteger() {
		return userListSortingMethod;
	}

	public void setInteger(Integer integer_) {
		userListSortingMethod = integer_;
	}
}

class UserPreferencePlaySoundWhenWaitingForMe extends UserPreference {
	private static final String KEY_playSoundWhenWaitingForMe = "play sound when waiting for me";

	private Boolean playSoundWhenWaitingForMe = null;
	private static final Boolean playSoundWhenWaitingForMeDefault = false;

	public void load() {
		playSoundWhenWaitingForMe = preferences.getBoolean(KEY_playSoundWhenWaitingForMe, playSoundWhenWaitingForMeDefault);
	}

	public void save() {
		preferences.putBoolean(KEY_playSoundWhenWaitingForMe, playSoundWhenWaitingForMe);
	}

	public Boolean getBoolean() {
		return playSoundWhenWaitingForMe;
	}

	public void setBoolean(Boolean boolean_) {
		playSoundWhenWaitingForMe = boolean_;
	}
}

class UserPreferencePathToSound extends UserPreference {
	private static final String KEY_pathToSound = "path to sound";

	private String pathToSound = null;
	private static final String pathToSoundDefault = "";

	public void load() {
		pathToSound = preferences.get(KEY_pathToSound, pathToSoundDefault);
	}

	public void save() {
		putString(KEY_pathToSound, pathToSound);
	}

	public String getString() {
		return pathToSound;
	}

	public void setString(String string_) {
		pathToSound = string_;
	}
}

class UserPreferenceWriteToLogFiles extends UserPreference {
	private static final String KEY_writeToLogFiles = "write to log files";

	private Boolean writeToLogFiles = null;
	private static final Boolean writeToLogFilesDefault = false;

	public void load() {
		writeToLogFiles = preferences.getBoolean(KEY_writeToLogFiles, writeToLogFilesDefault);
	}

	public void save() {
		preferences.putBoolean(KEY_writeToLogFiles, writeToLogFiles);
	}

	public Boolean getBoolean() {
		return writeToLogFiles;
	}

	public void setBoolean(Boolean boolean_) {
		writeToLogFiles = boolean_;
	}
}

class UserPreferencePathToLogFiles extends UserPreference {
	private static final String KEY_pathToLogFiles = "path to log files";

	private String pathToLogFiles = null;
	private static final String pathToLogFilesDefault = "";

	public void load() {
		pathToLogFiles = preferences.get(KEY_pathToLogFiles, pathToLogFilesDefault);
	}

	public void save() {
		putString(KEY_pathToLogFiles, pathToLogFiles);
	}

	public String getString() {
		return pathToLogFiles;
	}

	public void setString(String string_) {
		pathToLogFiles = string_;
	}
}

class UserPreferenceWhereToStartInReviewMode extends UserPreference {
	private static final String KEY_whereToStartInReviewMode = "where to start in review mode";

	private Integer whereToStartInReviewMode = null;
	private static final Integer whereToStartInReviewModeDefault = Review.START_AT_BEGINNING_OF_GAME;

	public void load() {
		whereToStartInReviewMode = preferences.getInt(KEY_whereToStartInReviewMode, whereToStartInReviewModeDefault);
		if (whereToStartInReviewMode < 0 || whereToStartInReviewMode > Review.START_END) {
			whereToStartInReviewMode = whereToStartInReviewModeDefault;
		}
	}

	public void save() {
		preferences.putInt(KEY_whereToStartInReviewMode, whereToStartInReviewMode);
	}

	public Integer getInteger() {
		return whereToStartInReviewMode;
	}

	public void setInteger(Integer integer_) {
		whereToStartInReviewMode = integer_;
	}
}

class UserPreferenceShowModalMessageDialogBoxes extends UserPreference {
	private static final String KEY_showModalMessageDialogBoxes = "show modal message dialog boxes";

	private Boolean showModalMessageDialogBoxes = null;
	private static final Boolean showModalMessageDialogBoxesDefault = false;

	public void load() {
		showModalMessageDialogBoxes = preferences.getBoolean(KEY_showModalMessageDialogBoxes, showModalMessageDialogBoxesDefault);
	}

	public void save() {
		preferences.putBoolean(KEY_showModalMessageDialogBoxes, showModalMessageDialogBoxes);
	}

	public Boolean getBoolean() {
		return showModalMessageDialogBoxes;
	}

	public void setBoolean(Boolean boolean_) {
		showModalMessageDialogBoxes = boolean_;
	}
}

class UserPreferenceGameBoardLabelMode extends UserPreference {
	private static final String KEY_gameBoardLabelMode = "game board label mode";

	private Integer gameBoardLabelMode = null;
	private static final Integer gameBoardLabelModeDefault = GameBoard.LABEL_COORDINATES;

	public void load() {
		gameBoardLabelMode = preferences.getInt(KEY_gameBoardLabelMode, gameBoardLabelModeDefault);
		if (gameBoardLabelMode < 0 || gameBoardLabelMode > GameBoard.LABEL_END) {
			gameBoardLabelMode = gameBoardLabelModeDefault;
		}
	}

	public void save() {
		preferences.putInt(KEY_gameBoardLabelMode, gameBoardLabelMode);
	}

	public Integer getInteger() {
		return gameBoardLabelMode;
	}

	public void setInteger(Integer integer_) {
		gameBoardLabelMode = integer_;
	}
}

class UserPreferenceShowMessagePrefixes extends UserPreference {
	private static final String KEY_showMessagePrefixes = "show message prefixes";

	private Boolean showMessagePrefixes = null;
	private static final Boolean showMessagePrefixesDefault = true;

	public void load() {
		showMessagePrefixes = preferences.getBoolean(KEY_showMessagePrefixes, showMessagePrefixesDefault);
	}

	public void save() {
		preferences.putBoolean(KEY_showMessagePrefixes, showMessagePrefixes);
	}

	public Boolean getBoolean() {
		return showMessagePrefixes;
	}

	public void setBoolean(Boolean boolean_) {
		showMessagePrefixes = boolean_;
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
