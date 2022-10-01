package programming3.chatsys.client.gui;

import programming3.chatsys.client.ChatMessage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UnreadPanel extends JPanel implements ActionListener {
    private final ChatGUI gui;

    private JLabel userNameLabel = new JLabel("Username");
    private JLabel passwordLabel = new JLabel("Password");
    private JTextField userNameField = new JTextField();
    private JTextField passwordField = new JTextField();
    private JButton unreadButton = new JButton("Get unread messages");

    public UnreadPanel(ChatGUI gui) {
        super();
        this.gui = gui;
        this.init();
    }

    private void init() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(userNameLabel)
                                .addComponent(passwordLabel)
                                .addComponent(unreadButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(userNameField)
                                .addComponent(passwordField))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(userNameLabel)
                                .addComponent(userNameField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordLabel)
                                .addComponent(passwordField))
                        .addComponent(unreadButton)
        );
        unreadButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Unread messages button pressed");
        if (this.gui.getClient() != null) {
            try {
                List<ChatMessage> messages = this.gui.getClient().getUnreadMessages(
                        this.userNameField.getText(),
                        this.passwordField.getText());
                String text = messages.stream().map(ChatMessage::toString).collect(Collectors.joining("\n"));
                JOptionPane.showMessageDialog(this.gui, "Received the following messages:\n" + text);
            } catch (IOException exception) {
                JOptionPane.showMessageDialog(this.gui, exception, "Network error", JOptionPane.ERROR_MESSAGE);
            } catch(RuntimeException exception) {
                JOptionPane.showMessageDialog(this.gui, exception, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this.gui, "No connection setup", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
