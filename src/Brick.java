import java.awt.*;
import java.util.ArrayList;

public class Brick {
    // Các loại gạch
    public static final int NORMAL = 0;
    public static final int EXPLOSIVE = 1;
    public static final int STRONG = 2;
    public static final int UNBREAKABLE = 3;

    private final int x, y, width, height;
    private final int type;
    private int hitPoints;
    private boolean destroyed;

    public Brick(int x, int y, int w, int h, int type) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
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

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isDestroyed() { return destroyed; }
    public int getType() { return type; }

    // Khi bóng chạm
    public void hit(ArrayList<Brick> bricks) {
        if (type == UNBREAKABLE || destroyed) return; // miễn nhiễm
        hitPoints--;
        if (hitPoints <= 0) {
            destroyed = true;
            if (type == EXPLOSIVE) {
                explode(bricks);
            }
        }
    }

    // Gạch nổ: phá gạch xung quanh
    private void explode(ArrayList<Brick> bricks) {
        Rectangle thisRect = getRect();
        for (Brick b : bricks) {
            if (!b.destroyed && b.type != UNBREAKABLE) {
                Rectangle bRect = b.getRect();
                if (thisRect.intersects(bRect)) {
                    b.destroyed = true;
                }
            }
        }
    }

    public void draw(Graphics g) {
        if (destroyed) return;
        switch (type) {
            case NORMAL:
                g.drawImage(Assets.brickNormal, x, y, width, height, null);
                break;
            case EXPLOSIVE:
                g.drawImage(Assets.brickExplosive, x, y, width, height, null);
                break;
            case STRONG:
                if (hitPoints == 3) {
                    g.drawImage(Assets.brickStrong3, x, y, width, height, null);
                } else if (hitPoints == 2) {
                    g.drawImage(Assets.brickStrong2, x, y, width, height, null);
                } else if (hitPoints == 1) {
                    g.drawImage(Assets.brickStrong1, x, y, width, height, null);
                }
                break;
            case UNBREAKABLE:
                g.drawImage(Assets.brickUnbreakable, x, y, width, height, null);
                break;
        }
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}
