package com.example.websocket_demo.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.Component; // Explicitly import java.awt.Component
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
// Removed unused import to avoid ambiguity
//import org.springframework.stereotype.Component;

import com.example.websocket_demo.Message;


public class ClientGUI extends JFrame implements MessageListner {
    private JPanel connectedUserPanel,messagePanel;
    private MyStompClient myStompClient;
    private String username;
    private JScrollPane messagePanelScrollPane;

    public ClientGUI(String username ) throws ExecutionException, InterruptedException {
        super("user: "+username);
        this.username = username;
        myStompClient = new MyStompClient(this,username);
        setSize(1218,685);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                int option=JOptionPane.showConfirmDialog(ClientGUI.this,"Kya app band karna chate ho isey ? ","Exit",JOptionPane.YES_NO_OPTION);
                if (option== JOptionPane.YES_OPTION){
                    myStompClient.disconnect(username);
                    ClientGUI.this.dispose();
                }
            }
        });

        addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e){
              updateMessageSize();
            }
        });


        getContentPane().setBackground(Utilities.PRIMARY_COLOR);
        addGuiComponents();
    }

    private void addGuiComponents() {
        addConnectedUsersComponent();
        addChatComponent();

    }
    private void addConnectedUsersComponent() {
        connectedUserPanel = new JPanel();
        connectedUserPanel.setBorder(Utilities.addPadding(10, 10, 10, 10));
        connectedUserPanel.setLayout(new BoxLayout(connectedUserPanel,BoxLayout.Y_AXIS));
        connectedUserPanel.setBackground(Utilities.SECONDARY_COLOR);
        connectedUserPanel.setPreferredSize(new Dimension(200, getHeight()));

        JLabel connectedusersLabel = new JLabel("Connected Users");
        connectedusersLabel.setFont(new Font("Arial", Font.BOLD, 18));
        connectedusersLabel.setForeground(Utilities.TEXT_COLOR);
        connectedUserPanel.add(connectedusersLabel);

        add(connectedUserPanel, BorderLayout.WEST);
        
    }

    private void addChatComponent(){
        JPanel chatPanel= new JPanel();
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel,BoxLayout.Y_AXIS));
        messagePanel.setBackground(Utilities.TRANSPARENT_COLOR);

        messagePanelScrollPane = new JScrollPane(messagePanel);
        messagePanelScrollPane.setBorder(Utilities.addPadding(10, 10, 10, 10));
        messagePanelScrollPane.setBackground(Utilities.TRANSPARENT_COLOR);
        messagePanelScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        messagePanelScrollPane.getVerticalScrollBar().setUnitIncrement(18);
        messagePanelScrollPane.getViewport().addChangeListener(new ChangeListener() {
    @Override
    public void stateChanged(ChangeEvent e) {
        revalidate();
        repaint();
    }
    });
        chatPanel.add(messagePanelScrollPane,BorderLayout.CENTER);
        
        //messagePanel.add(createChatMessageComponent(new Message("user1","Hello world!")));


        //messagePanel.add(new JLabel("Chat Messages") ) ;
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(Utilities.addPadding(100, 10, 10, 10));
        inputPanel.setLayout(new BoxLayout(inputPanel,BoxLayout.X_AXIS));
        inputPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        JTextField inputField = new JTextField();
        inputField.addKeyListener(new KeyAdapter(){
            @Override
            public void keyTyped(KeyEvent e){
                if (e.getKeyChar() == KeyEvent.VK_ENTER){
                    String input = inputField.getText();

                    // edge case : empty message(prevent sending empty message)

                    if (input.isEmpty()){
                        return;
                    }
                    
                    inputField.setText("");
                    
                    myStompClient.sendMessage(new Message(username, input));
                }
            }

        });
        inputField.setBackground(Utilities.INPUT_COLOR);
        inputField.setForeground(Utilities.TEXT_COLOR);
        inputField.setBorder(Utilities.addPadding(10, 10, 10, 10));
        inputField.setFont(new Font("Inter", Font.PLAIN, 16));
        inputField.setPreferredSize(new Dimension(inputPanel.getWidth(),50));
        inputPanel.add(inputField,BorderLayout.CENTER);
        chatPanel.add(inputPanel,BorderLayout.SOUTH);


        add(chatPanel,BorderLayout.CENTER);

    }

    private JPanel createChatMessageComponent(Message message){
        JPanel chatMessage = new JPanel();
        chatMessage.setLayout(new BoxLayout(chatMessage,BoxLayout.Y_AXIS));
        chatMessage.setBackground(Utilities.TRANSPARENT_COLOR); 
        chatMessage.setBorder(Utilities.addPadding(20, 20, 10, 20));

        JLabel userLabel = new JLabel(message.getUser());
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        userLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(userLabel);
        JLabel messageLabel = new JLabel();
        messageLabel.setText(
            "<html>"+
                " <body style ='width: " + (int)(0.60 * getWidth()) + "'px;>" +
                    message.getMessage() +
                "</body>" +
            "</html>");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setForeground(Utilities.TEXT_COLOR);
        chatMessage.add(messageLabel);
        return chatMessage;


    }

    @Override
    public void onActiveUsersUpdated(ArrayList<String> activeUsers) {
        // removeAllConnectedUsers(); // remove all connected users before adding new ones whch should be the secondary component in the panel 
        // the user list panel dosent get added until after and this is mainly for when the user get updated

        
        if(connectedUserPanel.getComponentCount() >= 2) {
            connectedUserPanel.remove(1); // remove the previous list of users
        }
        
        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BoxLayout(userListPanel,BoxLayout.Y_AXIS));
        userListPanel.setBackground(Utilities.TRANSPARENT_COLOR);

        for (String user : activeUsers) {
            JLabel username = new JLabel(user);
            username.setFont(new Font("Arial", Font.PLAIN, 16));
            username.setForeground(Utilities.TEXT_COLOR);
            userListPanel.add(username);
        }

        connectedUserPanel.add(userListPanel);
        revalidate();
        repaint(); 

    }

    @Override
    public void onMessageRecieved(Message message) {
        messagePanel.add(createChatMessageComponent(message));
        revalidate();
        repaint();
        
        messagePanelScrollPane.getVerticalScrollBar().setValue(messagePanelScrollPane.getVerticalScrollBar().getMaximum());

    }
    private void updateMessageSize() {
        
        for (int i = 0;i<messagePanel.getComponents().length;i++){
            Component component = messagePanel.getComponent(i);
            if(component instanceof JPanel){
                JPanel chatMessage = (JPanel) component;
                if (chatMessage.getComponent(1) instanceof JLabel){
                    JLabel messageLabel = (JLabel) chatMessage.getComponent(1);
                    messageLabel.setText(
                        "<html>"+
                            " <body style ='width: " + (int)(0.60 * getWidth()) + "'px;>" +
                                messageLabel.getText() +
                            "</body>" +
                        "</html>");
                }
            }
        }      
    }
}
