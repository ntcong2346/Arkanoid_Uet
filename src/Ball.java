import java.awt.*;
import java.util.Random;

public class Ball {
    private double x, y; //Position of ball
    private Velocity velocity; //Speed & direction by axis of ball
    private final int radius;
    private final int speed;
    private boolean inMotion; //Is ball flying?

    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    public double getDX() { return velocity.getDx(); }
    public double getDY() { return velocity.getDy(); }
    public void setDX(double dx) { velocity.setDx(dx); }
    public void setDY(double dy) { velocity.setDy(dy); }

    public int getRadius() { return radius; }
    public int getDiameter() { return radius * 2; }
    public int getSpeed() { return (int) velocity.getSpeed(); }
    public boolean isInMotion() { return inMotion; }

    public Ball(int x, int y, int r, int speed) {
        this.x = x;
        this.y = y;
        this.radius = r;
        this.speed = speed; // Gán giá trị speed từ tham số
        this.velocity = new Velocity(0, 0);
        this.inMotion = false;
    }

    public void launch() {
        Random rand = new Random();
        if (!inMotion) {
            inMotion = true;
            double dx = rand.nextBoolean() ? 1 : -1;
            double dy = -1; // bay len
            velocity = new Velocity(dx, dy);
            velocity.normalize(speed);
        }
    }

    public void move() {
        if (inMotion) {
            x += velocity.getDx();
            y += velocity.getDy();
        }
    }

    public void bounceHorizontal() {
        velocity.invertX();
    }

    public void bounceVertical() {
        velocity.invertY();
    }

    public void draw(Graphics g) {
        g.drawImage(Assets.ball, (int)(x - radius), (int)(y - radius), 2 * radius, 2 * radius, null);
    }

    public Rectangle getRect() {
        return new Rectangle((int) (x - radius), (int) (y - radius), 2 * radius, 2 * radius);
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

    public void reset(int nx, int ny) {
        x = nx;
        y = ny;
        velocity = new Velocity(0, 0);
        inMotion = false;
    }
}
