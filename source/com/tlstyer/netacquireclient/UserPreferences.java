package com.tlstyer.netacquireclient;

import java.net.*;
import java.util.*;
import java.util.prefs.*;

public class UserPreferences {
	// data
	private ArrayList<String> nicknames = null;
	private ArrayList<String> addressesAndPorts = null;
	private Integer maxPlayerCount = null;
	private Integer userListSortingMethod = null;
	private Boolean playSoundWhenWaitingForMe = null;
	private String pathToSound = null;
	private Boolean writeToLogFiles = null;
	private String pathToLogFiles = null;
	private Integer whereToStartInReviewMode = null;
	private Boolean showModalMessageDialogBoxes = null;
	private Integer gameBoardLabelMode = null;
	private Boolean showMessagePrefixes = null;

	// defaults
	private static final Integer maxPlayerCountDefault = 4;
	private static final Integer userListSortingMethodDefault = UserListPresenter.SORT_NONE;
	private static final Boolean playSoundWhenWaitingForMeDefault = false;
	private static final String pathToSoundDefault = "";
	private static final Boolean writeToLogFilesDefault = false;
	private static final String pathToLogFilesDefault = "";
	private static final Integer whereToStartInReviewModeDefault = Review.START_AT_BEGINNING_OF_GAME;
	private static final Boolean showModalMessageDialogBoxesDefault = false;
	private static final Integer gameBoardLabelModeDefault = GameBoard.LABEL_COORDINATES;
	private static final Boolean showMessagePrefixesDefault = true;

	// preference keys
	private static final String KEY_nicknames = "nicknames";
	private static final String KEY_addressesAndPorts = "addresses and ports";
	private static final String KEY_maxPlayerCount = "max player count";
	private static final String KEY_userListSortingMethod = "user list sorting method";
	private static final String KEY_playSoundWhenWaitingForMe = "play sound when waiting for me";
	private static final String KEY_pathToSound = "path to sound";
	private static final String KEY_writeToLogFiles = "write to log files";
	private static final String KEY_pathToLogFiles = "path to log files";
	private static final String KEY_whereToStartInReviewMode = "where to start in review mode";
	private static final String KEY_showModalMessageDialogBoxes = "show modal message dialog boxes";
	private static final String KEY_gameBoardLabelMode = "game board label mode";
	private static final String KEY_showMessagePrefixes = "show message prefixes";

	// other
	private static final Preferences preferences = Preferences.userNodeForPackage(UserPreferences.class);
	private static final String stringArraySeparator = "::";

	public UserPreferences() {
		load();
	}

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

		maxPlayerCount = preferences.getInt(KEY_maxPlayerCount, maxPlayerCountDefault);
		if (maxPlayerCount < 2 || maxPlayerCount > 6) {
			maxPlayerCount = maxPlayerCountDefault;
		}

		userListSortingMethod = preferences.getInt(KEY_userListSortingMethod, userListSortingMethodDefault);
		if (userListSortingMethod < 0 || userListSortingMethod > UserListPresenter.SORT_END) {
			userListSortingMethod = userListSortingMethodDefault;
		}

		playSoundWhenWaitingForMe = preferences.getBoolean(KEY_playSoundWhenWaitingForMe, playSoundWhenWaitingForMeDefault);

		pathToSound = preferences.get(KEY_pathToSound, pathToSoundDefault);

		writeToLogFiles = preferences.getBoolean(KEY_writeToLogFiles, writeToLogFilesDefault);

		pathToLogFiles = preferences.get(KEY_pathToLogFiles, pathToLogFilesDefault);

		whereToStartInReviewMode = preferences.getInt(KEY_whereToStartInReviewMode, whereToStartInReviewModeDefault);
		if (whereToStartInReviewMode < 0 || whereToStartInReviewMode > Review.START_END) {
			whereToStartInReviewMode = whereToStartInReviewModeDefault;
		}

		showModalMessageDialogBoxes = preferences.getBoolean(KEY_showModalMessageDialogBoxes, showModalMessageDialogBoxesDefault);

		gameBoardLabelMode = preferences.getInt(KEY_gameBoardLabelMode, gameBoardLabelModeDefault);
		if (gameBoardLabelMode < 0 || gameBoardLabelMode > GameBoard.LABEL_END) {
			gameBoardLabelMode = gameBoardLabelModeDefault;
		}

