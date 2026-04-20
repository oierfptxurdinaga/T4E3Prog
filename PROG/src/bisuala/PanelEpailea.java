package bisuala;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.PartiduaDAO;
import model.Denboraldia;
import model.ErabiltzaileEpaile;
import model.Jardunaldi;
import model.Partidua;
import model.Talde;

/**
 * Epailearen panela.
 * Jardunaldi bat aukeratuta, jokatutako partiduen emaitzak sartu edo ikusteko aukera ematen du.
 * Denboraldia editagarria ez bada, eremu guztiak desgaituta agertzen dira.
 */
public class PanelEpailea extends JPanel {

	/** Aplikazioaren leiho nagusia. */
	private APP app;

	private static final long serialVersionUID = 1L;

	/** Fondoaren kolorea. */
	private final Color KOLORE_FONDOA = Color.WHITE;

	/** Ertzaren kolorea. */
	private final Color KOLORE_BORDER = new Color(220, 220, 220);

	/** Jardunaldiak aukeratzeko combo-box-a. */
	private JComboBox<String> comboJardunaldiak;

	/** Partiduak erakusten dituen panela. */
	private JPanel panelPartiduak;

	/** Emaitzak sartu nahi diren denboraldia. */
	private Denboraldia denboraldia;

	/** Emaitzak sartzen dituen epailea. */
	private ErabiltzaileEpaile epaileAktiboa;

	/** Denboraldia editatu daitekeen ala ez adierazten du. */
	private boolean editagarria;

	/**
	 * Epailearen panela sortzen du.
	 *
	 * @param d           emaitzak sartu nahi diren denboraldia
	 * @param epaile      epaile erabiltzailea
	 * @param editagarria true bada emaitzak sartu daitezke; false bada ikusi bakarrik
	 * @param app         aplikazioaren leiho nagusia
	 */
	public PanelEpailea(Denboraldia d, ErabiltzaileEpaile epaile, boolean editagarria, APP app) {
		this.denboraldia = d;
		this.epaileAktiboa = epaile;
		this.editagarria = editagarria;
		this.app = app;

		setLayout(new BorderLayout());
		setBackground(KOLORE_FONDOA);

		JPanel panelGoikoa = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelGoikoa.setBackground(KOLORE_FONDOA);
		panelGoikoa.setBorder(new EmptyBorder(15, 15, 5, 15));

		String izenburua = editagarria ? "Sartu Emaitzak - Jardunaldia: " : "Emaitzak Ikusi (ITXITA) - Jardunaldia: ";
		JLabel lblIzenburua = new JLabel(izenburua);
		lblIzenburua.setFont(new Font("Arial", Font.BOLD, 14));
		if (!editagarria) {
			lblIzenburua.setForeground(Color.GRAY);
		}

		panelGoikoa.add(lblIzenburua);

		comboJardunaldiak = new JComboBox<>();
		comboJardunaldiak.setPreferredSize(new Dimension(200, 30));
		comboJardunaldiak.addActionListener(e -> eguneratuPartiduak());

		panelGoikoa.add(comboJardunaldiak);
		add(panelGoikoa, BorderLayout.NORTH);

		panelPartiduak = new JPanel();
		panelPartiduak.setLayout(new BoxLayout(panelPartiduak, BoxLayout.Y_AXIS));
		panelPartiduak.setBackground(KOLORE_FONDOA);
		panelPartiduak.setBorder(new EmptyBorder(10, 10, 10, 10));

		JScrollPane scroll = new JScrollPane(panelPartiduak);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(20);
		add(scroll, BorderLayout.CENTER);

		datuakKargatu();
	}

