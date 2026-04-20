package bisuala;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dao.ErabiltzaileDAO;
import model.Erabiltzaile;
import model.ErabiltzaileAdministraria;
import model.ErabiltzaileEpaile;
import model.ErabiltzailePresi;
import model.Federazioa;

/**
 * Erabiltzaile berri bat sortzeko elkarrizketa-leihoa.
 * Izena, pasahitza eta mota (Administraria, Epailea, Presidentea) sartzeko aukera ematen du.
 */
public class LeihoaErabiltzaileBerria extends JDialog {

    private static final long serialVersionUID = 1L;

    /** Erabiltzaile-izena sartzeko eremua. */
    private JTextField txtUser;

    /** Pasahitza sartzeko eremua. */
    private JPasswordField txtPass;

    /** Erabiltzaile mota aukeratzeko combo-box-a. */
    private JComboBox<String> cbMota;

    /** Federazioaren datuak, erabiltzaileak egiaztatzeko. */
    private Federazioa federazioa;

    /** Aplikazioaren leiho nagusia, aldaketak markatzeko. */
    private APP app;

    /** Datu-basera sarbidea. */
    private ErabiltzaileDAO edao;

    /** Sortutako erabiltzailea, kanpotik eskuratzeko. */
    private Erabiltzaile sortutakoa = null;

    /**
     * Erabiltzaile berria sortzeko leihoa sortzen du.
     *
     * @param parent     leihoa sortu duen frame nagusia
     * @param federazioa federazioaren datuak
     * @param app        aplikazioaren leiho nagusia
     */
    public LeihoaErabiltzaileBerria(JFrame parent, Federazioa federazioa, APP app) {
        super(parent, "Erabiltzaile Berria", true);
        this.federazioa = federazioa;
        this.app = app;

        setBounds(100, 100, 400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 20));
        pnlForm.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlForm.setBackground(Color.WHITE);

        pnlForm.add(new JLabel("Erabiltzaile Izena:"));
        txtUser = new JTextField();
        pnlForm.add(txtUser);

        pnlForm.add(new JLabel("Pasahitza:"));
        txtPass = new JPasswordField();
        pnlForm.add(txtPass);

        pnlForm.add(new JLabel("Erabiltzaile Mota:"));
        String[] motak = { "Administraria", "Epailea", "Presidentea" };
        cbMota = new JComboBox<>(motak);
        pnlForm.add(cbMota);

        add(pnlForm, BorderLayout.CENTER);

        JPanel pnlBotoiak = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnUtzi = new JButton("Utzi");
        JButton btnGorde = new JButton("Gorde");

        btnGorde.setBackground(new Color(46, 139, 87));
        btnGorde.setForeground(Color.WHITE);

        btnUtzi.addActionListener(e -> dispose());
        btnGorde.addActionListener(e -> gordeErabiltzailea());

        pnlBotoiak.add(btnUtzi);
        pnlBotoiak.add(btnGorde);
        add(pnlBotoiak, BorderLayout.SOUTH);
        this.getRootPane().setDefaultButton(btnGorde);
    }

    /**
     * Formularioko datuak baliozkotu eta erabiltzaile berria gordetzen du.
     * Eremu hutsik badago edo erabiltzailea jada existitzen bada, errore bat erakusten da.
     */
    private void gordeErabiltzailea() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        String mota = (String) cbMota.getSelectedItem();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Eremu guztiak bete behar dira.", "Errorea", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (Erabiltzaile e : federazioa.getErabiltzaileak()) {
            if (e.getErabiltzaile().equalsIgnoreCase(user)) {
                JOptionPane.showMessageDialog(this, "Erabiltzaile hori existitzen da jada.", "Errorea", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Erabiltzaile berria;
        if (mota.equals("Administraria")) {
            berria = new ErabiltzaileAdministraria(user, pass);
        } else if (mota.equals("Epailea")) {
            berria = new ErabiltzaileEpaile(user, pass);
        } else {
            berria = new ErabiltzailePresi(user, pass);
        }

        edao = new ErabiltzaileDAO();
        edao.gordeErabiltzaileaODB(berria);
        utils.LogKudeatzailea.gehituLog("Erabiltzaile berria sortu da: " + user + " [" + mota + "]");
        JOptionPane.showMessageDialog(this, "Erabiltzailea ondo sortu da!");
        this.sortutakoa = berria;



        dispose();
    }

    /**
     * Sortutako erabiltzailea itzultzen du, edo null sortu ez bada.
     *
     * @return sortutako erabiltzailea
     */
    public Erabiltzaile getErabiltzaileBerria() {
        return this.sortutakoa;
    }
}
