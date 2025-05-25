package assignment1;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AssignmentNodeTest {

    @Test
    void testEvaluate() throws Exception {
        VariableReferenceNode variableNode = new VariableReferenceNode("a");
        OperationNode additionNode = new OperationNode(
            new ConstantNode("2"), new ConstantNode("2"), OperationNode.OperationType.ADD
        );
        AssignmentNode assignmentNode = new AssignmentNode(variableNode, additionNode);
        Interpreter interpreter = new Interpreter(null, null);
        InterpreterDataType result = assignmentNode.evaluate(interpreter);
        assertEquals("4.0", result.getValue());
        InterpreterDataType variableValue = interpreter.getGlobalVariables().get("a");
        assertEquals("4.0", variableValue.getValue());
    }
}