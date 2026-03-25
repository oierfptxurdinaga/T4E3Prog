package model;

import javax.persistence.Entity;
import javax.swing.JOptionPane;

/**
 * Administratzaile motako erabiltzailea.
 * Erabiltzaile honek jokalariak talde
 * batetik bestera transferitu ahal ditu.
 */

@Entity
public class ErabiltzaileAdministraria extends Erabiltzaile {
	
	/** Objektuaren bertsioa serializazioan kontrolatzeko identifikatzailea */
	private static final long serialVersionUID = 1L;

	/**
     * Erabiltzaile Administratzailearen eraikitzailea.
     * 
     * @param erabiltzaile Erabiltzaile-izena (login egiteko).
     * @param pasahitza    Sarbide-pasahitza.
     */
	public ErabiltzaileAdministraria(String erabiltzaile, String pasahitza) {
		super(erabiltzaile, pasahitza);
	}
	
	public ErabiltzaileAdministraria() {
        super();
    }

	// Getterrak eta setterrak
	public String getErabiltzaile() {
		return erabiltzaile;
	}
	public void setErabiltzaile(String erabiltzaile) {
		this.erabiltzaile = erabiltzaile;
	}
	public String getPasahitza() {
		return pasahitza;
	}
	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}

	/**
	 * Jokalari bat talde batetik bestera pasatzeko balio du
	 * 
	 * @param jokalari     Zein jokalari aldatu nahi duzun taldez
	 * @param taldeZaharra jokalaria dagoen taldea
	 * @param taldeBerria  jokalria eramango den taldea
	 */
	public void aldatuJokalariak(Jokalari jokalari, Talde taldeZaharra, Talde taldeBerria, boolean denboraldiaHasiDa) {
		if (!denboraldiaHasiDa) {
			if (taldeZaharra.getJokalariak().contains(jokalari)) {
				taldeZaharra.getJokalariak().remove(jokalari);
				taldeBerria.getJokalariak().add(jokalari);
			} else {
				JOptionPane.showMessageDialog(null, "Jokalari hori ez dago talde horretan", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Ezin dituzu jokalariak aldatu denboraldia hasi delako", "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
}