package com.projectoop.model;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Edge extends Line {
    int length = 0, idFrom, idTo;
    Label label = new Label(length + "");

    public Label getLabel() {
        return label;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length, Pane container) {
        this.length = length;
        label.setText("   " + length + "   ");
        label.setLayoutX((getStartX() + getEndX()) / 2 - 20);
        label.setLayoutY((getStartY() + getEndY()) / 2 - 20);
        label.setViewOrder(98);
        if (!Graph.isShowWeight())
            label.setVisible(false);
        container.getChildren().add(label);
    }

    public int getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(int idFrom) {
        this.idFrom = idFrom;
    }

    public int getIdTo() {
        return idTo;
    }

    public void setIdTo(int idTo) {
        this.idTo = idTo;
    }

    public Edge() {
        setVisible(false);
        setFill(Color.BLUEVIOLET);
        setViewOrder(99);

        label.setStyle("-fx-background-radius: 20px; -fx-background-color: #dddad6;" +
                "-fx-pref-height: 40px;-fx-pref-width: 40px;-fx-alignment: center");
    }
}
