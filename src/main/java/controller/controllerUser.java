/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import database.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import security.BCrypt;

public class controllerUser {

    private static final int LOG_ROUNDS = 10;

    /** Hash BCrypt selalu 60 karakter; kolom lama varchar(20) tidak muat. */
    public static void pastikanSkema() throws SQLException {
        Connection c = Koneksi.getConnect();
        if (c == null) {
            throw new SQLException("Koneksi database gagal");
        }
    }

    public static boolean belumAdaAkun() throws SQLException {
        try (Statement st = Koneksi.getConnect().createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM `user`")) {
            rs.next();
            return rs.getInt(1) == 0;
        }
    }

    public static void buatAkun(String username, String passwordPlain) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        if (username.trim().length() > 20) {
            throw new IllegalArgumentException("Username maksimal 20 karakter");
        }
        if (passwordPlain == null || passwordPlain.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter");
        }
        if (passwordPlain.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("Password maksimal 72 byte");
        }
        String hash = BCrypt.hashpw(passwordPlain, BCrypt.gensalt(LOG_ROUNDS));
        String sql = "INSERT INTO `user` (username, password) VALUES (?, ?)";
        try (PreparedStatement ps = Koneksi.getConnect().prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, hash);
            ps.executeUpdate();
        }
    }

    /** @return username jika login benar, null jika salah. */
    public static String verifikasi(String username, String passwordPlain) throws SQLException {
        String sql = "SELECT idUser, username, password FROM `user` WHERE username=?";
        try (PreparedStatement ps = Koneksi.getConnect().prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                String tersimpan = rs.getString("password");
                int id = rs.getInt("idUser");
                String nama = rs.getString("username");

                if (tersimpan != null && tersimpan.startsWith("$2")) {
                    try {
                        return BCrypt.checkpw(passwordPlain, tersimpan) ? nama : null;
                    } catch (IllegalArgumentException e) {
                        return null; // hash rusak / format tidak dikenal
                    }
                }

                // Akun lama (password masih teks biasa): cocokkan, lalu langsung upgrade ke BCrypt.
                if (tersimpan != null && tersimpan.equals(passwordPlain)) {
                    upgradeKeBcrypt(id, passwordPlain);
                    return nama;
                }
                return null;
            }
        }
    }

    private static void upgradeKeBcrypt(int idUser, String passwordPlain) throws SQLException {
        String hash = BCrypt.hashpw(passwordPlain, BCrypt.gensalt(LOG_ROUNDS));
        try (PreparedStatement ps = Koneksi.getConnect()
                .prepareStatement("UPDATE `user` SET password=? WHERE idUser=?")) {
            ps.setString(1, hash);
            ps.setInt(2, idUser);
            ps.executeUpdate();
        }
    }
}
