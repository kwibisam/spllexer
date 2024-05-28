import java.io.*;
import java.util.*;

public class SPLLexer {
    // Enum for token types
    enum TokenType {
        KEYWORD, OPERATOR, SEPARATOR, LITERAL, IDENTIFIER
    }

    // Token class to store token information
    static class Token {
        TokenType type;
        String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "(" + type + ": " + value + ")";
        }
    }

    // Set of keywords
    static Set<String> keywords = new HashSet<>(Arrays.asList(
            "and", "or", "not", "add", "sub", "mult", "if", "then", "else",
            "while", "for", "eq", "input", "output", "halt", "num", "bool",
            "string", "proc", "T", "F"
    ));

    // Set of operators and separators
    static Set<Character> operators = new HashSet<>(Arrays.asList('<', '>', '=', ';', ','));
    static Set<Character> separators = new HashSet<>(Arrays.asList('□', '(', ')', '{', '}'));

    // Tokenize method to process the input file
    public static List<Token> tokenize(String filename) throws IOException {
        List<Token> tokens = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        int lineNum = 1;
        int colNum = 0;
        StringBuilder tokenBuilder = new StringBuilder();
        int c;

        while ((c = reader.read()) != -1) {
            char ch = (char) c;
            colNum++;

            if (ch == '\n') {
                lineNum++;
                colNum = 0;
                continue;
            }

            if (separators.contains(ch)) {
                if (ch == '□') {
                    // Skip □ characters within string literals
                    if (tokenBuilder.length() > 0 && tokenBuilder.charAt(0) == '"') {
                        tokenBuilder.append(ch);
                        continue;
                    }
                }
                if (tokenBuilder.length() > 0) {
                    tokens.add(getToken(tokenBuilder.toString()));
                    tokenBuilder.setLength(0);
                }
                tokens.add(new Token(TokenType.SEPARATOR, String.valueOf(ch)));
            } else if (operators.contains(ch)) {
                if (tokenBuilder.length() > 0) {
                    tokens.add(getToken(tokenBuilder.toString()));
                    tokenBuilder.setLength(0);
                }
                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(ch)));
            } else {
                tokenBuilder.append(ch);
            }
        }

        if (tokenBuilder.length() > 0) {
            tokens.add(getToken(tokenBuilder.toString()));
        }

        reader.close();
        return tokens;
    }

    // Method to get token type
    public static Token getToken(String tokenValue) {
        if (keywords.contains(tokenValue)) {
            return new Token(TokenType.KEYWORD, tokenValue);
        } else if (isLiteral(tokenValue)) {
            return new Token(TokenType.LITERAL, tokenValue);
        } else if (isValidIdentifier(tokenValue)) {
            return new Token(TokenType.IDENTIFIER, tokenValue);
        } else {
            // Check if it's an operator
            if (operators.contains(tokenValue.charAt(0))) {
                return new Token(TokenType.OPERATOR, tokenValue);
            } else {
                System.out.println("Lexical Error: Invalid token - " + tokenValue);
                return null;
            }
        }
    }

    // Method to check if a token is a valid literal
    public static boolean isLiteral(String tokenValue) {
        return tokenValue.matches("-?[1-9][0-9]*|0|\"[a-z0-9□]{0,8}\"");
    }

    // Method to check if a token is a valid identifier
    public static boolean isValidIdentifier(String tokenValue) {
        return tokenValue.matches("[a-z][a-z0-9]*") && !keywords.contains(tokenValue);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java SPLLexer <filename>");
            return;
        }

        String filename = args[0];
        try {
            List<Token> tokens = tokenize(filename);
            if (tokens != null) {
                for (int i = 0; i < tokens.size(); i++) {
                    System.out.println((i + 1) + ":" + tokens.get(i));
                }
                // Write tokens to a file
                FileWriter writer = new FileWriter("output.txt");
                for (Token token : tokens) {
                    writer.write(token + "\n");
                }
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
