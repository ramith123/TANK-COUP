import javax.sound.sampled.AudioInputStream; // for playing sound clips
import javax.sound.sampled.*;
import java.io.*;

import java.util.HashMap; // for storing sound clips

public class SoundManager { // a Singleton class
	HashMap<String, Clip> clips;
	public static String BGMUSIC = "backgroundMusic.wav";
	public static String GOMUSIC = "gameOverSound.wav";
	public boolean mute = false;

	private static SoundManager instance = null; // keeps track of Singleton instance
	private String directory = "sounds";

	private SoundManager() {
		clips = new HashMap<String, Clip>();

		// Auto load all music from sounds folder
		File directoryPath = new File(directory);
		String contents[] = directoryPath.list();
		Clip clip;
		for (int i = 0; i < contents.length; i++) {
			clip = loadClip(directory + "/" + contents[i]);
			clips.put(contents[i], clip);

		}

	}

	public static SoundManager getInstance() { // class method
		if (instance == null)
			instance = new SoundManager();

		return instance;
	}

	public Clip getClip(String fileName) {

		return clips.get(fileName);
	}

	public Clip loadClip(String fileName) { // gets clip from the specified file
		AudioInputStream audioIn;
		Clip clip = null;

		try {
			File file = new File(fileName);
			audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL());
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (Exception e) {
			System.out.println("Error opening sound files: " + e);
		}
		return clip;
	}

	public void playSound(String fileName, Boolean looping) {
		if (mute)
			return;
		Clip clip = getClip(fileName);
		if (clip != null) {
			clip.setFramePosition(0);
			if (looping)
				clip.loop(Clip.LOOP_CONTINUOUSLY);
			else
				clip.start();

		}
	}

	public void stopSound(String fileName) {
		Clip clip = getClip(fileName);
		if (clip != null) {
			clip.stop();
		}
	}

	public void mute() {
		if (mute) {
			mute = false;
			playSound(BGMUSIC, true);
		} else {
			mute = true;
			stopSound(BGMUSIC);
		}
	}
}