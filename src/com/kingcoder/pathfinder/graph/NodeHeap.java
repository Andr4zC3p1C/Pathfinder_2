package com.kingcoder.pathfinder.graph;

/**
 * Binarna kopica(ang. Binary Heap) objektov razreda Node za uporabo pri algoritmih.
 */
public class NodeHeap {
    private int size;
    private Node[] heap;
    // parent is k/2
    // left child is 2*k
    // right child is 2*k + 1

    public NodeHeap(){
        clear();
    }

    public void insert(Node el){
        if(size == heap.length-1)
            doubleSize();

        int pos = ++size;
        for(; pos > 1 && el.f < heap[pos/2].f; pos /= 2){
            heap[pos] = heap[pos/2];
        }

        heap[pos] = el;
    }

    public Node popRoot(){
        Node root = heap[1];
        heap[1] = heap[size--];

        // vstavlanje navzdol
        int k = 1;
        Node tmp = heap[k];
        int childIndex = 0;
        for(; 2*k <= size; k = childIndex) {
            childIndex = 2 * k;

            // izbirnaje manjšega child elementa in preverjanje, če je levi child element na koncu kopice
            if (childIndex != size && heap[childIndex + 1].f < heap[childIndex].f)
                childIndex++;

            if (tmp.f > heap[childIndex].f)
                heap[k] = heap[childIndex];
            else
                break;
        }
        heap[k] = tmp;

        return root;
    }

    private void doubleSize(){
        Node[] old = heap;
        heap = new Node[old.length * 2];
        System.arraycopy(old, 1, heap, 1, size);
    }

    public Node getNode(int index){
        return heap[index + 1];
    }
    public int getSize(){
        return size;
    }

    public boolean hasNode(Node n){
        //System.out.print("  " + size + "  ");
        for(int i = 1; i < size; i++){
            Node n1 = heap[i];
            if(n.equals(n1)){
                return true;
            }
        }

        return false;
    }

    public void setToNodeWithSameXY(Node n){
        for(int i = 1; i < size; i++){
            Node n1 = heap[i];
            if(n.equals(n1)){
                n = n1;
                return;
            }
        }
    }

    public void clear(){
        size = 0;
        heap = new Node[2];
    }

    public String toString(){
        String result = "";
        for(int i = 1; i <= size; i++)
            result += heap[i].f + " ";

        return result;
    }
}
