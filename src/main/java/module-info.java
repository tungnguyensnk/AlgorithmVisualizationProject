module java.com.projectoop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.projectoop to javafx.fxml;
    exports com.projectoop;
    exports com.projectoop.controller;
    opens com.projectoop.controller to javafx.fxml;
}