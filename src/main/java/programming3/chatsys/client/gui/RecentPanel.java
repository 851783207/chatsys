package programming3.chatsys.client.gui;

import programming3.chatsys.client.ChatMessage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RecentPanel extends JPanel implements ActionListener {
    private final ChatGUI gui;

    private JLabel nLabel = new JLabel("Number of messages");
    private JTextField nField = new JTextField();
    private JButton recentButton = new JButton("Get recent messages");

    public RecentPanel(ChatGUI gui) {
        super();
        this.gui = gui;
        this.init();
    }

    private void init() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(nLabel)
                                .addComponent(recentButton))
                        .addComponent(nField)
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(nLabel)
                                .addComponent(nField))
                        .addComponent(recentButton)
        );
        recentButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Recent messages button pressed");
        if (this.gui.getClient() != null) {
            try {
                int n = Integer.parseInt(this.nField.getText());
                List<ChatMessage> messages = this.gui.getClient().getRecentMessages(n);
                String text = messages.stream().map(ChatMessage::toString).collect(Collectors.joining("\n"));
                JOptionPane.showMessageDialog(this.gui, "Received the following messages:\n" + text);
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(this.gui, "Number of messages must be an integer", "Error", JOptionPane.ERROR_MESSAGE);
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
