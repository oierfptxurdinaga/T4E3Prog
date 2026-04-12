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

public class LeihoaErabiltzaileBerria extends JDialog {

    private static final long serialVersionUID = 1L;
    private JTextField txtUser;
    private JPasswordField txtPass;
    private JComboBox<String> cbMota;
    private Federazioa federazioa;
    private APP app;
    private ErabiltzaileDAO edao;

    // Sortutako erabiltzailea hemen gordeko dugu erreferentzia izateko
    private Erabiltzaile sortutakoa = null;

    public LeihoaErabiltzaileBerria(JFrame parent, Federazioa federazioa, APP app) {
        super(parent, "Erabiltzaile Berria", true); // true = MODAL
        this.federazioa = federazioa;
        this.app = app;

        setBounds(100, 100, 400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JPanel pnlForm = new JPanel(new GridLayout(4, 2, 10, 20));
        pnlForm.setBorder(new EmptyBorder(20, 20, 20, 20));
        pnlForm.setBackground(Color.WHITE);

        // 1. IZENA
        pnlForm.add(new JLabel("Erabiltzaile Izena:"));
        txtUser = new JTextField();
        pnlForm.add(txtUser);

        // 2. PASAHITZA
        pnlForm.add(new JLabel("Pasahitza:"));
        txtPass = new JPasswordField();
        pnlForm.add(txtPass);

        // 3. MOTA (Presidentea gehitu dugu zerrendara)
        pnlForm.add(new JLabel("Erabiltzaile Mota:"));
        String[] motak = { "Administraria", "Epailea", "Presidentea" };
        cbMota = new JComboBox<>(motak);
        pnlForm.add(cbMota);

        add(pnlForm, BorderLayout.CENTER);

        // --- BOTOIAK ---
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

    private void gordeErabiltzailea() {
        String user = txtUser.getText().trim();
        String pass = new String(txtPass.getPassword());
        String mota = (String) cbMota.getSelectedItem();

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Eremu guztiak bete behar dira.", "Errorea", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Konprobatu ea existitzen den
        for (Erabiltzaile e : federazioa.getErabiltzaileak()) {
            if (e.getErabiltzaile().equalsIgnoreCase(user)) {
                JOptionPane.showMessageDialog(this, "Erabiltzaile hori existitzen da jada.", "Errorea", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Objektua sortu
        Erabiltzaile berria;
        if (mota.equals("Administraria")) {
            berria = new ErabiltzaileAdministraria(user, pass);
        } else if (mota.equals("Epailea")) {
            berria = new ErabiltzaileEpaile(user, pass);
        } else {
            // Presidentea kasua
            berria = new ErabiltzailePresi(user, pass);
        }

        edao = new ErabiltzaileDAO();
        edao.gordeErabiltzaileaODB(berria);
     // --- LOG ---
        utils.LogKudeatzailea.gehituLog("Erabiltzaile berria sortu da: " + user + " [" + mota + "]");
        // -----------
        JOptionPane.showMessageDialog(this, "Erabiltzailea ondo sortu da!");
        this.sortutakoa = berria; // Erreferentzia gorde

        // 2. Aldaketak markatu APP-an
        if (app != null) {
            app.setAldaketakDauden(true);
        }

        dispose();
    }

    // METODO HAU BEHARREZKOA DA PanelPresi ez kexatzeko
    public Erabiltzaile getErabiltzaileBerria() {
        return this.sortutakoa;
    }
}