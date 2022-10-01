package programming3.chatsys.client.gui;

import programming3.chatsys.client.HTTPChatClient;

import javax.swing.*;
import java.io.IOException;

public class ChatGUI extends JFrame {
    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final JPanel connectionPanel = new ConnectionPanel(this);
    private final JPanel userPanel = new UserPanel(this);
    private final JPanel messagePanel = new MessagePanel(this);
    private final JPanel recentPanel = new RecentPanel(this);
    private final JPanel unreadPanel = new UnreadPanel(this);

    private HTTPChatClient client;

    public ChatGUI() {
        super("Simple Chat GUI");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.initTabbedPane();
    }

    private void initTabbedPane() {
        tabbedPane.add("Connection", connectionPanel);
        tabbedPane.add("Register user", userPanel);
        tabbedPane.add("Post message", messagePanel);
        tabbedPane.add("Recent messages", recentPanel);
        tabbedPane.add("Unread messages", unreadPanel);
        this.add(tabbedPane);
    }

    public HTTPChatClient getClient() {
        return client;
    }

    public void setClient(HTTPChatClient client) {
        this.client = client;
    }

    public static void main(String[] args) throws IOException {
        ChatGUI gui = new ChatGUI();
        gui.setVisible(true);
        gui.setSize(600, 600);
    }
}
