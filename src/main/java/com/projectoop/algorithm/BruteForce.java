package com.projectoop.algorithm;

import com.projectoop.step.DetailStep;
import com.projectoop.step.PseudoStep;
import com.projectoop.step.VertexDetailStep;

import java.util.ArrayList;

public class BruteForce extends Algorithm {

    ArrayList<String> pseudoTexts = new ArrayList<>();

    public ArrayList<String> getPseudoTexts() {
        return pseudoTexts;
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
        detailSteps.add(new DetailStep("The optimum tour cost for this graph is " + findTour(0, 1) + "."));
    }

    private int findTour(int pos, int mask) {
        //vào hàm
        pseudoSteps.add(new PseudoStep(0));
        detailSteps.add(new VertexDetailStep(
                "Position = " + pos + ", bitmask = " + mask + ".", true, getData().getVertexes().get(pos)
        ));

        //đã thăm hết node
        if (Integer.toBinaryString(mask).equals("1".repeat(getData().getVertexes().size()))) {
            pseudoSteps.add(new PseudoStep(1));
            detailSteps.add(new DetailStep("Every node has been visited. Returning the cost between the last and the " +
                    "original vertex. The current minimal tour cost is 76."));
            pseudoSteps.add(new PseudoStep(-1));
            detailSteps.add(new VertexDetailStep("Backtracking", false, getData().getVertexes().get(pos)));
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
                pseudoSteps.add(new PseudoStep(3));
                detailSteps.add(new VertexDetailStep(
                        "Going from " + pos + " to " + i + ".", true, getData().getVertexes().get(i)
                ));
                ans = Integer.min(ans, findTour(i, mask | (1 << i)) + getData().getLength(pos, i));
            }
        }

        pseudoSteps.add(new PseudoStep(4));
        detailSteps.add(new DetailStep("With the current state (2, 111), The cost to complete the tour out of the" +
                " remaining graph is 26, The current minimal tour cost is 76."));
        pseudoSteps.add(new PseudoStep(-1));
        detailSteps.add(new VertexDetailStep("Backtracking", false, getData().getVertexes().get(pos)));
        return ans;
    }
}
