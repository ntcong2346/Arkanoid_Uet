package menu;

import game.GameFrame;
import game.CoopGamePanel;
import game.GamePanel;
import graphics.Assets;
import sound.SoundManager;
import leaderboard.LeaderboardPanel;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public JCheckBox soundCheck;
    private final JComboBox<String> ballSpeedBox;
    private JComboBox<String> ballSizeBox;
    private JComboBox<String> paddleSpeedBox;
    private JComboBox<String> modeBox;
    private JButton startButton;
    private JButton leaderboardButton;

    public static boolean soundOn = true;
    public static int ballSpeed = 3;
    public static int ballSize = 12;
    public static int paddleSpeed = 6;
    public static int gameMode = 0; // 0: Single, 1: Coop
    public static String playerName = "Player"; // Tên mặc định

    public MenuPanel(JFrame menuFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Thêm các dòng trống để đẩy các component xuống
        gbc.gridy++;
        JLabel emptySpace1 = new JLabel("");
        emptySpace1.setPreferredSize(new Dimension(300, 35));
        add(emptySpace1, gbc);

        gbc.gridy++;
        JLabel emptySpace2 = new JLabel("");
        emptySpace2.setPreferredSize(new Dimension(300, 20));
        add(emptySpace2, gbc);

        gbc.gridy++;
        gbc.gridx = 1;
        soundCheck = new JCheckBox("", true);
        if (MenuPanel.soundOn) {
            SoundManager.getInstance().playMusic("bg_music");
        } else {
            SoundManager.getInstance().stopMusic();
        }
        add(soundCheck, gbc);

        gbc.gridy++;
        gbc.gridx = 1;
        ballSpeedBox = new JComboBox<>(new String[]{"Slow", "Normal", "Fast"});
        add(ballSpeedBox, gbc);

        gbc.gridy++;
        gbc.gridx = 1;
        ballSizeBox = new JComboBox<>(new String[]{"Small", "Normal", "Big"});
        add(ballSizeBox, gbc);

        gbc.gridy++;
        gbc.gridx = 1;
        paddleSpeedBox = new JComboBox<>(new String[]{"Slow", "Normal", "Fast"});
        add(paddleSpeedBox, gbc);

        gbc.gridy++;
        gbc.gridx = 1;
        modeBox = new JComboBox<>(new String[]{"1 Player", "2 Player"});
        add(modeBox, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        startButton = new JButton("Start");
        add(startButton, gbc);

        // THÊM NÚT BẢNG XẾP HẠNG
        gbc.gridy++;
        leaderboardButton = new JButton("Scoreboard");
        add(leaderboardButton, gbc);

        startButton.addActionListener(e -> {
            soundOn = soundCheck.isSelected();
            switch (ballSpeedBox.getSelectedIndex()) {
                case 0:
                    ballSpeed = 2;
                    break;
                case 1:
                    ballSpeed = 4;
                    break;
                case 2:
                    ballSpeed = 6;
                    break;
            }
            switch (ballSizeBox.getSelectedIndex()) {
                case 0:
                    ballSize = 5;
                    break;
                case 1:
                    ballSize = 8;
                    break;
                case 2:
                    ballSize = 10;
                    break;
            }
            switch (paddleSpeedBox.getSelectedIndex()) {
                case 0:
                    paddleSpeed = 4;
                    break;
                case 1:
                    paddleSpeed = 6;
                    break;
                case 2:
                    paddleSpeed = 10;
                    break;
            }
            gameMode = modeBox.getSelectedIndex();

            // HIỆN DIALOG NHẬP TÊN VỚI VALIDATION
            boolean validName = false;
            while (!validName) {
                String name = JOptionPane.showInputDialog(
                        menuFrame,
                        "Nhập tên của bạn:",
                        "Tên người chơi",
                        JOptionPane.QUESTION_MESSAGE
                );

                // XỬ LÝ KẾT QUẢ NHẬP TÊN
                if (name == null) {
                    // Người dùng bấm Cancel hoặc đóng dialog - thoát khỏi vòng lặp
                    return;
                } else if (name.trim().isEmpty()) {
                    // Tên rỗng - hiện cảnh báo
                    JOptionPane.showMessageDialog(
                            menuFrame,
                            "Tên không được để trống! Vui lòng nhập tên của bạn.",
                            "Lỗi",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    // Tên hợp lệ
                    playerName = name.trim();
                    validName = true;
                }
            }

            SoundManager.getInstance().stopMusic();

            menuFrame.dispose();
            JFrame gameFrame = new GameFrame();
            if (gameMode == 1) {
                gameFrame.setContentPane(new CoopGamePanel());
            } else {
                gameFrame.setContentPane(new GamePanel());
            }
            gameFrame.setVisible(true);
        });

        // THÊM SỰ KIỆN CHO NÚT BẢNG XẾP HẠNG
        leaderboardButton.addActionListener(e -> {
            LeaderboardPanel.showLeaderboardDialog(menuFrame);
        });

        soundCheck.addActionListener(e -> {
            boolean isSelected = soundCheck.isSelected();
            System.out.println("Sound toggled: " + isSelected);

            MenuPanel.soundOn = isSelected;

            if (isSelected) {
                SoundManager.getInstance().playMusic("bg_music");
            } else {
                SoundManager.getInstance().stopMusic();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Assets.backgroundMenu, 0, 0, getWidth(), getHeight(), this);
    }
}