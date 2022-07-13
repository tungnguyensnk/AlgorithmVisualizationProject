package com.projectoop.model;

import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Vertex extends StackPane implements Comparable<Vertex> {
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
        circle.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        label.setStyle("-fx-font-family: \"Comic Sans MS\"");
        this.getChildren().addAll(circle, label);
    }

    @Override
    public String toString() {
        return getLayoutX() + " " + getLayoutY();
    }

    @Override
    public int compareTo(Vertex o) {
        if (getLayoutX() > o.getLayoutX()) return 1;
        else if (getLayoutX() < o.getLayoutX()) return -1;
        else {
            return Double.compare(o.getLayoutY(), getLayoutY());
        }
    }
}
