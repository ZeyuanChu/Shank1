package assignment1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ConstantNodeTest {

    @Test
    void testEvaluate() throws Exception {
        ConstantNode constantNode = new ConstantNode("42");
        Interpreter interpreter = new Interpreter(null, null);
        InterpreterDataType result = constantNode.evaluate(interpreter);
        assertEquals("42", result.getValue());
    }
}

