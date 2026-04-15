package bisuala;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.TaldeDAO;
import db.DBConnection;
import model.DenboraldiTalde;
import model.Denboraldia;
import model.Erabiltzaile;
import model.Federazioa;
import model.Jokalari;
import model.Talde;

/**
 * Presidentearen panela.
 * Federazioko talde guztiak erakusten ditu, denboraldian aktiboak berde eta ez-aktiboak gorriz.
 * Denboraldi berria sortu eta erabiltzaile berriak gehitzeko botoiak ditu.
 */
public class PanelPresi extends JPanel {
    private static final long serialVersionUID = 1L;

    /** Ez-aktiboak markatzeko argi gorria. */
    private final Color LIGHTRED = new Color(219, 175, 175);

    /** Aktiboak markatzeko argi berdea. */
    private final Color LIGHTGREEN = new Color(218, 245, 213);

    /** Aplikazioaren leiho nagusia. */
    private APP aplikazioNagusia;

    /** Federazioaren datuak. */
    private Federazioa federazioa;

    /**
     * Presidentearen panela sortzen du federazioaren talde guztiekin.
     *
     * @param erab               presidente erabiltzailea
     * @param federazioa         federazioaren datuak
     * @param unekoDenboraldia   uneko denboraldia (aktiboak zehazteko)
     * @param app                aplikazioaren leiho nagusia
     */
    public PanelPresi(Erabiltzaile erab, Federazioa federazioa, Denboraldia unekoDenboraldia, APP app) {
        this.aplikazioNagusia = app;
        this.federazioa = federazioa;
        setLayout(new BorderLayout());

        ArrayList<Talde> taldeGuztiak = federazioa.getTaldeGuztiak();
        ArrayList<Talde> taldeJokatzen = new ArrayList<>();
        if (unekoDenboraldia != null && unekoDenboraldia.getLigakoTaldeak() != null) {
            for (DenboraldiTalde dt : unekoDenboraldia.getLigakoTaldeak()) {
                taldeJokatzen.add(dt.getTalde());
            }
        }

        JLabel lblIzenburua = new JLabel("Federazioko Presidentea: " + erab.getErabiltzaile());
        lblIzenburua.setHorizontalAlignment(SwingConstants.CENTER);
        lblIzenburua.setFont(new Font("Arial", Font.BOLD, 16));
        lblIzenburua.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(lblIzenburua, BorderLayout.NORTH);

        JPanel pnlZerrenda = new JPanel();
        pnlZerrenda.setLayout(new BoxLayout(pnlZerrenda, BoxLayout.Y_AXIS));

        if (taldeGuztiak != null && !taldeGuztiak.isEmpty()) {
            ArrayList<Talde> taldeOrdenatuak = new ArrayList<>(taldeGuztiak);

            Collections.sort(taldeOrdenatuak, new Comparator<Talde>() {
                @Override
                public int compare(Talde t1, Talde t2) {
                    boolean t1Jokatzen = taldeJokatzen.contains(t1);
                    boolean t2Jokatzen = taldeJokatzen.contains(t2);
                    if (t1Jokatzen && !t2Jokatzen) return -1;
                    else if (!t1Jokatzen && t2Jokatzen) return 1;
                    else return t1.getIzena().compareToIgnoreCase(t2.getIzena());
                }
            });

            for (Talde t : taldeOrdenatuak) {
                boolean jokatzenAriDa = taldeJokatzen.contains(t);
                Color kolorea = jokatzenAriDa ? LIGHTGREEN : LIGHTRED;
                pnlZerrenda.add(sortuTaldePanela(t, kolorea, jokatzenAriDa));
            }
        }

        JScrollPane scroll = new JScrollPane(pnlZerrenda);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll, BorderLayout.CENTER);

        JPanel pnlBotoiak = new JPanel();

        JButton btnHasi = new JButton("Denboraldia hasi");
        btnHasi.addActionListener(e -> {
            LeihoaDenboraldiBerria leihoa = new LeihoaDenboraldiBerria(federazioa);
            leihoa.setVisible(true);

            if (leihoa.isOndoSortuDa()) {
                aplikazioNagusia.interfazeaFreskatu();
                aplikazioNagusia.setAldaketakDauden(true);
            }
        });
        pnlBotoiak.add(btnHasi);

