package assignment1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Awk {

    public static void main(String[] args) throws Exception {
        // Read the entire file into a single string
        Path myPath = Paths.get("/Users/tommychu/Desktop/SomeFile.txt");
        String content = new String(Files.readAllBytes(myPath));
        try {
            Lexer l = new Lexer();
            String[] lines = content.split("\n");
            for (String line : lines) {
                l.lex(line);
            }

            // Print each token out once the lexing is complete
            for (Token tokens : l.tokens) {
                if (tokens.getType() == Token.TokenType.SEPARATOR)
                    System.out.println(Token.TokenType.SEPARATOR);
                else
                    System.out.print(tokens.toString());
            }

            // Parse the tokens using the Parser
            Parser parser = new Parser(new LinkedList<>(l.tokens));
            ProgramNode program = parser.Parse();

        } catch (Exception e) {
            System.out.println("There was an exception: " + e.getMessage());
        }
    }
}


