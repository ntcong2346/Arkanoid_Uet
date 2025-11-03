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

public class CoopGamePanel extends JPanel implements ActionListener, KeyListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private Paddle paddle1, paddle2;
    private Ball ball;
    private int paddleLaunchIndex = 0; // 0: paddle1, 1: paddle2
    private ArrayList<Brick> bricks;
    private CollisionInfo collisionInfo;

    /** List of active power-ups falling from destroyed bricks. */
    private final List<PowerUp> powerUps = new ArrayList<>();

    private final List<Laser> lasers = new ArrayList<>();  // Thêm

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

        Timer timer = new Timer(10, this);
        timer.start();
        PowerUpManager.setCoopPanel(this);
    }

    // --- THÊM MỚI: Constructor dùng cho LOAD GAME ---
    public CoopGamePanel(final GameSaveData loadedData) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        loadGame(loadedData); // Tải game

        Timer timer = new Timer(10, this);
        timer.start();
        PowerUpManager.setCoopPanel(this);
    }

    /**
     * Khởi tạo game bằng dữ liệu đã lưu.
     */
    private void loadGame(final GameSaveData loadedData) {
        // Khôi phục trạng thái game
        level = loadedData.getLevel();
        score = loadedData.getScore();
        lives = loadedData.getLives();
        win = false;
        gameOver = false;
        paddleLaunchIndex = 0; // Luôn dùng paddle 1 để launch khi load

        // Áp dụng các cài đặt đã lưu
        MenuPanel.gameMode = loadedData.getGameMode();
        MenuPanel.ballSpeed = loadedData.getBallSpeed();
        MenuPanel.ballSize = loadedData.getBallSize();
        MenuPanel.paddleSpeed = loadedData.getPaddleSpeed();
        MenuPanel.playerName = loadedData.getPlayerName();

        // Khởi tạo các đối tượng với cài đặt đã lưu
        paddle1 = new Paddle(loadedData.getPaddle1X(), HEIGHT - 50, 120, 15, MenuPanel.paddleSpeed);
        paddle2 = new Paddle(loadedData.getPaddle2X(), HEIGHT - 50, 120, 15, MenuPanel.paddleSpeed);
        paddle1.setSpeed(MenuPanel.paddleSpeed);
        paddle2.setSpeed(MenuPanel.paddleSpeed);

        // Reset bóng về paddle 1 (paddleLaunchIndex = 0)
        ball = new Ball(paddle1.getX(), paddle1.getTop() - MenuPanel.ballSize - 1, MenuPanel.ballSize, MenuPanel.ballSpeed);
        ball.reset(paddle1.getX(), paddle1.getTop() - MenuPanel.ballSize - 1); // Đảm bảo bóng PAUSED

        // Khôi phục trạng thái gạch
        bricks = loadedData.getBricksState();

        // Khởi tạo lại CollisionInfo với các đối tượng mới
        collisionInfo = new CollisionInfo(ball, paddle1, bricks);
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
                1, // <-- SỬA LỖI: Dùng giá trị cố định 1 (Co-op Player)
                level,
                lives,
                score,
                paddle1.getX(),
                paddle2.getX(),
                remainingBricks,
                MenuPanel.paddleSpeed,
                MenuPanel.ballSpeed,
                MenuPanel.ballSize
        );
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

            // Ball follows paddle before launch
            if (!ball.isInMotion()) {
                Paddle launchPad = (paddleLaunchIndex == 0) ? paddle1 : paddle2;
                ball.setX((int)launchPad.getX());
                ball.setY((int)launchPad.getTop() - ball.getRadius() - 1);
            }

            ball.move();

            // Collision detection & handler for ball, brick, paddle (coop mode)
            score += collisionInfo.updateCollisionCoop(paddle1, paddle2);
            LeaderboardManager.getInstance().tryUpdateCoop(MenuPanel.playerName, score);

            // Ball out of bounds
            if (ball.getY() > HEIGHT) {
                lives--;

                // TẮT LASER
                paddle1.deactivateLaser();
                paddle2.deactivateLaser();

                // TẮT WIDE PADDLE
                if (paddle1.isWideActive()) {
                    paddle1.deactivateWidePaddle();
                }
                if (paddle2.isWideActive()) {
                    paddle2.deactivateWidePaddle();
                }

                lasers.clear();
                powerUps.clear();

                checkGameOver();
                if (lives > 0){
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

                // TẮT LASER
                paddle1.deactivateLaser();
                paddle2.deactivateLaser();

                // TẮT WIDE PADDLE
                if (paddle1.isWideActive()) {
                    paddle1.deactivateWidePaddle();
                }
                if (paddle2.isWideActive()) {
                    paddle2.deactivateWidePaddle();
                }

                lasers.clear();
                powerUps.clear();

                if (level > 5) {
                    gameOver = true;
                    win = true;
                    SoundManager.getInstance().play("game_win");
                } else {
                    createLevel(level);
                    paddleLaunchIndex = 1 - paddleLaunchIndex;
                    ball.reset(0, 0);
                }
            }
        }
        updatePowerUps();
        updateLasers();
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
        renderLasers(g);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + lives, WIDTH - 100, 20);
        g.drawString("Level: " + level, WIDTH/2 - 40, 40);

        // THAY ĐỔI CHÍNH: Score to Beat / High Score
        ArrayList<LeaderboardEntry> top = LeaderboardManager.getInstance().getTopCoopEntries();
        int scoreToBeat = LeaderboardManager.getInstance().getScoreToBeat(score, true);
        boolean isTop1 = !top.isEmpty() && score >= top.get(0).getCoopScore();

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

            // TẮT LASER
            paddle1.deactivateLaser();
            paddle2.deactivateLaser();

            // TẮT WIDE PADDLE
            if (paddle1.isWideActive()) {
                paddle1.deactivateWidePaddle();
            }
            if (paddle2.isWideActive()) {
                paddle2.deactivateWidePaddle();
            }

            lasers.clear();
            powerUps.clear();
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

            if(!gameOver){
                Paddle hitPaddle = getHitPaddle(powerUp);
                if (hitPaddle != null) {
                    powerUp.applyEffect(hitPaddle);  // Áp dụng cho paddle bị va chạm
                    checkGameOver();
                    powerUps.remove(i);
                    continue;
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

    public Paddle getPaddle1() {
        return paddle1;
    }

    public Paddle getPaddle2() {
        return paddle2;
    }

    public void shootLaserFromPaddle(int x, int y) {
        if (paddle1.isLaserActive() || paddle2.isLaserActive()) {
            lasers.add(new Laser(x, y));
        }
    }

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

    private void renderLasers(Graphics g) {
        for (Laser laser : lasers) {
            laser.render(g);
        }
    }

    private void createLevel(int lvl) {
        bricks.clear();
        powerUps.clear();
        lasers.clear();

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