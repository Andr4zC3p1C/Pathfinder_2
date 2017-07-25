package com.kingcoder.pathfinder.algorithms;

import com.kingcoder.pathfinder.Algorithm;
import com.kingcoder.pathfinder.graph.Node;

public class BreathFirstSearch extends Algorithm {

    public BreathFirstSearch() {
        super(AlgorithmType.BFS);
    }

    public void runAlgorithm(){
        openedHeap.add(startNode);
        Node[] neighbors;
        Node currentNode;

        while(!openedHeap.isEmpty()){
            currentNode = openedHeap.get(0);
            openedHeap.remove(currentNode);

            if(currentNode.equals(goalNode)) {
                goalNode = currentNode;
                break;
            }

            neighbors = main.getGraph().getNeighbours(currentNode, optionsBox.isDiagonal());

            for(Node n : neighbors){
                if(n == null)
                    continue;

                if(!hasNode(closedHeap, n)){
                    openedHeap.add(n);
                    n.parent = currentNode;
                    closedHeap.add(n);
                }
            }
        }
    }

    public void runAlgorithmAnimated(){
        openedHeap.add(startNode);
        Node[] neighbors;
        Node currentNode;

        while(!openedHeap.isEmpty()){
            sleep(sleepTime);
            if(shouldStop())
                break;

            currentNode = openedHeap.get(0);
            openedHeap.remove(currentNode);

            if(currentNode.equals(goalNode)) {
                goalNode = currentNode;
                break;
            }

            neighbors = main.getGraph().getNeighbours(currentNode, optionsBox.isDiagonal());

            for(Node n : neighbors){
                if(n == null)
                    continue;

                if(!hasNode(closedHeap, n)){
                    openedHeap.add(n);
                    n.parent = currentNode;
                    closedHeap.add(n);
                }
            }
        }
    }

}
