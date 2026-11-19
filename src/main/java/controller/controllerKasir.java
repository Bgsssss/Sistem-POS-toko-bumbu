package controller;

import com.bumbu.Dialog;
import com.bumbu.Struk;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;
import model.Kasir;
import model.Sesi;
import view.viewKasir;

public class controllerKasir {

    // kolom tabel kasir
    private static final int C_ID = 0, C_NAMA = 1, C_BERAT = 2, C_HARGA = 3, C_QTY = 4, C_SUB = 5;

    private viewKasir vK;

    public controllerKasir(viewKasir vK) {
        this.vK = vK;
    }

    private DefaultTableModel model() {
        return (DefaultTableModel) vK.getTabelKasir().getModel();
    }

    private long angka(int baris, int kolom) {
        return ((Number) model().getValueAt(baris, kolom)).longValue();
    }

    public void tambahBarang() {
        if (vK.getIdBarang() == null || vK.getNamaBarang().getText().isEmpty()) {
            Dialog.peringatan(vK, "Pilih barang dulu lewat tombol Cari!");
            return;
        }
        int qty = Integer.parseInt(vK.getJmlBeli().getValue().toString());
        if (qty <= 0) {
            Dialog.peringatan(vK, "Jumlah barang tidak benar");
            return;
        }

        Kasir.Info info = Kasir.ambilInfo(Integer.parseInt(vK.getIdBarang()));
        if (info == null) {
            Dialog.peringatan(vK, "Barang tidak ditemukan");
            return;
        }

        // pilihan berat: 1x, 2x, 4x berat paket (250 / 500 / 1000 gram)
        int[] kelipatan = {1, 2, 4};
        String[] pilihan = new String[kelipatan.length];
        for (int i = 0; i < kelipatan.length; i++) {
            pilihan[i] = (info.beratGram * kelipatan[i]) + " gram";
        }

        String p = Dialog.pilih(vK, "Pilih Berat",
                "Pilih berat untuk \"" + info.namaBarang + "\":", pilihan);

        if (p == null) {
            return; // dibatalkan
        }
        int faktor = 1;
        for (int i = 0; i < pilihan.length; i++) {
            if (pilihan[i].equals(p)) {
                faktor = kelipatan[i];
            }
        }
        int berat = info.beratGram * faktor;
        long harga = info.hargaJual * faktor;

        // cek stok (termasuk yang sudah ada di keranjang)
        DefaultTableModel m = model();
        int sudahPaket = 0;
        for (int i = 0; i < m.getRowCount(); i++) {
            if (angka(i, C_ID) == info.idBarang) {
                sudahPaket += (int) (angka(i, C_QTY) * (angka(i, C_BERAT) / info.beratGram));
            }
        }
        int butuhPaket = qty * faktor;
        if (sudahPaket + butuhPaket > info.stok) {
            Dialog.peringatan(vK, "Stok tidak cukup!\nTersedia: " + info.stok + " paket ("
                    + (info.stok * info.beratGram) + " gram), sudah di keranjang: " + sudahPaket + " paket");
            return;
        }

        // gabungkan jika barang & berat sama
        boolean digabung = false;
        for (int i = 0; i < m.getRowCount(); i++) {
            if (angka(i, C_ID) == info.idBarang && angka(i, C_BERAT) == berat) {
                long qtyBaru = angka(i, C_QTY) + qty;
                m.setValueAt(qtyBaru, i, C_QTY);
                m.setValueAt(qtyBaru * harga, i, C_SUB);
                digabung = true;
                break;
            }
        }
        if (!digabung) {
            m.addRow(new Object[]{info.idBarang, info.namaBarang, berat, harga, (long) qty, harga * qty});
        }

        hitungTotal();

        // siap untuk barang berikutnya
        vK.setIdBarang(null);
        vK.getNamaBarang().setText("");
        vK.getHargaBarang().setText("");
        vK.getJmlBeli().setValue(1);
    }

    private void hitungTotal() {
        long total = 0;
        long jumlah = 0;
        for (int i = 0; i < model().getRowCount(); i++) {
            total += angka(i, C_SUB);
            jumlah += angka(i, C_QTY);
        }
        vK.getTotal().setText(String.valueOf(total));
        vK.getJumlahBarang().setText(String.valueOf(jumlah));
        vK.getLabelTotalHarga().setText(new DecimalFormat("#,##0",
                new DecimalFormatSymbols(Locale.forLanguageTag("id-ID"))).format(total));
    }

    /**
     * Hitung kembalian. Mengembalikan false jika belum bisa dibayar.
     */
    public boolean bayar() {
        long total = Long.parseLong(vK.getTotal().getText());
        long bayar = Long.parseLong(vK.getBayar().getText());
        if (total <= 0) {
            Dialog.peringatan(vK, "Keranjang masih kosong!");
            return false;
        }
        if (bayar < total) {
            Dialog.peringatan(vK, "Uang bayar kurang dari total belanja!");
            return false;
        }
        vK.getKembali().setText(String.valueOf(bayar - total));
        return true;
    }

    public boolean simpanTransaksi() {
        DefaultTableModel m = model();
        List<Kasir.Item> items = new ArrayList<>();
        for (int i = 0; i < m.getRowCount(); i++) {
            Kasir.Item it = new Kasir.Item();
            it.idBarang = (int) angka(i, C_ID);
            it.namaBarang = m.getValueAt(i, C_NAMA).toString();
            it.beratGram = (int) angka(i, C_BERAT);
            it.hargaSatuan = angka(i, C_HARGA);
            it.qty = (int) angka(i, C_QTY);
            items.add(it);
        }
        if (items.isEmpty()) {
            Dialog.peringatan(vK, "Keranjang masih kosong!");
            return false;
        }

        long bayar = Long.parseLong(vK.getBayar().getText());
        Kasir k = new Kasir();
        boolean ok = k.simpanTransaksi(vK.getNamaPembeli().getText().trim(), items, bayar, Sesi.namaKasir);
        if (ok) {
            Dialog.info(vK, "Transaksi berhasil disimpan");

            // ===== Struk =====
            Struk.Data d = new Struk.Data();
            d.noTransaksi = k.getNoTransaksi();
            d.waktu = k.getWaktu();
            d.kasir = Sesi.namaKasir;
            d.pembeli = vK.getNamaPembeli().getText();
            d.items = items;
            d.total = Long.parseLong(vK.getTotal().getText());
            d.bayar = bayar;
            Struk.tampilkan(vK, d);
        }
        return ok;
    }

    public void resetForm() {
        vK.setIdBarang(null);
        vK.getNamaBarang().setText("");
        vK.getHargaBarang().setText("");
        vK.getJmlBeli().setValue(1);
        vK.getLabelTotalHarga().setText("0");
        vK.getNamaPembeli().setText("");
        vK.getTotal().setText("0");
        vK.getJumlahBarang().setText("");
        vK.getBayar().setText("");
        vK.getKembali().setText("");
        model().setRowCount(0);
    }

    public void controlButtonBayar() {
        vK.getTombolSelesai().setEnabled(false);
        vK.getTombolBayar().setEnabled(true);
        vK.getTombolBatalTransaksi().setEnabled(true);
        vK.getTombolTambahBarang().setEnabled(true);
    }

    public void controlButtonSelesai() {
        vK.getTombolSelesai().setEnabled(true);
        vK.getTombolBayar().setEnabled(false);
        vK.getTombolBatalTransaksi().setEnabled(false);
        vK.getTombolTambahBarang().setEnabled(false);
    }
}
