package powerup;

import entity.Paddle;

import java.awt.Graphics;

/**
 * Power-up that reduces player lives when collected.
 * This power-up has no duration like ExtraLife power-up and immediately decreases the player's life count by 1
 */
public class LifeDownPowerUp extends PowerUp {
    private static final String TYPE = "life_down";

    public LifeDownPowerUp(int x, int y) {
        super(x, y, 100, 100, TYPE, 0);
    }

    @Override
    public void applyEffect(Paddle paddle) {
        PowerUpManager.addLife(-1);
    }

    @Override
    public void removeEffect(Paddle paddle) {
        // No effect to remove
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(graphics.Assets.lifeDownPowerUp, (int) getLeft(), (int) getTop(), width, height, null);
    }
}