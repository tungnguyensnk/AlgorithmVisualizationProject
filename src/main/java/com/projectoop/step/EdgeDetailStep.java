package com.projectoop.step;

import com.projectoop.model.Edge;
import com.projectoop.model.Graph;
import javafx.scene.paint.Color;

public class EdgeDetailStep extends DetailStep {
    Edge edge;

    public EdgeDetailStep(String text, boolean isHighlighted) {
        super(text, isHighlighted);
    }

    public EdgeDetailStep(String text, boolean isHighlighted, Edge edge) {
        super(text, isHighlighted);
        this.edge = edge;
    }

    @Override
    public void run() {
        if (isHighlighted) {
            edge.setStroke(Color.rgb(82, 182, 255));
            edge.setStrokeWidth(6);
        } else {
            edge.setStroke(Color.BLACK);
            edge.setStrokeWidth(3);
        }
    }
}
