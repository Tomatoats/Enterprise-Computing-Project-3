module baseline.pack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.naming;
    requires java.desktop;
/*
Name: Alexys Octavio Veloz
Course: CNT 4714 Fall 2023
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: October 29, 2023
Class: module-info
*/

    opens baseline.pack to javafx.fxml;
    exports baseline.pack;
}