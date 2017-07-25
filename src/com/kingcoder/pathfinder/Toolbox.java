package com.kingcoder.pathfinder;

import com.kingcoder.pathfinder.graph.Graph;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Toolbox extends VBox{

    // Da se algoritmi ustavijo
    public static boolean JUST_CLEARED = false;

    public static final int WIDTH = 300;
    public static final double ANIM_SPEED_MIN = 1.0;
    public static final double ANIM_SPEED_MAX = 240.0;

    private static Main main;

    private final int OPTIONS_BOX_PADDING_LEFT = 40;

    public static Background backgroundColor = new Background( new BackgroundFill( Color.web( "#4F6B7F" ), CornerRadii.EMPTY, Insets.EMPTY ) );
    public static Background titleColor = new Background( new BackgroundFill( Color.web( "#47585E" ), CornerRadii.EMPTY, Insets.EMPTY ) );

    // Main
    private VBox algorithmBox; // Node, ki ima uporabniški vmesnik algoritmov
    private Label optionsBoxLabel; // Naslov za ostale opcije

    // Options
    private HBox dialogButtonBox; // Node, ki ima gumb, ki ob pritisku pokaze informacije
    private VBox optionsBox;    // Node, ki ima vse opcije, ki niso algoritmi
    private CheckBox animateAlgorithm;  // CheckBox za animiranje algoritmov
    private Label animationSpeedLabel; // Napis nad drsnikom za hitrost animacije
    private Slider animationSpeedSlider;    // Drsnik za hitrost animacije
    private Button runButton, clearGridButton;  // Gumba za zagon algoritmov in pobris grafa
    private ToggleGroup drawToggleGroup;        // Povezava RadioButton gumbov, ki odloča, kako urejamo graf
    private ToggleGroup animateGroup;           // Povezava za RadioButton gumbe v algoritmih, ki nadzoruje kateri algoritem se prikazuje na grafu
    private RadioButton obstacleDrawButton, nullDrawButton, heavyWeightDrawButton; // RadioButton gumbi za spreminjanje grafa
    private boolean shouldResetRunButton = false;

    // Za algoritme
    private Algorithm.AlgorithmType drawType;

    private boolean pausedAlg;

    public Toolbox(int height){
        setBackground(backgroundColor);
        setAlignment(Pos.TOP_CENTER);
        setPrefWidth(WIDTH);
        setPrefHeight(height);

        algorithmBox = new VBox();
        algorithmBox.setPrefHeight(height - 370);

        optionsBoxLabel = new Label("Other Options");
        optionsBoxLabel.setFont(new Font("sans_serif", 20));
        optionsBoxLabel.setTextFill(Color.web("#99BCFF"));

        dialogButtonBox = new HBox();
        dialogButtonBox.setAlignment(Pos.CENTER);
        dialogButtonBox.setPadding(new Insets(5, 0, 10, 0));
        Button showInfoDialogButton = new Button("Show Information");
        showInfoDialogButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                main.showInfo();
            }
        });
        showInfoDialogButton.setPrefSize(120, 30);
        dialogButtonBox.getChildren().add(showInfoDialogButton);

        // Inicializacija ostalih opcij
        setupOptionsBox();

        // Skupina za  določevanje algoritmov za animiranje in risanje potekov algoritmov
        animateGroup = new ToggleGroup();
        animateGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                drawType = (Algorithm.AlgorithmType) newValue.getUserData();
            }
        });

        // Deafult -> Breath First Search
        drawType = Algorithm.AlgorithmType.BFS;
    }

    private void setupOptionsBox(){
        optionsBox = new VBox();
        optionsBox.setAlignment(Pos.TOP_LEFT);
        optionsBox.setPadding(new Insets(0,0,0,17));
        optionsBox.setSpacing(10);

        animateAlgorithm = new CheckBox("Animate the algorithm");
        animateAlgorithm.setPadding(new Insets(0,0,0,0));

        animationSpeedLabel = new Label("Animation Speed (operations per second):");
        animationSpeedLabel.setPadding(new Insets(0,0,3,0));

        // Animation speed slider
        animationSpeedSlider = new Slider(ANIM_SPEED_MIN, ANIM_SPEED_MAX, ANIM_SPEED_MAX / 2.0);
        animationSpeedSlider.setPrefWidth(50);
        animationSpeedSlider.setPadding(new Insets(5,15,0,0));
        animationSpeedSlider.setShowTickLabels(true);
        animationSpeedSlider.setShowTickMarks(true);
        animationSpeedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                Algorithm[] algorithms = main.getAlgorithms();
                for(Algorithm a : algorithms){
                    a.setSleepTime(1000000000 / newValue.intValue());
                }
            }
        });

        // Creating the buttons
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.TOP_LEFT);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(0, 0, 0 ,0 ));

        runButton = new Button("Run The Algorithms");
        runButton.setPrefWidth(120);
        runButton.setPadding(new Insets(5,5,5,0));
        runButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                if(!main.areAlgorithmsRunning()) {
                    JUST_CLEARED = false;

                    if(animateAlgorithm.isSelected()) {
                        runButton.setText("Pause");
                        clearGridButton.setText("Clear and Stop");
                    }

                    main.runAlgorithms();
                }else{
                    if(animateAlgorithm.isSelected()) {
                        if (!pausedAlg) {   // ni ustavljen in se bo ustavil
                            pausedAlg();
                        }else{ // je ustavljen in se bo nadaljeval
                            runButton.setText("Pause");
                            pausedAlg = false;
                        }
                    }
                }
            }
        });

        clearGridButton = new Button("Clear");
        clearGridButton.setPrefWidth(120);
        clearGridButton.setPadding(new Insets(5,5,5,0));
        clearGridButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                clearGridButton.setText("Clear");

                main.getGraph().clearGrid();
                Algorithm[] alg = main.getAlgorithms();
                for(Algorithm a : alg){
                    a.clear();
                }

                JUST_CLEARED = true;
            }
        });

        buttonBox.getChildren().addAll(runButton, clearGridButton);

        // Draw radio buttons
        Label drawLabel = new Label("Graph pen:");

        drawToggleGroup = new ToggleGroup();
        drawToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                main.getGraph().setDrawType((Byte)newValue.getUserData());
            }
        });

        obstacleDrawButton = new RadioButton("Obstacle pen");
        nullDrawButton = new RadioButton("Eraser pen");
        heavyWeightDrawButton = new RadioButton("Weighted node pen");

        obstacleDrawButton.setUserData(Graph.OBSTACLE_NODE_ID);
        nullDrawButton.setUserData(Graph.EMPTY_NODE_ID);
        heavyWeightDrawButton.setUserData(Graph.HEAVY_NODE_ID);

        obstacleDrawButton.setToggleGroup(drawToggleGroup);
        nullDrawButton.setToggleGroup(drawToggleGroup);
        heavyWeightDrawButton.setToggleGroup(drawToggleGroup);

        obstacleDrawButton.setPadding(new Insets(0,0,5,0));
        nullDrawButton.setPadding(new Insets(0,0,5,0));
        heavyWeightDrawButton.setPadding(new Insets(0,0,5,0));

        obstacleDrawButton.setSelected(true);

        optionsBox.getChildren().addAll(animateAlgorithm, animationSpeedLabel, animationSpeedSlider,
                                        drawLabel, obstacleDrawButton, heavyWeightDrawButton, nullDrawButton, buttonBox);
    }

    public void pack(Node... nodes){
        // Crte med algoritmi
        VBox[] paddings = new VBox[nodes.length];
        for(int i = 0; i < paddings.length; i++){
            paddings[i] = new VBox();
            paddings[i].setPrefHeight(1);
            paddings[i].setBackground(titleColor);
        }

        // Dodajanje grafičnega vmesnika algoritmov v toolbox
        for(int i = 0; i < nodes.length; i++){
            algorithmBox.getChildren().add(nodes[i]);
            algorithmBox.getChildren().add(paddings[i]);
        }

        // Nastavlanje privzete hitrosti animacije
        Algorithm[] algorithms = main.getAlgorithms();
        for(Algorithm a : algorithms){
            a.setSleepTime(1000000000 / (int)animationSpeedSlider.getValue());
        }

        getChildren().addAll(algorithmBox, optionsBoxLabel, dialogButtonBox, optionsBox);
    }

    public void update(){
        Algorithm[] alg = main.getAlgorithms();
        for(Algorithm a : alg){
            a.update(pausedAlg);
        }

        if(shouldResetRunButton){
            runButton.setText("Run The Algorithms");
            shouldResetRunButton = false;
        }
    }

    public void resetRunButton(){
        shouldResetRunButton = true;
        pausedAlg = false;
    }

    // GETTERS
    public boolean isPausedAlg(){
        return pausedAlg;
    }

    public void pausedAlg(){
        if(main.areAlgorithmsRunning()) {
            runButton.setText("Resume");
            pausedAlg = true;
        }
    }

    public boolean shouldAnimate(){
        return animateAlgorithm.isSelected();
    }

    public Algorithm.AlgorithmType getDrawType(){
        return drawType;
    }

    public ToggleGroup getAnimateGroup(){
        return animateGroup;
    }

    public static void setMain(Main main){
        Toolbox.main = main;
    }

}
