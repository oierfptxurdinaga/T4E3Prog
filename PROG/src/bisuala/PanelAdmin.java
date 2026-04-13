package bisuala;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Erabiltzaile;
import model.Federazioa;
import model.Jokalari;
import model.Talde;

/**
 * Administratzailearen panela.
 * Bi zerrendetako jokalariak talde batetik bestera mugitzeko aukera ematen du
 * merkatu-leiho baten antzera.
 */
public class PanelAdmin extends JPanel {

    private static final long serialVersionUID = 1L;

    /** Aplikazioaren leiho nagusia. */
    private APP app;

    /** Federazioaren datuak. */
    private Federazioa federazioa;

    /** Denboraldian parte hartzen duten taldeen zerrenda. */
    private ArrayList<Talde> taldeAktiboak;

    /** Ezkerreko taldea aukeratzeko combo-box-a. */
    private JComboBox<Talde> comboEzkerra;

    /** Eskumako taldea aukeratzeko combo-box-a. */
    private JComboBox<Talde> comboEskuma;

    /** Ezkerreko jokalarien zerrenda-modeloa. */
    private DefaultListModel<Jokalari> modelEzkerra;

    /** Eskumako jokalarien zerrenda-modeloa. */
    private DefaultListModel<Jokalari> modelEskuma;

    /** Ezkerreko jokalarien zerrenda bisual. */
    private JList<Jokalari> listEzkerra;

    /** Eskumako jokalarien zerrenda bisual. */
    private JList<Jokalari> listEskuma;

    /** Jokalaria eskuinera mugitzeko botoia. */
    private JButton btnMugituEskuinera;

    /** Jokalaria ezkerrera mugitzeko botoia. */
    private JButton btnMugituEzkerrera;

    /**
     * Admin panela sortzen du bi zerrendekin eta mugitzeko botoiekin.
     *
     * @param erab          erabiltzaile aktiboak (erreferentziarako)
     * @param federazioa    federazioaren datuak
     * @param taldeAktiboak denboraldian parte hartzen duten taldeak
     * @param app           aplikazioaren leiho nagusia
     */
    public PanelAdmin(Erabiltzaile erab, Federazioa federazioa, ArrayList<Talde> taldeAktiboak, APP app) {
        this.app = app;
        this.federazioa = federazioa;
        this.taldeAktiboak = taldeAktiboak;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblIzenburua = new JLabel("ADMINISTRAZIOA - Jokalarien Kudeaketa (Merkatu leihoa)");
        lblIzenburua.setFont(new Font("Arial", Font.BOLD, 22));
        lblIzenburua.setForeground(new Color(50, 50, 50));
        lblIzenburua.setHorizontalAlignment(SwingConstants.CENTER);
        lblIzenburua.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblIzenburua, BorderLayout.NORTH);

        JPanel panelNagusia = new JPanel(new GridLayout(1, 3, 20, 0));
        panelNagusia.setBackground(Color.WHITE);

        JPanel pnlEzkerra = new JPanel(new BorderLayout(0, 10));
        pnlEzkerra.setOpaque(false);
        comboEzkerra = sortuTaldeCombo();
        pnlEzkerra.add(comboEzkerra, BorderLayout.NORTH);
        modelEzkerra = new DefaultListModel<>();
        listEzkerra = sortuJokalariLista(modelEzkerra);
        pnlEzkerra.add(new JScrollPane(listEzkerra), BorderLayout.CENTER);

        JPanel pnlBotoiak = new JPanel(new GridBagLayout());
        pnlBotoiak.setOpaque(false);
        btnMugituEskuinera = sortuBotoia("Hona Mugitu  >>>", new Color(70, 130, 180));
        btnMugituEzkerrera = sortuBotoia("<<<  Hona Mugitu", new Color(70, 130, 180));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(10, 10, 10, 10); gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlBotoiak.add(btnMugituEskuinera, gbc);
        gbc.gridy = 1;
        pnlBotoiak.add(btnMugituEzkerrera, gbc);

        JPanel pnlEskuma = new JPanel(new BorderLayout(0, 10));
        pnlEskuma.setOpaque(false);
        comboEskuma = sortuTaldeCombo();
        pnlEskuma.add(comboEskuma, BorderLayout.NORTH);
        modelEskuma = new DefaultListModel<>();
        listEskuma = sortuJokalariLista(modelEskuma);
        pnlEskuma.add(new JScrollPane(listEskuma), BorderLayout.CENTER);

        panelNagusia.add(pnlEzkerra);
        panelNagusia.add(pnlBotoiak);
        panelNagusia.add(pnlEskuma);
        add(panelNagusia, BorderLayout.CENTER);

        comboEzkerra.addActionListener(e -> { eguneratuLista(comboEzkerra, modelEzkerra); botoiakEguneratu(); });
        comboEskuma.addActionListener(e -> { eguneratuLista(comboEskuma, modelEskuma); botoiakEguneratu(); });
        btnMugituEskuinera.addActionListener(e -> mugituJokalaria(true));
        btnMugituEzkerrera.addActionListener(e -> mugituJokalaria(false));

