import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    // Regular expression pattern
    private static final String INTEGER_PATTERN = "(-?[1-9][0-9]*|0)";
    private static final Pattern pattern = Pattern.compile(INTEGER_PATTERN);

    // Function to build integer literals
    public static String buildIntegerLiteral(String input) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            builder.append(c); // Append character to the current string

            // Check if the current string matches the integer pattern
            Matcher matcher = pattern.matcher(builder.toString());
            if (matcher.matches()) {
                // If matched, return the integer literal
                return builder.toString();
            }
        }
        // If no match found, return null
        return null;
    }

    public static void main(String[] args) {
        // Test the function with some input characters
        String input = "int x = 123;"; // Suppose we're reading characters one at a time
        String integerLiteral = buildIntegerLiteral(input);
        if (integerLiteral != null) {
            System.out.println("Integer literal found: " + integerLiteral);
        } else {
            System.out.println("No integer literal found.");
        }
    }
}
