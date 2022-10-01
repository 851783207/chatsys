package programming3.chatsys.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import programming3.chatsys.data.ChatMessage;
import programming3.chatsys.data.DatabaseAccessException;
import programming3.chatsys.data.User;
import programming3.chatsys.http.protocol.JSONProtocol;

import java.io.*;
import java.net.URI;
import java.util.List;

public class JSONRecentMessagesHandler extends RecentMessagesHandler {
    public JSONRecentMessagesHandler(HTTPChatServer server) {
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
