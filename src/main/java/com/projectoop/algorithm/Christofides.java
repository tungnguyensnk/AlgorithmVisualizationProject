package com.projectoop.algorithm;

import java.util.*;

public class Christofides extends Algorithm {

    private final ArrayList<ArrayList<Double>> adjMatrix;
    private final ArrayList<ArrayList<Double>> e;
    private ArrayList<ArrayList<Integer>> mstAdjList;
    private ArrayList<Integer> eulersPath;
    private double pathLength;

    public Christofides() {
        this.adjMatrix = new ArrayList<>();
        this.e = new ArrayList<>();
        this.pathLength = -1d;

        System.out.println("Broj gradova: " + getData().getVertexes().size());
        for (int i = 0; i < getData().getVertexes().size(); i++) {
            this.adjMatrix.add(new ArrayList<>(Collections.nCopies(getData().getVertexes().size(), 0d)));
        }
        for (int i = 0; i < getData().getVertexes().size(); i++) {
            for (int j = i; j < getData().getVertexes().size(); j++) {
                if (i == j) {
                    continue;
                }
                double d = getData().getLength(i, j);
                adjMatrix.get(i).set(j, d);
                adjMatrix.get(j).set(i, d);
                e.add(new ArrayList<>(Arrays.asList((double) i, (double) j, d)));
            }
        }
    }

    public void eulersPath() {
        Stack<Integer> currentPath = new Stack<>();
        Stack<Integer> eulersPath = new Stack<>();
        ArrayList<ArrayList<Integer>> mstCopy = mstAdjList;

        currentPath.push(0);
        while (!currentPath.isEmpty()) {

            int u = currentPath.peek();
            if (mstCopy.get(u).size() == 0) {
                eulersPath.push(u);
                currentPath.pop();

            } else {
                currentPath.push(mstCopy.get(u).get(0));
                mstCopy.get(u).remove(0);
            }
        }
        ArrayList<Integer> result = new ArrayList<>();

        while (!eulersPath.isEmpty()) {
            result.add(eulersPath.peek());
            eulersPath.pop();
        }
        this.eulersPath = result;
    }

    public void eulerToHamilton() {

        Set<Integer> unique = new LinkedHashSet<>(this.eulersPath);
        this.eulersPath.clear();
        this.eulersPath.addAll(unique);

        double length = 0;
        for (int i = 0; i < eulersPath.size() - 1; i++) {
            length += adjMatrix.get(eulersPath.get(i)).get(eulersPath.get(i + 1));
        }
        length += adjMatrix.get(0).get(eulersPath.get(eulersPath.size() - 1));
        this.pathLength = length;
    }

    public void greedyPerfectMatching() {

        ArrayList<Integer> mstOddVertices = new ArrayList<>();
        for (int i = 0; i < this.mstAdjList.size(); i++) {
            if (mstAdjList.get(i).size() % 2 == 1) {
                mstOddVertices.add(i);
            }
        }
        while (!mstOddVertices.isEmpty()) {

            double minDistance = Double.MAX_VALUE;
            int firstNode = mstOddVertices.get(0);
            int minDistanceNodeIndex = -1;

            for (int i = 0; i < mstOddVertices.size(); i++) {
                if (firstNode != mstOddVertices.get(i) &&
                        adjMatrix.get(firstNode).get(mstOddVertices.get(i)) < minDistance) {
                    minDistance = adjMatrix.get(firstNode).get(mstOddVertices.get(i));
                    minDistanceNodeIndex = i;
                }
            }
            this.mstAdjList.get(firstNode).add(mstOddVertices.get(minDistanceNodeIndex));
            this.mstAdjList.get(mstOddVertices.get(minDistanceNodeIndex)).add(firstNode);
            mstOddVertices.remove(minDistanceNodeIndex);
            mstOddVertices.remove(0);
        }
    }


    public void kruskalMst() {

        this.e.sort(Comparator.comparing(o -> o.get(2)));

        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        List<Integer> referenceNodes = new ArrayList<>();

        for (int i = 0; i < this.adjMatrix.size(); i++) {
            referenceNodes.add(i);
        }

        List<Integer> weights = new ArrayList<>(Collections.nCopies(this.adjMatrix.size(), 0));

        for (ArrayList<Double> doubles : this.e) {
            int firstNode = (int) Math.round(doubles.get(0));
            int secondNode = (int) Math.round(doubles.get(1));
            int referenceNodeOne = referenceNodes.get(firstNode);
            int temp = firstNode;

            while (temp != referenceNodeOne) {
                temp = referenceNodeOne;
                referenceNodeOne = referenceNodes.get(temp);
            }

            int referenceNodeTwo = referenceNodes.get(secondNode);
            temp = secondNode;
            while (temp != referenceNodeTwo) {
                temp = referenceNodeTwo;
                referenceNodeTwo = referenceNodes.get(temp);
            }

            if (referenceNodeOne != referenceNodeTwo) {

                int low;
                int high;
                if (weights.get(referenceNodeOne) < weights.get(referenceNodeTwo)) {
                    low = referenceNodeTwo;
                    high = referenceNodeOne;
                } else {
                    low = referenceNodeOne;
                    high = referenceNodeTwo;
                }
                referenceNodes.set(low, high);
                weights.set(high, weights.get(high) + weights.get(low));
                res.add(new ArrayList<>(Arrays.asList(firstNode, secondNode)));
            }
        }
        this.mstAdjList = new ArrayList<>(this.adjMatrix.size());
        for (int i = 0; i < this.adjMatrix.size(); i++) {
            mstAdjList.add(new ArrayList<>());
        }
        for (ArrayList<Integer> re : res) {
            mstAdjList.get(re.get(0)).add(re.get(1));
            mstAdjList.get(re.get(1)).add(re.get(0));
        }
    }

    @Override
    public void explore() {

    }
}
