module baseline.pack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires java.naming;
    requires java.desktop;


    opens baseline.pack to javafx.fxml;
    exports baseline.pack;
}