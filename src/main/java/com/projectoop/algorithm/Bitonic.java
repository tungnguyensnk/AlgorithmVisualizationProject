package com.projectoop.algorithm;

import com.projectoop.model.Edge;
import com.projectoop.model.Vertex;
import com.projectoop.step.DetailStep;
import com.projectoop.step.EdgeDetailStep;
import com.projectoop.step.PseudoStep;
import com.projectoop.step.VertexDetailStep;

import java.util.ArrayList;
import java.util.Collections;

public class Bitonic extends Algorithm {
    private ArrayList<Vertex> list;
    private int[][] ltable;
    private final ArrayList<Edge> stackEdges = new ArrayList<>();

    public Bitonic() {
        pseudoTexts.add("sort by (x, y) coordinates\n");
        pseudoTexts.add("function f(p1, p2)\n");
        pseudoTexts.add("if visited all nodes: return d[p1][v]+d[v][p2]\n");
        pseudoTexts.add("if (p1, p2) in memo: return memo[p1][p2]\n");
        pseudoTexts.add("ltr = d[p1][v] + f(v, p2)\n");
        pseudoTexts.add("rtl = d[v][p2] + f(p1, v)\n");
        pseudoTexts.add("return memo[p1][p2] = min(ltr, rtl)\n");
    }

    @Override
    public void explore() {
        list = (ArrayList<Vertex>) getData().getVertexes().clone();
        Collections.sort(list);
        ltable = new int[list.size()][list.size()];
        PseudoStep end = new PseudoStep(-1);
        end.addStep(new DetailStep(
                "The optimum tour cost for this graph is " + bitonic() + ".")
        );
        pseudoSteps.add(end);
    }

    public int bitonic() {
        PseudoStep step1 = new PseudoStep(0);
        pseudoSteps.add(step1);
        step1.addStep(new VertexDetailStep(
                "Start from smallest (x, y) vertex = " + list.get(0).getIdVertex() + ".",
                true,
                list.get(0)
        ));

        int tempDist;
        tempDist = getData().getLength(list.get(0).getIdVertex(), list.get(1).getIdVertex());

        PseudoStep step2 = new PseudoStep(4);
        pseudoSteps.add(step2);
        step2.addStep(new VertexDetailStep(
                "Try Left = " + list.get(0).getIdVertex() + " to Right = " + list.get(1).getIdVertex() + ".",
                true,
                list.get(1)
        ));
        stackEdges.add(getData().getEdgeById(
                list.get(0).getIdVertex(), list.get(1).getIdVertex()
        ));
        step2.addStep(new EdgeDetailStep("", true, stackEdges.get(stackEdges.size() - 1)));

        ltable[0][1] = tempDist;
        int temp;
        int min;
        for (int j = 2; j < list.size(); j++) {
            for (int i = 0; i < j; i++) {
                min = Integer.MAX_VALUE;
                if (j <= (i + 1)) {
                    for (int k = 0; k < i; k++) {
                        tempDist = getData().getLength(list.get(k).getIdVertex(), list.get(j).getIdVertex());
                        PseudoStep step3 = new PseudoStep(4);
                        pseudoSteps.add(step3);
                        step3.addStep(new VertexDetailStep(
                                "Try Left = " + list.get(k).getIdVertex() + " to Right = " + list.get(j).getIdVertex() + ".",
                                true,
                                list.get(j)
                        ));
                        stackEdges.add(
                                getData().getEdgeById(list.get(k).getIdVertex(),
                                        list.get(j).getIdVertex())
                        );
                        step3.addStep(new EdgeDetailStep(
                                "",
                                true,
                                stackEdges.get(stackEdges.size() - 1))
                        );

                        temp = ltable[k][i] + tempDist;
                        if (min > temp) {
                            min = temp;
                            PseudoStep step4 = new PseudoStep(3);
                            pseudoSteps.add(step4);
                            step4.addStep(new VertexDetailStep(
                                    "Returning...", false, list.get(j)
                            ));
                            step4.addStep(new EdgeDetailStep(
                                    "",
                                    false,
                                    stackEdges.get(stackEdges.size() - 1))
                            );
                            stackEdges.remove(stackEdges.size() - 1);
                        }
                    }

                    ltable[i][j] = min;
                } else {
                    tempDist = getData().getLength(list.get(j - 1).getIdVertex(), list.get(j).getIdVertex());
                    PseudoStep step3 = new PseudoStep(5);
                    pseudoSteps.add(step3);
                    step3.addStep(new VertexDetailStep(
                            "Try Right = " + list.get(j - 1).getIdVertex() + " to Left = " + list.get(j).getIdVertex() + ".",
                            true,
                            list.get(j - 1)
                    ));
                    stackEdges.add(
                            getData().getEdgeById(list.get(j - 1).getIdVertex(),
                                    list.get(j).getIdVertex())
                    );
                    step3.addStep(new EdgeDetailStep(
                            "",
                            true,
                            stackEdges.get(stackEdges.size() - 1))
                    );

                    ltable[i][j] = ltable[i][j - 1] + tempDist;
                }
            }
        }
        PseudoStep step5 = new PseudoStep(5);
        pseudoSteps.add(step5);
        step5.addStep(new VertexDetailStep(
                "Try Right = " + list.get(list.size() - 1).getIdVertex() + " to Left = " + list.get(0).getIdVertex() + ".",
                true,
                list.get(list.size() - 1)
        ));
        stackEdges.add(
                getData().getEdgeById(list.get(list.size() - 1).getIdVertex(),
                        list.get(0).getIdVertex())
        );
        step5.addStep(new EdgeDetailStep(
                "",
                true,
                stackEdges.get(stackEdges.size() - 1))
        );

        while (!stackEdges.isEmpty()) {
            PseudoStep step4 = new PseudoStep(2);
            pseudoSteps.add(step4);
            step4.addStep(new VertexDetailStep(
                    "Returning...", false, stackEdges.get(stackEdges.size() - 1).getTo()
            ));
            step4.addStep(new VertexDetailStep(
                    "Returning...", false, stackEdges.get(stackEdges.size() - 1).getFrom()
            ));
            step4.addStep(new EdgeDetailStep(
                    "",
                    false,
                    stackEdges.get(stackEdges.size() - 1))
            );
            stackEdges.remove(stackEdges.size() - 1);
        }

        PseudoStep step6 = new PseudoStep(6);
        pseudoSteps.add(step6);
        step6.addStep(new VertexDetailStep(
                "Returning...", false, list.get(0)
        ));
        return ltable[list.size() - 2][list.size() - 1] + getData().getLength(
                list.get(list.size() - 1).getIdVertex(), list.get(list.size() - 2).getIdVertex()
        );
    }
}
