package assignment1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class FunctionCallNodeTest {

    @Test
    void testEvaluate() throws Exception {
        Interpreter interpreter = new Interpreter(null, null);
        FunctionCallNode functionCallNode = new FunctionCallNode("add");
        functionCallNode.addParameter(new ConstantNode("2"));
        functionCallNode.addParameter(new ConstantNode("3"));
        FunctionDefinitionNode addFunction = new BuiltInFunctionDefinitionNode("add", parameters -> {
            int sum = 0;
            for (InterpreterDataType param : parameters.values()) {
                sum += Integer.parseInt(param.getValue());
            }
            return String.valueOf(sum);
        }, true);
        interpreter.getFunctionDefinitions().put("add", addFunction);
        InterpreterDataType result = functionCallNode.evaluate(interpreter);
        assertEquals("5", result.getValue());
    }
}
