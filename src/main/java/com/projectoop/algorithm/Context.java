package com.projectoop.algorithm;

public class Context {
    Algorithm algorithm;

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Context(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Context() {
    }

    public void doExploration() {
        algorithm.explore();
    }
}
