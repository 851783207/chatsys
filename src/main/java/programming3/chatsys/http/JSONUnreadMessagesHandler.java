package programming3.chatsys.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import programming3.chatsys.data.ChatMessage;
import programming3.chatsys.data.DatabaseAccessException;
import programming3.chatsys.http.protocol.JSONProtocol;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URI;
import java.util.List;

public class JSONUnreadMessagesHandler extends UnreadMessagesHandler {

    public JSONUnreadMessagesHandler(HTTPChatServer server) {
        super(server);
    }

    @Override
    protected void sendMessages(HttpExchange exchange, List<ChatMessage> messages){
        try {
            StringWriter string = new StringWriter();
            JSONProtocol protocol = new JSONProtocol(new BufferedWriter(string));
            protocol.writeMessages(messages);
            sendResponse(exchange, 200, string.toString());
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
