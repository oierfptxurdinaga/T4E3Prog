package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Denboraldi bateko sailkapena irudikatzen duen klasea.
 * Taldeak eta haien puntuak zerrendetan gordetzen ditu, indize berdinarekin.
 */
public class Sailkapena implements Serializable {

    /** Serializaziorako bertsioa. */
    private static final long serialVersionUID = 1L;

    /** Sailkapeneko taldeen zerrenda. */
    private ArrayList<Talde> taldeak;

    /** Talde bakoitzari dagokion puntu kopurua (indize berdinean). */
    private ArrayList<Integer> puntuak;

    /**
     * Sailkapen berri bat sortzen du taldeak eta puntuak zehaztuta.
     *
     * @param taldeak taldeen zerrenda
     * @param puntuak puntuen zerrenda
     */
    public Sailkapena(ArrayList<Talde> taldeak, ArrayList<Integer> puntuak) {
        super();
        this.taldeak = taldeak;
        this.puntuak = puntuak;
    }

    /** Sailkapen hutsa sortzen du, zerrendak hasieratuta. */
    public Sailkapena() {
        this.taldeak = new ArrayList<>();
        this.puntuak = new ArrayList<>();
    }

    /**
     * Taldeen zerrenda itzultzen du.
     *
     * @return taldeen zerrenda
     */
    public ArrayList<Talde> getTaldeak() { return taldeak; }

    /**
     * Taldeen zerrenda ezartzen du.
     *
     * @param taldeak talde berrien zerrenda
     */
    public void setTaldeak(ArrayList<Talde> taldeak) { this.taldeak = taldeak; }

    /**
     * Puntuen zerrenda itzultzen du.
     *
     * @return puntuen zerrenda
     */
    public ArrayList<Integer> getPuntuak() { return puntuak; }

    /**
     * Puntuen zerrenda ezartzen du.
     *
     * @param puntuak puntu berrien zerrenda
     */
    public void setPuntuak(ArrayList<Integer> puntuak) { this.puntuak = puntuak; }

    /**
     * Talde berri bat sailkapenean gehitzen du 0 punturekin.
     *
     * @param t gehitu nahi den taldea
     */
    public void gehituTaldea(Talde t) {
        if (this.taldeak == null) {
            this.taldeak = new ArrayList<>();
        }
        this.taldeak.add(t);

        if (this.puntuak == null) {
            this.puntuak = new ArrayList<>();
        }
        this.puntuak.add(0);
    }

    /**
     * Sailkapena puntuen arabera beheranzkoan ordenatzen du (Bubble Sort).
     * Puntuak aldatzen direnean, taldeak ere batera aldatzen dira.
     *
     * @param s ordenatu nahi den sailkapena
     */
    public void SailkapenaOrdenatu(Sailkapena s) {
        ArrayList<Talde> t = s.getTaldeak();
        ArrayList<Integer> p = s.getPuntuak();

        for (int i = 0; i < p.size() - 1; i++) {
            for (int j = 0; j < p.size() - i - 1; j++) {
                if (p.get(j) < p.get(j + 1)) {
                    int tempPuntos = p.get(j);
                    p.set(j, p.get(j + 1));
                    p.set(j + 1, tempPuntos);

                    Talde tempTalde = t.get(j);
                    t.set(j, t.get(j + 1));
                    t.set(j + 1, tempTalde);
                }
            }
        }
    }
}
