package fi.mqanaa.weatherapp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles reading and writing JSON data to and from files.
 * This class provides methods for reading JSON content from a file 
 * at program launch and writing JSON content to a file at program close.
 */
public class JsonFileHandler {

    /**
     * Reads the entire content of a file and returns it as a string.
     * 
     * @param fileName the name or path of the file to read.
     * @return a string containing the file's content.
     * @throws IOException if an I/O error occurs reading from the file.
     */
    public String readJsonFromFile(String fileName) throws IOException {
        return Files.readString(Path.of(fileName), StandardCharsets.UTF_8);
    }

    /**
     * Writes the specified JSON data to a file.
     * 
     * @param fileName the name or path of the file to write to.
     * @param jsonData the JSON string to write to the file.
     * @return true if the data was successfully written, false if an error occurred.
     */
    public boolean writeJsonToFile(String fileName, String jsonData) {
        try {
            Files.writeString(Path.of(fileName), jsonData, StandardCharsets.UTF_8);
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        } catch (SecurityException e) {
            System.err.println("Write access denied: " + e.getMessage());
        }
        return false;
    }
}
