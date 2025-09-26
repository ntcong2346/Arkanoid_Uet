import java.awt.*;
import java.util.Random;

public class Ball {
    double x, y;
    int diameter;
    int dx, dy;
    int speed = 3;
    boolean inMotion = false;

    public Ball(int x, int y, int d, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.diameter = d;
        this.dx = 0;
        this.dy = 0;
    }

    public void move() {
        if (inMotion) {
            x += dx;
            y += dy;
        }
    }

    public void launch() {
        Random rand = new Random();
        if (!inMotion) {
            inMotion = true;
            dx = rand.nextBoolean() ? speed : -speed;
            dy = -speed; // bay len
        }
    }

    public void draw(Graphics g) {
        g.drawImage(Assets.ball, (int) x, (int) y, diameter, diameter, null);
    }

    public Rectangle getRect() {
        return new Rectangle((int) x, (int) y, diameter, diameter);
    }

    public void reset(int nx, int ny) {
        x = nx;
        y = ny;
        dx = 0;
        dy = 0;
        inMotion = false;
    }
}
