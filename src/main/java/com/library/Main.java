package com.library;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        SwingUtilities.invokeLater(() -> {
            LibraryGUI gui = new LibraryGUI();
            gui.setVisible(true);
        });
    }
}
