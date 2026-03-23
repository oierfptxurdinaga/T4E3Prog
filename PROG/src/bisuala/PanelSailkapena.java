package bisuala;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import model.*; 

public class PanelSailkapena extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable taula;
    private DefaultTableModel modeloa;
    private int urtea;

    // Datuak zuzenean jasotzen ditugu
    // Ez dugu hemen kalkulurik egiten, datuak erakutsi bakarrik.
    public PanelSailkapena(ArrayList<DenboraldiTalde> listaStats, int urtea) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        this.urtea = urtea;

        // Izenburua
        JLabel lblIzenburua = new JLabel("SAILKAPENA");
        lblIzenburua.setFont(new Font("Arial", Font.BOLD, 24));
        lblIzenburua.setForeground(new Color(135, 21, 33));
        lblIzenburua.setHorizontalAlignment(SwingConstants.CENTER);
        lblIzenburua.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblIzenburua, BorderLayout.NORTH);

        // Zerrenda ordenatu: Lehenengo puntuak, gero gol diferentzia
        listaStats.sort((t1, t2) -> {
            int diffPuntuak = Integer.compare(t2.getPts(), t1.getPts());
            if (diffPuntuak != 0)
                return diffPuntuak;
            return Integer.compare(t2.getDG(), t1.getDG());
        });

        // Taulako datuak prestatu
        String[] zutabeak = { "Pos", "Taldea", "PJ", "I", "B", "G", "GF", "GC", "AVG", "PTS" };
        Object[][] data = new Object[listaStats.size()][10];

        for (int i = 0; i < listaStats.size(); i++) {
            DenboraldiTalde dt = listaStats.get(i);
            int average = dt.getDG();

            data[i][0] = i + 1; // Posizioa
            
            // Objektu osoa pasatzen dugu, gero Rendererrak aterako ditu izena eta argazkia
            data[i][1] = dt; 
            
            data[i][2] = dt.getPJ(); 
            data[i][3] = dt.getG();  // Irabaziak
            data[i][4] = dt.getE();  // Berdinduak
            data[i][5] = dt.getP();  // Galduak
            data[i][6] = dt.getGF(); // Aldeko golak
            data[i][7] = dt.getGC(); // Kontrako golak
            data[i][8] = average > 0 ? "+" + average : average;
            data[i][9] = dt.getPts(); // Puntuak
        }

        // Modeloa sortu
        modeloa = new DefaultTableModel(data, zutabeak) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            // Hau beharrezkoa da Rendererrak jakin dezan 1. zutabea objektu bat dela
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1)
                    return DenboraldiTalde.class;
                return super.getColumnClass(columnIndex);
            }
        };

        taula = new JTable(modeloa);
        
        // Inprimatzeko botoia
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

        // Diseinua aplikatu
        konfiguratuDiseinua();

        JScrollPane scroll = new JScrollPane(taula);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scroll, BorderLayout.CENTER);
    }

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

    // --- RENDERER PERTSONALIZATUA ---
    // Gelaxkak nola margotu definitzen du (batez ere irudiak jartzeko)
    private class EstiloRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        private boolean zentratu;

        public EstiloRenderer(boolean zentratu) {
            this.zentratu = zentratu;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setIcon(null);

            // 1. zutabea bada eta DenboraldiTalde motakoa bada, datuak atera
            if (column == 1 && value instanceof DenboraldiTalde) {
                DenboraldiTalde stats = (DenboraldiTalde) value;

                // Taldearen izena
                setText(stats.getTalde().getIzena());

                // Irudia kargatu
                String rutaImagen = "/images/TaldeArmarria/"+stats.getTalde().getEzkutua();
                if (rutaImagen != null) {
                    URL imgUrl = getClass().getResource(rutaImagen);
                    if (imgUrl != null) {
                        ImageIcon icon = new ImageIcon(imgUrl);
                        Image img = icon.getImage();
                        // Eskalatu txikiagoa izan dadin
                        Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                        setIcon(new ImageIcon(newImg));
                    }
                }

                setHorizontalAlignment(JLabel.LEFT);
                setBorder(new EmptyBorder(0, 10, 0, 0));
            } else {
                if (zentratu)
                    setHorizontalAlignment(JLabel.CENTER);
                else {
                    setHorizontalAlignment(JLabel.LEFT);
                    setBorder(new EmptyBorder(0, 10, 0, 0));
                }
            }
            
            // Ilaretako koloreak tartekatu
            if (!isSelected) {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 250));
            }
            // Letra beltza lehenengo eta azken zutabean
            if (column == 0 || column == 9) {
                setFont(getFont().deriveFont(Font.BOLD));
            }

            return this;
        }
    }
    
    public void eguneratuSailkapena(ArrayList<DenboraldiTalde> listaStatsBerria, int urteaBerria) {
        this.urtea = urteaBerria;
        
        // Taula hustu
        modeloa.setRowCount(0); 

        if (listaStatsBerria == null || listaStatsBerria.isEmpty()) {
            return; // Ez badago daturik, hutsik uzten dugu
        }

        // Zerrenda berriro ordenatu: Lehenengo puntuak, gero gol diferentzia
        listaStatsBerria.sort((t1, t2) -> {
            int diffPuntuak = Integer.compare(t2.getPts(), t1.getPts());
            if (diffPuntuak != 0) return diffPuntuak;
            return Integer.compare(t2.getDG(), t1.getDG());
        });

        // Datu berriak taulara sartu banan-banan
        for (int i = 0; i < listaStatsBerria.size(); i++) {
            DenboraldiTalde dt = listaStatsBerria.get(i);
            int average = dt.getDG();

            Object[] errenkada = new Object[10];
            errenkada[0] = i + 1; // Posizioa
            errenkada[1] = dt.getTalde();    // Objektua (Renderer-ak irudia jarriko du)
            errenkada[2] = dt.getPJ(); 
            errenkada[3] = dt.getG();  
            errenkada[4] = dt.getE();  
            errenkada[5] = dt.getP();  
            errenkada[6] = dt.getGF(); 
            errenkada[7] = dt.getGC(); 
            errenkada[8] = average > 0 ? "+" + average : average;
            errenkada[9] = dt.getPts(); 

            modeloa.addRow(errenkada); // Ilarak gehitu modeloari
        }
    }
}