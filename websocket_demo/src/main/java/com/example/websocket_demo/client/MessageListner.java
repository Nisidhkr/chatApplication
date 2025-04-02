package com.example.websocket_demo.client;

import java.util.ArrayList;

import com.example.websocket_demo.Message;

public interface MessageListner {
    void onMessageRecieved(Message message);
    void onActiveUsersUpdated(ArrayList<String> activeUsers);

}
