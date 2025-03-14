package main.client;

import main.model.Content;

import java.util.List;

public interface ApiClient<T extends Content> {
    List<T> getBody();
    Class<T> getClassType();
    String getType();
}
