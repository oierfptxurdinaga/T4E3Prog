package bisuala;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import model.Federazioa;

/**
 * Leiho itxiera kudeatzeko klase laguntzailea.
 * Leihoa ixten saiatzean aldaketak dauden egiaztatzen du eta
 * gorde nahi duen galdetzen dio erabiltzaileari.
 */
public class ItziKonfirmazioa extends WindowAdapter {

	/** Aplikazioaren leiho nagusia. */
	private APP app;

	/** Federazioaren datuak (erreferentziarako gordeta). */
	private Federazioa federazioa;

	/**
	 * ItziKonfirmazioa entzulea sortzen du.
	 *
	 * @param app        aplikazioaren leiho nagusia
	 * @param federazioa federazioaren datuak
	 */
	public ItziKonfirmazioa(APP app, Federazioa federazioa) {
		this.app = app;
		this.federazioa = federazioa;
	}

	/**
	 * Leihoa ixten saiatzean deitzen da.
	 * Aldaketak badaude, gorde nahi duen galdetzen du itxi aurretik.
	 *
	 * @param e leihoaren gertaera
	 */
	@Override
	public void windowClosing(WindowEvent e) {
	    if (app.isAldaketakDauden()) {
	        int aukera = JOptionPane.showConfirmDialog(app, "Aldaketak egin dituzu. Gorde nahi dituzu itxi aurretik?",
	                "Gorde aldaketak", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

	        if (aukera == JOptionPane.YES_OPTION) {
	            app.gordeDatuak();
	            System.exit(0);
	        } else if (aukera == JOptionPane.NO_OPTION) {
	            System.exit(0);
	        }
	    } else {
	        System.exit(0);
	    }
	}
}
