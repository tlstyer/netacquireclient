package com.tlstyer.netacquireclient;

import java.io.*;
import java.nio.charset.*;
import java.util.*;

public class LogFileWriter {

	private final ArrayList<String> messages = new ArrayList<>();
	private FileOutputStream fileOutputStream = null;
	private int numMessagesWritten = 0;

	private int mode;

	private static final Charset charset = Charset.forName("US-ASCII");

	public LogFileWriter() {
	}

	public static final int MESSAGE_INCOMING = 1;
	public static final int MESSAGE_OUTGOING = 2;

	private void writeMessage_(int type, String message) {
		if (mode != Main.MODE_IN_GAME && mode != Main.MODE_IN_GAME_WAITING_FOR_ME_TO_START_GAME) {
			return;
		}

		String messageFull;
		if (type == MESSAGE_INCOMING) {
			messageFull = "+" + message + "\n";
		} else {
			messageFull = "-" + message + "\n";
		}
		messages.add(messageFull);

		writeMessages_();
	}

	private void writeMessages_() {
		if (mode != Main.MODE_IN_GAME && mode != Main.MODE_IN_GAME_WAITING_FOR_ME_TO_START_GAME) {
			return;
		}

		if (Main.getUserPreferences().getBoolean(UserPreferences.WRITE_TO_LOG_FILES)) {
			if (fileOutputStream == null) {
				File file = new File(Main.getUserPreferences().getString(UserPreferences.PATH_TO_LOG_FILES), Util.getTimeString() + ".log");
				try {
					fileOutputStream = new FileOutputStream(file);
				} catch (FileNotFoundException fileNotFoundException) {
				}
				numMessagesWritten = 0;
			}
			if (fileOutputStream != null) {
				for (int index = numMessagesWritten; index < messages.size(); ++index) {
					try {
						fileOutputStream.write(charset.encode(messages.get(index)).array());
					} catch (IOException iOException) {
					}
				}
				numMessagesWritten = messages.size();
			}
		}
	}

	private void closeLogFile_() {
		if (fileOutputStream != null) {
			try {
				fileOutputStream.close();
			} catch (IOException iOException) {
			}
			fileOutputStream = null;
		}
	}

	private void setMode_(int mode_) {
		mode = mode_;

		if (mode != Main.MODE_IN_GAME && mode != Main.MODE_IN_GAME_WAITING_FOR_ME_TO_START_GAME) {
			messages.clear();
			closeLogFile_();
		}
	}

	public void writeMessage(int type, String message) {
		synchronized (this) {
			writeMessage_(type, message);
		}
	}

	public void writeMessages() {
		synchronized (this) {
			writeMessages_();
		}
	}

	public void closeLogFile() {
		synchronized (this) {
			closeLogFile_();
		}
	}

	public void setMode(int mode_) {
		synchronized (this) {
			setMode_(mode_);
		}
	}
}
