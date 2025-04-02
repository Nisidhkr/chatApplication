package com.example.websocket_demo.client;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.example.websocket_demo.Message;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {
    private String username;
    private MessageListner messageListner;

    public MyStompSessionHandler(MessageListner messageListner,String username) {
        this.username = username;
        this.messageListner = messageListner;
    }
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("Client connected to websocket server");
        
        session.subscribe( "/topic/messages", new StompFrameHandler() {
        
        @Override
        public Type getPayloadType (StompHeaders headers) {
            return Message.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            try{
                if (payload instanceof Message){
                    Message message = (Message) payload;
                    messageListner.onMessageRecieved(message);
                    System.out.println("Received: "+message.getUser()+":"+message.getMessage());
                } else{
                    System.out.println("Received un expected payload type: "+payload.getClass());
                }       
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        
    });
    System.out.println("Client Subscribed to /topic/messages");

    session.subscribe("/topic/users",new StompFrameHandler() {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return new ArrayList<String>().getClass(); 
        }
        @Override
        public void handleFrame(StompHeaders headers, Object payload){
            try{
                if(payload instanceof ArrayList<?>){
                    ArrayList<String> activeUsers = (ArrayList<String>) payload;
                    messageListner.onActiveUsersUpdated(activeUsers);
                    System.out.println("Received active users: "+activeUsers);
                } else{
                    System.out.println("Received un expected payload type: "+payload.getClass());
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        
    });
    System.out.println("Client Subscribed to /topic/users");
    session.send("/app/connect", username);
    session.send("/app/request-users", "");
}
    



    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        //System.out.println("handleTransportError");
        exception.printStackTrace();
    }
}
