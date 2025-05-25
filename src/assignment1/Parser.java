package assignment1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;

import assignment1.OperationNode.OperationType;

import java.util.List;

public class Parser {
    private TokenHandler tokenHandler;
    private ProgramNode programNode;


    // Constructor that accepts a list of tokens and creates a TokenHandler
    public Parser(LinkedList<Token> tokens) {
        this.tokenHandler = new TokenHandler(tokens);
    }

    // AcceptSeparators method,accepts any number of separators
    // Returns true if at least one separator is found
    public boolean AcceptSeparators() {
        boolean found = false;
        while (tokenHandler.MoreTokens()) {
            Optional<Token> token = tokenHandler.Peek(0);
            if (token.isPresent() && (token.get().getType() == Token.TokenType.SEPARATOR)) {
                found = true;
                tokenHandler.MatchAndRemove(token.get().getType());
            } else {
                break;
            }
        }
        return found;
    }

    public ProgramNode Parse() {	
        this.programNode = new ProgramNode();

        // Check if programNode is null and throw an exception if so
        if (programNode == null) {
            throw new RuntimeException("programNode is null");
        }

        // Loop through tokens until there are no more tokens to process
        while (tokenHandler.MoreTokens()) {
            // Check if the current token is an opening bracket
            if (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.LBRACKET) {
                // Parse a pattern
                parsePattern(); 
            } else {
                Optional<Node> statement = Optional.empty();

                // Try to parse different types of statements and actions
                if (!statement.isPresent()) statement = ParseFunction(programNode);
                if (!statement.isPresent()) statement = ParseAction(programNode);
                if (!statement.isPresent()) statement = ParseDelete(); 
                if (!statement.isPresent()) statement = ParseWhile();
                if (!statement.isPresent()) statement = ParseDoWhile();
                if (!statement.isPresent()) statement = ParseReturn();
                if (!statement.isPresent()) statement = ParseContinue();
                if (!statement.isPresent()) statement = ParseBreak();
                if (!statement.isPresent()) statement = ParseIf();

                // If no specific statement type is found, try to parse expressions
                if (!statement.isPresent()) {
                    statement = ParseTernary();
                    if (!statement.isPresent()) statement = ParseBooleanCompare();
                    if (!statement.isPresent()) statement = ParseExpression();
                }

                if (statement.isPresent()) {
                    if (statement.get() instanceof BlockNode) {
                        // Add a BlockNode directly to the ProgramNode
                        programNode.addOtherNode((BlockNode) statement.get());
                    } else {
                        // Create a BlockNode for the statement/expression and add it to the ProgramNode
                        BlockNode blockNode = new BlockNode();
                        blockNode.addStatement(statement.get());
                        programNode.addOtherNode(blockNode);
                    }
                } else {
                    // Throw an exception for unexpected tokens
                    throw new RuntimeException("Unexpected token encountered in Parse: " + tokenHandler.Peek(0).orElse(new Token(Token.TokenType.WORD, "UNKNOWN")).toString());
                }
            }

            // Consume all consecutive SEPARATOR tokens
            while (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.SEPARATOR) {
                tokenHandler.MatchAndRemove(Token.TokenType.SEPARATOR);
            }
        }

        return programNode;
    }

    
    public BlockNode ParseBlock() {
        BlockNode blockNode = new BlockNode();
        
        // Check for open curly brace token for multi-line block
        if (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.LBRACE) {
            tokenHandler.MatchAndRemove(Token.TokenType.LBRACE);
            AcceptSeparators();
            
            // Call ParseStatement() repeatedly until it doesn’t return a statement
            while (true) {
                Optional<Node> statement = ParseStatement();
                if (statement.isPresent()) {
                    blockNode.addStatement(statement.get());
                    AcceptSeparators();
                } else {
                    break;
                }
            }
            
            // Check for the close curly brace
            if (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.RBRACE) {
                tokenHandler.MatchAndRemove(Token.TokenType.RBRACE);
            } else {
                throw new RuntimeException("Expected closing curly brace.");
            }
        } else {
            // Handle single line block
            Optional<Node> statement = ParseStatement();
            if (statement.isPresent()) {
                blockNode.addStatement(statement.get());
            }
            AcceptSeparators();
        }
        
        return blockNode;
    }

