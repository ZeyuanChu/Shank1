package assignment1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class OperationNodeCompareTest {

    @Test
    void testEquals() throws Exception {
        OperationNode operationNode = new OperationNode(
            new ConstantNode("5"), new ConstantNode("5"), OperationNode.OperationType.EQ
        );
        Interpreter interpreter = new Interpreter(null, null);
        InterpreterDataType result = operationNode.evaluate(interpreter);
        assertEquals("1", result.getValue());
    }

    @Test
    void testNotEquals() throws Exception {
        OperationNode operationNode = new OperationNode(
            new ConstantNode("5"), new ConstantNode("7"), OperationNode.OperationType.NE
        );
        Interpreter interpreter = new Interpreter(null, null);
        InterpreterDataType result = operationNode.evaluate(interpreter);
        assertEquals("1", result.getValue());
    }

    @Test
    void testLessThan() throws Exception {
        OperationNode operationNode = new OperationNode(
            new ConstantNode("5"), new ConstantNode("7"), OperationNode.OperationType.LT
        );
        Interpreter interpreter = new Interpreter(null, null);
        InterpreterDataType result = operationNode.evaluate(interpreter);
        assertEquals("1", result.getValue());
    }

    @Test
    void testGreaterThan() throws Exception {
        OperationNode operationNode = new OperationNode(
            new ConstantNode("7"), new ConstantNode("5"), OperationNode.OperationType.GT
        );
        Interpreter interpreter = new Interpreter(null, null);
        InterpreterDataType result = operationNode.evaluate(interpreter);
        assertEquals("1", result.getValue());
    }
}
