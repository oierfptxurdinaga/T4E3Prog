package bisuala;

import javax.swing.*;
import java.awt.*; 
import java.util.ArrayList;
import model.*;
import utils.DatuKarga; 

public class Login extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtUser;
    private JPasswordField txtPass;
    
    private Federazioa federazioa; 

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Login() {
        // 1. Datuak kargatu (SOILIK FEDERAZIOA)
        // DatuKarga.kargatuErabiltzaileak() ez dugu gehiago behar
        federazioa = DatuKarga.kargatuFederazioa(); 

        // Federazioa null bada (fitxategia ez da existitzen), sortu berria
        if (federazioa == null) {
            federazioa = new Federazioa();
        }

        // 2. Egiaztatu erabiltzaileak dauden, bestela Admin sortu
        datuakHasieratuBeharBada();

        // 3. Leihoaren konfigurazioa
        setTitle("Saioa Hasi");
        setLayout(null);
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- UI OSAGAIAK ---
        JLabel lblUser = new JLabel("Erabiltzailea:");
        lblUser.setBounds(50, 50, 100, 25);
        add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(150, 50, 150, 25);
        add(txtUser);

        JLabel lblPass = new JLabel("Pasahitza:");
        lblPass.setBounds(50, 100, 100, 25);
        add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(150, 100, 150, 25);
        add(txtPass);

        JButton btnLogin = new JButton("Sartu");
        btnLogin.setBounds(150, 160, 100, 30);
        btnLogin.setBackground(new Color(70, 130, 180));
        btnLogin.setForeground(Color.WHITE);
        add(btnLogin);
        this.getRootPane().setDefaultButton(btnLogin);

        // --- LOGIKA ---
        btnLogin.addActionListener(e -> {
        	String u = txtUser.getText();
        	String p = new String(txtPass.getPassword());

        	Erabiltzaile userLogueado = utils.BDOOKudeatzailea.login(u, p);

        	if (userLogueado != null) {
        	    utils.LogKudeatzailea.gehituLog("Saioa hasi da: " + userLogueado.getErabiltzaile());
        	    new APP(userLogueado, federazioa).setVisible(true);
        	    dispose();
        	} else {
        	    JOptionPane.showMessageDialog(null, "Datu okerrak, saiatu berriro.", "Errorea", JOptionPane.ERROR_MESSAGE);
        	}
        });
    }

    private void datuakHasieratuBeharBada() {
        // Galdera orain Federazioari egiten diogu
        if (federazioa.getErabiltzaileak().isEmpty()) {
            
            ErabiltzailePresi admin = new ErabiltzailePresi("presi", "presi");
            
            // Federazioan gorde
            federazioa.getErabiltzaileak().add(admin);
           
            DatuKarga.gordeFederazioa(federazioa);
            
            System.out.println("Admin lehenetsia sortu da (presi/presi).");
        }
    }
}