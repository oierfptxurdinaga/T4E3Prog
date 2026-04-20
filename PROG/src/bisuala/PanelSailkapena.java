package bisuala;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import model.DenboraldiTalde;

/**
 * Sailkapena erakusten duen panela.
 * Taularen bidez taldeak puntu eta gol-diferentziaren arabera ordenatuta erakusten ditu.
 * Inprimatzeko eta PDFa sortzeko aukera du.
 */
public class PanelSailkapena extends JPanel {
    private static final long serialVersionUID = 1L;

    /** Sailkapenaren taula bisual. */
    private JTable taula;

    /** Taularen datu-modeloa. */
    private DefaultTableModel modeloa;

    /** Sailkapenaren urtea (inprimatzean erabiltzen da). */
    private int urtea;

    /**
     * Sailkapena panela sortzen du emandako estatistika zerrendarekin.
     *
     * @param listaStats taldeen estatistiken zerrenda
     * @param urtea      denboraldiaren urtea
     */
    public PanelSailkapena(ArrayList<DenboraldiTalde> listaStats, int urtea) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        this.urtea = urtea;

        JLabel lblIzenburua = new JLabel("SAILKAPENA");
        lblIzenburua.setFont(new Font("Arial", Font.BOLD, 24));
        lblIzenburua.setForeground(new Color(135, 21, 33));
        lblIzenburua.setHorizontalAlignment(SwingConstants.CENTER);
        lblIzenburua.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblIzenburua, BorderLayout.NORTH);

        listaStats.sort((t1, t2) -> {
            int diffPuntuak = Integer.compare(t2.getPts(), t1.getPts());
            if (diffPuntuak != 0) return diffPuntuak;
            return Integer.compare(t2.getDG(), t1.getDG());
        });

        String[] zutabeak = { "Pos", "Taldea", "PJ", "I", "B", "G", "GF", "GC", "AVG", "PTS" };
        Object[][] data = new Object[listaStats.size()][10];

        for (int i = 0; i < listaStats.size(); i++) {
            DenboraldiTalde dt = listaStats.get(i);
            int average = dt.getDG();
            data[i][0] = i + 1;
            data[i][1] = dt;
            data[i][2] = dt.getPJ();
            data[i][3] = dt.getG();
            data[i][4] = dt.getE();
            data[i][5] = dt.getP();
            data[i][6] = dt.getGF();
            data[i][7] = dt.getGC();
            data[i][8] = average > 0 ? "+" + average : average;
            data[i][9] = dt.getPts();
        }

        modeloa = new DefaultTableModel(data, zutabeak) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) { return false; }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) return DenboraldiTalde.class;
                return super.getColumnClass(columnIndex);
            }
        };

        taula = new JTable(modeloa);

        JButton btnPrint = new JButton("Inprimatu / PDF");
        btnPrint.addActionListener(e -> {
            try {
                java.text.MessageFormat header = new java.text.MessageFormat("Sailkapena - Denboraldia " + this.urtea);
                java.text.MessageFormat footer = new java.text.MessageFormat("Orrialdea {0,number,integer}");
                boolean complete = taula.print(JTable.PrintMode.FIT_WIDTH, header, footer);
                if (complete) {
                    JOptionPane.showMessageDialog(null, "Eginda!", "Inprimatzen", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (java.awt.print.PrinterException pe) {
                JOptionPane.showMessageDialog(null, "Arazoa inprimatzean: " + pe.getMessage());
            }
        });
        this.add(btnPrint, BorderLayout.SOUTH);

        konfiguratuDiseinua();

        JScrollPane scroll = new JScrollPane(taula);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Taularen itxura konfiguratu: tamainak, koloreak eta errenderatzaileak.
     */
    private void konfiguratuDiseinua() {
        taula.setRowHeight(40);
        taula.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        taula.setGridColor(new Color(230, 230, 230));
        taula.setShowVerticalLines(false);

        JTableHeader header = taula.getTableHeader();
        header.setBackground(new Color(135, 21, 33));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        taula.getColumnModel().getColumn(0).setPreferredWidth(40);
        taula.getColumnModel().getColumn(1).setPreferredWidth(250);

        for (int i = 0; i < taula.getColumnCount(); i++) {
            boolean zentratu = (i != 1);
            taula.getColumnModel().getColumn(i).setCellRenderer(new EstiloRenderer(zentratu));
        }
    }

    /**
     * Sailkapena datu berri batekin eguneratzen du.
     *
     * @param listaStatsBerria estatistika berrien zerrenda
     * @param urteaBerria      denboraldi berriaren urtea
     */
    public void eguneratuSailkapena(ArrayList<DenboraldiTalde> listaStatsBerria, int urteaBerria) {
        this.urtea = urteaBerria;
        modeloa.setRowCount(0);

        if (listaStatsBerria == null || listaStatsBerria.isEmpty()) return;

        listaStatsBerria.sort((t1, t2) -> {
            int diffPuntuak = Integer.compare(t2.getPts(), t1.getPts());
            if (diffPuntuak != 0) return diffPuntuak;
            return Integer.compare(t2.getDG(), t1.getDG());
        });

        for (int i = 0; i < listaStatsBerria.size(); i++) {
            DenboraldiTalde dt = listaStatsBerria.get(i);
            int average = dt.getDG();
            Object[] errenkada = new Object[10];
            errenkada[0] = i + 1;
            errenkada[1] = dt.getTalde();
            errenkada[2] = dt.getPJ();
            errenkada[3] = dt.getG();
            errenkada[4] = dt.getE();
            errenkada[5] = dt.getP();
            errenkada[6] = dt.getGF();
            errenkada[7] = dt.getGC();
            errenkada[8] = average > 0 ? "+" + average : average;
            errenkada[9] = dt.getPts();
            modeloa.addRow(errenkada);
        }
    }

    /**
     * Taula-gelaxkak nola margotu definitzen duen renderer pertsonalizatua.
     * Taldearen zutabeak ezkutua eta izena erakusten ditu; gainerakoak zentratu.
     */
    private class EstiloRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        /** true bada testua zentratu; false bada ezkerrera lerrokatu. */
        private boolean zentratu;

        /**
         * Renderer bat sortzen du lerrokatze batekin.
         *
         * @param zentratu true bada zentratu; false bada ezkerrera
         */
        public EstiloRenderer(boolean zentratu) {
            this.zentratu = zentratu;
        }

        /**
         * Gelaxka bakoitza margotzen du: talde-zutabeak ezkutua jartzen du;
         * gainerakoek kolore txandakatuak dituzte.
         *
         * @param table      taula
         * @param value      gelaxkaren balioa
         * @param isSelected hautatuta dagoen ala ez
         * @param hasFocus   fokua duen ala ez
         * @param row        ilara-indizea
         * @param column     zutabe-indizea
         * @return margotutako osagaia
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setIcon(null);

            if (column == 1 && value instanceof DenboraldiTalde) {
                DenboraldiTalde stats = (DenboraldiTalde) value;
                setText(stats.getTalde().getIzena());

                String rutaImagen = "/images/TaldeArmarria/"+stats.getTalde().getEzkutua();
                if (rutaImagen != null) {
                    URL imgUrl = getClass().getResource(rutaImagen);
                    if (imgUrl != null) {
                        ImageIcon icon = new ImageIcon(imgUrl);
                        Image img = icon.getImage();
                        Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(newImg));
                    }
                }

                setHorizontalAlignment(SwingConstants.LEFT);
                setBorder(new EmptyBorder(0, 10, 0, 0));
            } else {
                if (zentratu) setHorizontalAlignment(SwingConstants.CENTER);
                else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                    setBorder(new EmptyBorder(0, 10, 0, 0));
                }
            }

            if (!isSelected) {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250));
            }
            if (column == 0 || column == 9) {
                setFont(getFont().deriveFont(Font.BOLD));
            }

            return this;
        }
    }
}
