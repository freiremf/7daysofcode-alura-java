package model;

import java.time.LocalDate;
import java.util.List;

public interface Content {
    List<Long> genre_ids();
    String poster_path();
    LocalDate release_date();
    String title();
    Double vote_average();
}
