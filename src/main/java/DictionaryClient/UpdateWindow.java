package DictionaryClient;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateWindow {

    public UpdateWindow(String word, String originalString, Dimension mainWindowSize, Client client) {
        JFrame blankFrame = new JFrame();
        blankFrame.setSize(mainWindowSize); // Set size of the blank frame
        blankFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a panel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout());

        // Create a label to display the word on the left
        JLabel wordLabel = new JLabel(word);
        wordLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create a text area to display the string on the right
        JTextArea textArea = new JTextArea(originalString);
        textArea.setEditable(false);
        textArea.setLineWrap(true); // Enable line wrap
        textArea.setWrapStyleWord(true); // Wrap at word boundaries

        // Wrap the text area in a scroll pane
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scroll bar

        // Create buttons for delete and update
        JButton deleteButton = new JButton("Delete");
        JButton updateButton = new JButton("Update");
        updateButton.setEnabled(false); // Initially disabled

        deleteButton.addActionListener(_ -> {
            ClientRequest request = new ClientRequest(RequestType.DELETE, word, null);
            client.sendData(request);
            blankFrame.dispose();
        });

        // Add action listener to the update button
        updateButton.addActionListener(_ -> {
            String updatedString = textArea.getText();
            // Check if updated string is null or empty
            if (updatedString == null || updatedString.trim().isEmpty()) {
                JOptionPane.showMessageDialog(blankFrame, "Please enter a valid string to update.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } else {
                ClientRequest request = new ClientRequest(RequestType.UPDATE, word, updatedString);
                // Send the updated string to the server
                client.sendData(request);
                blankFrame.dispose();
            }
        });

        // Create a button for edit
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setEditable(true);
                updateButton.setEnabled(false);
                editButton.setEnabled(false);
            }
        });

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(deleteButton);
        buttonPanel.add(editButton);
        buttonPanel.add(updateButton);

        // Add components to the panel
        panel.add(wordLabel, BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add the panel to the frame
        blankFrame.getContentPane().add(panel);
        blankFrame.setVisible(true);

        // Add document listener to the text area to track changes
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateButton.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (textArea.getText().equals(originalString)) {
                    updateButton.setEnabled(false);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Plain text components don't fire these events
            }
        });
    }
}
