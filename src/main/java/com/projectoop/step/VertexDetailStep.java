package com.projectoop.step;

import com.projectoop.model.Graph;
import com.projectoop.model.Vertex;

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
        Graph.highlight(vertex,isHighlighted);
    }
}
