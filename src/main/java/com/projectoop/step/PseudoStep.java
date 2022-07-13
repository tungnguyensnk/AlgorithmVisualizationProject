package com.projectoop.step;

import java.util.ArrayList;

public class PseudoStep extends Step {
    public PseudoStep(String text) {
        super(text);
    }

    public PseudoStep(int i) {
        super(i + "");
    }

    public ArrayList<DetailStep> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<DetailStep> detail) {
        this.detail = detail;
    }

    public void addStep(DetailStep detail) {
        getDetail().add(detail);
    }

    ArrayList<DetailStep> detail = new ArrayList<>();

}