    public Optional<Node> ParseStatement() {
        Optional<Node> statement;

        // Try to parse each of the statement types

        statement = ParseIf();
        if (statement.isPresent()) return statement;

        statement = ParseWhile();
        if (statement.isPresent()) return statement;

        statement = ParseFor();
        if (statement.isPresent()) return statement;

        statement = ParseDelete();
        if (statement.isPresent()) return statement;

        statement = ParseDoWhile();
        if (statement.isPresent()) return statement;

        statement = ParseReturn();
        if (statement.isPresent()) return statement;

        statement = ParseContinue();
        if (statement.isPresent()) return statement;

        statement = ParseBreak();
        if (statement.isPresent()) return statement;

        // Default to parsing an expression if none of the above statements match
        statement = ParseExpression();
        if (statement.isPresent()) return statement;

        return Optional.empty();
    }

    public Optional<Node> ParseIf() {
        Optional<Token> token = tokenHandler.Peek(0);
        if (token.isPresent() && token.get().getType() == Token.TokenType.IF) {
            tokenHandler.MatchAndRemove(Token.TokenType.IF);
            tokenHandler.MatchAndRemove(Token.TokenType.LPAREN);
            
            // Parse the condition inside the parentheses
            Optional<Node> condition = ParseExpression(); 
            if (!condition.isPresent()) {
                throw new RuntimeException("Expected condition inside if statement.");
            }
            
            tokenHandler.MatchAndRemove(Token.TokenType.RPAREN);
            
            // Parse the body of the if statement
            Optional<Node> body = ParseAction(this.programNode); 
            if (!body.isPresent()) {
                throw new RuntimeException("Expected body for the if statement.");
            }
            
            // Create an IfNode with the parsed condition and body
            IfNode ifNode = new IfNode(condition.get(), body.get());
            return Optional.of(ifNode);
        }
        return Optional.empty();
    }
    
    public Optional<Node> ParseWhile() {
        Optional<Token> token = tokenHandler.Peek(0);
        if (token.isPresent() && token.get().getType() == Token.TokenType.WHILE) {
            tokenHandler.MatchAndRemove(Token.TokenType.WHILE);
            tokenHandler.MatchAndRemove(Token.TokenType.LPAREN);
            
            // Parse the condition inside the parentheses
            Optional<Node> condition = ParseExpression(); 
            if (!condition.isPresent()) {
                throw new RuntimeException("Expected a condition for the while statement.");
            }
            
            tokenHandler.MatchAndRemove(Token.TokenType.RPAREN);
            
            // Parse the body of the while statement
            Optional<Node> body = ParseAction(this.programNode); 
            if (!body.isPresent()) {
                throw new RuntimeException("Expected body for the while statement.");
            }
            
            // Create a WhileNode with the parsed condition and body
            WhileNode whileNode = new WhileNode(condition.get(), body.get());
            return Optional.of(whileNode);
        }
        return Optional.empty();
    }

