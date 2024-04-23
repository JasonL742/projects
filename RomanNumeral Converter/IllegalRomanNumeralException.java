/**
Exception to be thrown when an invalid Roman numeral is encountered.
*/
public class IllegalRomanNumeralException extends IllegalArgumentException{
/**
Constructs a new IllegalRomanNumeralException with the specified detail message.
@param message the detail message of the exception
*/
    public IllegalRomanNumeralException(String message) {
        super (message);
     }
  
    
}
