package assignment1;

public class PatternNode extends Node {
    private String pattern;

    // Constructor to initialize the PatternNode with a pattern
    public PatternNode(String pattern) {
        this.pattern = pattern;
    }

    // Getter method to retrieve the pattern
    public String getPattern() {
        return pattern;
    }

    // Override the toString method to provide a string representation of the PatternNode
    @Override
    public String toString() {
        return "Pattern(" + pattern + ")";
    }
    
 // Override the evaluate method to throw an exception
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) throws Exception {
        throw new UnsupportedOperationException("Patterns cannot be passed to functions or used in assignments.");
    }
}


