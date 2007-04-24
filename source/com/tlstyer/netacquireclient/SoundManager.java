package com.tlstyer.netacquireclient;

import java.io.*;
import javax.sound.sampled.*;

public class SoundManager {
	private static final int NUM_CLIPS = 3;
	private Clip[] clips = new Clip[NUM_CLIPS];
	private int currentClipIndex = 0;

	public SoundManager() {
		for (int index=0; index<NUM_CLIPS; ++index) {
			clips[index] = null;
			try {
				clips[index] = AudioSystem.getClip();
			} catch (LineUnavailableException lineUnavailableException) {
				lineUnavailableException.printStackTrace();
			}
		}

		loadSound(Main.getUserPreferences().getString(UserPreferences.PATH_TO_SOUND));
	}

	public boolean loadSound(String pathname) {
		currentClipIndex = (currentClipIndex + 1) % NUM_CLIPS;

		if (clips[currentClipIndex].isOpen()) {
			clips[currentClipIndex].close();
		}

		File file = new File(pathname);

		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException unsupportedAudioFileException) {
			unsupportedAudioFileException.printStackTrace();
			return false;
		} catch (IOException iOException) {
			iOException.printStackTrace();
			return false;
		}

		try {
			clips[currentClipIndex].open(audioInputStream);
		} catch (LineUnavailableException lineUnavailableException) {
			lineUnavailableException.printStackTrace();
			return false;
		} catch (IOException iOException) {
			iOException.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean playSound(String pathname) {
		boolean soundLoaded = loadSound(pathname);
		if (soundLoaded) {
			clips[currentClipIndex].start();
		}
		return soundLoaded;
	}
}
