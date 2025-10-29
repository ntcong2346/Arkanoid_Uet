import javax.swing.*;
import java.awt.*;

import game.CoopGamePanel;
import game.GameFrame;
import game.GamePanel;

public class MenuPanel extends JPanel {
    private JCheckBox soundCheck;
    private JComboBox<String> ballSpeedBox;
    private JComboBox<String> ballSizeBox;
    private JComboBox<String> paddleSpeedBox;
    private JComboBox<String> modeBox;
    private JButton startButton;

    public static boolean soundOn = true;
    public static int ballSpeed = 3;
    public static int ballSize = 12;
    public static int paddleSpeed = 6;
    public static int gameMode = 0; // 0: Single, 1: Coop, 2: Versus

    public MenuPanel(JFrame menuFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.gridx = 0; gbc.gridy = 0;

        JLabel title = new JLabel("Arkanoid Pro 2025 - MENU");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, gbc);

        gbc.gridy++;
        soundCheck = new JCheckBox("Bật tiếng", true);
        add(soundCheck, gbc);

        gbc.gridy++;
        add(new JLabel("Tốc độ bóng:"), gbc);
        gbc.gridx++;
        ballSpeedBox = new JComboBox<>(new String[]{"Slow", "Normal", "Fast"});
        add(ballSpeedBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Kích thước bóng:"), gbc);
        gbc.gridx++;
        ballSizeBox = new JComboBox<>(new String[]{"Small", "Normal", "Big"});
        add(ballSizeBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Tốc độ paddle:"), gbc);
        gbc.gridx++;
        paddleSpeedBox = new JComboBox<>(new String[]{"Slow", "Normal", "Fast"});
        add(paddleSpeedBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Chế độ chơi:"), gbc);
        gbc.gridx++;
        modeBox = new JComboBox<>(new String[]{"Một người", "Hợp tác"});
        add(modeBox, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        startButton = new JButton("Bắt đầu chơi");
        add(startButton, gbc);

        startButton.addActionListener(_ -> {
            soundOn = soundCheck.isSelected();
            switch (ballSpeedBox.getSelectedIndex()) {
                case 0: ballSpeed = 2; break;
                case 1: ballSpeed = 3; break;
                case 2: ballSpeed = 7; break;
            }
            switch (ballSizeBox.getSelectedIndex()) {
                case 0: ballSize = 6; break;
                case 1: ballSize = 12; break;
                case 2: ballSize = 22; break;
            }
            switch (paddleSpeedBox.getSelectedIndex()) {
                case 0: paddleSpeed = 4; break;
                case 1: paddleSpeed = 6; break;
                case 2: paddleSpeed = 15; break;
            }
            gameMode = modeBox.getSelectedIndex();
            menuFrame.dispose();
            JFrame gameFrame = new GameFrame();
            if (gameMode == 1) {
                gameFrame.setContentPane(new CoopGamePanel());
            } else {
                gameFrame.setContentPane(new GamePanel());
            }
            gameFrame.setVisible(true);
        });
    }
}