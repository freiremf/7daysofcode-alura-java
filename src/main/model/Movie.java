package main.model;

import java.time.LocalDate;
import java.util.List;

public record Movie (
    List<Long> genre_ids,
    String poster_path,
    LocalDate release_date,
    String title,
    Double vote_average
) implements Content{}
