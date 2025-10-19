import java.awt.*;
import java.awt.event.KeyEvent;

public class Paddle extends MovableObject {
    private int speed; //Overall speed
    private boolean glowing;
    private int glowTimer;// số frame phát sáng (~0.15s nếu 100fps)

    private boolean leftPressed;
    private boolean rightPressed;

    public Paddle(double x, double y, int width, int height, int speed) {
        super(x, y, width, height);
        this.speed = speed;
        this.glowing = false;
        this.glowTimer = 0;
        this.leftPressed = false;
        this.rightPressed = false;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void moveLeft() {
        x -= speed;
        if (x < 0) {
            x = 0;
        }
    }

    public void moveRight(int panelWidth) {
        x += speed;
        if (x + width > panelWidth)
            x = panelWidth - width;
    }

    public void setGlow() {
        glowing = true;
        glowTimer = 15;
    }

    public void updateGlow() {
        if (glowing) {
            glowTimer--;
            if (glowTimer <= 0) glowing = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) leftPressed = true;
        if (key == KeyEvent.VK_RIGHT) rightPressed = true;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) leftPressed = false;
        if (key == KeyEvent.VK_RIGHT) rightPressed = false;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    @Override
    public void update() {
        updateGlow();
    }

    @Override
    public void render(Graphics g) {
        if (glowing) {
            g.drawImage(Assets.paddleGlow, (int)x, (int)y, width, height, null);
        } else {
            g.drawImage(Assets.paddleNormal, (int)x, (int)y, width, height, null);
        }
    }

    public Rectangle getRect() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    public void reset(double nx, double ny) {
        // Nếu cần reset vị trí paddle, thêm code ở đây
        // Hiện tại để trống
    }
}
