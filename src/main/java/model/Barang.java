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
public class Barang {

    private String idBarang;
    private String namaBarang;
    private String kategori;
    private String beratGram;
    private String modal;
    private String hargaJual;
    private int stok;
    private String idPemasok;

    Koneksi koneksi = new Koneksi();

    public String getBeratGram() {
        return beratGram;
    }

    public void setBeratGram(String beratGram) {
        this.beratGram = beratGram;
    }

    public String getIdPemasok() {
        return idPemasok;
    }

    public void setIdPemasok(String idPemasok) {
        this.idPemasok = idPemasok;
    }

    public String getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(String idBarang) {
        this.idBarang = idBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getModal() {
        return modal;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public String getHargaJual() {
        return hargaJual;
    }

    public void setHargaJual(String hargaJual) {
        this.hargaJual = hargaJual;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public void simpanDataBarang() {
        String sql = "INSERT INTO barang (namaBarang, kategori, beratGram, modal, hargaJual, stok, idPemasok) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement eksekusi = koneksi.getConnect().prepareStatement(sql);
            eksekusi.setString(1, getNamaBarang());
            eksekusi.setString(2, getKategori());
            eksekusi.setString(3, getBeratGram());
            eksekusi.setString(4, getModal());
            eksekusi.setString(5, getHargaJual());
            eksekusi.setInt(6, getStok());
            eksekusi.setString(7, getIdPemasok());
            eksekusi.executeUpdate();

            Dialog.info(null, "Data Berhasil Disimpan");
        } catch (SQLException ex) {
            Dialog.error(null, "Data Gagal Disimpan\n" + ex.getMessage());
        }
    }

    public void ubahDataBarang() {
        String sql = "UPDATE barang SET namaBarang = ?, kategori = ?, beratGram = ?, "
                + "modal = ?, hargaJual = ?, stok = ?, idPemasok = ? WHERE idBarang = ?";

        try {
            PreparedStatement eksekusi = koneksi.getConnect().prepareStatement(sql);
            eksekusi.setString(1, getNamaBarang());
            eksekusi.setString(2, getKategori());
            eksekusi.setString(3, getBeratGram());
            eksekusi.setString(4, getModal());
            eksekusi.setString(5, getHargaJual());
            eksekusi.setInt(6, getStok());
            eksekusi.setString(7, getIdPemasok());
            eksekusi.setString(8, getIdBarang());
            eksekusi.executeUpdate();

            Dialog.info(null, "Data Berhasil Diubah");
        } catch (SQLException ex) {
            Dialog.error(null, "Data Gagal Diubah\n" + ex.getMessage());
        }
    }

    public boolean hapusDataBarang() {

        boolean konfirmasi = com.bumbu.Dialog.konfirmasi(null, "Apakah Anda yakin ingin menghapus data barang ini?");
        if (konfirmasi) {
            String sql = "DELETE FROM barang WHERE idBarang = ?";
            try {
                //inisialisasi preparedStatement
                PreparedStatement eksekusi = koneksi.getConnect().prepareStatement(sql);
                eksekusi.setString(1, getIdBarang());
                eksekusi.executeUpdate();
                //pemberitahuan jika data berhasil disimpan
                Dialog.info(null, "Data Berhasil Dihapus");
                return true;
            } catch (SQLException ex) {
                if (ex.getErrorCode() == 1451) {
                    Dialog.error(null, "Barang tidak bisa dihapus karena sudah terjual");
                } else {
                    Dialog.error(null, "Data Gagal Dihapus\n" + ex.getMessage());
                }
                return false;
            }
        }
        return false;
    }

    public void kurangiStok(String idBarang, int jumlahBeli) {
        String sql = "UPDATE barang SET stok = stok - ? WHERE idBarang = ? AND stok >= ?";
        try {
            PreparedStatement eksekusi = koneksi.getConnect().prepareStatement(sql);
            eksekusi.setInt(1, jumlahBeli);
            eksekusi.setString(2, idBarang);
            eksekusi.setInt(3, jumlahBeli);

            int affected = eksekusi.executeUpdate();

            if (affected == 0) {
                Dialog.peringatan(null, "Stok tidak cukup!");
            }
        } catch (SQLException e) {
            Dialog.error(null, "Terjadi kesalahan pada database:\n" + e.getMessage());
        }
    }
}
