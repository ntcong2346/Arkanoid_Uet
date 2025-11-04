package graphics;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;

public class Assets {
    public static Image background;
    public static Image backgroundMenu;
    public static Image backgroundSetting;
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
     * Power-up Image.
     */
    public static Image laserPowerUp;
    public static Image extraLifePowerUp;
    public static Image lifeDownPowerUp;
    public static Image widePaddlePowerUp;
    public static Image backgroundSettings;

    /**
     * Loads all game assets, including Images, from the resource files
     * and assigns them to the corresponding static variables.
     *
     * This method must be called once during application initialization.
     */
    public static void load() {
        try {
            backgroundMenu      = ImageIO.read(Assets.class.getResource("/assets/Background1.png"));
            backgroundSettings  =  ImageIO.read(Assets.class.getResource("/assets/Background3.png"));
            background          = ImageIO.read(Assets.class.getResource("/assets/Background2.png"));
            paddleNormal        = ImageIO.read(Assets.class.getResource("/assets/Player.png"));
            paddleGlow          = ImageIO.read(Assets.class.getResource("/assets/Player_flash.png"));
            ball                = ImageIO.read(Assets.class.getResource("/assets/Ball_small-blue.png"));

            brickNormal         = ImageIO.read(Assets.class.getResource("/assets/Brick1_4.png"));
            brickExplosive      = ImageIO.read(Assets.class.getResource("/assets/Brick7_4.png"));
            brickUnbreakable    = ImageIO.read(Assets.class.getResource("/assets/Brick_unbreakable2.png"));
            brickStrong3        = ImageIO.read(Assets.class.getResource("/assets/Brick5_4.png"));
            brickStrong2        = ImageIO.read(Assets.class.getResource("/assets/Brick6_4.png"));
            brickStrong1        = ImageIO.read(Assets.class.getResource("/assets/Brick9_4.png"));

            laserPowerUp        = ImageIO.read(Assets.class.getResource("/assets/laser.png"));
            extraLifePowerUp    = ImageIO.read(Assets.class.getResource("/assets/extralife.png"));
            lifeDownPowerUp     = ImageIO.read(Assets.class.getResource("/assets/debuff.png"));
            widePaddlePowerUp   = ImageIO.read(Assets.class.getResource("/assets/expand.png"));

        } catch (Exception e) {
            System.err.println("Error in Assets.load(): Can't read input file!");
            e.printStackTrace();
        }
    }
}
