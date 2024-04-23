import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.io.File;
import java.util.TreeMap;

/**
 * Implements the ActionListener interface to handle user events in a GUI component.
 * Processes Roman numerals from a text file.
 */


public class FileMenuHandler implements ActionListener {
    protected static File f; // Selected file
    protected static TextFileInput in;
    protected static String line; // Current line being read
    private RomanNumeralGUI gui;
    static int Number = 0; // Current arabic number
/**
     * Constructor that takes a RomanNumeralGUI object as a parameter and assigns it to the gui instance variable.
     * @param gui the RomanNumeralGUI object to be assigned to gui
     */

    public FileMenuHandler(RomanNumeralGUI gui) {
        this.gui = gui;
    }

/**
    * If "Open", a dialog box is displayed prompting the user to select a text file.
    * Reads in each line of the file, tokenizes the line using commas as delimiters, and creates a RomanNumeral object for each token.
    * The RomanNumeral objects are added to sorted tree map.
    * If an IllegalRomanNumeralException is caught while creating a RomanNumeral object, an error message is displayed.
    * The data in the sorted tree map is then passed to the RomanNumeralGUI object for display.
    * If "Quit", gui is closed and program is exited.
    * @param event the ActionEvent object that triggered this method call
    */
    public void actionPerformed(ActionEvent event) {
        String menuName = event.getActionCommand();
        if (menuName.equals("Open")) { //If "Open", a dialog box is displayed prompting the user to select a text file.
            JFileChooser fd = new JFileChooser();
            fd.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fd.showOpenDialog(null);
            f = fd.getSelectedFile(); //f set to selected file 
            in = new TextFileInput(f.getAbsolutePath()); //reads in file    
            line = in.readLine(); //reads first line
            TreeMap<RomanNumeral, Integer> treeMap = new TreeMap<>(new RomanComparator()); //creates a sorted tree map with roman numerals and their arabic value 

            //Reads in file and tokenizes 
            while (line != null) { //Continues until the end of the file
                StringTokenizer tokenizer = new StringTokenizer(line, ","); //tokenizes 

                while (tokenizer.hasMoreTokens()) { //While there still are more roman numerals
                    String RomanNum = tokenizer.nextToken(); //Proceeds to next roman numeral
                    try {
                        RomanNumeral roNum = new RomanNumeral(RomanNum);  //Creates a RomanNumeral object "roNum"
                        int Number = RomanNumeral.RomanToArabic(RomanNum); //Number is set to the arabic value of the current roman numeral
                        treeMap.put(roNum, Number); // Add the roman numeral and its arabic value to the TreeMap
                    } //try
                    catch (IllegalRomanNumeralException ex) { // Display an error message if the Roman numeral is invalid
                        JOptionPane.showMessageDialog(null, "Invalid");
                        System.out.println("Caught");
                        System.out.println(ex.getMessage());
                    }// catch
                } //inner while

                line = in.readLine(); //Reads in next line
            } //outer while

            in.close(); //Closes file
            gui.updateData(treeMap); //writes to gui
        } // if click open
        
            else if (menuName.equals("Quit")) {
            gui.dispose(); //closes gui and exits program
        } //else if 

    } // actionPerformed

} // class