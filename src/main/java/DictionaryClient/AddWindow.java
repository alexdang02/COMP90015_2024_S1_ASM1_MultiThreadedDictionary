package DictionaryClient;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddWindow {

    public AddWindow(JFrame parentFrame, Client client, String text) {
        JFrame addFrame = new JFrame();
        addFrame.setSize(parentFrame.getSize());
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel wordLabel = new JLabel("Word:");
        panel.add(wordLabel, gbc);

        gbc.gridx++;
        JTextField wordField = new JTextField(20);
        wordField.setText(text);
        panel.add(wordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel descriptionLabel = new JLabel("Description:");
        panel.add(descriptionLabel, gbc);

        gbc.gridx++;
        JTextField descriptionField = new JTextField(20);
        panel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        JButton addButton = new JButton("Add");
        addButton.setEnabled(false);
        panel.add(addButton, gbc);

        // Add action listener to enable/disable add button based on text fields' content
        wordField.getDocument().addDocumentListener(new TextFieldDocumentListener(addButton, wordField, descriptionField));
        descriptionField.getDocument().addDocumentListener(new TextFieldDocumentListener(addButton, wordField, descriptionField));

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = wordField.getText();
                String description = descriptionField.getText();
                // Add your logic to handle adding the entry here
                ClientRequest request = new ClientRequest(RequestType.ADD, word, description);
                client.sendData(request);
                addFrame.dispose();
            }
        });

        addFrame.getContentPane().add(panel);
        addFrame.setVisible(true);
    }

    // Document listener to enable/disable add button based on text fields' content
    private class TextFieldDocumentListener implements javax.swing.event.DocumentListener {
        private JButton addButton;
        private JTextField wordField;
        private JTextField descriptionField;

        public TextFieldDocumentListener(JButton addButton, JTextField wordField, JTextField descriptionField) {
            this.addButton = addButton;
            this.wordField = wordField;
            this.descriptionField = descriptionField;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            enableButton();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            enableButton();
        }

        private void enableButton() {
            if (!wordField.getText().isEmpty() && !descriptionField.getText().isEmpty()) {
                addButton.setEnabled(true);
            } else {
                addButton.setEnabled(false);
            }
        }
    }
}
