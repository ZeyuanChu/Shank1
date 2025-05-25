package assignment1;

import java.util.ArrayList;
import java.util.List;

public class DeleteNode extends StatementNode {
    private String arrayName;
    private List<Node> indices;

    // Constructor for DeleteNode
    public DeleteNode(String arrayName, List<Node> indices) {
        this.arrayName = arrayName;
        this.indices = indices;
    }

    // Getter method for retrieving the array name
    public String getArrayName() {
        return arrayName;
    }

    // Getter method for retrieving the list of indices
    public List<Node> getIndices() {
        return indices;
    }

    // Method to add an index to the indices list
    public void addIndex(Node index) {
        this.indices.add(index);
    }
    
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) throws Exception {
        InterpreterArrayDataType array = (InterpreterArrayDataType) interpreter.lookupVariable(arrayName);

        if (indices == null || indices.isEmpty()) {
            array.clear();  // Empty the entire array
        } else {
            for (Node indexNode : indices) {
                String index = indexNode.evaluate(interpreter).getValue();
                array.remove(index);  // Delete the specified index
            }
        }
        return new InterpreterDataType();  // return Null
    }
}

