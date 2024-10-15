package trainsofsheffield.views.customerarea.userdetails;

import trainsofsheffield.models.Address;
import trainsofsheffield.models.Session;
import trainsofsheffield.models.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UserDetailsPanel extends JPanel {
    private JTextField nameField, surnameField, emailField;
    private JLabel nameLabel, userNameLabel, surnameLabel, userSurnameLabel, emailLabel, userEmailLabel;
    private JButton editButton;
    private String name, surname, email, street, city, postCode;

    public UserDetailsPanel(Session session) {
        super(new BorderLayout());
        User user = session.getUser();
        Address userAddress = new Address(user.getAddressID());

        // Navigation Bar
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel personalDetails = new JPanel(new BorderLayout());
        JPanel address = new JPanel(new BorderLayout());

        // Details Fields
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2));

        // first name
        setFirstName(user);
        nameLabel = new JLabel("Name: ");
        userNameLabel = new JLabel(name);
        nameField = new JTextField(name);
        nameField.setVisible(false);
        fieldsPanel.add(nameLabel);
        fieldsPanel.add(userNameLabel);
        fieldsPanel.add(nameField);

        // surname
        setSurname(user);
        surnameLabel = new JLabel("Surname: ");
        userSurnameLabel = new JLabel(surname);
        surnameField = new JTextField(surname);
        surnameField.setVisible(false);
        fieldsPanel.add(surnameLabel);
        fieldsPanel.add(userSurnameLabel);
        fieldsPanel.add(surnameField);

        // Email
        setEmail(user);
        emailLabel = new JLabel("Email: ");
        userEmailLabel = new JLabel(email);
        emailField = new JTextField(email);
        emailField.setVisible(false);
        fieldsPanel.add(emailLabel);
        fieldsPanel.add(userEmailLabel);
        fieldsPanel.add(emailField);

        // Edit button
        editButton = new JButton("Edit");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent editClicked) {
                if (editButton.getText().equals("Edit")) {
                    editButton.setText("Done");

                    userNameLabel.setVisible(false);
                    nameField.setVisible(true);

                    userSurnameLabel.setVisible(false);
                    surnameField.setVisible(true);

                    userEmailLabel.setVisible(false);
                    emailField.setVisible(true);
                } else {
                    // Get the text from the text fields
                    String newName = nameField.getText();
                    String newSurname = surnameField.getText();
                    String newEmail = emailField.getText();

                    // Check if any field is empty
                    if (newName.isEmpty() || newSurname.isEmpty() || newEmail.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    setFirstName(newName);
                    setSurname(newSurname);
                    setEmail(newEmail);

                    user.updateUserDetails(name, email, surname);

                    editButton.setText("Edit");

                    userNameLabel.setVisible(true);
                    userNameLabel.setText(name);
                    nameField.setVisible(false);

                    userSurnameLabel.setVisible(true);
                    userSurnameLabel.setText(surname);
                    surnameField.setVisible(false);

                    userEmailLabel.setVisible(true);
                    userEmailLabel.setText(email);
                    emailField.setVisible(false);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(editButton);

        personalDetails.add(fieldsPanel, BorderLayout.CENTER);
        personalDetails.add(buttonPanel, BorderLayout.SOUTH);

        // Address Fields
        JPanel addressFieldsPanel = new JPanel(new GridLayout(3, 2));

        // Street
        setStreet(userAddress);
        JLabel streetLabel = new JLabel("Street Name: ");
        JLabel userStreetLabel = new JLabel(street);
        JTextField streetField = new JTextField(street);
        streetField.setVisible(false);
        addressFieldsPanel.add(streetLabel);
        addressFieldsPanel.add(userStreetLabel);
        addressFieldsPanel.add(streetField);

        // City
        setCity(userAddress);
        JLabel cityLabel = new JLabel("City: ");
        JLabel userCityLabel = new JLabel(city);
        JTextField cityField = new JTextField(city);
        cityField.setVisible(false);
        addressFieldsPanel.add(cityLabel);
        addressFieldsPanel.add(userCityLabel);
        addressFieldsPanel.add(cityField);

        // Postcode
        setPostCode(userAddress);
        JLabel postCodeLabel = new JLabel("Postcode: ");
        JLabel userPostCodeLabel = new JLabel(postCode);
        JTextField postCodeField = new JTextField(postCode);
        postCodeField.setVisible(false);
        addressFieldsPanel.add(postCodeLabel);
        addressFieldsPanel.add(userPostCodeLabel);
        addressFieldsPanel.add(postCodeField);

        // Edit button
        JButton addressEditButton = new JButton("Edit");
        addressEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent addressEditClicked) {
                if (addressEditButton.getText().equals("Edit")) {
                    addressEditButton.setText("Done");

                    userStreetLabel.setVisible(false);
                    streetField.setVisible(true);

                    userCityLabel.setVisible(false);
                    cityField.setVisible(true);

                    userPostCodeLabel.setVisible(false);
                    postCodeField.setVisible(true);
                } else {
                    // Get the text from the text fields
                    String newStreet = streetField.getText();
                    String newCity = cityField.getText();
                    String newPostCode = postCodeField.getText();

                    // Check if any field is empty
                    if (newStreet.isEmpty() || newCity.isEmpty() || newPostCode.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    setStreet(newStreet);
                    setCity(newCity);
                    setPostCode(newPostCode);

                    userAddress.updateAddress(street, city, postCode);

                    addressEditButton.setText("Edit");

                    userStreetLabel.setVisible(true);
                    userStreetLabel.setText(street);
                    streetField.setVisible(false);

                    userCityLabel.setVisible(true);
                    userCityLabel.setText(city);
                    cityField.setVisible(false);

                    userPostCodeLabel.setVisible(true);
                    userPostCodeLabel.setText(postCode);
                    postCodeField.setVisible(false);
                }
            }
        });

        JPanel editButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        editButtonPanel.add(addressEditButton);

        address.add(addressFieldsPanel, BorderLayout.CENTER);
        address.add(editButtonPanel, BorderLayout.SOUTH);


        tabbedPane.addTab("User Details", personalDetails);
        tabbedPane.addTab("Address", address);
        add(tabbedPane);
    }

    private void setFirstName(User user) {
        this.name = user.getFirstName();
    }

    private void setFirstName(String newFirstName) {
        this.name = newFirstName;
    }

    private void setSurname(User user) {
        this.surname = user.getSurname();
    }

    private void setSurname(String newSurname) {
        this.surname = newSurname;
    }

    private void setEmail(User user) {
        this.email = user.getEmail();
    }

    private void setEmail(String newEmail) {
        this.email = newEmail;
    }

    private void setStreet(Address address) {
        this.street = address.getStreet();
    }

    private void setStreet(String newStreet) {
        this.street = newStreet;
    }

    private void setCity(Address address) {
        this.city = address.getCity();
    }

    private void setCity(String newCity) {
        this.city = newCity;
    }

    private void setPostCode(Address address) {
        this.postCode = address.getPostCode();
    }

    private void setPostCode(String newPostCode) {
        this.postCode = newPostCode;
    }
}