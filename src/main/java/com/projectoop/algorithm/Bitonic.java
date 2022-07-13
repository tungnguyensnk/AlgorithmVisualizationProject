package com.projectoop.algorithm;

import com.projectoop.model.Vertex;
import com.projectoop.step.DetailStep;
import com.projectoop.step.PseudoStep;
import com.projectoop.step.VertexDetailStep;

import java.util.ArrayList;
import java.util.Collections;

public class Bitonic extends Algorithm {
    ArrayList<Vertex> list;
    int[][] ltable;
    int[][] ntable;

    public Bitonic() {
        pseudoTexts.add("sort by (x, y) coordinates\n");
        pseudoTexts.add("function f(p1, p2)\n");
        pseudoTexts.add("if visited all nodes: return d[p1][v]+d[v][p2]\n");
        pseudoTexts.add("if (p1, p2) in memo: return memo[p1][p2]");
        pseudoTexts.add("ltr = d[p1][v] + f(v, p2)\n");
        pseudoTexts.add("rtl = d[v][p2] + f(p1, v)\n");
        pseudoTexts.add("return memo[p1][p2] = min(ltr, rtl)\n");
    }

    @Override
    public void explore() {
        list = (ArrayList<Vertex>) getData().getVertexes().clone();
        Collections.sort(list);
        ntable = new int[list.size()][list.size()];
        ltable = new int[list.size()][list.size()];
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                ntable[i][j] = -1;
            }
        }
        PseudoStep end = new PseudoStep(-1);
        end.addStep(new DetailStep("The optimum tour cost for this graph is " + bitonic() + "."));
        pseudoSteps.add(end);
    }

    public int bitonic() {
        int tempDist;
        tempDist = getData().getLength(list.get(0).getIdVertex(), list.get(1).getIdVertex());
        ltable[0][1] = tempDist;
        ntable[0][1] = 0;
        int temp;
        int min;
        for (int j = 2; j < list.size(); j++) {
            for (int i = 0; i < j; i++) {
                min = Integer.MAX_VALUE;
                if (j <= (i + 1)) {
                    for (int k = 0; k < i; k++) {
                        tempDist = getData().getLength(list.get(k).getIdVertex(), list.get(j).getIdVertex());
                        temp = ltable[k][i] + tempDist;
                        if (min > temp) {
                            min = temp;
                            ntable[i][j] = k;
                        }
                    }
                    ltable[i][j] = min;
                } else {
                    tempDist = getData().getLength(list.get(j - 1).getIdVertex(), list.get(j).getIdVertex());
                    ltable[i][j] = ltable[i][j - 1] + tempDist;
                    ntable[i][j] = j - 1;
                }
            }
        }
        return ltable[list.size() - 2][list.size() - 1] + getData().getLength(
                list.get(list.size() - 1).getIdVertex(), list.get(list.size() - 2).getIdVertex()
        );
    }
}
