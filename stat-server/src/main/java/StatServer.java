import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class StatServer {

    private static int counter;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(7070),0);
        server.createContext("/products/stats",StatServer::handleProductsStats);
        server.start();
    }

    private static void handleProductsStats(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().equals("GET")) {
            counter++;
            exchange.sendResponseHeaders(200,0);
            try(OutputStream os = exchange.getResponseBody()) {
                os.write(("Visitors count: " + counter).getBytes());
            }
        } else {
            exchange.sendResponseHeaders(405,0);
            try(OutputStream os = exchange.getResponseBody()) {
                os.write("Method not allowed".getBytes());
            }
        }
    }

}
