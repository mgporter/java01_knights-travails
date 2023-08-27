public class Node {
    public int row;
    public int column;
    public Node parent = null;

    public Node(int newRow, int newColumn) {
        row = newRow;
        column = newColumn;
    }

    public String toString() {
        return "row: " + row + ", column: " + column;
    }
}