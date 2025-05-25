package assignment1;

import java.util.LinkedList;
import java.util.Optional;

public class TokenHandler {
    private LinkedList<Token> tokens;

    // Constructor to initialize the TokenHandler with a list of tokens
    public TokenHandler(LinkedList<Token> tokens) {
        this.tokens = tokens;
    }

    // Peek at the token at position j (0-based index) without removing it
    public Optional<Token> Peek(int j) {
        if (j < 0 || j >= tokens.size()) {
            return Optional.empty(); // Return empty if the index is out of bounds
        }
        return Optional.of(tokens.get(j));
    }

    // Check if there are more tokens in the list
    public boolean MoreTokens() {
        return !tokens.isEmpty();
    }

    // Match and remove a token of a specific type from the front of the list
    public Optional<Token> MatchAndRemove(Token.TokenType t) {
        if (!tokens.isEmpty() && tokens.getFirst().getType() == t) {
            return Optional.of(tokens.removeFirst()); // Remove and return the matched token
        }
        return Optional.empty(); // Return empty if the expected token type is not found
    }
}


