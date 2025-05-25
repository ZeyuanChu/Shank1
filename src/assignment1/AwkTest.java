package assignment1;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AwkTest {

    private Lexer lexer;

    @Before
    public void setUp() {
        lexer = new Lexer();
    }

    @Test
    public void testLexWithWord() {
        lexer.lex("goodbye");
        assertEquals(2, lexer.tokens.size());
        assertEquals(Token.TokenType.WORD, lexer.tokens.get(0).getType());
        assertEquals("goodbye", lexer.tokens.get(0).getValue());
    }

    @Test
    public void testLexWithNumber() {
        lexer.lex("321");
        assertEquals(2, lexer.tokens.size());
        assertEquals(Token.TokenType.NUMBER, lexer.tokens.get(0).getType());
        assertEquals("321", lexer.tokens.get(0).getValue());
    }

    @Test
    public void testLexWithFloatingNumber() {
        lexer.lex("5.23");
        assertEquals(2, lexer.tokens.size());
        assertEquals(Token.TokenType.NUMBER, lexer.tokens.get(0).getType());
        assertEquals("5.23", lexer.tokens.get(0).getValue());
    }

    @Test
    public void testLexWithSeparator() {
        lexer.lex("\n");
        assertEquals(2, lexer.tokens.size());
        assertEquals(Token.TokenType.SEPARATOR, lexer.tokens.get(0).getType());
    }    
    
    @Test
    public void testStringHandlerPeek() {
        StringHandler handler = new StringHandler("test");
        assertEquals('e', handler.peek(1));
    }

    @Test
    public void testStringHandlerGetNextChar() {
        StringHandler handler = new StringHandler("test");
        assertEquals('t', handler.getNextChar());
        assertEquals('e', handler.getNextChar());
    }

    @Test
    public void testStringHandlerIsEndOfDocument() {
        StringHandler handler = new StringHandler("t");
        handler.getNextChar();
        assertTrue(handler.isEndOfDocument());
    }
    
    @Test
    public void testLexWithStringLiteral() {
        lexer.lex("\"hello world\"");
        assertEquals(2, lexer.tokens.size());
        assertEquals(Token.TokenType.STRINGLITERAL, lexer.tokens.get(0).getType());
        assertEquals("hello world", lexer.tokens.get(0).getValue());
    }
    
    @Test
    public void testLexWithPattern() {
        lexer.lex("`pattern`");
        assertEquals(2, lexer.tokens.size());
        assertEquals(Token.TokenType.REGEX_LITERAL, lexer.tokens.get(0).getType());
        assertEquals("pattern", lexer.tokens.get(0).getValue());
    }

    @Test
    public void testLexWithSymbols() {
        lexer.lex("+= >= --");
        assertEquals(4, lexer.tokens.size());
        assertEquals(Token.TokenType.ADD_ASSIGN, lexer.tokens.get(0).getType());
        assertEquals(Token.TokenType.GE, lexer.tokens.get(1).getType());
        assertEquals(Token.TokenType.DECREMENT, lexer.tokens.get(2).getType());
    }
    
    @Test
    public void testLexWithTwoCharSymbols() {
        lexer.lex(">= &&");
        assertEquals(3, lexer.tokens.size());
        assertEquals(Token.TokenType.GE, lexer.tokens.get(0).getType());
        assertEquals(Token.TokenType.AND, lexer.tokens.get(1).getType());
    }

    @Test
    public void testLexWithSingleCharSymbols() {
        lexer.lex("{ }");
        assertEquals(3, lexer.tokens.size());
        assertEquals(Token.TokenType.LBRACE, lexer.tokens.get(0).getType());
        assertEquals(Token.TokenType.RBRACE, lexer.tokens.get(1).getType());
    }
    
}


