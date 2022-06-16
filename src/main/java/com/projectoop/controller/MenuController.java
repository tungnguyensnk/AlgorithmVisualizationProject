package com.projectoop.controller;

import com.projectoop.model.Edge;
import com.projectoop.model.Graph;
import com.projectoop.model.Vertex;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public Label textOfShowPanel;
    public Button showPanel;
    public Button codeTraceButton;
    public Button statusButton;
    public VBox panel;
    public Label textOfShowStatus;
    public Label textOfShowCodeTrace;
    public TextArea status;
    public TextArea codeTrace;
    public AnchorPane main;
    public Button showWeightButton;
    private boolean isDrag = false;

    private Edge currentLine;
    Robot robot = new Robot();

    public void showWeight() {
        showWeightButton.setText(Graph.isShowWeight() ? "Show Weight" : "Hide Weight");
        graph.setShowWeight(!Graph.isShowWeight());
    }

    static class InitMenu {
        TranslateTransition translateTransition;
        FadeTransition fadeTransition;
        RotateTransition rotateTransition;

        Node container;
        boolean isShow = false;

        public InitMenu(Node container, Label label) {
            this.container = container;
            translateTransition = new TranslateTransition(Duration.millis(100), container);
            fadeTransition = new FadeTransition(Duration.millis(100), container);
            rotateTransition = new RotateTransition(Duration.millis(100), label);
        }

        public void play(int byX) {
            if (!isShow) {
                container.setVisible(true);
                translateTransition.setByX(byX);
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(1);
                rotateTransition.setByAngle(180);
                translateTransition.play();
                fadeTransition.play();
                rotateTransition.play();
                isShow = true;
            } else {
                translateTransition.setByX(-byX);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                rotateTransition.setByAngle(-180);
                translateTransition.play();
                fadeTransition.play();
                rotateTransition.play();
                container.setVisible(false);
                isShow = false;
            }
        }
    }

    InitMenu initPanel, initStatus, initCodeTrace;
    Graph graph = new Graph();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPanel = new InitMenu(panel, textOfShowPanel);
        initStatus = new InitMenu(status, textOfShowStatus);
        initCodeTrace = new InitMenu(codeTrace, textOfShowCodeTrace);

        main.setFocusTraversable(true);
    }

    public void showPanel() {
        initPanel.play(20);
    }

    public void showStatus() {
        initStatus.play(100);
    }

    public void showCodeTrace() {
        initCodeTrace.play(100);
    }

    public void addOrLink(MouseEvent mouseEvent) {
        Node cur = mouseEvent.getPickResult().getIntersectedNode();
        System.out.println(cur);
        if (cur == main && !isDrag) {
            Vertex node = new Vertex();
            node.setLayoutX(robot.getMouseX() - main.localToScreen(main.getBoundsInLocal()).getMinX() - 22);
            node.setLayoutY(robot.getMouseY() - main.localToScreen(main.getBoundsInLocal()).getMinY() - 22);
            node.setIdVertex(graph.getVertexes().size());
            main.getChildren().add(node);
            graph.addVertex(node);

            node.setOnMouseEntered(mouseEvent1 -> node.requestFocus());
            node.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.DELETE) {
                    graph.removeEdgesOfVertex(node, main);
                    refreshNode();
                }
            });
        } else {
            int currentNode;
            if (!isDrag && (cur instanceof Circle || cur instanceof Text)) {
                isDrag = true;
                currentNode = checkIdNode(mouseEvent);
                currentLine = new Edge();
                currentLine.setVisible(true);
                currentLine.setStartX(graph.getVertexes().get(currentNode).getLayoutX() + 22);
                currentLine.setStartY(graph.getVertexes().get(currentNode).getLayoutY() + 22);
                currentLine.setEndX(currentLine.getStartX());
                currentLine.setEndY(currentLine.getStartY());
                currentLine.setIdFrom(currentNode);
                main.getChildren().add(currentLine);

            } else if (isDrag && (cur instanceof Circle || cur instanceof Text)) {
                isDrag = false;
                currentNode = checkIdNode(mouseEvent);
                currentLine.setEndX(graph.getVertexes().get(currentNode).getLayoutX() + 22);
                currentLine.setEndY(graph.getVertexes().get(currentNode).getLayoutY() + 22);
                graph.addEdge(currentLine);
                currentLine.setIdTo(currentNode);
                int fromX = (int) graph.getVertexes().get(currentLine.getIdFrom()).getLayoutX();
                int fromY = (int) graph.getVertexes().get(currentLine.getIdFrom()).getLayoutY();
                int toX = (int) graph.getVertexes().get(currentLine.getIdTo()).getLayoutX();
                int toY = (int) graph.getVertexes().get(currentLine.getIdTo()).getLayoutY();
                int length = (int) Math.sqrt(Math.pow(fromX - toX, 2) + Math.pow(fromY - toY, 2));
                currentLine.setLength(length, main);

                System.out.println("from " + currentLine.getIdFrom() + " to " + currentLine.getIdTo() + " length " + length);
            }
        }
    }

    private int checkIdNode(MouseEvent mouseEvent) {
        Parent parent = mouseEvent.getPickResult().getIntersectedNode().getParent();
        return Integer.parseInt(parent instanceof Label ? ((Label) parent).getText() :
                ((Label) ((StackPane) parent).getChildren().get(1)).getText());
    }

    private void refreshNode() {
        graph.getVertexes().forEach(stackPane -> ((Label) stackPane.getChildren().get(1)).setText(graph.getVertexes().indexOf(stackPane) + ""));
    }

    public void drawLine() {
        if (isDrag) {
            currentLine.setEndX(robot.getMouseX() - main.getLayoutX() - main.getParent().getScene().getWindow().getX());
            currentLine.setEndY(robot.getMouseY() - main.getLayoutY() - main.getParent().getScene().getWindow().getY());
        }
    }
}
