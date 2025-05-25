package assignment1;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

class ProcessStatementTest {

	@Test
	void testReturnType() throws Exception {
	    AssignmentNode assignmentNode = new AssignmentNode(new VariableReferenceNode("x"), new ConstantNode("10"));
	    Interpreter interpreter = new Interpreter(null, null);
	    Method method = Interpreter.class.getDeclaredMethod("processStatement", HashMap.class, Node.class);
	    method.setAccessible(true);  
	    ReturnType result = (ReturnType) method.invoke(interpreter, interpreter.getGlobalVariables(), assignmentNode);

	    assertEquals(ReturnType.ResultType.NORMAL, result.getResultType());
	    assertNull(result.getValue());  
	}


    @Test
    void testProcessStatementAssignment() throws Exception {
        Interpreter interpreter = new Interpreter(null, null);
        AssignmentNode assignmentNode = new AssignmentNode(new VariableReferenceNode("a"), new ConstantNode("5"));

        Method method = Interpreter.class.getDeclaredMethod("processStatement", HashMap.class, Node.class);
        method.setAccessible(true);  
        ReturnType result = (ReturnType) method.invoke(interpreter, interpreter.getGlobalVariables(), assignmentNode);

        assertEquals("5", interpreter.lookupVariable("a").getValue());
    }


    @Test
    void testProcessStatementBreak() throws Exception {
        Interpreter interpreter = new Interpreter(null, null);

        
        BlockNode loopBody = new BlockNode();
        loopBody.addStatement(new BreakNode());
        loopBody.addStatement(new AssignmentNode(new VariableReferenceNode("x"), new ConstantNode("10")));

        
        WhileNode whileNode = new WhileNode(new ConstantNode("1"), loopBody);
        whileNode.setBlock(loopBody);  

       
        BlockNode blockNode = new BlockNode();
        blockNode.addStatement(whileNode);

        interpreter.InterpretBlock(blockNode);

        
        assertNull(interpreter.getGlobalVariables().get("x"));
    }
    
    @Test
    void testInterpretProgram() throws Exception {
        ProgramNode programNode = new ProgramNode();

 
        BlockNode beginBlock = new BlockNode();
        beginBlock.addStatement(new AssignmentNode(new VariableReferenceNode("beginExecuted"), new ConstantNode("true")));
        
        programNode.addOtherNode(beginBlock);

        BlockNode mainBlock = new BlockNode();
        mainBlock.addStatement(new AssignmentNode(new VariableReferenceNode("mainExecuted"), new ConstantNode("true")));
        programNode.addOtherNode(mainBlock);

        BlockNode endBlock = new BlockNode();
        endBlock.addStatement(new AssignmentNode(new VariableReferenceNode("endExecuted"), new ConstantNode("true")));
 
        programNode.addOtherNode(endBlock);

    
        Interpreter interpreter = new Interpreter(programNode, "/Users/tommychu/Desktop/SomeFile.txt");
        interpreter.InterpretProgram(programNode);

        assertEquals("true", interpreter.lookupVariable("beginExecuted").getValue());
        assertEquals("true", interpreter.lookupVariable("mainExecuted").getValue());
        assertEquals("true", interpreter.lookupVariable("endExecuted").getValue());
    }

    @Test
    void testInterpretBlock() throws Exception {
        BlockNode blockNode = new BlockNode();
        blockNode.setCondition(Optional.of(new ConstantNode("1"))); // 总是真
        blockNode.addStatement(new AssignmentNode(new VariableReferenceNode("x"), new ConstantNode("10")));

        Interpreter interpreter = new Interpreter(null, null);
        interpreter.InterpretBlock(blockNode);

        assertEquals("10", interpreter.lookupVariable("x").getValue());
    }

}
