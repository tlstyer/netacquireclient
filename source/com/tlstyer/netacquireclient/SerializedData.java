import java.io.*;
import java.net.*;
import java.util.*;

public class SerializedData implements Serializable {
	private static final long serialVersionUID = -1818673008683293864L;
	
	private String nickname;
	private Integer maxPlayerCount;
	
	private static SerializedData serializedData = null;

	private static final String filename = "SerializedData.ser";

	private SerializedData() {
		try {
			nickname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			Random random = new Random();
			nickname = "" + random.nextInt();
		}
		maxPlayerCount = 4;
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
			objectInputStream.close();
			if (serializedData.maxPlayerCount < 2 || serializedData.maxPlayerCount > 6) {
				serializedData.maxPlayerCount = 4;
			}
		} catch(Exception e) {
			e.printStackTrace();
			serializedData = new SerializedData();
		}
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getMaxPlayerCount() {
		return maxPlayerCount;
	}

	public void setMaxPlayerCount(Integer maxPlayerCount) {
		this.maxPlayerCount = maxPlayerCount;
	}
}
