package programming3.chatsys.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import programming3.chatsys.data.ChatMessage;
import programming3.chatsys.data.DatabaseAccessException;
import programming3.chatsys.data.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.List;

public class UnreadMessagesHandler extends AbstractHandler {
    protected final HTTPChatServer server;

    public UnreadMessagesHandler(HTTPChatServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) {
        System.out.println("Got request from client");
        if (exchange.getRequestMethod().equals("GET")) {
                String username = "";
                String password = "";
                String query = exchange.getRequestURI().getQuery();
                System.out.println(query);
                String split[] = query.split("&");
                if (split.length < 2) {
                    sendResponse(exchange, 400, "User information was not formatted properly\n"
                            + "User should be formatted as <username>&<password>");
                }else{
                    username = split[0].split("=")[1];
                    password = split[1].split("=")[1];
                }
                try {
                    if (this.server.authenticate(username, password)) {
                        sendMessages(exchange ,this.server.getUnreadMessages(username));
                    } else {
                        sendResponse(exchange, 401, "Wrong username or password.");
                    }
                } catch (DatabaseAccessException e) {
                    sendResponse(exchange, 500, "Database cannot be accessed.");
                }
        } else {
            sendResponse(exchange, 405, "Request method should be GET");
        }
        exchange.close();
    }

    protected void sendMessages(HttpExchange exchange, List<ChatMessage> messages) {
        String response = "";
        for (ChatMessage message: messages) {
            response += message.getId() + "\t" + message.getMessage() + "\t";
            response += message.getUser() + "\t" + message.getTimestamp().getTime() + "\r\n";
        }
        this.sendResponse(exchange, 200, response);
    }
}
