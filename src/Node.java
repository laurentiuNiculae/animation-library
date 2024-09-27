public class Node {
    int Id;
    int Value;

    public Node(int id, int value) {
        Id = id;
        Value = value;
    }

    public String toString() {
        return String.format("Node {Id: %d, Value: %d}", Id, Value);
    } 
}
