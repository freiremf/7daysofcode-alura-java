package main.client;

import main.model.Comic;
import main.util.Md5Generator;
import main.util.YamlReader;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class MarvelApiClient implements ApiClient<Comic> {

    @Override
    public List<Comic> getBody  () {
        long timeStamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        String hash = Md5Generator.toMD5(timeStamp + getPrivateKey() + getPublicKey());
        String urlMarvel = "http://gateway.marvel.com/v1/public/comics?ts=" + timeStamp + "&apikey=" + getPublicKey()  + "&hash=" + hash;

        return List.of();
    }

    @Override
    public Class<Comic> getType() {
        return Comic.class;
    }

    private String getPublicKey() {
        return YamlReader.getProperties("src/resources/properties.yml", "publicKey");
    }

    private String getPrivateKey() {
        return YamlReader.getProperties("src/resources/properties.yml", "privateKey");
    }
}
