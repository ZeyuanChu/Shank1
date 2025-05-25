package assignment1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

// FunctionDefinitionNode class, derived from Node
public class FunctionDefinitionNode extends Node {
    private String functionName;  // Function name
    private List<String> parameters;  // List of parameters
    private LinkedList<StatementNode> statements;  // List of statements

    // Constructor
    public FunctionDefinitionNode(String functionName, List<String> parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
        this.statements = new LinkedList<>();
    }

    // Add a statement to the function definition node
    public void addStatement(StatementNode statement) {
        this.statements.add(statement);
    }

    // Get the function name
    public String getFunctionName() {
        return functionName;
    }

    // Get the list of parameters
    public List<String> getParameters() {
        return parameters;
    }

    // Get the list of statements
    public LinkedList<StatementNode> getStatements() {
        return statements;
    }

    // Override the toString method to provide a string representation of FunctionDefinitionNode
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Function: ").append(functionName).append("(");
        for (int i = 0; i < parameters.size(); i++) {
            sb.append(parameters.get(i));
            if (i < parameters.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(") {\n");
        for (StatementNode statement : statements) {
            sb.append("  ").append(statement.toString()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
    
    public InterpreterDataType execute(List<InterpreterDataType> evaluatedParams, HashMap<String, InterpreterDataType> locals, Interpreter interpreter) throws Exception {
        // Assuming there's a way to map parameters to the function's scope
        if (parameters.size() != evaluatedParams.size()) {
            throw new Exception("Incorrect number of parameters passed to function " + functionName);
        }

        // Create a new scope for the function execution
        HashMap<String, InterpreterDataType> functionScope = new HashMap<>(locals);

        // Map evaluated parameters to function's parameters
        for (int i = 0; i < parameters.size(); i++) {
            functionScope.put(parameters.get(i), evaluatedParams.get(i));
        }

        // Execute each statement in the function body
        InterpreterDataType lastValue = null;
        for (StatementNode statement : statements) {
            lastValue = statement.evaluate(interpreter, functionScope); // Each statement must also take 'functionScope' instead of 'locals'
        }
        
        // Return the last evaluated value, or null if the function does not return anything
        return lastValue;
    }
}



