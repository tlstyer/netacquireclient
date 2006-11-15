import java.io.*;
import javax.sound.sampled.*;

public class SoundManager {
	private Clip clip = null;
	private AudioInputStream audioInputStream = null;

	public SoundManager() {
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		File file = new File("my_turn.wav");
		
		try {
			audioInputStream = AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			clip.open(audioInputStream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void playSound() {
		clip.stop();
		clip.setFramePosition(0);
		clip.start();
	}
}
