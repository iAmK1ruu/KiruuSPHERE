module com.kiruu.kiruusphere {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.kiruu.kiruusphere to javafx.fxml;
    exports com.kiruu.kiruusphere;
}