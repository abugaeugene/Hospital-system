package com.mogeni.pharma;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Doctor extends JFrame{
    private JLabel txtwelcome;
    private JLabel icondoc;
    private JLabel txtnamept;
    private JPanel DoctorPane;
    private JButton REGISTERButton;
    private JButton PRESCRIBEButton;
    private JButton LOGOUTButton;

    public Doctor(){
        setContentPane(DoctorPane);
        setTitle("PHARMA HOSPITAL");
        setSize(700,500);

        setVisible(true);

        REGISTERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterPatient registerPatient=new RegisterPatient();
                dispose();
            }
        });
        PRESCRIBEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Prescription prescription=new Prescription();
                dispose();
            }
        });
        LOGOUTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(DoctorPane,"YOU HAVE LOGGED OUT SUCCESSFULLY");
                LoginDoctor loginDoctor=new LoginDoctor();
                dispose();
            }
        });
    }

    public static void main(String[] args) {
        Doctor gui=new Doctor();
    }
}
