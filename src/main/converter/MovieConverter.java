package main.converter;

import main.client.TmdbApiClient;
import main.model.Movie;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieConverter implements Converter {
    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";

    @Override
    public List<Movie> parse(String json) {
        List<Movie> movies = new ArrayList<>();
        String begin = "results\":[{";
        int start = json.indexOf(begin) + begin.length() - 1;
        int end = json.indexOf("}],\"total_pages") + 1;

        String body = json.substring(start, end).trim();
        List<String> moviesList = splitJson(body);

        for(String block : moviesList) {
            block = block.replace("{", "").replace("}", "").trim();

            String[] attributes = block.split(",(?=\\s*\"[^\"]+\":)");

            Map<String, String> attributeMap = new HashMap<>();

            for(String attribute : attributes) {
                attribute = attribute.trim();

                String[] keyValue = attribute.split(":", 2);
                if(keyValue.length == 2)
                    attributeMap.put(keyValue[0].replace("\"", "").trim(), keyValue[1].trim());
                }

            String poster = POSTER_BASE_URL + extractString(attributeMap.get("poster_path"));

            TmdbApiClient tmdbApiClient = new TmdbApiClient();

            Movie movie = new Movie(
                    extractLongList(attributeMap.get("genre_ids")),
                    poster,
                    extractDate(attributeMap.get("release_date")),
                    extractString(attributeMap.get("title")),
                    extractDouble(attributeMap.get("vote_average")),
                    tmdbApiClient.getType()
            );
            movies.add(movie);
        }
        return movies;
    }

    private static String extractString(String json) {
        int start = json.indexOf(":") + 1;
        String value = json.substring(start).trim();
        return value.replaceAll("\"", "");
    }

    private static Double extractDouble(String json) {
        int start = json.indexOf(":") + 1;
        String value = json.substring(start).trim();
        return Double.parseDouble(value);
    }

    private static LocalDateTime extractDate(String json) {
        int start = json.indexOf(":") + 1;
        String value = json.substring(start).trim();
        value = value.replaceAll("\"", "");
        if(value.length() != 10)
            return null;
        return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
    }

    private static List<Long> extractLongList(String json) {
        List<Long> list = new ArrayList<>();
        int start = json.indexOf("[") + 1;
        int end = json.indexOf("]");
        if (start > 0 && end > start) {
            String[] values = json.substring(start, end).split(",");
            for (String v : values) {
                list.add(Long.parseLong(v.trim()));
            }
        }
        return list;
    }
}
