module com.kiruu.kiruusphere {
    requires javafx.fxml;
    requires jdk.jshell;
    requires jdk.httpserver;
    requires javafx.web;
    requires java.desktop;


    opens com.kiruu.kiruusphere to javafx.fxml;
    exports com.kiruu.kiruusphere;
}