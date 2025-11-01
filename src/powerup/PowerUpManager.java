package powerup;

import entity.Paddle;
import game.CoopGamePanel;
import game.GamePanel;
import sound.SoundManager;

/**
 * Singleton manager for distributing power-ups to active game panels.
 */
public final class PowerUpManager {
    private static GamePanel singlePanel;
    private static CoopGamePanel coopPanel;

    private PowerUpManager() {}

    public static void setSinglePanel(GamePanel panel) {
        singlePanel = panel;
    }

    public static void setCoopPanel(CoopGamePanel panel) {
        coopPanel = panel;
    }

    public static void addPowerUp(PowerUp powerUp) {
        if (powerUp == null) {
            return;
        }
        if (singlePanel != null) {
            singlePanel.addPowerUp(powerUp);
        }
        if (coopPanel != null) {
            coopPanel.addPowerUp(powerUp);
        }
    }

    public static void addLife(int amount) {
        if (singlePanel != null) {
            singlePanel.addLife(amount);
        }
        if (coopPanel != null) {
            coopPanel.addLife(amount);
        }
    }

    /**
     * Shoots dual lasers from the left and right edges of the specified paddle.
     */
    public static void shootDualLasers(Paddle paddle) {
        if (paddle != null && paddle.isLaserActive()) {
            if (singlePanel != null) {
                int leftX = (int) paddle.getLeft();
                int rightX = (int) paddle.getRight();
                int paddleTop = (int) paddle.getTop();
                singlePanel.shootLaserFromPaddle(leftX, paddleTop);
                singlePanel.shootLaserFromPaddle(rightX, paddleTop);
                SoundManager.getInstance().play("laser");
            }
            if (coopPanel != null) {
                if (coopPanel.getPaddle1() == paddle) {
                    int leftX = (int) paddle.getLeft();
                    int rightX = (int) paddle.getRight();
                    int paddleTop = (int) paddle.getTop();
                    coopPanel.shootLaserFromPaddle(leftX, paddleTop);
                    coopPanel.shootLaserFromPaddle(rightX, paddleTop);
                    SoundManager.getInstance().play("laser");
                }
                if (coopPanel.getPaddle2() == paddle) {
                    int leftX = (int) paddle.getLeft();
                    int rightX = (int) paddle.getRight();
                    int paddleTop = (int) paddle.getTop();
                    coopPanel.shootLaserFromPaddle(leftX, paddleTop);
                    coopPanel.shootLaserFromPaddle(rightX, paddleTop);
                    SoundManager.getInstance().play("laser");
                }
            }
        }
    }
}