package entity;

import graphics.Assets;

import java.awt.*;
import java.util.Random;

public class Ball extends MovableObject {
    private final int radius;
    private final int speed;
    private boolean inMotion; // Is ball flying?

    public Ball(double x, double y, int r, int speed) {
        super(x, y, r * 2, r * 2);
        this.radius = r;
        this.speed = speed;
        this.inMotion = false;
        this.dx = 0;
        this.dy = 0;
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
            dx = rand.nextBoolean() ? 0.1 : -0.1; // Small dx value for less deviation.
            dy = -1; // Launch up
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

    public void reset(double nx, double ny) {
        x = nx;
        y = ny;
        dx = 0;
        dy = 0;
        inMotion = false;
    }
}
