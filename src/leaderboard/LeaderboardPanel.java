// LeaderboardPanel.java (OOP HOÀN CHỈNH)
package leaderboard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class LeaderboardPanel extends JPanel {
    private static final int PANEL_WIDTH = 600;
    private static final int PANEL_HEIGHT = 400;

    public LeaderboardPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());
        buildUI();
    }

    // === TÁCH NHỎ: XÂY DỰNG GIAO DIỆN ===
    private void buildUI() {
        add(createTitleLabel(), BorderLayout.NORTH);
        add(createContentPanel(), BorderLayout.CENTER);
    }

    // === TÁCH: TITLE ===
    private JLabel createTitleLabel() {
        JLabel label = new JLabel("BẢNG XẾP HẠNG", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.YELLOW);
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        return label;
    }

    // === TÁCH: NỘI DUNG CHÍNH ===
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        panel.add(createModePanel("SINGLE PLAYER", true));
        panel.add(createModePanel("CO-OP MODE", false));

        return panel;
    }

    // === TÁCH: PANEL MỖI CHẾ ĐỘ ===
    private JPanel createModePanel(String title, boolean isSinglePlayer) {
        ArrayList<LeaderboardEntry> entries = isSinglePlayer
                ? LeaderboardManager.getInstance().getTopSinglePlayerEntries()
                : LeaderboardManager.getInstance().getTopCoopEntries();

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 30, 60));
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        panel.add(createModeTitle(title), BorderLayout.NORTH);
        panel.add(createScoresList(entries, isSinglePlayer), BorderLayout.CENTER);

        return panel;
    }

    // === TÁCH: TIÊU ĐỀ CHẾ ĐỘ ===
    private JLabel createModeTitle(String title) {
        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setBackground(new Color(60, 60, 100));
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return label;
    }

    // === TÁCH: DANH SÁCH ĐIỂM ===
    private JPanel createScoresList(ArrayList<LeaderboardEntry> entries, boolean isSinglePlayer) {
        JPanel panel = new JPanel(new GridLayout(5, 1, 0, 5));
        panel.setBackground(new Color(30, 30, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < 5; i++) {
            panel.add(createScoreLabel(i, entries, isSinglePlayer));
        }
        return panel;
    }

    // === TÁCH: LABEL MỖI DÒNG ===
    private JLabel createScoreLabel(int rank, ArrayList<LeaderboardEntry> entries, boolean isSinglePlayer) {
        String text;
        Color color;

        if (rank < entries.size()) {
            LeaderboardEntry entry = entries.get(rank);
            int score = isSinglePlayer ? entry.getSinglePlayerScore() : entry.getCoopScore();
            text = (rank + 1) + ". " + entry.getPlayerName() + " (" + score + ")";
            color = Color.WHITE;
        } else {
            text = (rank + 1) + ". ---";
            color = Color.GRAY;
        }

        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(color);
        return label;
    }

    // === HIỂN THỊ DIALOG ===
    public static void showLeaderboardDialog(Component parent) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Bảng Xếp Hạng");
        dialog.setModal(true);
        dialog.setResizable(false);
        dialog.add(new LeaderboardPanel());
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
}