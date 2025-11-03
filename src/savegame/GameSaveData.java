package savegame;

import entity.Brick;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Lớp này dùng để lưu trữ trạng thái của trò chơi.
 * Phải implements Serializable để có thể lưu (ghi) và tải (đọc) đối tượng vào/từ file.
 */
public class GameSaveData implements Serializable {
    // Phiên bản SerialVersionUID là cần thiết cho quá trình Serialization
    private static final long serialVersionUID = 1L;

    // Dữ liệu chung
    private final String playerName;
    private final int gameMode; // 0: Single, 1: Coop

    // Trạng thái game
    private final int level;
    private final int lives;
    private final int score;
    private final double paddle1X;
    private final double paddle2X; // Chỉ dùng cho Coop Mode, đặt giá trị mặc định cho Single

    // Trạng thái màn chơi (Danh sách gạch còn lại)
    private final ArrayList<Brick> bricksState;

    // Cài đặt game đã lưu (Lấy từ MenuPanel trước khi chơi)
    private final int paddleSpeed;
    private final int ballSpeed;
    private final int ballSize;

    // Constructor cho Single Player
    public GameSaveData(String playerName, int gameMode, int level, int lives, int score, double paddleX,
                        ArrayList<Brick> bricksState, int paddleSpeed, int ballSpeed, int ballSize) {
        this.playerName = playerName;
        this.gameMode = gameMode;
        this.level = level;
        this.lives = lives;
        this.score = score;
        this.paddle1X = paddleX;
        this.paddle2X = 0; // Không dùng trong Single Player
        this.bricksState = bricksState;
        this.paddleSpeed = paddleSpeed;
        this.ballSpeed = ballSpeed;
        this.ballSize = ballSize;
    }

    // Constructor cho Coop Player (Thêm mới)
    public GameSaveData(String playerName, int gameMode, int level, int lives, int score, double paddle1X, double paddle2X,
                        ArrayList<Brick> bricksState, int paddleSpeed, int ballSpeed, int ballSize) {
        this.playerName = playerName;
        this.gameMode = gameMode;
        this.level = level;
        this.lives = lives;
        this.score = score;
        this.paddle1X = paddle1X;
        this.paddle2X = paddle2X;
        this.bricksState = bricksState;
        this.paddleSpeed = paddleSpeed;
        this.ballSpeed = ballSpeed;
        this.ballSize = ballSize;
    }

    // --- Các phương thức Getter (Tất cả phải là public và final theo OOP và Google Style) ---

    public final String getPlayerName() {
        return playerName;
    }

    public final int getGameMode() {
        return gameMode;
    }

    public final int getLevel() {
        return level;
    }

    public final int getLives() {
        return lives;
    }

    public final int getScore() {
        return score;
    }

    public final double getPaddle1X() {
        return paddle1X;
    }

    public final double getPaddle2X() {
        return paddle2X;
    }

    public final ArrayList<Brick> getBricksState() {
        return bricksState;
    }

    public final int getPaddleSpeed() {
        return paddleSpeed;
    }

    public final int getBallSpeed() {
        return ballSpeed;
    }

    public final int getBallSize() {
        return ballSize;
    }

    /**
     * Dùng để chuyển đổi các giá trị số của setting thành chuỗi mô tả (ví dụ: 3 -> "Normal").
     */
    public final String getSettingString(String settingType, int value) {
        return switch (settingType) {
            case "ballSpeed" -> switch (value) {
                case 3 -> "Normal";
                case 5 -> "Fast";
                default -> "Slow"; // Mặc định là 1 hoặc giá trị khác
            };
            case "ballSize" -> switch (value) {
                case 12 -> "Normal";
                case 18 -> "Big";
                default -> "Small"; // Mặc định là 8 hoặc giá trị khác
            };
            case "paddleSpeed" -> switch (value) {
                case 6 -> "Normal";
                case 10 -> "Fast";
                default -> "Slow"; // Mặc định là 4 hoặc giá trị khác
            };
            default -> String.valueOf(value);
        };
    }
}
