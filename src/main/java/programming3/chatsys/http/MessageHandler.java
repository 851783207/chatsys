package programming3.chatsys.http;

import com.sun.net.httpserver.HttpExchange;
import programming3.chatsys.data.DatabaseAccessException;
import programming3.chatsys.data.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

public class MessageHandler extends AbstractHandler {
    private final HTTPChatServer server;

    public MessageHandler(HTTPChatServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) {
        System.out.println("Got request from client");
        if (exchange.getRequestMethod().equals("POST")) {
            String query = exchange.getRequestURI().getQuery();
            System.out.println(query);
            String split[] = query.split("&");
            if (split.length < 2) {
                sendResponse(exchange, 400, "User information was not formatted properly\n" +
                        "User should be formatted as <username>&<password>");
            } else {
                String username = split[0].split("=")[1];
                String password = split[1].split("=")[1];
                if (this.server.authenticate(username, password)) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
                        String line = reader.readLine();
                        System.out.println(line);
                        this.server.addMessage(username, line);
                        sendResponse(exchange, 201, "Message sent successfully.");
                    } catch (IOException e) {
                        sendResponse(exchange, 400, "Cannot read from request body.");
                    }
                } else {
                    sendResponse(exchange, 401, "Username or password is invalid.");
                }
            }
        } else {
            sendResponse(exchange, 405, "Request method should be POST");
        }
        exchange.close();
    }
}