import java.awt.*;

public class Paddle {
    int x, y, width, height;
    int speed = 6;

    public Paddle(int x, int y, int w, int h) {
        this.x = x; this.y = y; this.width = w; this.height = h;
    }

    public void moveLeft() { x -= speed; if (x < 0) x = 0; }
    public void moveRight(int panelWidth) { 
        x += speed; 
        if (x + width > panelWidth) x = panelWidth - width; 
    }

    public void draw(Graphics g) {
        // Sử dụng paddleNormal để đảm bảo luôn hiện ra
        g.drawImage(Assets.paddleNormal, x, y, width, height, null);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }

    public void reset(int i) {
        // Nếu cần reset vị trí paddle, thêm code ở đây
        // Hiện tại để trống
    }
}
