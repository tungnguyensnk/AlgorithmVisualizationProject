package com.projectoop.algorithm;

import com.projectoop.model.Graph;
import com.projectoop.step.PseudoStep;

import java.util.ArrayList;

public abstract class Algorithm {
    Graph data = new Graph();

    ArrayList<PseudoStep> pseudoSteps = new ArrayList<>();

    public Graph getData() {
        return data;
    }

    public void setData(Graph data) {
        this.data = data;
    }

    ArrayList<String> pseudoTexts = new ArrayList<>();

    public ArrayList<String> getPseudoTexts() {
        return pseudoTexts;
    }

    public ArrayList<PseudoStep> getPseudoSteps() {
        return pseudoSteps;
    }

    public void setPseudoSteps(ArrayList<PseudoStep> pseudoSteps) {
        this.pseudoSteps = pseudoSteps;
    }

    public abstract void explore();
}
