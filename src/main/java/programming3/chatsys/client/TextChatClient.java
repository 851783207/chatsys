package programming3.chatsys.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TextChatClient extends HTTPChatClient {
    public TextChatClient() {
    }

    public TextChatClient(String host) {
        super(host);
    }

    public TextChatClient(int port) {
        super(port);
    }

    public TextChatClient(String host, int port) {
        super(host, port);
    }

    @Override
    protected List<ChatMessage> parseMessages(BufferedReader reader) {
        List<ChatMessage> messages = new LinkedList<>();
        for (String line: reader.lines().collect(Collectors.toList())) {
            String[] split = line.split("\t", 4);
            if (split.length < 4) {
                throw new IllegalArgumentException("The following ChatMessage does not contain enough tabulations and cannot be parsed: " + line);
            } else {
                messages.add(new ChatMessage(split[1], Long.parseLong(split[3]), split[2]));
            }
        }
        return messages;
    }

    @Override
    protected void formatUser(String userName, String fullName, String password, BufferedWriter writer) throws IOException {
        writer.write(userName + "\t" + fullName + "\t" + password);
    }
}
