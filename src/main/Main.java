package main;

import main.client.MarvelApiClient;
import main.client.TmdbApiClient;
import main.model.Comic;
import main.model.Content;
import main.model.Movie;
import main.view.HtmlGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        TmdbApiClient tmdbApiClient = new TmdbApiClient();
        List<Movie> movies = tmdbApiClient.getBody();

        MarvelApiClient marvelApiClient = new MarvelApiClient();
        List<Comic> comics = marvelApiClient.getBody();

        List<? extends Content> list = Stream
                .concat(movies.stream(), comics.stream())
                .map(record -> (Content) record)
                .toList();

        try (PrintWriter writer = new PrintWriter(new FileWriter("filmes.html"))) {
            HtmlGenerator generator = new HtmlGenerator(writer);
            generator.generate(list);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao gerar HTML: " + e.getMessage());
        }
    }
}
