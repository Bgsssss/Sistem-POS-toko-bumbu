package com.bumbu;

import java.awt.Toolkit;
import java.math.BigDecimal;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public final class InputAngka {

    private InputAngka() {
    }

    public static void hanyaAngka(final JTextField field, final String nama, final int maxDigit) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                replace(fb, offset, 0, string, attr);
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text == null || text.isEmpty()) {
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }
                if (!text.matches("\\d+")) {
                    tolak(field, "Kolom " + nama + " hanya boleh berisi angka 0-9 "
                            + "(tanpa titik, koma, spasi, atau tanda minus).");
                    return;
                }
                int panjangBaru = fb.getDocument().getLength() - length + text.length();
                if (panjangBaru > maxDigit) {
                    tolak(field, "Kolom " + nama + " maksimal " + maxDigit + " digit.");
                    return;
                }
                super.replace(fb, offset, length, text, attrs);
            }
        });
    }

    private static void tolak(final JTextField field, final String pesan) {
        Toolkit.getDefaultToolkit().beep();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Dialog.peringatan(field, pesan);
            }
        });
    }

    public static long ambil(String teks) {
        if (teks == null) {
            return -1;
        }
        String t = teks.trim();
        if (!t.matches("\\d{1,18}")) {
            return -1;
        }
        return Long.parseLong(t);
    }

    public static String keBulat(String teks) {
        try {
            return new BigDecimal(teks.trim()).toBigInteger().toString();
        } catch (Exception e) {
            return teks;
        }
    }
}
