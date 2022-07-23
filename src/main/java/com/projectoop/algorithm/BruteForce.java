package com.projectoop.algorithm;

import com.projectoop.model.Edge;
import com.projectoop.model.Vertex;
import com.projectoop.step.DetailStep;
import com.projectoop.step.EdgeDetailStep;
import com.projectoop.step.PseudoStep;
import com.projectoop.step.VertexDetailStep;

import java.util.ArrayList;

public class BruteForce extends Algorithm {

    private final ArrayList<Edge> stackEdges = new ArrayList<>();
    private ArrayList<Edge> findTour;
    private int currentCost = 0, cost = 0, min = Integer.MAX_VALUE;

    public ArrayList<Edge> getFindTour() {
        return findTour;
    }

    public BruteForce() {
        pseudoTexts.add("function findTour(pos, mask)\n");
        pseudoTexts.add("if every node has been visited: return cost[pos][0]\n");
        pseudoTexts.add("ans = ∞\n");
        pseudoTexts.add("""
                for every unvisited node V
                ans = min(ans,findTour(v,mask|(1<<V))+cost[pos][v])
                """);
        pseudoTexts.add("return ans\n");
    }

    @Override
    public void explore() {
        PseudoStep end = new PseudoStep(-1);
        end.addStep(new DetailStep(
                "The optimum tour cost for this graph is " + findTour(0, 1) + ".")
        );
        pseudoSteps.add(end);
    }

    private int findTour(int pos, int mask) {
        //vào hàm
        PseudoStep step1 = new PseudoStep(0);
        pseudoSteps.add(step1);
        step1.addStep(new VertexDetailStep(
                "Position = " + pos + ", bitmask = " + Integer.toBinaryString(mask) + ".",
                true,
                getData().getVertexes().get(pos)
        ));

        //đã thăm hết node
        if (Integer.toBinaryString(mask).equals("1".repeat(getData().getVertexes().size()))) {
            PseudoStep step2 = new PseudoStep(1);
            pseudoSteps.add(step2);
            currentCost += getData().getLength(pos, 0);
            cost = currentCost;
            step2.addStep(new DetailStep("Every node has been visited. Returning the cost between the last and the " +
                    "original vertex. The current minimal tour cost is " + currentCost + "."));
            stackEdges.add(getData().getEdgeById(pos, 0));
            step2.addStep(new EdgeDetailStep("", true, stackEdges.get(stackEdges.size() - 1)));

            if (min > currentCost) {
                min = currentCost;
                findTour = (ArrayList<Edge>) stackEdges.clone();
            }
            PseudoStep step3 = new PseudoStep(-1);
            pseudoSteps.add(step3);
            step3.addStep(new VertexDetailStep("Backtracking"));
            step3.addStep(
                    new EdgeDetailStep("", false, stackEdges.get(stackEdges.size() - 1))
            );
            stackEdges.remove(stackEdges.size() - 1);

            PseudoStep step3x = new PseudoStep(-1);
            pseudoSteps.add(step3x);
            step3x.addStep(
                    new VertexDetailStep("Backtracking", false, getData().getVertexes().get(pos))
            );
            step3x.addStep(
                    new EdgeDetailStep("", false, stackEdges.get(stackEdges.size() - 1))
            );
            stackEdges.remove(stackEdges.size() - 1);

            currentCost -= getData().getLength(pos, 0);
            return getData().getLength(pos, 0);
        }

        int ans = Integer.MAX_VALUE;

        StringBuilder mask_t = new StringBuilder(Integer.toBinaryString(mask));
        while (mask_t.length() < getData().getVertexes().size())
            mask_t.insert(0, "0");

        mask_t.reverse();

        //duyệt các node chưa thăm rồi đệ quy
        for (int i = 1; i < mask_t.length(); i++) {
            if (mask_t.charAt(i) == '0') {
                PseudoStep step4 = new PseudoStep(3);
                pseudoSteps.add(step4);
                step4.addStep(new VertexDetailStep(
                        "Going from " + pos + " to " + i + ".",
                        true,
                        getData().getVertexes().get(i)
                ));
                stackEdges.add(getData().getEdgeById(pos, i));
                step4.addStep(
                        new EdgeDetailStep("", true, stackEdges.get(stackEdges.size() - 1))
                );
                currentCost += getData().getLength(pos, i);
                ans = Integer.min(ans,
                        findTour(i, mask | (1 << i)) + getData().getLength(pos, i)
                );
                currentCost -= getData().getLength(pos, i);
            }
        }

        PseudoStep step5 = new PseudoStep(4);
        pseudoSteps.add(step5);
        step5.addStep(new DetailStep("With he current state (" + pos + ", " + Integer.toBinaryString(mask) + ")," +
                " The cost to complete the tour out of the remaining graph is " + (cost - currentCost) +
                ", The current minimal tour cost is " + cost + "."));
        PseudoStep step6 = new PseudoStep(-1);
        pseudoSteps.add(step6);
        step6.addStep(
                new VertexDetailStep(
                        "Backtracking",
                        false,
                        getData().getVertexes().get(pos)
                )
        );
        if (stackEdges.size() > 0) {
            step6.addStep(new EdgeDetailStep(
                    "",
                    false,
                    stackEdges.get(stackEdges.size() - 1)
            ));
            stackEdges.remove(stackEdges.size() - 1);
        }
        return ans;
    }
}
