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

	// getters and setters
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
		for (int i = 0; i < denboraldia.getLigakoJardunaldi().size(); i++) {
			for (int j = 0; j < denboraldia.getLigakoJardunaldi().get(i).getPartiduak().size(); j++) {
				if (denboraldia.getLigakoJardunaldi().get(i).getPartiduak().get(j).getEtxekoTaldea()
						.equals(etxekoTaldea)
						&& denboraldia.getLigakoJardunaldi().get(i).getPartiduak().get(j).getKanpokoTaldea()
								.equals(kanpokoTaldea)) {
					denboraldia.getLigakoJardunaldi().get(i).getPartiduak().get(j).setEtxekoGolak(etxekoGolak);
					denboraldia.getLigakoJardunaldi().get(i).getPartiduak().get(j).setKanpokoGolak(kanpokoGolak);
					return;
				}
			}
		}
	}
}
