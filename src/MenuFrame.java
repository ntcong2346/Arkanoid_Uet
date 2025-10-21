import javax.swing.*;

public class MenuFrame extends JFrame {
    public MenuFrame() {
        setTitle("Arkanoid Pro 2025 - MENU");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setContentPane(new MenuPanel(this));
    }
}