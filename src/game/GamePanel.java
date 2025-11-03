package game;

import collision.CollisionInfo;
import entity.Laser;
import leaderboard.LeaderboardEntry;
import menu.MenuPanel;
import graphics.Assets;
import entity.Ball;
import entity.Paddle;
import entity.Brick;
import powerup.PowerUp;
import powerup.PowerUpManager;
import sound.SoundManager;
import leaderboard.LeaderboardManager;
import savegame.GameSaveData;

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

    // --- THÊM MỚI: Constructor dùng cho LOAD GAME ---
    /**
     * Constructor dùng để khởi tạo game từ dữ liệu đã lưu.
     * @param loadedData Đối tượng GameSaveData đã được tải.
     */
    public GamePanel(final GameSaveData loadedData) {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        loadGame(loadedData); // Tải game

        Timer timer = new Timer(10, this); // 100 fps
        timer.start();
        PowerUpManager.setSinglePanel(this);
    }

    /**
     * Khởi tạo game bằng dữ liệu đã lưu.
     * @param loadedData Đối tượng GameSaveData chứa trạng thái game đã lưu.
     */
    private void loadGame(final GameSaveData loadedData) {
        // Khôi phục trạng thái game
        level = loadedData.getLevel();
        score = loadedData.getScore();
        lives = loadedData.getLives();
        win = false;
        gameOver = false;

        // Áp dụng các cài đặt đã lưu (Sử dụng static variable của MenuPanel)
        MenuPanel.gameMode = loadedData.getGameMode();
        MenuPanel.ballSpeed = loadedData.getBallSpeed();
        MenuPanel.ballSize = loadedData.getBallSize();
        MenuPanel.paddleSpeed = loadedData.getPaddleSpeed();
        MenuPanel.playerName = loadedData.getPlayerName(); // Khôi phục tên

        // Khởi tạo các đối tượng với cài đặt đã lưu
        // Vị trí Paddle được khôi phục
        paddle = new Paddle(loadedData.getPaddle1X(), HEIGHT - 50, 120, 15, MenuPanel.paddleSpeed);
        paddle.setSpeed(MenuPanel.paddleSpeed);

        // Reset bóng về paddle, không cần save vị trí bóng.
        ball = new Ball(paddle.getX(), paddle.getTop() - MenuPanel.ballSize - 1, MenuPanel.ballSize, MenuPanel.ballSpeed);
        // Đảm bảo bóng PAUSED (vì ball.reset() làm điều đó)
        ball.reset(paddle.getX(), paddle.getTop() - MenuPanel.ballSize - 1);

        // Khôi phục trạng thái gạch
        bricks = loadedData.getBricksState();

        // Khởi tạo lại CollisionInfo với các đối tượng mới
        collisionInfo = new CollisionInfo(ball, paddle, bricks);

        // Power-ups và Lasers được reset khi load game (theo yêu cầu không cần save power up)
        powerUps.clear();
        lasers.clear();
    }

    /**
     * Tạo đối tượng GameSaveData từ trạng thái game hiện tại.
     * @return Đối tượng GameSaveData.
     */
    public final GameSaveData createSaveData() {
        // --- LỌC DANH SÁCH GẠCH ---
        final ArrayList<Brick> remainingBricks = new ArrayList<>();
        for (final Brick b : bricks) {
            if (!b.isDestroyed()) {
                remainingBricks.add(b);
            }
        }

        return new GameSaveData(
                MenuPanel.playerName,
                0, // Dùng giá trị cố định 0 (Single Player)
                level,
                lives,
                score,
                paddle.getX(),
                remainingBricks,
                MenuPanel.paddleSpeed,
                MenuPanel.ballSpeed,
                MenuPanel.ballSize
        );
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
            LeaderboardManager.getInstance().tryUpdateSinglePlayer(MenuPanel.playerName, score);

            paddle.updateGlow(); // Cập nhật hiệu ứng mỗi frame

            // Ball out of bounds
            if (ball.getY() > HEIGHT) {
                lives--;

                // TẮT LASER
                paddle.deactivateLaser();

                // TẮT WIDE PADDLE
                if (paddle.isWideActive()) {
                    paddle.deactivateWidePaddle();
                }

                lasers.clear();
                powerUps.clear();

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

                // TẮT LASER
                paddle.deactivateLaser();

                // TẮT WIDE PADDLE
                if (paddle.isWideActive()) {
                    paddle.deactivateWidePaddle();
                }

                lasers.clear();
                powerUps.clear();

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
        updatePowerUps();
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
        g.drawString("Level: " + level, WIDTH / 2 - 40, 40);

        // THAY ĐỔI CHÍNH: Score to Beat / High Score
        ArrayList<LeaderboardEntry> top = LeaderboardManager.getInstance().getTopSinglePlayerEntries();
        int scoreToBeat = LeaderboardManager.getInstance().getScoreToBeat(score, false);
        boolean isTop1 = !top.isEmpty() && score >= top.get(0).getSinglePlayerScore();

        if (isTop1) {
            g.setColor(Color.YELLOW);
            g.drawString("High Score: " + score, WIDTH / 2 - 90, 20);
        } else {
            g.setColor(Color.WHITE);
            g.drawString("Score to Beat: " + scoreToBeat, WIDTH / 2 - 85, 20);
        }

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

            // TẮT LASER
            paddle.deactivateLaser();

            // TẮT WIDE PADDLE
            if (paddle.isWideActive()) {
                paddle.deactivateWidePaddle();
            }
            lasers.clear();
            powerUps.clear();
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
            if (!gameOver){
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
                }
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
                    if(!gameOver){
                        score += brick.takeHit(bricks);
                    }
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
        powerUps.clear();
        lasers.clear();

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

    public final boolean isGameOver() {
        return gameOver;
    }

    public final boolean isBallInMotion() {
        return ball.isInMotion();
    }
}