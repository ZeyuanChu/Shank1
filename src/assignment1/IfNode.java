package assignment1;

import java.util.HashMap;

public class IfNode extends Node {
    private Node condition;   // Condition to evaluate for the if statement
    private BlockNode block;  // Block of code to execute if the condition is true
    private IfNode next;      // Next if statement (for "else if" or "else") if present
    private Node body;        // Optional else block (for "else" statement) if present
    
    // Constructor for IfNode
    public IfNode(Node condition, Node body) {
        this.condition = condition;
        this.body = body;
    }
    
    // Getter method for retrieving the condition node
    public Node getCondition() {
        return condition;
    }

    // Setter method for setting the condition node
    public void setCondition(Node condition) {
        this.condition = condition;
    }

    // Getter method for retrieving the block of code
    public BlockNode getBlock() {
        return block;
    }

    // Setter method for setting the block of code
    public void setBlock(BlockNode block) {
        this.block = block;
    }

    // Getter method for retrieving the next if statement (for "else if" or "else")
    public IfNode getNext() {
        return next;
    }

    // Setter method for setting the next if statement
    public void setNext(IfNode next) {
        this.next = next;
    }
    // Start with the current IfNode
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) throws Exception {
        IfNode current = this;
        while (current != null) {
            if (current.getCondition() == null || current.getCondition().evaluate(interpreter).getValue().equals("1")) {
                ReturnType returnType = interpreter.interpretListOfStatements(current.getBlock().getStatements(), new HashMap<String, InterpreterDataType>());
                if (returnType.getResultType() != ReturnType.ResultType.NORMAL) {
                    return new InterpreterDataType(returnType.getValue());
                }
                break;
            }
            current = current.getNext();
        }
        return new InterpreterDataType(); // Return Null
    }
}
