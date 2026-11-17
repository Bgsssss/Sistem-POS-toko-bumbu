/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bumbu;

import view.viewLogin;

/**
 *
 * @author Bagas Saputra
 */
public class AplikasiTokoBumbu {
    public static void main(String[] args) {
        Tema.terapkan();   // tema Windows, font dasar, tinggi baris tabel

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                viewLogin vL = new viewLogin();
                vL.setVisible(true);   // tampilkan login
            }
        });
    }
}
