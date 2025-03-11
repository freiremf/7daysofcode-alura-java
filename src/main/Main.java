package main;

import main.client.TmdbApiClient;
import main.model.Movie;
import main.view.HtmlGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TmdbApiClient tmdbApiClient = new TmdbApiClient();
        List<Movie> movies = tmdbApiClient.getBody();

        try (PrintWriter writer = new PrintWriter(new FileWriter("filmes.html"))) {
            HtmlGenerator generator = new HtmlGenerator(writer);
            movies = movies.stream().sorted(Comparator.comparing(Movie::vote_average).reversed()).toList();
            generator.generate(movies);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar HTML: " + e.getMessage());
        }


    }
}
