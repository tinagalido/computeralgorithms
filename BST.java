import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Compile using: javac BST.java
 * Run using: java BST input_file output_file
 * The input file contains a set of commands of the following form:
 * add <key> <value> 
 * -or- 
 * delete <key>
 * Examples: 
 * add Ethan 1 (adding/inserting)
 * delete Ethan (deleting/removing)
 * 
 * The output file should contain the results of the following form:
 * (<key>, <value>) is the left child of (<parent.key>, <parent.value>)
 * -or-
 * Not found: <key>
 * -or-
 * (<key>,<value>) is the root
 * Examples:
 * Not found: Tom (if key is not found)
 * (Bob, 2) is the left child of (Gabriel, 3)
 * (Gabriel, 3) is the root
 */


public class BST {

    private static final String ADD_STRING = "add";
    private static final String DELETE_STRING = "delete";
    private Node root;

    static class Node
    {
        String key;
        int  value;
        Node left, right, parent;

        public Node (String key, int value)
        {
            this.key = key;
            this.value = value;
            left = right = parent = null;
        }
    }

    public static void main(String[] args) 
    {

        if (args == null || args.length == 0 || args.length < 2)
        {
            System.out.println("Usage: BST input_file output_file");
            System.exit(1);
        } else 
        if (args[0].equals("-h") || args[0].equals("--help")){
            System.out.println("Usage: BST input_file output_file");
            System.exit(1);
        } else
        if (args[1].equals("") || args[1].equals("")){
            System.out.println("Usage: BST input_file output_file");
            System.exit(1);
        }

        BST bst = new BST();
        String inputFileString = args[0];
        String outputFileString = args[1];
        try {
            BufferedReader readFile = new BufferedReader(new FileReader(inputFileString));
            String inputFileLine;
            BufferedWriter writeFile = new BufferedWriter(new FileWriter(outputFileString));

            while ((inputFileLine = readFile.readLine()) != null)
            {
                String[] inputLineParts = inputFileLine.split(" "); // space-delimited
                String commandPart = inputLineParts[0];
                String keyPart = inputLineParts[1];
                int valuePart;

                //Call main command functions
                if (commandPart.equalsIgnoreCase(ADD_STRING))
                {
                    valuePart = Integer.parseInt(inputLineParts[2]);
                    bst.add(keyPart, valuePart);
                } else if (commandPart.equalsIgnoreCase(DELETE_STRING))
                {
                    bst.delete(keyPart, writeFile);
                }

            }
            readFile.close();
            
            bst.printBySuccessor(bst.root, writeFile);
            writeFile.close();

            System.out.println("Processed successfully.");

        } catch (IOException ioException) {
            System.err.println("Error in reading or writing into the file:" 
                + ioException.toString());
        } catch (Exception exception)
        {
            System.err.println(exception.toString());
            
        }
        

    }

    /* Main handle for adding/inserting key-value pairs  
     * 
     * @param key - key of the new key-value pair
     * @param value - value of the new key-value pair
     * @throws Exception - For exception handling like null, etc.
     * @return - none
    */
    private void add(String key, int value) throws Exception
    {
        root = addNode(root, key, value,null); // Parent is null initially 
    }

    /* Processes adding/inserting key-value pairs into the binary search tree 
     * 
     * @param node - node to compare
     * @param key - key of the current node that will be compared
     * @param value - value of the current node that will be compared
     * @param parent - parent node
     * @throws Exception - For exception handling like null, etc.
     * @return - Node
    */
    private Node addNode(Node node, String key, int value, Node parent) throws Exception
    {
        if (node == null)
        {
            Node newNode = new Node(key, value);
            newNode.parent = parent;
            return newNode;
        }
        if (key.compareTo(node.key) < 0)
        {
            node.left = addNode(node.left, key, value, node);
        } else if (key.compareTo(node.key) > 0)
        {
            node.right = addNode(node.right, key, value, node);
        }
        return node;
    }

    /* Main handle for deleting/removing key  
     * 
     * @param key - key of the new key-value pair to be deleted
     * @param writer - the output writer (for the non-existent keys)
     * @throws IOException - For exception handling on the writer
     * @return - none
    */
    private void delete(String key, BufferedWriter writer) throws IOException
    {
        root = deleteNode(root, key, writer);
        if (root == null)
        {
            writer.write("Not found: " + key);
        }
        
    }

    /* Processes deleting/removing key from the binary search tree  
     * 
     * @param node - node of the key in the BST
     * @param key - key to be deleted
     * @param writer - the output writer (for the non-existent keys)
     * @throws IOException - For exception handling on the writer
     * @return - node
    */
    private Node deleteNode(Node node, String key, BufferedWriter writer) throws IOException
    {
        // If key not found on BST
        if (node == null)
        {
            writer.write("Not found:" + key);
            writer.newLine();
            return null;
        }

        // If key is found on the BST
        if (key.compareTo(node.key) < 0)
        {
            node.left = deleteNode(node.left, key, writer);
            if (node.left != null)
            {
                node.left.parent = node; //Update parent of the left child
            }
        } else if (key.compareTo(node.key) > 0)
        {
            node.right = deleteNode(node.right, key, writer);
            if (node.right != null)
            {
                node.right.parent = node; //Update parent of the right child
            }
        } else 
        {
            // Node to delete found
            if (node.left == null)
            {
                return node.right;
            } else if (node.right == null)
            {
                return node.left;
            }

            // Node - 2 children
            Node successorNode = min(node.right);
            node.key = successorNode.key;
            node.value = successorNode.value;
            // Remove the successor node
            node.right = deleteNode(node.right, successorNode.key, writer);
            if (node.right != null)
            {
                node.right.parent = node; // Update parent of right child
            }
        }
        return node;
    }

    /* Retrieves the minimum key from the BST  
     * 
     * @param node - current node to be evaluated
     * @return - node
    */
    private Node min(Node node)
    {
        while (node.left != null)
        {
            node = node.left;
        }
        return node;
    }

    /* Processes the writing into the output writer  
     * 
     * @param node - node to be evaluated and written into the writer
     * @param writer - the output writer 
     * @throws IOException - For exception handling on the writer
     * @return - none
    */
    private void printBySuccessor(Node node, BufferedWriter writer) throws IOException
    {
        if (node == null)
        {
            return;
        }
        printBySuccessor(node.left, writer);
    
        writer.write("(" + node.key + ", " + node.value + ")");

        Node parentNode = getParent(node);
        if (parentNode != null) {
            writer.write(" is the ");
            if (parentNode.left == node) {
                writer.write("left child of ");
            } else {
                writer.write("right child of ");
            }
            writer.write("(" + parentNode.key + ", " + parentNode.value + ")");
        } else {
            writer.write(" is the root");
        }
        writer.newLine();

        printBySuccessor(node.right, writer);
            

    } // end of function

    /* Retrieves the parent of the node  
     * 
     * @param node - node to be evaluated
     * @return - node
    */
    private Node getParent(Node node) {
        if (node == null || node.parent == null) {
            return null;
        }
        return node.parent;
    }
}
