import java.io.*;
import javax.sound.sampled.*;

public class SoundManager {
	public static int CLIP_WAITING_FOR_ME = 0;
	public static int CLIP_TEST = 1;
	public static int CLIP_COUNT = 2;

	private Clip[] clips = new Clip[CLIP_COUNT];

	public SoundManager() {
		if (SerializedData.getSerializedData().getPlaySoundWhenWaitingForMe()) {
			openSound(SerializedData.getSerializedData().getPathToSound(), CLIP_WAITING_FOR_ME);
		}
	}

	public boolean openSound(String pathname, int clipType) {
		clips[clipType] = null;
		
		Clip clip = null;
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return false;
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
			clip.open(audioInputStream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		clips[clipType] = clip;
		return true;
	}

	public void playSound(int clipType) {
		Clip clip = clips[clipType];
		if (clip != null) {
			clip.stop();
			clip.setFramePosition(0);
			clip.start();
		}
	}
}
