
import java.util.Scanner;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CurrencyRates {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите дату в формате dd/mm/yyyy: ");
        String date = scanner.nextLine();
        System.out.println("Введите код валюты (например, USD): ");
        String currencyCode = scanner.nextLine();
        scanner.close();

        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("https://www.cbr.ru/scripts/XML_daily.asp?date_req=" + date + ".xml");
            CloseableHttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            String json = EntityUtils.toString(entity);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);

            JsonNode currencyNode = rootNode.findPath(currencyCode.toUpperCase());
            if (currencyNode.isMissingNode()) {
                System.out.println("Валюта с таким кодом не найдена.");
            } else {
                String currencyName = currencyNode.findPath("name").asText();
                double exchangeRate = currencyNode.findPath("value").asDouble();
                System.out.println("Курс " + currencyName + " на " + date + ": " + exchangeRate);
            }
            response.close();
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

