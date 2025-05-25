package assignment1;

public class ReturnNode extends StatementNode {
    private Node expression;
 // Constructor that accepts a Node representing the return expression
    public ReturnNode(Node expression) {
        this.expression = expression;
    }
    
    public Node getExpression() {
        return expression;
    }

    public void setExpression(Node expression) {
        this.expression = expression;
    }
    
    @Override
    public InterpreterDataType evaluate(Interpreter interpreter) throws Exception {
        if (expression != null) {
            InterpreterDataType returnValue = expression.evaluate(interpreter);
            interpreter.setLastReturnType(new ReturnType(ReturnType.ResultType.RETURN, returnValue.getValue()));
        } else {
            interpreter.setLastReturnType(new ReturnType(ReturnType.ResultType.RETURN, null));
        }
        return new InterpreterDataType(); // 返回空值或特殊标记值
    }
}
