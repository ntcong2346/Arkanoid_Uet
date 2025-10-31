package entity;

import java.awt.*;

public abstract class MovableObject extends GameObject {
    protected double dx, dy;

    public MovableObject(double x, double y, int width, int height) {
        super(x, y, width, height);
        this.dx = 0;
        this.dy = 0;
    }

    public void move() {
        x += dx;
        y += dy;
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
     * Calculate vector speed using Pythagorean theorem.
     */
    public double getSpeed() {
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Normalize function to scale back moving speed to avoid sum of vertical and horizontal positions
     * larger & faster than original speed value to maintain stable speed.
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