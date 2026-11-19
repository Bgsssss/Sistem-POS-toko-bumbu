package model;

import com.bumbu.Dialog;
import database.Koneksi;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Kasir {

    public static class Item {

        public int idBarang;
        public String namaBarang;
        public int beratGram;     // berat per qty (250 / 500 / 1000)
        public long hargaSatuan;  // harga untuk beratGram tersebut
        public int qty;

        public long subtotal() {
            return hargaSatuan * qty;
        }
    }

    public static class Info {

        public int idBarang;
        public String namaBarang;
        public int beratGram;
        public long hargaJual;
        public int stok;
    }

    private String noTransaksi;
    private LocalDateTime waktu;

    public String getNoTransaksi() {
        return noTransaksi;
    }

    public LocalDateTime getWaktu() {
        return waktu;
    }

    public static Info ambilInfo(int idBarang) {
        String sql = "SELECT idBarang, namaBarang, beratGram, hargaJual, stok FROM barang WHERE idBarang = ?";
        try (PreparedStatement ps = Koneksi.getConnect().prepareStatement(sql)) {
            ps.setInt(1, idBarang);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Info i = new Info();
                    i.idBarang = rs.getInt("idBarang");
                    i.namaBarang = rs.getString("namaBarang");
                    i.beratGram = rs.getInt("beratGram");
                    i.hargaJual = rs.getBigDecimal("hargaJual").longValue();
                    i.stok = rs.getInt("stok");
                    return i;
                }
            }
        } catch (SQLException ex) {
            Dialog.error(null, "Gagal membaca data barang\n" + ex.getMessage());
        }
        return null;
    }

    /**
     * Simpan transaksi + detail + kurangi stok dalam SATU transaksi database.
     * Jika salah satu langkah gagal (mis. stok kurang), semuanya dibatalkan.
     */
    public boolean simpanTransaksi(String namaPembeli, List<Item> items, long bayar, String kasir) {
        Connection c = null;
        try {
            c = Koneksi.getConnect();
            c.setAutoCommit(false);

            long total = 0;
            int jumlah = 0;
            for (Item it : items) {
                total += it.subtotal();
                jumlah += it.qty;
            }

            // 1) header transaksi
            long idTrx = 0;
            String sqlT = "INSERT INTO transaksi (namaPembeli, jmlBarang, total, tglTransaksi, bayar, kasir) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = c.prepareStatement(sqlT, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, namaPembeli);
                ps.setString(2, String.valueOf(jumlah));
                ps.setBigDecimal(3, BigDecimal.valueOf(total));
                LocalDateTime sekarang = LocalDateTime.now();
                ps.setTimestamp(4, Timestamp.valueOf(sekarang));
                ps.setBigDecimal(5, BigDecimal.valueOf(bayar));
                ps.setString(6, kasir);
                this.waktu = sekarang;
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    idTrx = rs.getLong(1);
                }
            }

            // 2) nomor transaksi: TX-0933, dst.
            try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE transaksi SET noTransaksi = ? WHERE idTransaksi = ?")) {
                this.noTransaksi = String.format("TX-%04d", idTrx);
                ps.setString(1, this.noTransaksi);
                ps.setLong(2, idTrx);
                ps.executeUpdate();
            }

            // 3) detail + kurangi stok
            String sqlD = "INSERT INTO detail_transaksi "
                    + "(idTransaksi, idBarang, namaBarang, qty, beratGram, hargaSatuan, subtotal) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            String sqlP = "SELECT beratGram FROM barang WHERE idBarang = ? FOR UPDATE";
            String sqlS = "UPDATE barang SET stok = stok - ? WHERE idBarang = ? AND stok >= ?";
            try (PreparedStatement psD = c.prepareStatement(sqlD); PreparedStatement psP = c.prepareStatement(sqlP); PreparedStatement psS = c.prepareStatement(sqlS)) {

                for (Item it : items) {
                    int beratPaket;
                    psP.setInt(1, it.idBarang);
                    try (ResultSet rs = psP.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException("Barang \"" + it.namaBarang + "\" tidak ditemukan");
                        }
                        beratPaket = rs.getInt(1);
                    }
                    if (beratPaket <= 0 || it.beratGram % beratPaket != 0) {
                        throw new SQLException("Berat \"" + it.namaBarang + "\" tidak valid");
                    }

                    psD.setLong(1, idTrx);
                    psD.setInt(2, it.idBarang);
                    psD.setString(3, it.namaBarang);
                    psD.setInt(4, it.qty);
                    psD.setInt(5, it.beratGram);
                    psD.setBigDecimal(6, BigDecimal.valueOf(it.hargaSatuan));
                    psD.setBigDecimal(7, BigDecimal.valueOf(it.subtotal()));
                    psD.executeUpdate();

                    int kurang = it.qty * (it.beratGram / beratPaket);
                    psS.setInt(1, kurang);
                    psS.setInt(2, it.idBarang);
                    psS.setInt(3, kurang);
                    if (psS.executeUpdate() == 0) {
                        throw new SQLException("Stok \"" + it.namaBarang + "\" tidak cukup");
                    }
                }
            }

            c.commit();
            return true;
        } catch (SQLException ex) {
            try {
                if (c != null) {
                    c.rollback();
                }
            } catch (SQLException ignore) {
            }
            Dialog.error(null, "Transaksi gagal disimpan\n" + ex.getMessage());
            return false;
        } finally {
            try {
                if (c != null) {
                    c.setAutoCommit(true);
                }
            } catch (SQLException ignore) {
            }
        }
    }
}
