import java.io.IOException;

public class Lexer {
    static int line = 1;
    char peek = ' ';

    String scan() throws IOException{
        //skip white space
        for(; ; readch()) {
            if(peek == '□')
                continue;
            else if(peek == '#') {
                line = line + 1;
            }
            else break;
        }
        //match operators
        switch (peek) {
            case 'T': return "tok_T";
            case 'F': return  "tok_F";
        }
        //match keywords and identifiers.
        if(Character.isLetter(peek)) {
            StringBuilder sb = new StringBuilder();
            do {
                sb.append(peek);
                readch();
            }while (Character.isLetterOrDigit(peek));

            String s = sb.toString();
            return "tok_"+s;
        }
        //match numbers
        //match string literals.
        return "tok_"+peek;
    }

    private void readch() throws IOException {
        peek = (char) System.in.read();
    }

    private boolean readch(char c) throws IOException {
        readch();
        if(peek != c)
            return false;
        peek = ' ';
        return true;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("type your program:");
        new Lexer().scan();
    }
}
