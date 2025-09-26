import java.awt.*;

public class Paddle {
    int x, y, width, height;
    int speed = 6;

    private boolean glowing = false;
    private int glowTimer = 0;
    private static final int GLOW_DURATION = 15; // số frame phát sáng (~0.15s nếu 100fps)

    public Paddle(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
    }

    public void moveLeft() {
        x -= speed;
        if (x < 0)
            x = 0;
    }

    public void moveRight(int panelWidth) {
        x += speed;
        if (x + width > panelWidth)
            x = panelWidth - width;
    }

    public void setGlow() {
        glowing = true;
        glowTimer = GLOW_DURATION;
    }

    public void updateGlow() {
        if (glowing) {
            glowTimer--;
            if (glowTimer <= 0) glowing = false;
        }
    }

    public void draw(Graphics g) {
        if (glowing) {
            g.drawImage(Assets.paddleGlow, x, y, width, height, null);
        } else {
            g.drawImage(Assets.paddleNormal, x, y, width, height, null);
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public void reset(int i) {
        // Nếu cần reset vị trí paddle, thêm code ở đây
        // Hiện tại để trống
    }
}
