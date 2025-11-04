package menu;

import game.GameFrame;
import graphics.Assets;
import leaderboard.LeaderboardPanel;
import sound.SoundManager;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private final JFrame menuFrame;

    public MainMenuPanel(JFrame menuFrame) {
        this.menuFrame = menuFrame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        if (MenuPanel.soundOn) {
            // Play background music if enabled and not already running
            if (!SoundManager.getInstance().isMusicPlaying()) {
                SoundManager.getInstance().playMusic("bg_music");
            }
        } else {
            // Stop music if setting is off
            SoundManager.getInstance().stopMusic();
        }


        // Add some empty space for centering
        gbc.gridy++;
        JLabel emptySpace = new JLabel("");
        emptySpace.setPreferredSize(new Dimension(300, 50));
        add(emptySpace, gbc);

        // --- BUTTONS ---
        JButton startButton = new JButton("New Game");
        styleButton(startButton);
        gbc.gridy++;
        add(startButton, gbc);

        JButton loadButton = new JButton("Load Game");
        styleButton(loadButton);
        gbc.gridy++;
        add(loadButton, gbc);

        JButton settingsButton = new JButton("Settings");
        styleButton(settingsButton);
        gbc.gridy++;
        add(settingsButton, gbc);

        JButton leaderboardButton = new JButton("Leaderboard");
        styleButton(leaderboardButton);
        gbc.gridy++;
        add(leaderboardButton, gbc);

        JButton infoButton = new JButton("How To Play");
        styleButton(infoButton);
        gbc.gridy++;
        add(infoButton, gbc);

        // --- ACTION LISTENERS ---

        // Start button - Opens the game
        startButton.addActionListener(e -> {
            // Note: Start logic from original MenuPanel is complex (name input, game mode).
            // For simplicity, this will just call the startGame method of the SettingsPanel
            // which handles the name input and launching the game.
            new MenuPanel(menuFrame).newGame(menuFrame);
        });

        loadButton.addActionListener(e -> {
            new MenuPanel(menuFrame).loadGame(menuFrame); // <-- GỌI PHƯƠNG THỨC ĐÓNG GÓI
        });

        // Settings button - Opens the Settings Panel
        settingsButton.addActionListener(e -> {
            menuFrame.setContentPane(new MenuPanel(menuFrame));
            menuFrame.revalidate();
        });

        // Leaderboard button - Shows the Leaderboard Dialog
        leaderboardButton.addActionListener(e -> {
            LeaderboardPanel.showLeaderboardDialog(menuFrame);
        });

        // Info button - Shows a simple Info Dialog
        infoButton.addActionListener(e -> {
            showInfoDialog();
        });
    }
    // Helper method to apply consistent styling
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(50, 50, 150)); // Darker Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(250, 50));
    }

    private void showInfoDialog() {
        String info = "<html><p style='width: 300px; text-align: center;'>" +
                "<b>How to Play:</b> Use left/right arrow keys or A/D to move the paddle.<br>" +
                "Objective: Destroy all bricks to win levels.<br>" +
                "Utility:</b> Press F5 to save your current game state.<br><br>" +
                "</p></html>";
        JOptionPane.showMessageDialog(
                menuFrame,
                info,
                "Instructions",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Assuming Assets.backgroundMenu is accessible and the image exists
        g.drawImage(Assets.backgroundMenu, 0, 0, getWidth(), getHeight(), this);
    }
}