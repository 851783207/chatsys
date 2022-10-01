package programming3.chatsys.data;

import java.io.*;
import java.util.List;

public class TextDatabase extends InMemoryDatabase {
    private File userDb;
    private File messageDb;

    public TextDatabase(File userDb, File messageDb) {
        this.userDb = userDb;
        this.messageDb = messageDb;

        this.loadUsers();
        this.loadMessages();
    }

    @Override
    public void close() {
        this.saveUsers();
        this.saveMessages();
    }

    @Override
    public boolean register(User user) {
        boolean b = super.register(user);
        this.saveUsers();
        return b;
    }

    @Override
    public List<ChatMessage> getUnreadMessages(String userName) {
        List<ChatMessage> messages = super.getUnreadMessages(userName);
        this.saveUsers();
        return messages;
    }

    private void saveUsers() {
        System.out.println("Saving users.");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.userDb))) {
            for (User u : this.users) {
                writer.write(u.format() + "\r\n");
            }
        } catch (IOException e) {
            throw new DatabaseAccessException("Cannot write user DB file" + this.userDb, e);
        }
    }

    private void loadUsers() {
        System.out.println("Loading users.");
        if (this.userDb.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(this.userDb))) {
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        User u = new User(line);
                        this.users.add(u);
                    }
                }
            } catch (IOException e) {
                throw new DatabaseAccessException("Cannot access user DB file" + this.userDb, e);
            }
        }

    }

    @Override
    public ChatMessage addMessage(String userName, String message) {
       ChatMessage m = super.addMessage(userName, message);
       this.saveMessages();
        return m;
    }

    private void saveMessages() {
        System.out.println("Saving chat messages.");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.messageDb))) {
            for (ChatMessage message : this.message) {
                writer.write(message.format() + "\r\n");
            }
        } catch (IOException e) {
            throw new DatabaseAccessException("Cannot write user DB file" + this.messageDb, e);
        }
    }

    private void loadMessages() {
        System.out.println("Loading chat messages.");
        if (this.userDb.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(this.messageDb))) {
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        ChatMessage message = new ChatMessage(line);
                        this.message.add(message);
                    }
                }
            } catch (IOException e) {
                throw new DatabaseAccessException("Cannot access user DB file" + this.messageDb, e);
            }
        }
    }
}
