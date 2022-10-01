package programming3.chatsys.http.protocol;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import programming3.chatsys.data.ChatMessage;
import programming3.chatsys.data.User;
import java.sql.Timestamp;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JSONProtocolTest {
    private final JSONObject jsonJohn = new JSONObject("{username:johndoe, fullname:\"John Doe\", password:thepassword}");
    private final JSONObject jsonJane = new JSONObject("{username:jane, fullname:\"Jane Doe\", password:janespassword}");

    private final User john = new User("johndoe", "John Doe", "thepassword");
    private final User jane = new User("jane", "Jane Doe", "janespassword");

    private final long time = System.currentTimeMillis();
    private final JSONObject message1 = new JSONObject("{id:2, username:\"Jane Doe\", message:hello}");
    private final JSONObject message2 = new JSONObject("{id:3, username:\"Jane Doe\", message:HELLO}");

    private final ChatMessage mess1 = new ChatMessage(2,"Jane Doe", time,"hello");
    private final ChatMessage mess2 = new ChatMessage(3,"Jane Doe", time,"HELLO");
    @Test
    void readUser() throws IOException {
        JSONProtocol protocol = new JSONProtocol(new BufferedReader(new StringReader(jsonJohn.toString())));
        assertEquals(john, protocol.readUser());

        protocol = new JSONProtocol(new BufferedReader(new StringReader(jsonJane.toString())));
        assertEquals(jane, protocol.readUser());
    }

    @Test
    void writeUser() throws IOException {
        StringWriter string = new StringWriter();
        JSONProtocol protocol = new JSONProtocol(new BufferedWriter(string));
        protocol.writeUser(john);
        assertEquals(jsonJohn.toString(), string.toString());

        string = new StringWriter();
        protocol = new JSONProtocol(new BufferedWriter(string));
        protocol.writeUser(jane);
        assertEquals(jsonJane.toString(), string.toString());
    }

    @Test
    void readMessages() throws IOException{
        message1.put("timestamp", time);
        message2.put("timestamp", time);
        JSONProtocol protocol = new JSONProtocol(new BufferedReader(new StringReader(message2.toString()+'\n'+ message1)));
        List<ChatMessage> messages = protocol.readMessages();
        assertTrue(messages.contains(mess1));
        assertTrue(messages.contains(mess2));
    }

    @Test
    void writeMessages() throws IOException {
        List<ChatMessage> messages = new LinkedList<>();
        messages.add(mess1);
        messages.add(mess2);
        message1.put("timestamp", new Timestamp(time));
        message2.put("timestamp", new Timestamp(time));

        StringWriter string = new StringWriter();
        JSONProtocol protocol = new JSONProtocol(new BufferedWriter(string));
        protocol.writeMessages(messages);

    }
}