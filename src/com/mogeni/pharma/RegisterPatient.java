package com.mogeni.pharma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterPatient extends JFrame {
    private JPanel PatientPanel;
    private JTextField textname;
    private JTextField textgender;
    private JTextField textcontact;
    private JButton REGISTERButton;
    private JButton BACKButton;
    private JLabel txtname;
    private JLabel txtgender;
    private JLabel txtcontact;
    private String name;
    private String gender;
    private String contact;


    public RegisterPatient(){
        setContentPane(PatientPanel);
        setSize(700,500);
        setTitle("PHARMA HOSPITAL");

        setVisible(true);
        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Doctor doctor=new Doctor();
                dispose();
            }
        });
        REGISTERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    register_patient();
            }
        });
    }
    public void register_patient(){
        name=textname.getText();
        gender=textgender.getText();
        contact= textcontact.getText();


        if (name.isEmpty()||gender.isEmpty()||contact.isEmpty()){
            JOptionPane.showMessageDialog(PatientPanel,"PLEASE FILL ALL FIELDS","TRY AGAIN",JOptionPane.ERROR_MESSAGE);
            return;
        }

        else{
            JOptionPane.showMessageDialog(PatientPanel,"REGISTERED SUCCESSFULLY");
            Doctor doctor=new Doctor();
            dispose();
        }
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342", "root", "Connection123");
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tbl_patient( PatientName,Gender,Contact) VALUES(?,?,?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, gender);
            preparedStatement.setString(3, contact);
            int resultSet = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        RegisterPatient gui=new RegisterPatient();
    }
}
