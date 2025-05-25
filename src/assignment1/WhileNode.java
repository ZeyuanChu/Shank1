package assignment1;

import java.util.HashMap;

public class WhileNode extends Node {
    private Node condition;   // Condition to evaluate for the while loop
    private BlockNode block;  // Block of code to execute while the condition is true
    private Node body;        // Optional body for the while loop
    
    // Constructor for WhileNode
    public WhileNode(Node condition, Node body) {
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
    
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) throws Exception {
        while (condition.evaluate(interpreter).getValue().equals("1")) {
            ReturnType returnType = interpreter.interpretListOfStatements(block.getStatements(), new HashMap<String, InterpreterDataType>());
            if (returnType.getResultType() == ReturnType.ResultType.BREAK) {
                break;
            } else if (returnType.getResultType() == ReturnType.ResultType.RETURN) {
                return new InterpreterDataType(returnType.getValue());
            }
        }
        return new InterpreterDataType(); //return Null
    }
}
