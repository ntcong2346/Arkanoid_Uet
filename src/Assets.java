import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Assets {
    public static Image background;
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

    // gọi hàm này 1 lần khi khởi động game
    public static void load() {
        try {
            background = ImageIO.read(new File("assets/Background1.png"));
            paddleNormal = ImageIO.read(new File("assets/Player.png")); // paddle
            paddleGlow = ImageIO.read(new File("assets/Player_flash.png"));
            ball = ImageIO.read(new File("assets/Ball_small-blue.png"));
            brickNormal = ImageIO.read(new File("assets/Brick1_4.png"));
            brickExplosive = ImageIO.read(new File("assets/Brick7_4.png"));
            brickUnbreakable = ImageIO.read(new File("assets/Brick_unbreakable2.png"));
            brickStrong3 = ImageIO.read(new File("assets/Brick5_4.png")); // tím đậm (3 hit)
            brickStrong2 = ImageIO.read(new File("assets/Brick6_4.png")); // tím vừa (2 hit)
            brickStrong1 = ImageIO.read(new File("assets/Brick9_4.png")); // tím nhạt (1 hit)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
