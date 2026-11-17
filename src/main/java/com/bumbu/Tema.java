/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bumbu;

/**
 *
 * @author Bagas Saputra
 */
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;

public class Tema {

    // ===================== WARNA =====================
    public static final Color MERAH = new Color(235, 1, 8);
    public static final Color MERAH_GELAP = new Color(179, 0, 6);
    public static final Color MERAH_MUDA = new Color(255, 229, 230);
    public static final Color PUTIH = Color.WHITE;
    public static final Color PANEL = new Color(244, 244, 244);
    public static final Color TEKS = new Color(43, 43, 43);
    public static final Color TEKS_SAMAR = new Color(150, 150, 150);
    public static final Color GARIS = new Color(221, 221, 221);
    public static final Color NONAKTIF = new Color(235, 235, 235);
    public static final Color HIJAU = new Color(46, 125, 50);
    public static final Color ORANYE = new Color(249, 168, 37);

    // ===================== FONT =====================
    public static final String KELUARGA = "Segoe UI";

    public static final Font FONT_JUDUL = new Font(KELUARGA, Font.BOLD, 28);
    public static final Font FONT_HEADER = new Font(KELUARGA, Font.BOLD, 18);
    public static final Font FONT_MENU = new Font(KELUARGA, Font.BOLD, 14);
    public static final Font FONT_NORMAL = new Font(KELUARGA, Font.PLAIN, 14);
    public static final Font FONT_TABEL = new Font(KELUARGA, Font.PLAIN, 13);
    public static final Font FONT_TOMBOL = new Font(KELUARGA, Font.BOLD, 13);
    public static final Font FONT_UTAMA = new Font(KELUARGA, Font.BOLD, 16);
    public static final Font FONT_UANG = new Font(KELUARGA, Font.BOLD, 20);
    public static final Font FONT_TOTAL = new Font(KELUARGA, Font.BOLD, 32);

    // ===================== UKURAN =====================
    public static final int TINGGI_BARIS_TABEL = 28;
    public static final int TINGGI_MENU = 50;
    public static final int LEBAR_MENU = 220;
    public static final int UKURAN_IKON = 18;
    public static final int UKURAN_IKON_MENU = 20;

    private Tema() {
    }

