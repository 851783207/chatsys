package programming3.chatsys.http;

import programming3.chatsys.data.Database;
import programming3.chatsys.data.InMemoryDatabase;
import programming3.chatsys.data.SQLiteDatabase;
import programming3.chatsys.data.TextDatabase;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.IOException;

public class RunServer {
    public static final boolean supportsMySQL = false;
    public static final boolean supportsTextDB = true;
    public static final boolean supportsSQLite = true;

    public static void runServer(int port, Database db) {
        HTTPChatServer server = new HTTPChatServer(port, db, "json");
        try {
            server.start();
        } catch (IOException e) {
            System.out.println("Server could not be started.");
            e.printStackTrace();
        }
    }

    public static void run(int port) {
        Database db = new InMemoryDatabase();
        runServer(port, db);
    }

    public static void run(int port, String messageDb, String userDb) {
        Database db = new TextDatabase(new File(userDb), new File(messageDb));
        runServer(port, db);
    }

    public static void run(int port, String dbFile) {
        Database db = new SQLiteDatabase(dbFile);
        runServer(port, db);
    }

    public static void run(int port, String dbHost, int dbPort, String dbUser, String dbPassword, String dbName) {
        throw new NotImplementedException();
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            int port = Integer.parseInt(args[0]);
            if (args.length == 6) {
                System.out.println("Running server with MySQL database");
                run(port, args[1], Integer.parseInt(args[2]), args[3], args[4], args[5]);
            } else if (args.length == 3) {
                System.out.println("Running server with text database");
                run(port, args[1], args[2]);
            } else if (args.length == 2) {
                System.out.println("Running server with SQLite database");
                run(port, args[1]);
            } else {
                System.out.println("Running server with in memory database");
                run(port);
            }
        } else {
            System.out.println("Missing CLI arguments");
        }
    }
}
