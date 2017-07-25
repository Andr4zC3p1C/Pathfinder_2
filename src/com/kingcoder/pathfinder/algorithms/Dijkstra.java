package com.kingcoder.pathfinder.algorithms;

import com.kingcoder.pathfinder.Algorithm;
import com.kingcoder.pathfinder.graph.Node;

public class Dijkstra extends Algorithm{

    public Dijkstra(){
        super(AlgorithmType.DIJKSTRA);
    }

    public void runAlgorithm(){
        openedHeap.add(startNode);
        Node[] neighbors;
        Node currentNode;

        while(!openedHeap.isEmpty()){
            currentNode = getMin(openedHeap);
            openedHeap.remove(currentNode);
            closedHeap.add(currentNode);

            if(currentNode.equals(goalNode)) {
                goalNode = currentNode;
                break;
            }

            neighbors = main.getGraph().getNeighbours(currentNode, optionsBox.isDiagonal());

            for(Node n : neighbors){
                if(n == null || hasNode(closedHeap, n))
                    continue;

                if (hasNode(openedHeap, n)) {
                    n = getNodeFromList(openedHeap, n);
                }else{
                    openedHeap.add(n);
                }

                int newCostG = currentNode.g + Node.distance(currentNode, n);
                if(newCostG < n.g) {
                    n.updateGCost(newCostG);
                    n.parent = currentNode;
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

            currentNode = getMin(openedHeap);
            openedHeap.remove(currentNode);
            closedHeap.add(currentNode);

            if(currentNode.equals(goalNode)) {
                goalNode = currentNode;
                break;
            }

            neighbors = main.getGraph().getNeighbours(currentNode, optionsBox.isDiagonal());

            for(Node n : neighbors){
                if(n == null || hasNode(closedHeap, n))
                    continue;

                if (hasNode(openedHeap, n)) {
                    n = getNodeFromList(openedHeap, n);
                }else{
                    openedHeap.add(n);
                }

                int newCostG = currentNode.g + Node.distance(currentNode, n);
                if(newCostG < n.g) {
                    n.updateGCost(newCostG);
                    n.parent = currentNode;
                }
            }
        }
    }

}
