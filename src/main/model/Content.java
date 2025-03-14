package main.model;

import java.time.LocalDateTime;
import java.util.List;

public interface Content {
    List<Long> genre_ids();
    String poster();
    LocalDateTime releaseDate();
    String title();
    Double rating();
    String contentType();
}
