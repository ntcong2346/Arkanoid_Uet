package powerup;

import entity.Paddle;
import graphics.Assets;

import java.awt.Graphics;

/**
 * Power-up that triggers the wide paddle effect.
 * The paddle itself manages the timing and state of the effect.
 */
public class WidePaddlePowerUp extends PowerUp {
    /** Duration of wide paddle effect in milliseconds. */
    private static final int DURATION_MS = 10000;
    private static final String TYPE = "wide";

    public WidePaddlePowerUp(int x, int y) {
        super(x, y, 100, 100, TYPE, DURATION_MS);
    }

    /**
     * Applies the effect by telling the paddle to activate its wide state.
     *
     * @param paddle the paddle to apply the effect to
     */
    @Override
    public void applyEffect(Paddle paddle) {
        paddle.activateWidePaddle();
    }

    /**
     * Removes the wide paddle effect from the paddle.
     *
     * @param paddle the paddle to remove the effect from
     */
    @Override
    public void removeEffect(Paddle paddle) {
        paddle.deactivateWidePaddle();
    }

    /**
     * Renders the power-up.
     *
     * @param g the graphics context
     */
    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.widePaddlePowerUp, (int) getLeft(), (int) getTop(), width, height, null);
    }
}