import java.awt.*;

public class Brick {
    int x, y, width, height;
    boolean destroyed = false;

    public Brick(int x, int y, int w, int h) {
        this.x = x; this.y = y; this.width = w; this.height = h;
    }

    public void draw(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}
