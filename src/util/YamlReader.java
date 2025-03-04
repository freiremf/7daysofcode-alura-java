package util;

import java.io.BufferedReader;
import java.io.FileReader;

public class YamlReader {
    public static String getProperties(String filePath, String property) {
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                if(line.startsWith(property.concat(":")))
                    return line.split(":")[1].trim();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading file properties", e);
        }
        throw new RuntimeException("Propertie not found");
    }
}
