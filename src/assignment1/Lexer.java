package assignment1;

import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {
    ArrayList<Token> tokens;  // ArrayList to store the tokens extracted from the input content
    StringHandler stringHandler;  // StringHandler to manage the input content
    int lineNumber = 1;  // Current line number being processed
    int position = 0;  // Current position in the input content

    // HashMap to store the keywords and their corresponding token types
    private HashMap<String, Token.TokenType> keywordMap;
    private HashMap<String, Token.TokenType> twoCharSymbols;
    private HashMap<String, Token.TokenType> singleCharSymbols;
    
    public Lexer() {
        this.tokens = new ArrayList<Token>();
        initializeKeywordMap();  // Initialize the keyword map
        initializeTwoCharSymbols();  // Initialize the two-character symbols map
        initializeSingleCharSymbols();  // Initialize the single-character symbols map

    }

    // Helper method to initialize the keyword map
    private void initializeKeywordMap() {
        keywordMap = new HashMap<>();
        keywordMap.put("while", Token.TokenType.WHILE);
        keywordMap.put("if", Token.TokenType.IF);
        keywordMap.put("do", Token.TokenType.DO);
        keywordMap.put("for", Token.TokenType.FOR);
        keywordMap.put("break", Token.TokenType.BREAK);
        keywordMap.put("continue", Token.TokenType.CONTINUE);
        keywordMap.put("else", Token.TokenType.ELSE);
        keywordMap.put("return", Token.TokenType.RETURN);
        keywordMap.put("BEGIN", Token.TokenType.BEGIN);
        keywordMap.put("END", Token.TokenType.END);
        keywordMap.put("print", Token.TokenType.PRINT);
        keywordMap.put("printf", Token.TokenType.PRINTF);
        keywordMap.put("next", Token.TokenType.NEXT);
        keywordMap.put("in", Token.TokenType.IN);
        keywordMap.put("delete", Token.TokenType.DELETE);
        keywordMap.put("getline", Token.TokenType.GETLINE);
        keywordMap.put("exit", Token.TokenType.EXIT);
        keywordMap.put("nextfile", Token.TokenType.NEXTFILE);
        keywordMap.put("function", Token.TokenType.FUNCTION);
    }
    
 // Helper method to initialize the two-character symbols map
    private void initializeTwoCharSymbols() {
        twoCharSymbols = new HashMap<>();
        twoCharSymbols.put(">=", Token.TokenType.GE);
        twoCharSymbols.put("++", Token.TokenType.INCREMENT);
        twoCharSymbols.put("--", Token.TokenType.DECREMENT);
        twoCharSymbols.put("<=", Token.TokenType.LE);
        twoCharSymbols.put("==", Token.TokenType.EQ);
        twoCharSymbols.put("!=", Token.TokenType.NE);
        twoCharSymbols.put("^=", Token.TokenType.EXPONENT_ASSIGN);
        twoCharSymbols.put("%=", Token.TokenType.MOD_ASSIGN);
        twoCharSymbols.put("*=", Token.TokenType.MUL_ASSIGN);
        twoCharSymbols.put("/=", Token.TokenType.DIV_ASSIGN);
        twoCharSymbols.put("+=", Token.TokenType.ADD_ASSIGN);
        twoCharSymbols.put("-=", Token.TokenType.SUB_ASSIGN);
        twoCharSymbols.put("!~", Token.TokenType.NOT_MATCH);
        twoCharSymbols.put("&&", Token.TokenType.AND);
        twoCharSymbols.put(">>", Token.TokenType.APPEND);
        twoCharSymbols.put("||", Token.TokenType.OR);
    }

 // Helper method to initialize the single-character symbols map
    private void initializeSingleCharSymbols() {
        singleCharSymbols = new HashMap<>();
        singleCharSymbols.put("{", Token.TokenType.LBRACE);
        singleCharSymbols.put("}", Token.TokenType.RBRACE);
        singleCharSymbols.put("[", Token.TokenType.LBRACKET);
        singleCharSymbols.put("]", Token.TokenType.RBRACKET);
        singleCharSymbols.put("(", Token.TokenType.LPAREN);
        singleCharSymbols.put(")", Token.TokenType.RPAREN);
        singleCharSymbols.put("$", Token.TokenType.DOLLAR);
        singleCharSymbols.put("~", Token.TokenType.TILDE);
        singleCharSymbols.put("=", Token.TokenType.ASSIGN);
        singleCharSymbols.put("<", Token.TokenType.LT);
        singleCharSymbols.put(">", Token.TokenType.GT);
        singleCharSymbols.put("!", Token.TokenType.NOT);
        singleCharSymbols.put("+", Token.TokenType.PLUS);
        singleCharSymbols.put("^", Token.TokenType.CARET);
        singleCharSymbols.put("-", Token.TokenType.MINUS);
        singleCharSymbols.put("?", Token.TokenType.QUESTION);
        singleCharSymbols.put(":", Token.TokenType.COLON);
        singleCharSymbols.put("*", Token.TokenType.ASTERISK);
        singleCharSymbols.put("/", Token.TokenType.SLASH);
        singleCharSymbols.put("%", Token.TokenType.MODULUS);
        singleCharSymbols.put(";", Token.TokenType.SEPARATOR);
        singleCharSymbols.put("\n", Token.TokenType.SEPARATOR);
        singleCharSymbols.put("|", Token.TokenType.PIPE);
        singleCharSymbols.put(",", Token.TokenType.COMMA);
    }
    
    public void lex(String content) {
        this.stringHandler = new StringHandler(content);
        char c;
        while (!stringHandler.isEndOfDocument()) {  // Continue processing until end of document is reached
            c = stringHandler.peek(0);  // Peek at the next character without consuming it

            if (c == '#') {  
                while (c != '\n' && !stringHandler.isEndOfDocument()) {  // Skip characters until the end of the line
                    c = stringHandler.getNextChar();
                }
            } else if (Character.isLetter(c)) {
                processWord();  // Process a sequence of letters as a word token
            } else if (Character.isDigit(c) || c == '-') {
                processNumber();  // Process a sequence of digits as a number token
            } else if (c == '\n') {
                tokens.add(new Token(Token.TokenType.SEPARATOR, "\n"));  // Add a newline separator token
                stringHandler.getNextChar();  // Consume the newline character
            } else if (c == ' ') {
                stringHandler.getNextChar();  // Consume the space character without adding a token
            } else if (c == '"') {
                handleStringLiteral();
            } else if (c == '`') {
                handlePattern();
            } else {
                Token symbolToken = processSymbol();  // Process the symbol
                if (symbolToken != null) {
                    tokens.add(symbolToken);  // If a token is returned, add it to the list
                } else {
                    stringHandler.getNextChar();  // If no token is returned, consume the character
                }
            }
        }
     // Add a SEPARATOR only if the last token in the tokens list is not a SEPARATOR
        if (tokens.size() > 0 && tokens.get(tokens.size() - 1).getType() != Token.TokenType.SEPARATOR) {
            tokens.add(new Token(Token.TokenType.SEPARATOR, "\n"));  // Add a final newline separator token
        }
    }

 // This method returns a string of length 'len' starting from the current position
    private String peekString(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(stringHandler.peek(i));
        }
        return sb.toString();
    }

    private Token processSymbol() {
        String twoCharSymbol = peekString(2);
        if (twoCharSymbols.containsKey(twoCharSymbol)) {
            position += 2;  // Update the position by 2 characters
            stringHandler.getNextChar();  // Consume the first character
            stringHandler.getNextChar();  // Consume the second character
            return new Token(twoCharSymbols.get(twoCharSymbol), null);
        }

        String oneCharSymbol = peekString(1);
        if (singleCharSymbols.containsKey(oneCharSymbol)) {
            position++;  // Update the position by 1 character
            stringHandler.getNextChar();  // Consume the character
            return new Token(singleCharSymbols.get(oneCharSymbol), null);
        }
        stringHandler.getNextChar();
        return new Token(Token.TokenType.UNKNOWN, oneCharSymbol);
        
    }


    private void handleStringLiteral() {
        StringBuilder stringLiteral = new StringBuilder();
        char c = stringHandler.getNextChar();  // Consume the opening quote

        while (!stringHandler.isEndOfDocument()) {
            c = stringHandler.getNextChar();

            // If encounter a backslash, it might be an escape sequence
            if (c == '\\') {
                char nextChar = stringHandler.peek(0);
                if (nextChar == '"') {  // If it's an escaped quote
                    stringLiteral.append('"');
                    stringHandler.getNextChar();  // Consume the quote
                } else {
                    stringLiteral.append(c);  // Otherwise, just append the backslash
                }
            } else if (c == '"') {  // End of the string literal
                break;
            } else {
                stringLiteral.append(c);
            }
        }

        tokens.add(new Token(Token.TokenType.STRINGLITERAL, stringLiteral.toString()));
    }

    private void processWord() {
        StringBuilder word = new StringBuilder();
        char c;

        while (!stringHandler.isEndOfDocument() && Character.isLetterOrDigit(stringHandler.peek(0))) {
            c = stringHandler.getNextChar();  // Consume the character
            word.append(c);  // Append the character to the word
            position++;
        }

        // Check if the word is a known keyword
        if (keywordMap.containsKey(word.toString().toLowerCase())) {
            tokens.add(new Token(keywordMap.get(word.toString().toLowerCase()), null));  // Add the keyword token to the list
        } else {
            tokens.add(new Token(Token.TokenType.WORD, word.toString()));  // Add the word token to the list
        }
    }
    
    private void handlePattern() {
        StringBuilder pattern = new StringBuilder();
        char c = stringHandler.getNextChar();  // Consume the opening backtick

        while (!stringHandler.isEndOfDocument()) {
            c = stringHandler.getNextChar();

            if (c == '\\') {  // Handle escape sequences
                char nextChar = stringHandler.peek(0);
                if (nextChar == '`') {  // If it's an escaped backtick
                    pattern.append('`');
                    stringHandler.getNextChar();  // Consume the backtick
                } else {
                    pattern.append(c);  // Otherwise, just append the backslash
                }
            } else if (c == '`') {  // End of the pattern
                break;
            } else {
                pattern.append(c);
            }
        }

        tokens.add(new Token(Token.TokenType.REGEX_LITERAL, pattern.toString()));
    }
    
    private void processNumber() {
        StringBuilder number = new StringBuilder();
        char c;

        // If the current character is a negative sign and the next character is a number, it is processed as a negative number
        if (stringHandler.peek(0) == '-' && Character.isDigit(stringHandler.peek(1))) {
            c = stringHandler.getNextChar();  // Consume the '-'
            number.append(c);  // Append '-' to the number
            position++;
        }

        while (!stringHandler.isEndOfDocument() && (Character.isDigit(stringHandler.peek(0)) || (stringHandler.peek(0) == '.' && Character.isDigit(stringHandler.peek(1))))) {
            c = stringHandler.getNextChar();  // Consume the character
            number.append(c);  // Append the character to the number
            position++;
        }

        tokens.add(new Token(Token.TokenType.NUMBER, number.toString()));  // Add the number token to the list
    }

}

