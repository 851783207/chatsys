package programming3.chatsys.client.gui;

import programming3.chatsys.client.JSONChatClient;
import programming3.chatsys.client.TextChatClient;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionPanel extends JPanel implements ActionListener {
    private final ChatGUI gui;

    private JTextField hostField = new JTextField("localhost", 20);
    private JTextField portField = new JTextField("8090", 5);
    private JLabel hostLabel = new JLabel("Host");
    private JLabel portLabel = new JLabel("Port");
    private JButton textButton = new JButton("Set connection with text protocol");
    private JButton jsonButton = new JButton("Set connection with JSON");

    public ConnectionPanel(ChatGUI gui) {
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
                                .addComponent(hostLabel)
                                .addComponent(portLabel)
                                .addComponent(textButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(hostField)
                                .addComponent(portField)
                                .addComponent(jsonButton))
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(hostLabel)
                                .addComponent(hostField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(portLabel)
                                .addComponent(portField))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(textButton)
                                .addComponent(jsonButton))
        );
        textButton.setActionCommand("text");
        textButton.addActionListener(this);
        jsonButton.setActionCommand("json");
        jsonButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Connect button pressed");
        try {
            String host = this.hostField.getText();
            int port = Integer.parseInt(this.portField.getText());
            if (e.getActionCommand().equals("json")) {
                this.gui.setClient(new JSONChatClient(host, port));
                JOptionPane.showMessageDialog(this.gui, "Will connect to " + host + ":" + port +
                        " using JSON protocol");
            } else {
                this.gui.setClient(new TextChatClient(host, port));
                JOptionPane.showMessageDialog(this.gui, "Will connect to " + host + ":" + port +
                        " using text protocol");
            }
            System.out.println("Connection created " + this.gui.getClient());
        } catch(NumberFormatException exception) {
            JOptionPane.showMessageDialog(this.gui, "Port field must be an integer", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
