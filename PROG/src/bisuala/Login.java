package bisuala;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import model.Erabiltzaile;
import model.Federazioa;
import utils.DatuKarga;

/**
 * Saioa hasteko leihoa.
 * Erabiltzaile-izena eta pasahitza egiaztatu ondoren APP leiho nagusia irekitzen du.
 * Aplikazioaren sarrera puntua da.
 */
public class Login extends JFrame {
	private static final long serialVersionUID = 1L;

	/** Erabiltzaile-izena sartzeko eremua. */
	private JTextField txtUser;

	/** Pasahitza sartzeko eremua. */
	private JPasswordField txtPass;

	/** Federazioaren datuak datu-basekoak kargatzen dira. */
	private Federazioa federazioa;

	/**
	 * Aplikazioaren sarrera puntua.
	 *
	 * @param args komando-lerroko argumentuak (ez dira erabiltzen)
	 */
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

	/**
	 * Login leihoa sortzen du eta federazioaren datuak kargatzen ditu.
	 * Federazioa null bada, hutsik sortzen da.
	 */
	public Login() {
		federazioa = DatuKarga.kargatuFederazioaDB();

		if (federazioa == null) {
			federazioa = new Federazioa();
		}

		setTitle("Saioa Hasi");
		setLayout(null);
		setBounds(100, 100, 400, 300);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

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

		btnLogin.addActionListener(e -> {
			String u = txtUser.getText();
			String p = new String(txtPass.getPassword());

			Erabiltzaile userLogueado = utils.BDOOKudeatzailea.login(u, p);

			if (userLogueado != null) {
				utils.LogKudeatzailea.gehituLog("Saioa hasi da: " + userLogueado.getErabiltzaile());
				new APP(userLogueado, federazioa).setVisible(true);
				dispose();
			} else {
				JOptionPane.showMessageDialog(null, "Datu okerrak, saiatu berriro.", "Errorea",
						JOptionPane.ERROR_MESSAGE);
			}
		});
	}
}
