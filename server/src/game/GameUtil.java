package game;
public class GameUtil {
    private GameUtil() {}

    public static String captitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
