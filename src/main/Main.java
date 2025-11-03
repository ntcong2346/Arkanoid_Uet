package main;

import menu.MainMenuPanel; // Cần import MainMenuPanel
import menu.MenuPanel; // Vẫn cần MenuPanel để lấy static vars
import game.GameFrame;
import graphics.Assets;
import entity.Ball;
import entity.Paddle;
import sound.SoundManager;

import javax.swing.*;

public class Main {
    private static final int WIDTH = 0;
    private static final int HEIGHT = 0;

    public static void main(String[] args) {
        Assets.load();
        SoundManager.getInstance();

        // Vẫn giữ lại các dòng sử dụng MenuPanel để lấy giá trị static ban đầu
        Ball ball = new Ball(WIDTH / 2.0, HEIGHT - 70, MenuPanel.ballSize, MenuPanel.ballSpeed);
        Paddle paddle = new Paddle(WIDTH / 2.0, HEIGHT - 50, 120, 15, MenuPanel.paddleSpeed);
        paddle.setSpeed(MenuPanel.paddleSpeed);

        // Khởi tạo và hiển thị MenuFrame
        JFrame frame = new GameFrame();

        // SỬA ĐỔI QUAN TRỌNG: Load MainMenuPanel đầu tiên
        frame.setContentPane(new MainMenuPanel(frame));

        frame.setVisible(true);
    }
}