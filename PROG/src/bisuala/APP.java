package bisuala;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import model.DenboraldiTalde;
import model.Denboraldia;
import model.Erabiltzaile;
import model.ErabiltzaileAdministraria;
import model.ErabiltzaileEpaile;
import model.ErabiltzailePresi;
import model.Federazioa;
import model.Talde;

/**
 * Aplikazioaren leiho nagusia.
 * Erabiltzaile aktiboarekin eta federazioko datuekin inicializatzen da,
 * eta fitxen (tabs) bidez panel guztiak kudeatzen ditu.
 */
public class APP extends JFrame {
	private static final long serialVersionUID = 1L;

	/** Denboraldiak aukeratzeko combo-box-a. */
	private JComboBox<Denboraldia> cbDenboraldiak;

	/** Fitxak dituen panela. */
	private JTabbedPane tabs;

	/** Saioa hasi duen erabiltzailea. */
	private Erabiltzaile erabAktiboa;

	/** Federazioaren datu-egitura nagusia. */
	private Federazioa federazioa;

	/** Aldaketak egin diren ala ez adierazten du. */
	private boolean aldaketakDauden = false;

	/**
	 * APP leiho nagusia sortzen du erabiltzaile eta federazioarekin.
	 *
	 * @param erab       saioa hasi duen erabiltzailea
	 * @param federazioa federazioaren datuak
	 */
	public APP(Erabiltzaile erab, Federazioa federazioa) {
		this.erabAktiboa = erab;
		this.federazioa = federazioa;
		ArrayList<Denboraldia> denboraldiak = federazioa.getDenboraldiak();

		setTitle("FNS Kudeaketa - " + erab.getErabiltzaile());
		setBounds(100, 100, 950, 700);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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
			if (!denboraldiak.isEmpty()) {
				cbDenboraldiak.setSelectedIndex(denboraldiak.size() - 1);
			}
		}
		pnlEzkerra.add(cbDenboraldiak);

		JPanel pnlEskubia = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pnlEskubia.setOpaque(false);
		if (erabAktiboa instanceof ErabiltzaileAdministraria || erabAktiboa instanceof ErabiltzailePresi) {
			JButton btnEsportatuXML = new JButton("Esportatu XML");
			btnEsportatuXML.setBackground(new Color(70, 130, 180));
			btnEsportatuXML.setForeground(Color.WHITE);
			btnEsportatuXML.setFocusPainted(false);
			btnEsportatuXML.addActionListener(e -> gordeDatuak());
			pnlEskubia.add(btnEsportatuXML);
		}

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
			if (index == -1) {
				return;
			}

			Component panelAktiboa = tabs.getSelectedComponent();
			String titulua = tabs.getTitleAt(index);

			if (panelAktiboa instanceof PanelPresi) {
				labelDenboraldia.setVisible(false);
				cbDenboraldiak.setVisible(false);
			} else {
				labelDenboraldia.setVisible(true);
				cbDenboraldiak.setVisible(true);
			}

