package com.projectoop.step;

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
