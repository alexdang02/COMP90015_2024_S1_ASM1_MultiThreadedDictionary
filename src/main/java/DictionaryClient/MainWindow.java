package DictionaryClient;

import javax.swing.*;
import java.awt.*;

public class MainWindow implements ClientResponseListener {

    public JFrame frame;
    private JTextField textField;
    private final Client client;
    private String lastSearch;
    private JTextField textBox;

    public MainWindow(Client client) {
        this.client = client;
        client.setListener(this);
        initializeGUI();
    }



    public void onClientResponseReceived(ServerReply serverReply) {
        if (serverReply.replyCode == 500){
            JOptionPane.showMessageDialog(null, serverReply.warning, "SERVER INTERNAL ERROR", JOptionPane.INFORMATION_MESSAGE);
        }
        else if (serverReply.replyCode == 400) {
            JOptionPane.showMessageDialog(null, serverReply.warning, "BAD REQUEST", JOptionPane.INFORMATION_MESSAGE);
        }

        if (serverReply.requestType == RequestType.SEARCH) {
            if (serverReply.replyCode == 302) {
                GridBagConstraints gbc_textBox = new GridBagConstraints();
                gbc_textBox.insets = new Insets(0, 5, 5, 5);
                gbc_textBox.anchor = GridBagConstraints.WEST; // Align to the left
                gbc_textBox.weightx = 0.5;
                gbc_textBox.fill = GridBagConstraints.HORIZONTAL;
                gbc_textBox.gridx = 0;
                gbc_textBox.gridy = 2;
                gbc_textBox.gridwidth = 1;
                textBox = new JTextField("WORD FOUND");

                frame.getContentPane().add(textBox, gbc_textBox);
                textBox.setColumns(10);

                GridBagConstraints gbc_extendButton = new GridBagConstraints();
                gbc_extendButton.insets = new Insets(0, 5, 5, 5); // Adjusted insets for proper spacing
                gbc_extendButton.anchor = GridBagConstraints.WEST; // Align to the left
                gbc_extendButton.gridx = 0;
                gbc_extendButton.gridy = 3; // Adjusted to place it below the text box
                final JButton extendButton = getExtendButton(textBox, serverReply.replyData);

                frame.getContentPane().add(extendButton, gbc_extendButton);
                frame.getContentPane().revalidate();
                frame.getContentPane().repaint();
            } else if (serverReply.replyCode == 404) {
                GridBagConstraints gbc_textBox = new GridBagConstraints();
                gbc_textBox.insets = new Insets(0, 5, 5, 5);
                gbc_textBox.anchor = GridBagConstraints.WEST; // Align to the left
                gbc_textBox.weightx = 0.5;
                gbc_textBox.fill = GridBagConstraints.HORIZONTAL;
                gbc_textBox.gridx = 0;
                gbc_textBox.gridy = 2;
                gbc_textBox.gridwidth = 1;
                textBox = new JTextField("WORD NOT FOUND");


                frame.getContentPane().add(textBox, gbc_textBox);
                frame.getContentPane().revalidate();
                frame.getContentPane().repaint();
            }

        }
        else if (serverReply.requestType == RequestType.UPDATE) {
            if (serverReply.replyCode == 200) {
                JOptionPane.showMessageDialog(null, serverReply.warning, "SUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
            } else if (serverReply.replyCode == 302) {
                JOptionPane.showMessageDialog(null, serverReply.warning, "UNSUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if (serverReply.requestType == RequestType.ADD) {
            if (serverReply.replyCode == 201) {
                JOptionPane.showMessageDialog(null, serverReply.warning, "SUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
            } else if (serverReply.replyCode == 409) {
                JOptionPane.showMessageDialog(null, serverReply.warning, "UNSUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        else if (serverReply.requestType == RequestType.DELETE) {
            if (serverReply.replyCode == 200) {
                JOptionPane.showMessageDialog(null, serverReply.warning, "SUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
            } else if (serverReply.replyCode == 404) {
                JOptionPane.showMessageDialog(null, serverReply.warning, "SUCCESSFUL", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private JButton getExtendButton(JTextField textBox, String replyData) {
        JButton extendButton = new JButton("Extend");
        extendButton.addActionListener(_ -> {
            openNewFrame(replyData);
            textField.setText("");
            frame.getContentPane().remove(textBox);
            frame.getContentPane().remove(extendButton);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
        });
        return extendButton;
    }

    private void openNewFrame(String replyData) {
        new UpdateWindow(lastSearch, replyData, frame.getSize(), client);
    }

    @Override
    public void onError(Exception e) {

    }


    private void initializeGUI() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[]{0, 0, 0};
        gridBagLayout.rowHeights = new int[]{0, 0, 0};
        gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
        frame.getContentPane().setLayout(gridBagLayout);

        textField = new JTextField();
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.insets = new Insets(0, 0, 5, 5);
        gbc_textField.weightx = 0.5;
        gbc_textField.fill = GridBagConstraints.HORIZONTAL;
        gbc_textField.gridx = 0;
        gbc_textField.gridy = 0;
        frame.getContentPane().add(textField, gbc_textField);
        textField.setColumns(10);

        JButton searchBtn = new JButton("Search");
        JButton addBtn = new JButton("Add");

        GridBagConstraints gbc_searchBtn = new GridBagConstraints();
        gbc_searchBtn.insets = new Insets(0, 0, 5, 5);
        gbc_searchBtn.gridx = 1;
        gbc_searchBtn.gridy = 0;
        frame.getContentPane().add(searchBtn, gbc_searchBtn);

        GridBagConstraints gbc_addBtn = new GridBagConstraints();
        gbc_addBtn.insets = new Insets(0, 0, 5, 0);
        gbc_addBtn.gridx = 2; // Adjust the gridx for the Add button
        gbc_addBtn.gridy = 0; // Keep the same gridy for the Add button
        frame.getContentPane().add(addBtn, gbc_addBtn);

        JTextArea textArea = new JTextArea();
        GridBagConstraints gbc_textArea = new GridBagConstraints();
        gbc_textArea.insets = new Insets(0, 0, 0, 5);
        gbc_textArea.fill = GridBagConstraints.BOTH;
        gbc_textArea.gridx = 0;
        gbc_textArea.gridy = 1;
        gbc_textArea.gridwidth = 3; // Span the text area across 3 columns
        frame.getContentPane().add(textArea, gbc_textArea);

        searchBtn.addActionListener(_ -> {
            lastSearch = textField.getText();
            ClientRequest request = new ClientRequest(RequestType.SEARCH, lastSearch, null);
            client.sendData(request);
        });

        addBtn.addActionListener(_ -> {
            lastSearch = textField.getText();
            textField.setText("");
            frame.getContentPane().remove(textBox);
            frame.getContentPane().revalidate();
            frame.getContentPane().repaint();
            new AddWindow(frame, client, lastSearch);


        });


    }
}
