package fi.mqanaa.weatherapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;


/**
 * Class used to read from a json-file at program launch
 * and write to the same file at program close.
 */
public class JsonFileReaderWriter implements iReadAndWriteToFile {

    @Override
    public String readFromFile(String fileName) throws Exception {
        
        String fileContents = "";
        
        // Read weather file contents into a string variable
        try (var input = new Scanner(new File(fileName))) {
            while(input.hasNextLine()) {
                String line = input.nextLine();
                fileContents += line;
            }
        } catch (FileNotFoundException e) {
            throw new Exception();
        }
        return fileContents;
    }

    @Override
    public boolean writeToFile(String fileName, String jsonData) {
        try (var output = new PrintStream(fileName)) {
            output.print(jsonData);
            return true;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    
}
