package com.kingcoder.pathfinder.graph;

public class Node extends Vector2{
    public enum HeuristicType{
        MANHATTAN,
        CHEBYSHEV,
        OCTILE,
        EUCLIDEAN
    }

    public static final int NORMAL_COST = 100;
    public static final int HEAVY_COST = 200;

    // directions
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;
    public static final int DIAGONAL = 3;

    private static HeuristicType heuristicType;

    public int h, g, f, cost, costD;
    public Node parent;

    public Node(){}

    public Node(Vector2 pos){
        this.x = pos.x;
        this.y = pos.y;
        f = g = h = 0;
        g = Integer.MAX_VALUE; // neskoncnost
    }

    public void setCost(int cost){
        this.cost = cost;

        if(heuristicType == HeuristicType.CHEBYSHEV){
            costD = cost;
        }else{
            costD = (int)(cost * Math.sqrt(2));
        }
    }

    public Node translateByOne(int dir){
        Node n = new Node();

        if(dir == HORIZONTAL){
            n.x = x + 1;
            n.y = y;
        }else if(dir == VERTICAL){
            n.x = x;
            n.y = y + 1;
        }else{
            n.x = x + 1;
            n.y = y + 1;
        }

        return n;
    }

    public void updateGCost(int newCost){
        g = newCost;
        f = h + g;
    }

    public void heuristic(Node n1){
        int dx = Math.abs(x - n1.x);
        int dy = Math.abs(y - n1.y);

        switch (heuristicType){
            case MANHATTAN:
                h = cost * (dx + dy);
                break;

            case OCTILE:
            case CHEBYSHEV:
                h = cost * (dx + dy) - (cost * 2 - costD) * Math.min(dx, dy);
                break;

            case EUCLIDEAN:
                h = (int)(cost * Math.sqrt(dx * dx + dy * dy));
                break;
        }

        f = h + g;
    }

    public static int direction(Node n, Node n1){
        int dx = Math.abs(n.x - n1.x);
        int dy = Math.abs(n.y - n1.y);

        if(dx != 0 && dy == 0){
            return HORIZONTAL;
        }else if(dx == 0 && dy != 0){
            return VERTICAL;
        }

        return DIAGONAL;
    }

    public static int distance(Node n, Node n1){
        int dx = Math.abs(n.x - n1.x);
        int dy = Math.abs(n.y - n1.y);

        return (int)(n1.cost * Math.sqrt(dx * dx + dy * dy));
    }

    public static void setHeuristicType(HeuristicType hT){
        heuristicType = hT;
    }
}
