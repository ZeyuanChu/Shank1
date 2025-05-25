package assignment1;

import java.util.HashMap;

public class ForNode extends StatementNode {
    private Node initialization;   // Initialization part of the for loop
    private Node condition;        // Condition to check for loop continuation
    private Node postOperation;    // Post-operation to execute after each iteration
    private BlockNode block;       // Block of code to execute in the for loop

    // Constructor for ForNode
    public ForNode(Node initialization, Node condition, Node postOperation, BlockNode block) {
        this.initialization = initialization;
        this.condition = condition;
        this.postOperation = postOperation;
        this.block = block;
    }

    // Default constructor for ForNode
    public ForNode() {
        this.initialization = null;
        this.condition = null;
        this.postOperation = null;
        this.block = null;
    }

    // Getter method for retrieving the initialization node
    public Node getInitialization() {
        return initialization;
    }

    // Setter method for setting the initialization node
    public void setInitialization(Node initialization) {
        this.initialization = initialization;
    }

    // Getter method for retrieving the loop condition node
    public Node getCondition() {
        return condition;
    }

    // Setter method for setting the loop condition node
    public void setCondition(Node condition) {
        this.condition = condition;
    }

    // Getter method for retrieving the post-operation node
    public Node getPostOperation() {
        return postOperation;
    }

    // Setter method for setting the post-operation node
    public void setPostOperation(Node postOperation) {
        this.postOperation = postOperation;
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
        // Execute initialization code if it is provided
        if (initialization != null) {
            initialization.evaluate(interpreter);
        }

        // Continue looping while the condition is null or evaluates to "1"
        while (condition == null || condition.evaluate(interpreter).getValue().equals("1")) {
            // Interpret a list of statements within the block
            ReturnType returnType = interpreter.interpretListOfStatements(block.getStatements(), new HashMap<String, InterpreterDataType>());

            // Check the result type of the interpreted statements
            if (returnType.getResultType() == ReturnType.ResultType.BREAK) {
                // If the result type is BREAK, exit the loop
                break;
            } else if (returnType.getResultType() == ReturnType.ResultType.RETURN) {
                // If the result type is RETURN, return the value and exit the loop
                return new InterpreterDataType(returnType.getValue());
            }

            // Execute post-operation code if it is provided
            if (postOperation != null) {
                postOperation.evaluate(interpreter);
            }
        }

        // Return a default value (null) when the loop exits
        return new InterpreterDataType(); //return null
    }

}

