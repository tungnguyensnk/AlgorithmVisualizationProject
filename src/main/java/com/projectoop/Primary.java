package com.projectoop;

import javafx.stage.Stage;

public class Primary {
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        Primary.stage = stage;
    }

}
