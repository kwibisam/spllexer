import java.io.FileReader;
import java.io.IOException;

public class CharacterReader {
    public static void main(String[] args) {
        try {
            // Replace "file.txt" with the path to your text file
            FileReader fileReader = new FileReader("input.txt");

            int character;
            while ((character = fileReader.read()) != -1) {
                char c = (char) character;
                // Process each character as needed
                System.out.print(c);
            }

            fileReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
