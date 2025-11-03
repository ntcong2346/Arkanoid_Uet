package entity;

import java.awt.*;
import java.io.Serializable;

public abstract class GameObject implements Serializable{
    protected double x;
    protected double y;
    protected int width;
    protected int height;

    public GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update();

    public abstract void render(Graphics g);

    /**
     * Center (x,y) coordinates.
     */
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getLeft() {
        return x - width / 2.0;
    }

    public double getTop() {
        return y - height / 2.0;
    }

    public double getRight() {
        return x + width / 2.0;
    }

    public double getBottom() {
        return y + height / 2.0;
    }

    /**
     * Create new Rectangle with (x,y) as top left (JavaFx shape's default coordinates).
     */
    public Rectangle getRect() {
        return new Rectangle(
                (int)getLeft(),
                (int)getTop(),
                width,
                height
        );
    }

    public boolean intersects(GameObject other) {
        return getRect().intersects(other.getRect());
    }
}
