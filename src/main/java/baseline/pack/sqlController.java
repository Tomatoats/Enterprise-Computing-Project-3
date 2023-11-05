package baseline.pack;

import com.mysql.cj.jdbc.MysqlDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class sqlController {
    public boolean connected;

    private ObservableList<ObservableList> data;

    ObservableList<String> DBURL =  FXCollections.observableArrayList("project3.properties", "bikedb.properties");
    ObservableList<String> users = FXCollections.observableArrayList("root.properties","client1.properties","client2.properties");

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
        if (connected){
            String command =  AreaEnterSQL.getText();
            data = FXCollections.observableArrayList();
            try {

                MysqlDataSource dataSource = null;
                dataSource = connect();

                Connection connection = dataSource.getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(command);
                //ResultSetMetaData metaData = resultSet.getMetaData();
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    //We are using non property style for making dynamic table
                    final int j = i;
                    TableColumn col = new TableColumn();
                    col.setText(resultSet.getMetaData().getColumnName(i+1));

                    //col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        //public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            //return new SimpleStringProperty(param.getValue().get(j).toString());
                        //}
                    //});
                    //TableResults.getColumns().addAll(col);
                    System.out.println("Column [" + i + "] ");
                }

                while (resultSet.next()) {
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        //Iterate Column
                        row.add(resultSet.getString(i));
                    }
                    System.out.println("Row [1] added " + row);
                    data.add(row);


                }
                for (int i = 0; i < data.size();i++){
                    System.out.println(data.get(i));
                }
                //TableResults.setItems(data);
                //int numberOfColumns = metaData.getColumnCount();
                //TableResults.colum

                statement.close();
                connection.close();
            }
            catch ( SQLException sqlException )
            {
                AreaResults.setText("Error: Unfamiliar command or Privlige not given");
                sqlException.printStackTrace();
                System.exit( 1 );
            } // end catch
        }
        else {
            labelConnected.setText("You have to connect before executing commands");
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
            labelConnected.setText("Invalid user / pass combo");

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

}
