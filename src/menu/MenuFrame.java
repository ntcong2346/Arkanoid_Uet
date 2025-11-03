package menu;

import javax.swing.*;

public class MenuFrame extends JFrame {
    public MenuFrame() {
        setTitle("Arkanoid Pro 2025 - MAIN MENU");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        // Changed to load the new MainMenuPanel
        setContentPane(new MainMenuPanel(this));
    }
}