			Denboraldia aukeratutakoa = (Denboraldia) cbDenboraldiak.getSelectedItem();
			if (aukeratutakoa != null) {
				if (titulua.equals("Sailkapena")) {
					ArrayList<DenboraldiTalde> sailkapenBerria = aukeratutakoa.getSailkapena();
					PanelSailkapena pBerria = new PanelSailkapena(sailkapenBerria, aukeratutakoa.getUrtea());
					tabs.setComponentAt(index, pBerria);
				} else if (titulua.equals("Taldeak")) {
					ArrayList<Talde> taldeakBakarrik = new ArrayList<>();
					if (aukeratutakoa.getLigakoTaldeak() != null) {
						for (DenboraldiTalde dt : aukeratutakoa.getLigakoTaldeak()) {
							taldeakBakarrik.add(dt.getTalde());
						}
					}
					PanelTaldeak pTaldeakBerria = new PanelTaldeak(taldeakBakarrik, aukeratutakoa.getUrtea());
					tabs.setComponentAt(index, pTaldeakBerria);
				} else if (titulua.equals("Jardunaldiak")) {
					PanelJardunaldiak pJardunaldiakBerria = new PanelJardunaldiak(aukeratutakoa);
					tabs.setComponentAt(index, pJardunaldiakBerria);
				}
			}
		});

		cbDenboraldiak.addActionListener(e -> tabsEguneratu());
		btnLogout.addActionListener(e -> kudeatuIrteera(true));

		tabsEguneratu();
	}

	/**
	 * Fitxa guztiak birsortzen ditu aukeratutako denboraldiaren arabera.
	 * Denboraldia aldatzean automatikoki deitzen da.
	 */
	private void tabsEguneratu() {
		int aukeratutakoIndizea = tabs.getSelectedIndex();
		tabs.removeAll();

		Denboraldia aukeratutakoa = (Denboraldia) cbDenboraldiak.getSelectedItem();

		if (aukeratutakoa != null) {
			ArrayList<DenboraldiTalde> sailkapena = aukeratutakoa.getSailkapena();
			tabs.addTab("Sailkapena", new PanelSailkapena(sailkapena, aukeratutakoa.getUrtea()));

			ArrayList<Talde> taldeakBakarrik = new ArrayList<>();
			if (aukeratutakoa.getLigakoTaldeak() != null) {
				for (DenboraldiTalde dt : aukeratutakoa.getLigakoTaldeak()) {
					taldeakBakarrik.add(dt.getTalde());
				}
			}
			tabs.addTab("Taldeak", new PanelTaldeak(taldeakBakarrik, aukeratutakoa.getUrtea()));
			tabs.addTab("Jardunaldiak", new PanelJardunaldiak(aukeratutakoa));

			if (erabAktiboa instanceof ErabiltzaileAdministraria) {
				ArrayList<Talde> taldeakEditatzeko = new ArrayList<>();
				if (!federazioa.getDenboraldiak().isEmpty()) {
					Denboraldia azkena = federazioa.getDenboraldiak().get(federazioa.getDenboraldiak().size() - 1);
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

	/**
	 * Interfazea freskatu eta combo-box-a eguneratzen du denboraldi berriekin.
	 * Denboraldi berri bat sortu ondoren deitzen da.
	 */
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

	/**
	 * Aldaketak egin diren ala ez itzultzen du.
	 *
	 * @return true aldaketak badaude
	 */
	public boolean isAldaketakDauden() {
		return aldaketakDauden;
	}

	/**
	 * Aldaketen egoera ezartzen du.
	 *
	 * @param aldaketakDauden egoera berria
	 */
	public void setAldaketakDauden(boolean aldaketakDauden) {
		this.aldaketakDauden = aldaketakDauden;
	}

	/**
	 * Federazioaren datuak XML fitxategira esportatzen ditu.
	 * Errorerik bada, erabiltzaileari mezu bat erakusten zaio.
	 */
	public void gordeDatuak() {
		utils.LogKudeatzailea.gehituLog("Datuak gordetzen ...");
		utils.XmlKudeatzailea xmlKudeatzailea = new utils.XmlKudeatzailea();

		boolean xlmOndoBoolean = xmlKudeatzailea.esportatuXML(this.federazioa, "src/data/federazioa.xml");

		if (!xlmOndoBoolean) {
			utils.LogKudeatzailea.gehituErrorea("Huts egin du XML fitxategia esportatzean.");
			JOptionPane.showMessageDialog(this, "Errorea egon da XML-a gordetzean.", "Errorea",
					JOptionPane.ERROR_MESSAGE);
		} else {
			utils.LogKudeatzailea.gehituLog("XML esportazioa ondo burutu da.");
			JOptionPane.showMessageDialog(this,
					"Datuak ondo esportatu dira XML fitxategira!\n(src/data/federazioa.xml)", "Esportazioa Burututa",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Irteera kudeatzen du: aldaketak badaude gorde nahi duen galdetzen du.
	 *
	 * @param isLogout true bada saioaren itxiera da; false bada programaren itxiera
	 */
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

	/**
	 * Irteera benetan exekutatzen du: logout bada Login ikusten du, bestela programa ixten du.
	 *
	 * @param isLogout true bada Login pantailara itzultzen da
	 */
	private void exekutatuIrteera(boolean isLogout) {
		if (isLogout) {
			new Login().setVisible(true);
			dispose();
		} else {
			System.exit(0);
		}
	}
}
