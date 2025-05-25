package assignment1;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {
    private HashMap<String, InterpreterDataType> globalVariables; // Stores global variables for the interpreter.
    private HashMap<String, FunctionDefinitionNode> functionDefinitions;// Stores function definitions.
    private LineManager lineManager;// Manages the lines of code to be executed.
    private ReturnType lastReturnType;  // Store the last ReturnType
    
    public Interpreter(ProgramNode programNode, String filePath) throws Exception {
        this.globalVariables = new HashMap<>();
        this.functionDefinitions = new HashMap<>();
        
        // Only populate function hash map if programNode is not null
        if (programNode != null) {
            for (FunctionDefinitionNode func : programNode.getFunctionNodes()) {
                functionDefinitions.put(func.getFunctionName(), func);
            }
        }

        // Add built-in functions
        addBuiltInFunctions();

        // Read lines from file if path is provided
        if (filePath != null && !filePath.isEmpty()) {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            this.lineManager = new LineManager(lines);
        } else {
            this.lineManager = new LineManager();
        }
        
        // Set global variables
        globalVariables.put("FILENAME", new InterpreterDataType(filePath));
        globalVariables.put("FS", new InterpreterDataType(" "));
        globalVariables.put("OFMT", new InterpreterDataType("%.6g"));
        globalVariables.put("OFS", new InterpreterDataType(" "));
        globalVariables.put("ORS", new InterpreterDataType("\n"));
    }
    
 // Get the name of the function to call from the FunctionCallNode
    public InterpreterDataType RunFunctionCall(FunctionCallNode functionCallNode, HashMap<String, InterpreterDataType> locals) throws Exception {
        String functionName = functionCallNode.getFunctionName();
        FunctionDefinitionNode functionDefinition = this.getFunction(functionName);
        if (functionDefinition == null) {
            throw new Exception("Function '" + functionName + "' not defined.");
        }

        List<InterpreterDataType> evaluatedParams = new ArrayList<>();
        for (Node param : functionCallNode.getParameters()) {
            evaluatedParams.add(param.evaluate(this, locals));
        }

        if (functionDefinition.getParameters().size() != evaluatedParams.size()) {
            throw new Exception("Incorrect number of parameters for function '" + functionName + "'. Expected: " + functionDefinition.getParameters().size() + ", Provided: " + evaluatedParams.size());
        }

        HashMap<String, InterpreterDataType> functionLocals = new HashMap<>(locals);
        for (int i = 0; i < functionDefinition.getParameters().size(); i++) {
            functionLocals.put(functionDefinition.getParameters().get(i), evaluatedParams.get(i));
        }

        return executeFunction(functionDefinition, functionLocals);
    }
 // Helper method to execute the statements of a function
    private InterpreterDataType executeFunction(FunctionDefinitionNode functionDefinition, HashMap<String, InterpreterDataType> functionLocals) throws Exception {
        InterpreterDataType lastValue = null;
        for (StatementNode statement : functionDefinition.getStatements()) {
            lastValue = statement.evaluate(this, functionLocals);
        }
        return lastValue;
    }
    
    
    public void InterpretProgram(ProgramNode programNode) throws Exception {
    	    // Execute BEGIN blocks first
    	    for (BlockNode block : programNode.getOtherBlockNodes()) {
    	        // Assuming BEGIN blocks have a certain characteristic, e.g., a specific statement
    	        if (isBeginBlock(block)) {
    	            InterpretBlock(block);
    	        }
    	    }

    	    // Process main blocks for each line in the file
    	    while (lineManager.SplitAndAssign()) {
    	        for (BlockNode block : programNode.getOtherBlockNodes()) {
    	            // Assuming MAIN blocks have a certain characteristic
    	            if (isMainBlock(block)) {
    	                InterpretBlock(block);
    	            }
    	        }
    	    }

    	    // Execute END blocks
    	    for (BlockNode block : programNode.getOtherBlockNodes()) {
    	        // Assuming END blocks have a certain characteristic
    	        if (isEndBlock(block)) {
    	            InterpretBlock(block);
    	        }
    	    }
    	}

    	// Placeholder methods for checking block type
    	private boolean isBeginBlock(BlockNode block) {
    	    // Implement logic to determine if block is a BEGIN block
    	    return false; // Placeholder return
    	}

    	private boolean isMainBlock(BlockNode block) {
    	    // Implement logic to determine if block is a MAIN block
    	    return true; // Placeholder return
    	}

    	private boolean isEndBlock(BlockNode block) {
    	    // Implement logic to determine if block is an END block
    	    return false; // Placeholder return
    	}

    
    public void InterpretBlock(BlockNode blockNode) throws Exception {
        // Check and evaluate the condition if it exists
        if (blockNode.getCondition().isPresent()) {
            InterpreterDataType conditionResult = blockNode.getCondition().get().evaluate(this, globalVariables);
            if (!conditionResult.getValue().equals("1")) {
                return; // Skip block if condition is false
            }
        }

        // Execute each statement in the block
        for (Node statement : blockNode.getStatements()) {
            processStatement(globalVariables, statement);
        }
    }

 // Set the Latest ReturnType
    public void setLastReturnType(ReturnType returnType) {
        this.lastReturnType = returnType;
    }

    // getting the latest ReturnType
    public ReturnType getLastReturnType() {
        return lastReturnType;
    }
    
 // Method to look up variables in the interpreter's environment
    public InterpreterDataType lookupVariable(String variableName) throws Exception {
        InterpreterDataType value = globalVariables.get(variableName);
        if (value == null) {
            // Variable not found, handle this scenario, maybe throw an exception
            throw new Exception("Variable '" + variableName + "' not found.");
        }
        return value;
    }
    
    // Method to get a function definition by name
    public FunctionDefinitionNode getFunction(String functionName) throws Exception {
        FunctionDefinitionNode function = functionDefinitions.get(functionName);
        if (function == null) {
            // Function not found, handle this scenario, maybe throw an exception
            throw new Exception("Function '" + functionName + "' not defined.");
        }
        return function;
    }

    
    public HashMap<String, FunctionDefinitionNode> getFunctionDefinitions() {
        return functionDefinitions;
    }
    
    public ReturnType interpretListOfStatements(List<Node> statements, HashMap<String, InterpreterDataType> locals) throws Exception {
        for (Node statement : statements) {
            ReturnType result = processStatement(locals, statement);
            if (result.getResultType() != ReturnType.ResultType.NORMAL) {
                return result;
            }
        }
        return new ReturnType(ReturnType.ResultType.NORMAL);
    }

    private ReturnType processStatement(HashMap<String, InterpreterDataType> locals, Node stmt) throws Exception {
        if (stmt instanceof AssignmentNode) {
            // Handling assignment nodes
            AssignmentNode assignmentNode = (AssignmentNode) stmt;
            InterpreterDataType rightValue = assignmentNode.getExpression().evaluate(this, locals);
            if (assignmentNode.getTarget() instanceof VariableReferenceNode) {
                VariableReferenceNode varRef = (VariableReferenceNode) assignmentNode.getTarget();
                setVariable(varRef.getVariableName(), rightValue);
            }
            return new ReturnType(ReturnType.ResultType.NORMAL);
        } else if (stmt instanceof BreakNode) {
            //  BreakNode
            return new ReturnType(ReturnType.ResultType.BREAK);
        } else if (stmt instanceof ContinueNode) {
            // ContinueNode
            return new ReturnType(ReturnType.ResultType.CONTINUE);
        } else if (stmt instanceof DoWhileNode) {
            // DoWhileNode
            DoWhileNode doWhileNode = (DoWhileNode) stmt;
            do {
                ReturnType returnType = interpretListOfStatements(doWhileNode.getBlock().getStatements(), locals);
                if (returnType.getResultType() != ReturnType.ResultType.NORMAL) {
                    return returnType;
                }
            } while (doWhileNode.getCondition().evaluate(this, locals).getValue().equals("1"));
            return new ReturnType(ReturnType.ResultType.NORMAL);
        } else if (stmt instanceof ForNode) {
            // ForNode
            ForNode forNode = (ForNode) stmt;
            for (forNode.getInitialization().evaluate(this, locals);
                 forNode.getCondition().evaluate(this, locals).getValue().equals("1");
                 forNode.getPostOperation().evaluate(this, locals)) {
                ReturnType returnType = interpretListOfStatements(forNode.getBlock().getStatements(), locals);
                if (returnType.getResultType() != ReturnType.ResultType.NORMAL) {
                    return returnType;
                }
            }
            return new ReturnType(ReturnType.ResultType.NORMAL);
        } else if (stmt instanceof IfNode) {
            // IfNode
            IfNode ifNode = (IfNode) stmt;
            if (ifNode.getCondition().evaluate(this, locals).getValue().equals("1")) {
                return interpretListOfStatements(ifNode.getBlock().getStatements(), locals);
            }
            return new ReturnType(ReturnType.ResultType.NORMAL);
        } else if (stmt instanceof ReturnNode) {
            // ReturnNode
            ReturnNode returnNode = (ReturnNode) stmt;
            InterpreterDataType returnValue = returnNode.getExpression() != null
                ? returnNode.getExpression().evaluate(this, locals)
                : new InterpreterDataType(null);
            return new ReturnType(ReturnType.ResultType.RETURN, returnValue.getValue());
        } else if (stmt instanceof WhileNode) {
            // WhileNode
            WhileNode whileNode = (WhileNode) stmt;
            while (whileNode.getCondition().evaluate(this, locals).getValue().equals("1")) {
                ReturnType returnType = interpretListOfStatements(whileNode.getBlock().getStatements(), locals);
                if (returnType.getResultType() != ReturnType.ResultType.NORMAL) {
                    return returnType;
                }
            }
            return new ReturnType(ReturnType.ResultType.NORMAL);
        }
        
        stmt.evaluate(this, locals);
        return new ReturnType(ReturnType.ResultType.NORMAL);
    }

	private void addBuiltInFunctions() {
        // Example built-in function: print (treated as variadic)
        functionDefinitions.put("print", new BuiltInFunctionDefinitionNode("print", parameters -> {
            StringBuilder output = new StringBuilder();
            int index = 0;
            while (parameters.containsKey(String.valueOf(index))) {
                output.append(parameters.get(String.valueOf(index)).getValue());
                if (parameters.containsKey(String.valueOf(index + 1))) {
                    output.append(" ");  // Separator
                }
                index++;
            }
            System.out.print(output.toString());
            return "";  // print returns nothing
        }, true));
        
        // getline and next
        functionDefinitions.put("getline", new BuiltInFunctionDefinitionNode("getline", parameters -> {
            boolean success = lineManager.SplitAndAssign();
            return success ? "1" : "0";  // return success status
        }, false));

        functionDefinitions.put("next", new BuiltInFunctionDefinitionNode("next", parameters -> {
            boolean success = lineManager.SplitAndAssign();
            return success ? "1" : "0";  // return success status
        }, false));

     // gsub method
        functionDefinitions.put("gsub", new BuiltInFunctionDefinitionNode("gsub", parameters -> {
            String input = parameters.get("0").getValue();
            String pattern = parameters.get("1").getValue();
            String replacement = parameters.get("2").getValue();
            String result = input.replaceAll(pattern, replacement);
            return result;
        }, false));

        // match method
        functionDefinitions.put("match", new BuiltInFunctionDefinitionNode("match", parameters -> {
            String input = parameters.get("0").getValue();
            String pattern = parameters.get("1").getValue();
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(input);
            return m.find() ? "1" : "0";  // Return 1 if match found, otherwise 0
        }, false));

        // sub method
        functionDefinitions.put("sub", new BuiltInFunctionDefinitionNode("sub", parameters -> {
            String input = parameters.get("0").getValue();
            String pattern = parameters.get("1").getValue();
            String replacement = parameters.get("2").getValue();
            String result = input.replaceFirst(pattern, replacement);
            return result;
        }, false));

        // index method
        functionDefinitions.put("index", new BuiltInFunctionDefinitionNode("index", parameters -> {
            String input = parameters.get("0").getValue();
            String search = parameters.get("1").getValue();
            return String.valueOf(input.indexOf(search) + 1);  // AWK's index starts from 1
        }, false));
 
        // length method
        functionDefinitions.put("length", new BuiltInFunctionDefinitionNode("length", parameters -> {
            String input = parameters.get("0").getValue();
            return String.valueOf(input.length());
        }, false));

        // split
        functionDefinitions.put("split", new BuiltInFunctionDefinitionNode("split", parameters -> {
            String input = parameters.get("0").getValue();
            String delimiter = parameters.get("1").getValue();
            String[] parts = input.split(delimiter);
            HashMap<String, InterpreterDataType> result = new HashMap<>();
            for (int i = 0; i < parts.length; i++) {
                result.put(String.valueOf(i), new InterpreterDataType(parts[i]));
            }
            return String.valueOf(parts.length);  // Return number of splits
        }, false));

        // substr method
        functionDefinitions.put("substr", new BuiltInFunctionDefinitionNode("substr", parameters -> {
            String input = parameters.get("0").getValue();
            int start = Integer.parseInt(parameters.get("1").getValue());
            int length = Integer.parseInt(parameters.get("2").getValue());
            return input.substring(start - 1, start - 1 + length);  // Adjust for AWK's 1-indexing
        }, false));

        // tolower method
        functionDefinitions.put("tolower", new BuiltInFunctionDefinitionNode("tolower", parameters -> {
            String input = parameters.get("0").getValue();
            return input.toLowerCase();
        }, false));

        // toupper method
        functionDefinitions.put("toupper", new BuiltInFunctionDefinitionNode("toupper", parameters -> {
            String input = parameters.get("0").getValue();
            return input.toUpperCase();
        }, false));
    }
 // Get the LineManager instance.
    public LineManager getLineManager() {
        return lineManager;
    }
 // Get the global variables map.
    public HashMap<String, InterpreterDataType> getGlobalVariables() {
        return globalVariables;
    }
 // Split and assign variables from a line of code for testing purposes.
    public boolean splitAndAssignForTesting() {
        return lineManager.SplitAndAssign();
    }
 // Inner class for managing lines of code.
    private class LineManager {
        private List<String> lines;
        private int currentLineIndex = 0;
        
        public LineManager(List<String> lines) {
            this.lines = lines;
        }

        public LineManager() {
            this.lines = List.of();  
        }
     // Split a line of code and assign variables.
        public boolean SplitAndAssign() {
            if (currentLineIndex >= lines.size()) {
                return false;
            }
            String currentLine = lines.get(currentLineIndex);
            String fs = globalVariables.get("FS").getValue();
            String[] splitted = currentLine.split(fs);
            
            globalVariables.put("$0", new InterpreterDataType(currentLine));
            for (int i = 0; i < splitted.length; i++) {
                globalVariables.put("$" + (i+1), new InterpreterDataType(splitted[i]));
            }
            globalVariables.put("NF", new InterpreterDataType(String.valueOf(splitted.length)));
            
            // Update NR and FNR variables
            int nr = currentLineIndex + 1;
            globalVariables.put("NR", new InterpreterDataType(String.valueOf(nr)));
            globalVariables.put("FNR", new InterpreterDataType(String.valueOf(nr)));

            currentLineIndex++;
            return true;
        }
    }
    // Retrieve a variable's value from the global environment
    public InterpreterDataType getVariableValue(int index) {
        return globalVariables.get(index);
    }

    // Set a variable in the global environment
    public void setVariable(String varName, InterpreterDataType value) {
        globalVariables.put(varName, value);
    }

}