        JButton btnUserBerria = new JButton("Erabiltzaile Berria");
        btnUserBerria.setBackground(new Color(70, 130, 180));
        btnUserBerria.setForeground(Color.WHITE);
        btnUserBerria.addActionListener(e -> {
            LeihoaErabiltzaileBerria leihoaUser = new LeihoaErabiltzaileBerria(aplikazioNagusia, federazioa, aplikazioNagusia);
            leihoaUser.setVisible(true);
        });

        pnlBotoiak.add(Box.createHorizontalStrut(20));
        pnlBotoiak.add(btnUserBerria);

        add(pnlBotoiak, BorderLayout.SOUTH);
    }

    /**
     * Talde baten panel osoa sortzen du: ezkutua, datuak eta jokalarien zerrenda.
     *
     * @param t              erakutsi nahi den taldea
     * @param kolorea        panelaren atze-kolorea
     * @param jokatzenAriDa  true bada denboraldian aktibo dago
     * @return sortutako talde-panela
     */
    private JPanel sortuTaldePanela(Talde t, Color kolorea, boolean jokatzenAriDa) {
        JPanel pnlTaldeaPresi = new JPanel(new BorderLayout(20, 0));
        pnlTaldeaPresi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(220, 220, 220)),
                new EmptyBorder(15, 10, 15, 10)));
        pnlTaldeaPresi.setBackground(kolorea);
        pnlTaldeaPresi.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JLabel lblEskutua = new JLabel();
        String path = "/images/TaldeArmarria/" + t.getEzkutua();
        URL imgUrl = getClass().getResource(path);
        kargatuEskutua(lblEskutua, imgUrl);

        JButton btnAldatuEskutua = new JButton("Aldatu");
        btnAldatuEskutua.setFont(new Font("Arial", Font.PLAIN, 10));
        btnAldatuEskutua.setMargin(new Insets(2, 5, 2, 5));
        btnAldatuEskutua.setFocusPainted(false);

        btnAldatuEskutua.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Aukeratu " + t.getIzena() + " taldearen ezkutu berria");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Irudiak (PNG, JPG, JPEG)", "png", "jpg", "jpeg"));

            int erantzuna = fileChooser.showOpenDialog(this);
            if (erantzuna == JFileChooser.APPROVE_OPTION) {
                File jatorrizkoFitxategia = fileChooser.getSelectedFile();
                String fitxategiIzena = jatorrizkoFitxategia.getName();
                File helmugaFitxategia = new File("resources/images/TaldeArmarria", fitxategiIzena);

                try {
                    Files.copy(jatorrizkoFitxategia.toPath(), helmugaFitxategia.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    t.setEzkutua(fitxategiIzena);
                    aplikazioNagusia.setAldaketakDauden(true);
                    ImageIcon ikonoBerria = new ImageIcon(helmugaFitxategia.getAbsolutePath());
                    Image irudia = ikonoBerria.getImage();
                    Image irudiaEskalatuta = irudia.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                    lblEskutua.setIcon(new ImageIcon(irudiaEskalatuta));
                    lblEskutua.setText("");
                    TaldeDAO tdao = new TaldeDAO(DBConnection.obtenerConexion());
                    boolean aldatu = tdao.aldatuArmarria(fitxategiIzena, t.getId());
                    JOptionPane.showMessageDialog(this,
                        "Ezkutua ondo aldatu da.\nGogoan izan 'Saioa Itxi' edo 'Gorde' sakatzea aldaketak XML-an mantentzeko.",
                        "Ezkutua Aldatuta", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Errorea irudia gordetzean: " + ex.getMessage(), "Errorea", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JPanel pnlIrudiaBotoia = new JPanel();
        pnlIrudiaBotoia.setLayout(new BoxLayout(pnlIrudiaBotoia, BoxLayout.Y_AXIS));
        pnlIrudiaBotoia.setBackground(kolorea);
        lblEskutua.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAldatuEskutua.setAlignmentX(Component.CENTER_ALIGNMENT);
        pnlIrudiaBotoia.add(Box.createVerticalGlue());
        pnlIrudiaBotoia.add(lblEskutua);
        pnlIrudiaBotoia.add(Box.createRigidArea(new Dimension(0, 5)));
        pnlIrudiaBotoia.add(btnAldatuEskutua);
        pnlIrudiaBotoia.add(Box.createVerticalGlue());
        pnlIrudiaBotoia.setPreferredSize(new Dimension(100, 130));
        pnlTaldeaPresi.add(pnlIrudiaBotoia, BorderLayout.WEST);

        JPanel pnlDatuak = new JPanel();
        pnlDatuak.setLayout(new BoxLayout(pnlDatuak, BoxLayout.Y_AXIS));
        pnlDatuak.setBackground(kolorea);

        JLabel lblIzena = new JLabel(t.getIzena().toUpperCase());
        lblIzena.setFont(new Font("Arial", Font.BOLD, 18));
        lblIzena.setForeground(new Color(135, 21, 33));
        lblIzena.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblInfo = new JLabel("Zelaia: " + t.getFutbolZelaia() + "  |  Herria: " + t.getHiria());
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInfo.setForeground(Color.GRAY);
        lblInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        String egoeraTestua = jokatzenAriDa ? "(Ligan Inskribatuta)" : "(Ez du jokatzen denboraldi honetan)";
        JLabel lblEgoera = new JLabel(egoeraTestua);
        lblEgoera.setFont(new Font("Arial", Font.ITALIC, 10));
        lblEgoera.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlDatuak.add(lblIzena);
        pnlDatuak.add(Box.createRigidArea(new Dimension(0, 4)));
        pnlDatuak.add(lblInfo);
        pnlDatuak.add(lblEgoera);
        pnlDatuak.add(Box.createRigidArea(new Dimension(0, 12)));

        JPanel pnlJokalariak = new JPanel(new GridLayout(0, 2, 10, 5));
        pnlJokalariak.setBackground(kolorea);
        pnlJokalariak.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblJokIzenburua = new JLabel("JOKALARIAK:");
        lblJokIzenburua.setFont(new Font("Arial", Font.BOLD, 11));
        lblJokIzenburua.setAlignmentX(Component.LEFT_ALIGNMENT);

        pnlDatuak.add(lblJokIzenburua);
        pnlDatuak.add(Box.createRigidArea(new Dimension(0, 5)));

        if (t.getJokalariak() != null && !t.getJokalariak().isEmpty()) {
            for (Jokalari j : t.getJokalariak()) {
                String testua = "• " + j.getDortsala() + " - " + j.getIzena() + " (" + j.getPosizio() + ")";
                JLabel lblJokalari = new JLabel(testua);
                lblJokalari.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                lblJokalari.setHorizontalAlignment(SwingConstants.LEFT);
                pnlJokalariak.add(lblJokalari);
            }
        } else {
            JLabel lblHutsik = new JLabel("Ez dago jokalaririk");
            lblHutsik.setForeground(Color.RED);
            pnlJokalariak.add(lblHutsik);
        }
        pnlDatuak.add(pnlJokalariak);
        pnlTaldeaPresi.add(pnlDatuak, BorderLayout.CENTER);

        return pnlTaldeaPresi;
    }

    /**
     * Ezkutuaren irudia kargatu eta etiketan jartzen du.
     * Irudia aurkitzen ez bada, "Ez dago" testua jartzen du.
     *
     * @param lblEskutua irudia jasoko duen etiketa
     * @param imgUrl     irudiaren URL-a
     */
    private void kargatuEskutua(JLabel lblEskutua, URL imgUrl) {
        if (imgUrl != null) {
            ImageIcon ikonoOriginala = new ImageIcon(imgUrl);
            Image irudia = ikonoOriginala.getImage();
            Image irudiaEskalatuta = irudia.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
            lblEskutua.setIcon(new ImageIcon(irudiaEskalatuta));
            lblEskutua.setText("");
        } else {
            lblEskutua.setIcon(null);
            lblEskutua.setText("Ez dago");
        }
    }
}
