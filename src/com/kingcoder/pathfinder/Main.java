package com.kingcoder.pathfinder;

import com.kingcoder.pathfinder.algorithms.*;
import com.kingcoder.pathfinder.graph.Graph;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    public static boolean done;

    private Graph graph;
    private Toolbox toolbox;
    private Algorithm[] algorithms;
    private Thread algorithmThread;
    private boolean runAlgorithms;
    private InfoDialog infoDialog;

    public void start(Stage stage) throws Exception{
        // Creating the scene
        Group root = new Group();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

        init(screenBounds);

        HBox box = new HBox();
        box.getChildren().addAll(toolbox, graph.getCanvas());
        root.getChildren().add(box);

        // Initializing the stage
        stage.setTitle("Pathfinder");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

        // showing the information:
        infoDialog = new InfoDialog();
        infoDialog.showAndWait();
    }

    private void init(Rectangle2D screenBounds){
        Algorithm.setMainClass(this);
        AlgorithmOptions.setMain(this);
        Toolbox.setMain(this);
        Graph.setMain(this);

        // Graph
        graph = new Graph((int)screenBounds.getWidth() - Toolbox.WIDTH, (int)screenBounds.getHeight());

        // Toolbox
        toolbox = new Toolbox((int)screenBounds.getHeight());

        // Initialising algorithms
        algorithms = new Algorithm[3];
        algorithms[0] = new BreathFirstSearch();
        algorithms[1] = new Dijkstra();
        algorithms[2] = new A_Star();
        //algorithms[3] = new JumpPointSearch();
        //algorithms[4] = new Theta_Star();

        // Dodajanje Node-ov v VBox od toolbox-a
        toolbox.pack(algorithms);

        runAlgorithms = false;
        done = false;
        algorithmThread = new Thread(new Runnable() {
            public void run() {
                while(true){
                    if(runAlgorithms){
                        for(int i=0; i < algorithms.length; i++){
                            algorithms[i].run(graph.getStartNode(), graph.getGoalNode());
                        }

                        // Ce se je graf pobrisal, naj se JUST_CLEARED ponastavi, za nadaljno uporabo
                        toolbox.resetRunButton();
                        runAlgorithms = false;
                    }

                    try {
                        Thread.sleep(100);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }

                    if(done)
                        break;
                }
            }
        });

        algorithmThread.start();
        graph.start();
    }

    public void closeAllAlgorithmOptions(){
        for(int i = 0; i < algorithms.length; i++){
            algorithms[i].closeOptions();
        }
    }

    public void runAlgorithms(){
        runAlgorithms = true;
    }

    public void showInfo(){
        toolbox.pausedAlg();
        infoDialog.showAndWait();
    }

    public boolean areAlgorithmsRunning(){
        return runAlgorithms;
    }

    public Graph getGraph(){
        return graph;
    }

    public Toolbox getToolbox(){
        return toolbox;
    }

    public Algorithm[] getAlgorithms(){
        return algorithms;
    }

    public static void main(String[] args) {
        launch(args);
        done = true;
    }
}
