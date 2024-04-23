import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;
public class RomanNumeralGUI extends JFrame {   
 /**
 * Constructs a new RomanNumeralGUI with the specified title, height, and width. The GUI includes a file menu and a
 * convert menu, and is set to exit on close.
 *
 * @param title  the title of the GUI window
 * @param height the height of the GUI window
 * @param width  the width of the GUI window
 */
 
   public RomanNumeralGUI(String title, int height, int width) {
	    setTitle(title); //set title
	    setSize(height,width); //set size
       setLocation  (400,200); // set location on screen
	    createFileMenu(); //creates file menu
       createConvertMenu( ); //creates convert button
	    setDefaultCloseOperation(EXIT_ON_CLOSE); // set close operation
       setVisible(true); // make the GUI visible
   }
/**
 * Updates the data displayed in the GUI with the specified lists of Roman numerals and their Arabic values.
 * The GUI includes a table with three columns: "Roman Numerals", "Unsorted Arabic Values", and "Sorted Arabic Values".
 * @param unsortedList the list of Roman numerals and their corresponding unsorted Arabic values
 * @param sortedList   the list of Roman numerals and their corresponding sorted Arabic values
 */
public void updateData(TreeMap<RomanNumeral, Integer> treeMap) {
   // Remove all components from the panel
   getContentPane().removeAll();

   // Create a new JPanel with a GridLayout of 0 rows and 3 columns
   JPanel panel = new JPanel(new GridLayout(0, 2));

   // Create three JLabels for the column headers
   JLabel label1 = new JLabel("Roman Numerals");
   label1.setHorizontalAlignment(JLabel.CENTER);
   JLabel label2 = new JLabel("sorted Arabic Values");
   label2.setHorizontalAlignment(JLabel.CENTER);
   
   // Add the labels to the panel
   panel.add(label1);
   panel.add(label2);
   

   // Iterate over the entries in the TreeMap
   for (Map.Entry<RomanNumeral, Integer> entry : treeMap.entrySet()) {
       RomanNumeral romanNumeral = entry.getKey();
       int sortedValue = RomanNumeral.RomanToArabic(romanNumeral.toString());

       // Create three new JLabels for the current row
       JLabel romanNumeralLabel = new JLabel(romanNumeral.toString());
       JLabel unsortedValueLabel = new JLabel(Integer.toString(sortedValue));
      

       // Add the three labels for the current row to the panel
       panel.add(romanNumeralLabel);
       panel.add(unsortedValueLabel);
       
   }

   // Add the panel to the center of the content pane
   getContentPane().add(panel, BorderLayout.CENTER);

   // Resize the JFrame to fit its contents, and make it visible
   pack();
   setVisible(true);
} // update data

/**
Creates and adds the File menu to the GUI window's menu bar.
*/
   private void createFileMenu( ) {
      JMenuItem   item;
      JMenuBar    menuBar  = new JMenuBar();
      JMenu       fileMenu = new JMenu("File");
      FileMenuHandler fmh  = new FileMenuHandler(this);

      item = new JMenuItem("Open");    //Open...
      item.addActionListener( fmh );
      fileMenu.add( item );

      fileMenu.addSeparator();           //add a horizontal separator line
    
      item = new JMenuItem("Quit");       //Quit
      item.addActionListener( fmh );
      fileMenu.add( item );

      setJMenuBar(menuBar);
      menuBar.add(fileMenu);
    
   } //createFileMenu

/**
Creates and adds the Convert menu to the GUI window's menu bar.
*/

   private void createConvertMenu( ) {
    JMenuItem   item;
    JMenuBar    menuBar = getJMenuBar();
    JMenu       ConvertMenu = new JMenu("Convert");
    ConvertMenuHandler fmh  = new ConvertMenuHandler(this);

    item = new JMenuItem("Roman to Arabic");    //Open...
    item.addActionListener( fmh );
    ConvertMenu.add( item );
    setJMenuBar(menuBar);
    menuBar.add(ConvertMenu); 
 } //createConvertMenu

} //class

