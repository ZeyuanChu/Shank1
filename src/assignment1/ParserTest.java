package assignment1;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.LinkedList;

public class ParserTest {

    private Parser parser;
    private LinkedList<Token> tokens;

    @Before
    public void setUp() {
        tokens = new LinkedList<>();
        parser = new Parser(tokens);
    }

    @Test
    public void testAcceptSeparatorsWithValidSeparators() {
        tokens.add(new Token(Token.TokenType.SEPARATOR, ";"));
        tokens.add(new Token(Token.TokenType.SEPARATOR, "\n"));
        assertTrue(parser.AcceptSeparators());
    }


    @Test(expected = RuntimeException.class)
    public void testParseBottomLevelWithInvalidIncrement() {
        tokens.add(new Token(Token.TokenType.INCREMENT, "++"));
        tokens.add(new Token(Token.TokenType.SEPARATOR, ";"));
        parser.Parse();
    }

    @Test
    public void testParseLValueWithDollar() {
        tokens.add(new Token(Token.TokenType.DOLLAR, "$"));
        tokens.add(new Token(Token.TokenType.WORD, "a"));
        ProgramNode programNode = parser.Parse();
        assertEquals(1, programNode.getOtherBlockNodes().size());
    }

    @Test(expected = RuntimeException.class)
    public void testParseLValueWithInvalidDollar() {
        tokens.add(new Token(Token.TokenType.DOLLAR, "$"));
        tokens.add(new Token(Token.TokenType.SEPARATOR, ";"));
        parser.Parse();
    }
    

    @Test
    public void testParsePattern() {
        tokens.add(new Token(Token.TokenType.LBRACKET, "["));
        tokens.add(new Token(Token.TokenType.WORD, "abc"));
        tokens.add(new Token(Token.TokenType.RBRACKET, "]"));
        ProgramNode programNode = parser.Parse();
        assertEquals(1, programNode.getOtherBlockNodes().size());
    }

  
    @Test
    public void testParseFunctionWithParameters() {
        tokens.add(new Token(Token.TokenType.FUNCTION, "function"));
        tokens.add(new Token(Token.TokenType.WORD, "functionName"));
        tokens.add(new Token(Token.TokenType.LPAREN, "("));
        tokens.add(new Token(Token.TokenType.WORD, "param1"));
        tokens.add(new Token(Token.TokenType.COMMA, ","));
        tokens.add(new Token(Token.TokenType.WORD, "param2"));
        tokens.add(new Token(Token.TokenType.RPAREN, ")"));
        tokens.add(new Token(Token.TokenType.LBRACE, "{"));
        tokens.add(new Token(Token.TokenType.RBRACE, "}"));
        ProgramNode programNode = parser.Parse();
        assertEquals(1, programNode.getFunctionNodes().size());
        assertEquals(2, programNode.getFunctionNodes().get(0).getParameters().size());
    }
    
    @Test
    public void testParsePostIncrement() {
        tokens.add(new Token(Token.TokenType.WORD, "a"));
        tokens.add(new Token(Token.TokenType.INCREMENT, "++"));
        ProgramNode programNode = parser.Parse();
        BlockNode firstBlock = programNode.getOtherBlockNodes().get(0);
        Node firstStatement = firstBlock.getStatements().get(0);
        assertTrue(firstStatement instanceof OperationNode);
        assertEquals(OperationNode.OperationType.POSTINC, ((OperationNode) firstStatement).getOperationType());
    }
    
	@Test
	public void testParseFactor() {
	    tokens.add(new Token(Token.TokenType.MINUS, "-"));
	    tokens.add(new Token(Token.TokenType.NUMBER, "5"));
	    ProgramNode programNode = parser.Parse();
	    Node firstStatement = programNode.getOtherBlockNodes().get(0).getStatements().get(0);
	    assertTrue(firstStatement instanceof OperationNode);
	    assertEquals(OperationNode.OperationType.UNARYNEG, ((OperationNode) firstStatement).getOperationType());
	}

	@Test
	public void testParseTerm() {
	    tokens.add(new Token(Token.TokenType.NUMBER, "5"));
	    tokens.add(new Token(Token.TokenType.ASTERISK, "*"));
	    tokens.add(new Token(Token.TokenType.NUMBER, "3"));
	    ProgramNode programNode = parser.Parse();
	    Node firstStatement = programNode.getOtherBlockNodes().get(0).getStatements().get(0);
	    assertTrue(firstStatement instanceof OperationNode);
	    assertEquals(OperationNode.OperationType.MULTIPLY, ((OperationNode) firstStatement).getOperationType());
	}

