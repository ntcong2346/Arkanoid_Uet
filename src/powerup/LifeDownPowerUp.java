package powerup;

import entity.Paddle;

import java.awt.Graphics;

/**
 * Power-up that reduces player lives when collected.
 */
public class LifeDownPowerUp extends PowerUp {
    private static final int DURATION_MS = 0;  // Instant effect
    private static final String TYPE = "life_down";

    public LifeDownPowerUp(int x, int y) {
        super(x, y, 100, 100, TYPE, DURATION_MS);
    }

    @Override
    public void applyEffect(Paddle paddle) {
        // Không dùng paddle mà dùng PowerUpManager để giảm lives
        PowerUpManager.addLife(-1);  // Giảm 1 mạng
    }

    @Override
    public void removeEffect(Paddle paddle) {
        // Không cần remove vì hiệu ứng tức thời
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(graphics.Assets.lifeDownPowerUp, (int) x, (int) y, width, height, null);
    }
}