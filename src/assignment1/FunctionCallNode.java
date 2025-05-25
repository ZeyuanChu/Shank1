package assignment1;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionCallNode extends StatementNode {
    private String functionName; // The name of the function being called
    private List<Node> parameters; // List of parameters passed to the function

    // Constructor to initialize the FunctionCallNode with a function name
    public FunctionCallNode(String functionName) {
        this.functionName = functionName;
        this.parameters = new LinkedList<>();
    }

    // Getter for retrieving the function name
    public String getFunctionName() {
        return functionName;
    }

    // Method to add a parameter to the list of parameters
    public void addParameter(Node parameter) {
        parameters.add(parameter);
    }

    // Getter for retrieving the list of parameters
    public List<Node> getParameters() {
        return parameters;
    }

    // Method to evaluate the FunctionCallNode
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter, HashMap<String, InterpreterDataType> locals) throws Exception {
        FunctionDefinitionNode functionDefinition = interpreter.getFunction(functionName);
        if (functionDefinition == null) {
            throw new Exception("Function " + functionName + " is not defined.");
        }

        List<InterpreterDataType> evaluatedParams = parameters.stream()
            .map(param -> {
                try {
                    return param.evaluate(interpreter, locals);
                } catch (Exception e) {
                    throw new RuntimeException("Error evaluating parameter: " + e.getMessage(), e);
                }
            })
            .collect(Collectors.toList());

        if (functionDefinition.getParameters().size() != evaluatedParams.size()) {
            throw new Exception("Function " + functionName + " expects " + functionDefinition.getParameters().size() + " parameters but got " + evaluatedParams.size());
        }

        // Execute the function with the evaluated parameters and return the result
        return functionDefinition.execute(evaluatedParams, locals, interpreter);
    }
}


