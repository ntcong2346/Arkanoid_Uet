package game;

import collision.CollisionInfo;
import menu.MenuPanel;
import graphics.Assets;
import entity.Ball;
import entity.Paddle;
import entity.Brick;
import powerup.PowerUp;
import powerup.PowerUpManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class CoopGamePanel extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Timer timer;
    private Paddle paddle1, paddle2;
    private Ball ball;
    private int paddleLaunchIndex = 0; // 0: paddle1, 1: paddle2
    private ArrayList<Brick> bricks;
    private CollisionInfo collisionInfo;

    /** List of active power-ups falling from destroyed bricks. */
    private final List<PowerUp> powerUps = new ArrayList<>();

    private boolean leftPressed = false, rightPressed = false;
    private boolean aPressed = false, dPressed = false;
    private int score = 0, lives = 3;
    private boolean gameOver = false;
    private boolean win = false;
    private int level = 1;

    public CoopGamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        initGame();

        timer = new Timer(10, this);
        timer.start();
        PowerUpManager.setCoopPanel(this);
    }

    private void initGame() {
        score = 0;
        lives = 3;
        win = false;
        gameOver = false;
        level = 1;
        paddle2 = new Paddle(WIDTH / 4.0, HEIGHT - 50, 120, 15, MenuPanel.paddleSpeed);
        paddle1 = new Paddle(WIDTH * 3 / 4.0, HEIGHT - 50, 120, 15, MenuPanel.paddleSpeed);
        paddle1.setSpeed(MenuPanel.paddleSpeed);
        paddle2.setSpeed(MenuPanel.paddleSpeed);
        ball = new Ball(0, 0, MenuPanel.ballSize, MenuPanel.ballSpeed);
        paddleLaunchIndex = 0;
        bricks = new ArrayList<>();
        createLevel(level);
        collisionInfo = new CollisionInfo(ball, paddle1, bricks);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Paddle movement
            if (leftPressed) paddle1.moveLeft();
            if (rightPressed) paddle1.moveRight(WIDTH);
            if (aPressed) paddle2.moveLeft();
            if (dPressed) paddle2.moveRight(WIDTH);

            paddle1.updateGlow();
            paddle2.updateGlow();
            updatePowerUps();

            // Ball follows paddle before launch
            if (ball.isInMotion()) {
                Paddle launchPad = (paddleLaunchIndex == 0) ? paddle1 : paddle2;
                ball.setX((int)launchPad.getX() + launchPad.getWidth() / 2);
                ball.setY((int)launchPad.getY() - ball.getRadius() - 1);
            }

            ball.move();

            // Collision detection & handler for ball, brick, paddle (coop mode)
            score += collisionInfo.updateCollisionCoop(paddle1, paddle2);

            // Ball out of bounds
            if (ball.getY() > HEIGHT) {
                lives--;
                if (lives <= 0) {
                    gameOver = true;
                } else {
                    paddleLaunchIndex = 1 - paddleLaunchIndex; // Đổi paddle launch
                    ball.reset(0, 0); // Vị trí sẽ được cập nhật ở trên
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
                } else {
                    createLevel(level);
                    paddleLaunchIndex = 1 - paddleLaunchIndex;
                    ball.reset(0, 0);
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Assets.background, 0, 0, WIDTH, HEIGHT, null);
        paddle1.render(g);
        paddle2.render(g);
        ball.render(g);

        for (Brick b : bricks) {
            if (!b.isDestroyed()) b.render(g);
        }

        renderPowerUps(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + lives, WIDTH - 100, 20);
        g.drawString("Level: " + level, WIDTH/2 - 40, 20);

        if (!ball.isInMotion() && !gameOver) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Bấm SPACE để bắt đầu", WIDTH/2 - 130, HEIGHT/2 + 100);
        }

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
        if (k == KeyEvent.VK_A) aPressed = true;
        if (k == KeyEvent.VK_D) dPressed = true;
        if (k == KeyEvent.VK_SPACE) ball.launch();
        if (k == KeyEvent.VK_R && gameOver) initGame();
        if (k == KeyEvent.VK_S) { // Cheat: chuyển vòng
            level++;
            score = 0;
            if (level > 5)
                level = 1;
            createLevel(level);
            ball.reset(paddle1.getX() + paddle1.getWidth() / 2.0 - ball.getDiameter() / 2.0, paddle1.getY() - ball.getDiameter() - 2);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT) leftPressed = false;
        if (k == KeyEvent.VK_RIGHT) rightPressed = false;
        if (k == KeyEvent.VK_A) aPressed = false;
        if (k == KeyEvent.VK_D) dPressed = false;
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

    public void addLife(int amount) {
        lives += amount;
    }

    /**
     * Updates all active power-ups and handles paddle collisions.
     */
    private void updatePowerUps() {
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp powerUp = powerUps.get(i);
            powerUp.update();

            Paddle hitPaddle = getHitPaddle(powerUp);
            if (hitPaddle != null) {
                powerUp.applyEffect(hitPaddle);
                powerUps.remove(i);
                continue;
            }

            if (!powerUp.isActive()) {
                powerUps.remove(i);
            }
        }
    }

    private void renderPowerUps(Graphics g) {
        for (PowerUp powerUp : powerUps) {
            powerUp.render(g);
        }
    }

    /**
     * Returns the paddle that the power-up collided with, or null if none.
     *
     * @param powerUp the power-up to check
     * @return the hit paddle, or null
     */
    private Paddle getHitPaddle(PowerUp powerUp) {
        if (powerUp.getBounds().intersects(paddle1.getBounds())) {
            return paddle1;
        }
        if (powerUp.getBounds().intersects(paddle2.getBounds())) {
            return paddle2;
        }
        return null;
    }

    private void createLevel(int lvl) {
        bricks.clear();
        int rows = 5, cols = 10;
        int brickW = 70, brickH = 20;
        int offsetX = (WIDTH - cols * brickW) / 2;
        int offsetY = 50;

        switch (lvl) {
            case 1:
                for (int r = 0; r < rows; r++)
                    for (int c = 0; c < cols; c++)
                        bricks.add(new Brick(offsetX + c * brickW, offsetY + r * brickH, brickW - 2, brickH - 2, Brick.NORMAL));
                break;
            case 2:
                for (int r = 0; r < rows; r++)
                    for (int c = 0; c < cols; c++) {
                        int type = (r == 2 && c >= 3 && c <= 6) ? Brick.UNBREAKABLE : Brick.NORMAL;
                        bricks.add(new Brick(offsetX + c * brickW, offsetY + r * brickH, brickW - 2, brickH - 2, type));
                    }
                break;
            case 3:
                for (int r = 0; r < rows; r++)
                    for (int c = 0; c < cols; c++) {
                        int type = ( (r == 0 && (c == 0 || c == cols-1)) || (r == rows-1 && (c == 0 || c == cols-1)) )
                            ? Brick.EXPLOSIVE : Brick.NORMAL;
                        bricks.add(new Brick(offsetX + c * brickW, offsetY + r * brickH, brickW - 2, brickH - 2, type));
                    }
                break;
            case 4:
                for (int r = 0; r < rows; r++)
                    for (int c = 0; c < cols; c++) {
                        int type = (r == 0) ? Brick.STRONG : Brick.NORMAL;
                        bricks.add(new Brick(offsetX + c * brickW, offsetY + r * brickH, brickW - 2, brickH - 2, type));
                    }
                break;
            case 5:
                for (int r = 0; r < rows; r++)
                    for (int c = 0; c < cols; c++) {
                        int type;
                        if (r == 0) type = Brick.STRONG;
                        else if (r == 2 && c % 2 == 0) type = Brick.UNBREAKABLE;
                        else if ((r == 1 || r == 3) && (c == 2 || c == 7)) type = Brick.EXPLOSIVE;
                        else type = Brick.NORMAL;
                        bricks.add(new Brick(offsetX + c * brickW, offsetY + r * brickH, brickW - 2, brickH - 2, type));
                    }
                break;
            default:
                createLevel(1);
                break;
        }
    }
}