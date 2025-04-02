package com.example.websocket_demo.client;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.util.concurrent.ExecutionException;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String username = JOptionPane.showInputDialog(null,
                        "Enter your username (Max 16 Characters): ", 
                        "Chat application", JOptionPane.QUESTION_MESSAGE);

                        if (username == null ||  username.isEmpty()|| username.length() > 16) {
                            JOptionPane.showMessageDialog(null, "Invalid username. Please try again.",
                             "Error",
                              JOptionPane.ERROR_MESSAGE);
                            return;
                        }



                try {
                    ClientGUI clientGUI = new ClientGUI(username);
                    clientGUI.setVisible(true);
                } 
                catch (ExecutionException e) {
                    throw new RuntimeException(e);    
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
