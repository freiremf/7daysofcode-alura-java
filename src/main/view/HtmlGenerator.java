package main.view;

import main.model.Content;

import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

public class HtmlGenerator {
    private final PrintWriter writer;

    public HtmlGenerator (PrintWriter writer) {
        this.writer = writer;
    }

    public void generate(List<? extends Content> content) {
        writer.write(getHeader());
        writer.write("<body><div class='container'><div class='row'>");
        content.forEach(movie -> writer.write(getMovieCard(movie)));
        writer.write("</div></div></body></html>");
        writer.flush();
    }

    private String getHeader() {
        return
                """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset=\"utf-8\">
                    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">
                    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css\" 
                        + "integrity=\"sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm\" crossorigin=\"anonymous\">					
                </head>
                """;
    }

    private String getMovieCard(Content content) {

        String year = Optional.ofNullable(content.releaseDate())
                .map(date -> String.valueOf(date.getYear()))
                .orElse("Ano não disponível");
        return
                """
                <div class=\"card text-white bg-dark mb-3\" style=\"max-width: 18rem;\">
                    <h4 class=\"card-header\">%s</h4>
                    <div class=\"card-body\">
                        <p class=\"card-text\">%s</p>
                        <img class=\"card-img\" src=\"%s\" alt=\"%s\">
                        <p class=\"card-text mt-2\">Nota: %s - Ano: %s</p>
                    </div>
                </div>
                """.formatted(content.title(), content.contentType(), content.poster(), content.title(), content.rating(), year);
    }
}
