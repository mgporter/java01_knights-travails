import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class KnightsTravails {

    private Node start;
    private Node end;
    private int boardRows = 8;
    private int boardColumns = 8;
    private LinkedList<Node> queue = new LinkedList<Node>();

    private int[][] movements = {
        {1, 2},
        {2, 1},
        {2, -1},
        {1, -2},
        {-1, -2},
        {-2, -1},
        {-2, 1},
        {-1, 2},
    };

    private HashMap<Integer, ArrayList<Integer>> visitedLocs = new HashMap<>();

    public KnightsTravails(int startRow, int startCol) {
        
        if (isOutOfBounds(startRow, startCol) == "row") {
            throw new Error("Starting row must be between 0 and " + (boardRows - 1) + ". You entered " + startRow + ".");
        } else if (isOutOfBounds(startRow, startCol) == "col") {
            throw new Error("Starting column must be between 0 and " + (boardColumns - 1) + ". You entered " + startCol + ".");
        }

        start = new Node(startRow, startCol);

        // Create an empty ArrayList for each row of the board and add them to the hashmap
        // We will later record each node we visited here, so that we don't visit it multiple times
        for (int i = 0; i < boardRows; i++) {
            ArrayList<Integer> newRow = new ArrayList<Integer>();
            visitedLocs.put(i, newRow);
        }

        // Add the first node as a visited location
        visitedLocs.get(startRow).add(startCol);
    }

    public static void main(String[] args) {

        // Syntax:
        // KnightsTravails startingRow startingColumn endingRow endingColumn
        // Example: KnightsTravails 2 3 7 5
        // Output: "3 moves: Start (2, 3) --> (3, 5) --> (5, 6) --> End (7, 5)"
        // Note that positions are 0-indexed, so numbers can only be from 0-7 (or from 0 to (boardRows - 1)) for rows.

        int startRow = Integer.parseInt(args[0]);
        int startCol = Integer.parseInt(args[1]);
        int endRow = Integer.parseInt(args[2]);
        int endCol = Integer.parseInt(args[3]);

        KnightsTravails kt = new KnightsTravails(startRow, startCol);

        String result = kt.findShortestDistanceTo(endRow, endCol);

        System.out.println(result);

    }

    public String findShortestDistanceTo(int endRow, int endCol) {

        if (isOutOfBounds(endRow, endCol) == "row") {
            throw new Error("Ending row must be between 0 and " + (boardRows - 1) + ". You entered " + endRow + ".");
        } else if (isOutOfBounds(endRow, endCol) == "col") {
            throw new Error("Ending column must be between 0 and " + (boardColumns - 1) + ". You entered " + endCol + ".");
        }

        String outputString = "";
        int moveCounter = 1;

        end = new Node(endRow, endCol);

        Node finalNode = traverseNodes(start);

        if (finalNode != null) {
            // construct the output string
            if (finalNode.parent == null) {
                return "0 moves: Knight doesn't move";
            }

            outputString = "End (" + finalNode.row + ", " + finalNode.column + ")";

            Node tempNode = finalNode.parent;

            while (tempNode.parent != null) {
                outputString = "(" + tempNode.row + ", " + tempNode.column + ") --> " + outputString;
                tempNode = tempNode.parent;
                moveCounter++;
            }

            String noun = moveCounter == 1 ? "move" : "moves";
            outputString = moveCounter + " " + noun + ": Start (" + tempNode.row + ", " + tempNode.column + ") --> " + outputString;
            return outputString;
        } else {
            // We should never get here, but at least the program will output something just in case
            return "No path found";
        }
    }

    private String isOutOfBounds(int row, int col) {
        if (row >= boardRows || row < 0) {
            return "row";
        } else if (col >= boardColumns || col < 0) {
            return "col";
        } else {
            return "none";
        }
    }

    private Node traverseNodes(Node root) {
        
        queue.add(root);

        while (queue.size() != 0) {
            // Get the first element in the queue, and remove it at the same time
            Node current = queue.poll();

            // Base case. Return if the current node matches the end node.
            if (current.row == end.row && current.column == end.column) {
                return current;
            }

            // Push all of the node's valid children into the queue.
            ArrayList<Node> children = findChildNodes(current);
            if (children == null) continue;
            for (Node n : children) {
                queue.add(n);
            }
        }

        return null;
    }

    private ArrayList<Node> findChildNodes(Node beginningNode) {
        ArrayList<Node> children = new ArrayList<Node>();

        // Loop through the directions
        for (int[] mov : movements) {
            int newRow = beginningNode.row + mov[0];
            int newColumn = beginningNode.column + mov[1];

            // Continue the loop if we end up out of bounds
            String bounds = isOutOfBounds(newRow, newColumn);
            if (bounds == "row" || bounds == "col") continue;

            // Continue the loop if the node has been visited before, else add it to the ArrayList
            ArrayList<Integer> currentRow = visitedLocs.get(newRow);
            if (currentRow.contains(newColumn)) continue;
            currentRow.add(newColumn);

            // Finally, set the new node's parent to the beginningNode
            Node newNode = new Node(newRow, newColumn);
            newNode.parent = beginningNode;
            // System.out.println(newNode.toString());

            // Add the node to the list if it doesn't yet lead to the ending node
            children.add(newNode);
        }

        return children;
    }
}