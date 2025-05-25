package assignment1;

import java.util.Optional;

public class VariableReferenceNode extends Node {
    private String variableName;
    private Optional<Node> indexExpression;

    // Constructor for a simple variable reference without an index
    public VariableReferenceNode(String variableName) {
        this.variableName = variableName;
        this.indexExpression = Optional.empty();
    }

    // Constructor for a variable reference with an index
    public VariableReferenceNode(String variableName, Node indexExpression) {
        this.variableName = variableName;
        this.indexExpression = Optional.of(indexExpression);
    }

    // Getter method to retrieve the variable name
    public String getVariableName() {
        return variableName;
    }

    // Getter method to retrieve the optional index expression
    public Optional<Node> getIndexExpression() {
        return indexExpression;
    }
    
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) throws Exception {
        if (!indexExpression.isPresent()) {
            // It's a simple variable reference
            // Get the variable from the interpreter's global variables
            InterpreterDataType varValue = interpreter.getGlobalVariables().get(variableName);
            if (varValue == null) {
                throw new Exception("Variable '" + variableName + "' is not defined.");
            }
            return varValue;
        } else {
            // It's an array reference, so first resolve the index expression to get the key
            InterpreterDataType indexValue = indexExpression.get().evaluate(interpreter);

            // Now, get the array from the interpreter's global variables
            InterpreterDataType varValue = interpreter.getGlobalVariables().get(variableName);
            if (!(varValue instanceof InterpreterArrayDataType)) {
                throw new Exception("Variable '" + variableName + "' is not an array.");
            }

            InterpreterArrayDataType arrayVar = (InterpreterArrayDataType) varValue;
            // Use the index value to get the actual value from the array
            InterpreterDataType elementValue = arrayVar.get(indexValue.getValue());
            if (elementValue == null) {
                throw new Exception("Index '" + indexValue.getValue() + "' is not defined in array '" + variableName + "'.");
            }
            return elementValue;
        }
    }
    
    public void setValue(Interpreter interpreter, InterpreterDataType newValue) throws Exception {
        if (!indexExpression.isPresent()) {
            // It's a simple variable assignment
            interpreter.getGlobalVariables().put(variableName, newValue);
        } else {
            // It's an array assignment, so first resolve the index expression to get the key
            InterpreterDataType indexValue = indexExpression.get().evaluate(interpreter);

            // Check if the variable is an array in the global variables
            InterpreterDataType varValue = interpreter.getGlobalVariables().get(variableName);
            if (!(varValue instanceof InterpreterArrayDataType)) {
                throw new Exception("Variable '" + variableName + "' is not an array.");
            }

            // Cast to the appropriate array type
            InterpreterArrayDataType arrayVar = (InterpreterArrayDataType) varValue;
            // Update the specific element in the array
            arrayVar.set(indexValue.getValue(), newValue);
        }
    }
}
