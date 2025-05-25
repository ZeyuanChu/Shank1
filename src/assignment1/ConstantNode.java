package assignment1;

public class ConstantNode extends Node {
    private String value; // The value stored in the ConstantNode

    // Constructor to initialize the ConstantNode with a value
    public ConstantNode(String value) {
        this.value = value;
    }

    // Getter for retrieving the value
    public String getValue() {
        return value;
    }

    // Method to evaluate the ConstantNode
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) {
        // Create a new InterpreterDataType with the constant value and return it.
        return new InterpreterDataType(value);
    }

    // Method to convert ConstantNode to a string representation
    @Override
    public String toString() {
        return "Constant(" + value + ")";
    }
}
