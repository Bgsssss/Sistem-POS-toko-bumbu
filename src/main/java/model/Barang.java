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
    private String modal;
    private String hargaJual;
    private int stok;
    private String idPemasok;

    Koneksi koneksi = new Koneksi();

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
        //inisialisasi query untuk insert kedalam database
        String sql = ("INSERT INTO barang (namaBarang, kategori, modal, hargaJual, stok, idPemasok) "
                + " VALUES ('" + getNamaBarang() + "', '" + getKategori() + "' "
                + ", '" + getModal() + "', '" + getHargaJual() + "', '" + getStok() + "', '" + getIdPemasok() + "')");

        try {
            //inisialisasi preparedStatement
            PreparedStatement eksekusi = koneksi.getConnect().prepareStatement(sql);
            eksekusi.execute();
            //pemberitahuan jika data berhasil disimpan
            Dialog.info(null, "Data Berhasil Disimpan");

        } catch (SQLException ex) {
            Dialog.error(null, "Data Gagal Disimpan\n" + ex.getMessage());
        }

    }

    public void ubahDataBarang() {
        //inisialisasi query untuk update kedalam database
        String sql = "UPDATE barang SET namaBarang = '" + getNamaBarang() + "'"
                + ", kategori = '" + getKategori() + "'"
                + ", modal = '" + getModal() + "'"
                + ", hargaJual = '" + getHargaJual() + "'"
                + ", stok = '" + getStok() + "'"
                + ", idPemasok = '" + getIdPemasok() + "' WHERE idBarang = '" + getIdBarang() + "'";
        try {
            //inisialisasi preparedStatement
            PreparedStatement eksekusi = koneksi.getConnect().prepareStatement(sql);
            eksekusi.execute();
            //pemberitahuan jika data berhasil disimpan
            Dialog.info(null, "Data Berhasil Diubah");

        } catch (SQLException ex) {
            Dialog.error(null, "Data Gagal Diubah\n" + ex.getMessage());
        }

    }

    public boolean hapusDataBarang() {

        boolean konfirmasi = com.bumbu.Dialog.konfirmasi(null, "Apakah Anda yakin ingin menghapus data barang ini?");
        if (konfirmasi) {
            String sql = "DELETE FROM barang WHERE idBarang = '" + getIdBarang() + "'";
            try {
                //inisialisasi preparedStatement
                PreparedStatement eksekusi = koneksi.getConnect().prepareStatement(sql);
                eksekusi.execute();
                //pemberitahuan jika data berhasil disimpan
                Dialog.info(null, "Data Berhasil Dihapus");
                return true;
            } catch (SQLException ex) {
                Dialog.error(null, "Data Gagal Dihapus\n" + ex.getMessage());
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
