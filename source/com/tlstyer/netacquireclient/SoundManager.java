package com.tlstyer.netacquire;

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
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
		}
		
		loadSound(Main.getSerializedData().getPathToSound());
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
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		try {
			clips[currentClipIndex].open(audioInputStream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
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
