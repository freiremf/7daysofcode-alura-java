package converter;

import model.Content;

import java.util.List;

public interface Converter {
    List<? extends Content> parse(String json);
}
