package model;

import javax.persistence.Entity;

/**
 * Presidente motako erabiltzailea irudikatzen duen klasea.
 *
 * Erabiltzaile mota honek sisteman pribilegio gehienak ditu,
 * normalean kudeaketa orokorreko funtzioak betetzeko erabiltzen da.
 */

@Entity
public class ErabiltzailePresi extends Erabiltzaile {

	/** Objektuaren bertsioa serializazioan kontrolatzeko identifikatzailea */
	private static final long serialVersionUID = 1L;

	/**
     * Presidente motako erabiltzaile berri bat sortzen du.
     *
     * @param erabiltzaile erabiltzailearen izena
     * @param pasahitza erabiltzailearen pasahitza
     */
	public ErabiltzailePresi(String erabiltzaile, String pasahitza) {
		super(erabiltzaile, pasahitza);
	}
	public ErabiltzailePresi() {

	}

	// Getterrak eta setterrak
	@Override
	public String getErabiltzaile() {
		return erabiltzaile;
	}
	@Override
	public void setErabiltzaile(String erabiltzaile) {
		this.erabiltzaile = erabiltzaile;
	}
	@Override
	public String getPasahitza() {
		return pasahitza;
	}
	@Override
	public void setPasahitza(String pasahitza) {
		this.pasahitza = pasahitza;
	}
}
