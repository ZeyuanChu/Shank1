package assignment1;

public class BreakNode extends StatementNode {
    
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) {
        // BreakNode 不返回任何值，但其存在将由 ProcessStatement 方法识别
        return new InterpreterDataType("BREAK");
    }
}
