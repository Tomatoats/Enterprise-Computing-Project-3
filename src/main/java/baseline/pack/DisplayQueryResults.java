package baseline.pack;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;


public class DisplayQueryResults extends JFrame
{
    // default query retrieves all data from bikes table
    static final String DEFAULT_QUERY = "SELECT * FROM bikes";

    private ResultSetTableModel tableModel;
    private JTextArea queryArea;

    private JPanel panel;

    public JLabel connectLabel;

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
            Border border = BorderFactory.createLineBorder(Color.BLACK);
            queryArea.setBorder(BorderFactory.createCompoundBorder(border,
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

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

            Box interStep = Box.createVerticalBox();
            Label details = new Label("Connection Details");
            interStep.add(details);

            JPanel propPanel  = new JPanel(new GridLayout(4,2,10,10));

            //propPanel.add(details);
            //Label empty = new Label(" test");
            //propPanel.add(empty);
            //propPanel.add(empty);
            Label dburl = new Label ("DB URL Properties");
            propPanel.add(dburl);
            comboDBURL = new JComboBox<>(s1);
            propPanel.add(comboDBURL);
            Label userprop = new Label ("User Properties");
            propPanel.add(userprop);
            comboUser = new JComboBox<>(s2);
            propPanel.add(comboUser);
            Label username = new Label ("Username");
            propPanel.add(username);
            userField = new JTextField();
            propPanel.add(userField);
            Label password = new Label (" Password");
            propPanel.add(password);
            passField = new JTextField();
            propPanel.add(passField);
            //propPanel.add(connectButton);
            //propPanel.add(empty);
            //box.add(propPanel);
            interStep.add(propPanel);

            JButton connectButton = new JButton( "Connect to Database" );
            interStep.add(connectButton);

            JPanel executePanel = new JPanel(new GridLayout(2,1,5,5));
            Label enter = new Label("Enter an SQL Function");
            executePanel.add(enter);
            executePanel.add(queryArea);

            JPanel labelingPanel = new JPanel(new GridLayout(1,1,5,5));
            connectLabel = new JLabel("Please Connect");
            labelingPanel.setBorder(BorderFactory.createCompoundBorder(border,
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            labelingPanel.add(connectLabel);

            JPanel  buttonsPanel = new JPanel(new GridLayout(1,2,5,5));
            Button clearSQLButton = new Button("Clear SQL Command");
            Button executeSQLButton = new Button("Execute SQL Command");
            buttonsPanel.add(clearSQLButton);
            clearSQLButton.setBackground(Color.BLUE);
            clearSQLButton.setForeground(Color.WHITE);
            //submitButton.setBorderPainted(false);
            //submitButton.setOpaque(true);

            buttonsPanel.add(executeSQLButton);
            executeSQLButton.setBackground(Color.GREEN);
            executeSQLButton.setForeground(Color.WHITE);

            buttonsPanel.setSize(25,25);

            JPanel topPanel = new JPanel(new GridLayout(2,2,5,5));
            topPanel.add(interStep);
            topPanel.add(executePanel);
            topPanel.add(labelingPanel);
            topPanel.add(buttonsPanel);


            Box panel1 = Box.createHorizontalBox();
            //panel1.add(propPanel);
            panel1.add(topPanel);
            // need to tad this grid propPanel layout, and then a vertical layout, and put that all
            //in a horizontal plane. then put the connected label and the clear / execute in hori plane


            Button clearTableButton = new Button("Clear Result Window");

            // create JTable delegate for tableModel
            JTable resultTable = new JTable( tableModel );
            resultTable.setGridColor(Color.BLACK);

            Box allConnected = Box.createVerticalBox();
            allConnected.add(topPanel);
            allConnected.add(new JScrollPane(resultTable));
            allConnected.add(clearTableButton);

            // place GUI components on content pane

            add(allConnected, BorderLayout.NORTH);
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


            clearTableButton.addActionListener(

                    new ActionListener()
                    {
                        // pass query to table model
                        public void actionPerformed( ActionEvent event )
                        {

                           JTable resultTable = new JTable(tableModel);
                         }

                         // end actionPerformed
                    }  // end ActionListener inner class
            ); // end call to addActionListener

            connectButton.addActionListener(

                    new ActionListener()
                    {
                        // pass query to table model
                        public void actionPerformed( ActionEvent event )
                        {
                        String enteredUser = userField.getText();
                        String enteredPass = passField.getText();
                        //regex(enteredUser,enteredPass);


                        }

                        // end actionPerformed
                    }  // end ActionListener inner class
            );



            setSize( 1280, 1024 ); // set window size - formerly 600x300
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

    public boolean Regex(String user, String pass){

        String actualUser = UserGet("in//" +  (String) comboUser.getSelectedItem());
        String actualPass = PassGet("in//" + (String) comboUser.getSelectedItem());
        boolean flag = false;
        //String list[] = {"root","client1","client2"};
        if (user.equals(actualUser) && pass.equals(actualPass)){
            flag = true;
        }
        else {
            flag = false;
            connectLabel.setText("Invalid user / pass combo");

        }
        return flag;

    }

    private String UserGet(String file){
        String toRet = "";
        //System.out.printf(file);
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;
        FileInputStream fileuser = null;

        try {
            filein = new FileInputStream(file);
            properties.load(filein);
            dataSource = new MysqlDataSource();
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            //dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
            toRet = dataSource.getUser();
            //System.out.println(toRet);
            return toRet;
        }
        // end catch
        catch (IOException e) {
            e.printStackTrace();
        }

        return toRet;
    }
    private String PassGet(String file){
        String toRet = "";
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;
        FileInputStream fileuser = null;

        try {
            filein = new FileInputStream(file);
            properties.load(filein);
            dataSource = new MysqlDataSource();
            // dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
            toRet = dataSource.getPassword();
            //System.out.println(toRet);
            return toRet;
        }
        // end catch
        catch (IOException e) {
            e.printStackTrace();
        }

        return toRet;
    }
} // end class DisplayQueryResults
