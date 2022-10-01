package programming3.chatsys.data;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Objects;

public class ChatMessage {
    private Timestamp timestamp;
    private int id;
    private String user;
    private String message;

    private void init(int id, String user, Timestamp timestamp, String message) {
        if (message.indexOf('\n') >= 0) {
            throw new IllegalArgumentException("message contains a line feed");
        }
        this.id = id;
        this.user = user;
        this.timestamp = timestamp;
        this.message = message;
    }

    public ChatMessage(String formatted) {
        this.parse(formatted);
    }

    public ChatMessage(int id, String user, String message) {
        this.init(id, user, new Timestamp(System.currentTimeMillis()), message);
    }

    public ChatMessage(int id, String user, long timestamp, String message) {
        this.init(id, user, new Timestamp(timestamp), message);
    }

    public ChatMessage(int id, String user, Timestamp timestamp, String message) {
        this.init(id, user, timestamp, message);
    }

    public ChatMessage(JSONObject json) {
        this.init(json.getInt("id"), json.getString("username"), new Timestamp(json.getLong("timestamp")), json.getString("message"));
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage message1 = (ChatMessage) o;
        return id == message1.id && Objects.equals(timestamp, message1.timestamp) && Objects.equals(user, message1.user) && Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, id, user, message);
    }

    public String format(){
        return this.id + "\t" + this.user + "\t" + this.message + "\t" + this.timestamp;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "timestamp=" + timestamp +
                ", id=" + id +
                ", user='" + user + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public void parse(String s) {
        String[] split = s.split("\t");
        if (split.length == 4) {
            this.id = Integer.parseInt(split[0]);
            this.user = split[1];
            this.message = split[2];
            this.timestamp = Timestamp.valueOf(split[3]);
        } else {
            throw new IllegalArgumentException("The String to parse does not contain enough tabulations and cannot be parsed");
        }
    }

    public JSONObject toJSON() {
        long time = this.timestamp.getTime();
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("username", this.user);
        json.put("message", this.message);
        json.put("timestamp", time);
        return json;
    }
}
