package sound;

import menu.MenuPanel;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;
import java.util.HashMap;

/**
 * Singleton class for SFX.
 */
public class SoundManager {
    private static SoundManager instance;
    private final HashMap<String, URL> soundMap = new HashMap<>();
    private Clip musicClip;

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

    private URL loadSound(String... paths) {
        for (String path : paths) {
            URL url = getClass().getResource(path);
            if (url != null) {
                System.out.println("Loaded: " + path);
                return url;
            }
        }
        System.err.println("Failed to load sound from paths: " + String.join(", ", paths));
        return null;
    }

    public void play(String path) {
        // Kiểm tra setting âm thanh
        if (!MenuPanel.soundOn) {
            return;
        }

        try {
            URL url = soundMap.get(path);
            if (url == null) {
                System.err.println("Sound not found: " + path);
                return;
            }

            AudioInputStream stream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();

            clip.open(stream);

            // Tự động đóng clip sau khi phát xong để tránh memory leak
            clip.addLineListener(event -> {
                if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                    clip.close();
                }
            });

            clip.start();

        } catch (Exception e) {
            System.err.println("Error playing sound '" + path + "': " + e.getMessage());
        }
    }

    public void playMusic(String path) {
        stopMusic();
        if (!MenuPanel.soundOn) return;

        try {
            URL url = soundMap.get(path);
            if (url == null) {
                System.err.println("Music not found: " + path);
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            musicClip = AudioSystem.getClip();
            musicClip.open(ais);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } catch (Exception e) {
            System.err.println("Failed to play music: " + path);
        }
    }

    public void stopMusic() {
        if (musicClip != null) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
        }
    }

    public boolean isPlaying() {
        return false;
    }
}