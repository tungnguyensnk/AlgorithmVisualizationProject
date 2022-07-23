package com.projectoop.step;

import com.projectoop.model.Graph;
import com.projectoop.model.Vertex;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class VertexDetailStep extends DetailStep {
    Vertex vertex = new Vertex();

    public VertexDetailStep(String text) {
        super(text);
    }

    public VertexDetailStep(String text, boolean isHighlighted, Vertex vertex) {
        super(text, isHighlighted);
        this.vertex = vertex;
    }

    @Override
    public void run() {
        ((Circle) vertex.getChildren().get(0)).setFill(
                isHighlighted ? Color.rgb(113, 36, 164) : Color.DODGERBLUE
        );
        ((Label) vertex.getChildren().get(1)).setTextFill(
                isHighlighted ? Color.WHITE : Color.BLACK
        );
    }
}
