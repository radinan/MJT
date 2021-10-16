import java.util.Arrays;

public class WeatherForecaster {
    public static void main(String[] args) {
        int[] s1 = getsWarmerIn(new int[]{3, 4, 5, 1, -1, 2, 6, 3});
        int[] s2 = getsWarmerIn(new int[]{3, 4, 5, 6});
        int[] s3 = getsWarmerIn(new int[]{3, 6, 9});

        System.out.println(Arrays.toString(s1));
        System.out.println(Arrays.toString(s2));
        System.out.println(Arrays.toString(s3));

    }

    public static int[] getsWarmerIn(int[] temperatures) {
        int length = temperatures.length;
        int[] warmer = new int[length];

        for (int i = 0; i < length; ++i) {
            int counter = 0;

            for (int j = i + 1; j < length; ++j) {
                if (j == length - 1 && temperatures [j] <= temperatures[i]) {
                    counter = 0;
                    break;
                }

                counter++;

                if (temperatures[j] > temperatures[i]) {
                    break;
                }
            }

            warmer[i] = counter;
        }

        return warmer;
    }
}
