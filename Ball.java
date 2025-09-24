import java.awt.*;

public class Ball {
    int x, y, diameter;
    int dx, dy;
    int speed = 3;

    public Ball(int x, int y, int d, int dx, int dy) {
        this.x = x; this.y = y; this.diameter = d; 
        this.dx = dx; this.dy = dy;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, diameter, diameter);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, diameter, diameter);
    }

    public void reset(int nx, int ny) {
        x = nx; y = ny; dx = speed; dy = -speed;
    }
}
