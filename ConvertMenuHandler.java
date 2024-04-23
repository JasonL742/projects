import javax.swing.*;
import java.awt.event.*;
/**
 * A class that implements the ActionListener interface to handle user events in a GUI component.
 * This class is specifically designed to handle conversions of Roman numerals to Arabic numerals.
 */
public class ConvertMenuHandler implements ActionListener {
   JFrame jframe;
   static String romanNum;
   static  int num = 0;
/**
    * Constructor that takes a JFrame object as a parameter and assigns it to the jframe instance variable.
    * @param jf the JFrame object to be assigned to jframe
    */
   public ConvertMenuHandler (JFrame jf) {
      jframe = jf;
   } //ConvertMenuHandler

   /**
    * Method that is called when an action occurs in a GUI component.
    * If the action command is "Roman to Arabic", a dialog box is displayed prompting the user to enter a Roman numeral.
    * Converts the Roman numeral to an Arabic numeral using a method called RomanToArabic from a class named RomanNumeral.
    * If the conversion is successful, the Arabic numeral is displayed in another dialog box.
    * If the conversion fails, an IllegalRomanNumeralException is caught and an error message is displayed.
    * @param event the ActionEvent object that triggered this method call
    */

   public void actionPerformed(ActionEvent event) {
      
      String menuName = event.getActionCommand();
      if (menuName.equals("Roman to Arabic")){ 
         romanNum = JOptionPane.showInputDialog(null,"Enter Roman Numeral");
         //If the action command is "Roman to Arabic", a dialog box is displayed prompting the user to enter a Roman numeral.
        
         try{
            RomanNumeral roNum = new RomanNumeral(romanNum); //creates new roman numeral object
            num =  RomanNumeral.RomanToArabic(romanNum); //num is set the the arabic value of the roman numeral
            JOptionPane.showMessageDialog(null,num); //displays the converted roman numeral
               
         } //try
         catch (IllegalRomanNumeralException ex){ //if the creation of RomanNumeral object fails
           JOptionPane.showMessageDialog(null,"Invalid"); //displays error message
            System.out.println ("Caught"); //Print error to console
            System.out.println(ex.getMessage()); //Print the message from MyException object
         } //catch
        
       
            
        
      } // if button clicked
     }  //actionPerformed
   } //class
   
