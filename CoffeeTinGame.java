import utils.TextIO;

import java.util.Arrays;
import java.util.Random;

/**
 * @overview A program that performs the coffee tin game on a
 *    tin of beans and displays the result on the standard output.
 * The game starts with a tin of beans, which can be either blue or green.
 * Players take turns removing two beans at a time from the tin. If the two beans are the same color,
 * they are discarded and replaced with one blue bean.
 */

public class CoffeeTinGame {
    /**
     * constant value for the green bean
     */
    private static final char GREEN = 'G';
    /**
     * constant value for the blue bean
     */
    private static final char BLUE = 'B';
    /**
     * constant for removed beans
     */
    private static final char REMOVED = '-';
    /**
     * the null character
     */
    private static final char NULL = '\u0000';

    /**
     * the main procedure
     *
     * @effects initialise a coffee tin
     * {@link TextIO#putf(String, Object...)}: print the tin content
     * {@link @tinGame(char[])}: perform the coffee tin game on tin
     * {@link TextIO#putf(String, Object...)}: print the tin content again
     * if the last bean is correct
     * {@link TextIO#putf(String, Object...)}: print its colour
     * else
     * {@link TextIO#putf(String, Object...)}: print an error message
     */

    public static void main(String[] args) {
        // initialise some beans
        char[][] tins = {
                {BLUE, BLUE, BLUE, GREEN, GREEN},
                {BLUE, BLUE, BLUE, GREEN, GREEN, GREEN},
                {GREEN},
                {BLUE},
                {BLUE, GREEN}
        };

        for (char[] tin : tins) {
            // expected last bean
            // p0 = green parity /\
            // (p0=1 -> last=GREEN) /\ (p0=0 -> last=BLUE)
            // count number of greens
            int greens = 0;
            for (char bean : tin) {
                if (bean == GREEN)
                    greens++;
            }

            // expected last bean
            final char last = (greens % 2 == 1) ? GREEN : BLUE;

            // print the content of tin before the game
            System.out.printf("%nTIN (%d Gs): %s %n", greens, Arrays.toString(tin));

            // perform the game
            // get actual last bean
            char lastBean = tinGame(tin);
            // lastBean = last \/ lastBean != last

            // print the content of tin and last bean
            System.out.printf("tin after: %s %n", Arrays.toString(tin));

            // check if last bean as expected and print
            if (lastBean == last) {
                System.out.printf("last bean: %c%n", lastBean);
            } else {
                System.out.printf("Oops, wrong last bean: %c (expected: %c)%n", lastBean, last);
            }
        }
    }

    /**
     * Performs the coffee tin game to determine the colour of the last bean
     *
     * @requires tin is not null /\ tin.length > 0
     * @modifies tin
     * @effects <pre>
     *   Take out two beans from tin
     *   if the same colour
     *     throw both away, put one blue bean back
     *   else
     *     Put the green bean back
     *   let p0 = initial number of green beans
     *   if p0 = 1
     *     result = `G'
     *   else
     *     result = `B'
     *   </pre>
     */

    // Change this existing procedure to public: ''tinGame'' from private
    public static char tinGame(char[] tin) {
        while (hasAtLeastTwoBeans(tin)) {
            updateTin(tin);
        }
        return anyBean(tin);
    }

    /**
     * @effects if tin has at least two beans
     * return true
     * else
     * return false
     */

    private static boolean hasAtLeastTwoBeans(char[] tin) {
        int count = 0;
        for (char bean : tin) {
            if (bean != REMOVED) {
                count++;
            }
            if (count >= 2) // enough beans
                return true;
        }
        // not enough beans
        return false;
    }

    /**
     * @requires tin has at least 2 beans left
     * @modifies tin
     * @effects remove any two beans from tin and return them
     */

    private static char[] takeTwo(char[] tin) {
        char first = takeOne(tin);
        char second = takeOne(tin);

        return new char[]{first, second};
    }

    /**
     * @requires tin has at least one bean
     * @modifies tin
     * @effects remove any bean from tin and return it
     */

    // Change this existing procedure to public: ''takeOne'' from private
    public static char takeOne(char[] tin) {
        for (int i = 0; i < tin.length; i++) {
            char bean = tin[i];
            if (bean != REMOVED) {  // found one
                tin[i] = REMOVED;
                return bean;
            }
        }
        // no beans left
        return NULL;
    }

    /**
     * @requires tin has vacant positions for new beans
     * @modifies tin
     * @effects place bean into any vacant position in tin
     */

    private static void putIn(char[] tin, char bean) {
        for (int i = 0; i < tin.length; i++) {
            if (tin[i] == REMOVED) { // vacant position
                tin[i] = bean;
                break;
            }
        }
    }

    /**
     * @effects if there are beans in tin
     * return any such bean
     * else
     * return '\u0000' (null character)
     */

    private static char anyBean(char[] tin) {
        for (char bean : tin) {
            if (bean != REMOVED) {
                return bean;
            }
        }
        // no beans left
        return NULL;
    }

    /**
     * Create a constant named BeansBag that represents the bag of available beans
     *
     * @requires create an array with a length of at least 30
     * BeansBag needs to be an array whose length is at least 30, which contains blue beans, green beans,
     * and available spaces
     * Each type should account for roughly one-third
     */

    private static final char[] BeansBag = new char[30];
    static {
        int numBeans = BeansBag.length / 3;
        // get the number of beans per color by dividing the array length by 3
        // fill the array with blue and green beans
        for (int i = 0; i < numBeans; i++) {
            BeansBag[i] = BLUE;
            BeansBag[i + numBeans] = GREEN;
        }
        // fill the remaining available spaces with null characters, accounting for roughly one-third
        for (int i = numBeans * 2; i < BeansBag.length; i++) {
            BeansBag[i] = '-';
        }
    }

    public static int randInt(int n) {
        Random rand = new Random();
        return rand.nextInt(n);
    }

    // take as input an array of beans and a bean type (B or G), and look inside the array
    // find and return a randomly-selected bean that matches the bean type
    public static char getBean(char type) {
        int[] matchingIndices = new int[BeansBag.length];
        int count = 0;
        for (int i = 0; i < BeansBag.length; i++) {
            if (BeansBag[i] == type) {
                matchingIndices[count++] = i;
            }
        }
        if (count == 0) {
            return NULL; // No beans of this type left
        }
        int index = matchingIndices[randInt(count)];
        char bean = BeansBag[index];
        BeansBag[index] = NULL; // Remove the bean from the bag
        return bean;
    }

    // takes as input the tin and two beans and updates the tin accordingly
    public static void updateTin(char[] tin) {
        char[] twoBeans = takeTwo(tin);
        char bean1 = twoBeans[0];
        char bean2 = twoBeans[1];

        if (bean1 == bean2) {
            putIn(tin, getBean(BLUE)); // Get blue beans from BeansBag
        } else {
                putIn(tin, getBean(GREEN)); // Get green beans from BeansBag
        }
    }

}
