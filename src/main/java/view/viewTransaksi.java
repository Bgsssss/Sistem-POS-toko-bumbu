/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.PreparedStatement;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.Kasir;
import com.bumbu.Dialog;
import com.bumbu.Struk;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import com.bumbu.Tema;
import database.Koneksi;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class viewTransaksi extends javax.swing.JInternalFrame {

    /**
     * Creates new form viewTransaksi
     */
    private DefaultTableModel model;
    private DefaultTableModel modelPilih, modelDetail;
    private JTable tabelPilih, tabelDetail;
    private JTextField tfCari;
    private JButton tombolStruk;
    private JLabel lblInfo;
    private final List<Long> idDaftar = new ArrayList<>();   // idTransaksi per baris tabelPilih
    private Struk.Data dataStruk;

    public viewTransaksi() {
        initComponents();
        ((javax.swing.plaf.basic.BasicInternalFrameUI) this.getUI()).setNorthPane(null);
        model = new DefaultTableModel();
        tabelTransaksi.setModel(model);
        model.addColumn("ID TRANSAKSI");
        model.addColumn("NAMA PEMBELI");
        model.addColumn("TANGGAL TRANSAKSI");
        model.addColumn("JML BARANG");
        model.addColumn("TOTAL");

        Tema.banner(jPanel1, jLabel1);        // banner tosca jadi merah, teks putih
        Tema.tabel(tabelTransaksi);           // baris tinggi, selang-seling, terpilih merah muda
        Tema.tombolBiasa(tombolCetak, "printer");

        tampilDataTransaksi();
        susunTab();
        muatDaftar("");
    }

    private void tampilDataTransaksi() {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();

        String sql = "SELECT * FROM transaksi";
        try {
            Statement stat = (Statement) Koneksi.getConnect().createStatement();
            ResultSet res = stat.executeQuery(sql);

            while (res.next()) {
                //mengambil hasil query variable sql
                Object[] hasil;
                hasil = new Object[6]; // karena ada 6 field
                hasil[0] = res.getString("idTransaksi");
                hasil[1] = res.getString("namaPembeli");
                hasil[2] = res.getString("tglTransaksi");
                hasil[3] = res.getString("jmlBarang");
                hasil[4] = res.getString("total");

                model.addRow(hasil);
            }
        } catch (SQLException ex) {
            Logger.getLogger(viewTransaksi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static String rp(long n) {
        return "Rp" + java.text.NumberFormat.getIntegerInstance(
                new java.util.Locale("id", "ID")).format(n);
    }

    private void susunTab() {
        // ---------- Tab 1: Ringkasan (komponen lama dipindah) ----------
        JPanel tabRingkasan = new JPanel(new BorderLayout(0, 10));
        tabRingkasan.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabRingkasan.add(jScrollPane1, BorderLayout.CENTER);
        JPanel barisCetak = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        barisCetak.add(tombolCetak);
        tabRingkasan.add(barisCetak, BorderLayout.SOUTH);

        // ---------- Tab 2: Detail & Cetak Struk ----------
        modelPilih = new DefaultTableModel(
                new Object[]{"NO. TX", "TANGGAL", "PEMBELI", "TOTAL"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        modelDetail = new DefaultTableModel(
                new Object[]{"BARANG", "BERAT", "HARGA", "QTY", "SUBTOTAL"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        tabelPilih = new JTable(modelPilih);
        tabelPilih.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelDetail = new JTable(modelDetail);
        Tema.tabel(tabelPilih);
        Tema.tabel(tabelDetail);

        tabelPilih.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                muatDetail();
            }
        });

        tfCari = new JTextField();
        tfCari.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                muatDaftar(tfCari.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                muatDaftar(tfCari.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        JPanel atas = new JPanel(new BorderLayout(8, 0));
        atas.add(new JLabel("Cari (No. TX / pembeli):"), BorderLayout.WEST);
        atas.add(tfCari, BorderLayout.CENTER);

        JPanel panelDaftar = new JPanel(new BorderLayout(0, 6));
        panelDaftar.add(atas, BorderLayout.NORTH);
        panelDaftar.add(new JScrollPane(tabelPilih), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                panelDaftar, new JScrollPane(tabelDetail));
        split.setResizeWeight(0.55);
        split.setBorder(null);

        lblInfo = new JLabel();
        tombolStruk = new JButton("Cetak Struk");
        Tema.tombolUtama(tombolStruk, "receipt");
        tombolStruk.addActionListener(e -> {
            if (dataStruk != null) {
                Struk.tampilkan(this, dataStruk);
            }
        });
        JPanel bawah = new JPanel(new BorderLayout());
        bawah.add(lblInfo, BorderLayout.WEST);
        bawah.add(tombolStruk, BorderLayout.EAST);

        JPanel tabStruk = new JPanel(new BorderLayout(0, 10));
        tabStruk.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabStruk.add(split, BorderLayout.CENTER);
        tabStruk.add(bawah, BorderLayout.SOUTH);

        // ---------- Pasang ke frame ----------
        JTabbedPane tab = new JTabbedPane();
        tab.setFont(Tema.FONT_MENU);
        tab.addTab("Ringkasan", tabRingkasan);
        tab.addTab("Detail & Cetak Struk", tabStruk);

        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(jPanel1, BorderLayout.NORTH);   // banner "TRANSAKSI"
        getContentPane().add(tab, BorderLayout.CENTER);
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    /**
     * Isi tabel atas (daftar transaksi), terbaru di atas, dengan filter cari.
     */
    private void muatDaftar(String kata) {
        modelPilih.setRowCount(0);
        modelDetail.setRowCount(0);
        idDaftar.clear();
        dataStruk = null;
        tombolStruk.setEnabled(false);
        lblInfo.setText("Pilih satu transaksi di atas");

        String sql = "SELECT idTransaksi, noTransaksi, tglTransaksi, namaPembeli, total "
                + "FROM transaksi WHERE noTransaksi LIKE ? OR namaPembeli LIKE ? "
                + "ORDER BY idTransaksi DESC";
        try (PreparedStatement ps = Koneksi.getConnect().prepareStatement(sql)) {
            ps.setString(1, "%" + kata + "%");
            ps.setString(2, "%" + kata + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("idTransaksi");
                    String no = rs.getString("noTransaksi");
                    if (no == null) {                       // data lama sebelum ada noTransaksi
                        no = String.format("TX-%04d", id);
                    }
                    idDaftar.add(id);
                    modelPilih.addRow(new Object[]{
                        no,
                        rs.getString("tglTransaksi"),
                        rs.getString("namaPembeli"),
                        rp(rs.getBigDecimal("total").longValue())
                    });
                }
            }
        } catch (SQLException ex) {
            Dialog.error(this, "Gagal membaca transaksi\n" + ex.getMessage());
        }
    }

    /**
     * Isi tabel bawah (barang) + siapkan data struk untuk transaksi terpilih.
     */
    private void muatDetail() {
        modelDetail.setRowCount(0);
        dataStruk = null;
        tombolStruk.setEnabled(false);

        int baris = tabelPilih.getSelectedRow();
        if (baris < 0 || baris >= idDaftar.size()) {
            return;
        }
        long id = idDaftar.get(baris);

        try {
            Connection c = Koneksi.getConnect();
            Struk.Data d = new Struk.Data();
            d.salinan = true;     // cetak ulang -> "*** SALINAN ***"

            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT noTransaksi, tglTransaksi, namaPembeli, total, bayar, kasir "
                    + "FROM transaksi WHERE idTransaksi = ?")) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return;
                    }
                    String no = rs.getString("noTransaksi");
                    d.noTransaksi = (no != null) ? no : String.format("TX-%04d", id);
                    d.waktu = rs.getTimestamp("tglTransaksi").toLocalDateTime();
                    d.pembeli = rs.getString("namaPembeli");
                    d.total = rs.getBigDecimal("total").longValue();
                    d.kasir = rs.getString("kasir");
                    BigDecimal b = rs.getBigDecimal("bayar");
                    d.bayar = (b == null) ? 0 : b.longValue();
                }
            }

            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT idBarang, namaBarang, beratGram, hargaSatuan, qty "
                    + "FROM detail_transaksi WHERE idTransaksi = ?")) {
                ps.setLong(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Kasir.Item it = new Kasir.Item();
                        it.idBarang = rs.getInt("idBarang");
                        it.namaBarang = rs.getString("namaBarang");
                        it.beratGram = rs.getInt("beratGram");
                        it.hargaSatuan = rs.getBigDecimal("hargaSatuan").longValue();
                        it.qty = rs.getInt("qty");
                        d.items.add(it);

                        modelDetail.addRow(new Object[]{
                            it.namaBarang,
                            it.beratGram + " gr",
                            rp(it.hargaSatuan),
                            it.qty,
                            rp(it.subtotal())
                        });
                    }
                }
            }

            dataStruk = d;
            tombolStruk.setEnabled(true);
        } catch (SQLException ex) {
            Dialog.error(this, "Gagal membaca detail transaksi\n" + ex.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelTransaksi = new javax.swing.JTable();
        tombolCetak = new javax.swing.JButton();
        tabRingkasan = new javax.swing.JPanel();
        barisCetak = new javax.swing.JPanel();

        setClosable(true);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TRANSAKSI");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabelTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelTransaksi.setRowHeight(25);
        jScrollPane1.setViewportView(tabelTransaksi);

        tombolCetak.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tombolCetak.setText("Cetak");
        tombolCetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolCetakActionPerformed(evt);
            }
        });

        tabRingkasan.setBackground(new java.awt.Color(255, 255, 255));

        barisCetak.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout barisCetakLayout = new javax.swing.GroupLayout(barisCetak);
        barisCetak.setLayout(barisCetakLayout);
        barisCetakLayout.setHorizontalGroup(
            barisCetakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        barisCetakLayout.setVerticalGroup(
            barisCetakLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 26, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout tabRingkasanLayout = new javax.swing.GroupLayout(tabRingkasan);
        tabRingkasan.setLayout(tabRingkasanLayout);
        tabRingkasanLayout.setHorizontalGroup(
            tabRingkasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabRingkasanLayout.createSequentialGroup()
                .addContainerGap(99, Short.MAX_VALUE)
                .addComponent(barisCetak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        tabRingkasanLayout.setVerticalGroup(
            tabRingkasanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tabRingkasanLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(barisCetak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tabRingkasan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(584, 584, 584))
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(tombolCetak)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabRingkasan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                .addComponent(tombolCetak, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tombolCetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolCetakActionPerformed
        // TODO add your handling code here:
        try {
            Connection conn = new Koneksi().getConnect();
            HashMap<String, Object> parameter = new HashMap<>();
            InputStream is = getClass().getResourceAsStream("/laporan/laporanTransaksiv3.jrxml");
            JasperReport jr = JasperCompileManager.compileReport(is);
            JasperPrint jp = JasperFillManager.fillReport(jr, parameter, conn);
            JasperViewer.viewReport(jp, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_tombolCetakActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel barisCetak;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel tabRingkasan;
    private javax.swing.JTable tabelTransaksi;
    private javax.swing.JButton tombolCetak;
    // End of variables declaration//GEN-END:variables

}
