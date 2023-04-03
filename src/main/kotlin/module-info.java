module com.example.approximation {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;


    opens com.example.approximation to javafx.fxml;
    exports com.example.approximation;
}