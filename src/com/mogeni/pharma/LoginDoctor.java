package com.mogeni.pharma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginDoctor extends JFrame{
    private JPanel DoctorLogin;
    private JTextField textusername;
    private JLabel txtusername;
    private JPasswordField textpassword;
    private JLabel txtpassword;
    private JButton REGISTERButton;
    private JButton LOGINButton;
    private JButton BACKTOHOMEPAGEButton;
    private String username;
    private String password;

    public LoginDoctor(){
        setContentPane(DoctorLogin);
        setTitle("PHARMA HOSPITAL");
        setSize(700,500);
        setVisible(true);

        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirm_login();

            }
        });
        REGISTERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterDoctor registerDoctor=new RegisterDoctor();
                dispose();
            }
        });
        BACKTOHOMEPAGEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomePage homePage=new HomePage();
                dispose();
            }
        });
    }
    public void confirm_login() {
        username = textusername.getText();
        password = String.valueOf(textpassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(DoctorLogin, "PLEASE FILL ALL FIELDS", "TRY AGAIN", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Connect to the database and check for the doctor's credentials
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342","root","Connection123");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM tbl_doctor WHERE UserName = ? AND Docpassword = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Login successful, open the doctor's interface
                JOptionPane.showMessageDialog(DoctorLogin, "LOGIN SUCCESSFULLY");
                Doctor doctor = new Doctor();
                dispose();
            } else {
                // Credentials do not match, prompt to register
                int choice = JOptionPane.showConfirmDialog(DoctorLogin, "Doctor does not exist. Do you want to register as a new doctor?", "Register Doctor", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    RegisterDoctor registerDoctor = new RegisterDoctor();
                    dispose();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        LoginDoctor gui=new LoginDoctor();
    }
}
