package programming3.chatsys.data;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class TextDatabaseTest extends InMemoryDatabaseTest {
    private static final File USER_DB_FILE = new File("test_user_db.txt");
    private static final File MESSAGE_DB_FILE = new File("test_message_db.txt");

    @Override
    protected void initDatabase() {
        MESSAGE_DB_FILE.delete();
        USER_DB_FILE.delete();
        db = new TextDatabase(USER_DB_FILE, MESSAGE_DB_FILE);
    }

    @Test
    @Override
    public void close() {
        assertEquals(0, this.db.getNumberUsers());
        assertEquals(0, this.db.getNumberMessages());
        this.addMessage();
        assertEquals(2, this.db.getNumberUsers());
        assertEquals(4, this.db.getNumberMessages());
        super.close();

        Database freshDb = new TextDatabase(USER_DB_FILE, MESSAGE_DB_FILE);
        System.out.println(freshDb.getNumberUsers());
        System.out.println(freshDb.getNumberMessages());
        assertEquals(this.db.getUser("johndoe"), freshDb.getUser("johndoe"));
        assertEquals(this.db.getUser("jane"), freshDb.getUser("jane"));
        assertEquals(this.db.getNumberUsers(), freshDb.getNumberUsers());
        assertEquals(this.db.getNumberMessages(), freshDb.getNumberMessages());
    }
}