	@Test
	public void testParseExpression() {
	    tokens.add(new Token(Token.TokenType.NUMBER, "5"));
	    tokens.add(new Token(Token.TokenType.PLUS, "+"));
	    tokens.add(new Token(Token.TokenType.NUMBER, "3"));
	    ProgramNode programNode = parser.Parse();
	    Node firstStatement = programNode.getOtherBlockNodes().get(0).getStatements().get(0);
	    assertTrue(firstStatement instanceof OperationNode);
	    assertEquals(OperationNode.OperationType.ADD, ((OperationNode) firstStatement).getOperationType());
	}

	@Test
	public void testParseConcatenation() {
	    tokens.add(new Token(Token.TokenType.STRINGLITERAL, "\"Hello\""));
	    tokens.add(new Token(Token.TokenType.CONCATENATION, "."));
	    tokens.add(new Token(Token.TokenType.STRINGLITERAL, "\"World\""));
	    ProgramNode programNode = parser.Parse();
	    Node firstStatement = programNode.getOtherBlockNodes().get(0).getStatements().get(0);
	    assertTrue(firstStatement instanceof OperationNode);
	    assertEquals(OperationNode.OperationType.CONCATENATION, ((OperationNode) firstStatement).getOperationType());
	}

	@Test
	public void testParseExponent() {
	    tokens.add(new Token(Token.TokenType.NUMBER, "2"));
	    tokens.add(new Token(Token.TokenType.EXPONENT, "^"));
	    tokens.add(new Token(Token.TokenType.NUMBER, "3"));
	    ProgramNode programNode = parser.Parse();
	    BlockNode firstBlock = programNode.getOtherBlockNodes().get(0);
	    Node firstStatement = firstBlock.getStatements().get(0); 
	    assertTrue(firstStatement instanceof OperationNode);
	    assertEquals(OperationNode.OperationType.EXPONENT, ((OperationNode) firstStatement).getOperationType());
	}

	@Test
	public void testParseBooleanCompare() {
	    tokens.add(new Token(Token.TokenType.NUMBER, "5"));
	    tokens.add(new Token(Token.TokenType.LT, "<"));
	    tokens.add(new Token(Token.TokenType.NUMBER, "10"));
	    ProgramNode programNode = parser.Parse();
	    Node firstStatement = programNode.getOtherBlockNodes().get(0).getStatements().get(0);
	    assertTrue(firstStatement instanceof OperationNode);
	    assertEquals(OperationNode.OperationType.LT, ((OperationNode) firstStatement).getOperationType());
	}

	@Test
	public void testParseTernary() {
	    tokens.add(new Token(Token.TokenType.NUMBER, "5"));
	    tokens.add(new Token(Token.TokenType.LT, "<"));
	    tokens.add(new Token(Token.TokenType.NUMBER, "10"));
	    tokens.add(new Token(Token.TokenType.QUESTION, "?"));
	    tokens.add(new Token(Token.TokenType.NUMBER, "1"));
	    tokens.add(new Token(Token.TokenType.COLON, ":"));
	    tokens.add(new Token(Token.TokenType.NUMBER, "0"));
	    ProgramNode programNode = parser.Parse();
	    Node firstStatement = programNode.getOtherBlockNodes().get(0).getStatements().get(0);
	    assertTrue(firstStatement instanceof TernaryNode);
	}


    @Test
    public void testParseAssignment() {
        tokens.add(new Token(Token.TokenType.WORD, "a"));
        tokens.add(new Token(Token.TokenType.EQ, "="));
        tokens.add(new Token(Token.TokenType.NUMBER, "5"));
        ProgramNode programNode = parser.Parse();
        BlockNode firstBlock = programNode.getOtherBlockNodes().get(0);
        Node firstStatement = firstBlock.getStatements().get(0); 
        assertTrue(firstStatement instanceof AssignmentNode);
    }
    
    @Test
    public void testParseDeleteWithArrayName() {
        tokens.add(new Token(Token.TokenType.DELETE, "delete"));
        tokens.add(new Token(Token.TokenType.IDENTIFIER, "arrayName"));
        ProgramNode programNode = parser.Parse();
        BlockNode firstBlock = programNode.getOtherBlockNodes().get(0);
        Node firstStatement = firstBlock.getStatements().get(0);
        assertTrue(firstStatement instanceof DeleteNode);
        assertEquals("arrayName", ((DeleteNode) firstStatement).getArrayName());
    }

