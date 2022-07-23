package com.projectoop.controller;

import com.projectoop.algorithm.*;
import com.projectoop.model.Edge;
import com.projectoop.model.Graph;
import com.projectoop.model.Vertex;
import com.projectoop.step.DetailStep;
import com.projectoop.step.PseudoStep;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.robot.Robot;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
    public Slider stackStep;
    public Label speed;
    public Label labelStackStep;
    public VBox algorithmSelect;
    public ImageView playButton;
    private int speedValue = 1;
    private final Robot robot = new Robot();
    private Thread thread;
    private Algorithm algorithm = null;
    private boolean changeValueByApp = false, isPlaying = false;

    public void changeSpeed() {
        speedValue = (speedValue + 1) % 6;
        speedValue = speedValue == 0 ? 1 : speedValue;
        speed.setText("Delay " + (500 * speedValue) + "ms");
    }

    public void runStackStep() {
        changeTypePlay(false);
        if (thread != null)
            thread.interrupt();
        Platform.runLater(() -> {
            algorithmSelect.setVisible(false);
            graph.getVertexes().forEach(stackPane -> Graph.highlight(stackPane, false));
            graph.getEdges().forEach(edge -> Graph.highlight(edge, false));
            status.getChildren().clear();
        });
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                if (codeTrace.getChildren().isEmpty() ||
                        codeTrace.getChildren().size() != algorithm.getPseudoTexts().size()) {
                    for (int i = 0; i < algorithm.getPseudoTexts().size(); i++) {
                        Text text = new Text(algorithm.getPseudoTexts().get(i));
                        text.setStyle("-fx-font-size: 16px;-fx-font-weight: normal");
                        Platform.runLater(() -> codeTrace.getChildren().add(text));
                    }
                }
                Platform.runLater(() -> {
                    codeTrace.getChildren().forEach(node -> node.setStyle("-fx-font-weight: normal"));
                    int idPseudo = Integer.parseInt(
                            algorithm.getPseudoSteps().get((int) stackStep.getValue()).getText()
                    );
                    if (idPseudo != -1)
                        codeTrace.getChildren().get(idPseudo).setStyle("-fx-font-weight: bold");
                });
                for (int i = 0; i <= stackStep.getValue(); i++) {
                    for (DetailStep detail : algorithm.getPseudoSteps().get(i).getDetail()) {
                        Platform.runLater(() -> {
                            if (detail.getText().length() > 0) {
                                status.getChildren().clear();
                                status.getChildren().add(new Text(detail.getText()));
                            }
                            Platform.runLater(detail::run);
                        });
                    }
                }
                return null;
            }
        };

        thread = new Thread(task);
        thread.start();
    }

    public void exportGraph() {
        if (graph.getVertexes().size() == 0)
            return;
        StringBuilder sb = new StringBuilder();
        graph.getVertexes().forEach(vertex -> sb.append(vertex).append("\n"));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        if (new File(System.getProperty("user.home") + "/Documents").exists())
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home") + "/Documents")
            );

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Graph Data", "*.grd")
        );
        File selectedFile = fileChooser.showSaveDialog(new Stage());
        if (selectedFile != null) {
            try (BufferedWriter bw = Files.newBufferedWriter(
                    selectedFile.toPath(),
                    StandardCharsets.UTF_8
            )) {
                bw.write(sb.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void importGraph() {
        Graph tempGraph = new Graph();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn File Data");
        if (new File(System.getProperty("user.home") + "/Documents").exists())
            fileChooser.setInitialDirectory(
                    new File(System.getProperty("user.home") + "/Documents")
            );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Graph Data", "*.grd"),
                new FileChooser.ExtensionFilter("Tất cả", "*.*")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile == null)
            return;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(selectedFile.getPath()));
            String line = reader.readLine();
            while (line != null) {
                Vertex vertex = new Vertex();
                vertex.setLayoutX(Double.parseDouble(line.split(" ")[0]));
                vertex.setLayoutY(Double.parseDouble(line.split(" ")[1]));
                vertex.setIdVertex(tempGraph.getVertexes().size());
                tempGraph.addVertex(vertex);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < tempGraph.getVertexes().size() - 1; i++) {
            for (int j = i + 1; j < tempGraph.getVertexes().size(); j++) {
                if (i != j && graph.getEdgeById(i, j) == null) {
                    Edge edge = new Edge(
                            tempGraph.getVertexes().get(i),
                            tempGraph.getVertexes().get(j)
                    );
                    graph.addEdge(edge);
                }
            }
        }

        changeTypePlay(false);
        interrupt();
        main.getChildren().clear();
        graph = tempGraph;
        graph.getVertexes().forEach(node -> {
            node.setOnMouseEntered(mouseEvent1 -> node.requestFocus());
            node.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.DELETE) {
                    interrupt();
                    refreshVertex(node);
                }
            });
        });
        render(graph);

        algorithm.setData(graph);
        new Context(algorithm).doExploration();
        changeValueByApp = true;
        stackStep.setMax(algorithm.getPseudoSteps().size() - 1);
        stackStep.setBlockIncrement(1);
        stackStep.setValue(0);
        changeValueByApp = false;
        stackStep.valueProperty().addListener(observable -> {
            if (!changeValueByApp)
                runStackStep();
        });
    }

    public void bruteForceSelect() {
        algorithmSelect(new BruteForce());
    }

    public void bitonicSelect() {
        algorithmSelect(new Bitonic());
    }

    public void christofidesSelect() {
        algorithmSelect(new Christofides());
    }

    public void algorithmSelect(Algorithm algo) {
        changeTypePlay(false);
        interrupt();
        algorithm = algo;
        algorithmSelect.setVisible(false);
        Platform.runLater(() -> {
            codeTrace.getChildren().clear();
            for (int i = 0; i < algorithm.getPseudoTexts().size(); i++) {
                Text text = new Text(algorithm.getPseudoTexts().get(i));
                text.setStyle("-fx-font-size: 16px;-fx-font-weight: normal");
                Platform.runLater(() -> codeTrace.getChildren().add(text));
            }
        });
    }

    public void showListAlgorithm() {
        Platform.runLater(() -> algorithmSelect.setVisible(!algorithmSelect.isVisible()));
    }

    public void play() {
        if (isPlaying)
            interrupt();
        changeTypePlay(!isPlaying);
    }

    public void changeTypePlay(boolean isPlaying) {
        Image image;
        if (isPlaying && this.isPlaying != isPlaying) {
            image = new Image(Objects.requireNonNull(
                    getClass().getResource("pauseButton.jpg")
            ).toString());
            runStepByStep();
        } else {
            image = new Image(Objects.requireNonNull(
                    getClass().getResource("playButton.jpg")
            ).toString());
        }
        this.isPlaying = isPlaying;
        playButton.setImage(image);
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
        algorithmSelect.setVisible(false);
    }

    public void showStatus() {
        initStatus.play(-50);
    }

    public void showCodeTrace() {
        initCodeTrace.play(-50);
    }

    public void addOrLink(MouseEvent mouseEvent) {
        Node cur = mouseEvent.getPickResult().getIntersectedNode();
        if (cur == main) {
            changeTypePlay(false);
            interrupt();
            Vertex node = new Vertex();
            double x = robot.getMouseX() -
                    main.localToScreen(main.getBoundsInLocal()).getMinX() - 22;
            double y = robot.getMouseY() -
                    main.localToScreen(main.getBoundsInLocal()).getMinY() - 22;
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
                    interrupt();
                    refreshVertex(node);
                }
            });
        }
    }

    private void refreshIdVertex() {
        graph.getVertexes().forEach(stackPane ->
                stackPane.setIdVertex(graph.getVertexes().indexOf(stackPane))
        );
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
            refreshIdVertex();
        }
    }

    public void showWeight() {
        algorithmSelect.setVisible(false);
        showWeightButton.setText(Graph.isShowWeight() ? "Show Weight" : "Hide Weight");
        graph.setShowWeight(!Graph.isShowWeight());
    }

    public void example() {
        changeTypePlay(false);
        interrupt();
        algorithmSelect.setVisible(false);
        main.getChildren().clear();
        graph = Graph.example();
        graph.getVertexes().forEach(node -> {
            node.setOnMouseEntered(mouseEvent1 -> node.requestFocus());
            node.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.DELETE) {
                    interrupt();
                    refreshVertex(node);
                }
            });
        });
        render(graph);

        if (algorithm != null) {
            algorithm.setData(graph);
            new Context(algorithm).doExploration();
            stackStep.setMax(algorithm.getPseudoSteps().size() - 1);
            stackStep.setBlockIncrement(1);
            changeValueByApp = true;
            stackStep.setValue(0);
            changeValueByApp = false;
            stackStep.valueProperty().addListener(observable -> {
                if (!changeValueByApp)
                    runStackStep();
            });
        }
    }

    private void render(Graph graph) {
        graph.getVertexes().forEach(stackPane -> main.getChildren().add(stackPane));
        graph.getEdges().forEach(edge -> main.getChildren().addAll(edge.getLabel(), edge));
    }

    public void exit() {
        System.exit(0);
    }

    private void interrupt() {
        if (thread != null)
            thread.interrupt();
        Platform.runLater(() -> {
            algorithmSelect.setVisible(false);
            graph.getVertexes().forEach(stackPane -> Graph.highlight(stackPane, false));
            graph.getEdges().forEach(edge -> Graph.highlight(edge, false));
            codeTrace.getChildren().clear();
            status.getChildren().clear();
        });
    }

    public void run() {
        interrupt();
        if (graph.getVertexes().isEmpty() || algorithm == null)
            return;

        algorithm.setData(graph);
        new Context(algorithm).doExploration();
        stackStep.setMax(algorithm.getPseudoSteps().size() - 1);
        stackStep.setBlockIncrement(1);
        stackStep.setValue(0);
        stackStep.valueProperty().addListener(observable -> {
            if (!changeValueByApp)
                runStackStep();
        });
        changeTypePlay(true);
    }

    public void runStepByStep() {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                Platform.runLater(() -> codeTrace.getChildren().clear());
                if (!initCodeTrace.isShow)
                    showCodeTrace();
                if (!initStatus.isShow)
                    showStatus();
                for (int i = 0; i < algorithm.getPseudoTexts().size(); i++) {
                    Text text = new Text(algorithm.getPseudoTexts().get(i));
                    text.setStyle("-fx-font-size: 16px;-fx-font-weight: normal");
                    Platform.runLater(() -> codeTrace.getChildren().add(text));
                }
                for (PseudoStep step : algorithm.getPseudoSteps()
                        .subList((int) stackStep.getValue(), algorithm.getPseudoSteps().size())) {
                    if (codeTrace.getChildren().isEmpty() ||
                            codeTrace.getChildren().size() > algorithm.getPseudoTexts().size()) {
                        Platform.runLater(() -> codeTrace.getChildren().clear());
                        for (int i = 0; i < algorithm.getPseudoTexts().size(); i++) {
                            Text text = new Text(algorithm.getPseudoTexts().get(i));
                            text.setStyle("-fx-font-size: 16px;-fx-font-weight: normal");
                            Platform.runLater(() -> codeTrace.getChildren().add(text));
                        }
                    }
                    Platform.runLater(() -> {
                        changeValueByApp = true;
                        stackStep.adjustValue(algorithm.getPseudoSteps().indexOf(step));
                        changeValueByApp = false;
                        codeTrace.getChildren().forEach(node -> node.setStyle("-fx-font-weight: normal"));
                        int idPseudo = Integer.parseInt(step.getText());
                        if (idPseudo != -1)
                            codeTrace.getChildren().get(idPseudo).setStyle("-fx-font-weight: bold");
                    });

                    for (DetailStep detail : step.getDetail()) {
                        Platform.runLater(() -> {
                            if (detail.getText().length() > 0) {
                                status.getChildren().clear();
                                status.getChildren().add(new Text(detail.getText()));
                            }
                            Platform.runLater(detail::run);
                        });
                    }
                    try {
                        Thread.sleep(speedValue * 500L);
                    } catch (InterruptedException e) {
                        return null;
                    }
                }
                graph.getVertexes().forEach(stackPane -> Graph.highlight(stackPane, true));
                graph.getEdges().forEach(edge -> Graph.highlight(edge, false));
                BruteForce bf = new BruteForce();
                bf.setData(graph);
                new Context(bf).doExploration();

                bf.getFindTour().forEach(edge -> Graph.highlight(edge, true));
                return null;
            }
        };

        thread = new Thread(task);
        thread.start();
    }
}
