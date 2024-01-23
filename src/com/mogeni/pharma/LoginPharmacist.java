package com.mogeni.pharma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPharmacist extends JFrame {
    private JPanel PharmacistPanel;
    private JTextField textusername;
    private JButton LOGINButton;
    private JPasswordField textpassword;
    private JLabel text_username;
    private JLabel text_password;
    private JButton REGISTERButton;
    private JButton backhome;
    private String username;
    private String password;

    public LoginPharmacist(){
        setContentPane(PharmacistPanel);
        setSize(700,500);
        setTitle("PHARMA HOSPITAL");

        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirm_login();

            }
        });


        setVisible(true);
        REGISTERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterPharmacist registerPharmacist=new RegisterPharmacist();
                dispose();
            }
        });
        backhome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomePage homePage=new HomePage();
                dispose();
            }
        });
    }
    public void confirm_login(){
        username = textusername.getText();
        password = String.valueOf(textpassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(PharmacistPanel, "PLEASE FILL ALL FIELDS", "TRY AGAIN", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Connect to the database and check for the doctor's credentials
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342","root","Connection123");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tbl_pharmacist WHERE UserName = ? AND Pharmapassword = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Login successful, open the doctor's interface
                JOptionPane.showMessageDialog(PharmacistPanel, "LOGIN SUCCESSFULLY");
                 Pharmacist pharmacist=new Pharmacist();
                dispose();
            } else {
                // Credentials do not match, prompt to register
                int choice = JOptionPane.showConfirmDialog(PharmacistPanel, "Pharmacist does not exist. Do you want to register as a new Pharmacist?", "Register Pharmacist", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    RegisterPharmacist registerPharmacist = new RegisterPharmacist();
                    dispose();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LoginPharmacist gui=new LoginPharmacist();
    }
}
