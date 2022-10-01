package programming3.chatsys.client;

import java.sql.Timestamp;

public class ChatMessage {
    private String userName;
    private Timestamp timestamp;
    private String message;

    public ChatMessage(String userName, long timestamp, String message) {
        this.userName = userName;
        this.timestamp = new Timestamp(timestamp);
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "userName='" + userName + '\'' +
                ", timestamp=" + timestamp +
                ", message='" + message + '\'' +
                '}';
    }
}
