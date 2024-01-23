package com.mogeni.pharma;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomePage extends JFrame {
    private JPanel panel1;
    private JButton DOCTORButton;
    private JButton PHARMACISTButton;
    private JPanel Whitepanel;
    private ImageIcon image;

    public HomePage(){
        setSize(700,500);
        setContentPane(panel1);

        setTitle("PHARMA HOSPITAL");


        setVisible(true);
        DOCTORButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginDoctor loginDoctor=new LoginDoctor();
                dispose();
            }
        });
        PHARMACISTButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginPharmacist loginPharmacist=new LoginPharmacist();
                dispose();
            }
        });
    }




    public static void main(String[] args) {
        HomePage gui=new HomePage();
    }
}
