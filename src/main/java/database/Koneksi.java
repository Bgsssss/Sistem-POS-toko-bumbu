package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Koneksi {

    private static final String CONFIG_DEFAULT = "D:\\TokoBumbu\\config.properties";
    private static final String[] KUNCI_WAJIB = {
        "db.host", "db.port", "db.name", "db.user", "db.password"
    };

    private static Connection conn;

    static {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static String lokasiConfig() {
        String dariEnv = System.getenv("TOKO_CONFIG");
        if (dariEnv != null && !dariEnv.trim().isEmpty()) {
            return dariEnv.trim();
        }
        return CONFIG_DEFAULT;
    }

    private static Properties muatKonfigurasi() throws SQLException {
        String path = lokasiConfig();
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream(path)) {
            p.load(in);
        } catch (IOException e) {
            throw new SQLException("Berkas konfigurasi database tidak ditemukan atau tidak bisa dibaca: " + path, e);
        }
        for (String kunci : KUNCI_WAJIB) {
            String nilai = p.getProperty(kunci);
            if (nilai == null || nilai.trim().isEmpty()) {
                throw new SQLException("Konfigurasi '" + kunci + "' belum diisi pada " + path);
            }
        }
        return p;
    }

    private static boolean masihHidup() {
        try {
            return conn != null && !conn.isClosed() && conn.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    public static synchronized Connection getConnect() throws SQLException {
        if (masihHidup()) {
            return conn;
        }
        tutup();

        Properties p = muatKonfigurasi();
        String url = "jdbc:mysql://" + p.getProperty("db.host").trim()
                + ":" + p.getProperty("db.port").trim()
                + "/" + p.getProperty("db.name").trim()
                + "?connectTimeout=3000";
        try {
            conn = DriverManager.getConnection(url,
                    p.getProperty("db.user").trim(),
                    p.getProperty("db.password"));
        } catch (SQLException e) {
            conn = null;
            throw new SQLException("Koneksi ke database gagal. Periksa apakah MySQL berjalan "
                    + "dan konfigurasi sudah benar.\n" + e.getMessage(), e);
        }
        return conn;
    }

    public static synchronized void tutup() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignore) {
            }
            conn = null;
        }
    }
}