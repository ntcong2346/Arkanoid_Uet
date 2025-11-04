package entity;

import powerup.PowerUp;
import powerup.PowerUpFactory;
import powerup.PowerUpManager;
import graphics.Assets;

import java.awt.*;
import java.util.ArrayList;
import java.io.Serializable;

public class Brick extends GameObject implements Serializable{
    // Các loại gạch
    public static final int NORMAL = 0;
    public static final int EXPLOSIVE = 1;
    public static final int STRONG = 2;
    public static final int UNBREAKABLE = 3;

    // Giá trị điểm (Thêm mới)
    private static final int SCORE_NORMAL = 10;
    private static final int SCORE_STRONG = 30;
    private static final int SCORE_EXPLOSIVE = 10;
    private static final int SCORE_UNBREAKABLE = 0;

    private final int type;
    private int hitPoints;
    private boolean destroyed;

    public Brick(int x, int y, int w, int h, int type) {
        super(x + w / 2.0, y + h / 2.0, w, h);
        this.type = type;
        this.destroyed = false;

        switch (type) {
            case NORMAL:
            case EXPLOSIVE:
                hitPoints = 1;
                break;
            case STRONG:
                hitPoints = 3; // Cần 3 lần
                break;
            case UNBREAKABLE:
                hitPoints = Integer.MAX_VALUE; // Không bao giờ vỡ
                break;
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public int getType() {
        return type;
    }

    public int getScoreValue() {
        return switch (type) {
            case NORMAL -> SCORE_NORMAL;
            case STRONG -> SCORE_STRONG;
            case EXPLOSIVE -> SCORE_EXPLOSIVE;
            case UNBREAKABLE -> SCORE_UNBREAKABLE;
            default -> 0;
        };
    }

    public int takeHit(ArrayList<Brick> bricks) {
        if (type == UNBREAKABLE || destroyed) {
            return 0;
        }
        hitPoints--;
        if (hitPoints <= 0) {
            destroyed = true;
            int totalScore = getScoreValue();
            if (type == EXPLOSIVE) {
                // Cộng điểm cho toàn bộ bricks bị phá hủy trong explosionZone
                totalScore += explode(bricks);
            }
            // Gọi hàm thả PowerUp khi chắc chắn gạch bị phá hủy
            dropPowerUp();

            return totalScore;
        }
        return 0;
    }

    private int explode(ArrayList<Brick> bricks) {
        int explosionScore = 0;
        // Tạo Vùng Nổ (3x3 lần kích thước gạch, lùi lại 1 lần width/height để bao quanh gạch đang nổ)
        Rectangle explosionZone = new Rectangle(
                (int)(this.x - this.width * 1.5),       // Góc X mới
                (int)(this.y - this.height * 1.5),      // Góc Y mới
                this.width * 3,            // Chiều rộng mới (gấp 3 lần)
                this.height * 3            // Chiều cao mới (gấp 3 lần)
        );

        for (Brick b : bricks) {
            // Kiểm tra nếu có gạch phá hủy được ở trong explosionScore để cộng điểm
            if (b != this && !b.destroyed && b.type != UNBREAKABLE && explosionZone.intersects(b.getRect())) {
                b.destroyed = true;
                explosionScore += b.getScoreValue();
            }
        }
        return explosionScore;
    }

    @Override
    public void update() {}

    @Override
    public void render(Graphics g) {
        if (destroyed) return;
        // Chuyển đổi từ tọa độ trung tâm trở lại tọa độ góc trên bên trái để kiểm tra va chạm
        int drawX = (int)getLeft();
        int drawY = (int)getTop();
        switch (type) {
            case NORMAL:
                g.drawImage(Assets.brickNormal, drawX, drawY, width, height, null);
                break;
            case EXPLOSIVE:
                g.drawImage(Assets.brickExplosive, drawX, drawY, width, height, null);
                break;
            case STRONG:
                if (hitPoints == 3) {
                    g.drawImage(Assets.brickStrong3, drawX, drawY, width, height, null);
                } else if (hitPoints == 2) {
                    g.drawImage(Assets.brickStrong2, drawX, drawY, width, height, null);
                } else if (hitPoints == 1) {
                    g.drawImage(Assets.brickStrong1, drawX, drawY, width, height, null);
                }
                break;
            case UNBREAKABLE:
                g.drawImage(Assets.brickUnbreakable, drawX, drawY, width, height, null);
                break;
        }
    }

    /**
     * 20% khả năng thả power-up ở trung tâm gạch bị phá hủy.
     */
    private void dropPowerUp() {
        if (Math.random() < 0.2) { // 20%
            // Tính toán vị trí của Power-up (tâm gạch)
            int powerUpX = (int) this.x;
            int powerUpY = (int) this.y;

            PowerUp powerUp = PowerUpFactory.createRandom(powerUpX, powerUpY);
            PowerUpManager.addPowerUp(powerUp);
        }
    }
}
