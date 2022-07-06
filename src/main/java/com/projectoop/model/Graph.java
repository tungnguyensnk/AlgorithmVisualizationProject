package com.projectoop.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Graph {
    private final ArrayList<Vertex> vertexes = new ArrayList<>();
    private final ArrayList<Edge> edges = new ArrayList<>();

    private static boolean showWeight = false;

    public static boolean isShowWeight() {
        return showWeight;
    }

    public ArrayList<Vertex> getVertexes() {
        return vertexes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public Graph() {
    }

    public void addVertex(Vertex vertex) {
        getVertexes().forEach(vertex1 -> addEdge(new Edge(vertex, vertex1)));
        vertexes.add(vertex);
    }

    public int getLength(int start, int end) {
        for (Edge edge : edges) {
            if ((edge.getFrom().getIdVertex() == start && edge.getTo().getIdVertex() == end) ||
                    (edge.getTo().getIdVertex() == start && edge.getFrom().getIdVertex() == end))
                return edge.length;
        }
        return -1;
    }

    public Edge getEdgeById(int start, int end) {
        for (Edge edge : edges) {
            if ((edge.getFrom().getIdVertex() == start && edge.getTo().getIdVertex() == end) ||
                    (edge.getTo().getIdVertex() == start && edge.getFrom().getIdVertex() == end))
                return edge;
        }
        return null;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void setShowWeight(boolean bool) {
        showWeight = bool;
        getEdges().forEach(edge -> edge.label.setVisible(bool));
    }

    public static void highlight(Vertex vertex, boolean isLight) {
        ((Circle) vertex.getChildren().get(0)).setFill(isLight ? Color.LIGHTBLUE : Color.DODGERBLUE);
    }

    public static void highlight(Edge edge, boolean isLight) {
        if (isLight) {
            edge.setFill(Color.RED);
            edge.setStrokeWidth(3);
        } else {
            edge.setFill(Color.BLUEVIOLET);
            edge.setStrokeWidth(1);
        }
    }

    public static Graph example() {
        Graph graph = new Graph();
        return graph;
    }
}
