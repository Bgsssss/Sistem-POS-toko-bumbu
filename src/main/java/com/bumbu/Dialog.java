/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bumbu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class Dialog {

    private Dialog() { }

    /** Pesan berhasil. */
    public static void info(Component parent, String pesan) {
        tampil(parent, "Informasi", pesan, "OK", null);
    }

    /** Validasi / peringatan (data kosong, belum memilih data, dll). */
    public static void peringatan(Component parent, String pesan) {
        tampil(parent, "Peringatan", pesan, "OK", null);
    }

    /** Pesan gagal / error. */
    public static void error(Component parent, String pesan) {
        tampil(parent, "Gagal", pesan, "OK", null);
    }

    /** Konfirmasi Ya/Tidak. Mengembalikan true jika memilih "Ya". */
    public static boolean konfirmasi(Component parent, String pesan) {
        return tampil(parent, "Konfirmasi", pesan, "Ya", "Tidak");
    }

    private static String html(String teks) {
        if (teks == null) {
            return "";
        }
        return teks.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>");
    }

    private static boolean tampil(Component parent, String judul, String pesan,
                                  String teksYa, String teksTidak) {
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        final JDialog d = new JDialog(owner, judul,
                java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        final boolean[] hasil = { false };

        // ===== banner judul (sama seperti banner form lain) =====
        JPanel pnlJudul = new JPanel(new BorderLayout());
        JLabel lblJudul = new JLabel(judul.toUpperCase());
        Tema.banner(pnlJudul, lblJudul);
        lblJudul.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        pnlJudul.add(lblJudul, BorderLayout.CENTER);

        // ===== isi pesan =====
        JLabel lblPesan = new JLabel(
                "<html><body style='width: 280px'>" + html(pesan) + "</body></html>");
        lblPesan.setFont(Tema.FONT_NORMAL);
        lblPesan.setForeground(Tema.TEKS);
        JPanel pnlIsi = new JPanel(new BorderLayout());
        pnlIsi.setBackground(Tema.PUTIH);
        pnlIsi.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));
        pnlIsi.add(lblPesan, BorderLayout.CENTER);

        // ===== tombol =====
        JPanel pnlTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        pnlTombol.setBackground(Tema.PANEL);
        pnlTombol.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.GARIS));

        JButton btnYa = new JButton(teksYa);
        Tema.tombolUtama(btnYa);
        btnYa.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hasil[0] = true;
                d.dispose();
            }
        });

        JButton btnTidak = null;
        if (teksTidak != null) {
            btnTidak = new JButton(teksTidak);
            Tema.tombolBiasa(btnTidak);
            btnTidak.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hasil[0] = false;
                    d.dispose();
                }
            });
            pnlTombol.add(btnTidak);
        }
        pnlTombol.add(btnYa);

        // ===== susun dialog =====
        d.getContentPane().setBackground(Tema.PUTIH);
        d.getContentPane().setLayout(new BorderLayout());
        d.getContentPane().add(pnlJudul, BorderLayout.NORTH);
        d.getContentPane().add(pnlIsi, BorderLayout.CENTER);
        d.getContentPane().add(pnlTombol, BorderLayout.SOUTH);

        // Enter = tombol default (untuk konfirmasi default-nya "Tidak" supaya aman)
        d.getRootPane().setDefaultButton(btnTidak != null ? btnTidak : btnYa);

        // Esc = tutup (dianggap "Tidak")
        d.getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hasil[0] = false;
                d.dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        d.setResizable(false);
        d.pack();
        d.setMinimumSize(new java.awt.Dimension(360, 0));
        d.pack();
        d.setLocationRelativeTo(owner);
        d.setVisible(true);

        return hasil[0];
    }
    
    /** Pilihan dropdown (ComboBox). Mengembalikan item terpilih, atau null jika Batal/Esc. */
    public static String pilih(Component parent, String judul, String pesan, String[] pilihan) {
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        final JDialog d = new JDialog(owner, judul,
                java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        final String[] hasil = { null };

        // ===== banner judul =====
        JPanel pnlJudul = new JPanel(new BorderLayout());
        JLabel lblJudul = new JLabel(judul.toUpperCase());
        Tema.banner(pnlJudul, lblJudul);
        lblJudul.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        pnlJudul.add(lblJudul, BorderLayout.CENTER);

        // ===== isi pesan + dropdown =====
        JLabel lblPesan = new JLabel(
                "<html><body style='width: 280px'>" + html(pesan) + "</body></html>");
        lblPesan.setFont(Tema.FONT_NORMAL);
        lblPesan.setForeground(Tema.TEKS);
        lblPesan.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        final JComboBox<String> cbPilihan = new JComboBox<>(pilihan);
        cbPilihan.setFont(Tema.FONT_NORMAL);
        cbPilihan.setForeground(Tema.TEKS);
        cbPilihan.setBackground(Tema.PUTIH);

        JPanel pnlIsi = new JPanel(new BorderLayout());
        pnlIsi.setBackground(Tema.PUTIH);
        pnlIsi.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        pnlIsi.add(lblPesan, BorderLayout.NORTH);
        pnlIsi.add(cbPilihan, BorderLayout.CENTER);

        // ===== tombol =====
        JPanel pnlTombol = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        pnlTombol.setBackground(Tema.PANEL);
        pnlTombol.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.GARIS));

        JButton btnBatal = new JButton("Batal");
        Tema.tombolBiasa(btnBatal);
        btnBatal.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hasil[0] = null;
                d.dispose();
            }
        });

        JButton btnOk = new JButton("OK");
        Tema.tombolUtama(btnOk);
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object terpilih = cbPilihan.getSelectedItem();
                hasil[0] = terpilih != null ? terpilih.toString() : null;
                d.dispose();
            }
        });

        pnlTombol.add(btnBatal);
        pnlTombol.add(btnOk);

        // ===== susun dialog =====
        d.getContentPane().setBackground(Tema.PUTIH);
        d.getContentPane().setLayout(new BorderLayout());
        d.getContentPane().add(pnlJudul, BorderLayout.NORTH);
        d.getContentPane().add(pnlIsi, BorderLayout.CENTER);
        d.getContentPane().add(pnlTombol, BorderLayout.SOUTH);

        d.getRootPane().setDefaultButton(btnOk);
        d.getRootPane().registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hasil[0] = null;
                d.dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        d.setResizable(false);
        d.pack();
        d.setMinimumSize(new java.awt.Dimension(360, 0));
        d.pack();
        d.setLocationRelativeTo(owner);
        d.setVisible(true);

        return hasil[0];
    }
}
