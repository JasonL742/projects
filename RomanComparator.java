import java.util.Comparator;
/**
 * The RomanComparator class implements the Comparator interface to compare
 * RomanNumeral objects.
 */
    public class RomanComparator implements Comparator<RomanNumeral> {
 /**
     * Compares two RomanNumeral objects.
     *
     * @param num1 The first RomanNumeral to compare
     * @param num2 The second RomanNumeral to compare
     * @return A negative integer if num1 is less than num2, zero if they are equal,
     *         or a positive integer if num1 is greater than num2.
     */
        public int compare(RomanNumeral num1, RomanNumeral num2) {
            
            return num1.compareTo(num2);
        }

        } // class
    
    

