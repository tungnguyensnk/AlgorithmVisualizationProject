package com.projectoop.controller;

import com.projectoop.Primary;
import com.projectoop.algorithm.BruteForce;
import com.projectoop.model.Edge;
import com.projectoop.model.Graph;
import com.projectoop.model.Vertex;
import com.projectoop.step.DetailStep;
import com.projectoop.step.PseudoStep;
import com.projectoop.step.Step;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public Label textOfShowPanel;
    public Button showPanel;
    public Button codeTraceButton;
    public Button statusButton;
    public VBox panel;
    public Label textOfShowStatus;
    public Label textOfShowCodeTrace;
    public AnchorPane main;
    public Button showWeightButton;
    public TextFlow status;
    public TextFlow codeTrace;
    Robot robot = new Robot();

    public void showWeight() {
        showWeightButton.setText(Graph.isShowWeight() ? "Show Weight" : "Hide Weight");
        graph.setShowWeight(!Graph.isShowWeight());
    }

    public void exit() {
        System.exit(0);
    }

    public void run() {
        BruteForce bf = new BruteForce();
        bf.setData(graph);
        bf.explore();
        Task<Void> task = new Task<>() {
            @Override
            public Void call() throws Exception {
                Platform.runLater(() -> codeTrace.getChildren().clear());
                if (!initCodeTrace.isShow)
                    showCodeTrace();
                if (!initStatus.isShow)
                    showStatus();
                for (int i = 0; i < bf.getPseudoTexts().size(); i++) {
                    Text text = new Text(bf.getPseudoTexts().get(i));
                    text.setStyle("-fx-font-size: 16px");
                    Platform.runLater(() -> codeTrace.getChildren().add(text));
                }
                for (PseudoStep step : bf.getPseudoSteps()) {
                    Platform.runLater(() -> {
                        codeTrace.getChildren().forEach(node -> node.setStyle("-fx-font-weight: normal"));
                        int idPseudo = Integer.parseInt(step.getText());
                        if (idPseudo != -1)
                            codeTrace.getChildren().get(idPseudo).setStyle("-fx-font-weight: bold");
                    });
                    Thread.sleep(2000);
                }
                return null;
            }
        };

        Task<Void> task1 = new Task<>() {
            @Override
            public Void call() throws Exception {
                for (DetailStep step : bf.getDetailSteps()) {
                    Platform.runLater(() -> {
                        status.getChildren().clear();
                        status.getChildren().add(new Text(step.getText()));
                        Platform.runLater(step::run);
                    });
                    Thread.sleep(2000);
                }
                return null;
            }
        };
        new Thread(task).start();
        new Thread(task1).start();
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
                PauseTransition p = new PauseTransition(Duration.millis(100));
                p.setOnFinished(actionEvent -> container.setVisible(false));
                p.play();
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
        initStatus.play(-50);
    }

    public void showCodeTrace() {
        initCodeTrace.play(-50);
    }

    public void addOrLink(MouseEvent mouseEvent) {
        Node cur = mouseEvent.getPickResult().getIntersectedNode();
        System.out.println(cur);
        if (cur == main) {
            Vertex node = new Vertex();
            double x = robot.getMouseX() - main.localToScreen(main.getBoundsInLocal()).getMinX() - 22;
            double y = robot.getMouseY() - main.localToScreen(main.getBoundsInLocal()).getMinY() - 22;
            x = Math.min(x, main.getPrefWidth() - 44);
            y = Math.min(y, main.getPrefHeight() - 44);
            node.setLayoutX(x);
            node.setLayoutY(y);
            node.setIdVertex(graph.getVertexes().size());
            main.getChildren().add(node);
            refreshVertex(node);

            node.setOnMouseEntered(mouseEvent1 -> node.requestFocus());
            node.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.DELETE) {
                    refreshNode();
                    refreshVertex(node);
                }
            });
        }
    }

    private void refreshNode() {
        graph.getVertexes().forEach(stackPane -> ((Label) stackPane.getChildren().get(1)).setText(graph.getVertexes().indexOf(stackPane) + ""));
    }

    private void refreshVertex(Vertex vertex) {
        if (!graph.getVertexes().contains(vertex)) {
            graph.addVertex(vertex);
            graph.getEdges().forEach(edge -> {
                if (!main.getChildren().contains(edge))
                    main.getChildren().addAll(edge, edge.getLabel());
            });
        } else {
            graph.getEdges().removeIf(edge -> {
                if (edge.getFrom().equals(vertex) || edge.getTo().equals(vertex)) {
                    main.getChildren().removeAll(edge, edge.getLabel());
                    return true;
                } else
                    return false;
            });
            main.getChildren().remove(vertex);
            graph.getVertexes().remove(vertex);
            graph.getVertexes().forEach(stackPane ->
                    ((Label) stackPane.getChildren().get(1)).setText(graph.getVertexes().indexOf(stackPane) + ""));
        }
    }
}
