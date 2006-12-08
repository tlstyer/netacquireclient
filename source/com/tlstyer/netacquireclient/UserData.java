package com.tlstyer.netacquire;

import java.beans.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class UserData {
	private ArrayList<String> nicknames = null;
	private ArrayList<String> addressesAndPorts = null;
	private Integer maxPlayerCount = null;
	private Integer userListSortingMethod = null;
	private Boolean playSoundWhenWaitingForMe = null;
	private String pathToSound = null;
	private Boolean writeToLogFiles = null;
	private String pathToLogFiles = null;
	
	private static final String filename = "UserData.xml";

	public UserData() {
	}
	
	private void initializeNullFields() {
		if (nicknames == null) {
			nicknames = new ArrayList<String>();
			String nickname;
			try {
				nickname = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				Random random = new Random();
				nickname = "" + random.nextInt();
			}
			nicknames.add(nickname);
		}
		
		if (addressesAndPorts == null) {
			addressesAndPorts = new ArrayList<String>();
			addressesAndPorts.add("acquire.game-host.org:1001");
			addressesAndPorts.add("acquire.sbg.org:1001");
			addressesAndPorts.add("acquire.sbg.org:1002");
			addressesAndPorts.add("acquire2.sbg.org:1001");
			addressesAndPorts.add("localhost:1001");
		}
		
		if (maxPlayerCount == null || maxPlayerCount < 2 || maxPlayerCount > 6) {
			maxPlayerCount = 4;
		}
		
		if (userListSortingMethod == null || userListSortingMethod < 0 || userListSortingMethod > UserListPresenter.SORT_END) {
			userListSortingMethod = UserListPresenter.SORT_GAME_NUMBER;
		}

		if (playSoundWhenWaitingForMe == null) {
			playSoundWhenWaitingForMe = false;
		}
		
		if (pathToSound == null) {
			pathToSound = "";
		}
		
		if (writeToLogFiles == null) {
			writeToLogFiles = false;
		}
		
		if (pathToLogFiles == null) {
			pathToLogFiles = "";
		}
	}

	public void SaveUserData() {
		try {
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));
			encoder.writeObject(this);
			encoder.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static UserData LoadUserData() {
		UserData userData = null;
		try {
			XMLDecoder decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
			userData = (UserData)decoder.readObject();
			decoder.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		if (userData == null) {
			userData = new UserData();
		}
		userData.initializeNullFields();
		return userData;
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
