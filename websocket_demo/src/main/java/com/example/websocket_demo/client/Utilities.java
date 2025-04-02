package com.example.websocket_demo.client;

import java.awt.Color;

import javax.swing.border.EmptyBorder;

public class Utilities {
    public static final Color TRANSPARENT_COLOR = new Color(1, 255, 49, 1);
    public static final Color PRIMARY_COLOR = Color.decode("#71cb00");
    public static final Color SECONDARY_COLOR = Color.decode("#153000");
    public static final Color TEXT_COLOR = Color.decode("#001a3c");
    public static final Color INPUT_COLOR = Color.decode("#f4e454");

    public static EmptyBorder addPadding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }
}
