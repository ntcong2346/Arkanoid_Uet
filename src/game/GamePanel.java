package game;

import collision.CollisionInfo;
import entity.Laser;
import menu.MenuPanel;
import graphics.Assets;
import entity.Ball;
import entity.Paddle;
import entity.Brick;
import powerup.PowerUp;
import powerup.PowerUpManager;
import sound.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Paddle paddle;
    private Ball ball;
    private ArrayList<Brick> bricks;
    private CollisionInfo collisionInfo;

    /** List of active power-ups falling from destroyed bricks. */
    private final List<PowerUp> powerUps = new ArrayList<>();
    private final List<Laser> lasers = new ArrayList<>();  // Thêm

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

        Timer timer = new Timer(10, this); // 100 fps
        timer.start();
        PowerUpManager.setSinglePanel(this);
    }

    private void initGame() {
        level = 1;
        score = 0;
        lives = 3;
        win = false;
        gameOver = false;

        paddle = new Paddle(WIDTH / 2.0, HEIGHT - 50, 120, 15, MenuPanel.paddleSpeed);
        paddle.setSpeed(MenuPanel.paddleSpeed);

        ball = new Ball(WIDTH / 2.0, HEIGHT - 70, MenuPanel.ballSize, MenuPanel.ballSpeed);

        bricks = new ArrayList<>();
        createLevel(level);
        collisionInfo = new CollisionInfo(ball, paddle, bricks);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (leftPressed)
                paddle.moveLeft();
            if (rightPressed)
                paddle.moveRight(WIDTH);
            // Ball in the paddle before launch
            if (!ball.isInMotion()) {
                ball.setX((int)paddle.getX());
                ball.setY((int)paddle.getTop() - ball.getRadius() - 1);
            }

            ball.move();
            
            // Collision detection & handler for ball, brick, paddle (add score based on collision)
            score += collisionInfo.updateCollision();

            paddle.updateGlow();// Cập nhật hiệu ứng mỗi frame
            updatePowerUps();
            
            // Ball out of bounds
            if (ball.getY() > HEIGHT) {
                lives--;
                checkGameOver();
                if (lives > 0) {
                    // reset ball position in the paddle
                    ball.reset((int)(paddle.getX() + paddle.getWidth() / 2.0), (int)(paddle.getY() - ball.getRadius() - 1));
                }
            }

            // Kiểm tra qua màn
            boolean allDestroyed = true;
            for (Brick b : bricks) {
                if (!b.isDestroyed() && b.getType() != Brick.UNBREAKABLE) {
                    allDestroyed = false;
                    break;
                }
            }
            if (allDestroyed) {
                level++;
                if (level > 5) {
                    gameOver = true;
                    win = true;
                    SoundManager.getInstance().play("game_win");
                } else {
                    createLevel(level);
                    ball.reset((int)paddle.getX() + paddle.getWidth() / 2.0,
                            (int)paddle.getY() - ball.getRadius() - 1);
                }
            }
        }
        updateLasers();  // Thêm
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Assets.background, 0, 0, WIDTH, HEIGHT, null);

        paddle.render(g);
        ball.render(g);

        for (Brick b : bricks) {
            if (!b.isDestroyed())
                b.render(g);
        }

        renderPowerUps(g);
        renderLasers(g);  // Thêm

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + lives, WIDTH - 100, 20);
        g.drawString("Level: " + level, WIDTH / 2 - 40, 20);

        if (!ball.isInMotion() && !gameOver) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Press SPACE to start", WIDTH/2 - 130, HEIGHT/2 + 100);
        }

        if (win) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("You Win!", WIDTH/2 - 180 , HEIGHT/2 - 100 );
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
        if (k == KeyEvent.VK_LEFT)
            leftPressed = true;
        if (k == KeyEvent.VK_RIGHT)
            rightPressed = true;
        if (k == KeyEvent.VK_SPACE)
            ball.launch(); // Press space to launch the ball
        if (k == KeyEvent.VK_R && gameOver) {
            initGame();
        }
        if (k == KeyEvent.VK_S) { // Change levels
            level++;
            score = 0;
            if (level > 5)
                level = 1;
            createLevel(level);
            ball.reset(paddle.getX() + paddle.getWidth() / 2.0, paddle.getY() - ball.getRadius() - 1);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT)
            leftPressed = false;
        if (k == KeyEvent.VK_RIGHT)
            rightPressed = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Adds a power-up to the active power-ups list.
     *
     * @param powerUp the power-up to add
     */
    public void addPowerUp(PowerUp powerUp) {
        if (powerUp != null) {
            powerUps.add(powerUp);
        }
    }

    /**
     * Updates all active power-ups: movement, collision detection, and removal.
     */
    private void updatePowerUps() {
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = powerUps.get(i);
            powerUp.update();

            if (powerUp.getBounds().intersects(paddle.getBounds())) {
                // Play power ups SFX
                switch (powerUp.getType()) {
                    case "wide" -> SoundManager.getInstance().play("extend_paddle");
                    case "life" -> SoundManager.getInstance().play("life_up");
                    case "life_down" -> SoundManager.getInstance().play("life_down");
                }

                powerUp.applyEffect(paddle);
                checkGameOver();
                powerUps.remove(i);
            } else if (!powerUp.isActive()) {
                powerUps.remove(i);
            }
        }
    }

    private void renderPowerUps(Graphics g) {
        for (PowerUp powerUp : powerUps) {
            powerUp.render(g);
        }
    }

    public void addLife(int amount) {
        lives += amount;
    }

    /**
     * Shoots a laser from the specified coordinates.
     */
    public void shootLaserFromPaddle(int x, int y) {
        if (paddle.isLaserActive()) {
            lasers.add(new Laser(x, y));
        }
    }

    /**
     * Updates all active lasers and handles collisions with bricks.
     */
    private void updateLasers() {
        for (int i = lasers.size() - 1; i >= 0; i--) {
            Laser laser = lasers.get(i);
            laser.update();
            if (!laser.isActive()) {
                lasers.remove(i);
                continue;
            }
            for (int j = bricks.size() - 1; j >= 0; j--) {
                Brick brick = bricks.get(j);
                if (!brick.isDestroyed() && laser.getBounds().intersects(brick.getRect())) {
                    score += brick.takeHit(bricks);
                    lasers.remove(i);
                    break;
                }
            }
        }
    }

    /**
     * Renders all active lasers.
     */
    private void renderLasers(Graphics g) {
        for (Laser laser : lasers) {
            laser.render(g);
        }
    }

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
                        if (r == 2 && c % 3 == 0)
                            type = Brick.UNBREAKABLE;
                        else
                            type = Brick.NORMAL;
                        break;

                    case 3:
                        // Thêm gạch EXPLOSIVE rải rác
                        if ((r + c) % 7 == 0)
                            type = Brick.EXPLOSIVE;
                        else
                            type = Brick.NORMAL;
                        break;

                    case 4:
                        // Hàng 1 và 3 là gạch STRONG
                        if (r == 1 || r == 3)
                            type = Brick.STRONG;
                        else
                            type = Brick.NORMAL;
                        break;

                    default: // Level 5 trở đi
                        if ((r + c) % 9 == 0)
                            type = Brick.UNBREAKABLE;
                        else if ((r + c) % 7 == 0)
                            type = Brick.EXPLOSIVE;
                        else if ((r + c) % 5 == 0)
                            type = Brick.STRONG;
                        else
                            type = Brick.NORMAL;
                        break;
                }

                bricks.add(new Brick(x, y, brickW - 2, brickH - 2, type));
            }
        }
    }
    private void checkGameOver() {
        if (lives <= 0) {
            gameOver = true;
            SoundManager.getInstance().play("game_over");
        }
    }
}