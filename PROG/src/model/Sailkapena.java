package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Liga edo denboraldi bateko sailkapena irudikatzen duen klasea.
 * 
 * Sailkapenak taldeak eta haien puntuak paraleloan gordetzen ditu,
 * eta puntu kopuruaren arabera ordenatzeko funtzionalitatea eskaintzen du.
 */
public class Sailkapena implements Serializable{

	/** Objektuaren bertsioa serializazioan kontrolatzeko identifikatzailea */
	private static final long serialVersionUID = 1L;
	private ArrayList<Talde> taldeak;
    private ArrayList<Integer> puntuak;

    /**
     * Hasieratutako sailkapen berri bat sortzen du.
     *
     * @param taldeak sailkapenean parte hartzen duten taldeak
     * @param puntuak talde bakoitzari dagokion puntu kopurua
     */
	public Sailkapena(ArrayList<Talde> taldeak, ArrayList<Integer> puntuak) {
		super();
		this.taldeak = taldeak;
		this.puntuak = puntuak;
	}
	public Sailkapena() {
	    this.taldeak = new ArrayList<>();
	    this.puntuak = new ArrayList<>();
	}
	
	// Getterrak eta setterrak
    public ArrayList<Talde> getTaldeak() {
		return taldeak;
	}

	public void setTaldeak(ArrayList<Talde> taldeak) {
		this.taldeak = taldeak;
	}

	public ArrayList<Integer> getPuntuak() {
		return puntuak;
	}

	public void setPuntuak(ArrayList<Integer> puntuak) {
		this.puntuak = puntuak;
	}

	/**
     * Talde berri bat sailkapenean gehitzen du.
     * 
     * Taldea hasieran 0 punturekin sartzen da.
     * 
     * @param t gehitu nahi den taldea
     */
	public void gehituTaldea(Talde t) {
        if (this.taldeak == null) this.taldeak = new ArrayList<>();
        this.taldeak.add(t);

        if (this.puntuak == null) this.puntuak = new ArrayList<>();
        this.puntuak.add(0); 
    }
    
	/**
     * Sailkapena puntu kopuruaren arabera ordenatzen du.
     *
     * Ordenazioa beheranzkoa da (puntu gehienetik gutxienera),
     * eta burbuila-algoritmoa (Bubble Sort) erabiltzen da.
     * 
     * Puntuak ordenatzean, dagokien taldeak ere elkarrekin
     * trukatzen dira koherentzia mantentzeko.
     * 
     * @param s ordenatu beharreko sailkapena
     */
    public void SailkapenaOrdenatu(Sailkapena s) {
        ArrayList<Talde> t = s.getTaldeak();
        ArrayList<Integer> p = s.getPuntuak();

        // Burbuja metodoa
        for (int i = 0; i < p.size() - 1; i++) {
            for (int j = 0; j < p.size() - i - 1; j++) {
                
                // Behekoak goikoak baino puntu gehiago baditu...
                if (p.get(j) < p.get(j + 1)) {
                    
                    // 1. Puntuak trukatzen ditugu
                    int tempPuntos = p.get(j);
                    p.set(j, p.get(j + 1));
                    p.set(j + 1, tempPuntos);

                    // 2. Derrigorrezkoa taldeak ere trukatzea
                    Talde tempTalde = t.get(j);
                    t.set(j, t.get(j + 1));
                    t.set(j + 1, tempTalde);
                }
            }
        }
    }

}