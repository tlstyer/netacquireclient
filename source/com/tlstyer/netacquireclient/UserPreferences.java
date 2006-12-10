package com.tlstyer.netacquire;

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

	// defaults
	private static final Integer maxPlayerCountDefault = 4;
	private static final Integer userListSortingMethodDefault = UserListPresenter.SORT_GAME_NUMBER;

	// preference keys
	private static final String KEY_nicknames = "nicknames";
	private static final String KEY_addressesAndPorts = "addresses and ports";
	private static final String KEY_maxPlayerCount = "max player count";
	private static final String KEY_userListSortingMethod = "user list sorting method";
	private static final String KEY_playSoundWhenWaitingForMe = "play sound when waiting for me";
	private static final String KEY_pathToSound = "path to sound";
	private static final String KEY_writeToLogFiles = "write to log files";
	private static final String KEY_pathToLogFiles = "path to log files";

	// preferences object
	private static final Preferences preferences = Preferences.userNodeForPackage(UserPreferences.class);

	public UserPreferences() {
		load();
	}
	
	public void load() {
		String[] nicknamesArray = preferences.get(KEY_nicknames, "").split(";", -1);
		nicknames = new ArrayList<String>();
		if (nicknamesArray.length == 1 && nicknamesArray[0].equals("")) {
			String nickname;
			try {
				nickname = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				Random random = new Random();
				nickname = "" + random.nextInt();
			}
			nicknames.add(nickname);
		} else {
			for (String nickname : nicknamesArray) {
				nicknames.add(nickname);
			}
		}
		
		String[] addressesAndPortsArray = preferences.get(KEY_addressesAndPorts, "").split(";", -1);
		addressesAndPorts = new ArrayList<String>();
		if (addressesAndPortsArray.length == 1 && addressesAndPortsArray[0].equals("")) {
			addressesAndPorts.add("acquire.game-host.org:1001");
			addressesAndPorts.add("acquire.sbg.org:1001");
			addressesAndPorts.add("acquire.sbg.org:1002");
			addressesAndPorts.add("acquire2.sbg.org:1001");
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

		playSoundWhenWaitingForMe = preferences.getBoolean(KEY_playSoundWhenWaitingForMe, false);
		
		pathToSound = preferences.get(KEY_pathToSound, "");
		
		writeToLogFiles = preferences.getBoolean(KEY_writeToLogFiles, false);
		
		pathToLogFiles = preferences.get(KEY_pathToLogFiles, "");
	}

	public void save() {
		preferences.put(KEY_nicknames, Util.join(nicknames.toArray(), ";"));
		preferences.put(KEY_addressesAndPorts, Util.join(addressesAndPorts.toArray(), ";"));
		preferences.putInt(KEY_maxPlayerCount, maxPlayerCount);
		preferences.putInt(KEY_userListSortingMethod, userListSortingMethod);
		preferences.putBoolean(KEY_playSoundWhenWaitingForMe, playSoundWhenWaitingForMe);
		preferences.put(KEY_pathToSound, pathToSound);
		preferences.putBoolean(KEY_writeToLogFiles, writeToLogFiles);
		preferences.put(KEY_pathToLogFiles, pathToLogFiles);
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
}
