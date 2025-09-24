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
        paddle = new Paddle(WIDTH/2 - 60, HEIGHT - 50, 120, 15);
        ball = new Ball(WIDTH/2, HEIGHT - 70, 10, 4, 4);
        bricks = new ArrayList<>();

        int rows = 5, cols = 10;
        int brickW = 70, brickH = 20;
        int offsetX = (WIDTH - cols*brickW)/2;
        int offsetY = 50;

        for (int r=0; r<rows; r++) {
            for (int c=0; c<cols; c++) {
                bricks.add(new Brick(offsetX + c*brickW, offsetY + r*brickH, brickW-2, brickH-2));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            if (leftPressed) paddle.moveLeft();
            if (rightPressed) paddle.moveRight(WIDTH);

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
                    b.destroyed = true;
                    score += 100;
                    ball.dy *= -1;
                    break;
                }
            }

            // ball out of bounds
            if (ball.y > HEIGHT) {
                lives--;
                if (lives <= 0) {
                    gameOver = true;
                } else {
                    ball.reset(WIDTH/2, HEIGHT - 70);
                }
            }
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        paddle.draw(g);
        ball.draw(g);

        for (Brick b : bricks) {
            if (!b.destroyed) b.draw(g);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Lives: " + lives, WIDTH - 100, 20);

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("GAME OVER - Press R to Restart", 120, HEIGHT/2);
        }
    }

    // KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_LEFT) leftPressed = true;
        if (k == KeyEvent.VK_RIGHT) rightPressed = true;
        if (k == KeyEvent.VK_R && gameOver) {
            gameOver = false; score = 0; lives = 3;
            initGame();
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
}
