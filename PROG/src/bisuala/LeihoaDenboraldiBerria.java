package bisuala;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.DenboraldiDAO;
import model.DenboraldiTalde;
import model.Denboraldia;
import model.Federazioa;
import model.Talde;
import utils.PartiduKudeatzailea;

/**
 * Denboraldi berri bat konfiguratzeko elkarrizketa-leihoa.
 * Erabiltzaileak urtea eta parte hartuko duten 6 taldeak aukeratzen ditu.
 * Denboraldia sortzean egutegia automatikoki kalkulatzen da.
 */
public class LeihoaDenboraldiBerria extends JDialog {
	private static final long serialVersionUID = 1L;

	/** Denboraldiaren urtea sartzeko eremua. */
	private JTextField txtUrtea;

	/** Federazioaren datuak. */
	private Federazioa federazioa;

	/** Taldeen checkbox zerrenda. */
	private ArrayList<JCheckBox> checkTaldeak;

	/** Aukeratutako talde kopurua erakusten duen etiketa. */
	private JLabel lblKontagailua;

	/** Denboraldian parte har dezaketen talde kopuru maximoa. */
	private final int MAX_TALDEAK = 6;

	/** Denboraldia ondo sortu den ala ez adierazten du. */
	private boolean ondoSortuDa = false;


	/**
	 * Denboraldi berria konfiguratzeko leihoa sortzen du.
	 *
	 * @param federazioa federazioaren datuak
	 */
    public LeihoaDenboraldiBerria(Federazioa federazioa) {
        this.federazioa = federazioa;
        this.checkTaldeak = new ArrayList<>();

        setTitle("Denboraldi Berria Konfiguratu");
        setModal(true);
        setBounds(100, 100, 500, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Denboraldia azkena = null;
        ArrayList<Denboraldia> zerrenda = federazioa.getDenboraldiak();
        if (zerrenda != null && !zerrenda.isEmpty()) {
            azkena = zerrenda.get(zerrenda.size() - 1);
        }
        int hurrengoUrtea = (azkena != null) ? azkena.getUrtea() + 1 : 2024;

        JPanel pnlUrtea = new JPanel();
        pnlUrtea.add(new JLabel("Denboraldiaren Urtea:"));
        txtUrtea = new JTextField(String.valueOf(hurrengoUrtea), 10);
        if (azkena != null) {
            txtUrtea.setEditable(false);
            txtUrtea.setBackground(Color.WHITE);
        }
        pnlUrtea.add(txtUrtea);
        add(pnlUrtea, BorderLayout.NORTH);

        JPanel pnlLista = new JPanel();
        pnlLista.setLayout(new BoxLayout(pnlLista, BoxLayout.Y_AXIS));
        pnlLista.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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
                if (count < MAX_TALDEAK) {
                    chk.setSelected(true);
                    count++;
                }
                chk.putClientProperty("taldeObj", t);
                chk.addItemListener(e -> eguneratuCheckak());
                pnlLista.add(chk);
                checkTaldeak.add(chk);
            }
        }
        add(new JScrollPane(pnlLista), BorderLayout.CENTER);

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

        eguneratuCheckak();
    }

    /**
     * Denboraldia ondo sortu den ala ez itzultzen du.
     *
     * @return true ondo sortu bada
     */
    public boolean isOndoSortuDa() {
        return ondoSortuDa;
    }

    /**
     * Checkbox-en egoera denbora errealean kudeatzen du.
     * 6 talde aukeratuta daudenean, gainerakoak blokeatzen ditu.
     */
    private void eguneratuCheckak() {
        int aukeratuak = 0;

        for (JCheckBox chk : checkTaldeak) {
            if (chk.isSelected()) {
                aukeratuak++;
            }
        }

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

    /**
     * Aukeratutako datuekin denboraldia sortzen du eta datu-basean gordetzen du.
     * 6 talde aukeratu ez badira, errore bat erakusten da.
     */
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

            boolean ondo = false;
            
            try (java.sql.Connection conn = db.DBConnection.obtenerConexion()) {
                DenboraldiDAO ddao = new DenboraldiDAO(conn);
                ondo = ddao.txertatuDenboraldiaOsoa(d);
                
            } catch (java.sql.SQLException sqlEx) {
                sqlEx.printStackTrace();
            }

            if (ondo) {
                federazioa.gehituDenboraldia(d);
                utils.LogKudeatzailea.gehituLog("Denboraldi berria sortu da: " + urtea + " (" + taldeAukeratuak.size() + " talde)");
                this.ondoSortuDa = true;
                JOptionPane.showMessageDialog(this, "Denboraldia (" + urtea + ") ondo sortu da!");
                dispose();
            } else {
                utils.LogKudeatzailea.gehituLog("Denboraldi " + urtea + " sortzerakoan errore bat egon da datu-basean.");
                JOptionPane.showMessageDialog(this, "Zerbait txarto joan da datu-basean gordetzean.", "Errorea", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Errorea: " + ex.getMessage());
        }
    }
}