    public static void terapkan() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Tema.class.getName())
                    .log(java.util.logging.Level.WARNING, "Look and feel gagal diatur", ex);
        }

        FontUIResource normal = new FontUIResource(FONT_NORMAL);
        for (Object key : java.util.Collections.list(UIManager.getDefaults().keys())) {
            if (UIManager.get(key) instanceof FontUIResource) {
                UIManager.put(key, normal);
            }
        }
        UIManager.put("Table.font", new FontUIResource(FONT_TABEL));
        UIManager.put("TableHeader.font", new FontUIResource(
                new Font(KELUARGA, Font.BOLD, 13)));
        UIManager.put("Table.rowHeight", TINGGI_BARIS_TABEL);
    }

    // ===================== IKON SVG =====================
    /**
     * Ikon SVG dari resources/icons/<nama>.svg dengan warna tunggal sesuai
     * tema.
     */
    public static Icon ikon(String nama, int ukuran, final Color warna) {
        FlatSVGIcon i = new FlatSVGIcon("icons/" + nama + ".svg", ukuran, ukuran);
        i.setColorFilter(new FlatSVGIcon.ColorFilter(c -> warna));
        return i;
    }

    /**
     * Pasang ikon pada tombol (aktif berwarna, nonaktif abu).
     */
    public static void ikonTombol(JButton b, String nama, Color warna) {
        if (nama == null) {
            return;
        }
        b.setIcon(ikon(nama, UKURAN_IKON, warna));
        b.setDisabledIcon(ikon(nama, UKURAN_IKON, TEKS_SAMAR));
        b.setIconTextGap(8);
    }

    public static void pasangLogo(JLabel label, int ukuran) {
        URL url = Tema.class.getResource("/img/logo.png");
        if (url != null) {
            Image img = new ImageIcon(url).getImage()
                    .getScaledInstance(ukuran, ukuran, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(img));
        }
    }

    public static void banner(JPanel panel, JLabel judul) {
        panel.setBackground(MERAH);
        judul.setFont(FONT_HEADER);
        judul.setForeground(PUTIH);
        judul.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static void bannerUtama(JPanel panel, JLabel judul) {
        panel.setBackground(MERAH);
        judul.setFont(FONT_JUDUL);
        judul.setForeground(PUTIH);
        judul.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public static void tombolUtama(JButton b) {
        gayaTombol(b, MERAH, MERAH_GELAP, PUTIH, MERAH);
    }

    public static void tombolBiasa(JButton b) {
        gayaTombol(b, PUTIH, MERAH_MUDA, TEKS, GARIS);
    }

    public static void tombolHapus(JButton b) {
        gayaTombol(b, PUTIH, MERAH_MUDA, MERAH_GELAP, MERAH);
    }

    public static void tombolUtama(JButton b, String nama) {
        tombolUtama(b);
        ikonTombol(b, nama, PUTIH);
    }

    public static void tombolBiasa(JButton b, String nama) {
        tombolBiasa(b);
        ikonTombol(b, nama, TEKS);
    }

    public static void tombolHapus(JButton b, String nama) {
        tombolHapus(b);
        ikonTombol(b, nama, MERAH_GELAP);
    }

    private static void gayaTombol(final JButton b, final Color bg, final Color bgHover,
            final Color fg, final Color garis) {
        b.setUI(new BasicButtonUI());
        b.setFont(FONT_TOMBOL);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        final boolean[] hover = {false};
        final Runnable segarkan = new Runnable() {
            public void run() {
                if (!b.isEnabled()) {
                    b.setBackground(NONAKTIF);
                    b.setForeground(TEKS_SAMAR);
                    b.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(GARIS),
                            BorderFactory.createEmptyBorder(6, 14, 6, 14)));
                    b.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                } else {
                    b.setBackground(hover[0] ? bgHover : bg);
                    b.setForeground(fg);
                    b.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(garis),
                            BorderFactory.createEmptyBorder(6, 14, 6, 14)));
                    b.setCursor(new Cursor(Cursor.HAND_CURSOR));
                }
            }
        };

        b.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover[0] = true;
                segarkan.run();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover[0] = false;
                segarkan.run();
            }
        });
        b.addPropertyChangeListener("enabled", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                segarkan.run();
            }
        });
        segarkan.run();
    }

    public static void tombolMenu(JButton b) {
        b.setUI(new BasicButtonUI());
        b.setFont(FONT_MENU);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GARIS),
                BorderFactory.createEmptyBorder(0, 16, 0, 8)));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBackground(PUTIH);
        b.setForeground(TEKS);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setPreferredSize(new Dimension(LEBAR_MENU - 20, TINGGI_MENU));
        b.setMinimumSize(new Dimension(LEBAR_MENU - 20, TINGGI_MENU));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, TINGGI_MENU));
    }

    /**
     * Menu samping dengan ikon SVG. Ikon otomatis putih saat menu aktif.
     */
    public static void tombolMenu(JButton b, String nama) {
        tombolMenu(b);
        b.putClientProperty("ikonMenu", nama);
        b.setIcon(ikon(nama, UKURAN_IKON_MENU, TEKS));
        b.setIconTextGap(12);
    }

    /**
     * Tandai satu menu sebagai aktif (merah tua), sisanya putih.
     */
    public static void aktifkanMenu(JButton aktif, JButton... semua) {
        for (JButton b : semua) {
            b.setBackground(PUTIH);
            b.setForeground(TEKS);
            aturIkonMenu(b, TEKS);
        }
        aktif.setBackground(MERAH_GELAP);
        aktif.setForeground(PUTIH);
        aturIkonMenu(aktif, PUTIH);
    }

    private static void aturIkonMenu(JButton b, Color warna) {
        Object nama = b.getClientProperty("ikonMenu");
        if (nama != null) {
            b.setIcon(ikon(nama.toString(), UKURAN_IKON_MENU, warna));
        }
    }

    public static void hoverMenu(JButton b, boolean masuk) {
        b.setBackground(masuk ? MERAH_MUDA : PUTIH);
    }

    public static void tabel(JTable t) {
        t.setFont(FONT_TABEL);
        t.setRowHeight(TINGGI_BARIS_TABEL);
        t.setGridColor(GARIS);
        t.setShowVerticalLines(false);
        t.setSelectionBackground(MERAH_MUDA);
        t.setSelectionForeground(TEKS);
        t.getTableHeader().setFont(new Font(KELUARGA, Font.BOLD, 13));
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean selected, boolean focus, int row, int col) {
                Component c = super.getTableCellRendererComponent(
                        tbl, value, selected, focus, row, col);
                if (selected) {
                    c.setBackground(MERAH_MUDA);
                    c.setForeground(TEKS);
                } else {
                    c.setBackground(row % 2 == 0 ? PUTIH : PANEL);
                    c.setForeground(TEKS);
                }
                setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
                return c;
            }
        });
    }

    public static void latarJendela(javax.swing.JFrame f) {
        f.getContentPane().setBackground(PUTIH);
    }
}
