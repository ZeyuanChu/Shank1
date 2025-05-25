package assignment1;

public class ForEachNode extends StatementNode {
    private String variable;  // Name of the loop variable
    private String array;     // Name of the array to iterate over
    private BlockNode block;  // Block of code to execute in the loop

    // Constructor for ForEachNode
    public ForEachNode(String variable, String array, BlockNode block) {
        this.variable = variable;
        this.array = array;
        this.block = block;
    }

    // Default constructor for ForEachNode
    public ForEachNode() {
        this.variable = "";
        this.array = "";
        this.block = null;
    }

    // Getter method for retrieving the loop variable name
    public String getVariable() {
        return variable;
    }

    // Setter method for setting the loop variable name
    public void setVariable(String variable) {
        this.variable = variable;
    }

    // Getter method for retrieving the array name
    public String getArray() {
        return array;
    }

    // Setter method for setting the array name
    public void setArray(String array) {
        this.array = array;
    }

    // Getter method for retrieving the block of code
    public BlockNode getBlock() {
        return block;
    }

    // Setter method for setting the block of code
    public void setBlock(BlockNode block) {
        this.block = block;
    }
}

