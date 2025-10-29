package graphics;
import menu.MenuPanel;

import menu.MenuPanel;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Assets {
    public static Image background;
    public static Image backgroundMenu;
    public static Image paddle;
    public static Image ball;
    public static Image brick;

    public static Image paddleNormal;
    public static Image paddleGlow;

    public static Image brickNormal;
    public static Image brickExplosive;
    public static Image brickUnbreakable;
    public static Image brickStrong3;
    public static Image brickStrong2;
    public static Image brickStrong1;

    /**
     * PowerUp Image.
     */
    public static Image laserPowerUp;
    public static Image extraLifePowerUp;
    public static Image lifeDownPowerUp;
    public static Image widePaddlePowerUp;

    // Sound clips
    public static Clip backgroundMusic;
    public static Clip paddleHitSound;
    public static Clip unbreakableHitSound;
    public static Clip explosionSound;
    
    private static boolean soundEnabled = true;
    private static final String SOUND_PATH = "assets/sound/";

    // gọi hàm này 1 lần khi khởi động game
    public static void load() {
        try {
            backgroundMenu = ImageIO.read(new File("assets/Background1.png"));
            background = ImageIO.read(new File("assets/Background2.png"));
            paddleNormal = ImageIO.read(new File("assets/Player.png")); // paddle
            paddleGlow = ImageIO.read(new File("assets/Player_flash.png"));
            ball = ImageIO.read(new File("assets/Ball_small-blue.png"));
            brickNormal = ImageIO.read(new File("assets/Brick1_4.png"));
            brickExplosive = ImageIO.read(new File("assets/Brick7_4.png"));
            brickUnbreakable = ImageIO.read(new File("assets/Brick_unbreakable2.png"));
            brickStrong3 = ImageIO.read(new File("assets/Brick5_4.png")); // tím đậm (3 hit)
            brickStrong2 = ImageIO.read(new File("assets/Brick6_4.png")); // tím vừa (2 hit)
            brickStrong1 = ImageIO.read(new File("assets/Brick9_4.png")); // tím nhạt (1 hit)
            laserPowerUp = ImageIO.read(new File("assets/laser.png"));
            extraLifePowerUp = ImageIO.read(new File("assets/extralife.png"));
            lifeDownPowerUp = ImageIO.read(new File("assets/debuff.png"));
            widePaddlePowerUp = ImageIO.read(new File("assets/expand.png"));

            // Debug: Print working directory
            System.out.println("Working Directory: " + System.getProperty("user.dir"));

            // Load sounds with debug info
            backgroundMusic = loadSoundWithDebug(SOUND_PATH + "bg_music.wav");
            paddleHitSound = loadSoundWithDebug(SOUND_PATH + "hitpadle.wav");
            unbreakableHitSound = loadSoundWithDebug(SOUND_PATH + "ks-motion-metal-hit-351564.wav");
            explosionSound = loadSoundWithDebug(SOUND_PATH + "vine-boom-392646.wav");

            // Start background music if enabled and loaded successfully
            if (backgroundMusic != null) {
                System.out.println("Starting background music...");
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } catch (Exception e) {
            System.err.println("Error in Assets.load(): " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Clip loadSoundWithDebug(String path) {
        try {
            File soundFile = new File(path);
            System.out.println("Loading sound: " + path);
            System.out.println("File exists: " + soundFile.exists());
            System.out.println("File absolute path: " + soundFile.getAbsolutePath());

            if (!soundFile.exists()) {
                System.err.println("Sound file not found: " + path);
                return null;
            }

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundFile);
            System.out.println("AudioInputStream created successfully for: " + path);
            
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            System.out.println("Clip opened successfully for: " + path);
            
            return clip;
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio format for " + path + ": " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable for " + path + ": " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO error loading " + path + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error loading " + path + ": " + e.getMessage());
        }
        return null;
    }

    public static void playSound(Clip clip) {
        if (clip != null && MenuPanel.soundOn) {
            try {
                clip.setFramePosition(0);
                clip.start();
                System.out.println("Playing sound clip...");
            } catch (Exception e) {
                System.err.println("Error playing sound: " + e.getMessage());
            }
        }
    }

    public static void toggleSound(boolean enabled) {
        System.out.println("Toggling sound: " + enabled);
        if (backgroundMusic != null) {
            try {
                if (enabled) {
                    backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
                    System.out.println("Background music started");
                } else {
                    backgroundMusic.stop();
                    System.out.println("Background music stopped");
                }
            } catch (Exception e) {
                System.err.println("Error toggling background music: " + e.getMessage());
            }
        }
    }
}
