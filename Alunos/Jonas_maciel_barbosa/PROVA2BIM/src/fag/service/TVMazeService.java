package fag.service;

import fag.exceptions.ApiException;
import fag.json.JsonParser;
import fag.model.Series;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TVMazeService {

    private static final String BASE_URL = "https://api.tvmaze.com";

    private final HttpClient httpClient;

    public TVMazeService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @SuppressWarnings("unchecked")
    public List<Series> searchShows(String query) throws ApiException {
        if (query == null || query.trim().isEmpty()) {
            throw new ApiException("O termo de busca nao pode ser vazio.");
        }
        String encodedQuery = URLEncoder.encode(query.trim(), StandardCharsets.UTF_8);
        String url = BASE_URL + "/search/shows?q=" + encodedQuery;

        String body = doGet(url);

        Object parsed;
        try {
            parsed = JsonParser.parse(body);
        } catch (Exception ex) {
            throw new ApiException("A resposta da API do TVMaze nao pode ser interpretada.", ex);
        }

        List<Series> results = new ArrayList<>();
        if (parsed instanceof List) {
            for (Object item : (List<Object>) parsed) {
                if (item instanceof Map) {
                    Object showObj = ((Map<String, Object>) item).get("show");
                    if (showObj instanceof Map) {
                        try {
                            results.add(Series.fromApiMap((Map<String, Object>) showObj));
                        } catch (Exception ex) {

                        }
                    }
                }
            }
        }
        return results;
    }

    private String doGet(String url) throws ApiException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                return "[]";
            }
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new ApiException("A API do TVMaze retornou um erro HTTP " + response.statusCode() + ".");
            }
            return response.body();
        } catch (IOException ex) {
            throw new ApiException("Nao foi possivel conectar a API do TVMaze. Verifique sua conexao com a internet.", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new ApiException("A requisicao a API do TVMaze foi interrompida.", ex);
        } catch (IllegalArgumentException ex) {
            throw new ApiException("URL invalida ao consultar a API do TVMaze.", ex);
        }
    }
}
