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
        g.setColor(Color.CYAN);
        g.fillRect(x, y, width, height);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}
