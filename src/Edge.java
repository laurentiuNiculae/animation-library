public class Edge {
    Node To;
    Node From;
    float Cost;

    public Edge(Node from, Node to) {
        To = to;
        From = from;
        Cost = 1;
    }

    public Edge(Node from, Node to, float cost) {
        To = to;
        From = from;
        Cost = cost;
    }

    public String toString() {
        return String.format("Edge {From: %d, To: %d, Cost: %f}", From.Id, To.Id, Cost);
    } 
}
