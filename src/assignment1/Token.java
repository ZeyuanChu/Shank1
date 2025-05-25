package assignment1;

public class Token {
    public enum TokenType {
        WORD, NUMBER, 
        WHILE, IF, DO, FOR, BREAK, CONTINUE, ELSE, RETURN, 
        BEGIN, END, PRINT, PRINTF, NEXT, IN, DELETE, GETLINE, 
        EXIT, NEXTFILE, FUNCTION, STRINGLITERAL, REGEX_LITERAL,
        GE,  // >=
        INCREMENT,  // ++
        DECREMENT,  // --
        LE,  // <=
        EQ,  // ==
        NE,  // !=
        EXPONENT_ASSIGN,  // ^=
        MOD_ASSIGN,  // %=
        MUL_ASSIGN,  // *=
        DIV_ASSIGN,  // /=
        ADD_ASSIGN,  // +=
        SUB_ASSIGN,  // -=
        NOT_MATCH,  // !~
        AND,  // &&
        APPEND,  // >>
        OR, // ||
        LBRACE,  // {
        RBRACE,  // }
        LBRACKET,  // [
        RBRACKET,  // ]
        LPAREN,  // (
        RPAREN,  // )
        DOLLAR,  // $
        TILDE,  // ~
        ASSIGN,  // =
        LT,  // <
        GT,  // >
        NOT,  // !
        PLUS,  // +
        CARET,  // ^
        MINUS,  // -
        QUESTION,  // ?
        COLON,  // :
        ASTERISK,  // *
        SLASH,  // /
        MODULUS,  // %
        SEPARATOR,  // ; and \n
        PIPE,  // |
        COMMA,  // ,
        MATCH, NOTMATCH,
        PREINC, POSTINC, PREDEC, POSTDEC, UNARYPOS, UNARYNEG,
        EXPONENT, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, CONCATENATION, PATTERN, UNKNOWN, ELSEIF, IDENTIFIER, SEMICOLON,
    }

    private TokenType type;  // The type of the token (WORD, NUMBER, SEPARATOR, etc.)
    private String value;    // The value associated with the token

    // Constructor to create a new Token instance with a given type and value
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    // Returns the type of the token
    public TokenType getType() {
        return this.type;
    }

    // Returns the value associated with the token
    public String getValue() {
        return this.value;
    }

    // Overrides the toString method to provide a human-readable representation of the token
    @Override
    public String toString() {
        if (this.type == TokenType.SEPARATOR) {
            return "SEPARATOR";
        } else if (this.value == null) {
            return this.type.toString();  // For keywords, just return the type
        } else {
            return this.type + "(" + this.value + ") ";
        }
    }
}
