package com.projectoop;

import com.projectoop.controller.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class App extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("app.fxml"));
        Parent root = loader.load();
        AppController.setStage(stage);
        stage.setTitle("Algorithm Visualization");
        Scene scene = new Scene(root);
        AppController.getStage().setScene(scene);
        /*
         * set màu và kiểu trong suốt để lấy bo góc tuyệt đối
         * tạo khả năng movable
         */
        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });
        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        AppController.getStage().setScene(scene);
        AppController.getStage().setAlwaysOnTop(true);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}