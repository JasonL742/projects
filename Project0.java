import javax.swing.*;
//Upper and lowercase letter e counter
public class Project0 {

    public static void main(String[] args) {
    String inputWord = "idk"; // placeholder to initialize the string
    
    while( inputWord.equalsIgnoreCase("stop") == false){// Loops if user input is not "stop"
    int eCount = 0; // resets count to 0 after looping
    int Ecount = 0;
 
    inputWord = JOptionPane.showInputDialog(null, "Please enter a sentence "); // prompts and takes user input
 
    if (inputWord.equalsIgnoreCase("stop") == true)System.exit(0); // Exits program if userinput is "stop"
     for( int i = 0; i < inputWord.length(); i++){ // traversing the input 
        if (inputWord.charAt(i)== 'e') eCount++; // adds 1 to eCount if there is a lower case e found
        else if (inputWord.charAt(i)== 'E') Ecount++; // if there is a upper case E, adds 1 to Ecount
    }//end of for loop
    
    JOptionPane.showMessageDialog(null, "Number of lower case e's " +  " " +  eCount  + "\n" + "Number of upper case e's " + Ecount); // output
    }// end of while
    } //main
}// class project0
