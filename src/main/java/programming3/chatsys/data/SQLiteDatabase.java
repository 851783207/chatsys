package programming3.chatsys.data;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class SQLiteDatabase implements Database {
    private Connection connection;

    public SQLiteDatabase(String url) {
        try {
            this.connection = DriverManager.getConnection(url);
            this.createUsersTable();
            this.createMessagesTable();
        } catch (SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    private void createUsersTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS user (\n" +
                "id integer PRIMARY KEY,\n" +
                "username text UNIQUE NOT NULL,\n" +
                "fullname text NOT NULL,\n" +
                "password text NOT NULL,\n" +
                "last_read_id integer DEFAULT 0\n" +
                ");";
        Statement statement = this.connection.createStatement();
        statement.execute(query);
    }

    private void createMessagesTable() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS chatmessage (\n" +
                "id integer PRIMARY KEY,\n" +
                "user text NOT NULL,\n" +
                "time timestamp DEFAULT CURRENT_TIMESTAMP,\n" +
                "message text NOT NULL\n" +
                ");";
        Statement statement = this.connection.createStatement();
        statement.execute(query);
    }

    @Override
    public boolean register(User user) {
        String query = "INSERT INTO user(username, fullname, password) VALUES(?, ?, ?)";
        String query1 = "UPDATE user SET last_read_id = ? WHERE username = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getFullName());
            statement.setString(3, user.getPassword());
            int b = statement.executeUpdate();
            if(user.getLastReadId()>0) {
                PreparedStatement STAT = this.connection.prepareStatement(query1);
                STAT.setInt(1, user.getLastReadId());
                STAT.setString(2, user.getUserName());
                STAT.execute();
            }
            return b == 1;
        } catch (SQLException e) {
            if (e.getErrorCode() == 19) {
                return false;
            } else {
                throw new DatabaseAccessException(e);
            }
        }
    }

    @Override
    public int getNumberUsers() {
        String query = "SELECT COUNT(*) FROM user";
        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
            return statement.getResultSet().getInt(1);
        } catch (SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    @Override
    public User getUser(String userName) {
        String query = "SELECT * FROM user WHERE username = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setString(1, userName);
            statement.execute();
            ResultSet result = statement.getResultSet();
            if (result.next()) {
                return new User(
                        result.getString("username"),
                        result.getString("fullname"),
                        result.getString("password")
                );
            } else {
                throw new IllegalArgumentException(userName + " is not a registered user");
            }
        } catch (SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    @Override
    public boolean authenticate(String userName, String password) {
        String query = "SELECT COUNT(*) FROM user WHERE username = ? AND password = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setString(1, userName);
            statement.setString(2, password);
            statement.execute();
            ResultSet result = statement.getResultSet();
            return result.getInt(1) == 1;
        } catch (SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    @Override
    public ChatMessage addMessage(String userName, String message) {
        String query = "INSERT INTO chatmessage(user, message) VALUES(?, ?)";
        String query2 = "SELECT * FROM user WHERE username = ?";
        String query1 = "select * from chatmessage WHERE 1=1 order by id desc limit 1";
        try {
            PreparedStatement statement2 = this.connection.prepareStatement(query2);
            statement2.setString(1, userName);
            statement2.execute();
            ResultSet result2 = statement2.getResultSet();
            if (!result2.next()) {
                throw new IllegalArgumentException(userName + " is not a registered user");
            }
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setString(1, userName);
            statement.setString(2, message);
            statement.execute();
            Statement statement1 = this.connection.createStatement();
            statement1.execute(query1);
            ResultSet result = statement1.getResultSet();
            if (result.next()) {
                return new ChatMessage(result.getInt("id"),
                        result.getString("user"),
                        result.getTimestamp("time"),
                        result.getString("message")
                );
            } else {
                throw new IllegalArgumentException("Add message failed");
            }
        } catch(SQLException e) {
            throw new DatabaseAccessException(e);
        }
}

    @Override
    public int getNumberMessages() {
        String query = "SELECT COUNT(*) FROM chatmessage";
        try {
            Statement statement = this.connection.createStatement();
            statement.execute(query);
            return statement.getResultSet().getInt(1);
        } catch(SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    @Override
    public List<ChatMessage> getRecentMessages(int n) {
        if (n > this.getNumberMessages()) {
            throw new IllegalArgumentException("Do not have enough messages");
        }
        List<ChatMessage> chatMessages = new LinkedList<>();
        String query = "select * from (select * from chatmessage order by id desc limit ?) order by id";
        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setInt(1, n);
            statement.execute();
            ResultSet result = statement.getResultSet();
            while (result.next()) {
                chatMessages.add( new ChatMessage(
                        result.getInt("id"),
                        result.getString("user"),
                        result.getTimestamp("time"),
                        result.getString("message")
                ));
            }
            return chatMessages;
        } catch(SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    @Override
    public List<ChatMessage> getUnreadMessages(String userName) {
        List<ChatMessage> chatMessages = new LinkedList<>();
        String query1 = "select * from user WHERE username = ?";
        String query2 = "select * from (select * from chatmessage order by id desc limit ?) order by id";
        String query3 = "UPDATE user SET last_read_id = ? WHERE username = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(query1);
            statement.setString(1, userName);
            statement.execute();
            ResultSet result = statement.getResultSet();
            int n;
            if (result.next()){
                    n = result.getInt("last_read_id");
            } else {
                throw new IllegalArgumentException(userName + " is not a registered user");
            }
            statement = this.connection.prepareStatement(query2);
            statement.setInt(1, this.getNumberMessages()-n);
            statement.execute();
            result = statement.getResultSet();
            while (result.next()) {
                chatMessages.add(new ChatMessage(
                        result.getInt("id"),
                        result.getString("user"),
                        result.getTimestamp("time"),
                        result.getString("message")
                ));
            }
            PreparedStatement STAT = this.connection.prepareStatement(query3);
            STAT.setInt(1, this.getNumberMessages());
            STAT.setString(2, userName);
            STAT.execute();
            return chatMessages;
        }catch(SQLException e) {
            throw new DatabaseAccessException(e);
        }
    }

    @Override
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
