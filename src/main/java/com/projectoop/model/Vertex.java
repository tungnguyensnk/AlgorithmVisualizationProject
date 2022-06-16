package com.projectoop.model;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class Vertex extends StackPane {
    private int idVertex;
    private final Label label = new Label();

    public int getIdVertex() {
        return idVertex;
    }

    public void setIdVertex(int id) {
        this.idVertex = id;
        label.setText(idVertex + "");
    }

    public Vertex() {
        this.setPrefSize(2, 14);
        Circle circle = new Circle();
        circle.setRadius(22);
        circle.setFill(Color.DODGERBLUE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeType(StrokeType.INSIDE);
        this.getChildren().addAll(circle, label);
    }
}
