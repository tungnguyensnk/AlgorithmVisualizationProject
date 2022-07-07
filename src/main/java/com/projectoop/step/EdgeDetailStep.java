package com.projectoop.step;

import com.projectoop.model.Edge;
import com.projectoop.model.Graph;

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
        Graph.highlight(edge, isHighlighted);
    }
}
