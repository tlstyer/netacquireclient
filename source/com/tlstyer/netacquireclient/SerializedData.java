import java.io.*;
import java.net.*;
import java.util.*;

public class SerializedData implements Serializable {
	private static final long serialVersionUID = -1818673008683293864L;
	
	private Vector<String> nicknames = null;
	private Vector<String> addressesAndPorts = null;
	private Integer maxPlayerCount = 4;
	
	private static SerializedData serializedData = null;

	private static final String filename = "SerializedData.ser";

	private SerializedData() {
		initializeNullFields();
	}
	
	private void initializeNullFields() {
		if (nicknames == null) {
			nicknames = new Vector<String>();
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
			addressesAndPorts = new Vector<String>();
			addressesAndPorts.add("localhost:1001");
			addressesAndPorts.add("acquire.sbg.org:1001");
			addressesAndPorts.add("acquire.sbg.org:1002");
			addressesAndPorts.add("acquire2.sbg.org:1001");
		}
		
		if (maxPlayerCount == null || maxPlayerCount < 2 || maxPlayerCount > 6) {
			maxPlayerCount = 4;
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

	public Vector<String> getNicknames() {
		return nicknames;
	}

	public Vector<String> getAddressesAndPorts() {
		return addressesAndPorts;
	}

	public Integer getMaxPlayerCount() {
		return maxPlayerCount;
	}

	public void setMaxPlayerCount(Integer maxPlayerCount) {
		this.maxPlayerCount = maxPlayerCount;
	}
}
