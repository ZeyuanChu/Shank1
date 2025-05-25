package assignment1;

import java.util.HashMap;

public class DoWhileNode extends StatementNode {
    private Node condition;
    private BlockNode block;
    private Node body;

    // Constructor for DoWhileNode
    public DoWhileNode(Node body, Node condition) {
        this.body = body;
        this.condition = condition;
    }

    // Getter method for retrieving the condition node
    public Node getCondition() {
        return condition;
    }

    // Setter method for setting the condition node
    public void setCondition(Node condition) {
        this.condition = condition;
    }

    // Getter method for retrieving the block node
    public BlockNode getBlock() {
        return block;
    }

    // Setter method for setting the block node
    public void setBlock(BlockNode block) {
        this.block = block;
    }
    
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) throws Exception {
        ReturnType returnType;
        boolean continueLoop;

        do {
            //  HashMap
            returnType = interpreter.interpretListOfStatements(block.getStatements(), new HashMap<String, InterpreterDataType>());
            if (returnType.getResultType() == ReturnType.ResultType.BREAK) {
                break;
            } else if (returnType.getResultType() == ReturnType.ResultType.RETURN) {
                return new InterpreterDataType(returnType.getValue());
            }

            InterpreterDataType conditionValue = condition.evaluate(interpreter);
            continueLoop = conditionValue.getValue().equals("1");
        } while (continueLoop);

        return new InterpreterDataType();  // Return Null
    }
}


