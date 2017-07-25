package com.kingcoder.pathfinder;

import com.kingcoder.pathfinder.graph.Graph;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class InfoDialog extends Dialog<String>{

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 780;

    private VBox panel;

    public InfoDialog(){
        setTitle("Information");
        setHeaderText(null);

        getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        Node closeButton = getDialogPane().lookupButton(ButtonType.CLOSE);
        closeButton.managedProperty().bind(closeButton.visibleProperty());
        closeButton.setVisible(false);

        setupPanel();

        getDialogPane().setContent(panel);
    }

    private void setupPanel(){
        panel = new VBox();
        panel.setAlignment(Pos.TOP_LEFT);
        panel.setPrefSize(WIDTH, HEIGHT);
        panel.setBackground(Toolbox.backgroundColor);
        panel.setSpacing(10);

        Label introduction = new Label("The purpose of this application is to visualize and compare some of the pathfinding algorithms in action.\n\n" +
                "Those are:" +
                " Breath First Search" +
                ", Dijkstra and" +
                " A-Star." +
                "\n\n" +
                "These algorithms find their shortest path from START to GOAL in a grid based graph of nodes." +
                "The graph can also be more complicated and realistic\nusing so called nav-meshes, however for the ease of visualization the graph in this program is just a grid of nodes.\n" +
                "\n" +
                "A-Star can also have a so called heuristic configured, which is a way for the algorithm to orient its search depending on the GOAL node.\n");
        introduction.setTextFill(Color.color(0.5255, 0.702, 0.3176));   // Izbrana barva z IntelliJ IDEA
        introduction.setFont(Font.font(15));
        introduction.setPadding(new Insets(0,0,30,0));

        Label finderLab = new Label("WHAT IS WHAT:");
        finderLab.setTextFill(Color.color(0.7255, 0.4824, 0.2078));
        finderLab.setFont(Font.font(13));

        Label howToUseLab = new Label("HOW TO USE:\n" +
                "");
        howToUseLab.setTextFill(Color.color(0.7255, 0.4824, 0.2078));
        howToUseLab.setFont(Font.font(13));

        VBox line = new VBox();
        line.setBackground(Toolbox.titleColor);
        line.setPrefHeight(1);

        VBox line2 = new VBox();
        line2.setBackground(Toolbox.titleColor);
        line2.setPrefHeight(1);

        VBox line3 = new VBox();
        line3.setBackground(Toolbox.titleColor);
        line3.setPrefHeight(1);

        HBox signitureBox = new HBox();
        signitureBox.setAlignment(Pos.CENTER);
        Label signiture = new Label("Made by Andraž Čepič");
        signiture.setTextFill(Color.color(0.5255, 0.702, 0.3176));   // Izbrana barva z IntelliJ IDEA
        signiture.setFont(Font.font(15));
        signitureBox.getChildren().add(signiture);

        // close vbox
        final VBox closeBox = new VBox();
        closeBox.setBackground(Algorithm.titleColorBlank);
        closeBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                closeBox.setBackground(Algorithm.titleColorHover);
            }
        });
        closeBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                closeBox.setBackground(Algorithm.titleColorBlank);
            }
        });
        closeBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                close();
            }
        });

        Label closeLabel = new Label("Close");
        closeLabel.setTextFill(Color.gray(0.7255));
        closeLabel.setFont(Font.font(15));
        closeBox.setAlignment(Pos.CENTER);
        closeBox.getChildren().add(closeLabel);
        closeBox.setPadding(new Insets(10, 0,10,0));

        panel.getChildren().add(introduction);
        panel.getChildren().add(line);
        panel.getChildren().add(finderLab);
        panel.getChildren().add(setupFinder());
        panel.getChildren().add(line2);
        panel.getChildren().add(howToUseLab);
        panel.getChildren().add(setupHowToUse());
        panel.getChildren().add(line3);
        panel.getChildren().add(signitureBox);
        panel.getChildren().add(closeBox);
    }

    private HBox setupFinder(){
        HBox finder = new HBox();
        finder.setSpacing(40);

        VBox line = new VBox();
        line.setBackground(Toolbox.titleColor);
        line.setPrefWidth(1);

        ///////
        // GRID
        ///////
        VBox gridBox = new VBox();
        gridBox.setSpacing(5);

        Label gridLab = new Label("Most of the screen is the grid-based graph where:\n");
        gridLab.setTextFill(Color.gray(0.7255));
        gridLab.setFont(Font.font(13));

        // Start node
        HBox startNode = new HBox();
        startNode.setSpacing(10);
        startNode.setPadding(new Insets(0,0,0, 20));

        Rectangle startNodeRec = new Rectangle();
        startNodeRec.setWidth(20);
        startNodeRec.setHeight(20);
        startNodeRec.setFill(Graph.START_COLOR);

        Label startNodeLabel = new Label("is the START node,");
        startNodeLabel.setTextFill(Color.gray(0.7255));
        startNodeLabel.setFont(Font.font(13));
        startNode.getChildren().addAll(startNodeRec, startNodeLabel);

        // Goal node
        HBox goalNode = new HBox();
        goalNode.setSpacing(10);
        goalNode.setPadding(new Insets(0,0,0, 20));

        Rectangle goalNodeRec = new Rectangle();
        goalNodeRec.setWidth(20);
        goalNodeRec.setHeight(20);
        goalNodeRec.setFill(Graph.GOAL_COLOR);

        Label goalNodeLabel = new Label("is the GOAL node,");
        goalNodeLabel.setTextFill(Color.gray(0.7255));
        goalNodeLabel.setFont(Font.font(13));
        goalNode.getChildren().addAll(goalNodeRec, goalNodeLabel);

        // Obstacle node
        HBox obstacleNode = new HBox();
        obstacleNode.setSpacing(10);
        obstacleNode.setPadding(new Insets(0,0,0, 20));

        Rectangle obstacleNodeRec = new Rectangle();
        obstacleNodeRec.setWidth(20);
        obstacleNodeRec.setHeight(20);
        obstacleNodeRec.setFill(Graph.WALL_COLOR);

        Label obstacleNodeLabel = new Label("is an obstacle node,");
        obstacleNodeLabel.setTextFill(Color.gray(0.7255));
        obstacleNodeLabel.setFont(Font.font(13));
        obstacleNode.getChildren().addAll(obstacleNodeRec, obstacleNodeLabel);

        // Heavy node
        HBox heavyNode = new HBox();
        heavyNode.setSpacing(10);
        heavyNode.setPadding(new Insets(0,0,0, 20));

        Rectangle heavyNodeRec = new Rectangle();
        heavyNodeRec.setWidth(20);
        heavyNodeRec.setHeight(20);
        heavyNodeRec.setFill(Graph.HEAVY_COLOR);

        Label heavyNodeLabel = new Label("is a heavier node,");
        heavyNodeLabel.setTextFill(Color.gray(0.7255));
        heavyNodeLabel.setFont(Font.font(13));
        heavyNode.getChildren().addAll(heavyNodeRec, heavyNodeLabel);

        // Free node
        HBox freeNode = new HBox();
        freeNode.setSpacing(10);
        freeNode.setPadding(new Insets(0,0,0, 20));

        Rectangle freeNodeRec = new Rectangle();
        freeNodeRec.setWidth(20);
        freeNodeRec.setHeight(20);
        freeNodeRec.setFill(Graph.EMPTY_COLOR);

        Label freeNodeLabel = new Label("is an obstacle-free node,");
        freeNodeLabel.setTextFill(Color.gray(0.7255));
        freeNodeLabel.setFont(Font.font(13));
        freeNode.getChildren().addAll(freeNodeRec, freeNodeLabel);

        // Open set node
        HBox openNode = new HBox();
        openNode.setSpacing(10);
        openNode.setPadding(new Insets(0,0,0, 20));

        Rectangle openNodeRec = new Rectangle();
        openNodeRec.setWidth(20);
        openNodeRec.setHeight(20);
        openNodeRec.setFill(Graph.OPENED_COLOR);

        Label openNodeLabel = new Label("is a node in queue to be checked.");
        openNodeLabel.setTextFill(Color.gray(0.7255));
        openNodeLabel.setFont(Font.font(13));
        openNode.getChildren().addAll(openNodeRec, openNodeLabel);

        // Closed set node
        HBox closedNode = new HBox();
        closedNode.setSpacing(10);
        closedNode.setPadding(new Insets(0,0,0, 20));

        Rectangle closedNodeRec = new Rectangle();
        closedNodeRec.setWidth(20);
        closedNodeRec.setHeight(20);
        closedNodeRec.setFill(Graph.CLOSED_COLOR);

        Label closedNodeLabel = new Label("is a node that has already been checked and");
        closedNodeLabel.setTextFill(Color.gray(0.7255));
        closedNodeLabel.setFont(Font.font(13));
        closedNode.getChildren().addAll(closedNodeRec, closedNodeLabel);

        gridBox.getChildren().add(gridLab);
        gridBox.getChildren().add(startNode);
        gridBox.getChildren().add(goalNode);
        gridBox.getChildren().add(obstacleNode);
        gridBox.getChildren().add(heavyNode);
        gridBox.getChildren().add(freeNode);
        gridBox.getChildren().add(closedNode);
        gridBox.getChildren().add(openNode);

        ////////////////
        // Toolbox info
        ////////////////
        VBox toolboxBox = new VBox();
        toolboxBox.setSpacing(5);
        Label toolboxInfo = new Label("To the left is the toolbox where all the algorithm options are configured and displayed.\n" +
                "Each algorithm can have / has: \n" +
                "\t- the time taken to find the path at last search shown,\n" +
                "\t- the path shown,\n" +
                "\t- diagonal search selected or not,\n" +
                "\t- animation selected\n" +
                "\t- its own color for showing path\n" +
                "Some of the Algorithms also have heuristics and at the bottom of the toolbox are other options.\n" +
                "There you can choose to animate algorithms, animation speed, drawing styles for graph and \nactually run the algorithms.");
        toolboxInfo.setTextFill(Color.gray(0.7255));
        toolboxInfo.setFont(Font.font(13));

        toolboxBox.getChildren().add(toolboxInfo);

        finder.getChildren().add(gridBox);
        finder.getChildren().add(line);
        finder.getChildren().add(toolboxBox);

        return finder;
    }

    private VBox setupHowToUse(){
        VBox howToUse = new VBox();

        Label howToUseLab = new Label("Changing the graph:\n" +
                "\t- select the 'drawing pen' in the toolbox to draw with the selected node" +
                "\t\t\t\t\t- click on Clear / Clear and Stop Button to clear the grid\n" +
                "\t- use LMB to draw\n" +
                "\n Using the algorithms:\n" +
                "\t- click on an algorithm on the left to change its settings\n" +
                "\t- click on Run The Algorithms button to run them and then pause them if needed\n" +
                "\n To reopen this window click on the Show Information Button.");
        howToUseLab.setTextFill(Color.gray(0.7255));
        howToUseLab.setFont(Font.font(13));

        howToUse.getChildren().add(howToUseLab);
        return howToUse;
    }
}
