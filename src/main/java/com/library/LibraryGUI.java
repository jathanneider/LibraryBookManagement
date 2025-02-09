package com.library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LibraryGUI extends JFrame {
    private JTextField searchField;
    private JTextArea resultArea;
    private JButton searchButton, recommendButton;
    private BookDAO bookDAO;

    public LibraryGUI() {
        bookDAO = new BookDAO();

        setTitle("Library Book Management System");
        setSize(600, 400);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Adds spacing

        searchField = new JTextField(30);
        searchButton = new JButton("Search");
        recommendButton = new JButton("Recommend Something");

        topPanel.add(new JLabel("Search (Title, Author, ISBN): "));
        topPanel.add(searchField);
        topPanel.add(searchButton);
        topPanel.add(recommendButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setPreferredSize(new Dimension(550, 250));
        JScrollPane scrollPane = new JScrollPane(resultArea);


        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText();
                if (!query.isEmpty()) {
                    List<String> results = bookDAO.searchBooks(query);
                    resultArea.setText(String.join("\n", results));
                } else {
                    resultArea.setText("Please enter a search term.");
                }
            }
        });

        recommendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String recommendation = bookDAO.getRandomBook();
                resultArea.setText(recommendation);
            }
        });
    }
}
