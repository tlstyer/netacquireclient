package com.tlstyer.netacquireclient;

import java.io.*;
import javax.sound.sampled.*;

public final class SoundManager {

	private static final int NUM_CLIPS = 3;
	private final Clip[] clips = new Clip[NUM_CLIPS];
	private int currentClipIndex = 0;

	public SoundManager() {
		for (int index = 0; index < NUM_CLIPS; ++index) {
			clips[index] = null;
			try {
				clips[index] = AudioSystem.getClip();
			} catch (LineUnavailableException e) {
				Util.printStackTrace(e);
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

		AudioInputStream audioInputStream;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException | IOException e) {
			Util.printStackTrace(e);
			return false;
		}

		try {
			clips[currentClipIndex].open(audioInputStream);
		} catch (LineUnavailableException | IOException e) {
			Util.printStackTrace(e);
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
