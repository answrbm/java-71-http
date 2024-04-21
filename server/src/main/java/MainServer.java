import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class MainServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080),0);
        server.createContext("/products",MainServer::handleProducts);
        server.start();
    }

    private static void handleProducts(HttpExchange exchange) throws IOException {
        if(exchange.getRequestMethod().equals("GET")) {
            exchange.sendResponseHeaders(200,0);
            try(OutputStream os = exchange.getResponseBody()) {
                os.write("Products list".getBytes());
            }
        } else {
            exchange.sendResponseHeaders(405,0);
            try(OutputStream os = exchange.getResponseBody()) {
                os.write("Method not allowed".getBytes());
            }
        }
    }

}
