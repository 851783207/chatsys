package programming3.chatsys.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryDatabaseTest {
    protected User user1;
    protected User user2;
    protected User user3;
    protected User user4;
    protected User user5;
    protected User user6;
    protected Database db;



    @BeforeEach
    void setUp() {
        user1 = new User("johndoe", "John Doe", "thepassword");
        user2 = new User("jane", "Jane Doe", "janespassword");
        user3 = new User("johndoe", "John Doe", "thepassword");
        user4 = new User("johndoe", "John Doe", "anotherpassword");
        user5 = new User("johndoe", "John Doe 2", "anotherpassword");
        user6 = user1;
        this.initDatabase();
    }

    protected void initDatabase() {
        db = new InMemoryDatabase();
    }

    @AfterEach
    void tearDown() {
        db.close();
    }

    @Test
    void register() {
        assertTrue(db.register(user1));
        assertTrue(db.register(user2));
        assertFalse(db.register(user3));
        assertFalse(db.register(user4));
        assertFalse(db.register(user5));
        assertFalse(db.register(user6));
    }

    @Test
    void getNumberUsers() {
        assertEquals(0, db.getNumberUsers());
        db.register(user1);
        assertEquals(1, db.getNumberUsers());
        db.register(user2);
        assertEquals(2, db.getNumberUsers());
        db.register(user3);
        assertEquals(2, db.getNumberUsers());
        db.register(user4);
        assertEquals(2, db.getNumberUsers());
        db.register(user5);
        assertEquals(2, db.getNumberUsers());
        db.register(user6);
        assertEquals(2, db.getNumberUsers());
    }

    @Test
    void getUser() {
        db.register(user1);
        db.register(user2);
        assertEquals(user1, db.getUser("johndoe"));
        assertEquals(user2, db.getUser("jane"));
        assertThrows(IllegalArgumentException.class, () -> {
            db.getUser("john");
        });
    }

    @Test
    void authenticate() {
        db.register(user1);
        assertTrue(db.authenticate("johndoe", "thepassword"));
        assertFalse(db.authenticate("johndoe", "wrongpassword"));
        assertFalse(db.authenticate("john", "thepassword"));
    }

    @Test
    void addMessage(){
        this.register();
        db.addMessage("johndoe", "hello1");
        db.addMessage("jane", "hello2");
        db.addMessage("jane", "hello3");
        db.addMessage("johndoe", "hello4");
    }

    @Test
    void Message() {
        db.register(user1);
        db.register(user2);
        assertEquals("johndoe",db.addMessage("johndoe", "hello").getUser());
        assertEquals(2, db.addMessage("jane", "hello").getId());
        assertEquals(3, db.addMessage("jane", "HELLO").getId());
        assertThrows(IllegalArgumentException.class, () -> {
            db.addMessage("JOHN", "hello");
        });
    }

    @Test
    void getNumberMessages() {
        assertEquals(0, db.getNumberMessages());
        db.register(user1);
        db.register(user2);
        db.addMessage("johndoe", "hello");
        assertEquals(1, db.getNumberMessages());
        db.addMessage("johndoe", "hello");
        assertEquals(2, db.getNumberMessages());
        db.addMessage("jane", "hello");
        assertEquals(3, db.getNumberMessages());
        db.addMessage("johndoe", "ABC");
        assertEquals(4, db.getNumberMessages());
    }

    @Test
    void getRecentMessages() {
        db.register(user1);
        db.register(user2);
        List<ChatMessage> message;
        db.addMessage("johndoe", "hello1");
        db.addMessage("jane", "hello2");
        db.addMessage("jane", "hello3");
        db.addMessage("johndoe", "ABC");
        message = db.getRecentMessages(2);
        assertEquals(2, message.size());
        assertEquals("johndoe", message.get(1).getUser());
        assertEquals("ABC", message.get(1).getMessage());
        assertEquals("jane", message.get(0).getUser());
        assertEquals("hello3", message.get(0).getMessage());
    }

    @Test
    void getUnreadMessages() {
        List<ChatMessage> message;
        user1.setLastReadId(2);
        user2.setLastReadId(4);
        db.register(user1);
        db.register(user2);
        db.addMessage("johndoe", "hello1");
        db.addMessage("jane", "hello2");
        db.addMessage("jane", "hello3");
        db.addMessage("johndoe", "ABC");
        message = db.getUnreadMessages("johndoe");
        System.out.println(message.get(0).toString());
        System.out.println(message.get(1).toString());
        assertEquals("hello3", message.get(0).getMessage());
        assertEquals("ABC", message.get(1).getMessage());
        assertEquals(new LinkedList<>(), db.getUnreadMessages("jane"));

    }

    @Test
    void close() {
        this.db.close();
    }
}