import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

public class WeatherService {

    private static final String API_KEY = "46da6bfb-822b-48ab-b38a-d56ebf0b5e69";

    public static void main(String[] args) {
        String url = "https://api.weather.yandex.ru/v2/forecast?lat=55.78&lon=37.45&limit=3";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("X-Yandex-Weather-Key", API_KEY)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Код статуса: " + response.statusCode());
            System.out.println("Ответ сервера:");
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
