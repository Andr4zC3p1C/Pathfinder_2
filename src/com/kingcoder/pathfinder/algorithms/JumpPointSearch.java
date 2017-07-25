package com.kingcoder.pathfinder.algorithms;

import com.kingcoder.pathfinder.Algorithm;
import com.kingcoder.pathfinder.graph.Node;

public class JumpPointSearch extends Algorithm{

    public JumpPointSearch(){
        super(AlgorithmType.JPS);
    }

    public void runAlgorithm(){

    }

    public void runAlgorithmAnimated(){

    }

    private void successors(Node x){
        Node[] neighbours = main.getGraph().getNeighbours(x, true);

        // Ce je zacetna tocka, ni treba odstranjevati
        if(!x.equals(startNode))
            pruneNeighbours(x.parent, neighbours);

        for(Node n : neighbours){
            n = jump(x, Node.direction(x, n));
            closedHeap.add(n);
        }
    }

    private Node jump(Node x, int dir){
        Node n = x.translateByOne(dir);

        if(!main.getGraph().isValid(n))
            return null;

        if(n.equals(goalNode)){
            return n;
        }

        if(hasForcedNeighbour(n)){
            return n;
        }

        if(dir == Node.DIAGONAL){
            if(jump(n, Node.HORIZONTAL) != null)
                return n;

            if(jump(n, Node.VERTICAL) != null)
                return n;
        }

        return jump(n, dir);
    }

    private void pruneNeighbours(Node currNode, Node[] neighbors) {
        Node n;
        int dx;
        int dy;        int costFromParent;
        int costThroughCurrNode;

        // TODO: FORCED NEIGHBOURS
        if (Node.direction(currNode.parent, currNode) == Node.DIAGONAL) {
            for (int i = 0; i < neighbors.length; i++) {
                n = neighbors[i];

                // MANHATTAN
                dx = Math.abs(currNode.parent.x - n.x);
                dy = Math.abs(currNode.parent.y - n.y);
                costFromParent = n.cost * (dx + dy);
                costThroughCurrNode = currNode.g + Node.distance(currNode, n);

                if (costFromParent < costThroughCurrNode)
                    neighbors[i] = null;
            }
        }else{
            for (int i = 0; i < neighbors.length; i++) {
                n = neighbors[i];

                // EUCLIDEAN
                costFromParent = Node.distance(currNode.parent, n);
                costThroughCurrNode = currNode.g + Node.distance(currNode, n);

                if (costFromParent < costThroughCurrNode)
                    neighbors[i] = null;
            }
        }
    }

    private boolean hasForcedNeighbour(Node n){
        Node[] neighbours = main.getGraph().getNeighbours(n, true);
        for(Node n1 : neighbours){
            if(main.getGraph().isForcedNeighbour(n1)){
                return true;
            }
        }

        return false;
    }

}
