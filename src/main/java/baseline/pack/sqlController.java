package baseline.pack;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class sqlController {
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

    public static void Setup(){
        String temp1[] ={ "project3.properties", "bikedb.properties"} ;
        String temp2[] ={ "root.properties","client1.properties","client2.properties"};
        //ChoiceDBURL = new ChoiceBox<>(FXCollections.observableArrayList( temp1));
        //ChoiceBox ChoiceUser = new ChoiceBox<>(FXCollections.observableArrayList( temp2));
        //ChoiceUser.setItems(FXCollections.observableArrayList(temp2).sorted());

    }

    @FXML
    void ButtonConnectPressed(ActionEvent event) {
        String user = fieldUsername.getText();
        String pass = fieldPassword.getText();
        if (Regex(user,pass)){
            //todo, connect!
            labelConnected.setText("Connected!");
        }
        else{
            //Todo,don't connect
            labelConnected.setText("Invalid user / pass combo");
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
                //for each command make sure they have the proper permissions

        //Else: say that they gotta be connected first
        String command =  AreaEnterSQL.getText();
    }

    boolean Regex(String user, String pass){
        boolean flag = false;
        String list[] = {"root","client1","client2"};
        if (user.equals(list[0]) && pass.equals(list[0])){
            flag = true;
        }
        else if (user.equals(list[1]) && pass.equals(list[1])){
            flag = true;
        }

        else if (user.equals(list[2]) && pass.equals(list[2])){
            flag = true;
        }
        else {
            flag = false;
        }
        return flag;

    }
    @FXML
    private void initialize(){
        ChoiceDBURL.setValue("");
        ChoiceDBURL.setItems(DBURL);
        ChoiceUser.setValue("");
        ChoiceUser.setItems(users);

    }

}
