package programming3.chatsys.data;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteDatabaseTest extends InMemoryDatabaseTest {
    private final File DB_FILE = new File("test_db.sqlite");

    @Override
    protected void initDatabase() {
        DB_FILE.delete();
        db = new SQLiteDatabase("jdbc:sqlite:" + DB_FILE.getPath());
    }

    @Test
    @Override
    public void close() {
        List<ChatMessage> message;
        assertEquals(0, this.db.getNumberUsers());
        assertEquals(0, this.db.getNumberMessages());
        this.addMessage();
        assertEquals(2, this.db.getNumberUsers());
        assertEquals(4, this.db.getNumberMessages());
        Database freshDb = new SQLiteDatabase("jdbc:sqlite:" + DB_FILE.getPath());
        System.out.println(freshDb.getNumberUsers());
        assertEquals(this.db.getUser("johndoe"), freshDb.getUser("johndoe"));
        assertEquals(this.db.getUser("jane"), freshDb.getUser("jane"));
        assertEquals(this.db.getNumberUsers(), freshDb.getNumberUsers());
        assertEquals(this.db.getNumberMessages(), freshDb.getNumberMessages());
        freshDb.close();
    }
}