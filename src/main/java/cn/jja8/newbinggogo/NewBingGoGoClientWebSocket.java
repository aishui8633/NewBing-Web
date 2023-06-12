package cn.jja8.NewBing;

import fi.iki.elonen.NanoWSD;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.Map;

public class NewBingClientWebSocket extends WebSocketClient {
    NewBingServerWebSocket NewBingServerWebSocket;
    LinkedList<String> messList;
    public NewBingClientWebSocket(URI serverUri,Map<String,String> httpHeaders,NewBingServerWebSocket NewBingServerWebSocket,LinkedList<String> messList) {
        super(serverUri,httpHeaders);
        this.NewBingServerWebSocket = NewBingServerWebSocket;
        this.messList = messList;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        for (String s : messList) {
            send(s);
        }
    }


    @Override
    public void onMessage(String message) {
        try {
            NewBingServerWebSocket.send(message);
        } catch (IOException e) {
            close();
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        NanoWSD.WebSocketFrame.CloseCode rcode = NanoWSD.WebSocketFrame.CloseCode.find(code);
        if(rcode==null){
            rcode = NanoWSD.WebSocketFrame.CloseCode.NormalClosure;
        }
        try {
            NewBingServerWebSocket.close(rcode,reason,false);
        } catch (IOException e) {

        }
    }

    @Override
    public void onError(Exception ex) {
        close();
        try {
            String errorMessage = "{\"type\": 2,\"result\":{\"value\":\"Error\",\"message\":\"魔法服务器连接到bing聊天时发生错误"+ex+"\"}}";
            NewBingServerWebSocket.send(errorMessage);
            NewBingServerWebSocket.close(NanoWSD.WebSocketFrame.CloseCode.NormalClosure,"error",false);
        } catch (IOException e) {

        }

    }
}
