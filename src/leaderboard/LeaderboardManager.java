// LeaderboardManager.java (HOÀN CHỈNH – KHÔI PHỤC TẤT CẢ)
package leaderboard;

import java.io.*;
import java.util.*;

public class LeaderboardManager {
    private static final String FILE_NAME = "leaderboard.txt";
    private static final int MAX_SINGLE = 5;
    private static final int MAX_COOP = 5;
    private final ArrayList<LeaderboardEntry> entries;
    private static LeaderboardManager instance;

    private LeaderboardManager() {
        entries = new ArrayList<>();
        loadFromFile();
        initializeSampleData();
    }

    public static LeaderboardManager getInstance() {
        if (instance == null) instance = new LeaderboardManager();
        return instance;
    }

    // === CẬP NHẬT LIÊN TỤC TRONG GAME ===
    public void tryUpdateSinglePlayer(String playerName, int score) {
        if (score <= 0) return;
        ArrayList<LeaderboardEntry> top = getTopSinglePlayerEntries();
        if (top.size() < MAX_SINGLE || score > top.get(top.size() - 1).getSinglePlayerScore()) {
            addOrUpdateEntry(playerName, score, 0);
        }
    }

    public void tryUpdateCoop(String playerName, int score) {
        if (score <= 0) return;
        ArrayList<LeaderboardEntry> top = getTopCoopEntries();
        if (top.size() < MAX_COOP || score > top.get(top.size() - 1).getCoopScore()) {
            addOrUpdateEntry(playerName, 0, score);
        }
    }

    // === THÊM HOẶC CẬP NHẬT ENTRY ===
    private void addOrUpdateEntry(String name, int single, int coop) {
        // Xóa entry cũ của cùng tên (nếu có)
        entries.removeIf(e -> e.getPlayerName().equals(name));
        entries.add(new LeaderboardEntry(name, single, coop));
        trimToLimits();
        saveToFile();
    }

    // === TOP 5 MỖI CHẾ ĐỘ ===
    public ArrayList<LeaderboardEntry> getTopSinglePlayerEntries() {
        ArrayList<LeaderboardEntry> list = new ArrayList<>();
        for (LeaderboardEntry e : entries) if (e.getSinglePlayerScore() > 0) list.add(e);
        list.sort((a, b) -> Integer.compare(b.getSinglePlayerScore(), a.getSinglePlayerScore()));
        return new ArrayList<>(list.subList(0, Math.min(list.size(), MAX_SINGLE)));
    }

    public ArrayList<LeaderboardEntry> getTopCoopEntries() {
        ArrayList<LeaderboardEntry> list = new ArrayList<>();
        for (LeaderboardEntry e : entries) if (e.getCoopScore() > 0) list.add(e);
        list.sort((a, b) -> Integer.compare(b.getCoopScore(), a.getCoopScore()));
        return new ArrayList<>(list.subList(0, Math.min(list.size(), MAX_COOP)));
    }

    // === HIGH SCORE ===
    public int getSinglePlayerHighScore() {
        return getTopSinglePlayerEntries().stream()
                .mapToInt(LeaderboardEntry::getSinglePlayerScore)
                .max().orElse(0);
    }

    public int getCoopHighScore() {
        return getTopCoopEntries().stream()
                .mapToInt(LeaderboardEntry::getCoopScore)
                .max().orElse(0);
    }

    // === SCORE TO BEAT (RẤT QUAN TRỌNG) ===
    public int getScoreToBeat(int currentScore, boolean isCoop) {
        ArrayList<LeaderboardEntry> top = isCoop ? getTopCoopEntries() : getTopSinglePlayerEntries();
        if (top.isEmpty()) return 0;

        for (int i = 0; i < top.size(); i++) {
            int score = isCoop ? top.get(i).getCoopScore() : top.get(i).getSinglePlayerScore();
            if (currentScore >= score) {
                if (i == 0) return score; // Đã là top 1
                int above = isCoop ? top.get(i - 1).getCoopScore() : top.get(i - 1).getSinglePlayerScore();
                return above;
            }
        }
        // Chưa vào top → cần vượt top 5
        int last = isCoop ? top.get(top.size() - 1).getCoopScore() : top.get(top.size() - 1).getSinglePlayerScore();
        return last;
    }

    // === GIỮ TỐI ĐA 5 MỖI LOẠI ===
    private void trimToLimits() {
        ArrayList<LeaderboardEntry> single = new ArrayList<>();
        ArrayList<LeaderboardEntry> coop = new ArrayList<>();

        for (LeaderboardEntry e : entries) {
            if (e.getSinglePlayerScore() > 0) single.add(e);
            if (e.getCoopScore() > 0) coop.add(e);
        }

        single.sort((a, b) -> Integer.compare(b.getSinglePlayerScore(), a.getSinglePlayerScore()));
        coop.sort((a, b) -> Integer.compare(b.getCoopScore(), a.getCoopScore()));

        entries.clear();
        entries.addAll(single.subList(0, Math.min(single.size(), MAX_SINGLE)));
        entries.addAll(coop.subList(0, Math.min(coop.size(), MAX_COOP)));
    }

    // === LƯU & LOAD ===
    private void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split("\\|", -1);
                if (p.length >= 3) {
                    String name = p[0];
                    int s = parseInt(p[1]);
                    int c = parseInt(p[2]);
                    entries.add(new LeaderboardEntry(name, s, c));
                }
            }
        } catch (Exception e) {
            // File không tồn tại → bỏ qua
        }
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (LeaderboardEntry e : entries) {
                bw.write(e.toString());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Lỗi lưu leaderboard: " + e.getMessage());
        }
    }

    private int parseInt(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    // === DỮ LIỆU MẪU ===
    private void initializeSampleData() {
        if (entries.isEmpty()) {
            // === SINGLE PLAYER: 5 NGƯỜI ===
            tryUpdateSinglePlayer("Ace", 850);
            tryUpdateSinglePlayer("Nova", 720);
            tryUpdateSinglePlayer("Blaze", 610);
            tryUpdateSinglePlayer("Storm", 480);
            tryUpdateSinglePlayer("Rogue", 320);

            // === CO-OP: 5 ĐỘI ===
            tryUpdateCoop("Thunder Duo", 2950);
            tryUpdateCoop("Fire & Ice", 2680);
            tryUpdateCoop("Twin Strike", 2410);
            tryUpdateCoop("Alpha Beta", 2190);
            tryUpdateCoop("Shadow Light", 1920);
        }
    }
}