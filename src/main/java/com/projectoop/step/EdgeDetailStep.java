package com.projectoop.step;

import com.projectoop.model.Edge;

public class EdgeDetailStep extends DetailStep {
    Edge edge;

    public EdgeDetailStep(String text, boolean isHighlighted) {
        super(text, isHighlighted);
    }

    public EdgeDetailStep(String text, boolean isHighlighted, Edge edge) {
        super(text, isHighlighted);
        this.edge = edge;
    }
}
