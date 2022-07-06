package com.projectoop.step;

import com.projectoop.model.Edge;
import com.projectoop.model.Vertex;

public abstract class Step {
    String text;

    public String getText() {
        return text;
    }

    public Step(String text) {
        this.text = text;
    }

    public void run() {}
}
