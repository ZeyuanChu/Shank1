package assignment1;

import assignment1.OperationNode.OperationType;

public class AssignmentNode extends Node {
    private Node target; // The left-hand side of the assignment
    private Node expression; // The right-hand side expression

    // Constructor to initialize the AssignmentNode
    public AssignmentNode(Node target, Node expression) {
        this.target = target;
        this.expression = expression;
    }

    // Getter for the target
    public Node getTarget() {
        return target;
    }

    // Getter for the expression
    public Node getExpression() {
        return expression;
    }

    // Setter for the target
    public void setTarget(Node target) {
        this.target = target;
    }

    // Setter for the expression
    public void setExpression(Node expression) {
        this.expression = expression;
    }

    // Method to evaluate the assignment operation
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) throws Exception {
        // Evaluate the right-hand side expression
        InterpreterDataType rightValue = expression.evaluate(interpreter);

        if (target instanceof VariableReferenceNode) {
            // If the target is a VariableReferenceNode, assign the value to the corresponding variable.
            VariableReferenceNode varRef = (VariableReferenceNode) target;
            interpreter.setVariable(varRef.getVariableName(), rightValue);
        } else if (target instanceof OperationNode) {
            // If the target is an OperationNode, check the operation type.
            OperationNode operation = (OperationNode) target;
            if (operation.getOperationType() == OperationType.DOLLAR) {
                // If it's a dollar operation, evaluate the left side to get an index.
                InterpreterDataType indexValue = operation.getLeft().evaluate(interpreter);
                int index = Integer.parseInt(indexValue.getValue());
                String varName = "$" + index;
                // Set the variable with the computed name to the right-hand side value.
                interpreter.setVariable(varName, rightValue);
            } else {
                // Invalid operation type for assignment.
                throw new Exception("Invalid target for assignment.");
            }
        } else {
            // Invalid target type for assignment.
            throw new Exception("The target of assignment must be a variable.");
        }

        return rightValue; // Return the value that was assigned.
    }

    // Method to convert AssignmentNode to a string representation
    @Override
    public String toString() {
        return "AssignmentNode(Target: " + target.toString() + ", Expression: " + expression.toString() + ")";
    }
}
