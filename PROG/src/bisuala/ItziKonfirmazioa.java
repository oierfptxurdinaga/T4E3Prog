package bisuala;

import javax.swing.*;
import java.awt.event.*;
import model.Federazioa;
import utils.DatuKarga;
import utils.XmlKudeatzailea;

public class ItziKonfirmazioa extends WindowAdapter {

	private APP app; 
	private Federazioa federazioa;

	public ItziKonfirmazioa(APP app, Federazioa federazioa) {
		this.app = app;
		this.federazioa = federazioa;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if (app.isAldaketakDauden()) {
			int aukera = JOptionPane.showConfirmDialog(app, "Aldaketak egin dituzu. Gorde nahi dituzu itxi aurretik?",
					"Gorde aldaketak", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

			if (aukera == JOptionPane.YES_OPTION) {

				utils.LogKudeatzailea.gehituLog("Datuak gordeta (.ser) eta aplikazioa itxita.");
				XmlKudeatzailea xmlKudeatzailea = new XmlKudeatzailea();
				Boolean xlmOndoBoolean = xmlKudeatzailea.esportatuXML(federazioa, "src/data/federazioa.xml");
				if (!xlmOndoBoolean) {
					utils.LogKudeatzailea.gehituErrorea("Huts egin du XML fitxategia esportatzean.");
					JOptionPane.showMessageDialog(null, "Errorea inprimatzean: ", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					utils.LogKudeatzailea.gehituLog("XML esportazioa zuzena.");
				}
				app.dispose();
				System.exit(0);
			} else if (aukera == JOptionPane.NO_OPTION) {
				app.dispose();
				System.exit(0);
			}

		} else {
			int aukera = JOptionPane.showConfirmDialog(app, "Ziur zaude programa itxi nahi duzula?", "Irten",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (aukera == JOptionPane.YES_OPTION) {
				app.dispose();
				System.exit(0);
			}
		}
	}
}