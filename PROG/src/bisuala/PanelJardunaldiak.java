package bisuala;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Denboraldia;
import model.Jardunaldi;
import model.Partidua;
import model.Talde;

/**
 * Jardunaldiak ikusteko panela.
 * Combo-box baten bidez jardunaldia aukeratu eta partida guztiak erakusten ditu,
 * emaitzak eta koloreak barne (irabazia, galera edo berdinketa).
 */
public class PanelJardunaldiak extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Irabazitako taldearen atze-kolorea. */
    private final Color KOLORE_IRABAZI = new Color(200, 255, 200);

    /** Galdutako taldearen atze-kolorea. */
    private final Color KOLORE_GALDU = new Color(255, 220, 220);

    /** Berdinketa kasuko atze-kolorea. */
    private final Color KOLORE_BERDINKETA = new Color(245, 245, 245);

    /** Jokatu gabeko partiduen atze-kolorea. */
    private final Color KOLORE_JOKATU_GABE = Color.WHITE;

    /** Jardunaldiak aukeratzeko combo-box-a. */
    private JComboBox<String> comboJardunaldiak;

    /** Partiduak erakusten dituen panela. */
    private JPanel panelPartiduak;

    /** Erakusten ari den denboraldia. */
    private Denboraldia denboraldia;

    /**
     * Jardunaldiak panela sortzen du emandako denboraldiarekin.
     *
     * @param d erakutsi nahi den denboraldia
     */
    public PanelJardunaldiak(Denboraldia d) {
        this.denboraldia = d;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel panelGoikoa = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelGoikoa.setBackground(Color.WHITE);
        panelGoikoa.setBorder(new EmptyBorder(15, 15, 5, 15));

        JLabel lblIzenburua = new JLabel("Aukeratu Jardunaldia: ");
        lblIzenburua.setFont(new Font("Arial", Font.BOLD, 14));
        panelGoikoa.add(lblIzenburua);

        comboJardunaldiak = new JComboBox<>();
        comboJardunaldiak.setPreferredSize(new Dimension(200, 30));
        comboJardunaldiak.addActionListener(e -> eguneratuPartiduak());

        panelGoikoa.add(comboJardunaldiak);
        add(panelGoikoa, BorderLayout.NORTH);

        panelPartiduak = new JPanel();
        panelPartiduak.setLayout(new BoxLayout(panelPartiduak, BoxLayout.Y_AXIS));
        panelPartiduak.setBackground(Color.WHITE);
        panelPartiduak.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(panelPartiduak);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll, BorderLayout.CENTER);

        datuakKargatu();
    }

    /**
     * Denboraldia aldatzen denean panela eguneratzen du.
     * APP.java-tik deitzen da denboraldia aldatzean.
     *
     * @param dBerria denboraldi berria
     */
    public void denboraldiaAldatu(Denboraldia dBerria) {
        this.denboraldia = dBerria;
        datuakKargatu();
    }

    /**
     * Combo-box-a jardunaldiekin betetzen du eta lehenengoa aukeratzen du.
     */
    private void datuakKargatu() {
        ActionListener[] listeners = comboJardunaldiak.getActionListeners();
        for (ActionListener al : listeners) comboJardunaldiak.removeActionListener(al);

        comboJardunaldiak.removeAllItems();
        panelPartiduak.removeAll();

        if (denboraldia != null && denboraldia.getLigakoJardunaldi() != null) {
            for (Jardunaldi j : denboraldia.getLigakoJardunaldi()) {
                comboJardunaldiak.addItem("Jardunaldia " + j.getJardunaldiZbk());
            }
        }

        for (ActionListener al : listeners) comboJardunaldiak.addActionListener(al);

        if (comboJardunaldiak.getItemCount() > 0) {
            comboJardunaldiak.setSelectedIndex(0);
            eguneratuPartiduak();
        } else {
            JLabel lblHutsik = new JLabel("Ez dago jardunaldirik denboraldi honetan.");
            lblHutsik.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelPartiduak.add(lblHutsik);
            panelPartiduak.revalidate();
            panelPartiduak.repaint();
        }
    }

    /**
     * Aukeratutako jardunaldiko partiduak pantailan kargatzen ditu.
     */
    private void eguneratuPartiduak() {
        panelPartiduak.removeAll();

        int index = comboJardunaldiak.getSelectedIndex();
        if (index >= 0 && denboraldia.getLigakoJardunaldi() != null) {
            Jardunaldi jardunaldia = denboraldia.getLigakoJardunaldi().get(index);

            if (jardunaldia.getPartiduak() != null) {
                for (Partidua p : jardunaldia.getPartiduak()) {
                    panelPartiduak.add(sortuPartiduPanela(p));
                    panelPartiduak.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }
        panelPartiduak.revalidate();
        panelPartiduak.repaint();
    }

    /**
     * Partida bat erakusteko panel bat sortzen du emaitzarekin eta koloreekin.
     *
     * @param p erakutsi nahi den partida
     * @return sortutako partida-panela
     */
    private JPanel sortuPartiduPanela(Partidua p) {
        JPanel panelErrenkada = new JPanel(new BorderLayout(10, 0));
        panelErrenkada.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        panelErrenkada.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panelErrenkada.setPreferredSize(new Dimension(500, 80));

        Color atzekoKoloreEtxekoa = KOLORE_JOKATU_GABE;
        Color atzekoKoloreKanpokoa = KOLORE_JOKATU_GABE;

        if (p.jokatutaDago()) {
            if (p.getEtxekoGolak() > p.getKanpokoGolak()) {
                atzekoKoloreEtxekoa = KOLORE_IRABAZI;
                atzekoKoloreKanpokoa = KOLORE_GALDU;
            } else if (p.getKanpokoGolak() > p.getEtxekoGolak()) {
                atzekoKoloreEtxekoa = KOLORE_GALDU;
                atzekoKoloreKanpokoa = KOLORE_IRABAZI;
            } else {
                atzekoKoloreEtxekoa = KOLORE_BERDINKETA;
                atzekoKoloreKanpokoa = KOLORE_BERDINKETA;
            }
        }

        JPanel pnlEtxekoa = sortuTaldePanela(p.getEtxekoTaldea(), SwingConstants.RIGHT, atzekoKoloreEtxekoa);
        JPanel pnlKanpokoa = sortuTaldePanela(p.getKanpokoTaldea(), SwingConstants.LEFT, atzekoKoloreKanpokoa);

        JPanel pnlMarkagailua = new JPanel(new GridBagLayout());
        pnlMarkagailua.setBackground(Color.WHITE);
        pnlMarkagailua.setPreferredSize(new Dimension(100, 0));

        JLabel lblEmaitza = new JLabel();
        lblEmaitza.setFont(new Font("Arial", Font.BOLD, 24));

        if (p.jokatutaDago()) {
            lblEmaitza.setText(p.getEtxekoGolak() + " - " + p.getKanpokoGolak());
        } else {
            lblEmaitza.setText("vs");
            lblEmaitza.setForeground(Color.GRAY);
            lblEmaitza.setFont(new Font("Arial", Font.ITALIC, 18));
        }
        pnlMarkagailua.add(lblEmaitza);

        panelErrenkada.add(pnlEtxekoa, BorderLayout.WEST);
        panelErrenkada.add(pnlMarkagailua, BorderLayout.CENTER);
        panelErrenkada.add(pnlKanpokoa, BorderLayout.EAST);

        return panelErrenkada;
    }

    /**
     * Talde baten izena eta ezkutua erakusten dituen panel bat sortzen du.
     *
     * @param t             erakutsi nahi den taldea
     * @param alineazioa    SwingConstants.RIGHT edo LEFT
     * @param atzekoKolorea panelaren atze-kolorea
     * @return sortutako talde-panela
     */
    private JPanel sortuTaldePanela(Talde t, int alineazioa, Color atzekoKolorea) {
        JPanel p = new JPanel(new FlowLayout(alineazioa == SwingConstants.RIGHT ? FlowLayout.RIGHT : FlowLayout.LEFT, 10, 5));
        p.setBackground(atzekoKolorea);
        p.setPreferredSize(new Dimension(250, 60));

        JLabel lblIzena = new JLabel(t.getIzena());
        lblIzena.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblEzkutua = new JLabel();
        if (t.getEzkutua() != null) {
            URL url = getClass().getResource("/images/TaldeArmarria/"+t.getEzkutua());
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                lblEzkutua.setIcon(new ImageIcon(img));
            }
        }

        if (alineazioa == SwingConstants.RIGHT) {
            p.add(lblIzena);
            p.add(lblEzkutua);
        } else {
            p.add(lblEzkutua);
            p.add(lblIzena);
        }
        return p;
    }
}
