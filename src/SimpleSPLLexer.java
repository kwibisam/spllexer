import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class SimpleSPLLexer {
    FileReader fileReader;
    private Hashtable<String,TokenType> words = new Hashtable<>();
//    private final String text;
    private int pos;
    char ch;
    int r;
    private int line;
    private int col;
//    private char c;
    public SimpleSPLLexer(FileReader fileReader) throws IOException {
//        this.text = text;
//        pos = -1;
        line = 1;
        col = 0;

        words.put("if", TokenType.TT_IF);
        words.put("add", TokenType.TT_ADD);
        words.put("sub", TokenType.TT_SUB);
        words.put("mult", TokenType.TT_MULT);
        words.put("and", TokenType.TT_AND);
        words.put("or", TokenType.TT_OR);
        words.put("not", TokenType.TT_NOT);
        words.put("then", TokenType.TT_THEN);
        words.put("else", TokenType.TT_ELSE);
        words.put("while", TokenType.TT_WHILE);
        words.put("for", TokenType.TT_FOR);
        words.put("eq", TokenType.TT_EQ);
        words.put("input", TokenType.TT_INPUT);
        words.put("output", TokenType.TT_OUTPUT);
        words.put("halt", TokenType.TT_HALT);
        words.put("num", TokenType.TT_NUM);
        words.put("bool", TokenType.TT_BOOL);
        words.put("string", TokenType.TT_STRING);
        words.put("proc", TokenType.TT_PROC);
        words.put("T", TokenType.TT_T);
        words.put("F", TokenType.TT_F);
        this.fileReader = fileReader;
        advance();
    }

    public Token scan() throws Exception {
        if(r == -1) {
            return new Token(TokenType.TT_EOF, "EOF");
        }
        //skip white space
//        do {
//            if (ch == '#' ) {
//                line++;
//                col = 0;
//            }
//            advance();
//        }while (ch == '□' || ch == '#' || ch == '\n' || ch == '\r');

        while (ch == '□' || ch == '#' || ch == '\n' || ch == '\r') {
            if (ch == '#' ) {
                line++;
                col = 0;
            }
            advance();
        }

        //identify separators
        switch (ch) {
            case '<':
                advance();
                return new Token("<");
            case '>':
                advance();
                return new Token(">");
            case '(':
                advance();
                return new Token(TokenType.TT_OPEN_PAREN, "(");
            case ')':
                advance();
                return new Token(TokenType.TT_CLOSE_PAREN, ")");
            case '{':
                advance();
                return new Token(TokenType.TT_OPEN_BRACE, "{");
            case '}':
                advance();
                return new Token(TokenType.TT_CLOSE_BRACE, "}");
            case '=':
                advance();
                return new Token("=");
            case ',':
                advance();
                return new Token(",");
            case ';':
                advance();
                return new Token(";");
            case '-': {
                advance();
                if (Character.isDigit(ch)) {
                    retract();
                    return makeIntegerLiteral();
                }
            }
            case '"': {
                return makeStringLiteral();
            }

        }

        //match identifiers
        if(Character.isLetter(ch)) {
            StringBuilder sb = new StringBuilder();
            do {
                sb.append(ch);
                advance();
            }while (Character.isLetterOrDigit(ch) && r != -1);
            String id = sb.toString();
            TokenType word = words.get(id);
            if (word != null) {
                return new Token(word,id);
            }
            return new Token(TokenType.TT_ID, id);
        }
        //match numbers
        if(Character.isDigit(ch)) {
            return makeIntegerLiteral();
        }

        throw new
                Exception(String
                .format("Lexical Error [line: %d, col: %d]: %s isn't a valid character", line, col, Character.toString(ch)));
//        System.out.println("Unknown character: "+ Character.toString(ch) + " int value: "+ r);
//        return new Token(Character.toString(ch));
    }

    //match strings
    private Token makeStringLiteral() throws IOException {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(ch);
            advance();
        }while (Character.isLetterOrDigit(ch) || ch == '□' && r != -1);
        sb.append(ch);
        String literal = sb.toString();
        if (ch == '"') {
            advance();
            return new Token(TokenType.TT_STRING, literal);
        }
        advance();
        return new Token(literal);
    }


    private Token makeIntegerLiteral() throws IOException {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(ch);
            advance();
        }while (Character.isDigit(ch) && r != -1);
        return new Token(TokenType.TT_INT, sb.toString());
    }

    private void advance() throws IOException {
        r = fileReader.read();
        if(r != -1) {
            ch = (char) r;
            col++;
            System.out.print(ch);
        }
//        pos++;
//        if(pos < text.length()) {
//            c = text.charAt(pos);
//            col++;
//        }
    }

    private void retract() throws IOException {
//        pos--;
        fileReader.mark(1);
        col--;
//        c = text.charAt(pos);
        ch = (char) r;
//        System.out.println("retract to: "+ch);
    }

    static public class Token {
        TokenType type;
        String value;
        public Token(String value) {
            this.value = value;
        }

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return value+" ("+type+")";
        }
    }
    public enum TokenType {
        TT_OPEN_PAREN("tok_oparen"),
        TT_CLOSE_PAREN("tok_cparen"),
        TT_OPEN_BRACE("tok_obrace"),
        TT_CLOSE_BRACE("tok_cbrace"),
        TT_HALT("tok_halt"),
        TT_NUM("tok_num"),
        TT_BOOL("tok_bool"),
        TT_EOF("EOF"), TT_F("tok_f"),
        TT_T("tok_t"), TT_PROC("tok_proc"),
        TT_STRING("tok_string"), TT_IF("tok_if"),
        TT_AND("tok_and"), TT_OR("tok_or"), TT_NOT("tok_not"),
        TT_THEN("tok_then"), TT_WHILE("tok_while"),
        TT_FOR("tok_for"), TT_EQ("tok_eq"), TT_INPUT("tok_input"),
        TT_OUTPUT("tok_output"), TT_ID("tok_id"),
        TT_MULT("tok_mult"), TT_SUB("tok_sub"), TT_ADD("tok_add"),
        TT_ELSE("tok_else"), TT_INT("tok_int");
        private final String displayName;
        TokenType(String string) {
            this.displayName = string;
        }

        @Override
        public String toString() {
            return this.displayName;
        }
    }


    public static void main(String[] args) throws Exception {
//        String input = "if□3(□eq□\"□□7\"##{□□add□sub";
//        String input2 = "□(□□□4##{-67□□add□sub";
        List<Token> tokenList = new LinkedList<>();
        FileReader fileReader = new FileReader("input.txt");
        SimpleSPLLexer lexer = new SimpleSPLLexer(fileReader);
        Token token = lexer.scan();
//        tokenList.add(token);
//        while (!Objects.equals(token.value,"EOF")) {
//            System.out.println(token);
//            token = lexer.scan();
//            tokenList.add(token);
//        }
        do {
//            System.out.println(token);
            tokenList.add(token);
            token = lexer.scan();
        }while (!Objects.equals(token.value,"EOF"));
        fileReader.close();

        FileWriter writer = new FileWriter("output.txt");
        for(Token tok: tokenList) {
            writer.write((1+tokenList.indexOf(tok))+": "+tok + "\n");
        }
        writer.close();

    }

}

