package sound;

import menu.MenuPanel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Singleton class for SFX.
 */
public class SoundManager {
    private static SoundManager instance;
    private final HashMap<String, URL> soundMap = new HashMap<>();
    private Clip musicClip;

    // Thread pool for sounds
    private final ExecutorService soundExecutor = Executors.newCachedThreadPool();

    private SoundManager() {}

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
            instance.loadSounds();
        }
        return instance;
    }

    private void loadSounds() {
        soundMap.put("bounce", loadSound("/assets/sound/hitpadle.wav"));
        soundMap.put("wall", loadSound("/assets/sound/wall-hit.wav"));
        soundMap.put("brick_break", loadSound("/assets/sound/hitbrick.wav"));
        soundMap.put("explode", loadSound("/assets/sound/retro-explode.wav"));
        soundMap.put("extend_paddle", loadSound("/assets/sound/extend-paddle-powerup.wav"));
        soundMap.put("laser", loadSound("/assets/sound/laser-powerup.wav"));
        soundMap.put("life_up", loadSound("/assets/sound/heart-powerup.wav"));
        soundMap.put("life_down", loadSound("/assets/sound/life-down-powerup.wav"));
        soundMap.put("game_win", loadSound("/assets/sound/game-win.wav"));
        soundMap.put("game_over", loadSound("/assets/sound/game-over.wav"));
        soundMap.put("bg_music", loadSound("/assets/sound/bg_music.wav"));
    }

    private URL loadSound(String path) {
        URL url = getClass().getResource(path);
        if (url != null) {
            System.out.println("Sound loaded: " + path);
            return url;
        }
        System.err.println("Sound failed to load: " + path);
        return null;
    }

    /**
     * Play SFX.
     */
    public void play(String key) {
        if (!MenuPanel.soundOn) return;

        URL url = soundMap.get(key);
        if (url == null) {
            System.err.println("Sound not found: " + key);
            return;
        }

        soundExecutor.submit(() -> {
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(stream);

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });

                clip.start();
            } catch (Exception e) {
                System.err.println("Error playing sound '" + key + "': " + e.getMessage());
                // Xu li loi?
            }
        });
    }

    /**
     * Play music.
     */
    public synchronized void playMusic(String key) {
        stopMusic();
        if (!MenuPanel.soundOn) return;

        try {
            URL url = soundMap.get(key);
            if (url == null) {
                System.err.println("Music not found: " + key);
                return;
            }

            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            musicClip = AudioSystem.getClip();
            musicClip.open(ais);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();

        } catch (Exception e) {
            System.err.println("Failed to play music: " + key + " – " + e.getMessage());
        }
    }

    public synchronized void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
        }
    }

    public synchronized boolean isMusicPlaying() {
        return musicClip != null && musicClip.isRunning();
    }
}