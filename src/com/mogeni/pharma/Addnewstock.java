package com.mogeni.pharma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Addnewstock extends JFrame {
    private JPanel AddnewPanel;
    private JTextField DrugTypefield;
    private JTextField QuantityField;
    private JTextField yyyyMmDdTextField;
    private JButton ADDButton;
    private JButton BACKButton;
    private JTextField PriceField;

    public Addnewstock(){
        setContentPane(AddnewPanel);
        setSize(700,500);
        setTitle("PHARMA HOSPITAL");
        setVisible(true);
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStockToDatabase();
            }
        });
        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pharmacist pharmacist=new Pharmacist();
                dispose();
            }
        });
    }
    public void addStockToDatabase() {
        String drugName = DrugTypefield.getText();
        int quantity = Integer.parseInt(QuantityField.getText());
        int price=Integer.parseInt(PriceField.getText());
        String expiryDate = yyyyMmDdTextField.getText();


        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342","root","Connection123")) {
            String sql = "INSERT INTO tbl_drug(DrugType,Price,Date_of_expiry,QUANTITY) VALUES(?, ?, ?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, drugName);
            preparedStatement.setInt(2, price);
            preparedStatement.setString(3, expiryDate);
            preparedStatement.setInt(4,quantity);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(AddnewPanel, "Stock added successfully");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(AddnewPanel, "Error: Failed to add stock to the database");
            ex.printStackTrace();
        }
        DrugTypefield.setText("");
        QuantityField.setText("");
        PriceField.setText("");
        yyyyMmDdTextField.setText("");
    }


    public static void main(String[] args) {
        Addnewstock gui=new Addnewstock();
    }
}
