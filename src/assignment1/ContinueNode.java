package assignment1;

public class ContinueNode extends StatementNode {
    
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) {
        // ContinueNode does not return any value
        return new InterpreterDataType("CONTINUE");
    }
}
