package Concessionaria.service;

import Concessionaria.model.FipeItem;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiService {
    private static final String BASE_URL = "https://parallelum.com.br/fipe/api/v1/carros";
    private static final HttpClient client = HttpClient.newHttpClient();

    // Método para consumir os endpoints da FIPE
    public static String get(String endpoint) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + endpoint))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            System.out.println("Erro de conexão com a API da FIPE: " + e.getMessage());
            return null;
        }
    }

    // Extrai os itens (código e nome) usando Regex (evita dependências externas de JSON)
    public static List<FipeItem> extrairItens(String json) {
        List<FipeItem> itens = new ArrayList<>();
        if (json == null) return itens;

        Pattern pattern = Pattern.compile("\"codigo\"\\s*:\\s*\"?([^\",}]+)\"?\\s*,\\s*\"nome\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        while (matcher.find()) {
            itens.add(new FipeItem(matcher.group(1), matcher.group(2)));
        }
        return itens;
    }

    // Isola apenas os modelos para evitar misturar com os "anos" no retorno de modelos
    public static List<FipeItem> extrairModelos(String json) {
        if (json == null) return new ArrayList<>();
        int anosIndex = json.indexOf("\"anos\"");
        String modelosJson = anosIndex != -1 ? json.substring(0, anosIndex) : json;
        return extrairItens(modelosJson);
    }

    // Extrai o valor do veículo na tabela FIPE e converte para Double
    public static Double extrairValorFipe(String json) {
        if (json == null) return 0.0;
        Pattern pattern = Pattern.compile("\"Valor\"\\s*:\\s*\"R\\$\\s*([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            String valorStr = matcher.group(1).replace(".", "").replace(",", ".");
            return Double.parseDouble(valorStr);
        }
        return 0.0;
    }
}