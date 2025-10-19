import java.awt.*;
import java.util.ArrayList;

public class Brick extends GameObject {
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
        super(x, y, w, h);
        this.type = type;
        this.destroyed = false;

        switch (type) {
            case NORMAL:
            case EXPLOSIVE:
                hitPoints = 1;
                break;
            case STRONG:
                hitPoints = 3; // cần 3 lần
                break;
            case UNBREAKABLE:
                hitPoints = Integer.MAX_VALUE; // không bao giờ vỡ
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
                // Add score from all bricks destroyed in the explosion zone
                totalScore += explode(bricks);
            }
            return totalScore;
        }
        return 0;
    }

    private int explode(ArrayList<Brick> bricks) {
        int explosionScore = 0;
        // Tạo Vùng Nổ (3x3 lần kích thước gạch, lùi lại 1 lần width/height để bao quanh gạch đang nổ)
        Rectangle explosionZone = new Rectangle(
                (int)this.x - this.width,       // Góc X mới
                (int)this.y - this.height,      // Góc Y mới
                this.width * 3,            // Chiều rộng mới (gấp 3 lần)
                this.height * 3            // Chiều cao mới (gấp 3 lần)
        );

        for (Brick b : bricks) {
            // Check if there are breakable brick inside the zone for additional score
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
        switch (type) {
            case NORMAL:
                g.drawImage(Assets.brickNormal, (int)x, (int)y, width, height, null);
                break;
            case EXPLOSIVE:
                g.drawImage(Assets.brickExplosive, (int)x, (int)y, width, height, null);
                break;
            case STRONG:
                if (hitPoints == 3) {
                    g.drawImage(Assets.brickStrong3, (int)x, (int)y, width, height, null);
                } else if (hitPoints == 2) {
                    g.drawImage(Assets.brickStrong2, (int)x, (int)y, width, height, null);
                } else if (hitPoints == 1) {
                    g.drawImage(Assets.brickStrong1, (int)x, (int)y, width, height, null);
                }
                break;
            case UNBREAKABLE:
                g.drawImage(Assets.brickUnbreakable, (int)x, (int)y, width, height, null);
                break;
        }
    }

    public Rectangle getRect() {
        return getBounds();
    }
}
