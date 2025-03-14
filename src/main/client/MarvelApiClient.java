package main.client;

import main.converter.ComicsConverter;
import main.model.Comic;
import main.util.Md5Generator;
import main.util.YamlReader;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class MarvelApiClient implements ApiClient<Comic> {
    private static final String URL = "http://gateway.marvel.com/v1/public/comics?ts=";
    private static final String LIMIT = "5";
    @Override
    public List<Comic> getBody  () {
        long timeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        String hash = Md5Generator.toMD5(timeStamp + getPrivateKey() + getPublicKey());
        String urlMarvel = URL + timeStamp + "&apikey=" + getPublicKey()  + "&hash=" + hash + "&limit=" + LIMIT;

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(urlMarvel))
                .GET()
                .timeout(Duration.ofSeconds(10))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200)
                throw new RuntimeException(response.body());
            return new ComicsConverter().parse(response.body());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter os dados do API Marvel: " + e.getMessage());
        }
    }

    @Override
    public Class<Comic> getClassType() {
        return Comic.class;
    }

    @Override
    public String getType() {
        return "Comic";
    }

    private String getPublicKey() {
        return YamlReader.getProperties("src/resources/properties.yml", "publicKey");
    }

    private String getPrivateKey() {
        return YamlReader.getProperties("src/resources/properties.yml", "privateKey");
    }
}
