package com.mogeni.pharma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RegisterPharmacist extends JFrame {
    private JPanel RegisterPanel;
    private JTextField textname;
    private JTextField textusername;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JLabel pass;
    private JButton REGISTERButton;
    private JButton BACKButton;

    String name;
    String username;
    String password;
    String confirm_password;

    public RegisterPharmacist(){
        setContentPane(RegisterPanel);
        setSize(700,500);
        setTitle("PHARMA HOSPITAL");

        REGISTERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register_pharmacist();
            }
        });
        setVisible(true);
        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginPharmacist loginPharmacist=new LoginPharmacist();
                dispose();
            }
        });
    }
    public void register_pharmacist() {
        name = textname.getText();
        username = textusername.getText();
        password = String.valueOf(passwordField1.getPassword());
        confirm_password = String.valueOf(passwordField2.getPassword());

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirm_password.isEmpty()) {
            JOptionPane.showMessageDialog(RegisterPanel, "PLEASE FILL ALL FIELDS", "TRY AGAIN", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirm_password)) {
            JOptionPane.showMessageDialog(RegisterPanel, "PASSWORDS DO NÓT MATCH", "TRY AGAIN", JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            JOptionPane.showMessageDialog(RegisterPanel, "REGISTERED SUCCESSFULLY");
            LoginPharmacist loginPharmacist = new LoginPharmacist();
            dispose();
        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342", "root", "Connection123");
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tbl_pharmacist( PharmaName,Pharmapassword,UserName) VALUES(?,?,?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, username);
            int resultSet = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
    public static void main (String[]args){
        RegisterPharmacist gui = new RegisterPharmacist();
    }
}
