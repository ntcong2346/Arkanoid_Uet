package menu;

import game.GameFrame;
import game.CoopGamePanel;
import game.GamePanel;
import graphics.Assets;
import savegame.GameSaveData;
import savegame.GameSaverLoader;
import sound.SoundManager;
import leaderboard.LeaderboardPanel; // Cần import nếu LeaderboardPanel được dùng ở đâu đó
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer; // Cần thiết cho ButtonGroupConsumer

// Tên file và class vẫn là MenuPanel
public class MenuPanel extends JPanel {
    public JCheckBox soundCheck;

    // Loai bỏ JComboBox, thay bằng JPanel chứa JRadioButton
    private JPanel ballSpeedPanel;
    private JPanel ballSizePanel;
    private JPanel paddleSpeedPanel;
    private JPanel modePanel;

    private JButton backButton;

    // Khai báo ButtonGroup để quản lý lựa chọn
    private ButtonGroup ballSpeedGroup;
    private ButtonGroup ballSizeGroup;
    private ButtonGroup paddleSpeedGroup;
    private ButtonGroup modeGroup;

    // Static variables remain the same
    public static boolean soundOn = true;
    public static int ballSpeed = 3;
    public static int ballSize = 12;
    public static int paddleSpeed = 6;
    public static int gameMode = 0; // 0: Single, 1: Coop
    public static String playerName = "Player"; // Tên mặc định

    // Giao diện để trả về ButtonGroup (được sử dụng trong createButtonSettingGroup)
    @FunctionalInterface
    interface ButtonGroupConsumer extends Consumer<ButtonGroup> {}

    public MenuPanel(JFrame menuFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Thêm dòng trống
        gbc.gridy++;
        JLabel emptySpace1 = new JLabel("");
        emptySpace1.setPreferredSize(new Dimension(300, 35));
        add(emptySpace1, gbc);

        // =======================================================
        // Khởi tạo JCheckBox (Sound)
        // =======================================================
        soundCheck = new JCheckBox("", true);
        soundCheck.setSelected(MenuPanel.soundOn);

        // =======================================================
        // Khởi tạo các nhóm JRadioButton (thay thế JComboBox)
        // =======================================================

        // 2. Ball Speed
        ballSpeedPanel = createButtonSettingGroup(
                new String[]{"Slow", "Normal", "Fast"},
                getSpeedIndex(MenuPanel.ballSpeed),
                "ballSpeed",
                out -> ballSpeedGroup = out
        );

        // 3. Ball Size
        ballSizePanel = createButtonSettingGroup(
                new String[]{"Small", "Normal", "Big"},
                getSizeIndex(MenuPanel.ballSize),
                "ballSize",
                out -> ballSizeGroup = out
        );

        // 4. Paddle Speed
        paddleSpeedPanel = createButtonSettingGroup(
                new String[]{"Slow", "Normal", "Fast"},
                getSpeedIndex(MenuPanel.paddleSpeed),
                "paddleSpeed",
                out -> paddleSpeedGroup = out
        );

        // 5. Game Mode
        modePanel = createButtonSettingGroup(
                new String[]{"Individual", "Co-op"},
                MenuPanel.gameMode,
                "gameMode",
                out -> modeGroup = out
        );

        // =======================================================
        // Container cho các components Settings - Dạng 2 cột (hàng ngang)
        // =======================================================
        JPanel settingsContainer = new JPanel(new GridBagLayout());
        settingsContainer.setOpaque(false);

        GridBagConstraints sgbc = new GridBagConstraints();
        sgbc.insets = new Insets(15, 20, 15, 20);
        sgbc.anchor = GridBagConstraints.CENTER;

        // 1. Sound Checkbox (Cột 0, Hàng 0)
        sgbc.gridx = 0;
        sgbc.gridy = 0;
        settingsContainer.add(createSettingGroup("Sound On/Off:", soundCheck), sgbc);

        // 2. Ball Speed (Cột 1, Hàng 0)
        sgbc.gridx = 1;
        sgbc.gridy = 0;
        settingsContainer.add(createSettingGroup("Ball Speed:", ballSpeedPanel), sgbc);

        // 3. Ball Size (Cột 0, Hàng 1)
        sgbc.gridx = 0;
        sgbc.gridy = 1;
        settingsContainer.add(createSettingGroup("Ball Size:", ballSizePanel), sgbc);

        // 4. Paddle Speed (Cột 1, Hàng 1)
        sgbc.gridx = 1;
        sgbc.gridy = 1;
        settingsContainer.add(createSettingGroup("Paddle Speed:", paddleSpeedPanel), sgbc);

        // 5. Game Mode (Cột 0, Hàng 2 - Chiếm 2 cột)
        sgbc.gridx = 0;
        sgbc.gridy = 2;
        sgbc.gridwidth = 2;
        settingsContainer.add(createSettingGroup("Game Mode:", modePanel), sgbc);
        sgbc.gridwidth = 1; // Reset

        // Thêm container settings vào panel chính
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(settingsContainer, gbc);

        // Separator
        gbc.gridy++;
        gbc.gridwidth = 2;
        add(new JSeparator(), gbc);

        // Back Button
        gbc.gridy++;
        backButton = new JButton("Back to Main Menu");
        backButton.setPreferredSize(new Dimension(250, 40));
        add(backButton, gbc);

        // --- ACTION LISTENERS ---

        soundCheck.addActionListener(e -> {
            boolean isSelected = soundCheck.isSelected();
            MenuPanel.soundOn = isSelected;

            if (isSelected) {
                if (!SoundManager.getInstance().isPlaying()) {
                    SoundManager.getInstance().playMusic("bg_music");
                }
            } else {
                SoundManager.getInstance().stopMusic();
            }
        });

        // Back button action - Quay lại MainMenuPanel
        backButton.addActionListener(e -> {
            applySettings();
            // Đảm bảo MainMenuPanel được import và tồn tại
            menuFrame.setContentPane(new MainMenuPanel(menuFrame));
            menuFrame.revalidate();
        });

        // Xử lý nhạc nền khi Settings Panel được mở
        if (MenuPanel.soundOn && !SoundManager.getInstance().isPlaying()) {
            SoundManager.getInstance().playMusic("bg_music");
        }
        // Loại bỏ đoạn else và khai báo startButton thừa ở cuối constructor
    }

