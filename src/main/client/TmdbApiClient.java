package main.client;

import main.model.Movie;
import main.converter.MovieConverter;
import main.util.YamlReader;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TmdbApiClient implements ApiClient<Movie> {
    private static final String URL = "https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1";

    @Override
    public List<Movie> getBody() {
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

    @Override
    public Class<Movie> getType() {
        return Movie.class;
    }

    private String getApiKey() {
        return YamlReader.getProperties("src/resources/properties.yml", "apiToken");
    }
}