	/**
	 * Combo-box-a betetzeko jardunaldiak kargatzen ditu eta lehenengoa aukeratzen du.
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
			JLabel lblHutsik = new JLabel("Ez dago jardunaldirik kargatuta.");
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
	                panelPartiduak.add(sortuPartiduEditagarria(p, jardunaldia));
	                panelPartiduak.add(Box.createRigidArea(new Dimension(0, 10)));
	            }
			}
		}
		panelPartiduak.revalidate();
		panelPartiduak.repaint();
	}

	/**
	 * Partida bat erakusteko panel bat sortzen du.
	 * Editagarria bada, emaitzak sartzeko eremuekin; bestela, irakurtzeko soilik.
	 *
	 * @param p          erakutsi nahi den partida
	 * @param jardunaldia partida dagoen jardunaldia
	 * @return sortutako partida-panela
	 */
	private JPanel sortuPartiduEditagarria(Partidua p, Jardunaldi jardunaldia) {
        JPanel panelErrenkada = new JPanel(new BorderLayout(10, 0));
        panelErrenkada.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(KOLORE_BORDER, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        panelErrenkada.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        panelErrenkada.setPreferredSize(new Dimension(600, 80));
        panelErrenkada.setBackground(Color.WHITE);

        JPanel pnlEtxekoa = sortuTaldePanela(p.getEtxekoTaldea(), SwingConstants.RIGHT);
        JPanel pnlKanpokoa = sortuTaldePanela(p.getKanpokoTaldea(), SwingConstants.LEFT);

        JPanel pnlEmaitzak = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        pnlEmaitzak.setBackground(Color.WHITE);
        pnlEmaitzak.setPreferredSize(new Dimension(220, 0));

        JTextField txtEtxekoa = new JTextField(2);
        txtEtxekoa.setHorizontalAlignment(SwingConstants.CENTER);
        txtEtxekoa.setFont(new Font("Arial", Font.BOLD, 18));

        JTextField txtKanpokoa = new JTextField(2);
        txtKanpokoa.setHorizontalAlignment(SwingConstants.CENTER);
        txtKanpokoa.setFont(new Font("Arial", Font.BOLD, 18));

        txtEtxekoa.setEditable(this.editagarria);
        txtKanpokoa.setEditable(this.editagarria);

        if (!this.editagarria) {
            txtEtxekoa.setBackground(new Color(245, 245, 245));
            txtKanpokoa.setBackground(new Color(245, 245, 245));
        }

        JLabel lblGidoia = new JLabel("-");
        lblGidoia.setFont(new Font("Arial", Font.BOLD, 18));

        if (p.jokatutaDago()) {
            txtEtxekoa.setText(String.valueOf(p.getEtxekoGolak()));
            txtKanpokoa.setText(String.valueOf(p.getKanpokoGolak()));
        }

        JButton btnGorde = new JButton("Gorde");
        btnGorde.setBackground(new Color(70, 130, 180));
        btnGorde.setForeground(Color.WHITE);
        btnGorde.setFocusPainted(false);
        btnGorde.setEnabled(this.editagarria);
        if (!this.editagarria) {
            btnGorde.setText("Itxita");
            btnGorde.setBackground(Color.GRAY);
        }

        btnGorde.addActionListener(e -> {
            try {
                String strEtxekoa = txtEtxekoa.getText().trim();
                String strKanpokoa = txtKanpokoa.getText().trim();

                if (strEtxekoa.isEmpty() || strKanpokoa.isEmpty()) return;

                int golEtxekoa = Integer.parseInt(strEtxekoa);
                int golKanpokoa = Integer.parseInt(strKanpokoa);

                if (golEtxekoa < 0 || golKanpokoa < 0) {
                    JOptionPane.showMessageDialog(this, "Emaitzak ezin dira negatiboak izan.");
                    return;
                }

                if (epaileAktiboa != null) {
                    epaileAktiboa.sartuEmaitza(denboraldia, p.getEtxekoTaldea(), p.getKanpokoTaldea(), golEtxekoa, golKanpokoa);

                 // PON ESTO EN SU LUGAR:
                    boolean ondoGordeta = false;
                    try (java.sql.Connection conn = db.DBConnection.obtenerConexion()) {
                        PartiduaDAO partiduaDAO = new PartiduaDAO(conn);
                        ondoGordeta = partiduaDAO.eguneratuEmaitzaDB(
                                jardunaldia.getId(),
                                p.getEtxekoTaldea().getId(),
                                p.getKanpokoTaldea().getId(),
                                golEtxekoa,
                                golKanpokoa
                        );
                         
                    } catch (java.sql.SQLException ex) {
                        ex.printStackTrace();
                    }

                    if (ondoGordeta) {
                        utils.LogKudeatzailea.gehituLog("Emaitza Eguneratua: " +
                                p.getEtxekoTaldea().getIzena() + " " + golEtxekoa + " - " +
                                golKanpokoa + " " + p.getKanpokoTaldea().getIzena());
                        btnGorde.setBackground(new Color(46, 139, 87));
                        btnGorde.setText("OK");
                    } else {
                        JOptionPane.showMessageDialog(this, "Errorea datu-basean gordetzean.", "Errorea", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Zenbakiak bakarrik sartu.", "Errorea", JOptionPane.ERROR_MESSAGE);
            }
        });

        pnlEmaitzak.add(txtEtxekoa);
        pnlEmaitzak.add(lblGidoia);
        pnlEmaitzak.add(txtKanpokoa);
        pnlEmaitzak.add(Box.createHorizontalStrut(10));
        pnlEmaitzak.add(btnGorde);

        panelErrenkada.add(pnlEtxekoa, BorderLayout.WEST);
        panelErrenkada.add(pnlEmaitzak, BorderLayout.CENTER);
        panelErrenkada.add(pnlKanpokoa, BorderLayout.EAST);

        return panelErrenkada;
    }

	/**
	 * Talde baten izena eta ezkutua erakusten dituen panel bat sortzen du.
	 *
	 * @param t          erakutsi nahi den taldea
	 * @param alineazioa SwingConstants.RIGHT edo LEFT
	 * @return sortutako talde-panela
	 */
	private JPanel sortuTaldePanela(Talde t, int alineazioa) {
        JPanel p = new JPanel(new BorderLayout(10, 0));
        p.setBackground(Color.WHITE);
        p.setPreferredSize(new Dimension(220, 50));

        JLabel lblIzena = new JLabel(t.getIzena());
        lblIzena.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel lblEzkutua = new JLabel();
        if (t.getEzkutua() != null) {
            URL url = getClass().getResource("/images/TaldeArmarria"+t.getEzkutua());
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
                lblEzkutua.setIcon(new ImageIcon(img));
            }
        }

        if (alineazioa == SwingConstants.RIGHT) {
            lblIzena.setHorizontalAlignment(SwingConstants.RIGHT);
            p.add(lblIzena, BorderLayout.CENTER);
            p.add(lblEzkutua, BorderLayout.EAST);
        } else {
            lblIzena.setHorizontalAlignment(SwingConstants.LEFT);
            p.add(lblEzkutua, BorderLayout.WEST);
            p.add(lblIzena, BorderLayout.CENTER);
        }

        return p;
    }
}
