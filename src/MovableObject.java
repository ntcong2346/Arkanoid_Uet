import java.awt.*;

public abstract class MovableObject extends GameObject {
    protected double dx, dy;

    public MovableObject(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.dx = 0;
        this.dy = 0;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    @Override
    public void update() {
        move();
    }

    public void invertX() {
        dx = -dx;
    }

    public void invertY() {
        dy = -dy;
    }

    /**
     * Calculate vector speed using Pythagorean theorem
     * @return
     */
    public double getSpeed() {
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    /**
     * Normalize function to scale back moving speed to avoid sum of vertical and horizontal positions
     * larger & faster than original speed value to maintain stable speed
     */
    public void normalize(double originalSpeed) {
        double length = getSpeed();
        if (length == 0) {
            return;
        }
        dx = (dx / length) * originalSpeed;
        dy = (dy / length) * originalSpeed;
    }

    @Override
    public abstract void render(Graphics g);
}
