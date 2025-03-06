import model.Movie;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MovieConverter {
    public static List<Movie> parseMovie(String json) {
        List<Movie> movies = new ArrayList<>();
        String begin = "results\":[{";
        int start = json.indexOf(begin) + begin.length();
        int end = json.indexOf("}],\"total_pages");
        String body = json.substring(start, end).trim();
        String[] movieBlocks = body.split("},\\{");

        for(String block : movieBlocks) {
            Movie movie = new Movie();
            block = block.replace("{", "").replace("}", "").trim();

            String[] attributes = block.split(",(?=\\s*\"[^\"]+\":)");

            for(String attribute : attributes) {
                attribute = attribute.trim();
                if(attribute.startsWith("\"adult\"")) {
                    movie.setAdult(extractBoolean(attribute));
                    continue;
                }
                if(attribute.startsWith("\"video\"")) {
                    movie.setVideo(extractBoolean(attribute));
                    continue;
                }
                if(attribute.startsWith("\"original_language\"")) {
                    movie.setOriginal_language(extractString(attribute));
                    continue;
                }
                if(attribute.startsWith("\"original_title\"")) {
                    movie.setOriginal_title(extractString(attribute));
                    continue;
                }
                if(attribute.startsWith("\"overview\"")) {
                    movie.setOverview(extractString(attribute));
                    continue;
                }
                if(attribute.startsWith("\"backdrop_path\"")) {
                    movie.setOverview(extractString(attribute));
                    continue;
                }
                if(attribute.startsWith("\"poster_path\"")) {
                    movie.setPoster_path(extractString(attribute));
                    continue;
                }
                if(attribute.startsWith("\"title\"")) {
                    movie.setTitle(extractString(attribute));
                    continue;
                }
                if(attribute.startsWith("\"popularity\"")) {
                    movie.setPopularity(extractDouble(attribute));
                    continue;
                }
                if(attribute.startsWith("\"vote_average\"")) {
                    movie.setVote_average(extractDouble(attribute));
                    continue;
                }
                if(attribute.startsWith("\"vote_count\"")) {
                    movie.setVote_count(extractLong(attribute));
                    continue;
                }
                if(attribute.startsWith("\"id\"")) {
                    movie.setId(extractLong(attribute));
                    continue;
                }
                if(attribute.startsWith("\"release_date\"")) {
                    movie.setRelease_date(extractDate(attribute));
                    continue;
                }
                if(attribute.startsWith("\"genre_ids\"")) {
                    movie.setGenre_ids(extractLongList(attribute));
                }
            }
            movies.add(movie);
        }
        return movies;
    }

    private static Boolean extractBoolean(String json) {
        if(json.contains("true")) {
            return Boolean.TRUE;
        }
        if(json.contains("false")) {
            return Boolean.FALSE;
        }
        return null;
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

    private static Long extractLong(String json) {
        int start = json.indexOf(":") + 1;
        String value = json.substring(start).trim();
        return Long.parseLong(value);
    }

    private static LocalDate extractDate(String json) {
        int start = json.indexOf(":") + 1;
        String value = json.substring(start).trim();
        value = value.replaceAll("\"", "");
        if(value.length() != 10)
            return null;
        return LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
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
