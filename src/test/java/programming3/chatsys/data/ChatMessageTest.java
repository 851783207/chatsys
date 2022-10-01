package programming3.chatsys.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class ChatMessageTest {
    Timestamp time =new Timestamp(System.currentTimeMillis());
    long TIME = System.currentTimeMillis();
    private ChatMessage mess1;
    private ChatMessage mess2;
    private ChatMessage mess3;
    private ChatMessage mess4;
    private ChatMessage mess5;

    private final JSONObject message1 = new JSONObject("{id:1, username:\"johndoe\", message:hello}");
    private final JSONObject message2 = new JSONObject("{id:2, username:\"johndoe\", message:HELLO}");


    @BeforeEach
    void setUp() {
        mess1 = new ChatMessage(1,"johndoe", time, "hello");
        mess2 = new ChatMessage(2,"johndoe", time, "HELLO");
        mess3 = new ChatMessage(3,"johndo", "HELLO");
        mess4 = new ChatMessage(4,"johndoe", "Hello");
        mess5 = mess1;

        message1.put("timestamp", TIME);
        message2.put("timestamp", TIME);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testEquals() {
        assertSame(mess1, mess5);
        assertNotSame(mess1, mess4);
        assertEquals(mess1, mess5);
        assertNotEquals(mess2, mess3);
    }

    @Test
    void testFormat() {
        assertEquals("1\tjohndoe\thello\t" + time, mess1.format());
    }

    @Test
    void testParse() {
        mess1.parse("1\tjohndoe\tHello\t"+time);
        assertEquals(1, mess1.getId());
        assertEquals("johndoe", mess1.getUser());
        assertEquals("Hello", mess1.getMessage());
        assertEquals(time, mess1.getTimestamp());

        assertThrows(IllegalArgumentException.class, () -> {
            mess1.parse("1\tjohndoe");
        });
    }

    @Test
    void testToJSON() {
        message1.put("timestamp", time);
        message2.put("timestamp", time);
        assertEquals(mess1.toJSON().toString(), message1.toString());
        assertEquals(mess2.toJSON().toString(), message2.toString());
    }

    @Test
    void testFromJSON() {
        ChatMessage message = new ChatMessage(message1);
        ChatMessage MESSAGE = new ChatMessage(message2);
        assertEquals(mess1, message);
        assertEquals(mess1.getId(), message.getId());
        assertEquals(mess1.getUser(),message.getUser());
        assertEquals(mess1.getTimestamp(), message.getTimestamp());
        assertEquals(mess2, MESSAGE);
        assertEquals(mess2.getId(), MESSAGE.getId());
        assertEquals(mess2.getUser(),MESSAGE.getUser());
        assertEquals(mess2.getTimestamp(), MESSAGE.getTimestamp());

        assertThrows(JSONException.class, () -> {
            new ChatMessage(new JSONObject("{}"));
        });
        assertThrows(JSONException.class, () -> {
            new ChatMessage(new JSONObject("{id:1, username:\"johndoe\", message:hello}"));
        });
        assertThrows(JSONException.class, () -> {
            new ChatMessage(new JSONObject("{id:1, username:johndoe, message:hello}"));
        });
    }
}