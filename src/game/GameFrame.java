package game;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import savegame.GameSaveData;
import savegame.GameSaverLoader;

public class GameFrame extends JFrame {

    private JComponent activeGamePanel; // Thêm biến để lưu panel đang hoạt động

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

        // THAY ĐỔI: Dùng DO_NOTHING_ON_CLOSE để bắt sự kiện đóng và lưu game
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);

        // THÊM: Logic lưu game khi đóng cửa sổ
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                // Chỉ lưu game nếu đang trong Game Panel VÀ game CHƯA KẾT THÚC VÀ BÓNG ĐANG BAY
                if (activeGamePanel instanceof GamePanel singlePanel) {
                    // Điều kiện
                    if (!singlePanel.isGameOver() && singlePanel.isBallInMotion()) {
                        GameSaveData saveData = singlePanel.createSaveData();
                        GameSaverLoader.saveGame(saveData);
                    }
                } else if (activeGamePanel instanceof CoopGamePanel coopPanel) {
                    // Điều kiện
                    if (!coopPanel.isGameOver() && coopPanel.isBallInMotion()) {
                        GameSaveData saveData = coopPanel.createSaveData();
                        GameSaverLoader.saveGame(saveData);
                    }
                }

                // Sau khi kiểm tra và lưu xong, đóng cửa sổ
                System.exit(0);
            }
        });

        this.setVisible(true);
    }
}