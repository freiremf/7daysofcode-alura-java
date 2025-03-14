package main.converter;

import main.client.MarvelApiClient;
import main.model.Comic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComicsConverter implements Converter {
    @Override
    public List<Comic> parse(String json) {
        List<Comic> comics = new ArrayList<>();
        String begin = "results\":[{";
        int start = json.indexOf(begin) + begin.length() - 1;
        int end = json.indexOf("}]}}") + 1;

        String body = json.substring(start, end).trim();
        List<String> comicsList = splitJson(body);

        for(String block : comicsList) {
            block = block.replace("{", "").replace("}", "").trim();

            String[] attributes = block.split(",(?=\\s*\"[^\"]+\":)");

            Map<String, String> attributeMap = new HashMap<>();

            for(String attribute : attributes) {
                attribute = attribute.trim();

                String[] keyValue = attribute.split(":", 2);
                if(keyValue.length == 2)
                    attributeMap.put(keyValue[0].replace("\"", "").trim(), keyValue[1].trim());
            }

            String poster = extractString(attributeMap.get("thumbnail")) + "." + extractString(attributeMap.get("extension"));

            MarvelApiClient client = new MarvelApiClient();

            Comic comic = new Comic(
                    null,
                    poster,
                    extractDate(attributeMap.get("modified")),
                    extractString(attributeMap.get("title")),
                    null,
                    client.getType().toString()
            );
            comics.add(comic);
        }
        return comics;
    }

    private static String extractString(String json) {
        int start = json.indexOf(":") + 1;
        String value = json.substring(start).trim();
        return value.replaceAll("\"", "").replaceAll("]", "");
    }

    private static Double extractDouble(String json) {
        int start = json.indexOf(":") + 1;
        String value = json.substring(start).trim();
        return Double.parseDouble(value);
    }

    private static LocalDateTime extractDate(String json) {
        String value = json.replaceAll("\"", "");
        if(value.length() != 24)
            return null;
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
    }
}
