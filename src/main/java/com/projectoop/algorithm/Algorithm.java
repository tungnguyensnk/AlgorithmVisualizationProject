package com.projectoop.algorithm;

import com.projectoop.model.Graph;
import com.projectoop.step.DetailStep;
import com.projectoop.step.PseudoStep;

import java.util.ArrayList;

public abstract class Algorithm {
    Graph data = new Graph();

    ArrayList<PseudoStep> pseudoSteps = new ArrayList<>();
    ArrayList<DetailStep> detailSteps = new ArrayList<>();

    public Graph getData() {
        return data;
    }

    public void setData(Graph data) {
        this.data = data;
    }

    public ArrayList<PseudoStep> getPseudoSteps() {
        return pseudoSteps;
    }

    public void setPseudoSteps(ArrayList<PseudoStep> pseudoSteps) {
        this.pseudoSteps = pseudoSteps;
    }

    public ArrayList<DetailStep> getDetailSteps() {
        return detailSteps;
    }

    public void setDetailSteps(ArrayList<DetailStep> detailSteps) {
        this.detailSteps = detailSteps;
    }

    public abstract void explore();
}
