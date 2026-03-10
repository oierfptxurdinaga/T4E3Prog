package bisuala;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import model.*;

public class PanelTaldeak extends JPanel {

    private static final long serialVersionUID = 1L;
    private JComboBox<Talde> comboTaldeak;
    private JPanel panelInfoTaldea;
    private JPanel panelJokalariak;
    private int urtea;

    public PanelTaldeak(ArrayList<Talde> taldeak, int urtea) {
        this.urtea = urtea;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // --- 1. IPARRALDEKO ZONA (Bi zati: ComboBox + InfoTaldea) ---
        JPanel panelNorteContainer = new JPanel();
        panelNorteContainer.setLayout(new BoxLayout(panelNorteContainer, BoxLayout.Y_AXIS));
        panelNorteContainer.setBackground(Color.WHITE);
        JPanel panelCombo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCombo.setBackground(Color.WHITE);
        panelCombo.setBorder(new EmptyBorder(15, 15, 5, 15));
        
        JLabel lblTitulo = new JLabel("Aukeratu Taldea: ");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        
        comboTaldeak = new JComboBox<>();
        if (taldeak != null) {
            for (Talde t : taldeak) {
                comboTaldeak.addItem(t);
            }
        }
        comboTaldeak.setPreferredSize(new Dimension(250, 30));
        comboTaldeak.addActionListener(e -> kargatuDatuak());

        panelCombo.add(lblTitulo);
        panelCombo.add(comboTaldeak);
        
        // B. TALDE INFO PANELA
        panelInfoTaldea = new JPanel(new BorderLayout());
        panelInfoTaldea.setBackground(Color.WHITE);
        // Borde gris fina behean
        panelInfoTaldea.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(5, 20, 15, 20),
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))
        ));
        panelInfoTaldea.setPreferredSize(new Dimension(0, 100));

        panelNorteContainer.add(panelCombo);
        panelNorteContainer.add(panelInfoTaldea);
        
        add(panelNorteContainer, BorderLayout.NORTH);

        // --- 2. ERDIA: JOKALARIEN GRID-A ---
        panelJokalariak = new JPanel(new GridLayout(0, 4, 15, 15)); 
        panelJokalariak.setBackground(Color.WHITE);
        panelJokalariak.setBorder(new EmptyBorder(10, 15, 10, 15));
        
        JScrollPane scroll = new JScrollPane(panelJokalariak);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        // Hasierako karga
        if (taldeak != null && !taldeak.isEmpty()) {
            kargatuDatuak();
        }
    }

    private void kargatuDatuak() {
        Talde aukeratua = (Talde) comboTaldeak.getSelectedItem();
        if (aukeratua == null) return;

        eguneratuTaldeInfo(aukeratua);

        panelJokalariak.removeAll();
        ArrayList<Jokalari> lista = aukeratua.getJokalariak();
        
        if (lista != null) {
            for (Jokalari j : lista) {
                panelJokalariak.add(sortuJokalariKarta(j));
            }
        } else {
            panelJokalariak.add(new JLabel("Ez dago jokalaririk."));
        }

        panelJokalariak.revalidate();
        panelJokalariak.repaint();
    }

    // --- TALDEAREN INFORMAZIOA (ESCUDO + TEXTO CLÁSICO) ---
    private void eguneratuTaldeInfo(Talde t) {
        panelInfoTaldea.removeAll();

        // 1. EZKUTUA
        JLabel lblEscudo = new JLabel();
        lblEscudo.setPreferredSize(new Dimension(80, 80));
        lblEscudo.setHorizontalAlignment(SwingConstants.CENTER);
        
        // ALDAKETA NAGUSIA HEMEN: Objektuari galdetzen diogu bidea
        String irudiBidea = t.getEzkutua(); 
        
        if (irudiBidea != null && !irudiBidea.isEmpty()) {
            try {
                URL url = getClass().getResource(irudiBidea);
                if (url != null) {
                    ImageIcon original = new ImageIcon(url);
                    Image img = original.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    lblEscudo.setIcon(new ImageIcon(img));
                } else {
                     // Bidea existitzen da baina fitxategia ez
                     lblEscudo.setText("No Img");
                }
            } catch (Exception e) {
                lblEscudo.setText("Errorea");
            }
        } else {
             lblEscudo.setText("No Img");
        }

        // 2. TESTUA (Estilo garbia)
        JPanel pnlText = new JPanel(new GridLayout(2, 1));
        pnlText.setBackground(Color.WHITE);
        pnlText.setBorder(new EmptyBorder(0, 20, 0, 0));
        
        JLabel lblIzena = new JLabel(t.getIzena().toUpperCase());
        lblIzena.setFont(new Font("Arial", Font.BOLD, 24));
        lblIzena.setForeground(new Color(50, 50, 50)); // Gris iluna
        
        String azpiTestua = "Hiria: " + t.getHiria() + "  |  Estadioa: " + t.getFutbolZelaia();
        JLabel lblAzpi = new JLabel(azpiTestua);
        lblAzpi.setFont(new Font("Arial", Font.PLAIN, 14));
        lblAzpi.setForeground(Color.GRAY); // Gris argiagoa
        
        pnlText.add(lblIzena);
        pnlText.add(lblAzpi);

        panelInfoTaldea.add(lblEscudo, BorderLayout.WEST);
        panelInfoTaldea.add(pnlText, BorderLayout.CENTER);

        panelInfoTaldea.revalidate();
        panelInfoTaldea.repaint();
    }

    // --- JOKALARI KARTA ---
    private JPanel sortuJokalariKarta(Jokalari j) {
        JPanel karta = new JPanel(new BorderLayout());
        karta.setBackground(new Color(250, 250, 252));
        karta.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        JLabel lblIrudia = new JLabel();
        lblIrudia.setHorizontalAlignment(SwingConstants.CENTER);
        lblIrudia.setPreferredSize(new Dimension(100, 100));
        
        ImageIcon icon = sortuAvatarLokala(j.getIzena(), j.getAbizena(), this.urtea);
        lblIrudia.setIcon(icon);
        
        JPanel panelDatos = new JPanel(new GridLayout(2, 1));
        panelDatos.setBackground(null);
        panelDatos.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JLabel lblIzena = new JLabel(j.getIzena());
        lblIzena.setFont(new Font("Arial", Font.BOLD, 12));
        lblIzena.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblInfo = new JLabel("#" + j.getDortsala() + " - " + j.getPosizio());
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 10));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelDatos.add(lblIzena);
        panelDatos.add(lblInfo);

        karta.add(lblIrudia, BorderLayout.CENTER);
        karta.add(panelDatos, BorderLayout.SOUTH);

        return karta;
    }

    private ImageIcon sortuAvatarLokala(String izena, String abizena, int urtea) {
        int size = 80;
        BufferedImage imagen = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = imagen.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        float baseHue = Math.abs((izena + abizena).hashCode() % 1000) / 1000f;
        float yearShift = (urtea % 100) * 0.2f; 
        float finalHue = (baseHue + yearShift) % 1.0f;
        
        Color colorFondo = Color.getHSBColor(finalHue, 0.6f, 0.85f);

        g2.setColor(colorFondo);
        g2.fillOval(0, 0, size, size);
        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        
        String iniciales = "";
        if (izena.length() > 0) iniciales += izena.charAt(0);
        if (abizena.length() > 0) iniciales += abizena.charAt(0);
        
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(iniciales)) / 2;
        int y = ((size - fm.getHeight()) / 2) + fm.getAscent();
        
        g2.drawString(iniciales.toUpperCase(), x, y);
        g2.dispose();
        return new ImageIcon(imagen);
    }
}