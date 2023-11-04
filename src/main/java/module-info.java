module baseline.pack {
    requires javafx.controls;
    requires javafx.fxml;


    opens baseline.pack to javafx.fxml;
    exports baseline.pack;
}