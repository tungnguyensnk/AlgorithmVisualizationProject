package com.projectoop.model;

import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class Edge extends Line {
    private Vertex from, to;
    int length;

    public Vertex getFrom() {
        return from;
    }

    public void setFrom(Vertex from) {
        this.from = from;
        setStartX(from.getLayoutX() + 22);
        setStartY(from.getLayoutY() + 22);
    }

    public Vertex getTo() {
        return to;
    }

    public void setTo(Vertex to) {
        this.to = to;
        setEndY(to.getLayoutY() + 22);
        setEndX(to.getLayoutX() + 22);
    }

    Label label = new Label("");

    public Label getLabel() {
        return label;
    }

    public Edge(Vertex vertex1, Vertex vertex2) {
        setTo(vertex2);
        setFrom(vertex1);
        length = (int) Math.sqrt(
                Math.pow(getStartX() - getEndX(), 2) + Math.pow(getStartY() - getEndY(), 2)
        );

        label.setText("   " + length + "   ");
        label.setLayoutX((getStartX() + getEndX()) / 2 - 20);
        label.setLayoutY((getStartY() + getEndY()) / 2 - 20);
        label.setViewOrder(98);
        if (!Graph.isShowWeight())
            label.setVisible(false);

        setStrokeWidth(3);
        setViewOrder(99);

        label.setStyle("-fx-background-radius: 20px; -fx-background-color: #dddad6;" +
                "-fx-pref-height: 40px;-fx-pref-width: 40px;-fx-alignment: center");
    }
}
