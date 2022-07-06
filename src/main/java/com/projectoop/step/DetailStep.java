package com.projectoop.step;

public class DetailStep extends Step {
    boolean isHighlighted;

    public DetailStep(String text) {
        super(text);
    }

    public DetailStep(String text, boolean isHighlighted) {
        super(text);
        this.isHighlighted = isHighlighted;
    }
}
