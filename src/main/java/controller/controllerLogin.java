/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import com.bumbu.mainAppAdm;
import database.Koneksi;
import model.Sesi;
import view.viewLogin;

public class controllerLogin {

    private viewLogin vL;

    public controllerLogin(viewLogin vL) {
        this.vL = vL;
    }

    public void login() {
        try {
            String user = vL.getUsername().getText().trim();
            String pass = new String(vL.getPassword().getPassword());
            String nama = controllerUser.verifikasi(user, pass);
            if (nama != null) {
                Sesi.namaKasir = nama;
                vL.dispose();
                new mainAppAdm().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "Username atau Password Salah");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "ERROR: " + ex.getMessage());
        }
    }

}
