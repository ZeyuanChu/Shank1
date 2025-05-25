package assignment1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class VariableReferenceNodeTest {

    @Test
    void testEvaluateScalar() throws Exception {
        Interpreter interpreter = new Interpreter(null, null);
        interpreter.getGlobalVariables().put("a", new InterpreterDataType("42"));
        VariableReferenceNode variableNode = new VariableReferenceNode("a");
        InterpreterDataType result = variableNode.evaluate(interpreter);
        assertEquals("42", result.getValue());
    }

    @Test
    void testEvaluateArray() throws Exception {
        Interpreter interpreter = new Interpreter(null, null);
        InterpreterArrayDataType array = new InterpreterArrayDataType();
        array.set("0", new InterpreterDataType("apple"));
        array.set("1", new InterpreterDataType("banana"));
        interpreter.getGlobalVariables().put("fruits", array);
        VariableReferenceNode variableNode = new VariableReferenceNode("fruits", new ConstantNode("0"));
        InterpreterDataType result = variableNode.evaluate(interpreter);
        assertEquals("apple", result.getValue());
    }
}

