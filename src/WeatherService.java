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

        // Получаем ответ
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Код статуса: " + response.statusCode());
            System.out.println("Ответ сервера:");
            System.out.println(response.body());

            //Создаем маппер и превращаем ответ в дерево JsonNode
            ObjectMapper mapper = new ObjectMapper();

            JsonNode rootNode = mapper.readTree(response.body());

            // Парсим текущую температуру
            int CurrentTemp = rootNode.path("fact").path("temp").asInt();

            System.out.println("Текущая температура: " + CurrentTemp + "°C");

            //Считаем среднюю температуру по прогнозу
            //Превращаем массив forecasts в дерево JsonNode
            JsonNode forecastsNode = rootNode.path("forecasts");

            double TotalSum = 0; // Сумма
            int count = 0; // Счетчик значений

            if (forecastsNode.isArray()) {
                for (JsonNode dayForecast : forecastsNode) {
                    JsonNode tempAvgNode = dayForecast.path("parts").path("day").path("temp_avg");

                    if (!tempAvgNode.isMissingNode()) {
                        TotalSum += tempAvgNode.asDouble();
                        count++;
                    }
                }
            }

            if (count > 0) {
                double averageTemp = TotalSum / count;
                System.out.println("Средняя температура на " + count + " дн.: " + averageTemp + "°C");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
