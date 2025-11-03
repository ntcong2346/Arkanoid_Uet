package savegame;

import java.io.*;

/**
 * Lớp tiện ích tĩnh dùng để quản lý việc lưu và tải game bằng cơ chế Serialization.
 * Ưu tiên sử dụng các từ khóa nhập môn (class, final, static) và các lớp IO cơ bản.
 */
public final class GameSaverLoader {
    private static final String SAVE_FILE_NAME = "arkanoid_save.ser";

    /**
     * Constructor private để ngăn chặn việc khởi tạo đối tượng GameSaverLoader.
     */
    private GameSaverLoader() {
        // Class tiện ích, không cần khởi tạo.
    }

    /**
     * Lưu trạng thái trò chơi vào file.
     * Sử dụng FileOutputStream và ObjectOutputStream.
     *
     * @param data Đối tượng GameSaveData cần lưu.
     */
    public static final void saveGame(final GameSaveData data) {
        try (FileOutputStream fileOut = new FileOutputStream(SAVE_FILE_NAME);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(data);
            System.out.println("Game saved successfully to " + SAVE_FILE_NAME);

        } catch (final IOException i) {
            System.err.println("Error saving game: " + i.getMessage());
            i.printStackTrace();
        }
    }

    /**
     * Tải trạng thái trò chơi từ file.
     * Sử dụng FileInputStream và ObjectInputStream.
     *
     * @return Đối tượng GameSaveData đã tải, hoặc null nếu lỗi/không tìm thấy file.
     */
    public static final GameSaveData loadGame() {
        try (FileInputStream fileIn = new FileInputStream(SAVE_FILE_NAME); // Sửa lỗi: Bỏ thừa 'new'
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) { // Sửa lỗi: Bỏ thừa 'new'

            // Ép kiểu đối tượng đọc được thành GameSaveData
            final GameSaveData loadedData = (GameSaveData) objectIn.readObject();
            System.out.println("Game loaded successfully from " + SAVE_FILE_NAME);
            return loadedData;

        } catch (final FileNotFoundException f) {
            // Trường hợp chưa có file save
            System.err.println("Save file not found.");
            return null;
        } catch (final IOException | ClassNotFoundException i) {
            System.err.println("Error loading game: " + i.getMessage());
            i.printStackTrace();
            return null;
        }
    }

    /**
     * Kiểm tra xem file save có tồn tại hay không.
     *
     * @return true nếu file save tồn tại, ngược lại là false.
     */
    public static final boolean saveFileExists() {
        final File file = new File(SAVE_FILE_NAME);
        return file.exists() && file.length() > 0;
    }

    /**
     * Xóa file save.
     */
    public static final void deleteSaveFile() {
        final File file = new File(SAVE_FILE_NAME);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Save file deleted successfully.");
            } else {
                System.err.println("Failed to delete save file.");
            }
        }
    }
}