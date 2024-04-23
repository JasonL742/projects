import java.util.Objects;
import java.util.HashMap;
public class RomanNumeral{

private String RomanNum;
private int ArabicNum;
/**
 * Constructor for the RomanNumeral class. Takes a String argument representing a Roman Numeral
 * and initializes the instance variables RomanNum and ArabicNum using the RomanToArabic() method.
 *
 * @param R a String representing a Roman Numeral
 */

public RomanNumeral (String R) throws IllegalRomanNumeralException{
    
        if (!R.matches("[IVXLCDM]+")) // if illegal roman numeral  
        throw new IllegalRomanNumeralException(R);
        RomanNum = R; 
        ArabicNum = RomanToArabic(RomanNum);
        
                    
}//RomanNumeral

/**
 * Getter method for the RomanNum instance variable.
 * @return the RomanNum instance variable as a String
 */

public String getRomanNum(){
    return RomanNum;
}//getRomanNum

/**
 * Setter method for the RomanNum instance variable.
 * @param R the new value for the RomanNum instance variable
 */

public void setRomanNum(String R){
    RomanNum = R;

}//setRomanNum

/**
 * Getter method for the ArabicNum instance variable.
 * @return the ArabicNum instance variable as an int
 */

public int getArabicNum(){
    return ArabicNum;
}//getArabicNum

/**
 * Setter method for the ArabicNum instance variable.
 * @param A the new value for the ArabicNum instance variable
 */

public void setArabicNum(int A){
    ArabicNum = A;
}//setArabicNum



    /**
     * Converts a Roman numeral to its equivalent Arabic value.
     *
     * @param RomanNum The Roman numeral to convert
     * @return The equivalent Arabic value
     * @throws IllegalRomanNumeralException if the Roman numeral is invalid
     */

public static int RomanToArabic( String RomanNum)throws IllegalRomanNumeralException{
    HashMap<Character, Integer> map = new HashMap<Character, Integer>(); // Create a HashMap to map Roman numerals to their corresponding Arabic values
    map.put('I', 1);
    map.put('V', 5);
    map.put('X', 10);
    map.put('L', 50);
    map.put('C', 100);
    map.put('D', 500);
    map.put('M', 1000);
    int len = RomanNum.length(), result = map.get(RomanNum.charAt(len - 1)); 
    for (int i = len - 2; i >= 0; i--) { // Iterate through each character of the Roman numeral in reverse order
        if (map.get(RomanNum.charAt(i)) >= map.get(RomanNum.charAt(i + 1)))  
        // If the current character's value is greater than or equal to the next character's value, add it to the result
            result += map.get(RomanNum.charAt(i));
        else
          // If the current character's value is smaller than the next character's value, subtract it from the result
            result -= map.get(RomanNum.charAt(i));
         
    }//for
    return result;

    }// RomanToArabic 


/**
 * Overrides the equals() method to compare RomanNumerals based on their RomanNum and ArabicNum values.
 *
 * @param o the Object to compare to the calling RomanNumeral instance
 * @return true if the Object is equal to the calling RomanNumeral instance, false otherwise
 */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RomanNumeral that = (RomanNumeral) o;
        return ArabicNum == that.ArabicNum &&
                Objects.equals(RomanNum, that.RomanNum);
    }//equals

/**
 * Returns a string representation of the RomanNumeral object.
 * Overrides the toString() method in Object class.
 *
 * @return a string representation of the RomanNumeral object
 */
    public String toString() {
        return RomanNum;
    }//toString
    
/**
 * Compares the ArabicNum value of the calling RomanNumeral instance to that of another RomanNumeral object.
 * Overrides the compareTo() method in the Comparable interface.
 *
 * @param other the RomanNumeral object to compare to the calling RomanNumeral instance
 * @return a negative integer, zero, or a positive integer as the ArabicNum value of the calling RomanNumeral
 *         instance is less than, equal to, or greater than the ArabicNum value of the other RomanNumeral object.
 */
    public int compareTo(RomanNumeral other) {
        return Integer.compare(this.ArabicNum, other.ArabicNum);
    }//compareTo

}//class



