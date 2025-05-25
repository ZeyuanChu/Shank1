package assignment1;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TernaryNodeTest {

    @Test
    void testEvaluateTrueBranch() throws Exception {
        Interpreter interpreter = new Interpreter(null, null);
        interpreter.getGlobalVariables().put("condition", new InterpreterDataType("1"));
        TernaryNode ternaryNode = new TernaryNode(
            new VariableReferenceNode("condition"),
            new ConstantNode("trueResult"),
            new ConstantNode("falseResult")
        );
        InterpreterDataType result = ternaryNode.evaluate(interpreter);
        assertEquals("falseResult", result.getValue());
    }

    @Test
    void testEvaluateFalseBranch() throws Exception {
        Interpreter interpreter = new Interpreter(null, null);
        interpreter.getGlobalVariables().put("condition", new InterpreterDataType("0"));
        TernaryNode ternaryNode = new TernaryNode(
            new VariableReferenceNode("condition"),
            new ConstantNode("trueResult"),
            new ConstantNode("falseResult")
        );
        InterpreterDataType result = ternaryNode.evaluate(interpreter);
        assertEquals("falseResult", result.getValue());
    }
}

