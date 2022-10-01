package programming3.chatsys.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;
    private User user6;

    private final JSONObject jsonJohn = new JSONObject("{username:johndoe, fullname:\"John Doe\", password:thepassword}");
    private final JSONObject jsonJane = new JSONObject("{username:jane, fullname:\"Jane Doe\", password:janespassword}");

    @BeforeEach
    void setUp() {
        user1 = new User("johndoe", "John Doe", "thepassword");
        user2 = new User("jane", "Jane Doe", "janespassword");
        user3 = new User("johndoe", "John Doe", "thepassword");
        user4 = new User("johndoe", "John Doe", "anotherpassword");
        user5 = new User("johndoe", "John Doe 2", "anotherpassword");
        user6 = user1;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testEquals() {
        assertSame(user1, user6);
        assertNotSame(user1, user3);
        assertEquals(user3, user1);
        assertNotEquals(user2, user1);
        assertEquals(user4, user1);
        assertEquals(user5, user1);
        assertEquals(user6, user1);
    }

    @Test
    void testFormat() {
        assertEquals("johndoe\tJohn Doe\tthepassword\t0", user1.format());
    }

    @Test
    void testParse() {
        user1.parse("janedoe\tJane Doe\tthepassword\t0");
        assertEquals("janedoe", user1.getUserName());
        assertEquals("Jane Doe", user1.getFullName());

        assertThrows(IllegalArgumentException.class, () -> {
            user1.parse("janedoe\tJane Doe");
        });
    }

    @Test
    void testToJSON() {
        assertEquals(jsonJohn.toString(), user1.toJSON().toString());
        assertEquals(jsonJane.toString(), user2.toJSON().toString());
    }

    @Test
    void testFromJSON() {
        User john = new User(jsonJohn);
        User jane = new User(jsonJane);
        assertEquals(user1, john);
        assertEquals(user1.getUserName(), john.getUserName());
        assertEquals(user1.getFullName(), john.getFullName());
        assertEquals(user1.getPassword(), john.getPassword());
        assertEquals(user2, jane);
        assertEquals(user2.getUserName(), jane.getUserName());
        assertEquals(user2.getFullName(), jane.getFullName());
        assertEquals(user2.getPassword(), jane.getPassword());

        assertThrows(JSONException.class, () -> {
            new User(new JSONObject("{}"));
        });
        assertThrows(JSONException.class, () -> {
            new User(new JSONObject("{fullname:\"John Doe\", password:thepassword}"));
        });
        assertThrows(JSONException.class, () -> {
            new User(new JSONObject("{username:johndoe, password:thepassword}"));
        });
        assertThrows(JSONException.class, () -> {
            new User(new JSONObject("{username:johndoe, fullname:\"John Doe\"}"));
        });
        assertThrows(JSONException.class, () -> {
            new User(new JSONObject("{username:johndoe, fullname:\"John Doe\", password:thepassword"));
        });
        assertThrows(JSONException.class, () -> {
            new User(new JSONObject("{username:johndoe, fullname:{firstname:John, lastname:Doe}, password:thepassword}"));
        });
    }
}