import java.io.*;
import java.net.*;
import java.util.*;

public class SerializedData implements Serializable {
	private static final long serialVersionUID = -1818673008683293864L;
	
	private ArrayList<String> nicknames = null;
	private ArrayList<String> addressesAndPorts = null;
	private Integer maxPlayerCount = 4;
	private Integer userListSortingMethod = UserListPresenter.SORT_GAME_NUMBER;
	private Boolean playSoundWhenWaitingForMe = false;
	private String pathToSound = null;
	private Boolean writeToLogFiles = null;
	private String pathToLogFiles = null;
	
	private static SerializedData serializedData = null;

	private static final String filename = "SerializedData.ser";

	private SerializedData() {
		initializeNullFields();
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

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public static SerializedData getSerializedData() {
		return serializedData;
	}

	public static void SaveSerializedData() {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(filename);
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(serializedData);
			objectOutputStream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void LoadSerializedData() {
		FileInputStream fileInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			fileInputStream = new FileInputStream(filename);
			objectInputStream = new ObjectInputStream(fileInputStream);
			serializedData = (SerializedData)objectInputStream.readObject();
			serializedData.initializeNullFields();
			objectInputStream.close();
		} catch(Exception e) {
			e.printStackTrace();
			serializedData = new SerializedData();
		}
	}

	public ArrayList<String> getNicknames() {
		return nicknames;
	}

	public ArrayList<String> getAddressesAndPorts() {
		return addressesAndPorts;
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
