package main.converter;

import main.model.Content;

import java.util.List;

public interface Converter {
    List<? extends Content> parse(String json);
}