    private Optional<Node> ParseFor() {
        if (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.FOR) {
            tokenHandler.MatchAndRemove(Token.TokenType.FOR);
            AcceptSeparators();

            if (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.LPAREN) {
                tokenHandler.MatchAndRemove(Token.TokenType.LPAREN);
                
                // Check if it's a for-each loop format
                if (tokenHandler.Peek(1).isPresent() && tokenHandler.Peek(1).get().getType() == Token.TokenType.IN) {
                    String variable = tokenHandler.Peek(0).get().getValue();
                    tokenHandler.MatchAndRemove(Token.TokenType.IDENTIFIER);
                    tokenHandler.MatchAndRemove(Token.TokenType.IN);
                    String array = tokenHandler.Peek(0).get().getValue();
                    tokenHandler.MatchAndRemove(Token.TokenType.IDENTIFIER);
                    tokenHandler.MatchAndRemove(Token.TokenType.RPAREN);

                    BlockNode block = ParseBlock();

                    ForEachNode forEachNode = new ForEachNode();
                    forEachNode.setVariable(variable);
                    forEachNode.setArray(array);
                    forEachNode.setBlock(block);

                    return Optional.of((Node) forEachNode);
                } else {
                    // It's a regular for loop
                    Optional<Node> initializationOpt = ParseOperation();
                    if (!initializationOpt.isPresent()) {
                        throw new RuntimeException("Expected an initialization operation for the for loop.");
                    }
                    Node initialization = initializationOpt.get();
                    tokenHandler.MatchAndRemove(Token.TokenType.SEMICOLON);

                    Optional<Node> conditionOpt = ParseOperation();
                    if (!conditionOpt.isPresent()) {
                        throw new RuntimeException("Expected a condition for the for loop.");
                    }
                    Node condition = conditionOpt.get();
                    tokenHandler.MatchAndRemove(Token.TokenType.SEMICOLON);

                    Optional<Node> postOperationOpt = ParseOperation();
                    if (!postOperationOpt.isPresent()) {
                        throw new RuntimeException("Expected a post-operation for the for loop.");
                    }
                    Node postOperation = postOperationOpt.get();
                    tokenHandler.MatchAndRemove(Token.TokenType.RPAREN);

                    BlockNode block = ParseBlock();

                    ForNode forNode = new ForNode();
                    forNode.setInitialization(initialization);
                    forNode.setCondition(condition);
                    forNode.setPostOperation(postOperation);
                    forNode.setBlock(block);

                    return Optional.of((Node) forNode);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Node> ParseDelete() {
        Optional<Token> token = tokenHandler.Peek(0);
        if (token.isPresent() && token.get().getType() == Token.TokenType.DELETE) {
            tokenHandler.MatchAndRemove(Token.TokenType.DELETE);
            
            token = tokenHandler.MatchAndRemove(Token.TokenType.IDENTIFIER);
            if (!token.isPresent()) {
                throw new RuntimeException("Expected array name after DELETE token.");
            }
            String arrayName = token.get().getValue();
            
            List<Node> indices = new LinkedList<>();
            
            // Check for left bracket (indicating indices)
            token = tokenHandler.Peek(0);
            if (token.isPresent() && token.get().getType() == Token.TokenType.LBRACKET) {
                tokenHandler.MatchAndRemove(Token.TokenType.LBRACKET);
                
                // Parse indices
                while (true) {
                    Optional<Node> index = ParseExpression(); // Assuming you have a method to parse expressions
                    if (index.isPresent()) {
                        indices.add(index.get());
                        
                        token = tokenHandler.Peek(0);
                        if (token.isPresent() && token.get().getType() == Token.TokenType.COMMA) {
                            tokenHandler.MatchAndRemove(Token.TokenType.COMMA);
                        } else {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                
                // Check for right bracket
                token = tokenHandler.MatchAndRemove(Token.TokenType.RBRACKET);
                if (!token.isPresent()) {
                    throw new RuntimeException("Expected right bracket after indices.");
                }
            }
            
            DeleteNode deleteNode = new DeleteNode(arrayName, indices);
            return Optional.of(deleteNode);
        }
        return Optional.empty();
    }

    public Optional<Node> ParseDoWhile() {
        if (tokenHandler.MatchAndRemove(Token.TokenType.DO).isPresent()) {
            // Parse the body of the do-while loop
        	Optional<Node> body = Optional.of(ParseBlock());
            if (!body.isPresent()) {
                throw new RuntimeException("Expected a block for the do-while statement.");
            }

            if (!tokenHandler.MatchAndRemove(Token.TokenType.WHILE).isPresent()) {
                throw new RuntimeException("Expected 'while' keyword after do block.");
            }

            if (!tokenHandler.MatchAndRemove(Token.TokenType.LPAREN).isPresent()) {
                throw new RuntimeException("Expected '(' after 'while' keyword.");
            }

            Optional<Node> condition = ParseExpression(); 
            if (!condition.isPresent()) {
                throw new RuntimeException("Expected a condition for the do-while statement.");
            }

            if (!tokenHandler.MatchAndRemove(Token.TokenType.RPAREN).isPresent()) {
                throw new RuntimeException("Expected ')' after do-while condition.");
            }

            return Optional.of(new DoWhileNode(body.get(), condition.get()));
        }
        return Optional.empty();
    }


    public Optional<Node> ParseReturn() {
        Optional<Token> token = tokenHandler.Peek(0);
        if (token.isPresent() && token.get().getType() == Token.TokenType.RETURN) {
            tokenHandler.MatchAndRemove(Token.TokenType.RETURN);
            
            // Parse the expression after the return keyword
            Optional<Node> expression = ParseExpression();
            if (!expression.isPresent()) {
                throw new RuntimeException("Expected an expression after the return keyword.");
            }
            
            // Create a ReturnNode with the parsed expression and return it
            ReturnNode returnNode = new ReturnNode(expression.get());
            return Optional.of(returnNode);
        }
        return Optional.empty();
    }


    private Optional<Node> ParseContinue() {
        if (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.CONTINUE) {
            tokenHandler.MatchAndRemove(Token.TokenType.CONTINUE);
            AcceptSeparators();

            // Create a ContinueNode
            ContinueNode continueNode = new ContinueNode();

            return Optional.of(continueNode);
        }
        return Optional.empty();
    }

    private Optional<Node> ParseBreak() {
        if (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.BREAK) {
            tokenHandler.MatchAndRemove(Token.TokenType.BREAK);
            AcceptSeparators();

            // Create a BreakNode
            BreakNode breakNode = new BreakNode();

            return Optional.of(breakNode);
        }
        return Optional.empty();
    }



    private void parsePattern() {
        tokenHandler.MatchAndRemove(Token.TokenType.LBRACKET);
        // Handle the content of the pattern here
        if (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.WORD) {
            tokenHandler.MatchAndRemove(Token.TokenType.WORD);
        }
        tokenHandler.MatchAndRemove(Token.TokenType.RBRACKET);
        
        // Create a BlockNode for the pattern and add it to the ProgramNode
        BlockNode blockNode = new BlockNode();
        programNode.addOtherNode(blockNode);
    }


	// ParseFunction method, tries to parse a function definition and adds it to the ProgramNode
    public Optional<Node> ParseFunction(ProgramNode programNode) {
        Optional<Token> token = tokenHandler.Peek(0);
        if (token.isPresent() && token.get().getType() == Token.TokenType.FUNCTION) {
            tokenHandler.MatchAndRemove(Token.TokenType.FUNCTION);
            
            token = tokenHandler.MatchAndRemove(Token.TokenType.WORD); 
            if (!token.isPresent()) {
                throw new RuntimeException("Expected function name after FUNCTION token.");
            }
            String functionName = token.get().getValue();
            
            List<String> parameters = new LinkedList<>();
            
            // Check for left parenthesis
            token = tokenHandler.MatchAndRemove(Token.TokenType.LPAREN);
            if (!token.isPresent()) {
                throw new RuntimeException("Expected left parenthesis after function name.");
            }
            
            // Parse parameters
            while (true) {
                token = tokenHandler.Peek(0);
                if (token.isPresent() && token.get().getType() == Token.TokenType.WORD) {
                    parameters.add(token.get().getValue());
                    tokenHandler.MatchAndRemove(Token.TokenType.WORD);
                    
                    token = tokenHandler.Peek(0);
                    if (token.isPresent() && token.get().getType() == Token.TokenType.COMMA) {
                        tokenHandler.MatchAndRemove(Token.TokenType.COMMA);
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            
            // Check for right parenthesis
            token = tokenHandler.MatchAndRemove(Token.TokenType.RPAREN);
            if (!token.isPresent()) {
                throw new RuntimeException("Expected right parenthesis after parameters.");
            }

            // Handle the function body
            token = tokenHandler.MatchAndRemove(Token.TokenType.LBRACE);
            if (!token.isPresent()) {
                throw new RuntimeException("Expected left brace after function parameters.");
            }
            token = tokenHandler.MatchAndRemove(Token.TokenType.RBRACE);
            if (!token.isPresent()) {
                throw new RuntimeException("Expected right brace at the end of function body.");
            }
            
            FunctionDefinitionNode functionDefinition = new FunctionDefinitionNode(functionName, parameters);
            programNode.getFunctionNodes().add(functionDefinition);
            return Optional.of(functionDefinition);
        }
        return Optional.empty();
    }


 // ParseAction method, tries to parse an action and adds it to the ProgramNode
    public Optional<Node> ParseAction(ProgramNode programNode) {
        Optional<Token> token = tokenHandler.Peek(0);
        if (!token.isPresent()) {
            return Optional.empty();
        }

        if (token.get().getType() == Token.TokenType.LBRACE) {
            tokenHandler.MatchAndRemove(Token.TokenType.LBRACE);
            BlockNode blockNode = new BlockNode();
            
            // Continue parsing the content inside the block until a right brace is found
            while (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() != Token.TokenType.RBRACE) {
                Optional<Node> statement = ParseStatement(); // Use a new method to handle different statement types
                if (statement.isPresent()) {
                    blockNode.addStatement(statement.get());
                } else {
                    throw new RuntimeException("Expected statement inside action block.");
                }
            }
            
            tokenHandler.MatchAndRemove(Token.TokenType.RBRACE);
            programNode.getOtherBlockNodes().add(blockNode);
            return Optional.of(blockNode);
        }
        return Optional.empty();
    }


    public Optional<Node> ParseLValue() {
        Optional<Token> token = tokenHandler.Peek(0);

        // Check if there are no more tokens
        if (!token.isPresent()) {
            return Optional.empty();
        }

        Node lvalueNode = null;

        switch (token.get().getType()) {
            case DOLLAR:
                // Handle DOLLAR (variable reference) case
                tokenHandler.MatchAndRemove(Token.TokenType.DOLLAR);
                Optional<Node> valueNode = ParseBottomLevel();
                
                // Create an OperationNode with DOLLAR operation type
                if (valueNode.isPresent()) {
                    lvalueNode = new OperationNode(valueNode.get(), OperationNode.OperationType.DOLLAR);
                }
                break;

            case WORD:
                // Handle WORD (variable or array) case
                String variableName = token.get().getValue();
                tokenHandler.MatchAndRemove(Token.TokenType.WORD);
                
                // Check if it's an array element with an index
                if (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.LBRACKET) {
                    tokenHandler.MatchAndRemove(Token.TokenType.LBRACKET);
                    Optional<Node> indexNode = ParseOperation();
                    
                    // Check for a closing bracket after the array index
                    if (!indexNode.isPresent() || tokenHandler.Peek(0).get().getType() != Token.TokenType.RBRACKET) {
                        throw new RuntimeException("Expected closing bracket after array index.");
                    }
                    tokenHandler.MatchAndRemove(Token.TokenType.RBRACKET);
                    
                    // Create a VariableReferenceNode with an array index
                    lvalueNode = new VariableReferenceNode(variableName, indexNode.get());
                } else {
                    // Create a VariableReferenceNode for a simple variable
                    lvalueNode = new VariableReferenceNode(variableName);
                }
                break;

            default:
                break;
        }

        // If an lvalue was parsed and the next token is an assignment operator, parse the rvalue
        if (lvalueNode != null && tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.EQ) {
            tokenHandler.MatchAndRemove(Token.TokenType.EQ);
            Node rvalue = ParseExpression().orElseThrow(() -> new RuntimeException("Expected expression after '=' in assignment."));
            return Optional.of(new AssignmentNode(lvalueNode, rvalue));
        }

        // Return the parsed lvalue or empty if none of the cases matched
        return Optional.ofNullable(lvalueNode);
    }

    // Parse the ternary operation
    private Optional<Node> ParseTernary() {
        Optional<Node> condition = ParseBooleanCompare();
        if (condition.isPresent() && tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.QUESTION) {
            tokenHandler.MatchAndRemove(Token.TokenType.QUESTION);
            Node trueExpression = ParseExpression().orElseThrow(() -> new RuntimeException("Expected expression after '?' in ternary operation."));
            tokenHandler.MatchAndRemove(Token.TokenType.COLON);
            Node falseExpression = ParseExpression().orElseThrow(() -> new RuntimeException("Expected expression after ':' in ternary operation."));
            return Optional.of(new TernaryNode(condition.get(), trueExpression, falseExpression));
        }
        return condition;
    }
    
 // Parse boolean comparison operations
    private Optional<Node> ParseBooleanCompare() {
        Optional<Node> left = ParseConcatenation();
        if (!left.isPresent()) {
            return Optional.empty();
        }

        if (tokenHandler.Peek(0).isPresent()) {
            Token.TokenType type = tokenHandler.Peek(0).get().getType();
            OperationType opType;

            switch (type) {
                case LT:
                    opType = OperationType.LT;
                    break;
                case LE:
                    opType = OperationType.LE;
                    break;
                case GT:
                    opType = OperationType.GT;
                    break;
                case GE:
                    opType = OperationType.GE;
                    break;
                case EQ:
                    opType = OperationType.EQ;
                    break;
                case NE:
                    opType = OperationType.NE;
                    break;
                default:
                    return left; // If the token isn't a comparison operator, return the left operand
            }

            tokenHandler.MatchAndRemove(type);
            Node right = ParseConcatenation().orElseThrow(() -> new RuntimeException("Expected right operand for boolean comparison."));
            return Optional.of(new OperationNode(left.get(), right, opType));
        }
        return left;
    }

    // Parse concatenation operation
    private Optional<Node> ParseConcatenation() {
        Optional<Node> left = ParseExpression();

        while (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.CONCATENATION) {
            tokenHandler.MatchAndRemove(Token.TokenType.CONCATENATION);
            Node right = ParseExpression().orElseThrow(() -> new RuntimeException("Expected right operand for concatenation."));
            left = Optional.of(new OperationNode(left.get(), right, OperationType.CONCATENATION));
        }

        return left.isPresent() ? left : Optional.empty();
    }

    private Optional<Node> ParseAssignment() {
        Optional<Node> lValue = ParseLValue();
        if (lValue.isPresent() && tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.EQ) {
            tokenHandler.MatchAndRemove(Token.TokenType.EQ);
            Optional<Node> rValue = ParseExpression();
            if (rValue.isPresent()) {
                return Optional.of(new AssignmentNode(lValue.get(), rValue.get()));
            }
        }
        return Optional.empty();
    }

    // Parse basic arithmetic operations// Parse basic arithmetic operations
    private Optional<Node> ParseExpression() {
        Optional<Token> token = tokenHandler.Peek(0);

        // Check for STRINGLITERAL at the beginning
        if (token.isPresent() && token.get().getType() == Token.TokenType.STRINGLITERAL) {
            Node stringLiteralNode = new ConstantNode(token.get().getValue());
            tokenHandler.MatchAndRemove(Token.TokenType.STRINGLITERAL);
            return Optional.of(stringLiteralNode);
        }

        Optional<Node> left = ParseTerm();
        while (tokenHandler.Peek(0).isPresent()) {
            Token.TokenType type = tokenHandler.Peek(0).get().getType();

            OperationType opType;
            switch (type) {
                case PLUS:
                    opType = OperationType.ADD;
                    break;
                case MINUS:
                    opType = OperationType.SUBTRACT;
                    break;
                default:
                    return left;
            }
            tokenHandler.MatchAndRemove(type);
            Node right = ParseTerm().orElseThrow(() -> new RuntimeException("Expected right operand for arithmetic operation."));
            left = Optional.of(new OperationNode(left.get(), right, opType));
        }
        return left;
    }


    private Optional<Node> ParseTerm() {
        // Parse the left operand (a factor)
        Optional<Node> left = ParseFactor();

        // Continue parsing if there are more tokens
        while (tokenHandler.Peek(0).isPresent()) {
            Token.TokenType type = tokenHandler.Peek(0).get().getType();

            OperationType opType;
            switch (type) {
                case ASTERISK:
                    opType = OperationType.MULTIPLY;
                    break;
                case SLASH:
                    opType = OperationType.DIVIDE;
                    break;
                case MODULUS:
                    opType = OperationType.MODULO;
                    break;
                default:
                    // If the current token is not an arithmetic operator, return the left operand
                    return left;
            }

            // Match and remove the current operator token
            tokenHandler.MatchAndRemove(type);

            // Parse the right operand (a factor)
            Node right = ParseFactor().orElseThrow(() -> new RuntimeException("Expected right operand for arithmetic operation."));

            // Create an operation node with the left and right operands and the operation type
            left = Optional.of(new OperationNode(left.get(), right, opType));
        }

        // Return the result, which will be the left operand or a complex expression if there are multiple terms
        return left;
    }

    
    private Optional<Node> ParseFactor() {
        // First, try to match a NUMBER token
        Optional<Token> num = tokenHandler.MatchAndRemove(Token.TokenType.NUMBER);
        if (num.isPresent()) {
            return Optional.of(new ConstantNode(num.get().getValue()));
        }

        // Next, check for expressions enclosed in parentheses
        if (tokenHandler.MatchAndRemove(Token.TokenType.LPAREN).isPresent()) {
            Optional<Node> exp = ParseExpression();
            if (!exp.isPresent()) {
                throw new RuntimeException("Expected expression inside parentheses.");
            }

            if (tokenHandler.MatchAndRemove(Token.TokenType.RPAREN).isEmpty()) {
                throw new RuntimeException("Expected closing parenthesis.");
            }

            return exp;
        }

        // Check for the existing functionality in your ParseFactor method
        Optional<Node> node = ParseExponent();
        if (node.isPresent() && tokenHandler.Peek(0).isPresent()) {
            Token nextToken = tokenHandler.Peek(0).get();
            if (nextToken.getType() == Token.TokenType.INCREMENT) {
                tokenHandler.MatchAndRemove(Token.TokenType.INCREMENT);
                return Optional.of(new OperationNode(node.get(), OperationType.POSTINC));
            }
        }

        if (!node.isPresent() && tokenHandler.Peek(0).isPresent()) {
            Token token = tokenHandler.Peek(0).get();
            if (token.getType() == Token.TokenType.WORD) {
                node = Optional.of(new VariableReferenceNode(token.getValue()));
                tokenHandler.MatchAndRemove(Token.TokenType.WORD);
            }
        }

        if (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.MINUS) {
            tokenHandler.MatchAndRemove(Token.TokenType.MINUS);
            Node rightNode = ParseFactor().orElseThrow(() -> new RuntimeException("Expected expression after MINUS."));
            return Optional.of(new OperationNode(rightNode, OperationType.UNARYNEG));
        }

        return node;
    }

    private Optional<Node> ParseExponent() {
        Optional<Node> left = ParseBottomLevel();

        while (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.EXPONENT) {
            tokenHandler.MatchAndRemove(Token.TokenType.EXPONENT);
            Node right = ParseBottomLevel().orElseThrow(() -> new RuntimeException("Expected right operand for exponentiation."));
            left = Optional.of(new OperationNode(left.get(), right, OperationType.EXPONENT));
        }

        return left;
    }

    
    public Optional<Node> ParseOperation() {
        // Check for PreInc and PreDec
        if (tokenHandler.Peek(0).isPresent()) {
            Token.TokenType type = tokenHandler.Peek(0).get().getType();
            
            switch (type) {
                case INCREMENT:
                    tokenHandler.MatchAndRemove(Token.TokenType.INCREMENT);
                    Optional<Node> postIncNode = ParseLValue();
                    if (!postIncNode.isPresent()) {
                        throw new RuntimeException("Expected a variable after ++.");
                    }
                    return Optional.of(new OperationNode(postIncNode.get(), OperationNode.OperationType.PREINC));

                case DECREMENT:
                    tokenHandler.MatchAndRemove(Token.TokenType.DECREMENT);
                    Optional<Node> postDecNode = ParseLValue();
                    if (!postDecNode.isPresent()) {
                        throw new RuntimeException("Expected a variable after --.");
                    }
                    return Optional.of(new OperationNode(postDecNode.get(), OperationNode.OperationType.PREDEC));

                default:
                    break;
            }
        }

        Optional<Node> node = ParseLValue();  // Parse the lvalue 
        
        if (node.isPresent() && tokenHandler.Peek(0).isPresent()) {
            Token.TokenType type = tokenHandler.Peek(0).get().getType();
            
            switch (type) {
                case ASSIGN:
                    tokenHandler.MatchAndRemove(Token.TokenType.ASSIGN);
                    Optional<Node> rightNode = ParseOperation();
                    if (!rightNode.isPresent()) {
                        throw new RuntimeException("Expected an expression after assignment.");
                    }
                    return Optional.of(new AssignmentNode(node.get(), rightNode.get()));

                case INCREMENT:
                    tokenHandler.MatchAndRemove(Token.TokenType.INCREMENT);
                    return Optional.of(new OperationNode(node.get(), OperationNode.OperationType.POSTINC));

                case DECREMENT:
                    tokenHandler.MatchAndRemove(Token.TokenType.DECREMENT);
                    return Optional.of(new OperationNode(node.get(), OperationNode.OperationType.POSTDEC));

                default:
                    break;
            }
        }

        // Try to parse a function call
        Optional<Node> functionCall = ParseFunctionCall();
        if (functionCall.isPresent()) {
            return functionCall;
        }

        return node;
    }
    
private Optional<Node> ParseFunctionCall() {
    Optional<Token> token = tokenHandler.Peek(0);
    if (token.isPresent() && token.get().getType() == Token.TokenType.IDENTIFIER) {
        String functionName = token.get().getValue();
        tokenHandler.MatchAndRemove(Token.TokenType.IDENTIFIER);

        // Check if the function is one of the special functions that can be called without parenthesis
        if (Arrays.asList("getline", "print", "printf", "exit", "nextfile", "next").contains(functionName)) {
            return Optional.of(new FunctionCallNode(functionName));
        }

        if (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.LPAREN) {
            tokenHandler.MatchAndRemove(Token.TokenType.LPAREN);
            FunctionCallNode functionCallNode = new FunctionCallNode(functionName);

            while (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() != Token.TokenType.RPAREN) {
                Optional<Node> parameter = ParseOperation(); // Assuming ParseOperation() is a method you already have
                if (parameter.isPresent()) {
                    functionCallNode.addParameter(parameter.get());
                }

                if (tokenHandler.Peek(0).isPresent() && tokenHandler.Peek(0).get().getType() == Token.TokenType.COMMA) {
                    tokenHandler.MatchAndRemove(Token.TokenType.COMMA);
                }
            }

            tokenHandler.MatchAndRemove(Token.TokenType.RPAREN);
            return Optional.of(functionCallNode);
        }
    }
    return Optional.empty();
}

    public Optional<Node> ParseBottomLevel() {
        Optional<Token> token = tokenHandler.Peek(0);

        // Check if there are no more tokens
        if (!token.isPresent()) {
            return Optional.empty();
        }

        switch (token.get().getType()) {
           
            case NUMBER:
                // Create a ConstantNode for numbers
                return Optional.of(new ConstantNode(token.get().getValue()));

            case REGEX_LITERAL:
                // Create a PatternNode for regex literals
                return Optional.of(new PatternNode(token.get().getValue()));

            case LPAREN:
                // Handle expressions enclosed in parentheses
                tokenHandler.MatchAndRemove(Token.TokenType.LPAREN);
                Optional<Node> innerOperation = ParseOperation();
                if (!tokenHandler.Peek(0).isPresent() || tokenHandler.Peek(0).get().getType() != Token.TokenType.RPAREN) {
                    throw new RuntimeException("Expected closing parenthesis.");
                }

                tokenHandler.MatchAndRemove(Token.TokenType.RPAREN);
                return innerOperation;

            default:
                Optional<Node> functionCall = ParseFunctionCall();
                if (functionCall.isPresent()) {
                    return functionCall;
                }
                return ParseLValue();
        }
    }


}

