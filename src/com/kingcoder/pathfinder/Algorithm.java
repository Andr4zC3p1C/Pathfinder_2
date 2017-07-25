package com.kingcoder.pathfinder;

import com.kingcoder.pathfinder.graph.Node;
import com.kingcoder.pathfinder.graph.Vector2;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

public abstract class Algorithm extends HBox{
    public enum AlgorithmType{
        BFS("Breath First Search", Color.web("#FF2100")), DIJKSTRA("Dijkstra", Color.web("#FFA530")), A_STAR("A - Star", Color.web("#42BC3C")),  //Color.web("#77FF2D")
        JPS("Jump Point Search", Color.web("#54B2FF")), THETA_STAR("Theta - Star", Color.web("#C23FFF"));

        private String name;
        private Color color;

        AlgorithmType(String name, Color color){
            this.name = name;
            this.color = color;
        }

        public String getName(){
            return name;
        }

        public Color getColor(){
            return color;
        }
    }

    protected static Main main;

    // graphics
    private static final Background arrowUpImage = new Background(new BackgroundImage(new Image("file:res/arrow_up.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
    private static final Background arrowDownImage = new Background(new BackgroundImage(new Image("file:res/arrow_down.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT));
    public static final Background titleColorHover = new Background( new BackgroundFill( Color.web( "#344A7A" ), CornerRadii.EMPTY, Insets.EMPTY ) );
    public static final Background titleColorBlank = new Background( new BackgroundFill( Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY ) );
    private Label title;
    private Color color;

    protected int sleepTime;
    protected AlgorithmOptions optionsBox;

    // Algorithm
    private AlgorithmType type;
    protected ArrayList<Node> closedHeap, openedHeap;
    protected Node startNode, goalNode;
    private ArrayList<Vector2> path;
    private long clock;
    private boolean clockChanged = false;
    private boolean paused = false;

    // Ostalo
    private Toolbox toolbox;

    public Algorithm(AlgorithmType type) {
        this.type = type;

        // graphics
        this.color = type.getColor();
        this.title = new Label(type.getName());
        this.title.setFont(new Font("sans_serif", 20));
        this.title.setTextFill(Color.color(0.7, 0.7, 0.7));
        final VBox titleBox = new VBox(this.title);
        titleBox.setAlignment(Pos.CENTER);

        titleBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                boolean isShown = optionsBox.isShown();

                main.closeAllAlgorithmOptions();

                if(isShown){
                    optionsBox.setShown(false);
                }else{
                    optionsBox.setShown(true);
                }
            }
        });

        titleBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                titleBox.setBackground(titleColorHover);
            }
        });

        titleBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                titleBox.setBackground(titleColorBlank);
            }
        });

        // Nastavitve za algoritm
        optionsBox = new AlgorithmOptions(type);

        // Glavni box za opcije
        VBox mainBox = new VBox(titleBox, optionsBox);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPrefWidth(Toolbox.WIDTH - 5);

        // Barvna črta ob strani
        VBox colorBox = new VBox();
        colorBox.setPrefWidth(5);
        colorBox.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY )));

        // Adding nodes to this Node
        getChildren().addAll(mainBox, colorBox);
        setPrefWidth(Toolbox.WIDTH);

        // Algorithm
        openedHeap = new ArrayList<Node>();
        closedHeap = new ArrayList<Node>();
        path = new ArrayList<Vector2>();

        // Ostalo
        toolbox = main.getToolbox();
    }

    public void closeOptions(){
        optionsBox.setShown(false);
    }

    // Se izvede v posebni niti za algoritme
    public void run(Vector2 startNode, Vector2 goalNode){
        clear();

        Node.setHeuristicType(optionsBox.getHeuristicType());

        this.startNode = new Node(startNode);
        this.startNode.setCost(0);

        this.goalNode = new Node(goalNode);
        this.goalNode.setCost(Node.NORMAL_COST);

        this.startNode.heuristic(this.goalNode);

        if(toolbox.shouldAnimate() && toolbox.getDrawType() == type) {
            runAlgorithmAnimated();
        }else{
            clock = System.nanoTime();
            runAlgorithm();
            clock = System.nanoTime() - clock; // how much time it took to calculate
            clock /= 1000;
            clockChanged = true;
        }

        generatePath();

        if(Toolbox.JUST_CLEARED){
            clear();
        }
    }

    public void update(boolean paused){
        // update clock label, since clock is measured in a different thread
        if(clockChanged) {
            optionsBox.setClock(clock);
            clockChanged = false;
        }

        this.paused = paused;
    }

    protected abstract void runAlgorithm();

    protected abstract void runAlgorithmAnimated();

    public void generatePath(){
        if(goalNode.parent == null)
            return;

        Node n = goalNode;
        path.add(n);
        while(!n.equals(startNode)){
            path.add(n.parent);
            n = n.parent;
        }
    }

    protected void sleep(int time){
        try {
            while(paused){
                // Do nothing while paused
                // Have to do sth to update paused
                Thread.sleep(1);

                if(shouldStop() || !paused)
                    break;
            }

            long timer = System.nanoTime();
            while (System.nanoTime() - timer < time) {
                // Do nothing for the time; hence sleep
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void clear(){
        openedHeap.clear();
        closedHeap.clear();
        path.clear();
    }

    // SETTERS
    public void setSleepTime(int time){
        sleepTime = time;
    }

    public static void setMainClass(Main main){
        Algorithm.main = main;
    }

    // GETTERS
    protected boolean shouldStop(){
        return Main.done || Toolbox.JUST_CLEARED;
    }

    protected boolean hasNode(ArrayList<Node> list, Node n){
        for(Node n1 : list){
            if(n.equals(n1))
                return true;
        }

        return false;
    }

    protected Node getMin(ArrayList<Node> list){
        Node min = list.get(0);
        Node n;
        for(int i = 1; i < list.size(); i++){
            n = list.get(i);
            if(n.f < min.f)
                min = n;
        }

        return min;
    }

    protected Node getNodeFromList(ArrayList<Node> list, Node n){
        for(Node n1 : list){
            if(n.equals(n1))
                return n1;
        }

        return null;
    }

    public AlgorithmType getType(){
        return type;
    }

    public ArrayList<Vector2> getPath(){
        return path;
    }

    public ArrayList<Node> getOpenedHeap(){
        return openedHeap;
    }

    public ArrayList<Node> getClosedHeap(){
        return closedHeap;
    }

    public boolean isPathShown(){
        return optionsBox.isPathShown();
    }

    public Color getColor(){
        return color;
    }
}
