package model;

import java.io.Serializable;
import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Kirol federazio bat irudikatzen duen klasea.
 * Talde guztiak, denboraldiak eta erabiltzaileak kudeatzen ditu.
 */
@XmlRootElement(name = "Federazioa")
@XmlAccessorType(XmlAccessType.FIELD)
public class Federazioa implements Serializable {

    /** Serializaziorako bertsioa. */
    private static final long serialVersionUID = 1L;

    /** Federazioko talde guztien zerrenda (12 talde). */
    @XmlElementWrapper(name = "TaldeGuztiak")
    @XmlElement(name = "Talde")
    private ArrayList<Talde> taldeGuztiak;

    /** Jokatutako denboraldien historia. */
    @XmlElementWrapper(name = "Denboraldiak")
    @XmlElement(name = "Denboraldia")
    private ArrayList<Denboraldia> denboraldiak;

    /** Sistemako erabiltzaileen zerrenda. */
    @XmlElementWrapper(name = "Erabiltzaileak")
    @XmlElement(name = "Erabiltzailea")
    private ArrayList<Erabiltzaile> erabiltzaileak;

    /** Eraikitzaile hutsa, zerrendak hasieratuta. */
    public Federazioa() {
        this.taldeGuztiak = new ArrayList<>();
        this.denboraldiak = new ArrayList<>();
        this.erabiltzaileak = new ArrayList<>();
    }

    /**
     * Erabiltzaileen zerrenda itzultzen du.
     * Zerrenda null bada, automatikoki sortzen da.
     *
     * @return erabiltzaileen zerrenda
     */
    public ArrayList<Erabiltzaile> getErabiltzaileak() {
        if (this.erabiltzaileak == null) {
            this.erabiltzaileak = new ArrayList<>();
        }
        return this.erabiltzaileak;
    }

    /**
     * Talde berri bat federazioan gehitzen du.
     * Jada badago, ez da errepikatuko.
     *
     * @param t gehitu nahi den taldea
     */
    public void gehituTaldea(Talde t) {
        if (!taldeGuztiak.contains(t)) {
            taldeGuztiak.add(t);
        }
    }

    /**
     * Denboraldi berri bat federazioan gehitzen du.
     *
     * @param d gehitu nahi den denboraldia
     */
    public void gehituDenboraldia(Denboraldia d) {
        this.denboraldiak.add(d);
    }

    /**
     * Federazioko talde guztien zerrenda itzultzen du.
     *
     * @return taldeen zerrenda
     */
    public ArrayList<Talde> getTaldeGuztiak() { return taldeGuztiak; }

    /**
     * Denboraldien zerrenda itzultzen du.
     *
     * @return denboraldien zerrenda
     */
    public ArrayList<Denboraldia> getDenboraldiak() { return denboraldiak; }

    /**
     * Denboraldien zerrenda ezartzen du.
     *
     * @param denboraldiak denboraldi berrien zerrenda
     */
    public void setDenboraldiak(ArrayList<Denboraldia> denboraldiak) { this.denboraldiak = denboraldiak; }

    /**
     * Uneko (azken) denboraldia itzultzen du.
     *
     * @return azken denboraldia, edo null zerrenda hutsik badago
     */
    public Denboraldia getUnekoDenboraldia() {
        if (denboraldiak != null && !denboraldiak.isEmpty()) {
            return denboraldiak.get(denboraldiak.size() - 1);
        }
        return null;
    }
}
