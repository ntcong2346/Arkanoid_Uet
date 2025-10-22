package powerup;

import game.CoopGamePanel;
import game.GamePanel;

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

    public static void addLife() {
        if (singlePanel != null) {
            singlePanel.addLife(1);
        }
        if (coopPanel != null) {
            coopPanel.addLife(1);
        }
    }
}