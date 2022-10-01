package programming3.chatsys.client;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public abstract class HTTPChatClient {
    private String host = "localhost";
    private int port = 8080;

    public HTTPChatClient() {
    }

    public HTTPChatClient(String host) {
        this.host = host;
    }

    public HTTPChatClient(int port) {
        this.port = port;
    }

    public HTTPChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private URL getURL(String resource) throws MalformedURLException {
        return new URL("http://" + this.host + ":" + this.port + "/" + resource);
    }

    private BufferedReader getMessages(String resource) throws IOException {
        URL url = this.getURL(resource);
        return new BufferedReader(new InputStreamReader(url.openStream()));
    }

    private HttpURLConnection post(String resource) throws IOException {
        URL url = this.getURL(resource);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        return connection;
    }

    private List<ChatMessage> readMessages(String resource) throws IOException {
        try (BufferedReader reader = this.getMessages(resource)) {
            return this.parseMessages(reader);
        } catch(IOException e) {
            throw(e);
        }
    }

    protected abstract List<ChatMessage> parseMessages(BufferedReader reader);

    public List<ChatMessage> getRecentMessages(int n) throws IOException {
        return this.readMessages("recent/" + n);
    }

    public List<ChatMessage> getUnreadMessages(String userName, String password) throws IOException {
        String resource = this.loginResource("unread", userName, password);
        return this.readMessages(resource);
    }

    private String loginResource(String resource, String userName, String password) {
        return resource + "/?username=" + userName + "&password=" + password;
    }

    public HttpURLConnection register(String userName, String fullName, String password) throws IOException {
        HttpURLConnection connection = this.post("user/" + userName);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
            this.formatUser(userName, fullName, password, writer);
        } catch (IOException e) {
            throw(e);
        }
        return connection;
    }

    protected abstract void formatUser(String userName, String fullName, String password, BufferedWriter writer) throws IOException;

    public HttpURLConnection postMessage(String userName, String password, String message) throws IOException {
        HttpURLConnection connection = this.post(this.loginResource("message", userName, password));
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()))) {
            writer.write(message);
        } catch (IOException e) {
            throw(e);
        }
        return connection;
    }
}
