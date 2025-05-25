package assignment1;

import java.util.HashMap;
import java.util.function.Function;

public class BuiltInFunctionDefinitionNode extends FunctionDefinitionNode {

    // A function that represents the built-in function's execution logic.
    private Function<HashMap<String, InterpreterDataType>, String> execute;

    // Indicates whether the function is variadic (accepts a variable number of arguments).
    private boolean isVariadic;

    // Constructor for the BuiltInFunctionDefinitionNode class.
    public BuiltInFunctionDefinitionNode(String functionName, 
                                         Function<HashMap<String, InterpreterDataType>, String> execute, 
                                         boolean isVariadic) {
        // Call the constructor of the parent class (FunctionDefinitionNode).
        super(functionName, null); 

        // Initialize the execute function and isVariadic flag.
        this.execute = execute;
        this.isVariadic = isVariadic;
    }

    // Execute the built-in function with the provided parameters.
    public String execute(HashMap<String, InterpreterDataType> parameters) {
        return this.execute.apply(parameters);
    }

    // Check if the function is variadic (accepts a variable number of arguments).
    public boolean isVariadic() {
        return isVariadic;
    }
}
