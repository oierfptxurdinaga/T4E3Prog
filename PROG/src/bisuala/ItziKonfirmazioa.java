package bisuala;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import model.Federazioa;

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