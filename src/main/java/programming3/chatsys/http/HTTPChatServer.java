package programming3.chatsys.http;

import com.sun.net.httpserver.HttpServer;
import programming3.chatsys.data.ChatMessage;
import programming3.chatsys.data.Database;
import programming3.chatsys.data.User;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class HTTPChatServer {
    private final int port;
    private final Database database;
    private final String protocol;

    public HTTPChatServer(int port, Database database, String protocol) {
        this.port = port;
        this.database = database;
        this.protocol = protocol;
    }

    public HTTPChatServer(int port, Database database) {
        this.port = port;
        this.database = database;
        this.protocol = "text";
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(this.port), 0);
        if (this.protocol.equals("json")) {
            server.createContext("/user/", new JSONUserHandler(this));
        } else {
            server.createContext("/user/", new UserHandler(this));
        }

        if (this.protocol.equals("json")) {
            server.createContext("/recent/", new JSONRecentMessagesHandler((this)));
        } else {
            server.createContext("/recent/", new RecentMessagesHandler(this));
        }

        if (this.protocol.equals("json")) {
            server.createContext("/unread/", new JSONUnreadMessagesHandler(this));
        } else {
            server.createContext("/unread/", new UnreadMessagesHandler(this));
        }
        server.createContext("/message/", new MessageHandler(this));

        server.start();
        System.out.println("Server is running");
    }

    public boolean register(User user) {
        return this.database.register(user);
    }

    public List<ChatMessage> getRecentMessages(int n) {
        return this.database.getRecentMessages(n);
    }

    public List<ChatMessage> getUnreadMessages(String username) {
        return this.database.getUnreadMessages(username);
    }

    public boolean authenticate(String username, String password) {
        return this.database.authenticate(username, password);
    }

    public ChatMessage addMessage(String username, String message) {
        return this.database.addMessage(username, message);
    }
}


