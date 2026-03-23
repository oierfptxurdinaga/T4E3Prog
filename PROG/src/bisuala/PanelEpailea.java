package bisuala;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import model.*;

import bisuala.APP;
import dao.PartiduaDAO;

public class PanelEpailea extends JPanel {

	private APP app;

	private static final long serialVersionUID = 1L;

	private final Color KOLORE_FONDOA = Color.WHITE;
	private final Color KOLORE_BORDER = new Color(220, 220, 220);

	private JComboBox<String> comboJardunaldiak;
	private JPanel panelPartiduak;

	private Denboraldia denboraldia;
	private ErabiltzaileEpaile epaileAktiboa;

	// 1. VARIABLE BERRIA: Denboraldia editatu daitekeen ala ez
	private boolean editagarria;

	// 2. ERAIKITZAILEA ALDATU (boolean editagarria gehitu)
	public PanelEpailea(Denboraldia d, ErabiltzaileEpaile epaile, boolean editagarria, APP app) {
		this.denboraldia = d;
		this.epaileAktiboa = epaile;
		this.editagarria = editagarria;
		this.app = app;

		setLayout(new BorderLayout());
		setBackground(KOLORE_FONDOA);

		// --- GOIKO ALDEA ---
		JPanel panelGoikoa = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panelGoikoa.setBackground(KOLORE_FONDOA);
		panelGoikoa.setBorder(new EmptyBorder(15, 15, 5, 15));

		// Testua aldatu egoeraren arabera
		String izenburua = editagarria ? "Sartu Emaitzak - Jardunaldia: " : "Emaitzak Ikusi (ITXITA) - Jardunaldia: ";
		JLabel lblIzenburua = new JLabel(izenburua);
		lblIzenburua.setFont(new Font("Arial", Font.BOLD, 14));
		if (!editagarria)
			lblIzenburua.setForeground(Color.GRAY);

		panelGoikoa.add(lblIzenburua);

		comboJardunaldiak = new JComboBox<>();
		comboJardunaldiak.setPreferredSize(new Dimension(200, 30));
		comboJardunaldiak.addActionListener(e -> eguneratuPartiduak());

		panelGoikoa.add(comboJardunaldiak);
		add(panelGoikoa, BorderLayout.NORTH);

		// --- ERDIKO ALDEA ---
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
	 * Datuak kargatu, etc. (Metodo hauek berdin jarraitzen dute, kopiatu aurreko
	 * kodea hemen, ez dago aldaketarik datuakKargatu edo eguneratuPartiduak-en)
	 */
	private void datuakKargatu() {
		// Listener-ak desgaitu kargatzean
		ActionListener[] listeners = comboJardunaldiak.getActionListeners();
		for (ActionListener al : listeners)
			comboJardunaldiak.removeActionListener(al);

		comboJardunaldiak.removeAllItems();
		panelPartiduak.removeAll();

		if (denboraldia != null && denboraldia.getLigakoJardunaldi() != null) {
			for (Jardunaldi j : denboraldia.getLigakoJardunaldi()) {
				comboJardunaldiak.addItem("Jardunaldia " + j.getJardunaldiZbk());
			}
		}

		// Listener-ak berriro jarri
		for (ActionListener al : listeners)
			comboJardunaldiak.addActionListener(al);

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

	// --- ALDAKETA NAGUSIA HEMEN DAGO ---
	private JPanel sortuPartiduEditagarria(Partidua p, Jardunaldi jardunaldia) { 
        JPanel panelErrenkada = new JPanel(new BorderLayout(10, 0));
        panelErrenkada.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(KOLORE_BORDER, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        panelErrenkada.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); 
        panelErrenkada.setPreferredSize(new Dimension(600, 80));
        panelErrenkada.setBackground(Color.WHITE);

        // TALDEAK
        JPanel pnlEtxekoa = sortuTaldePanela(p.getEtxekoTaldea(), SwingConstants.RIGHT);
        JPanel pnlKanpokoa = sortuTaldePanela(p.getKanpokoTaldea(), SwingConstants.LEFT);

        // ERDIKO ZONA
        JPanel pnlEmaitzak = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 15));
        pnlEmaitzak.setBackground(Color.WHITE);
        pnlEmaitzak.setPreferredSize(new Dimension(220, 0));

        JTextField txtEtxekoa = new JTextField(2);
        txtEtxekoa.setHorizontalAlignment(JTextField.CENTER);
        txtEtxekoa.setFont(new Font("Arial", Font.BOLD, 18));
        
        JTextField txtKanpokoa = new JTextField(2);
        txtKanpokoa.setHorizontalAlignment(JTextField.CENTER);
        txtKanpokoa.setFont(new Font("Arial", Font.BOLD, 18));
        
        // 3. LOGIKA: Editagarria ez bada, inputak desgaitu
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
        
        // 4. LOGIKA: Editagarria ez bada, botoia desgaitu
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
                    
          
                    boolean ondoGordeta = PartiduaDAO.eguneratuEmaitzaDB(
                            jardunaldia.getJardunaldiZbk(),
                            p.getEtxekoTaldea().getId(),
                            p.getKanpokoTaldea().getId(),
                            golEtxekoa,
                            golKanpokoa
                    );

                    if (ondoGordeta) {
                        if (app != null) {
                            app.setAldaketakDauden(true); 
                        }
                        
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


	private JPanel sortuTaldePanela(Talde t, int alineazioa) {
        JPanel p = new JPanel(new BorderLayout(10, 0)); 
        p.setBackground(Color.WHITE);
        p.setPreferredSize(new Dimension(220, 50)); 

        JLabel lblIzena = new JLabel(t.getIzena());
        lblIzena.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        
        JLabel lblEzkutua = new JLabel();
        if (t.getEzkutua() != null) {
            URL url = getClass().getResource("/images/"+t.getEzkutua());
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