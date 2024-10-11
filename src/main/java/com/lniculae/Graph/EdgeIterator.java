package com.lniculae.Graph;
import java.util.Iterator; 
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.List; 
import java.util.ArrayList; 

public class EdgeIterator implements Iterable<Edge> {
    private Map<Integer,List<Edge>> edges;

    public EdgeIterator(Map<Integer,List<Edge>> edges) {
        this.edges = edges;
    }

    @Override
    public Iterator<Edge> iterator() {
        return new IteratorImpl(edges);
    }

    private class IteratorImpl implements Iterator<Edge> {
        private Map<Integer,List<Edge>> edges;
        private List<Integer> nodeList;
        private int currNodeIndex;
        private int currEdgeIndex;

        public IteratorImpl(Map<Integer,List<Edge>> edges) {
            this.edges = edges;
    
            this.nodeList = new ArrayList<>();
            edges.forEach((nodeId, x) -> this.nodeList.add(nodeId));
        }

        @Override
        public boolean hasNext() {
            if (currNodeIndex >= nodeList.size()) {
                return false;
            }
    
            var auxNodeIndex = currNodeIndex;
            var auxEdgeIndex = currEdgeIndex;
    
            while (auxEdgeIndex >= edges.get(nodeList.get(auxNodeIndex)).size()) {
                auxNodeIndex++;
                if (auxNodeIndex >= nodeList.size()) {
                    return false;
                }
    
                auxEdgeIndex = 0;
            }
    
            return true;
        }
    
        @Override
        public Edge next() {
            int nodeId = nodeList.get(currNodeIndex);
            var nodeEdges = edges.get(nodeId);
    
            while (currEdgeIndex >= nodeEdges.size()) {
                currNodeIndex++;
                if (nodeId >= nodeList.size()) {
                    throw new NoSuchElementException();
                }
    
                currEdgeIndex = 0;
                nodeId = nodeList.get(currNodeIndex);
                nodeEdges = edges.get(nodeId);
            }
    
            Edge edge = nodeEdges.get(currEdgeIndex);
            
            currEdgeIndex++;
    
            return edge;
        }
    }
}
