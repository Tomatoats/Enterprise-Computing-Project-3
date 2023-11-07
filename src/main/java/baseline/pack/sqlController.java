package baseline.pack;
/*
Name: Alexys Octavio Veloz
Course: CNT 4714 Fall 2023
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: October 29, 2023
Class: sqlController
*/
import com.mysql.cj.jdbc.MysqlDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class sqlController {
    public boolean connected;

    private ObservableList<ObservableList> data;

    ObservableList<String> DBURL =  FXCollections.observableArrayList("project3.properties", "bikedb.properties");
    ObservableList<String> users = FXCollections.observableArrayList("root.properties","client1.properties","client2.properties");

    private JScrollPane table;

    @FXML
    private TextArea AreaEnterSQL;

    @FXML
    private TextArea AreaResults;

    @FXML
    private Button ButtonClearResult;

    @FXML
    private Button ButtonClearSQL;

    @FXML
    private Button ButtonConnect;

    @FXML
    private Button ButtonExecute;

    @FXML
    private ChoiceBox<String> ChoiceDBURL;

    @FXML
    private  ChoiceBox<String> ChoiceUser;

    @FXML
    public TextField fieldPassword;

    @FXML
    public TextField fieldUsername;

    @FXML
    public Label labelConnected;
    @FXML
    private TableView<ObservableList> TableResults;

    public static void Setup(){
        //String temp1[] ={ "project3.properties", "bikedb.properties"} ;
        //String temp2[] ={ "root.properties","client1.properties","client2.properties"};
        //ChoiceDBURL = new ChoiceBox<>(FXCollections.observableArrayList( temp1));
        //ChoiceBox ChoiceUser = new ChoiceBox<>(FXCollections.observableArrayList( temp2));
        //ChoiceUser.setItems(FXCollections.observableArrayList(temp2).sorted());

    }

    @FXML
    void ButtonConnectPressed(ActionEvent event) {
        String user = fieldUsername.getText();
        String pass = fieldPassword.getText();
        if (Regex(user,pass)){
            String connect = URLGet("in//"+ChoiceDBURL.getValue());
            labelConnected.setText("CONNECTED TO: " + connect);
            connected = true;
        }
        else{
            //labelConnected.setText("Invalid user / pass combo");
            connected = false;
        }
    }

    @FXML
    void ButtonClearResultPressed(ActionEvent event) {
        AreaResults.clear();
    }

    @FXML
    void ButtonClearSQLPressed(ActionEvent event) {
        AreaEnterSQL.clear();
    }

    @FXML
    void ButtonExecutePressed(ActionEvent event) {
        //todo: take this and make the sql command
        //IF: Connected - COntinue on and make sure the sql command is correct
        if (connected) {
            String command = AreaEnterSQL.getText();
            data = FXCollections.observableArrayList();
            ArrayList<String> newData = new ArrayList<>();
            if (command.contains("select")) {


                try {

                    MysqlDataSource dataSource = null;
                    dataSource = connect();

                    Connection connection = dataSource.getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(command);
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int numberOfColumns = metaData.getColumnCount();

                    for ( int i = 1; i <= numberOfColumns; i++ )
                        System.out.printf( "%-20s\t", metaData.getColumnName( i ) );

                    System.out.println();



                    /*
                    while (resultSet.next()) {
                        //Iterate Row
                        ObservableList<String> row = FXCollections.observableArrayList();
                        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                            //Iterate Column
                            row.add(resultSet.getString(i));
                        }
                        System.out.println("Row [1] added " + row);
                        newData.add(row.toString());
                        data.add(row);


                    }
                    */


                    for (int i = 0; i < data.size(); i++) {
                        AreaResults.appendText(newData.get(i));
                        AreaResults.appendText("\n");
                    }
                    //TableResults.setItems(data);
                    //int numberOfColumns = metaData.getColumnCount();
                    //TableResults.colum

                    statement.close();
                    connection.close();
                    updateLog(fieldUsername.getText(),true);
                } catch (SQLException sqlException) {
                    String error =  sqlException.getMessage();
                    makeABox(error, "Database Error");
                } // end catch
            }
            else {
                try {
                    MysqlDataSource dataSource = null;
                    dataSource = connect();

                    Connection connection = dataSource.getConnection();
                    Statement statement = connection.createStatement();
                    int result =  statement.executeUpdate(command);
                    String text = "Succesful update...";
                    String rows = String.valueOf(result);
                    String changed = " rows changed.";
                    text = text.concat(rows);
                    text =  text.concat(changed);

                    makeABox(text,"Successful Update");
                    updateLog(fieldUsername.getText(),false);



                }
                catch (SQLException sqlException) {
                    //AreaResults.setText("Error: Unfamiliar command or Privlige not given");
                    String error =  sqlException.getMessage();
                    makeABox(error, "Database Error");
                    //sqlException.printStackTrace();
                    //System.exit(1);
                }
            }
        }
        else {
            //labelConnected.setText("You have to connect before executing commands");
            //new DisplayQueryResults();

        }
                //for each command make sure they have the proper permissions

        //Else: say that they gotta be connected first
        String command =  AreaEnterSQL.getText();
    }
    @FXML
    boolean Regex(String user, String pass){
        if (ChoiceDBURL.getValue().equals("")){
            labelConnected.setText("Please choose which Database Property to use");
            return false;
        }
        if (ChoiceUser.getValue().equals("")){
            labelConnected.setText("Please choose which User Property to use");
            return false;
        }
        String actualUser = UserGet("in//" +  ChoiceUser.getValue());
        String actualPass = PassGet("in//" + ChoiceUser.getValue());
        boolean flag = false;
        //String list[] = {"root","client1","client2"};
        if (user.equals(actualUser) && pass.equals(actualPass)){
            flag = true;
        }
        else {
            flag = false;
            labelConnected.setText("NOT CONNECTED - User Credentials Do Not Match Properties File!");

        }
        return flag;

    }
    @FXML
    private void initialize(){
        ChoiceDBURL.setValue("");
        ChoiceDBURL.setItems(DBURL);
        ChoiceUser.setValue("");
        ChoiceUser.setItems(users);
        //DisplayBikes.Setup();

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

    private String URLGet(String file){
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
            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));

            toRet = dataSource.getURL();
            //System.out.println(toRet);
            return toRet;
        }
        // end catch
        catch (IOException e) {
            e.printStackTrace();
        }

        return toRet;
    }
    @FXML
    public MysqlDataSource connect(){
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;
        FileInputStream fileuser = null;
        try {
            filein = new FileInputStream("in//"+ ChoiceDBURL.getValue());
            properties.load(filein);
            dataSource = new MysqlDataSource();

            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            fileuser = new FileInputStream("in//"+ChoiceUser.getValue());
            properties.load(fileuser);
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
            return dataSource;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return  dataSource;
    }

    private void makeABox(String temp, String title) {
        Stage stage = new Stage();
        VBox layout = new VBox(2);
        Label label = new Label(temp);
        layout.getChildren().add(label);
        Button button = new Button();
        button.setText("Okay");
        button.setOnAction(value -> {
            Stage curStage = (Stage) button.getScene().getWindow();
            curStage.close();
        });
        layout.getChildren().add(button);
        Scene scene = new Scene(layout);
        layout.setMinSize(400, 150);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }

    public void updateLog (String user, Boolean didQuery){
        Properties properties = new Properties();
        FileInputStream filein = null;
        MysqlDataSource dataSource = null;
        FileInputStream fileuser = null;
        try {
            filein = new FileInputStream("in//operationslog.properties");
            properties.load(filein);
            dataSource = new MysqlDataSource();

            dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
            fileuser = new FileInputStream("in//project3app.properties");
            properties.load(fileuser);
            dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
            dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));

            Connection connection = dataSource.getConnection();

            // create Statement for querying database
            Statement statement = connection.createStatement();
            //String keys[] = new String[3];
            //ResultSet resultSet = connection.getMetaData();
            int rowcount = 0;
            String querying = "select count(*) from operationscount where login_username = \"";
            String temp = fieldUsername.getText();
            querying =  querying.concat(temp);
            temp = "\";";
            querying = querying.concat(temp);
            System.out.println(querying);

            ResultSet resultSet = statement.executeQuery(querying );
            resultSet.next();
            rowcount = resultSet.getInt(1);

            System.out.println(rowcount);
            if (rowcount == 0){
                String command = "insert into operationscount (login_username, num_queries, num_updates) VALUES ('";
                command = command.concat(user);
                command = command.concat("',");
                if (didQuery == true){
                    command = command.concat("1,0);");
                    int result = statement.executeUpdate(command);
                }
                else {
                    command = command.concat("0,1);");
                    int result = statement.executeUpdate(command);
                }

            }
            else{

                querying = "select * from operationscount where login_username = \"";
                temp = fieldUsername.getText();
                querying =  querying.concat(temp);
                temp = "\";";
                querying = querying.concat(temp);
                System.out.println(querying);

                resultSet = statement.executeQuery(querying );
                resultSet.next();

                String command = "update operationscount set ";
                 if (didQuery == true){
                     //update and ++query
                     int number =  resultSet.getInt("num_queries");

                     number++;
                     temp = "num_queries = ";
                     temp = temp.concat(String.valueOf(number));
                     command = command.concat(temp);
                     command = command.concat(" where login_username = \"");
                     temp = fieldUsername.getText();
                     command = command.concat(temp);
                     command = command.concat("\";");

                     int result = statement.executeUpdate(command);

                 }
                 else {
                     //update and ++updates
                     int number =  resultSet.getInt("num_updates");
                     String nameOfColumn = resultSet.getMetaData().getColumnName(1);
                     System.out.println(nameOfColumn);
                     System.out.println("number = "+number);
                     number++;
                     System.out.println("number now equals "+number);
                     temp = "num_updates = ";
                     temp = temp.concat(String.valueOf(number));
                     command = command.concat(temp);
                     command = command.concat(" where login_username = \"");
                     temp = fieldUsername.getText();
                     command = command.concat(temp);
                     command = command.concat("\";");

                     System.out.println(command);

                     int result = statement.executeUpdate(command);
                 }

            }
            //check
            //int result = statement.executeUpdate("");
            statement.close();
            connection.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    public void getTable (JTable resultTable){
        //AreaResults.equals(resultTable);
    }

}
