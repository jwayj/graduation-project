package com.graphhopper.example;


import java.io.FileWriter;
import java.io.IOException;


public class SaveGeoJson {
    public static void saveToFile(String geoJson, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(geoJson);
        }
    }
}
