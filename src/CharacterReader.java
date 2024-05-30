import java.io.*;

public class CharacterReader {
    public static void main(String[] args) {
        try {
            // Replace "file.txt" with the path to your text file
            BufferedReader bufferedReader = new BufferedReader(new FileReader("input.txt"));
            int character;
            while ((character = bufferedReader.read()) != -1) {
                char c = (char) character;
                // Process each character as needed
                System.out.print(c);
            }

            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
