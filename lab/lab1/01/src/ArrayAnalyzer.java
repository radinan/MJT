public class ArrayAnalyzer {
    public static void main(String[] args) {
        System.out.println(isMountainArray(new int[]{2, 1}));
        System.out.println(isMountainArray(new int[]{3, 5, 5}));
        System.out.println(isMountainArray(new int[]{0, 3, 2, 1}));
    }

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
