package com.kingcoder.pathfinder.graph;

import com.kingcoder.pathfinder.Algorithm;
import com.kingcoder.pathfinder.Main;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Graph extends AnimationTimer{

    public static final int SQUARE_SIZE = 24;
    public static final byte EMPTY_NODE_ID = 0;
    public static final byte HEAVY_NODE_ID = 1;
    public static final byte OBSTACLE_NODE_ID = 2;

    // Graphics
    public static final Color EMPTY_COLOR = Color.web("#DADADA");
    public static final Color HEAVY_COLOR = Color.web("#ADADAD");
    public static final Color WALL_COLOR = Color.web("#545454");
    public static final Color START_COLOR = Color.web("#3DC62B");
    public static final Color GOAL_COLOR = Color.web("#3344C4");
    public static final Color CLOSED_COLOR = Color.web("#91C8FF");
    public static final Color OPENED_COLOR = Color.web("#7CFF9F");

    private static Main main;

    private Canvas canvas;
    private GraphicsContext gc;

    // Karta
    private byte[] map;
    private int mWidth, mHeight;
    private Vector2 startNode, goalNode;

    // Input
    private int mouseX, mouseY;
    private byte drawType = OBSTACLE_NODE_ID;  // For the type of node to be set to map on input
    private boolean mouseDraggingStartNode, mouseDraggingGoalNode;

    public Graph(int width, int height){
        canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        mWidth = width / SQUARE_SIZE + 1;
        mHeight = height / SQUARE_SIZE;
        map = new byte[mWidth * mHeight];

        startNode = new Vector2((mWidth / 2) - 10, mHeight / 2);
        goalNode = new Vector2((mWidth / 2) + 10, mHeight/2);

        mouseDraggingStartNode = false;
        mouseDraggingGoalNode = false;

        setupCanvasInput();
    }

    public void clearGrid(){
        for(int i=0; i < map.length; i++){
            map[i] = EMPTY_NODE_ID;
        }
    }

    private void setupCanvasInput(){
        // Mouse input
        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                mouseX = (int)event.getX() / SQUARE_SIZE;
                mouseY = (int)event.getY() / SQUARE_SIZE;
            }
        });
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(main.areAlgorithmsRunning())
                    return;

                mouseX = (int)event.getX() / SQUARE_SIZE;
                mouseY = (int)event.getY() / SQUARE_SIZE;

                if(startNode.equals(mouseX, mouseY)) {
                    mouseDraggingStartNode = true;
                }else if(goalNode.equals(mouseX, mouseY)){
                    mouseDraggingGoalNode = true;
                }

                if(mouseDraggingStartNode){
                    startNode.x = mouseX;
                    startNode.y = mouseY;
                }else if(mouseDraggingGoalNode){
                    goalNode.x = mouseX;
                    goalNode.y = mouseY;
                }else{
                    setNode(mouseX, mouseY);
                }
            }
        });
        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(main.areAlgorithmsRunning())
                    return;

                if(!(startNode.equals(mouseX, mouseY) || goalNode.equals(mouseX, mouseY))){
                    setNode(mouseX, mouseY);
                }
            }
        });
        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if(main.areAlgorithmsRunning())
                    return;

                mouseDraggingStartNode = false;
                mouseDraggingGoalNode = false;
            }
        });
    }

    private void clearCanvas(){
        gc.setFill(EMPTY_COLOR);
        gc.fillRect(0,0, canvas.getWidth(), canvas.getHeight());
    }

    private void setNode(int x, int y){
        map[x + y * mWidth] = drawType;
    }

    private void drawGraph(){
        for(int y=0; y < mHeight; y++) {
            for (int x = 0; x < mWidth; x++) {
                switch(map[x + y * mWidth]){
                case HEAVY_NODE_ID:
                    gc.setFill(HEAVY_COLOR);
                    gc.fillRect(x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    break;

                case OBSTACLE_NODE_ID:
                    gc.setFill(WALL_COLOR);
                    gc.fillRect(x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    break;
                }
            }
        }

        gc.setFill(START_COLOR);
        gc.fillRect(startNode.x * SQUARE_SIZE, startNode.y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

        gc.setFill(GOAL_COLOR);
        gc.fillRect(goalNode.x * SQUARE_SIZE, goalNode.y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    private void drawGrid(){
        gc.setFill(Color.WHITE);

        // horizontal
        for(int y = 0; y <= mHeight; y++){
            gc.fillRect(0, y * SQUARE_SIZE - 1, mWidth * SQUARE_SIZE, 1);
        }

        // vertical
        for(int x = 0; x <= mWidth; x++){
            gc.fillRect(x * SQUARE_SIZE - 1, 0, 1, mHeight * SQUARE_SIZE);
        }
    }

    private void drawPaths(){
        gc.setLineWidth(2);

        Algorithm[] algorithms = main.getAlgorithms();
        ArrayList<Vector2> path;
        Algorithm alg;
        Vector2 point1, point2;
        for(int i=0; i < algorithms.length; i++){
            alg = algorithms[i];

            if(alg.isPathShown()){
                gc.setStroke(alg.getColor());
                path = alg.getPath();

                for(int j = 1; j < path.size(); j++){
                    point1 = path.get(j-1);
                    point2 = path.get(j);

                    gc.strokeLine(point1.x * SQUARE_SIZE + SQUARE_SIZE / 2, point1.y * SQUARE_SIZE + SQUARE_SIZE / 2,
                                  point2.x * SQUARE_SIZE + SQUARE_SIZE / 2, point2.y * SQUARE_SIZE + SQUARE_SIZE / 2);
                }
            }
        }
    }

    private void drawAnimation(){
        Algorithm[] algorithms = main.getAlgorithms();
        ArrayList<Node> openedHeap, closedHeap;
        Algorithm alg;
        Algorithm.AlgorithmType type = main.getToolbox().getDrawType();

        for(int i=0; i < algorithms.length; i++){
            alg = algorithms[i];

            if(type == alg.getType()) {
                openedHeap = alg.getOpenedHeap();
                closedHeap = alg.getClosedHeap();

                // CLOSED
                gc.setFill(CLOSED_COLOR);
                for (int j = 0; j < closedHeap.size(); j++) {
                    Node n = closedHeap.get(j);
                    if (n != null)
                        gc.fillRect(n.x * SQUARE_SIZE, n.y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                }

                // OPENED
                gc.setFill(OPENED_COLOR);
                for (int j = 0; j < openedHeap.size(); j++) {
                    Node n = openedHeap.get(j);
                    if (n != null) {
                        gc.fillRect(n.x * SQUARE_SIZE, n.y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    }
                }
            }
        }
    }

    private void draw(){
        clearCanvas();
        drawAnimation();
        drawGraph();
        drawGrid();
        drawPaths();
    }

    // this is the loop for the map graphics
    public void handle(long now) {
        draw();

        // update toolbox
        main.getToolbox().update();
    }

    public void setDrawType(byte type){
        drawType = type;
    }

    public static void setMain(Main main){
        Graph.main = main;
    }

    // Functions for algorithms using the graph
    public boolean isInBounds(Vector2 n){
        return n.x >= 0 && n.x < mWidth && n.y >= 0 && n.y < mHeight;
    }

    public boolean isValid(Vector2 n){
        if(!isInBounds(n))
            return false;

        if(map[n.x + n.y * mWidth] == OBSTACLE_NODE_ID)
            return false;

        return true;
    }

    private boolean isValid(Vector2 n, Vector2 parent){
        if(!isValid(n))
            return false;

        int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
        int dx = n.x - parent.x;
        int dy = n.y - parent.y;
        if(dx == -1 && dy == -1){   // left - down
            x1 = parent.x - 1;
            y1 = parent.y;
            x2 = parent.x;
            y2 = parent.y - 1;
        }else if(dx == 1 && dy == -1){   // right - down
            x1 = parent.x;
            y1 = parent.y - 1;
            x2 = parent.x + 1;
            y2 = parent.y;
        }else if(dx == 1 && dy == 1){   // right - up
            x1 = parent.x;
            y1 = parent.y + 1;
            x2 = parent.x + 1;
            y2 = parent.y;
        }else if(dx == -1 && dy == 1){   // left - up
            x1 = parent.x;
            y1 = parent.y + 1;
            x2 = parent.x - 1;
            y2 = parent.y;
        }

        if(x1 != 0 || x2 != 0 || y1 != 0 || y2 != 0) {
            if (isInBounds(new Vector2(x1, y1)) && isInBounds(new Vector2(x2, y2)) && map[x1 + y1 * mWidth] == OBSTACLE_NODE_ID && map[x2 + y2 * mWidth] == OBSTACLE_NODE_ID) {
                // Both nodes are walls so we CANNOT travel diagonally between them
                return false;
            }
        }

        return true;
    }

    public Node[] getNeighbours(Node n, boolean diagonal){
        Node[] result;

        if(diagonal)
            result = new Node[8];
        else
            result = new Node[4];

        Node newNode;

        int index = 0;
        int indexN = 0;
        int indexD = 0;
        for(int dx = -1; dx <= 1; dx++){
            for(int dy = -1; dy <= 1; dy++){
                if(dx == 0 && dy == 0)
                    continue;

                // diagonal
                if(dx != 0 && dy != 0){
                    if(!diagonal)
                        continue;

                    index = 4+indexD;
                    indexD++;
                }else{
                    index = indexN;
                    indexN++;
                }

                newNode = new Node(new Vector2(n.x + dx, n.y + dy));

                if(isValid(newNode, n)) {
                    if(map[newNode.x + newNode.y * mWidth] == EMPTY_NODE_ID)
                        newNode.setCost(Node.NORMAL_COST);
                    else
                        newNode.setCost(Node.HEAVY_COST);

                    result[index] = newNode;
                }
            }
        }

        return result;
    }

    public boolean isForcedNeighbour(Node n){
        return false;
    }

    // GETTERS
    public Canvas getCanvas(){
        return canvas;
    }
    public Vector2 getStartNode(){ return startNode; }
    public Vector2 getGoalNode(){ return goalNode; }
}
