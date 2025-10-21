import javax.swing.*;

public class Main {
    private static final int WIDTH = 0;
    private static final int HEIGHT = 0;

    public static void main(String[] args) {
        Ball ball = new Ball(WIDTH / 2.0, HEIGHT - 70, MenuPanel.ballSize, MenuPanel.ballSpeed);
        Paddle paddle = new Paddle(WIDTH / 2.0 - 60, HEIGHT - 50, 120, 15, MenuPanel.paddleSpeed);
        paddle.setSpeed(MenuPanel.paddleSpeed);
        JFrame frame = new GameFrame();
        frame.setContentPane(new MenuPanel(frame));
        frame.setVisible(true);
        Assets.load();
    }
}