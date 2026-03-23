package bisuala;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import model.*;

public class PanelAdmin extends JPanel {

    private static final long serialVersionUID = 1L;

    private APP app;
    private Federazioa federazioa;          
    private ArrayList<Talde> taldeAktiboak; 
    
    // UI Osagaiak
    private JComboBox<Talde> comboEzkerra;
    private JComboBox<Talde> comboEskuma;
    
    private DefaultListModel<Jokalari> modelEzkerra;
    private DefaultListModel<Jokalari> modelEskuma;
    
    private JList<Jokalari> listEzkerra;
    private JList<Jokalari> listEskuma;
    
    private JButton btnMugituEskuinera;
    private JButton btnMugituEzkerrera;

    public PanelAdmin(Erabiltzaile erab, Federazioa federazioa, ArrayList<Talde> taldeAktiboak, APP app) {
        this.app = app;
        this.federazioa = federazioa;
        this.taldeAktiboak = taldeAktiboak;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- IZENBURUA ---
        JLabel lblIzenburua = new JLabel("ADMINISTRAZIOA - Jokalarien Kudeaketa (Merkatu leihoa)");
        lblIzenburua.setFont(new Font("Arial", Font.BOLD, 22));
        lblIzenburua.setForeground(new Color(50, 50, 50));
        lblIzenburua.setHorizontalAlignment(SwingConstants.CENTER);
        lblIzenburua.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblIzenburua, BorderLayout.NORTH);

        // --- PANEL NAGUSIA ---
        JPanel panelNagusia = new JPanel(new GridLayout(1, 3, 20, 0));
        panelNagusia.setBackground(Color.WHITE);

        // EZKERRA
        JPanel pnlEzkerra = new JPanel(new BorderLayout(0, 10));
        pnlEzkerra.setOpaque(false);
        comboEzkerra = sortuTaldeCombo(); // Usa taldeAktiboak
        pnlEzkerra.add(comboEzkerra, BorderLayout.NORTH);
        modelEzkerra = new DefaultListModel<>();
        listEzkerra = sortuJokalariLista(modelEzkerra);
        pnlEzkerra.add(new JScrollPane(listEzkerra), BorderLayout.CENTER);
        
        // BOTOIAK
        JPanel pnlBotoiak = new JPanel(new GridBagLayout());
        pnlBotoiak.setOpaque(false);
        btnMugituEskuinera = sortuBotoia("Hona Mugitu  >>>", new Color(70, 130, 180));
        btnMugituEzkerrera = sortuBotoia("<<<  Hona Mugitu", new Color(70, 130, 180));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(10, 10, 10, 10); gbc.fill = GridBagConstraints.HORIZONTAL;
        pnlBotoiak.add(btnMugituEskuinera, gbc);
        gbc.gridy = 1;
        pnlBotoiak.add(btnMugituEzkerrera, gbc);

        // ESKUMA
        JPanel pnlEskuma = new JPanel(new BorderLayout(0, 10));
        pnlEskuma.setOpaque(false);
        comboEskuma = sortuTaldeCombo(); // Usa taldeAktiboak
        pnlEskuma.add(comboEskuma, BorderLayout.NORTH);
        modelEskuma = new DefaultListModel<>();
        listEskuma = sortuJokalariLista(modelEskuma);
        pnlEskuma.add(new JScrollPane(listEskuma), BorderLayout.CENTER);

        panelNagusia.add(pnlEzkerra);
        panelNagusia.add(pnlBotoiak);
        panelNagusia.add(pnlEskuma);
        
        add(panelNagusia, BorderLayout.CENTER);

        // LISTENERS
        comboEzkerra.addActionListener(e -> { eguneratuLista(comboEzkerra, modelEzkerra); botoiakEguneratu(); });
        comboEskuma.addActionListener(e -> { eguneratuLista(comboEskuma, modelEskuma); botoiakEguneratu(); });
        btnMugituEskuinera.addActionListener(e -> mugituJokalaria(true));
        btnMugituEzkerrera.addActionListener(e -> mugituJokalaria(false));

