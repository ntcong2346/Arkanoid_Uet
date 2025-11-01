package leaderboard;

/**
 * Represents a single entry in the leaderboard.
 * Stores player name, scores for both game modes, and date.
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
     * Gets the highest score between single player and coop modes.
     */
    public int getHighestScore() {
        return Math.max(singlePlayerScore, coopScore);
    }

    @Override
    public String toString() {
        return playerName + "|" + singlePlayerScore + "|" + coopScore;
    }
}