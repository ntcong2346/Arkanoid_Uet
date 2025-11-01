package menu;
import game.GameFrame;
import game.CoopGamePanel;
import game.GamePanel;
import graphics.Assets;
import sound.SoundManager;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {
    public JCheckBox soundCheck;
    private final JComboBox<String> ballSpeedBox;
    private JComboBox<String> ballSizeBox;
    private JComboBox<String> paddleSpeedBox;
    private JComboBox<String> modeBox;
    private JButton startButton;

    public static boolean soundOn = true;
    public static int ballSpeed = 3;
    public static int ballSize = 12;
    public static int paddleSpeed = 6;
    public static int gameMode = 0; // 0: Single, 1: Coop

    public MenuPanel(JFrame menuFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Thay Title bằng 1 component trống
        JLabel emptySpace = new JLabel("");
        emptySpace.setPreferredSize(new Dimension(300, 33)); // Kích thước tương đương title
        add(emptySpace, gbc);

        gbc.gridy++;
        gbc.gridx = 1;
        soundCheck = new JCheckBox("", true);
        if (MenuPanel.soundOn) {
            SoundManager.getInstance().playMusic("bg_music");
        }
        add(soundCheck, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridx = 1;
        ballSpeedBox = new JComboBox<>(new String[]{"Slow", "Normal", "Fast"});
        add(ballSpeedBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridx++;
        ballSizeBox = new JComboBox<>(new String[]{"Small", "Normal", "Big"});
        add(ballSizeBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridx++;
        paddleSpeedBox = new JComboBox<>(new String[]{"Slow", "Normal", "Fast"});
        add(paddleSpeedBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridx++;
        modeBox = new JComboBox<>(new String[]{"1 Player", "2 Player"});
        add(modeBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        startButton = new JButton("Play");
        add(startButton, gbc);

        startButton.addActionListener(e -> {
            soundOn = soundCheck.isSelected();
            SoundManager.getInstance().stopMusic();
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
            menuFrame.dispose();
            JFrame gameFrame = new GameFrame();
            if (gameMode == 1) {
                gameFrame.setContentPane(new CoopGamePanel());
            } else {
                gameFrame.setContentPane(new GamePanel());
            }
            gameFrame.setVisible(true);
        });

        soundCheck.addActionListener(e -> {
            boolean isSelected = soundCheck.isSelected();
            System.out.println("Sound toggled: " + isSelected);

            MenuPanel.soundOn = isSelected;

            if (isSelected) {
                SoundManager.getInstance().play("bg_music");
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