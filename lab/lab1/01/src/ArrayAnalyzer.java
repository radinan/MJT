public class ArrayAnalyzer {
    public static boolean isMountainArray(int[] array) {
        if (array.length < 3) {
            return false;
        }

        int pitch = -1;
        for (int i = 0; i < array.length - 1; ++i) {
            if (array[i] == array[i + 1]) {
                return false;
            } else if (array[i] < array[i+1]) {
                if (pitch != -1) {
                    return false;
                }
            } else {
                if (pitch == -1) {
                    pitch = i;
                }
            }
        }

        return true;
    }
}
