/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bumbu;

import controller.controllerUser;
import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import view.viewLogin;

/**
 *
 * @author Bagas Saputra
 */
public class AplikasiTokoBumbu {

    public static void main(String[] args) {
        Tema.terapkan();   // tema Windows, font dasar, tinggi baris tabel

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (!siapkanAkun()) {
                    System.exit(0);
                }
                viewLogin vL = new viewLogin();
                vL.setVisible(true);   // tampilkan login
            }
        });
    }

    private static boolean siapkanAkun() {
        try {
            controllerUser.pastikanSkema();
            if (!controllerUser.belumAdaAkun()) {
                return true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database tidak siap: " + e.getMessage());
            return false;
        }

        while (true) {
            JTextField user = new JTextField("admin");
            JPasswordField pass = new JPasswordField();
            JPasswordField ulang = new JPasswordField();
            JPanel panel = new JPanel(new GridLayout(0, 1, 0, 4));
            panel.add(new JLabel("Belum ada akun. Buat akun admin pertama:"));
            panel.add(new JLabel("Username"));
            panel.add(user);
            panel.add(new JLabel("Password (min. 6 karakter)"));
            panel.add(pass);
            panel.add(new JLabel("Ulangi password"));
            panel.add(ulang);

            int pilih = JOptionPane.showConfirmDialog(null, panel, "Setup Akun Pertama",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (pilih != JOptionPane.OK_OPTION) {
                return false;
            }
            String p1 = new String(pass.getPassword());
            if (!p1.equals(new String(ulang.getPassword()))) {
                JOptionPane.showMessageDialog(null, "Password dan ulangi password tidak sama");
                continue;
            }
            try {
                controllerUser.buatAkun(user.getText(), p1);
                JOptionPane.showMessageDialog(null, "Akun admin berhasil dibuat. Silakan login.");
                return true;
            } catch (IllegalArgumentException | SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
}
