package programming3.chatsys.http.protocol;

import org.json.JSONArray;
import org.json.JSONObject;
import programming3.chatsys.data.ChatMessage;
import programming3.chatsys.data.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class JSONProtocol {
    private BufferedReader reader;
    private BufferedWriter writer;

    public JSONProtocol(BufferedReader reader, BufferedWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public JSONProtocol(BufferedReader reader) {
        this.reader = reader;
    }

    public JSONProtocol(BufferedWriter writer) {
        this.writer = writer;
    }

    public User readUser() throws IOException {
        String json = "";
        String line ;
        while ((line = this.reader.readLine()) != null) {
            json += line;
        }
        return new User(new JSONObject(json));
    }

    public void writeUser(User user) throws IOException {
        JSONObject json = user.toJSON();
        json.write(this.writer);
        this.writer.flush();
    }

    public List<ChatMessage> readMessages () throws IOException{
        List<ChatMessage> messages = new LinkedList<>();
        String line;
        while ((line = this.reader.readLine()) != null) {
            ChatMessage message = new ChatMessage(new JSONObject(line));
            messages.add(message);
        }

        return messages;
    }

    public void writeMessages(List<ChatMessage> messages) throws IOException{
        JSONArray array = new JSONArray();
        for (ChatMessage message : messages) {
            JSONObject json = message.toJSON();
            array.put(json);
        }
        JSONObject json = new JSONObject();
        json.put("messages", array);
        json.write(this.writer);
        this.writer.flush();
    }
}
