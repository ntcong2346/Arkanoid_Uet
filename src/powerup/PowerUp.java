package powerup;

import entity.GameObject;
import entity.Paddle;

import java.awt.Rectangle;

/**
 * Abstract base class for all power-ups that fall from destroyed bricks.
 */
public abstract class PowerUp extends GameObject {
    /** Default falling velocity in pixels per frame. */
    private static final int DEFAULT_VELOCITY_Y = 2;

    /** Power-up screen height limit before removal. */
    private static final int SCREEN_HEIGHT_LIMIT = 620;

    protected final String type;
    protected final int duration;

    protected PowerUp(int x, int y, int width, int height, String type, int duration) {
        super(x, y, width, height);
        this.type = type;
        this.duration = duration;
    }

    @Override
    public void update() {
        y += DEFAULT_VELOCITY_Y;
    }

    public String getType() {
        return type;
    }

    /**
     * Applies the power-up effect to the given paddle.
     */
    public abstract void applyEffect(Paddle paddle);

    /**
     * Removes the power-up effect from the given paddle.
     */
    public abstract void removeEffect(Paddle paddle);

    public boolean isActive() {
        return y < SCREEN_HEIGHT_LIMIT;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }
}