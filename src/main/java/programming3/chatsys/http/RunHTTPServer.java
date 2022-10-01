package programming3.chatsys.http;

import programming3.chatsys.data.Database;
import programming3.chatsys.data.InMemoryDatabase;
import programming3.chatsys.data.SQLiteDatabase;
import programming3.chatsys.data.TextDatabase;

import java.io.File;
import java.io.IOException;

public class RunHTTPServer {
    public static void main(String[] args) throws IOException {
        Database db = new SQLiteDatabase("jdbc:sqlite:test_db.sqlite");
        File userDb = new File("test_user_db.txt");
        File messageDb = new File("test_message_db.txt");
        Database DB = new TextDatabase(userDb, messageDb);
        HTTPChatServer server = new HTTPChatServer(8080, db, "json");
        HTTPChatServer server1 = new HTTPChatServer(8090, DB);
        try {
            server.start();
            server1.start();
        } catch (IOException e) {
            System.out.println("Server could not be started.");
            e.printStackTrace();
        }
    }
}