        if (comboEzkerra.getItemCount() > 0) comboEzkerra.setSelectedIndex(0);
        if (comboEskuma.getItemCount() > 1) comboEskuma.setSelectedIndex(1);
    }

    /**
     * Taldeak aukeratzeko combo-box bat sortzen du talde aktiboekin.
     *
     * @return taldeen combo-box-a
     */
    private JComboBox<Talde> sortuTaldeCombo() {
        JComboBox<Talde> combo = new JComboBox<>();
        for (Talde t : taldeAktiboak) {
            combo.addItem(t);
        }
        combo.setPreferredSize(new Dimension(200, 40));
        return combo;
    }

    /**
     * Jokalarien zerrenda bisual bat sortzen du, izenarekin eta abizenarekin.
     *
     * @param model zerrendaren modeloa
     * @return jokalarien JList-a
     */
    private JList<Jokalari> sortuJokalariLista(DefaultListModel<Jokalari> model) {
        JList<Jokalari> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(30);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Jokalari) {
                    setText(((Jokalari) value).getIzena() + " " + ((Jokalari) value).getAbizena());
                }
                return this;
            }
        });
        return list;
    }

    /**
     * Botoi bat sortzen du kolore eta testu batekin.
     *
     * @param text botoiaren testua
     * @param bg   botoiaren atzeko kolorea
     * @return sortutako botoia
     */
    private JButton sortuBotoia(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 40));
        return btn;
    }

    /**
     * Combo-box bateko aukeratutako taldearen jokalariak zerrendan kargatzen ditu.
     *
     * @param combo aukeratutako taldea duen combo-box-a
     * @param model eguneratu beharreko zerrendaren modeloa
     */
    private void eguneratuLista(JComboBox<Talde> combo, DefaultListModel<Jokalari> model) {
        model.clear();
        Talde t = (Talde) combo.getSelectedItem();
        if (t != null && t.getJokalariak() != null) {
            for (Jokalari j : t.getJokalariak()) model.addElement(j);
        }
    }

    /**
     * Bi taldeetan talde bera aukeratuta badago, mugitzeko botoiak desgaitzen ditu.
     */
    private void botoiakEguneratu() {
        Talde t1 = (Talde) comboEzkerra.getSelectedItem();
        Talde t2 = (Talde) comboEskuma.getSelectedItem();
        boolean berdinak = (t1 != null && t2 != null && t1.equals(t2));
        btnMugituEskuinera.setEnabled(!berdinak);
        btnMugituEzkerrera.setEnabled(!berdinak);
    }

    /**
     * Hautatutako jokalaria jatorrizko taldetik helburu-taldera mugitzen du.
     * Aldaketa pantailan, federazioaren zerrendan eta datu-basean egiten da.
     *
     * @param eskuinera true bada ezkerretik eskuinera mugitzen du; false bada alderantziz
     */
    private void mugituJokalaria(boolean eskuinera) {
        JList<Jokalari> jatorrizkoLista = eskuinera ? listEzkerra : listEskuma;
        DefaultListModel<Jokalari> jatorrizkoModel = eskuinera ? modelEzkerra : modelEskuma;
        DefaultListModel<Jokalari> helburuModel = eskuinera ? modelEskuma : modelEzkerra;

        Talde jatorrizkoTaldea = (Talde) (eskuinera ? comboEzkerra.getSelectedItem() : comboEskuma.getSelectedItem());
        Talde helburuTaldea = (Talde) (eskuinera ? comboEskuma.getSelectedItem() : comboEzkerra.getSelectedItem());
        Jokalari hautatua = jatorrizkoLista.getSelectedValue();

        if (hautatua == null || jatorrizkoTaldea == null || helburuTaldea == null) return;

        jatorrizkoTaldea.getJokalariak().remove(hautatua);
        helburuTaldea.sartuJokalaria(hautatua);

        jatorrizkoModel.removeElement(hautatua);
        helburuModel.addElement(hautatua);

        aplikatuAldaketaFederazioan(jatorrizkoTaldea, helburuTaldea, hautatua);

        boolean ondoGordeta = dao.JokalariDAO.aldatuJokalariarenTaldeaDB(hautatua.getId(), helburuTaldea.getId());

        if (ondoGordeta) {
            if (app != null) app.setAldaketakDauden(true);
            utils.LogKudeatzailea.gehituLog("DB EGUNERAKETA: " + hautatua.getIzena() + " " + hautatua.getAbizena() + " - " + hautatua.getDortsala() + " jokalariaren taldea aldatu da.");
        } else {
            JOptionPane.showMessageDialog(this, "Errorea egon da jokalaria datu-basean eguneratzean.", "Errorea DBan", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Aldaketa berbera federazioaren zerrenda nagusian bilatzen eta aplikatzen du.
     * Objektu ezberdinak direnez, izenaren bidez bilatzen du.
     *
     * @param tOrig jatorrizko taldea
     * @param tDest helburu-taldea
     * @param jok   mugitu beharreko jokalaria
     */
    private void aplikatuAldaketaFederazioan(Talde tOrig, Talde tDest, Jokalari jok) {
        Talde masterOrig = null;
        Talde masterDest = null;

        for (Talde t : federazioa.getTaldeGuztiak()) {
            if (t.getIzena().equals(tOrig.getIzena())) masterOrig = t;
            if (t.getIzena().equals(tDest.getIzena())) masterDest = t;
        }

        if (masterOrig != null && masterDest != null) {
            Jokalari masterJok = null;

            for (Jokalari j : masterOrig.getJokalariak()) {
                if (j.getIzena().equals(jok.getIzena()) && j.getAbizena().equals(jok.getAbizena())) {
                    masterJok = j;
                    break;
                }
            }

            if (masterJok != null) {
                masterOrig.getJokalariak().remove(masterJok);
                masterDest.getJokalariak().add(masterJok);

                String logMezua = "FITXAKETA: " + masterJok.getIzena() + " " + masterJok.getAbizena() +
                                  " mugitu da (" + masterOrig.getIzena() + " -> " + masterDest.getIzena() + ")";
                utils.LogKudeatzailea.gehituLog(logMezua);
            }
        }
    }
}
