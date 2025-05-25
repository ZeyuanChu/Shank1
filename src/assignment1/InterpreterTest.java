package assignment1;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.HashMap;

public class InterpreterTest {

	 private Interpreter interpreter;

	    @Before
	    public void setUp() throws Exception {
	        ProgramNode dummyProgram = new ProgramNode();
	        interpreter = new Interpreter(dummyProgram, null);
	    }

	    @Test
	    public void testGsub() {
	        HashMap<String, InterpreterDataType> params = new HashMap<>();
	        params.put("0", new InterpreterDataType("apple apple apple"));
	        params.put("1", new InterpreterDataType("apple"));
	        params.put("2", new InterpreterDataType("banana"));

	        BuiltInFunctionDefinitionNode function = (BuiltInFunctionDefinitionNode) interpreter.getFunctionDefinitions().get("gsub");
	        String result = function.execute(params);

	        assertEquals("banana banana banana", result);
	    }

    @Test
    public void testMatch() {
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("0", new InterpreterDataType("apple pie"));
        params.put("1", new InterpreterDataType("apple"));
        
        BuiltInFunctionDefinitionNode function = (BuiltInFunctionDefinitionNode) interpreter.getFunctionDefinitions().get("match");
        String result = function.execute(params); 

        assertEquals("1", result);
    }
    @Test
    public void testSub() {
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("0", new InterpreterDataType("apple apple apple"));
        params.put("1", new InterpreterDataType("apple"));
        params.put("2", new InterpreterDataType("banana"));

        BuiltInFunctionDefinitionNode function = (BuiltInFunctionDefinitionNode) interpreter.getFunctionDefinitions().get("sub");
        String result = function.execute(params);

        assertEquals("banana apple apple", result);
    }

    @Test
    public void testIndex() {
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("0", new InterpreterDataType("apple pie"));
        params.put("1", new InterpreterDataType("pie"));

        BuiltInFunctionDefinitionNode function = (BuiltInFunctionDefinitionNode) interpreter.getFunctionDefinitions().get("index");
        String result = function.execute(params);

        assertEquals("7", result);
    }

    @Test
    public void testLength() {
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("0", new InterpreterDataType("apple"));

        BuiltInFunctionDefinitionNode function = (BuiltInFunctionDefinitionNode) interpreter.getFunctionDefinitions().get("length");
        String result = function.execute(params);

        assertEquals("5", result);
    }

    @Test
    public void testSplit() {
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("0", new InterpreterDataType("apple,banana,orange"));
        params.put("1", new InterpreterDataType(","));
        params.put("2", new InterpreterDataType("a")); 

        BuiltInFunctionDefinitionNode function = (BuiltInFunctionDefinitionNode) interpreter.getFunctionDefinitions().get("split");
        String result = function.execute(params);

        assertEquals("3", result);
    }


    @Test
    public void testToLower() {
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("0", new InterpreterDataType("APPLE"));
        
        BuiltInFunctionDefinitionNode function = (BuiltInFunctionDefinitionNode) interpreter.getFunctionDefinitions().get("tolower");
        String result = function.execute(params);

        assertEquals("apple", result);
    }

    @Test
    public void testToUpper() {
        HashMap<String, InterpreterDataType> params = new HashMap<>();
        params.put("0", new InterpreterDataType("apple"));
        
        BuiltInFunctionDefinitionNode function = (BuiltInFunctionDefinitionNode) interpreter.getFunctionDefinitions().get("toupper");
        String result = function.execute(params);

        assertEquals("APPLE", result);
    }
    
    
}

