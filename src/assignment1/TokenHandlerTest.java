package assignment1;

import org.junit.Before;
import org.junit.Test;
import java.util.LinkedList;
import java.util.Optional;
import static org.junit.Assert.*;

public class TokenHandlerTest {

    private TokenHandler tokenHandler;

    @Before
    public void setUp() {
        // Initialize a TokenHandler with a LinkedList of tokens for testing.
        LinkedList<Token> tokens = new LinkedList<>();
        tokens.add(new Token(Token.TokenType.WORD, "test"));
        tokens.add(new Token(Token.TokenType.SEPARATOR, ";"));
        tokenHandler = new TokenHandler(tokens);
    }

    @Test
    public void testPeek() {
        // Test the Peek() method to ensure it returns the expected token.
        Optional<Token> token = tokenHandler.Peek(0);
        assertTrue(token.isPresent());
        assertEquals(Token.TokenType.WORD, token.get().getType());
        assertEquals("test", token.get().getValue());
    }

    @Test
    public void testMoreTokens() {
        // Test the MoreTokens() method to check if there are more tokens.
        assertTrue(tokenHandler.MoreTokens());

        // Match and remove tokens, and then check if there are more tokens.
        tokenHandler.MatchAndRemove(Token.TokenType.WORD);
        tokenHandler.MatchAndRemove(Token.TokenType.SEPARATOR);
        assertFalse(tokenHandler.MoreTokens());
    }

    @Test
    public void testMatchAndRemove() {
        // Test the MatchAndRemove() method to ensure it matches and removes the expected token.
        Optional<Token> token = tokenHandler.MatchAndRemove(Token.TokenType.WORD);
        assertTrue(token.isPresent());
        assertEquals(Token.TokenType.WORD, token.get().getType());
        assertEquals("test", token.get().getValue());

        // Try to match and remove the same token again, which should return Optional.empty().
        assertFalse(tokenHandler.MatchAndRemove(Token.TokenType.WORD).isPresent());
    }
}

