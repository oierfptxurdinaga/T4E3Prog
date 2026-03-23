package bisuala;

import javax.swing.*;

import dao.DenboraldiDAO;

import java.awt.*;
import java.util.ArrayList;
import model.*;
import utils.PartiduKudeatzailea;

public class LeihoaDenboraldiBerria extends JDialog {
	private static final long serialVersionUID = 1L;
	private JTextField txtUrtea;
    private Federazioa federazioa;
    private ArrayList<JCheckBox> checkTaldeak;
    private JLabel lblKontagailua; 
    private final int MAX_TALDEAK = 6; 
    private boolean ondoSortuDa = false;
    private DenboraldiDAO ddao;

    public LeihoaDenboraldiBerria(Federazioa federazioa) {
    	ddao = new DenboraldiDAO();
        this.federazioa = federazioa;
        this.checkTaldeak = new ArrayList<>();

        setTitle("Denboraldi Berria Konfiguratu");
        setModal(true);
        setBounds(100, 100, 500, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 0. DATUAK PRESTATU ---
        Denboraldia azkena = null;
        ArrayList<Denboraldia> zerrenda = federazioa.getDenboraldiak();
        if (zerrenda != null && !zerrenda.isEmpty()) {
            azkena = zerrenda.get(zerrenda.size() - 1);
        }
        int hurrengoUrtea = (azkena != null) ? azkena.getUrtea() + 1 : 2024;

        // --- 1. GOIKO ALDEA: URTEA ---
        JPanel pnlUrtea = new JPanel();
        pnlUrtea.add(new JLabel("Denboraldiaren Urtea:"));
        txtUrtea = new JTextField(String.valueOf(hurrengoUrtea), 10);
        if (azkena != null) {
            txtUrtea.setEditable(false);
            txtUrtea.setBackground(Color.WHITE);
        }
        pnlUrtea.add(txtUrtea);
        add(pnlUrtea, BorderLayout.NORTH);

        // --- 2. ERDIKO ALDEA: TALDEAK ---
        JPanel pnlLista = new JPanel();
        pnlLista.setLayout(new BoxLayout(pnlLista, BoxLayout.Y_AXIS));
        pnlLista.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Informazio panela eta kontagailua
        JPanel pnlInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlInfo.add(new JLabel("Aukeratu taldeak (Zehazki 6):"));
        
        lblKontagailua = new JLabel("0 / " + MAX_TALDEAK);
        lblKontagailua.setFont(new Font("Arial", Font.BOLD, 14));
        lblKontagailua.setForeground(Color.BLUE);
        pnlInfo.add(lblKontagailua);
        
        pnlLista.add(pnlInfo);
        pnlLista.add(Box.createRigidArea(new Dimension(0, 5)));

        if (federazioa.getTaldeGuztiak() != null) {
            int count = 0;
            for (Talde t : federazioa.getTaldeGuztiak()) {
                JCheckBox chk = new JCheckBox(t.getIzena() + " (" + t.getHiria() + ")");
                
                // LEHENENGO 6ak BAKARRIK markatu defektuz (bestela errorea emango luke hasieran)
                if (count < MAX_TALDEAK) {
                    chk.setSelected(true);
                    count++;
                }

                chk.putClientProperty("taldeObj", t);
                
                // --- ENTZULEA (LISTENER) GEHITU ---
                // Honek deituko du 'eguneratuCheckak()' CheckBox bakoitza aldatzen denean
                chk.addItemListener(e -> eguneratuCheckak());

                pnlLista.add(chk);
                checkTaldeak.add(chk);
            }
        }
        add(new JScrollPane(pnlLista), BorderLayout.CENTER);

        // --- 3. BEHEKO ALDEA ---
        JPanel pnlBotoiak = new JPanel(new BorderLayout());
        pnlBotoiak.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlBenetakoBotoiak = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSortu = new JButton("Sortu eta Egutegia Kalkulatu");
        JButton btnUtzi = new JButton("Utzi");
        if (azkena != null && !azkena.isAmaituta()) {
            btnSortu.setEnabled(false);
            JLabel lblOharra = new JLabel("<html><center><font color='red'>* Aurreko denboraldia amaitu arte<br>ezin da berria sortu.</font></center></html>");
            lblOharra.setHorizontalAlignment(SwingConstants.CENTER);
            pnlBotoiak.add(lblOharra, BorderLayout.NORTH);
        }

        btnUtzi.addActionListener(e -> dispose());
        btnSortu.addActionListener(e -> sortuDenboraldia());

        pnlBenetakoBotoiak.add(btnSortu);
        pnlBenetakoBotoiak.add(btnUtzi);
        pnlBotoiak.add(pnlBenetakoBotoiak, BorderLayout.CENTER);
        add(pnlBotoiak, BorderLayout.SOUTH);

        // Hasierako egoera eguneratu (blokeoak aplikatzeko)
        eguneratuCheckak();
    }

    // Getter PanelPresi-rentzat
    public boolean isOndoSortuDa() {
        return ondoSortuDa;
    }

    /**
     * Metodo honek denbora errealean kudeatzen du CheckBox-en egoera.
     */
    private void eguneratuCheckak() {
        int aukeratuak = 0;

        // 1. Zenbatu
        for (JCheckBox chk : checkTaldeak) {
            if (chk.isSelected()) {
                aukeratuak++;
            }
        }

        // 2. Etiketa eguneratu
        lblKontagailua.setText(aukeratuak + " / " + MAX_TALDEAK);
        if (aukeratuak == MAX_TALDEAK) {
            lblKontagailua.setForeground(new Color(0, 150, 0));
        } else {
            lblKontagailua.setForeground(Color.RED); 
        }


        boolean mugaIritsia = (aukeratuak >= MAX_TALDEAK);

        for (JCheckBox chk : checkTaldeak) {
            if (!chk.isSelected()) {

                chk.setEnabled(!mugaIritsia);
            }
        }
    }

    private void sortuDenboraldia() {
        try {
            ArrayList<Talde> taldeAukeratuak = new ArrayList<>();

            for (JCheckBox chk : checkTaldeak) {
                if (chk.isSelected()) {
                    Talde jatorrizkoTaldea = (Talde) chk.getClientProperty("taldeObj");
                    taldeAukeratuak.add(jatorrizkoTaldea.kopiatu());
                }
            }

            if (taldeAukeratuak.size() != MAX_TALDEAK) {
                JOptionPane.showMessageDialog(this, "Zehazki 6 talde aukeratu behar dituzu.", "Errorea", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int urtea = Integer.parseInt(txtUrtea.getText().trim());

            Denboraldia d = new Denboraldia(urtea);
            
            ArrayList<DenboraldiTalde> dtAukeratuak = new ArrayList<>();
            for (Talde t : taldeAukeratuak) {
                dtAukeratuak.add(new DenboraldiTalde(t, true));
            }
            d.setLigakoTaldeak(dtAukeratuak);
            
            d.setLigakoJardunaldi(PartiduKudeatzailea.sortuEgutegia(taldeAukeratuak));
            boolean ondo = ddao.txertatuDenboraldiaOsoa(d);
            if (ondo) {
            federazioa.gehituDenboraldia(d);
            utils.LogKudeatzailea.gehituLog("Denboraldi berria sortu da: " + urtea + " (" + taldeAukeratuak.size() + " talde)");
            this.ondoSortuDa = true;
            JOptionPane.showMessageDialog(this, "Denboraldia (" + urtea + ") ondo sortu da!");
            dispose();
            } else {
            	utils.LogKudeatzailea.gehituLog("Denboraldi "+urtea+" sortzerakoan errore bat egon da.");
            	JOptionPane.showMessageDialog(this, "Zerbait txarto joan da.", "Errorea", JOptionPane.WARNING_MESSAGE);
            }
            


        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errorea: " + ex.getMessage());
        }
    }
}