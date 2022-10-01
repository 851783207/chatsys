package programming3.chatsys.client.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.stream.Collectors;

public class UserPanel extends JPanel implements ActionListener {
    private final ChatGUI gui;

    private JLabel userNameLabel = new JLabel("Username");
    private JLabel fullNameLabel = new JLabel("Full name");
    private JLabel passwordLabel = new JLabel("Password");
    private JTextField userNameField = new JTextField();
    private JTextField fullNameField = new JTextField();
    private JTextField passwordField = new JTextField();
    private JButton userButton = new JButton("Register user");

    public UserPanel(ChatGUI gui) {
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
                                .addComponent(fullNameLabel)
                                .addComponent(passwordLabel)
                                .addComponent(userButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(userNameField)
                                .addComponent(fullNameField)
                                .addComponent(passwordField))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(userNameLabel)
                                .addComponent(userNameField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(fullNameLabel)
                                .addComponent(fullNameField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordLabel)
                                .addComponent(passwordField))
                        .addComponent(userButton)
        );
        userButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Register user button pressed");
        if (this.gui.getClient() != null) {
            try {
                HttpURLConnection con = this.gui.getClient().register(
                        this.userNameField.getText(),
                        this.fullNameField.getText(),
                        this.passwordField.getText());
                int code = con.getResponseCode();
                if (code == 201) {
                    JOptionPane.showMessageDialog(this.gui, "User was successfully registered");
                } else if (code < 600 & code >= 400) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                        String message = "Registration failed:";
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
