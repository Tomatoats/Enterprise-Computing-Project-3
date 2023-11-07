package baseline.pack;

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


                    for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                        //We are using non property style for making dynamic table


                    }

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

                    for (int i = 0; i < data.size(); i++) {
                        AreaResults.appendText(newData.get(i));
                        AreaResults.appendText("\n");
                    }
                    //TableResults.setItems(data);
                    //int numberOfColumns = metaData.getColumnCount();
                    //TableResults.colum

                    statement.close();
                    connection.close();
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
                    text.concat(rows);
                    text.concat(changed);

                    makeABox(text,"Successful Update");



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
        DisplayBikes.Setup();

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

    public void getTable (JTable resultTable){
        //AreaResults.equals(resultTable);
    }

}
