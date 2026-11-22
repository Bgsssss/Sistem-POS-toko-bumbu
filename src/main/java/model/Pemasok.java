/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import com.bumbu.Dialog;
import database.Koneksi;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Bagas Saputra
 */
public class Pemasok {

    private String idPemasok;
    private String namaPemasok;
    private String alamat;
    private String kontak;

    Koneksi koneksi = new Koneksi();

    public String getIdPemasok() {
        return idPemasok;
    }

    public void setIdPemasok(String idPemasok) {
        this.idPemasok = idPemasok;
    }

    public String getNamaPemasok() {
        return namaPemasok;
    }

    public void setNamaPemasok(String namaPemasok) {
        this.namaPemasok = namaPemasok;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKontak() {
        return kontak;
    }

    public void setKontak(String kontak) {
        this.kontak = kontak;
    }

    public void simpanDataPemasok() {
        //inisialisasi query untuk insert kedalam database
        String sql = "INSERT INTO pemasok (namaPemasok, alamat, kontak) VALUES (?,?,?)";
        try {
            //inisialisasi preparedStatement
            PreparedStatement eksekusi = koneksi.getConnect().prepareStatement(sql);
            eksekusi.setString(1, getNamaPemasok());
            eksekusi.setString(2, getAlamat());
            eksekusi.setString(3, getKontak());
            eksekusi.executeUpdate();
            //pemberitahuan jika data berhasil disimpan
            Dialog.info(null, "Data Berhasil Disimpan");

        } catch (SQLException ex) {
            Dialog.error(null, "Data Gagal Disimpan\n" + ex);
        }

    }

    public boolean ubahDataPemasok() {
        boolean konfirmasi = Dialog.konfirmasi(null, "Apakah Anda yakin ingin mengubah data pemasok ini?");
        if (konfirmasi) {
            //inisialisasi query untuk update kedalam database
            String sql = "UPDATE pemasok SET namaPemasok = ? ,alamat = ? ,kontak = ? WHERE idPemasok = ?";
            try {
                //inisialisasi preparedStatement
                PreparedStatement eksekusi = koneksi.getConnect().prepareStatement(sql);
                eksekusi.setString(1, getNamaPemasok());
                eksekusi.setString(2, getAlamat());
                eksekusi.setString(3, getKontak());
                eksekusi.setString(4, getIdPemasok());
                eksekusi.executeUpdate();
                //pemberitahuan jika data berhasil disimpan
                Dialog.info(null, "Data Berhasil Diubah");
                return true;
            } catch (SQLException ex) {
                Dialog.error(null, "Data Gagal Diubah\n" + ex);
                return false;
            }
        }
        return false;
    }

    public boolean hapusDataPemasok() {
        boolean konfirmasi = Dialog.konfirmasi(null, "Apakah Anda yakin ingin menghapus data pemasok ini?");
        if (konfirmasi) {
            String sql = "DELETE FROM pemasok WHERE idPemasok = ?";
            try {
                //inisialisasi preparedStatement
                PreparedStatement eksekusi = koneksi.getConnect().prepareStatement(sql);
                eksekusi.setString(1, getIdPemasok());
                eksekusi.executeUpdate();
                //pemberitahuan jika data berhasil disimpan
                Dialog.info(null, "Data Berhasil Dihapus");
                return true;
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 1451) {
                    Dialog.error(null, "Pemasok tidak bisa dihapus karena masih dipakai oleh data barang");
                } else {
                    Dialog.error(null, "Data Gagal Dihapus\n" + ex);
                }
                return false;
            }
        }
        return false;
    }
}
