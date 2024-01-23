package com.mogeni.pharma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Pharmacist extends JFrame{
    private JPanel PharmaPanel;
    private JButton CLICKButton;
    private JButton SELLButton;
    private JButton REMOVEButton;
    private JButton LogOutButton;
    private JButton SHOWEXPIREDDRUGSButton;
    private JComboBox PatientComboBox;

    public Pharmacist(){
        setContentPane(PharmaPanel);
        setSize(700,600);
        setTitle("PHARMA HOSPITAL");

        CLICKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Addnewstock addnewstock=new Addnewstock();
                dispose();
            }
        });
        populatePrescriptions();
        setVisible(true);
        LogOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginPharmacist loginPharmacist=new LoginPharmacist();
                dispose();
            }
        });
        SHOWEXPIREDDRUGSButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExpiredDrugs();
            }
        });
        REMOVEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeExpiredStockFromDatabase();
            }
        });
        SELLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sellDrugsToSelectedPatient();
            }
        });
    }
    public void showExpiredDrugs() {
         // SQLite database URL
        boolean hasExpiredDrugs = false;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342","root","Connection123")) {
            String sql = "SELECT * FROM tbl_drug WHERE Date_of_expiry < CURDATE()";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            StringBuilder expiredDrugs = new StringBuilder();
            while (resultSet.next()) {
                hasExpiredDrugs = true;
                expiredDrugs.append("Drug Name: ").append(resultSet.getString("DrugType")).append(", Expiry Date: ").append(resultSet.getString("Date_of_expiry")).append("\n");
            }

            if (hasExpiredDrugs) {
                JOptionPane.showMessageDialog(PharmaPanel, expiredDrugs.toString(), "Expired Drugs", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(PharmaPanel, "No expired drugs found.", "Expired Drugs", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(PharmaPanel, "Error: Failed to retrieve expired drugs from the database");
            ex.printStackTrace();
        }
    }
    private void removeExpiredStockFromDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342", "root", "Connection123")) {
            String sql = "DELETE FROM tbl_drug WHERE Date_of_expiry < CURDATE()";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(PharmaPanel, "Expired stock removed successfully");
            } else {
                JOptionPane.showMessageDialog(PharmaPanel, "No expired stock to remove");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(PharmaPanel, "Error: Failed to remove expired stock from the database");
            ex.printStackTrace();
        }
    }
    private void sellDrugsToSelectedPatient() {
        String selectedPatient = (String) PatientComboBox.getSelectedItem();

        // Extracting the patient number and quantity
        String[] parts = selectedPatient.split(", ");
        String patientInfo = parts[0];  // This is "Patient: 1"
        String patientNumberStr = patientInfo.split(": ")[1];  // This extracts "1" from "Patient: 1"
        int selectedPatientNumber = Integer.parseInt(patientNumberStr);  // Parsing "1" to an integer

        String quantityInfo = parts[2];  // This is "Quantity: 5"
        String quantityStr = quantityInfo.split(": ")[1];  // This extracts "5" from "Quantity: 5"
        int selectedQuantity = Integer.parseInt(quantityStr);  // Parsing "5" to an integer

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342","root","Connection123")) {
            // Use the extracted patient number
            String fetchPrescriptionSql = "SELECT DrugType, Quantity FROM tbl_prescriptions WHERE PatientNo = ?";
            PreparedStatement fetchStatement = connection.prepareStatement(fetchPrescriptionSql);
            fetchStatement.setInt(1, selectedPatientNumber);
            ResultSet resultSet = fetchStatement.executeQuery();

            if (resultSet.next()) {
                String drugType = resultSet.getString("DrugType");
                int quantityPrescribed = resultSet.getInt("Quantity");

                // Fetch price for the drug from tbl_Drug
                String fetchDrugPriceSql = "SELECT Price FROM tbl_drug WHERE DrugType = ?";
                PreparedStatement fetchPriceStatement = connection.prepareStatement(fetchDrugPriceSql);
                fetchPriceStatement.setString(1, drugType);
                ResultSet priceResultSet = fetchPriceStatement.executeQuery();

                if (priceResultSet.next()) {
                    int price = priceResultSet.getInt("Price");

                    // Calculate total price
                    int totalPrice = price * selectedQuantity;  // Using the selected quantity, not the prescribed quantity
                    String updateQuantitySql = "UPDATE tbl_drug SET Quantity = Quantity - ? WHERE DrugType = ?";
                    PreparedStatement updateQuantityStatement = connection.prepareStatement(updateQuantitySql);
                    updateQuantityStatement.setInt(1, quantityPrescribed);
                    updateQuantityStatement.setString(2, drugType);
                    updateQuantityStatement.executeUpdate();
                    String insertOrderSql = "INSERT INTO tbl_orders (PatientNo, Quantity, Price, Order_date) VALUES (?, ?, ?, CURDATE())";
                    PreparedStatement insertOrderStatement = connection.prepareStatement(insertOrderSql);
                    insertOrderStatement.setInt(1, selectedPatientNumber);
                    insertOrderStatement.setInt(2, selectedQuantity);
                    insertOrderStatement.setInt(3, totalPrice);
                    insertOrderStatement.executeUpdate();

                    // Remove from tbl_prescriptions
                    String deletePrescriptionSql = "DELETE FROM tbl_prescriptions WHERE PatientNo = ?";
                    PreparedStatement deletePrescriptionStatement = connection.prepareStatement(deletePrescriptionSql);
                    deletePrescriptionStatement.setInt(1, selectedPatientNumber);
                    deletePrescriptionStatement.executeUpdate();

                    // Display selling informations
                    JOptionPane.showMessageDialog(PharmaPanel, "Selling " + selectedQuantity + " units of " + drugType + " to Patient " + selectedPatientNumber + " for a total price of $" + totalPrice);
                    DefaultComboBoxModel model = (DefaultComboBoxModel) PatientComboBox.getModel();
                    String selected = (String) PatientComboBox.getSelectedItem();
                    model.removeElement(selected);
                } else {
                    JOptionPane.showMessageDialog(PharmaPanel, "Drug price not found for " + drugType);
                }
            } else {
                JOptionPane.showMessageDialog(PharmaPanel, "No prescription found for Patient " + selectedPatientNumber);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(PharmaPanel, "Error in selling drugs");
            ex.printStackTrace();
        }
    }

    private void populatePrescriptions() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342","root","Connection123")) {
            String fetchPrescriptionsSql = "SELECT PatientNo, DrugType, Quantity FROM tbl_prescriptions";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchPrescriptionsSql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int patientNo = resultSet.getInt("PatientNo");
                String drugType = resultSet.getString("DrugType");
                int quantity = resultSet.getInt("Quantity");

                String displayInfo = "Patient: " + patientNo + ", Drug: " + drugType + ", Quantity: " + quantity;
                PatientComboBox.addItem(displayInfo);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(PharmaPanel, "Error fetching prescriptions from the database");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Pharmacist gui=new Pharmacist();

    }
}
