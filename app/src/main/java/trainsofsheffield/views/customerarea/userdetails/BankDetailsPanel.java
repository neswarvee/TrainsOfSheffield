package trainsofsheffield.views.customerarea.userdetails;

import javax.swing.*;

import trainsofsheffield.models.BankDetails;
import trainsofsheffield.models.BankDetails.CardType;
import trainsofsheffield.models.Session;
import trainsofsheffield.models.User;

import java.awt.*;
import java.awt.event.*;

public class BankDetailsPanel extends JPanel {
    private JPanel fieldsPanel, editButtoPanel;
    private JButton editButton;
    private JComboBox<CardType> cardTypeComboBox;
    private JLabel cardHolderNameLabel, cardNumberLabel, cardExpiryDateLabel, cardCVVLabel, successLabel, cardTypeLabel;
    private JTextField cardHolderNameTextField, cardNumberTextField, cardCVVTextField, cardExpiryDateField;

    public BankDetailsPanel(Session session) {
        super(new BorderLayout());
        User user = session.getUser();
        BankDetails bankDetails = new BankDetails(user.getUserID());

        // Fields
        fieldsPanel = new JPanel(new GridLayout(5, 2));

        // Card type
        cardTypeLabel = new JLabel("Card Type: ");
        CardType[] cardTypes = CardType.values();
        cardTypeComboBox = new JComboBox<>(cardTypes);
        fieldsPanel.add(cardTypeLabel);
        fieldsPanel.add(cardTypeComboBox);

        // Card Holder's name
        cardHolderNameLabel = new JLabel("Card Holder's Name: ");
        cardHolderNameTextField = new JTextField();
        fieldsPanel.add(cardHolderNameLabel);
        fieldsPanel.add(cardHolderNameTextField);

        // Card Number
        cardNumberLabel = new JLabel("Card Number: ");
        cardNumberTextField = new JTextField();
        fieldsPanel.add(cardNumberLabel);
        fieldsPanel.add(cardNumberTextField);

        // Card Exiry Date
        cardExpiryDateLabel = new JLabel("Card's Expiry Date in format mm/yy: ");
        cardExpiryDateField = new JTextField();
        fieldsPanel.add(cardExpiryDateLabel);
        fieldsPanel.add(cardExpiryDateField);

        // Card CVV
        cardCVVLabel = new JLabel("Card's CVV: ");
        cardCVVTextField = new JTextField();
        fieldsPanel.add(cardCVVLabel);
        fieldsPanel.add(cardCVVTextField);

        // Success message and edit button
        successLabel = new JLabel("Payment card details added successfully");
        successLabel.setHorizontalAlignment(JLabel.CENTER);
        editButtoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        editButton = new JButton();
        editButtoPanel.add(editButton);

        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Init view
        if (bankDetails.getBankDetailsAdded()) {
            editButton.setText("Edit Card Details");
            add(successLabel, BorderLayout.CENTER);
            add(editButtoPanel, BorderLayout.SOUTH);
        }
        else {
            editButton.setText("Done");
            add(fieldsPanel, BorderLayout.CENTER);
            add(editButtoPanel, BorderLayout.SOUTH);
        }

        // Edit button listener
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent editClicked) {
                if (editButton.getText().equals("Edit Card Details")) {
                    editButton.setText("Done");
                    remove(successLabel);
                    add(fieldsPanel, BorderLayout.CENTER);
                }
                else {
                    // Check if the fields are empty
                    if (cardHolderNameTextField.getText().isEmpty() || cardNumberTextField.getText().isEmpty() || cardExpiryDateField.getText().isEmpty() || cardCVVTextField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Validate bank details for the correct format and store them into variables
                    CardType cardType = CardType.stringToRole(cardTypeComboBox.getSelectedItem().toString());
                    String cardHolderName = cardHolderNameTextField.getText();
                    String cardNumber = cardNumberTextField.getText();
                    String cvv = cardCVVTextField.getText();
                    String expiryDate = cardExpiryDateField.getText();

                    // If the validation fails, show an error message and return
                    String validationError = BankDetails.validateBankDetails(cardHolderName, cardNumber, expiryDate, cvv);
                    if (validationError != null) {
                        JOptionPane.showMessageDialog(null, validationError, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    bankDetails.insertUpdateBankDetails(cardType, cardHolderName, cardNumber, expiryDate, cvv);

                    editButton.setText("Edit Card Details");
                    remove(fieldsPanel);
                    add(successLabel, BorderLayout.CENTER);
                }
            }
        });
    }
}