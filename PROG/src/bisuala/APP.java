package bisuala;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import model.*;
import utils.DatuKarga;

public class APP extends JFrame {
	private static final long serialVersionUID = 1L;

	private JComboBox<Denboraldia> cbDenboraldiak;
	private JTabbedPane tabs;

	private Erabiltzaile erabAktiboa;
	private Federazioa federazioa;
	private boolean aldaketakDauden = false;

	public APP(Erabiltzaile erab, Federazioa federazioa) {
		this.erabAktiboa = erab;
		this.federazioa = federazioa;
		ArrayList<Denboraldia> denboraldiak = federazioa.getDenboraldiak();

		setTitle("FNS Kudeaketa - " + erab.getErabiltzaile());
		setBounds(100, 100, 950, 700);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		// Leihoa ixteko konfirmazioa
		addWindowListener(new ItziKonfirmazioa(this, federazioa));

		// --- GOIKO MENUA ---
		JPanel pnlGoikoa = new JPanel(new BorderLayout());
		pnlGoikoa.setBackground(new Color(230, 230, 230));

		JPanel pnlEzkerra = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlEzkerra.setOpaque(false);
		JLabel labelDenboraldia = new JLabel("Denboraldia:");
		pnlEzkerra.add(labelDenboraldia);

		cbDenboraldiak = new JComboBox<>();
		if (denboraldiak != null) {
			for (Denboraldia d : denboraldiak) {
				cbDenboraldiak.addItem(d);
			}
			// Azken denboraldia aukeratu defektuz
			if (!denboraldiak.isEmpty()) {
				cbDenboraldiak.setSelectedIndex(denboraldiak.size() - 1);
			}
		}
		pnlEzkerra.add(cbDenboraldiak);

		JPanel pnlEskubia = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnlEskubia.setOpaque(false);
		JButton btnLogout = new JButton("Saioa Itxi");
		btnLogout.setBackground(new Color(135, 21, 33));
		btnLogout.setForeground(Color.WHITE);
		pnlEskubia.add(btnLogout);

		pnlGoikoa.add(pnlEzkerra, BorderLayout.WEST);
		pnlGoikoa.add(pnlEskubia, BorderLayout.EAST);

		// --- TABS (FITXAK) ---
		tabs = new JTabbedPane();

		getContentPane().add(pnlGoikoa, BorderLayout.NORTH);
		getContentPane().add(tabs, BorderLayout.CENTER);

		tabs.addChangeListener(e -> {
			int index = tabs.getSelectedIndex();
			if (index == -1)
				return;

			Component panelAktiboa = tabs.getSelectedComponent();
			String titulua = tabs.getTitleAt(index);

			// Kudeatu ComboBox-aren ikusgarritasuna
			if (panelAktiboa instanceof PanelPresi) {
				labelDenboraldia.setVisible(false);
				cbDenboraldiak.setVisible(false);
			} else {
				labelDenboraldia.setVisible(true);
				cbDenboraldiak.setVisible(true);
			}

			// --- FRESKATZE LOGIKA ---
			Denboraldia aukeratutakoa = (Denboraldia) cbDenboraldiak.getSelectedItem();
			if (aukeratutakoa != null) {

				if (titulua.equals("Sailkapena")) {
					// Sailkapena birkalkulatu
					ArrayList<DenboraldiTalde> sailkapenBerria = aukeratutakoa.getSailkapena();
					PanelSailkapena pBerria = new PanelSailkapena(sailkapenBerria, aukeratutakoa.getUrtea());
					tabs.setComponentAt(index, pBerria);
				} else if (titulua.equals("Taldeak")) {
					// Extraer los Talde
					ArrayList<Talde> taldeakBakarrik = new ArrayList<>();
					if (aukeratutakoa.getLigakoTaldeak() != null) {
						for (DenboraldiTalde dt : aukeratutakoa.getLigakoTaldeak()) {
							taldeakBakarrik.add(dt.getTalde());
						}
					}
					// Taldeak freskatu
					PanelTaldeak pTaldeakBerria = new PanelTaldeak(taldeakBakarrik, aukeratutakoa.getUrtea());
					tabs.setComponentAt(index, pTaldeakBerria);
				}
				// --- HAU DA GEHITU BEHAR DUZUNA ---
				else if (titulua.equals("Jardunaldiak")) {
					PanelJardunaldiak pJardunaldiakBerria = new PanelJardunaldiak(aukeratutakoa);
					tabs.setComponentAt(index, pJardunaldiakBerria);
				}
			}
		});

		// Ekintzak
		cbDenboraldiak.addActionListener(e -> tabsEguneratu());
		btnLogout.addActionListener(e -> kudeatuIrteera(true));

		// Hasieratu
		tabsEguneratu();
	}

