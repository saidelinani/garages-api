package com.renault.garagesapi.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonReader {

    public static String loadJsonFromResources(String filename) throws IOException {
        try (InputStream inputStream = JsonReader.class.getResourceAsStream("/json/" + filename)) {
            if (inputStream == null) {
                throw new FileNotFoundException("File JSON not found: /json/" + filename);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public static String loadJsonWithPlaceholders(String filename, Map<String, String> placeholders) throws IOException {
        String json = loadJsonFromResources(filename);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            json = json.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return json;
    }
}
