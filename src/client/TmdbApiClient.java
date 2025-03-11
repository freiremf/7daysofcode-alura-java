package client;

import model.Movie;
import converter.MovieConverter;
import util.YamlReader;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TmdbApiClient {
    private static final String URL = "https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1";

    public List<Movie> getMovies() {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + getApiKey())
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200)
                throw new RuntimeException(response.body());
            return new MovieConverter().parse(response.body());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter os dados do API: " + e.getMessage());
        }
    }

    private String getApiKey() {
        return YamlReader.getProperties("src/resources/properties.yml", "apiToken");
    }
}
