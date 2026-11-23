package com.bumbu;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import model.Kasir;

/**
 * Membuat, menampilkan, dan mencetak struk belanja (kertas thermal 80mm).
 */
public final class Struk {

    // ====== Ubah data toko di sini ======
    private static final String NAMA_TOKO = "UD. ILHAM JAYA";
    private static final String ALAMAT_1 = "Pasar Induk Kramat Jati Los C";
    private static final String ALAMAT_2 = "Kelurahan Tengah No. 70, RT 003/RW 007, Kecamatan Kramat Jati, Jakarta Timur";
    private static final String KONTAK = "WA: 085758368039";
    private static final String[] PENUTUP = {
        "Terima kasih sudah berbelanja di toko",
        "kami. Sehat & sukses selalu!"
    };
    private static final int LEBAR = 40;   // jumlah karakter per baris
    private static final String[] BULAN = {"Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
        "Jul", "Agu", "Sep", "Okt", "Nov", "Des"};

    /**
     * Data yang dibutuhkan untuk satu struk.
     */
    public static class Data {

        public String noTransaksi;      // contoh: TX-0927
        public LocalDateTime waktu;
        public String kasir;
        public String pembeli;
        public List<Kasir.Item> items = new ArrayList<>();
        public long total;
        public long bayar;
        public boolean salinan;
    }

    private Struk() {
    }

    // ===================== ISI STRUK =====================
    public static String buat(Data d) {
        StringBuilder sb = new StringBuilder();
        String garis = ulang('-', LEBAR);

        sb.append(tengah(NAMA_TOKO)).append('\n');
        sb.append(tengah(ALAMAT_1)).append('\n');
        if (!ALAMAT_2.isEmpty()) {
            sb.append(tengah(ALAMAT_2)).append('\n');
        }

        sb.append(tengah(KONTAK)).append('\n');
        if (d.salinan) {
            sb.append(tengah("*** SALINAN ***")).append('\n');   // BARU
        }
        sb.append(garis).append('\n');

        sb.append("No. Struk : #").append(d.noTransaksi).append('\n');
        sb.append("Tanggal   : ").append(tanggal(d.waktu)).append('\n');
        sb.append("Kasir     : ").append(d.kasir).append('\n');
        String pembeli = (d.pembeli == null || d.pembeli.trim().isEmpty()) ? "-" : d.pembeli.trim();
        sb.append("Pembeli   : ").append(pembeli).append('\n');
        sb.append(garis).append('\n');

        for (Kasir.Item it : d.items) {
            sb.append(potong(it.namaBarang.toUpperCase(), LEBAR)).append('\n');
            long totalGram = (long) it.beratGram * it.qty;
            long perKg = Math.round(it.hargaSatuan * 1000.0 / it.beratGram);
            sb.append(kiriKanan("  " + totalGram + " gr x " + rp(perKg) + "/kg",
                    rp(it.subtotal()))).append('\n');
        }
        sb.append(garis).append('\n');

        sb.append(kiriKanan("TOTAL", rp(d.total))).append('\n');
        if (d.bayar > 0) {
            sb.append(kiriKanan("Tunai", rp(d.bayar))).append('\n');
            sb.append(kiriKanan("Kembali", rp(d.bayar - d.total))).append('\n');
            sb.append("Status: ").append(d.bayar >= d.total ? "LUNAS" : "BELUM LUNAS").append('\n');
        }
        sb.append(garis).append('\n');

        for (String s : PENUTUP) {
            sb.append(tengah(s)).append('\n');
        }
        return sb.toString();
    }

    // ===================== TAMPIL + CETAK =====================
    /**
     * Tampilkan pratinjau; user bisa pilih Cetak atau Tutup.
     */
    public static void tampilkan(java.awt.Component parent, Data d) {
        final String teks = buat(d);
        JTextArea area = new JTextArea(teks);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setRows(Math.min(30, teks.split("\n").length + 1));
        area.setColumns(LEBAR + 2);

        Object[] tombol = {"Cetak", "Tutup"};
        int pilih = JOptionPane.showOptionDialog(parent, new JScrollPane(area),
                "Struk #" + d.noTransaksi, JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, tombol, tombol[0]);
        if (pilih == 0) {
            try {
                cetak(teks);
            } catch (PrinterException ex) {
                Dialog.error(parent, "Struk gagal dicetak\n" + ex.getMessage());
            }
        }
    }

    /**
     * Kirim teks struk ke printer (muncul dialog pilih printer).
     */
    public static void cetak(final String teks) throws PrinterException {
        final String[] baris = teks.split("\n");
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("Struk");

        // Kertas 80mm (226 pt), tinggi mengikuti jumlah baris
        double lebar = 226;
        double tinggi = baris.length * 11 + 40;
        Paper p = new Paper();
        p.setSize(lebar, tinggi);
        p.setImageableArea(8, 8, lebar - 16, tinggi - 16);
        PageFormat pf = job.defaultPage();
        pf.setPaper(p);

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat f, int halaman) {
                if (halaman > 0) {
                    return NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) g;
                g2.translate(f.getImageableX(), f.getImageableY());

                // kecilkan font sampai 1 baris penuh muat di lebar kertas
                int ukuran = 9;
                Font font;
                FontMetrics fm;
                do {
                    font = new Font(Font.MONOSPACED, Font.PLAIN, ukuran--);
                    fm = g2.getFontMetrics(font);
                } while (fm.stringWidth(ulang('M', LEBAR)) > f.getImageableWidth() && ukuran > 4);

                g2.setFont(font);
                int y = fm.getAscent();
                for (String b : baris) {
                    g2.drawString(b, 0, y);
                    y += fm.getHeight();
                }
                return PAGE_EXISTS;
            }
        }, pf);

        if (job.printDialog()) {
            job.print();
        }
    }

    // ===================== PEMBANTU FORMAT =====================
    private static String rp(long n) {
        DecimalFormatSymbols s = new DecimalFormatSymbols(Locale.forLanguageTag("id-ID"));
        s.setGroupingSeparator('.');
        return "Rp" + new DecimalFormat("#,##0", s).format(n);
    }

    private static String tanggal(LocalDateTime w) {
        return String.format("%d %s %d, %02d:%02d", w.getDayOfMonth(),
                BULAN[w.getMonthValue() - 1], w.getYear(), w.getHour(), w.getMinute());
    }

    private static String ulang(char c, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    private static String potong(String s, int maks) {
        return s.length() <= maks ? s : s.substring(0, maks);
    }

    private static String tengah(String s) {
        s = potong(s, LEBAR);
        int kiri = (LEBAR - s.length()) / 2;
        return ulang(' ', kiri) + s;
    }

    /**
     * Teks kiri dan kanan dalam satu baris, kanan rata kanan.
     */
    private static String kiriKanan(String kiri, String kanan) {
        int sisa = LEBAR - kiri.length() - kanan.length();
        if (sisa < 1) {
            kiri = potong(kiri, Math.max(0, LEBAR - kanan.length() - 1));
            sisa = LEBAR - kiri.length() - kanan.length();
        }
        return kiri + ulang(' ', sisa) + kanan;
    }
}
