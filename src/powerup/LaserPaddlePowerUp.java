package powerup;

import entity.Paddle;
import graphics.Assets;

import java.awt.Graphics;

/**
 * Power-up that triggers the laser effect on the paddle.
 * The paddle itself manages the timing and state of the effect.
 */
public class LaserPaddlePowerUp extends PowerUp {
    /** Duration of laser power-up in milliseconds. */
    private static final int DURATION_MS = 3000;

    /** Type identifier for this power-up. */
    private static final String TYPE = "laser";

    public LaserPaddlePowerUp(int x, int y) {
        super(x, y, 100, 100, TYPE, DURATION_MS);
    }

    /**
     * Applies the effect by telling the paddle to activate its laser logic.
     * @param paddle the paddle to apply the effect to
     */
    @Override
    public void applyEffect(Paddle paddle) {
        paddle.activateLaser();
    }

    /**
     * Removes the effect by telling the paddle to deactivate its laser logic.
     * @param paddle the paddle to remove the effect from
     */
    @Override
    public void removeEffect(Paddle paddle) {
        paddle.deactivateLaser();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.laserPowerUp, (int) getLeft(), (int) getTop(), width, height, null);
    }
}