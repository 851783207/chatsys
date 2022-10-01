package programming3.chatsys.client;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class JSONChatClient extends HTTPChatClient {
    public JSONChatClient() {
    }

    public JSONChatClient(String host) {
        super(host);
    }

    public JSONChatClient(int port) {
        super(port);
    }

    public JSONChatClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected List<ChatMessage> parseMessages(BufferedReader reader) {
        List<ChatMessage> messages = new LinkedList<>();
        String lines = reader.lines().collect(Collectors.joining("\n"));
        JSONArray array = new JSONObject(lines).getJSONArray("messages");
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            messages.add(new ChatMessage(
                    jsonObject.getString("username"),
                    jsonObject.getLong("timestamp"),
                    jsonObject.getString("message")));
        }
        return messages;
    }

    @Override
    protected void formatUser(String userName, String fullName, String password, BufferedWriter writer) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", userName);
        jsonObject.put("fullname", fullName);
        jsonObject.put("password", password);
        jsonObject.write(writer);
    }
}
