import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;

import model.Movie;
import util.YamlReader;
import view.HtmlGenerator;

public class Main {
    public static void main(String[] args) {
        String url = "https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1";

        String apiKey = YamlReader.getProperties("src/resources/properties.yml", "apiToken");

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .GET()
                .build();

        List<Movie> movies;

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200)
                throw new RuntimeException(response.body());
            System.out.println(response.body());
            movies = MovieConverter.parseMovie(response.body());
            System.out.println(movies.size() + " filmes encontrados!");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter os dados do API: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("filmes.html"))) {
            HtmlGenerator generator = new HtmlGenerator(writer);
            movies = movies.stream().sorted(Comparator.comparing(Movie::getVote_average).reversed()).toList();
            generator.generate(movies);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar HTML: " + e.getMessage());
        }
    }
}
