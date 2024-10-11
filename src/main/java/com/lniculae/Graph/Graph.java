package com.lniculae.Graph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;


public class Graph {
    private Map<Integer,Node> nodeValues;
    private Map<Integer,List<Edge>> edges;

    public Graph() {
        nodeValues = new HashMap<>();
        edges = new HashMap<>();
    }

    public void setNodes(List<Node> newNodes) {
        newNodes.forEach(node -> {
            nodeValues.putIfAbsent(node.Id, node);
            edges.putIfAbsent(node.Id, new ArrayList<>());
        });
    }

    public void setNode(int id, int value) {
        nodeValues.putIfAbsent(id, new Node(id, value));
        edges.putIfAbsent(id, new ArrayList<>());
    }

    public void setEdge(int id1, int id2) {
        var nodeEdges = edges.get(id1);
        if (nodeEdges == null) {
            nodeEdges = new ArrayList<>();
        }

        nodeEdges.add(new Edge(nodeValues.get(id1), nodeValues.get(id2)));
        edges.put(id1, nodeEdges);
    }
    
    public void setEdge(int id1, int id2, float cost) {
        var nodeEdges = edges.get(id1);
        if (nodeEdges == null) {
            nodeEdges = new ArrayList<>();
        }

        nodeEdges.add(new Edge(nodeValues.get(id1), nodeValues.get(id2), cost));
        edges.put(id1, nodeEdges);
    }

    public void PrintNodes() {
        nodeValues.forEach((nodeId, nodeValue) -> {
            System.out.printf("node: %d Value: %d", nodeId, nodeValue.Value);
            System.out.println("");
        });
    }

    public void PrintEdges() {
        edges.forEach((nodeId, list) -> {
            var listStr = list.stream().map(t -> Integer.toString(t.To.Id)).collect(Collectors.joining(", "));
            System.out.printf("node: %d, list: %s", nodeId, listStr);
            System.out.println("");
        });
    }

    public int nodeCount() {
        return nodeValues.size();
    }

    public List<Node> Nodes() {
        return nodeValues.entrySet().stream().map(t -> t.getValue()).collect(toList());
    }

    public EdgeIterator Edges() {
        return new EdgeIterator(edges);
    }
}

