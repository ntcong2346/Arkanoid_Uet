package powerup;

import entity.Paddle;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Power-up that grants the player one additional life.
 *
 * <p>This power-up has no duration and immediately increases the player's life count
 * by 1 when collected.</p>
 */
public class ExtraLifePowerUp extends PowerUp {
    private static final String TYPE = "life";

    /**
     * Constructs an ExtraLifePowerUp at the specified position.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public ExtraLifePowerUp(int x, int y) {
        super(x, y, 100, 100, TYPE, 0);
    }

    /**
     * Adds one life to the player via PowerUpManager.
     *
     * @param paddle the collecting paddle (unused)
     */
    @Override
    public void applyEffect(Paddle paddle) {
        PowerUpManager.addLife(1);
    }

    @Override
    public void removeEffect(Paddle paddle) {
        // No effect to remove
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(graphics.Assets.extraLifePowerUp, (int) x, (int) y, width, height, null);
    }
}