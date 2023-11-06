// Display the results of queries against the bikes table in the bikedb database.
package baseline.pack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;


public class DisplayQueryResults extends JFrame 
{
   // default query retrieves all data from bikes table
   static final String DEFAULT_QUERY = "SELECT * FROM bikes";
   
   private ResultSetTableModel tableModel;
   private JTextArea queryArea;

   //private JPanel panel;

   private JComboBox comboDBURL, comboUser;

   private JTextField userField, passField;
   
   // create ResultSetTableModel and GUI

   public DisplayQueryResults() 
   {   
      super( "Displaying Query Results" );
        
      // create ResultSetTableModel and display database table
      try 
      {
         // create TableModel for results of query SELECT * FROM bikes
         tableModel = new ResultSetTableModel( DEFAULT_QUERY );

         // set up JTextArea in which user types queries
		//	queryArea = new JTextArea( 3, 100);
         queryArea = new JTextArea( DEFAULT_QUERY, 3, 100 );
         queryArea.setWrapStyleWord( true );
         queryArea.setLineWrap( true );
         
         JScrollPane scrollPane = new JScrollPane( queryArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
         
         // set up JButton for submitting queries
         JButton submitButton = new JButton( "Submit Query" );
         submitButton.setBackground(Color.BLUE);
         submitButton.setForeground(Color.YELLOW);
         submitButton.setBorderPainted(false);
         submitButton.setOpaque(true);
 
         
         // create Box to manage placement of queryArea and 
         // submitButton in GUI
         Box box = Box.createHorizontalBox();
         box.add( scrollPane );
         box.add( submitButton );

         String s1[] = {"project3.properties", "bikedb.properties"};
         String s2[] = {"root.properties","client1.properties","client2.properties"};

         JPanel panel  = new JPanel(new GridLayout(6,2,10,10));

         Label details = new Label("Connection Details");
         panel.add(details);
         Label empty = new Label(" test");
         panel.add(empty);
         panel.add(empty);
         Label dburl = new Label ("DB URL Properties");
         panel.add(dburl);
         comboDBURL = new JComboBox<>(s1);
         panel.add(comboDBURL);
         Label userprop = new Label ("User Properties");
         panel.add(userprop);
         comboUser = new JComboBox<>(s2);
         panel.add(comboUser);
         Label username = new Label ("Username");
         panel.add(username);
         userField = new JTextField();
         panel.add(userField);
         Label password = new Label (" Password");
         panel.add(password);
         passField = new JTextField();
         panel.add(passField);
         JButton connectButton = new JButton( "Connect to Database" );
         panel.add(connectButton);
         panel.add(empty);
         //box.add(panel);


         Box panel1 = Box.createVerticalBox();
         panel1.add(panel);
         // need to tad this grid panel layout, and then a vertical layout, and put that all
         //in a horizontal plane. then put the connected label and the clear / execute in hori plane




         // create JTable delegate for tableModel
         JTable resultTable = new JTable( tableModel );
         resultTable.setGridColor(Color.BLACK);

         // place GUI components on content pane
         //add(panel1, BorderLayout.NORTH);
         //add( box, BorderLayout.NORTH );
         add( new JScrollPane( resultTable ), BorderLayout.CENTER );


         // create event listener for submitButton
         submitButton.addActionListener( 
         
            new ActionListener() 
            {
               // pass query to table model
               public void actionPerformed( ActionEvent event )
               {
                  // perform a new query
                  try 
                  {
                     tableModel.setQuery( queryArea.getText() );
                  } // end try
                  catch ( SQLException sqlException ) 
                  {
                     JOptionPane.showMessageDialog( null, 
                        sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE );
                     
                     // try to recover from invalid user query 
                     // by executing default query
                     try 
                     {
                        tableModel.setQuery( DEFAULT_QUERY );
                        queryArea.setText( DEFAULT_QUERY );
                     } // end try
                     catch ( SQLException sqlException2 ) 
                     {
                        JOptionPane.showMessageDialog( null, 
                           sqlException2.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE );
         
                        // ensure database connection is closed
                        tableModel.disconnectFromDatabase();
         
                        System.exit( 1 ); // terminate application
                     } // end inner catch                   
                  } // end outer catch
               } // end actionPerformed
            }  // end ActionListener inner class          
         ); // end call to addActionListener

         setSize( 700, 400 ); // set window size - formerly 600x300
         setVisible( true ); // display window  
      } // end try
      catch ( ClassNotFoundException classNotFound ) 
      {
         JOptionPane.showMessageDialog( null, 
            "MySQL driver not found", "Driver not found",
            JOptionPane.ERROR_MESSAGE );
         
         System.exit( 1 ); // terminate application
      } // end catch
      catch ( SQLException sqlException ) 
      {
         JOptionPane.showMessageDialog( null, sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE );
               
         // ensure database connection is closed
         tableModel.disconnectFromDatabase();
         
         System.exit( 1 );   // terminate application
      } // end catch
      
      // dispose of window when user quits application (this overrides
      // the default of HIDE_ON_CLOSE)
      setDefaultCloseOperation( DISPOSE_ON_CLOSE );
      
      // ensure database connection is closed when user quits application
      addWindowListener(new WindowAdapter() 
         {
            // disconnect from database and exit when window has closed
            public void windowClosed( WindowEvent event )
            {
               tableModel.disconnectFromDatabase();
               System.exit( 0 );
            } // end method windowClosed
         } // end WindowAdapter inner class
      ); // end call to addWindowListener
   } // end DisplayQueryResults constructor
   
   // execute application
   public static void main( String args[] ) 
   {
      new DisplayQueryResults();     
   } // end main
} // end class DisplayQueryResults