        if (comboEzkerra.getItemCount() > 0) comboEzkerra.setSelectedIndex(0);
        if (comboEskuma.getItemCount() > 1) comboEskuma.setSelectedIndex(1);
    }

    // --- METODO LAGUNTZAILEAK ---
    private JComboBox<Talde> sortuTaldeCombo() {
        JComboBox<Talde> combo = new JComboBox<>();
        for (Talde t : taldeAktiboak) {
            combo.addItem(t);
        }
        combo.setPreferredSize(new Dimension(200, 40));
        return combo;
    }

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

    private JButton sortuBotoia(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 40));
        return btn;
    }

    private void eguneratuLista(JComboBox<Talde> combo, DefaultListModel<Jokalari> model) {
        model.clear();
        Talde t = (Talde) combo.getSelectedItem();
        if (t != null && t.getJokalariak() != null) {
            for (Jokalari j : t.getJokalariak()) model.addElement(j);
        }
    }

    private void botoiakEguneratu() {
        Talde t1 = (Talde) comboEzkerra.getSelectedItem();
        Talde t2 = (Talde) comboEskuma.getSelectedItem();
        boolean berdinak = (t1 != null && t2 != null && t1.equals(t2));
        btnMugituEskuinera.setEnabled(!berdinak);
        btnMugituEzkerrera.setEnabled(!berdinak);
    }

    // --- LOGIKA NAGUSIA: "ISPILU EFEKTUA" ---
    private void mugituJokalaria(boolean eskuinera) {
        JList<Jokalari> jatorrizkoLista = eskuinera ? listEzkerra : listEskuma;
        DefaultListModel<Jokalari> jatorrizkoModel = eskuinera ? modelEzkerra : modelEskuma;
        DefaultListModel<Jokalari> helburuModel = eskuinera ? modelEskuma : modelEzkerra;
        
        Talde jatorrizkoTaldea = (Talde) (eskuinera ? comboEzkerra.getSelectedItem() : comboEskuma.getSelectedItem());
        Talde helburuTaldea = (Talde) (eskuinera ? comboEskuma.getSelectedItem() : comboEzkerra.getSelectedItem());
        Jokalari hautatua = jatorrizkoLista.getSelectedValue();

        if (hautatua == null || jatorrizkoTaldea == null || helburuTaldea == null) return;

        // 1. MUGIMENDUA ORAIN 
        jatorrizkoTaldea.getJokalariak().remove(hautatua);
        helburuTaldea.sartuJokalaria(hautatua);

        // UI eguneratu
        jatorrizkoModel.removeElement(hautatua);
        helburuModel.addElement(hautatua);

        // 2. MUGIMENDUA ETORKIZUNERAKO 
        aplikatuAldaketaFederazioan(jatorrizkoTaldea, helburuTaldea, hautatua);

        // 3. --- DATU-BASEAN EGUNERATU --- 
        boolean ondoGordeta = dao.JokalariDAO.aldatuJokalariarenTaldeaDB(hautatua.getId(), helburuTaldea.getId());
        
        if (ondoGordeta) {
            if (app != null) app.setAldaketakDauden(true);
            utils.LogKudeatzailea.gehituLog("DB EGUNERAKETA: " + hautatua.getIzena() + " " + hautatua.getAbizena() + " - " + hautatua.getDortsala() + " jokalariaren taldea aldatu da.");
        } else {
            // Datu-baseak huts egiten badu, abisua eman
            JOptionPane.showMessageDialog(this, 
                "Errorea egon da jokalaria datu-basean eguneratzean.", 
                "Errorea DBan", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo honek aldaketa bera bilatzen du Federazioaren zerrenda nagusian
    private void aplikatuAldaketaFederazioan(Talde tOrig, Talde tDest, Jokalari jok) {
        // 1. Bilatu benetako taldeak Federazioan (Izena erabiliz)
        Talde masterOrig = null;
        Talde masterDest = null;
        
        for(Talde t : federazioa.getTaldeGuztiak()) {
            if(t.getIzena().equals(tOrig.getIzena())) masterOrig = t;
            if(t.getIzena().equals(tDest.getIzena())) masterDest = t;
        }

        // 2. Taldeak aurkitu badira, bilatu jokalaria
        if(masterOrig != null && masterDest != null) {
            Jokalari masterJok = null;
            
            // Jokalaria bilatu behar dugu izenaren eta abizenaren bidez (objektu ezberdinak direlako)
            for(Jokalari j : masterOrig.getJokalariak()) {
                if(j.getIzena().equals(jok.getIzena()) && j.getAbizena().equals(jok.getAbizena())) {
                    masterJok = j;
                    break;
                }
            }

            // 3. Mugimendua egin zerrenda nagusian
            if(masterJok != null) {
            	// 1. Ezabatu jatorrizkotik
                masterOrig.getJokalariak().remove(masterJok);
                // 2. Gehitu berrira
                masterDest.getJokalariak().add(masterJok);

                // --- LOG ---
                String logMezua = "FITXAKETA: " + masterJok.getIzena() + " " + masterJok.getAbizena() + 
                                  " mugitu da (" + masterOrig.getIzena() + " -> " + masterDest.getIzena() + ")";
                utils.LogKudeatzailea.gehituLog(logMezua);
                // -----------
            }
        }
    }
}