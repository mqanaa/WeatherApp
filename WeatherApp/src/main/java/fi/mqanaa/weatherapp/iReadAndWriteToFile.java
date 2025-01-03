package fi.mqanaa.weatherapp;

/**
 * Interface with methods to read from a file and write to a file.
 */
public interface iReadAndWriteToFile {

    /**
     * Reads JSON from the given file.
     * @param fileName name of the file to read from.
     * @return the file contents as a String.
     * @throws Exception if the method e.g, cannot find the file.
     */
    public String readFromFile(String fileName) throws Exception;

    /**
     * Writes the JSON data into the given file.
     * @param fileName name of the file to write to.
     * @param data the contents that should be written into the file.
     * @return true if the write was successful, otherwise false.
     * @throws Exception if the method e.g., cannot write to a file.
     */
    public boolean writeToFile(String fileName, String data) throws Exception;
}
