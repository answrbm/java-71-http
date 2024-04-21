import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StatServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(7070),0);
        server.createContext("/products/stats",StatServer::handleProductsStats);
        server.start();
    }

    private static void handleProductsStats(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().equals("GET")) {

            HttpClient client = HttpClient.newHttpClient();

            URI uri = URI.create("http://localhost:8080/products");

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();

            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

            try {
                HttpResponse<String> response = client.send(request, handler);

                System.out.println("Response code: " + response.statusCode());
                System.out.println("Response body: " + response.body());

                exchange.sendResponseHeaders(200,0);
                try(OutputStream os = exchange.getResponseBody()) {
                    os.write(response.body().getBytes());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            exchange.sendResponseHeaders(405,0);
            try(OutputStream os = exchange.getResponseBody()) {
                os.write("Method not allowed".getBytes());
            }
        }



    }

}
