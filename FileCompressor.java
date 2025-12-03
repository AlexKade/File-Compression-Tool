import java.io.*;
import java.util.Scanner;

public class FileCompressor {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the file path: ");
            String filePath = scanner.nextLine();

            File inputFile = new File(filePath);
            if (!inputFile.exists()) {
                System.out.println("File does not exist.");
                return;
            }

            System.out.print("Compress (C) or Decompress (D)? ");
            String choice = scanner.nextLine().trim().toUpperCase();

            if (!choice.equals("C") && !choice.equals("D")) {
                System.out.println("Invalid choice.");
                return;
            }

            try {
                String content = readFile(filePath);
                String outputContent;
                String outputFilePath;

                if (choice.equals("C")) {
                    outputContent = compress(content);
                    outputFilePath = filePath + ".rle";
                } else {
                    outputContent = decompress(content);
                    if (filePath.endsWith(".rle")) {
                        outputFilePath = filePath.substring(0, filePath.length() - 4) + "_decompressed.txt";
                    } else {
                        outputFilePath = filePath + "_decompressed.txt";
                    }
                }

                writeFile(outputFilePath, outputContent);
                System.out.println("Operation completed successfully. Output file: " + outputFilePath);

            } catch (IOException e) {
                System.out.println("Error reading/writing file: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Error during decompression: " + e.getMessage());
            }
        }
    }

    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (FileReader reader = new FileReader(filePath)) {
            int c;
            while ((c = reader.read()) != -1) {
                content.append((char) c);
            }
        }
        return content.toString();
    }

    private static void writeFile(String filePath, String content) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
    }

    public static String compress(String input) {
        if (input.isEmpty()||input==null){ return "";}
    
        StringBuilder compressed = new StringBuilder();
        char currentChar = input.charAt(0);
        int count = 1;
    
        for (int i = 1; i < input.length(); i++) {
            if (input.charAt(i) == currentChar) {
                count++;
            } else {
                // Append count ONLY if > 1
                if (count > 1) {
                    compressed.append(count);
                }
                compressed.append(currentChar);
                currentChar = input.charAt(i);
                count = 1;
            }
        }
    
        // Handle the last character
        if (count > 1) {
            compressed.append(count);
        }
        compressed.append(currentChar);
    
        return compressed.toString();
    }

    public static String decompress(String input) {
        StringBuilder decompressed = new StringBuilder();
        int i = 0;
    
        while (i < input.length()) {
            // Check if current position is a digit
            if (Character.isDigit(input.charAt(i))) {
                // Extract the full count (e.g., "12" from "12A")
                int j = i;
                while (j < input.length() && Character.isDigit(input.charAt(j))) {
                    j++;
                }
                int count = Integer.parseInt(input.substring(i, j));
    
                if (j >= input.length()) {
                    throw new IllegalArgumentException("Missing character after count at position " + j);
                }
    
                char c = input.charAt(j);
                for (int k = 0; k < count; k++) {
                    decompressed.append(c);
                }
    
                i = j + 1; // Move past the character
            } else {
                // No count: append the character once
                decompressed.append(input.charAt(i));
                i++;
            }
        }
    
        return decompressed.toString();
    }

}
