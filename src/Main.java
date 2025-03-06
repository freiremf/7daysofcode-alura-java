import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import model.Movie;
import util.YamlReader;

public class Main {
    public static void main(String[] args) {

        String url = "https://api.themoviedb.org/3/search/movie?query=matrix&include_adult=false&language=en-US&page=1";

        String apiKey = YamlReader.getProperties("src/resources/properties.yml", "apiToken");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Resposta da API: " + response.body());
            List<Movie> movies = MovieConverter.parseMovie(response.body());
            System.out.println(movies.size() + " filmes encontrados!");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter os dados do API: " + e.getMessage());
        }
    }
}
