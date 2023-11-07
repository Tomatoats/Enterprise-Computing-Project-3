package baseline.pack;

/*
Name: Alexys Octavio Veloz
Course: CNT 4714 Fall 2023
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: October 29, 2023
Class: mainApplication
*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(mainApplication.class.getResource("sqlComplete.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        //sqlController.Setup();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}