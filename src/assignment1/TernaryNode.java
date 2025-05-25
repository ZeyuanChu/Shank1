package assignment1;

class TernaryNode extends Node {
    private Node condition;         // The condition expression
    private Node trueExpression;    // The expression to evaluate if the condition is true
    private Node falseExpression;   // The expression to evaluate if the condition is false

    // Constructor
    public TernaryNode(Node condition, Node trueExpression, Node falseExpression) {
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    // Getter for the condition
    public Node getCondition() {
        return condition;
    }

    // Getter for the true expression
    public Node getTrueExpression() {
        return trueExpression;
    }

    // Getter for the false expression
    public Node getFalseExpression() {
        return falseExpression;
    }

    // Setter for the condition
    public void setCondition(Node condition) {
        this.condition = condition;
    }

    // Setter for the true expression
    public void setTrueExpression(Node trueExpression) {
        this.trueExpression = trueExpression;
    }

    // Setter for the false expression
    public void setFalseExpression(Node falseExpression) {
        this.falseExpression = falseExpression;
    }

    @Override
    public String toString() {
        return "TernaryNode(Condition: " + condition.toString() + ", True: " + trueExpression.toString() + ", False: " + falseExpression.toString() + ")";
    }
    
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) throws Exception {
        // Evaluate the condition
        InterpreterDataType conditionResult = condition.evaluate(interpreter);
        
        boolean result = Boolean.parseBoolean(conditionResult.getValue());

        // Evaluate and return the appropriate branch based on the condition's result
        if (result) {
            // If condition is true, evaluate the true expression
            return trueExpression.evaluate(interpreter);
        } else {
            // If condition is false, evaluate the false expression
            return falseExpression.evaluate(interpreter);
        }
    }

}


