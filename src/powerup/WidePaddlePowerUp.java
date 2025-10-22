package powerup;

import entity.Paddle;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Power-up that temporarily doubles the paddle width for enhanced ball control.
 *
 * <p>When collected:
 * <ul>
 *   <li>Paddle width doubles (if not already active)</li>
 *   <li>Effect lasts 10 seconds</li>
 *   <li>Subsequent collections reset the timer (no stacking)</li>
 * </ul></p>
 *
 * <p>Visual: Cyan square with ➤ symbol</p>
 */
public class WidePaddlePowerUp extends PowerUp {
    /** Duration of wide paddle effect in milliseconds. */
    private static final int DURATION_MS = 10000;

    /** Unique type identifier for this power-up. */
    private static final String TYPE = "wide";

    /**
     * Constructs a WidePaddlePowerUp at the specified position.
     *
     * @param x x-coordinate of the power-up
     * @param y y-coordinate of the power-up
     */
    public WidePaddlePowerUp(int x, int y) {
        super(x, y, 25, 25, TYPE, DURATION_MS);
    }

    /**
     * Applies the wide paddle effect or resets the timer if already active.
     *
     * @param paddle the paddle to apply the effect to
     */
    @Override
    public void applyEffect(Paddle paddle) {
        if (!paddle.isWideActive()) {
            paddle.setWideActive(true, paddle.getWidth());
        }
        resetTimer(paddle);
    }

    /**
     * Removes the wide paddle effect from the paddle.
     *
     * @param paddle the paddle to remove the effect from
     */
    @Override
    public void removeEffect(Paddle paddle) {
        if (paddle.isWideActive()) {
            paddle.setWideActive(false, paddle.getOriginalWidth());
        }
    }

    /**
     * Renders the power-up with cyan background and white ➤ symbol.
     *
     * @param g the graphics context
     */
    @Override
    public void render(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect((int) x, (int) y, width, height);
        g.setColor(Color.WHITE);
        g.drawString("➤", (int) x + 6, (int) y + 16);
    }

    /**
     * Schedules automatic effect removal after duration expires.
     *
     * @param paddle the paddle to remove effect from
     */
    private void resetTimer(Paddle paddle) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (paddle.isWideActive()) {
                    removeEffect(paddle);
                }
            }
        }, duration);
    }
}