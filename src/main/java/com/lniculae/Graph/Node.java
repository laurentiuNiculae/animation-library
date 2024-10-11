package com.lniculae.Graph;
public class Node {
    public int Id;
    public int Value;

    public Node(int id, int value) {
        Id = id;
        Value = value;
    }

    public String toString() {
        return String.format("Node {Id: %d, Value: %d}", Id, Value);
    } 
}
