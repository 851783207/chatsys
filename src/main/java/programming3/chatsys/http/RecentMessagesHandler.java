package programming3.chatsys.http;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import programming3.chatsys.data.ChatMessage;
import programming3.chatsys.data.DatabaseAccessException;

import java.io.*;
import java.net.URI;
import java.util.List;

public class RecentMessagesHandler extends AbstractHandler {
    protected final HTTPChatServer server;

    public RecentMessagesHandler(HTTPChatServer server) {
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) {
        System.out.println("Got request from client");
        if (exchange.getRequestMethod().equals("GET")) {
            try {
                sendMessages(exchange, parseN(exchange));
            } catch(NumberFormatException e) {
                sendResponse(exchange, 400, "Number of chat messages should be provided in URL");
            }
        } else {
            sendResponse(exchange, 405, "Request method should be GET");
        }
    }

    private void sendMessages(HttpExchange exchange, int n) {
        if (n > 0) {
            List<ChatMessage> messages = this.server.getRecentMessages(n);
            try {
                sendMessages(exchange, messages);
            } catch(DatabaseAccessException e) {
                sendResponse(exchange, 500, "Database cannot be accessed.");
            }
        } else {
            sendResponse(exchange, 400, "Number of chat messages should be > 0");
        }
    }

    protected void sendMessages(HttpExchange exchange, List<ChatMessage> messages) {
        String response = "";
        for (ChatMessage message: messages) {
            response += message.getId() + "\t" + message.getMessage() + "\t";
            response += message.getUser() + "\t" + message.getTimestamp().getTime() + "\r\n";
        }
        this.sendResponse(exchange, 200, response);
    }

    protected int parseN(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String context = exchange.getHttpContext().getPath();
        return Integer.parseInt(path.substring(context.length()));
    }
}