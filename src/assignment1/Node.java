package assignment1;

import java.util.HashMap;

public abstract class Node {
    // Existing evaluate method
    public InterpreterDataType evaluate(Interpreter interpreter) throws Exception {
        // This method could remain as is if it's used in other contexts without locals.
        throw new UnsupportedOperationException("Not implemented");
    }

    // Overloaded evaluate method that includes a map for local variables
    public InterpreterDataType evaluate(Interpreter interpreter, HashMap<String, InterpreterDataType> locals) throws Exception {
        // By default, this simply calls the method without locals, but it can be overridden.
        return evaluate(interpreter);
    }
}
