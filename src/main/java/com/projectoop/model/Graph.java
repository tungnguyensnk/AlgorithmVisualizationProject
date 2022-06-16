package com.projectoop.model;

import javafx.scene.layout.AnchorPane;
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
        vertexes.add(vertex);
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public void removeEdgesOfVertex(Vertex node, AnchorPane main) {
        getVertexes().remove(node);
        main.getChildren().remove(node);
        for (int i = 0; i < getEdges().size(); i++) {
            Edge edge = getEdges().get(i);
            if (edge.getIdFrom() == node.getIdVertex() || edge.getIdTo() == node.getIdVertex()) {
                main.getChildren().removeAll(edge, edge.getLabel());
                getEdges().remove(edge);
                i--;
            }
        }
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
