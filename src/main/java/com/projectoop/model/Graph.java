package com.projectoop.model;

import javafx.scene.control.Label;
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
        //Image im = new Image(String.valueOf(Graph.class.getResource("circle.png")));
        //new ImagePattern(im)
        ((Circle) vertex.getChildren().get(0)).setFill(
                isLight ? Color.rgb(113, 36, 164) : Color.DODGERBLUE
        );
        ((Label) vertex.getChildren().get(1)).setTextFill(
                isLight ? Color.WHITE : Color.BLACK
        );
    }

    public static void highlight(Edge edge, boolean isLight) {
        if (isLight) {
            edge.setStroke(Color.rgb(82, 182, 255));
            edge.setStrokeWidth(6);
        } else {
            edge.setStroke(Color.BLACK);
            edge.setStrokeWidth(3);
        }
    }

    public static Graph example() {
        Graph graph = new Graph();
        ArrayList<Vertex> vertexs = new ArrayList<>();
        int k = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Vertex node = new Vertex();
                node.setLayoutX(450 + i * 200);
                node.setLayoutY(150 + j * 200);
                node.setIdVertex(k);
                k++;
                vertexs.add(node);
                graph.addVertex(node);
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = i + 1; j < 4; j++) {
                if (i != j && graph.getEdgeById(i, j) == null) {
                    Edge edge = new Edge(vertexs.get(i), vertexs.get(j));
                    graph.addEdge(edge);
                }
            }
        }
        return graph;
    }
}
