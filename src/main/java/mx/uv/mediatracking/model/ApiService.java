package mx.uv.mediatracking.model;

import com.google.gson.Gson;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class ApiService {
    private static final String API_BASE_URL = "https://api.themoviedb.org/3";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public List<Media> searchMedia(String query) throws Exception {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String apiKey = ConfigLoader.getApiKey();
        String urlString = API_BASE_URL + "/search/multi?api_key=" + apiKey + "&query=" + encodedQuery + "&language=es-ES";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            ApiResponse apiResponse = gson.fromJson(response.body(), ApiResponse.class);

            return apiResponse.getResults().stream()
                    .filter(result -> "movie".equals(result.getMediaType()) || "tv".equals(result.getMediaType()))
                    .map(result -> {
                        Media media;
                        if ("movie".equals(result.getMediaType())) {
                            media = new Movie();
                        } else {
                            media = new Serie();
                        }
                        media.setId(result.getId());
                        media.setTitle(result.getTitle());
                        media.setOverview(result.getOverview());
                        media.setPosterPath(result.getPosterPath());
                        return media;
                    })
                    .collect(Collectors.toList());
        } else {
            throw new RuntimeException("Error al conectar con la API: " + response.statusCode());
        }
    }
}