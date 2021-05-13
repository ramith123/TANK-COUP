import java.util.Random;

public class Util {
    public static int getRandomNumberUsingNextInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static boolean isNumberBetween(int min, int max, int n) {
        if (n >= min && n <= max)
            return true;
        return false;
    }
}
