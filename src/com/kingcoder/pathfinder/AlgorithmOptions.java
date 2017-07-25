package com.kingcoder.pathfinder;

import com.kingcoder.pathfinder.graph.Node;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class AlgorithmOptions extends VBox{

    private static Main main;

    private boolean isShown = false;

    private CheckBox showPath;
    private CheckBox diagonal;

    private RadioButton animateAlg;

    // heuristics
    private Node.HeuristicType heuristicType;
    private ToggleGroup heuristicGroup;
    private RadioButton manhattan;
    private RadioButton chebyshev;
    private RadioButton octile;
    private RadioButton euclidean;

    // Števec
    private long clock;
    private Label clockLabel;

    public AlgorithmOptions(Algorithm.AlgorithmType type){
        showPath = new CheckBox("Show path");
        diagonal = new CheckBox("Diagonal movement");

        animateAlg = new RadioButton("Show nodes");
        animateAlg.setToggleGroup(main.getToolbox().getAnimateGroup());
        animateAlg.setUserData(type);

        // Po defualtu je nastalvljen BFS
        if(type == Algorithm.AlgorithmType.BFS) {
            showPath.setSelected(true);
            animateAlg.setSelected(true);
            isShown = true;
        }

        // Stevec
        HBox clockBox = new HBox();
        clockBox.setSpacing(5);
        Label prefixC = new Label("Time taken: ");
        clockLabel = new Label("" + clock);
        clockLabel.setTextFill(Color.color(1, 0.5569, 0.2784)); // Chosen using IntelliJ IDEA
        Label suffixC = new Label(" micro sec");
        clockBox.getChildren().addAll(prefixC, clockLabel, suffixC);

        getChildren().add(clockBox);
        getChildren().add(showPath);
        getChildren().add(diagonal);
        getChildren().add(animateAlg);

        // Breath First Search nima Heuristic-ov
        if(type != Algorithm.AlgorithmType.BFS && type != Algorithm.AlgorithmType.DIJKSTRA)
            getChildren().add(heuristicBox());

        heuristicType = Node.HeuristicType.MANHATTAN;

        setAlignment(Pos.TOP_LEFT);
        setSpacing(10);
        setPadding(new Insets(0,0, 10, 25));
        setVisible(isShown);
        setManaged(isShown);
    }

    private VBox heuristicBox(){
        VBox heuBox = new VBox();
        heuBox.setSpacing(10);

        Label title = new Label("Heuristics:");
        title.setTextFill(Color.web("#99BCFF"));

        heuristicGroup = new ToggleGroup();
        heuristicGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                heuristicType = (Node.HeuristicType) newValue.getUserData();
            }
        });

        manhattan = new RadioButton("Manhattan");
        manhattan.setToggleGroup(heuristicGroup);
        manhattan.setUserData(Node.HeuristicType.MANHATTAN);
        manhattan.setSelected(true);

        chebyshev = new RadioButton("Chebyshev");
        chebyshev.setToggleGroup(heuristicGroup);
        chebyshev.setUserData(Node.HeuristicType.CHEBYSHEV);

        octile = new RadioButton("Octile");
        octile.setToggleGroup(heuristicGroup);
        octile.setUserData(Node.HeuristicType.OCTILE);

        euclidean = new RadioButton("Euclidean");
        euclidean.setToggleGroup(heuristicGroup);
        euclidean.setUserData(Node.HeuristicType.EUCLIDEAN);

        heuBox.getChildren().addAll(title, manhattan, chebyshev, octile, euclidean);
        return heuBox;
    }

    // SETTERS
    public void setShown(boolean shown){
        isShown = shown;
        setVisible(isShown);
        setManaged(isShown);
    }

    public void setClock(long clock){
        this.clock = clock;
        clockLabel.setText("" + clock);
    }

    public static void setMain(Main main){
        AlgorithmOptions.main = main;
    }

    // GETTERS
    public boolean isShown(){
        return isShown;
    }

    public boolean isPathShown(){
        return showPath.isSelected();
    }

    public boolean isDiagonal(){ return diagonal.isSelected();}

    public Node.HeuristicType getHeuristicType(){
        return heuristicType;
    }

}
