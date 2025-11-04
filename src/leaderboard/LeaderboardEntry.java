package leaderboard;

/**
 * Mục lưu trữ tên người chơi, điểm số cho cả hai chế độ chơi, và ngày.
 */
public class LeaderboardEntry {
    private final String playerName;
    private final int singlePlayerScore;
    private final int coopScore;

    public LeaderboardEntry(String playerName, int singlePlayerScore, int coopScore) {
        this.playerName = playerName;
        this.singlePlayerScore = singlePlayerScore;
        this.coopScore = coopScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getSinglePlayerScore() {
        return singlePlayerScore;
    }

    public int getCoopScore() {
        return coopScore;
    }

    /**
     * Lấy điểm số cao nhất giữa chế độ Single và chế độ Co-op.
     */
    public int getHighestScore() {
        return Math.max(singlePlayerScore, coopScore);
    }

    @Override
    public String toString() {
        return playerName + "|" + singlePlayerScore + "|" + coopScore;
    }
}