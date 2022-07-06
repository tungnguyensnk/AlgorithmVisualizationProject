package com.projectoop.step;

public class PseudoStep extends Step {
    public PseudoStep(String text) {
        super(text);
    }

    public PseudoStep(int i) {
        super(i + "");
    }

    @Override
    public void run() {
    }
}