    // NEW: Helper method để tạo nhóm JRadioButton
    private JPanel createButtonSettingGroup(String[] options, int selectedIndex, String actionCommandPrefix, ButtonGroupConsumer groupConsumer) {
        JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        groupPanel.setOpaque(false);

        ButtonGroup buttonGroup = new ButtonGroup();

        for (int i = 0; i < options.length; i++) {
            JRadioButton radio = new JRadioButton(options[i]);
            radio.setForeground(Color.WHITE);
            radio.setOpaque(false);
            radio.setActionCommand(actionCommandPrefix + i); // Đặt ActionCommand để xác định tùy chọn

            // Thêm ActionListener để áp dụng cài đặt khi người dùng nhấn
            radio.addActionListener(e -> applySettings());

            if (i == selectedIndex) {
                radio.setSelected(true);
            }

            buttonGroup.add(radio);
            groupPanel.add(radio);
        }

        groupConsumer.accept(buttonGroup);
        return groupPanel;
    }


    // Helper method để tạo nhóm Label-Component (Giữ nguyên)
    private JPanel createSettingGroup(String labelText, JComponent component) {
        JPanel group = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        group.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));

        group.add(label);
        group.add(component);
        return group;
    }

    // =======================================================
    // Cập nhật: Logic áp dụng cài đặt phải đọc ActionCommand
    // =======================================================
    public void applySettings() {
        // Cần kiểm tra null cho ButtonGroup.getSelection() trong trường hợp đặc biệt,
        // nhưng với JRadioButton, một nút luôn được chọn (do có selectedIndex ban đầu).

        // Đọc chỉ số đã chọn từ ActionCommand
        int ballSpeedIndex = Integer.parseInt(ballSpeedGroup.getSelection().getActionCommand().substring("ballSpeed".length()));
        int ballSizeIndex = Integer.parseInt(ballSizeGroup.getSelection().getActionCommand().substring("ballSize".length()));
        int paddleSpeedIndex = Integer.parseInt(paddleSpeedGroup.getSelection().getActionCommand().substring("paddleSpeed".length()));
        int modeIndex = Integer.parseInt(modeGroup.getSelection().getActionCommand().substring("gameMode".length()));

        // Apply Ball Speed
        switch (ballSpeedIndex) {
            case 0: ballSpeed = 2; break;
            case 1: ballSpeed = 4; break;
            case 2: ballSpeed = 6; break;
        }

        // Apply Ball Size
        switch (ballSizeIndex) {
            case 0: ballSize = 5; break;
            case 1: ballSize = 8; break;
            case 2: ballSize = 10; break;
        }

        // Apply Paddle Speed
        switch (paddleSpeedIndex) {
            case 0: paddleSpeed = 4; break;
            case 1: paddleSpeed = 6; break;
            case 2: paddleSpeed = 10; break;
        }

        // Apply Game Mode
        gameMode = modeIndex;
    }

    // ... (startGame method and utility methods getSpeedIndex, getSizeIndex, paintComponent remain the same) ...

    // Encapsulated logic for starting the game
    public void newGame(JFrame menuFrame) {
        applySettings();

        boolean validName = false;
        while (!validName) {
            String name = JOptionPane.showInputDialog(menuFrame, "Nhập tên của bạn:", "Tên người chơi", JOptionPane.QUESTION_MESSAGE);
            if (name == null) {
                return;
            } else if (name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(menuFrame, "Tên không được để trống! Vui lòng nhập tên của bạn.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            } else {
                playerName = name.trim();
                validName = true;
                GameSaverLoader.deleteSaveFile();
            }
        }

        SoundManager.getInstance().stopMusic();

        menuFrame.dispose();
        if (gameMode == 1) {
            new GameFrame(new CoopGamePanel());
        } else {
            new GameFrame(new GamePanel());
        }
    }

    /**
     * Logic for load game button.
     * @param menuFrame
     */
    public void loadGame(JFrame menuFrame){
        // 1. Kiểm tra sự tồn tại của file save
        if (!GameSaverLoader.saveFileExists()) {
            JOptionPane.showMessageDialog(
                    menuFrame,
                    "Chưa có file save game nào được tạo.",
                    "Lỗi Load Game",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // 2. Tải dữ liệu để kiểm tra và hiển thị
        final GameSaveData loadedData = GameSaverLoader.loadGame();
        if (loadedData == null) {
            JOptionPane.showMessageDialog(
                    menuFrame,
                    "Lỗi đọc file save.",
                    "Lỗi Load Game",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Xây dựng thông báo xác nhận
        final String ballSpeedStr = loadedData.getSettingString("ballSpeed", loadedData.getBallSpeed());
        final String ballSizeStr = loadedData.getSettingString("ballSize", loadedData.getBallSize());
        final String paddleSpeedStr = loadedData.getSettingString("paddleSpeed", loadedData.getPaddleSpeed());
        final String gameModeStr = (loadedData.getGameMode() == 1) ? "Co-op" : "Single Player";

        final String message = "Bạn có chắc muốn chơi lại lần trước?\n\n"
                + "Người chơi: " + loadedData.getPlayerName() + " (" + gameModeStr + ")\n"
                + "Level: " + loadedData.getLevel() + "\n"
                + "Lives: " + loadedData.getLives() + "\n"
                + "Score: " + loadedData.getScore() + "\n"
                + "Tổng gạch còn lại: " + loadedData.getBricksState().size() + "\n\n"
                + "Cài đặt của lần trước:\n"
                + "- Tốc độ bóng: " + ballSpeedStr + "\n"
                + "- Kích thước bóng: " + ballSizeStr + "\n"
                + "- Tốc độ Paddle: " + paddleSpeedStr;

        final int result = JOptionPane.showConfirmDialog(
                menuFrame,
                message,
                "Xác nhận chơi lại?",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            SoundManager.getInstance().stopMusic();

            menuFrame.dispose();

            // Cập nhật biến tĩnh gameMode, playerName để đồng bộ với game đã tải
            MenuPanel.gameMode = loadedData.getGameMode();
            MenuPanel.playerName = loadedData.getPlayerName();

            // Khởi tạo game với dữ liệu đã tải
            if (loadedData.getGameMode() == 1) {
                new GameFrame(new CoopGamePanel(loadedData));
            } else {
                new GameFrame(new GamePanel(loadedData));
            }
        }
    }

    // Utility methods
    private int getSpeedIndex(int speed) {
        if (speed == 2) return 0;
        if (speed == 4) return 1;
        if (speed == 6) return 2;
        return 1;
    }
    private int getSizeIndex(int size) {
        if (size == 5) return 0;
        if (size == 8 || size == 12) return 1;
        if (size == 10) return 2;
        return 1;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Assets.backgroundSettings, 0, 0, getWidth(), getHeight(), this);
    }
}