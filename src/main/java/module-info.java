module com.kiruu.kiruusphere {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jshell;
    requires java.desktop;
    requires jdk.httpserver;


    opens com.kiruu.kiruusphere to javafx.fxml;
    exports com.kiruu.kiruusphere;
}