    @Test
    public void testParseDeleteWithIndices() {
        tokens.add(new Token(Token.TokenType.DELETE, "delete"));
        tokens.add(new Token(Token.TokenType.IDENTIFIER, "arrayName"));
        tokens.add(new Token(Token.TokenType.LBRACKET, "["));
        tokens.add(new Token(Token.TokenType.NUMBER, "1"));
        tokens.add(new Token(Token.TokenType.COMMA, ","));
        tokens.add(new Token(Token.TokenType.NUMBER, "2"));
        tokens.add(new Token(Token.TokenType.RBRACKET, "]"));
        ProgramNode programNode = parser.Parse();
        BlockNode firstBlock = programNode.getOtherBlockNodes().get(0);
        Node firstStatement = firstBlock.getStatements().get(0);
        assertTrue(firstStatement instanceof DeleteNode);
        assertEquals(2, ((DeleteNode) firstStatement).getIndices().size());
    }

    @Test
    public void testParseDoWhile() {
        tokens.add(new Token(Token.TokenType.DO, "do"));
        tokens.add(new Token(Token.TokenType.LBRACE, "{"));
        tokens.add(new Token(Token.TokenType.RBRACE, "}"));
        tokens.add(new Token(Token.TokenType.WHILE, "while"));
        tokens.add(new Token(Token.TokenType.LPAREN, "("));
        tokens.add(new Token(Token.TokenType.NUMBER, "1"));
        tokens.add(new Token(Token.TokenType.RPAREN, ")"));
        ProgramNode programNode = parser.Parse();
        BlockNode firstBlock = programNode.getOtherBlockNodes().get(0);
        Node firstStatement = firstBlock.getStatements().get(0);
        assertTrue(firstStatement instanceof DoWhileNode);
    }

    @Test
    public void testParseReturn() {
        tokens.add(new Token(Token.TokenType.RETURN, "return"));
        tokens.add(new Token(Token.TokenType.NUMBER, "5"));
        ProgramNode programNode = parser.Parse();
        BlockNode firstBlock = programNode.getOtherBlockNodes().get(0);
        Node firstStatement = firstBlock.getStatements().get(0);
        assertTrue(firstStatement instanceof ReturnNode);
    }

    @Test
    public void testParseContinue() {
        tokens.add(new Token(Token.TokenType.CONTINUE, "continue"));
        ProgramNode programNode = parser.Parse();
        BlockNode firstBlock = programNode.getOtherBlockNodes().get(0);
        Node firstStatement = firstBlock.getStatements().get(0);
        assertTrue(firstStatement instanceof ContinueNode);
    }

    @Test
    public void testParseBreak() {
        tokens.add(new Token(Token.TokenType.BREAK, "break"));
        ProgramNode programNode = parser.Parse();
        BlockNode firstBlock = programNode.getOtherBlockNodes().get(0);
        Node firstStatement = firstBlock.getStatements().get(0);
        assertTrue(firstStatement instanceof BreakNode);
    }
    
    @Test
    public void testParseIf() {
        tokens.add(new Token(Token.TokenType.IF, "if"));
        tokens.add(new Token(Token.TokenType.LPAREN, "("));
        tokens.add(new Token(Token.TokenType.NUMBER, "1"));
        tokens.add(new Token(Token.TokenType.RPAREN, ")"));
        tokens.add(new Token(Token.TokenType.LBRACE, "{"));
        tokens.add(new Token(Token.TokenType.RBRACE, "}"));
        ProgramNode programNode = parser.Parse();
        assertTrue("ProgramNode should have blocks", !programNode.getOtherBlockNodes().isEmpty());
        BlockNode firstBlock = programNode.getOtherBlockNodes().get(0);
        assertTrue("First block should have statements", !firstBlock.getStatements().isEmpty());
        Node firstStatement = firstBlock.getStatements().get(0);
        assertTrue(firstStatement instanceof IfNode);
        assertTrue("First statement should be an instance of WhileNode", firstStatement instanceof WhileNode);
    }

    @Test
    public void testParseWhile() {
        tokens.add(new Token(Token.TokenType.WHILE, "while"));
        tokens.add(new Token(Token.TokenType.LPAREN, "("));
        tokens.add(new Token(Token.TokenType.NUMBER, "1"));
        tokens.add(new Token(Token.TokenType.RPAREN, ")"));
        tokens.add(new Token(Token.TokenType.LBRACE, "{"));
        tokens.add(new Token(Token.TokenType.RBRACE, "}"));
        ProgramNode programNode = parser.Parse();

        // Check if programNode has any blocks
        assertTrue("ProgramNode should have blocks", !programNode.getOtherBlockNodes().isEmpty());
        BlockNode firstBlock = programNode.getOtherBlockNodes().get(0);

        // Check if firstBlock has any statements
        assertTrue("First block should have statements", !firstBlock.getStatements().isEmpty());
        Node firstStatement = firstBlock.getStatements().get(0);

        assertTrue("First statement should be an instance of WhileNode", firstStatement instanceof WhileNode);
    }

    
}

