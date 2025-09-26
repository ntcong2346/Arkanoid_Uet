import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Timer timer;
    private Paddle paddle;
    private Ball ball;
    private ArrayList<Brick> bricks;
    private boolean leftPressed = false, rightPressed = false;
    private int score = 0, lives = 3;
    private boolean gameOver = false;
    private boolean win = false;
    private int level = 1; // Thêm biến level

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        initGame();

        timer = new Timer(10, this); // 100 fps
        timer.start();
    }

    private void initGame() {
        level = 1;
        score = 0;
        lives = 3;
        win = false;
        gameOver = false;
        paddle = new Paddle(WIDTH/2 - 60, HEIGHT - 50, 120, 15);
        ball = new Ball(WIDTH/2, HEIGHT - 70, 12, 8, 8);
        bricks = new ArrayList<>();
        createLevel(level);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (leftPressed) paddle.moveLeft();
            if (rightPressed) paddle.moveRight(WIDTH);
            // ball in the paddle
            if (!ball.inMotion) {
                ball.x = paddle.x + paddle.width/2 - ball.diameter/2;
                ball.y = paddle.y - ball.diameter - 1;
            }

            ball.move();

            // ball vs walls
            if (ball.x <= 0 || ball.x + ball.diameter >= WIDTH) ball.dx *= -1;
            if (ball.y <= 0) ball.dy *= -1;

            // ball vs paddle
            if (ball.getRect().intersects(paddle.getRect())) {
                ball.dy *= -1;
            }

            // ball vs bricks
           for (Brick b : bricks) {
    if (!b.destroyed && ball.getRect().intersects(b.getRect())) {
        Rectangle br = b.getRect();
        Rectangle bl = ball.getRect();

        // Gọi hit để xử lý logic gạch
        b.hit(bricks);
        if (b.destroyed && b.type != Brick.UNBREAKABLE) {
            score += 100;
        }

        // Xác định hướng va chạm
        double overlapLeft   = bl.getMaxX() - br.getMinX();
        double overlapRight  = br.getMaxX() - bl.getMinX();
        double overlapTop    = bl.getMaxY() - br.getMinY();
        double overlapBottom = br.getMaxY() - bl.getMinY();

        // Tìm cạnh có overlap nhỏ nhất → đó là cạnh va chạm
        double minOverlapX = Math.min(overlapLeft, overlapRight);
        double minOverlapY = Math.min(overlapTop, overlapBottom);

        if (minOverlapX < minOverlapY) {
            // Va chạm ngang → đảo dx
            ball.dx = -ball.dx;
            if (overlapLeft < overlapRight) {
                ball.x = br.x - ball.diameter; // đẩy bóng ra bên trái
            } else {
                ball.x = br.x + br.width;      // đẩy bóng ra bên phải
            }
        } else {
            // Va chạm dọc → đảo dy
            ball.dy = -ball.dy;
            if (overlapTop < overlapBottom) {
                ball.y = br.y - ball.diameter; // đẩy bóng lên trên
            } else {
                ball.y = br.y + br.height;     // đẩy bóng xuống dưới
            }
        }

        break; // chỉ xử lý 1 gạch mỗi frame
    }
}


            // ball out of bounds
            if (ball.y > HEIGHT) {
                lives--;
                if (lives <= 0) {
                    gameOver = true;
                } else {
                    // reset ball position in the paddle
                    ball.reset(paddle.x + paddle.width/2 - ball.diameter/2, paddle.y - ball.diameter - 2);
                }
            }

            // Kiểm tra qua màn
            boolean allDestroyed = true;
            for (Brick b : bricks) {
                if (!b.destroyed && b.type != Brick.UNBREAKABLE) {
                    allDestroyed = false;
                    break;
                }
            }
            if (allDestroyed) {
                level++;
                if (level > 5) {
                    gameOver = true;
                    win = true;
                } else {
                    createLevel(level);
                    ball.reset(paddle.x + paddle.width/2 - ball.diameter/2,
                               paddle.y - ball.diameter - 2);
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Assets.background, 0, 0, WIDTH, HEIGHT, null);
        paddle.draw(g);
        ball.draw(g);

        for (Brick b : bricks) {
            if (!b.destroyed) b.draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + lives, WIDTH - 100, 20);
        g.drawString("Level: " + level, WIDTH/2 - 40, 20);

        if (win) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Bạn đã chiến thắng!", WIDTH/2 - 180 , HEIGHT/2 - 100 );
        }

        if (gameOver && !win) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Game Over! Press R to Restart", 120, HEIGHT/2);
        }
        if (gameOver && win) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Press R to Restart", 235, HEIGHT/2 + 50);
        }
    }

    // KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT) leftPressed = true;
        if (k == KeyEvent.VK_RIGHT) rightPressed = true;
        if (k == KeyEvent.VK_SPACE) ball.launch(); // press space to launch the ball
        if (k == KeyEvent.VK_R && gameOver) {
            initGame();
        }
        if (k == KeyEvent.VK_S) { // Cheat: chuyển vòng
            level++;
            if (level > 5) level = 1;
            createLevel(level);
            ball.reset(paddle.x + paddle.width/2 - ball.diameter/2, paddle.y - ball.diameter - 2);
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT) leftPressed = false;
        if (k == KeyEvent.VK_RIGHT) rightPressed = false;
    }
    @Override
    public void keyTyped(KeyEvent e) {}

    private void createLevel(int lvl) {
    bricks.clear();

    int rows = 6;
    int cols = 12;
    int brickW = 60, brickH = 20;
    int offsetX = (WIDTH - cols * brickW) / 2;
    int offsetY = 60;

    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            int x = offsetX + c * brickW;
            int y = offsetY + r * brickH;

            int type = Brick.NORMAL;

            switch (lvl) {
                case 1:
                    // Toàn bộ là gạch thường
                    type = Brick.NORMAL;
                    break;

                case 2:
                    // Thêm gạch UNBREAKABLE ở hàng giữa
                    if (r == 2 && c % 3 == 0) type = Brick.UNBREAKABLE;
                    else type = Brick.NORMAL;
                    break;

                case 3:
                    // Thêm gạch EXPLOSIVE rải rác
                    if ((r + c) % 7 == 0) type = Brick.EXPLOSIVE;
                    else type = Brick.NORMAL;
                    break;

                case 4:
                    // Hàng 1 và 3 là gạch STRONG
                    if (r == 1 || r == 3) type = Brick.STRONG;
                    else type = Brick.NORMAL;
                    break;

                default: // Level 5 trở đi
                    if ((r + c) % 9 == 0) type = Brick.UNBREAKABLE;
                    else if ((r + c) % 7 == 0) type = Brick.EXPLOSIVE;
                    else if ((r + c) % 5 == 0) type = Brick.STRONG;
                    else type = Brick.NORMAL;
                    break;
            }

            bricks.add(new Brick(x, y, brickW - 2, brickH - 2, type));
        }
    }
}
}