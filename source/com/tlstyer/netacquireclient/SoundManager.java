import java.io.*;
import javax.sound.sampled.*;

public class SoundManager {
	public SoundManager() {
		loadSound(SerializedData.getSerializedData().getPathToSound());
	}
	
	public Clip loadSound(String pathname) {
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return null;
		}
		
		File file = new File(pathname);

		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		try {
			clip.open(audioInputStream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return clip;
	}

	public boolean playSound(String pathname) {
		Clip clip = loadSound(pathname);
		if (clip != null) {
			clip.start();
			return true;
		} else {
			return false;
		}
	}
}