		showMessagePrefixes = preferences.getBoolean(KEY_showMessagePrefixes, showMessagePrefixesDefault);
	}

	public void save() {
		putString(KEY_nicknames, Util.join(nicknames.toArray(), stringArraySeparator));
		putString(KEY_addressesAndPorts, Util.join(addressesAndPorts.toArray(), stringArraySeparator));
		preferences.putInt(KEY_maxPlayerCount, maxPlayerCount);
		preferences.putInt(KEY_userListSortingMethod, userListSortingMethod);
		preferences.putBoolean(KEY_playSoundWhenWaitingForMe, playSoundWhenWaitingForMe);
		putString(KEY_pathToSound, pathToSound);
		preferences.putBoolean(KEY_writeToLogFiles, writeToLogFiles);
		putString(KEY_pathToLogFiles, pathToLogFiles);
		preferences.putInt(KEY_whereToStartInReviewMode, whereToStartInReviewMode);
		preferences.putBoolean(KEY_showModalMessageDialogBoxes, showModalMessageDialogBoxes);
		preferences.putInt(KEY_gameBoardLabelMode, gameBoardLabelMode);
		preferences.putBoolean(KEY_showMessagePrefixes, showMessagePrefixes);
	}

	private void putString(String key, String value) {
		if (value.length() > Preferences.MAX_VALUE_LENGTH) {
			value = value.substring(0, Preferences.MAX_VALUE_LENGTH);
		}
		preferences.put(key, value);
	}

	public ArrayList<String> getNicknames() {
		return nicknames;
	}

	public void setNicknames(ArrayList<String> nicknames) {
		this.nicknames = nicknames;
	}

	public ArrayList<String> getAddressesAndPorts() {
		return addressesAndPorts;
	}

	public void setAddressesAndPorts(ArrayList<String> addressesAndPorts) {
		this.addressesAndPorts = addressesAndPorts;
	}

	public Integer getMaxPlayerCount() {
		return maxPlayerCount;
	}

	public void setMaxPlayerCount(Integer maxPlayerCount) {
		this.maxPlayerCount = maxPlayerCount;
	}

	public Integer getUserListSortingMethod() {
		return userListSortingMethod;
	}

	public void setUserListSortingMethod(Integer userListSortingMethod) {
		this.userListSortingMethod = userListSortingMethod;
	}

	public Boolean getPlaySoundWhenWaitingForMe() {
		return playSoundWhenWaitingForMe;
	}

	public void setPlaySoundWhenWaitingForMe(Boolean playSoundWhenWaitingForMe) {
		this.playSoundWhenWaitingForMe = playSoundWhenWaitingForMe;
	}

	public String getPathToSound() {
		return pathToSound;
	}

	public void setPathToSound(String pathToSound) {
		this.pathToSound = pathToSound;
	}

	public Boolean getWriteToLogFiles() {
		return writeToLogFiles;
	}

	public void setWriteToLogFiles(Boolean writeToLogFiles) {
		this.writeToLogFiles = writeToLogFiles;
	}

	public String getPathToLogFiles() {
		return pathToLogFiles;
	}

	public void setPathToLogFiles(String pathToLogFiles) {
		this.pathToLogFiles = pathToLogFiles;
	}

	public Integer getWhereToStartInReviewMode() {
		return whereToStartInReviewMode;
	}

	public void setWhereToStartInReviewMode(Integer whereToStartInReviewMode) {
		this.whereToStartInReviewMode = whereToStartInReviewMode;
	}

	public Boolean getShowModalMessageDialogBoxes() {
		return showModalMessageDialogBoxes;
	}

	public void setShowModalMessageDialogBoxes(Boolean showModalMessageDialogBoxes) {
		this.showModalMessageDialogBoxes = showModalMessageDialogBoxes;
	}

	public Integer getGameBoardLabelMode() {
		return gameBoardLabelMode;
	}

	public void setGameBoardLabelMode(Integer gameBoardLabelMode) {
		this.gameBoardLabelMode = gameBoardLabelMode;
	}

	public Boolean getShowMessagePrefixes() {
		return showMessagePrefixes;
	}

	public void setShowMessagePrefixes(Boolean showMessagePrefixes) {
		this.showMessagePrefixes = showMessagePrefixes;
	}
}