	/**
	 * Metodo honek fitxak (tabs) birsortzen ditu denboraldia aldatzean
	 */
	private void tabsEguneratu() {
		int aukeratutakoIndizea = tabs.getSelectedIndex();
		tabs.removeAll();

		Denboraldia aukeratutakoa = (Denboraldia) cbDenboraldiak.getSelectedItem();

		if (aukeratutakoa != null) {
			// SAILKAPENA
			ArrayList<DenboraldiTalde> sailkapena = aukeratutakoa.getSailkapena();
			tabs.addTab("Sailkapena", new PanelSailkapena(sailkapena, aukeratutakoa.getUrtea()));

			// TALDEAK
			ArrayList<Talde> taldeakBakarrik = new ArrayList<>();
			if (aukeratutakoa.getLigakoTaldeak() != null) {
				for (DenboraldiTalde dt : aukeratutakoa.getLigakoTaldeak()) {
					taldeakBakarrik.add(dt.getTalde());
				}
			}
			tabs.addTab("Taldeak", new PanelTaldeak(taldeakBakarrik, aukeratutakoa.getUrtea()));

			// JARDUNALDIAK
			tabs.addTab("Jardunaldiak", new PanelJardunaldiak(aukeratutakoa));

			// PANEL BEREZIAK (Erabiltzailearen arabera)
			if (erabAktiboa instanceof ErabiltzaileAdministraria) {

				ArrayList<Talde> taldeakEditatzeko = new ArrayList<>();
				if (!federazioa.getDenboraldiak().isEmpty()) {
					Denboraldia azkena = federazioa.getDenboraldiak().get(federazioa.getDenboraldiak().size() - 1);
					// Extraer los Talde
					if (azkena.getLigakoTaldeak() != null) {
						for (DenboraldiTalde dt : azkena.getLigakoTaldeak()) {
							taldeakEditatzeko.add(dt.getTalde());
						}
					}
				} else {
					taldeakEditatzeko = federazioa.getTaldeGuztiak();
				}

				tabs.addTab("Admin - Jokalariak", new PanelAdmin(erabAktiboa, federazioa, taldeakEditatzeko, this));
			} else if (erabAktiboa instanceof ErabiltzaileEpaile) {
				boolean isUnekoDenboraldia = (aukeratutakoa == federazioa.getUnekoDenboraldia());

				tabs.addTab("Epailea - Sartu Emaitzak",
						new PanelEpailea(aukeratutakoa, (ErabiltzaileEpaile) erabAktiboa, isUnekoDenboraldia, this));

			} else if (erabAktiboa instanceof ErabiltzailePresi) {
				tabs.addTab("Presidentea - Taldea",
						new PanelPresi(erabAktiboa, this.federazioa, federazioa.getUnekoDenboraldia(), this));
			}
		}

		// Fitxa berreskuratu
		final int indexFinala = aukeratutakoIndizea;
		SwingUtilities.invokeLater(() -> {
			if (indexFinala != -1 && indexFinala < tabs.getTabCount()) {
				tabs.setSelectedIndex(indexFinala);
			} else if (tabs.getTabCount() > 0) {
				tabs.setSelectedIndex(0);
			}
			tabs.revalidate();
			tabs.repaint();
		});
	}

	public void interfazeaFreskatu() {
		this.aldaketakDauden = true;

		ActionListener[] listeners = cbDenboraldiak.getActionListeners();
		for (ActionListener al : listeners) {
			cbDenboraldiak.removeActionListener(al);
		}

		cbDenboraldiak.removeAllItems();
		ArrayList<Denboraldia> denboraldiak = federazioa.getDenboraldiak();

		if (denboraldiak != null) {
			for (Denboraldia d : denboraldiak) {
				cbDenboraldiak.addItem(d);
			}
			if (!denboraldiak.isEmpty()) {
				cbDenboraldiak.setSelectedIndex(denboraldiak.size() - 1);
			}
		}

		for (ActionListener al : listeners) {
			cbDenboraldiak.addActionListener(al);
		}

		tabsEguneratu();
	}

	// --- GETTERS & SETTERS ---
	public boolean isAldaketakDauden() {
		return aldaketakDauden;
	}

	public void setAldaketakDauden(boolean aldaketakDauden) {
		this.aldaketakDauden = aldaketakDauden;
	}

	public void gordeDatuak() {
	    utils.LogKudeatzailea.gehituLog("Datuak gordetzen...");
	    utils.XmlKudeatzailea xmlKudeatzailea = new utils.XmlKudeatzailea();
	    
	    boolean xlmOndoBoolean = xmlKudeatzailea.esportatuXML(this.federazioa, "src/data/federazioa.xml");
	    
	    if (!xlmOndoBoolean) {
	        utils.LogKudeatzailea.gehituErrorea("Huts egin du XML fitxategia esportatzean.");
	        JOptionPane.showMessageDialog(this, "Errorea XML-a gordetzean", "Error", JOptionPane.ERROR_MESSAGE);
	    } else {
	        utils.LogKudeatzailea.gehituLog("XML esportazioa ondo burutu da.");
	    }
	}

	// --- IRTEERA KUDEAKETA ---
	public void kudeatuIrteera(boolean isLogout) {
		if (aldaketakDauden) {
			int aukera = JOptionPane.showConfirmDialog(this, "Aldaketak egin dituzu. Gorde nahi dituzu irten aurretik?",
					"Gorde aldaketak", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

			if (aukera == JOptionPane.YES_OPTION) {
				gordeDatuak();
				exekutatuIrteera(isLogout);
			} else if (aukera == JOptionPane.NO_OPTION) {
				exekutatuIrteera(isLogout);
			}
		} else {
			int aukera = JOptionPane.showConfirmDialog(this,
					isLogout ? "Ziur zaude saioa itxi nahi duzula?" : "Ziur zaude programa itxi nahi duzula?",
					"Konfirmatu", JOptionPane.YES_NO_OPTION);

			if (aukera == JOptionPane.YES_OPTION) {
				exekutatuIrteera(isLogout);
			}
		}
	}
	
	

	private void exekutatuIrteera(boolean isLogout) {
		if (isLogout) {
			new Login().setVisible(true);
			dispose();
		} else {
			System.exit(0);
		}
	}
}