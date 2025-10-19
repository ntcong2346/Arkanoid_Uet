import java.awt.*;
import java.util.Random;

public class Ball extends MovableObject {
    private final int radius;
    private final int speed;
    private boolean inMotion; //Is ball flying?

    public Ball(double x, double y, int r, int speed) {
        super(x, y, r * 2, r * 2);
        this.radius = r;
        this.speed = speed;
        this.inMotion = false;
        this.dx = 0;
        this.dy = 0;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public int getDiameter() {
        return radius * 2;
    }

    public boolean isInMotion() {
        return inMotion;
    }

    public void launch() {
        if (!inMotion) {
            inMotion = true;
            Random rand = new Random();
            dx = rand.nextBoolean() ? 1 : -1;
            dy = -1; // bay len
            normalize(speed);
        }
    }

    public void move() {
        if (inMotion) {
            x += dx;
            y += dy;
        }
    }

    public void bounceHorizontal() {
        invertX();
    }

    public void bounceVertical() {
        invertY();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.ball, (int)(x - radius), (int)(y - radius), 2 * radius, 2 * radius, null);
    }

    public Rectangle getRect() {
        return new Rectangle((int)(x - radius), (int)(y - radius), 2 * radius, 2 * radius);
    }

    public boolean hitsPaddle(Paddle paddle) {
        Rectangle ballRect = getRect();
        Rectangle paddleRect = paddle.getRect();
        return ballRect.intersects(paddleRect);
    }

    public boolean hitsBrick(Brick brick) {
        Rectangle ballRect = getRect();
        Rectangle brickRect = brick.getRect();
        return ballRect.intersects(brickRect);
    }

    public void reset(double nx, double ny) {
        x = nx;
        y = ny;
        dx = 0;
        dy = 0;
        inMotion = false;
    }
}
