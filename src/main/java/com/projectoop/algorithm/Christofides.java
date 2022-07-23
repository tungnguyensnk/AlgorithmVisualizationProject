package com.projectoop.algorithm;

import com.projectoop.step.DetailStep;
import com.projectoop.step.EdgeDetailStep;
import com.projectoop.step.PseudoStep;
import com.projectoop.step.VertexDetailStep;

import java.util.*;

public class Christofides extends Algorithm {

    public Christofides() {
        pseudoTexts.add("Get the MST of the (complete) graph\n");
        pseudoTexts.add("Find odd degree vertices in the MST\n");
        pseudoTexts.add("Find a min cost matching among those vertices\n");
        pseudoTexts.add("Obtain a eulerian tour using edges used in MST + match\n");
        pseudoTexts.add("""
                Obtain a TSP tour from the obtained eulerian tour and
                skip repeated vertices
                """);
    }

    @Override
    public void explore() {
        ArrayList<Integer> finalAns = new ArrayList<>();
        ArrayList<Integer> dfsTrack = new ArrayList<>();

        boolean[] visitedNodes = new boolean[getData().getVertexes().size()];

        for (int i = 0; i < getData().getVertexes().size(); i++) {
            visitedNodes[i] = false;
        }
        int[] parent = primMST();
        PseudoStep step1 = new PseudoStep(0);
        pseudoSteps.add(step1);
        step1.addStep(
                new DetailStep("Creat MST of the graph" + Arrays.toString(primMST()) + ".")
        );
        dfs(parent, 0, visitedNodes, finalAns, dfsTrack);
        Object[] dfsTrackArray = dfsTrack.toArray();

        for (int i = 1; i < dfsTrackArray.length; i++) {
            int source = (int) dfsTrackArray[i - 1];
            int destination = (int) dfsTrackArray[i];
            PseudoStep step4 = new PseudoStep(1);
            pseudoSteps.add(step4);
            step4.addStep(new VertexDetailStep(
                    "The dfsTrack tour of the MST is " + dfsTrack + ".",
                    true,
                    getData().getVertexes().get(source)
            ));
            step4.addStep(
                    new EdgeDetailStep(
                            "",
                            true,
                            getData().getEdgeById(source, destination)
                    )
            );
        }
        PseudoStep step4 = new PseudoStep(1);
        pseudoSteps.add(step4);
        step4.addStep(new VertexDetailStep(
                "The dfsTrack tour of the MST is " + dfsTrack + ".",
                true,
                getData().getVertexes().get((int) dfsTrackArray[dfsTrackArray.length - 1])
        ));

        finalAns.add(0);
        int[] exsited = new int[getData().getVertexes().size()];


        for (int i = 0; i < dfsTrackArray.length; i++) {
            if (exsited[dfsTrack.get(i)] == 0) {
                exsited[dfsTrack.get(i)] = 1;
            } else {
                PseudoStep step5 = new PseudoStep(2);
                pseudoSteps.add(step5);
                step5.addStep(new EdgeDetailStep(
                        "Found approximation TSP tour " + finalAns,
                        true,
                        getData().getEdgeById((int) dfsTrackArray[i - 1], (int) dfsTrackArray[i])
                ));
                step5.addStep(new EdgeDetailStep(
                        "Found approximation TSP tour " + finalAns,
                        true,
                        getData().getEdgeById((int) dfsTrackArray[i], (int) dfsTrackArray[i + 1])
                ));
                step5.addStep(new EdgeDetailStep(
                        "Found approximation TSP tour " + finalAns,
                        true,
                        getData().getEdgeById((int) dfsTrackArray[i - 1], (int) dfsTrackArray[i + 1])
                ));
            }
        }

        PseudoStep end = new PseudoStep(-1);
        end.addStep(new EdgeDetailStep(
                "Found approximation TSP tour " + finalAns + " with cost = " + calculate(finalAns),
                true,
                getData().getEdgeById((int) dfsTrackArray[dfsTrackArray.length - 1], 0)
        ));
        pseudoSteps.add(end);
    }

    private int minKey(int[] key, boolean[] mstSet) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < getData().getVertexes().size(); v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    private int[] primMST() {
        int V = getData().getVertexes().size();
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];

        for (int i = 0; i < V; i++) {
            key[i] = Integer.MAX_VALUE;
            mstSet[i] = false;
        }

        key[0] = 0;
        parent[0] = -1;

        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet);
            mstSet[u] = true;
            int weight;

            for (int v = 0; v < V; v++) {
                weight = getData().getLength(u, v) != -1 ? getData().getLength(u, v) : 0;
                if (weight != 0 && !mstSet[v] && weight < key[v]) {
                    parent[v] = u;
                    key[v] = weight;
                }
            }
        }

        return parent;
    }

    private void dfs(int[] parent, int startingVertex, boolean[] visitedNodes, ArrayList<Integer> finalAns, ArrayList<Integer> dfsTrack) // getting the preorder walk of the MST using DFS
    {
        finalAns.add(startingVertex);
        dfsTrack.add(startingVertex);
        visitedNodes[startingVertex] = true;

        int count = 0;

        for (int i = 0; i < getData().getVertexes().size(); i++) {
            if (i == startingVertex) {
                continue;
            }
            if (parent[i] == startingVertex) {
                count++;
                if (count > 1) {
                    dfsTrack.add(startingVertex);
                }
                if (visitedNodes[i]) {
                    continue;
                }
                dfs(parent, i, visitedNodes, finalAns, dfsTrack);
            }
        }
    }

    private int calculate(ArrayList<Integer> finalAns) {
        int total = 0;
        Object[] finalAnsArray = finalAns.toArray();
        for (int i = 1; i < finalAnsArray.length; i++) {
            total = total + getData().getLength((int) finalAnsArray[i - 1], (int) finalAnsArray[i]);
        }

        return total;
    }
}
