package main.converter;

import main.model.Content;

import java.util.ArrayList;
import java.util.List;

public interface Converter {
    List<? extends Content> parse(String json);

    default List<String> splitJson(String json) {
        List<String> blocks = new ArrayList<>();
        int start = 0;
        int openBraces = 0;
        boolean insideComic = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '{') {
                openBraces++;
                if (!insideComic) {
                    insideComic = true;
                    start = i;
                }
            } else if (c == '}') {
                openBraces--;
                if (openBraces == 0 && insideComic) {
                    blocks.add(json.substring(start, i + 1));
                    insideComic = false;
                }
            }
        }
        return blocks;
    }
}
