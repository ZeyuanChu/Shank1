package assignment1;

import java.util.Optional;

class OperationNode extends Node {
    public enum OperationType {
        // List of supported operation types
        EQ, NE, LT, LE, GT, GE, AND, OR, NOT, MATCH, NOTMATCH, DOLLAR,
        PREINC, POSTINC, PREDEC, POSTDEC, UNARYPOS, UNARYNEG, IN,
        EXPONENT, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, CONCATENATION, ADD_ASSIGN, STRINGLITERAL,
    }

    private Node left; // Left operand
    private Optional<Node> right; // Right operand (optional)
    private OperationType operationType; // Type of operation

    // Constructor for unary operations
    public OperationNode(Node left, OperationType operationType) {
        this.left = left;
        this.right = Optional.empty(); // No right operand
        this.operationType = operationType;
    }

    // Constructor for binary operations
    public OperationNode(Node left, Node right, OperationType operationType) {
        this.left = left;
        this.right = Optional.of(right); // Set right operand
        this.operationType = operationType;
    }

    // Getter for the left operand
    public Node getLeft() {
        return left;
    }

    // Getter for the optional right operand
    public Optional<Node> getRight() {
        return right;
    }

    // Getter for the operation type
    public OperationType getOperationType() {
        return operationType;
    }

    // Setter for the left operand
    public void setLeft(Node left) {
        this.left = left;
    }

    // Setter for the right operand
    public void setRight(Node right) {
        this.right = Optional.ofNullable(right);
    }

    // Setter for the operation type
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) throws Exception {
        InterpreterDataType leftValue = left.evaluate(interpreter);

        if (operationType == OperationType.DOLLAR) {
            // Evaluate the expression on the left to get the index
            String indexStr = leftValue.getValue();

            int index;
            try {
                index = Integer.parseInt(indexStr);
            } catch (NumberFormatException e) {
                throw new Exception("Invalid index for variable access: " + indexStr);
            }

            // Fetch the variable by index from the interpreter's environment
            InterpreterDataType variable = interpreter.getVariableValue(index);
            if (variable == null) {
                throw new Exception("Variable at index " + index + " is not defined.");
            }

            // Return the value of the variable
            return variable;
        } else if (!right.isPresent()) {
            // Process operations that do not require a right operand
            switch (operationType) {
                // List of unary operations
                case NOT:
                    boolean leftBool = convertToBoolean(leftValue.getValue());
                    return new InterpreterDataType(leftBool ? "0" : "1");
                case PREINC:
                    float preIncValue = convertToFloat(leftValue.getValue()) + 1;
                    leftValue.setValue(String.valueOf(preIncValue));
                    interpreter.setVariable(((VariableReferenceNode) left).getVariableName(), leftValue);
                    return leftValue;
                case POSTINC:
                    float originalValueInc = convertToFloat(leftValue.getValue());
                    float postIncValue = originalValueInc + 1;
                    InterpreterDataType postIncDataType = new InterpreterDataType(String.valueOf(originalValueInc));
                    interpreter.setVariable(((VariableReferenceNode) left).getVariableName(), new InterpreterDataType(String.valueOf(postIncValue)));
                    return postIncDataType;
                case PREDEC:
                    float preDecValue = convertToFloat(leftValue.getValue()) - 1;
                    leftValue.setValue(String.valueOf(preDecValue));
                    interpreter.setVariable(((VariableReferenceNode) left).getVariableName(), leftValue);
                    return leftValue;
                case POSTDEC:
                    float originalValueDec = convertToFloat(leftValue.getValue());
                    float postDecValue = originalValueDec - 1;
                    InterpreterDataType postDecDataType = new InterpreterDataType(String.valueOf(originalValueDec));
                    interpreter.setVariable(((VariableReferenceNode) left).getVariableName(), new InterpreterDataType(String.valueOf(postDecValue)));
                    return postDecDataType;
                default:
                    throw new UnsupportedOperationException("Unary operation " + operationType + " not supported.");
            }
        } else {
            // For binary operations, the right operand is evaluated
            InterpreterDataType rightValue = right.get().evaluate(interpreter);

            // Process operations that require a right operand
            switch (operationType) {
                case EQ:
                    return new InterpreterDataType(
                            leftValue.getValue().equals(rightValue.getValue()) ? "1" : "0"
                    );
                case NE:
                    return new InterpreterDataType(
                            !leftValue.getValue().equals(rightValue.getValue()) ? "1" : "0"
                    );
                case LT:
                    return new InterpreterDataType(
                            convertToFloat(leftValue.getValue()) < convertToFloat(rightValue.getValue()) ? "1" : "0"
                    );
                case GT:
                    return new InterpreterDataType(
                            convertToFloat(leftValue.getValue()) > convertToFloat(rightValue.getValue()) ? "1" : "0"
                    );
                case ADD:
                    float resultAdd = convertToFloat(leftValue.getValue()) + convertToFloat(rightValue.getValue());
                    return new InterpreterDataType(String.valueOf(resultAdd));
                case SUBTRACT:
                    float resultSubtract = convertToFloat(leftValue.getValue()) - convertToFloat(rightValue.getValue());
                    return new InterpreterDataType(String.valueOf(resultSubtract));
                case MULTIPLY:
                    float resultMultiply = convertToFloat(leftValue.getValue()) * convertToFloat(rightValue.getValue());
                    return new InterpreterDataType(String.valueOf(resultMultiply));
                case DIVIDE:
                    float resultDivide = convertToFloat(leftValue.getValue()) / convertToFloat(rightValue.getValue());
                    return new InterpreterDataType(String.valueOf(resultDivide));
                case MODULO:
                    float resultModulo = convertToFloat(leftValue.getValue()) % convertToFloat(rightValue.getValue());
                    return new InterpreterDataType(String.valueOf(resultModulo));
                case EXPONENT:
                    double resultExponent = Math.pow(convertToFloat(leftValue.getValue()), convertToFloat(rightValue.getValue()));
                    return new InterpreterDataType(String.valueOf((float) resultExponent));
                case CONCATENATION:
                    String resultConcatenation = leftValue.getValue().concat(rightValue.getValue());
                    return new InterpreterDataType(resultConcatenation);
                default:
                    throw new UnsupportedOperationException("Binary operation " + operationType + " not supported.");
            }
        }
    }

    // Helper method to convert a value to a boolean
    private boolean convertToBoolean(String value) {
        return !value.equals("") && !value.equals("0") && convertToFloat(value) != 0.0f;
    }

    // Helper method to convert a value to a float
    private float convertToFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Value " + value + " cannot be converted to float.");
        }
    }

    @Override
    public String toString() {
        if (right.isPresent()) {
            return "OperationNode(Left: " + left.toString() + ", Operation: " + operationType + ", Right: " + right.get().toString() + ")";
        } else {
            return "OperationNode(Operand: " + left.toString() + ", Operation: " + operationType + ")";
        }
    }
}




