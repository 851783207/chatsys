package programming3.chatsys.client.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.stream.Collectors;

public class MessagePanel extends JPanel implements ActionListener {
    private final ChatGUI gui;

    private JLabel userNameLabel = new JLabel("Username");
    private JLabel passwordLabel = new JLabel("Password");
    private JTextField userNameField = new JTextField();
    private JTextField passwordField = new JTextField();
    private JLabel messageLabel = new JLabel("Message");
    private JTextField messageField = new JTextField();
    private JButton messageButton = new JButton("Post message");

    public MessagePanel(ChatGUI gui) {
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
                                .addComponent(messageLabel)
                                .addComponent(messageButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(userNameField)
                                .addComponent(passwordField)
                                .addComponent(messageField))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(userNameLabel)
                                .addComponent(userNameField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordLabel)
                                .addComponent(passwordField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(messageLabel)
                                .addComponent(messageField))
                        .addComponent(messageButton)
        );
        messageButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Post message button pressed.");
        if (this.gui.getClient() != null) {
            try {
                HttpURLConnection con = this.gui.getClient().postMessage(
                        this.userNameField.getText(),
                        this.passwordField.getText(),
                        this.messageField.getText());
                int code = con.getResponseCode();
                if (code == 201) {
                    JOptionPane.showMessageDialog(this.gui, "Message was posted successfully");
                } else if (code < 600 & code >= 400) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                        String message = "Posting message failed:";
                        message += "Received response " + code + " " + con.getResponseMessage();
                        message += reader.lines().collect(Collectors.joining("\n"));
                        JOptionPane.showMessageDialog(this.gui, message);
                    } catch(IOException exception) {
                        JOptionPane.showMessageDialog(this.gui, exception, "Network error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this.gui, "Received incorrect response code " + code);
                }
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
