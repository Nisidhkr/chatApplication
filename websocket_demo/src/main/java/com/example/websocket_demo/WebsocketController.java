package com.example.websocket_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Simple;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@Controller
public class WebsocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketSessionMannager sessionMannager;

    @Autowired
    public WebsocketController(SimpMessagingTemplate messagingTemplate, WebSocketSessionMannager sessionMannager) {
        this.sessionMannager = sessionMannager;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/message")
    public void handleMessage(Message message) {
        System.out.println("Received message: " + message.getUser()+":"+message.getMessage());
        messagingTemplate.convertAndSend("/topic/messages", message);
        System.out.println("sent message t0 /topic/messages: "+message.getUser()+":"+message.getMessage());
    }
    @MessageMapping("/connect")
    public void connectUser(String username){
        sessionMannager.addUsername(username);
        sessionMannager.broadcastActiveUsernames();
        System.out.println(username + " connected");

    }

    @MessageMapping("/disconnect")
    public void disconnectUser(String username){
        sessionMannager.removeUsername(username);
        sessionMannager.broadcastActiveUsernames();
        System.out.println(username + " disconnected");

    }

    @MessageMapping("/request-users")
    public void requestUsers(){
        sessionMannager.broadcastActiveUsernames();
        System.out.println("Requesting users...");
    }
}
