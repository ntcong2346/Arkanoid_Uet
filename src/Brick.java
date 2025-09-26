import java.awt.*;
import java.util.ArrayList;

public class Brick {
    // Các loại gạch
    public static final int NORMAL = 0;
    public static final int EXPLOSIVE = 1;
    public static final int STRONG = 2;
    public static final int UNBREAKABLE = 3;

    int x, y, width, height;
    int type;
    int hitPoints;
    boolean destroyed = false;

    public Brick(int x, int y, int w, int h, int type) {
        this.x = x; this.y = y; this.width = w; this.height = h;
        this.type = type;

        switch (type) {
            case NORMAL: 
                hitPoints = 1; 
                break;
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

    // Khi bóng chạm
    public void hit(ArrayList<Brick> bricks) {
        if (type == UNBREAKABLE) return; // miễn nhiễm
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
        for (Brick b : bricks) {
            if (!b.destroyed && b.type != UNBREAKABLE) {
                double dx = Math.abs(b.x - this.x);
                double dy = Math.abs(b.y - this.y);
                if (dx <= width * 1.5 && dy <= height * 1.5) {
                    b.destroyed = true;
                }
            }
        }
    }

    public void draw(Graphics g) {
        if (destroyed) return;

        switch (type) {
            case NORMAL:
                g.setColor(Color.ORANGE);
                break;
            case EXPLOSIVE:
                g.setColor(Color.RED);
                break;
            case STRONG:
                // Đổi màu theo số lần bị đánh
                if (hitPoints == 3) g.setColor(new Color(128, 0, 128)); // tím đậm
                else if (hitPoints == 2) g.setColor(new Color(180, 90, 180)); // tím nhạt hơn
                else if (hitPoints == 1) g.setColor(new Color(220, 180, 220)); // tím rất nhạt
                break;
            case UNBREAKABLE:
                g.setColor(Color.GRAY);
                break;
        }

        g.fillRect(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, width, height);
    }
}
