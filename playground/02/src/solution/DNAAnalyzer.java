package solution;

public class DNAAnalyzer {
    public static String longestRepeatingSequence(String dna) {
        int slowPtr = 0;
        int length = 0;
        int fastPtr = slowPtr + 1;
        StringBuilder current = new StringBuilder();
        StringBuilder longest = new StringBuilder();


        while (slowPtr < dna.length()) {
            if (fastPtr >= dna.length()) {
                //clear current & length
                ++slowPtr;
                continue;
            }

            //current.append(...);

            String first = dna.substring(slowPtr, slowPtr + length);
            String second = dna.substring(fastPtr, fastPtr + length);

            if (first.equals(second)) { //for string
                //longest = current;
                //++length;
            } else {
                ++fastPtr;
            }
        }

    }
}
