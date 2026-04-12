package model;

import javax.persistence.Entity;

/**
 * Epaile motako erabiltzailea.
 * Erabiltzaile honek jokatutako partiduen emaitzak
 * sisteman sartu ahal ditu.
 */

@Entity
public class ErabiltzaileEpaile extends Erabiltzaile {

	/** Objektuaren bertsioa serializazioan kontrolatzeko identifikatzailea */
	private static final long serialVersionUID = 1L;

	 /**
     * Epaile motako erabiltzaile berri bat sortzen du.
     *
     * @param erabiltzaile erabiltzailearen izena
     * @param pasahitza erabiltzailearen pasahitza
     */
	public ErabiltzaileEpaile(String erabiltzaile, String pasahitza) {
		super(erabiltzaile, pasahitza);
	}
	public ErabiltzaileEpaile() {
		// TODO Auto-generated constructor stub
    }

	// getters and setters
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

	/**
     * Partidu baten emaitza sisteman erregistratzen du.
     *
     * @param denboraldia   Partidua dagokion denboraldia.
     * @param etxekoTaldea  Etxeko taldea.
     * @param kanpokoTaldea Kanpoko taldea.
     * @param etxekoGolak   Etxeko taldeak sartutako gol kopurua.
     * @param kanpokoGolak  Kanpoko taldeak sartutako gol kopurua.
     */
	public void sartuEmaitza(Denboraldia denboraldia, Talde etxekoTaldea, Talde kanpokoTaldea, int etxekoGolak,
			int kanpokoGolak) {
		for (Jardunaldi element : denboraldia.getLigakoJardunaldi()) {
			for (Partidua element2 : element.getPartiduak()) {
				if (element2.getEtxekoTaldea()
						.equals(etxekoTaldea)
						&& element2.getKanpokoTaldea()
								.equals(kanpokoTaldea)) {
					element2.setEtxekoGolak(etxekoGolak);
					element2.setKanpokoGolak(kanpokoGolak);
					return;
				}
			}
		}
	}
}
