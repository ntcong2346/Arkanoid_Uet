package game;

import javax.swing.*;

public class GameFrame extends JFrame {

    private JComponent activeGamePanel;

    /**
     * Constructor GỐC (Không tham số).
     */
    public GameFrame() {
        GamePanel panel = new GamePanel();
        this.activeGamePanel = panel;
        setupFrame(panel);
    }

    /**
     * Constructor quá tải để thiết lập ContentPane từ bên ngoài (MenuPanel).
     */
    public GameFrame(final JComponent gamePanel) {
        this.activeGamePanel = gamePanel;
        setupFrame(gamePanel);
    }

    // Khởi tạo Frame chung
    private void setupFrame(final JComponent panel) {
        // Sử dụng setContentPane thay vì add()
        this.setContentPane(panel);
        this.setTitle("Arkanoid 2025");

        // Quay lại chế độ đóng cửa sổ thông thường
        // Không còn cần lắng nghe sự kiện đóng cửa sổ để lưu game nữa
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);

        // LOẠI BỎ TOÀN BỘ KHỐI addWindowListener VÀ LOGIC LƯU GAME

        this.setVisible(true);
    }
}