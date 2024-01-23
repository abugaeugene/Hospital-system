package com.mogeni.pharma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Prescription extends JFrame{
    private JPanel PrescriptionPanel;
    private JTextField patientnofield;
    private JTextField docnofield;
    private JTextField drugtypefield;
    private JTextField quantityfield;
    private JButton PRESCRIBEButton;
    private JButton BACKButton;
    private JComboBox SearchComboBox;

    public Prescription(){
        setContentPane(PrescriptionPanel);
        setTitle("PHARMA HOSPITAL");
        setSize(700,500);
        setVisible(true);
        SearchDrugs();

        PRESCRIBEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PrescriptionPatient();
            }
        });
        BACKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Doctor doctor=new Doctor();
                dispose();
            }
        });
    }
public void PrescriptionPatient(){
    String patientNumber = patientnofield.getText();
    String doctorNumber = docnofield.getText();
    String drugType = drugtypefield.getText();
    String quantityText = quantityfield.getText();
    int quantity = Integer.parseInt(quantityText);

    if (patientNumber.isEmpty() || doctorNumber.isEmpty() || drugType.isEmpty() ||quantityText.isEmpty() ) {
        JOptionPane.showMessageDialog(PrescriptionPanel, "Please fill in all fields.");
        return;
    }

    if (!isPatientDoctorValid(patientNumber, doctorNumber)) {
        JOptionPane.showMessageDialog(PrescriptionPanel, "Patient or Doctor does not exist in the database.");
        return;
    }
    if (!isDrugTypeValid(drugType)) {
        JOptionPane.showMessageDialog(PrescriptionPanel, "Drug type does not exist in the database.");
        return;
    }

    if (!isQuantityAvailable(drugType, quantity)) {
        JOptionPane.showMessageDialog(PrescriptionPanel, "Prescribed quantity exceeds the available quantity for the drug.");
        return;
    }

    // Now, you can save the prescription details to the database.
    if (savePrescriptionToDatabase(patientNumber, doctorNumber, drugType, quantity)) {
        JOptionPane.showMessageDialog(PrescriptionPanel, "Prescription saved successfully.");
    } else {
        JOptionPane.showMessageDialog(PrescriptionPanel, "Failed to save prescription.");
    }
    patientnofield.setText("");
    docnofield.setText("");
    drugtypefield.setText("");
    quantityfield.setText("");

}
    private boolean isPatientDoctorValid(String patientNumber, String doctorNumber) {
        String patientQuery = "SELECT COUNT(*) FROM tbl_patient WHERE PatientNo = ?";
        String doctorQuery = "SELECT COUNT(*) FROM tbl_doctor WHERE DocNo = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342","root","Connection123")) {
            PreparedStatement patientStmt = conn.prepareStatement(patientQuery);
            patientStmt.setString(1, patientNumber);
            ResultSet patientResult = patientStmt.executeQuery();
            patientResult.next();
            int patientCount = patientResult.getInt(1);

            PreparedStatement doctorStmt = conn.prepareStatement(doctorQuery);
            doctorStmt.setString(1, doctorNumber);
            ResultSet doctorResult = doctorStmt.executeQuery();
            doctorResult.next();
            int doctorCount = doctorResult.getInt(1);

            return patientCount > 0 && doctorCount > 0; // Both patient and doctor exist if counts are greater than 0
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred during database validation
        }
    }

    // Save prescription details to the database (you need to implement this).
    private boolean savePrescriptionToDatabase(String patientNumber, String doctorNumber, String drugType, int quantity) {
        // Define your database connection and query to insert data.

        String sql = "INSERT INTO tbl_prescriptions (PatientNo,DocNo,DrugType,Quantity) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342","root","Connection123");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, patientNumber);
            pstmt.setString(2, doctorNumber);
            pstmt.setString(3, drugType);
            pstmt.setInt(4, quantity);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean isDrugTypeValid(String drugType) {
        String drugQuery = "SELECT COUNT(*) FROM tbl_drug WHERE DrugType = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342", "root", "Connection123")) {
            PreparedStatement drugStmt = conn.prepareStatement(drugQuery);
            drugStmt.setString(1, drugType);
            ResultSet drugResult = drugStmt.executeQuery();
            drugResult.next();
            int drugCount = drugResult.getInt(1);

            return drugCount > 0; // Drug type exists if count is greater than 0
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred during database validation
        }
    }

    private boolean isQuantityAvailable(String drugType, int prescribedQuantity) {
        String quantityQuery = "SELECT Quantity FROM tbl_drug WHERE DrugType = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342", "root", "Connection123")) {
            PreparedStatement quantityStmt = conn.prepareStatement(quantityQuery);
            quantityStmt.setString(1, drugType);
            ResultSet quantityResult = quantityStmt.executeQuery();

            if (quantityResult.next()) {
                int availableQuantity = quantityResult.getInt("Quantity");
                return prescribedQuantity <= availableQuantity; // Check if prescribed quantity is less than or equal to available quantity
            } else {
                return false; // Drug type not found in the database
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Error occurred during database validation
        }
    }

    private void SearchDrugs() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_eugene_mogeni_15342","root","Connection123")) {
            String fetchPrescriptionsSql = "SELECT DrugNo,DrugType, Quantity FROM tbl_drug";
            PreparedStatement preparedStatement = connection.prepareStatement(fetchPrescriptionsSql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int drugNo = resultSet.getInt("DrugNo");
                String drugType = resultSet.getString("DrugType");
                int quantity = resultSet.getInt("Quantity");

                String displayInfo = "Number: " + drugNo + ", Drug: " + drugType + ", Quantity: " + quantity;
                SearchComboBox.addItem(displayInfo);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(PrescriptionPanel, "Error fetching prescriptions from the database");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Prescription gui=new Prescription();
    }
}
