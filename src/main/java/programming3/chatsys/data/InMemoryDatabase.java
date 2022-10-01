package programming3.chatsys.data;

import java.util.LinkedList;
import java.util.List;

public class InMemoryDatabase implements Database{
    protected List<User> users;
    protected List<ChatMessage> message;
    private int lastId = 0;

    public InMemoryDatabase() {
        users = new LinkedList<>();
        message = new LinkedList<>();

    }

    @Override
    public String toString() {
        return "InMemoryDatabase{" +
                "users=" + users + message+
                '}';
    }

    @Override
    public boolean register(User user) {
        if(this.contains(user)){
            return false;
        }
        this.users.add(user);
        return true;
    }

    private boolean contains(User user) {
        return this.contains(user.getUserName());
    }

    private boolean contains(String userName) {
        try {
            return this.getUser(userName)!=null;
        }catch (IllegalArgumentException e){
            return false;
        }
    }

    @Override
    public int getNumberUsers() {
        return this.users.size();
    }

    @Override
    public User getUser(String userName) {
        for(User u: this.users){
            if (u.getUserName().equals(userName)){
                return u;
            }
        }
        throw new IllegalArgumentException(userName+" is not a register user.");
    }

    @Override
    public boolean authenticate(String userName, String password) {
        try{
            return this.getUser(userName).getPassword().equals(password);
        }catch (IllegalArgumentException e){
            return false;
        }
    }

    @Override
    public ChatMessage addMessage(String userName, String message) {
        if (this.contains(userName)) {
            this.lastId = this.getNumberMessages()+1;
            ChatMessage chatMessage = new ChatMessage(this.lastId, userName, message);
            this.message.add(chatMessage);
            return chatMessage;
        } else {
            throw new IllegalArgumentException("User " + userName + " is not registered.");
        }
    }

    @Override
    public int getNumberMessages() {
        return this.message.size();
    }

    @Override
    public List<ChatMessage> getRecentMessages(int n) {
        if (n > 0 && n < this.message.size()) {
            return message.subList(message.size() - n, message.size());
        } else {
            return new LinkedList<>(message);
        }
    }

    @Override
    public List<ChatMessage> getUnreadMessages(String userName) {
        User user = this.getUser(userName);
        final int lastReadId = user.getLastReadId();
        if (lastReadId == this.lastId) {
            return new LinkedList<>();
        } else {
            int firstUnread = 0;
            for (ChatMessage m : this.message) {
                firstUnread = m.getId();
                if (firstUnread > lastReadId) {
                    break;
                }
            }
            this.getUser(userName).setLastReadId(this.getNumberMessages());
            return this.message.subList(firstUnread - 1, this.message.size());
        }
    }

    @Override
    public void close() {
    }
}
