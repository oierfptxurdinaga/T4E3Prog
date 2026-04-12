package model;

import java.io.Serializable;
import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Futbol edo kirol federazio bat irudikatzen duen klasea.
 *
 * Federazioak talde guztiak eta historikoki jokatutako denboraldiak kudeatzen ditu.
 * Talde eta denboraldi berriak gehitzeko metodoak eskaintzen ditu,
 * baita uneko denboraldia lortzeko funtzionalitatea ere.
 */
@XmlRootElement(name = "Federazioa")
@XmlAccessorType(XmlAccessType.FIELD)
public class Federazioa implements Serializable {
	/** Objektuaren bertsioa serializazioan kontrolatzeko identifikatzailea */
    private static final long serialVersionUID = 1L;

    // 1. TALDE GUZTIAK (Masterra: Hemen 12ak egongo dira)
    @XmlElementWrapper(name = "TaldeGuztiak")
    @XmlElement(name = "Talde")
    private ArrayList<Talde> taldeGuztiak;

    // 2. DENBORALDIAK (Historiala)
    @XmlElementWrapper(name = "Denboraldiak")
    @XmlElement(name = "Denboraldia")
    private ArrayList<Denboraldia> denboraldiak;

    @XmlElementWrapper(name = "Erabiltzaileak")
    @XmlElement(name = "Erabiltzailea")
    private ArrayList <Erabiltzaile> erabiltzaileak;


    public Federazioa() {
        this.taldeGuztiak = new ArrayList<>();
        this.denboraldiak = new ArrayList<>();
        this.erabiltzaileak = new ArrayList<>();
    }

    /**
     * Talde berri bat federazioan gehitzen du.
     *
     * Taldea jada existitzen bada, ez da errepikatuko.
     *
     * @param t gehitu nahi den taldea
     */
    public ArrayList<Erabiltzaile> getErabiltzaileak() {
        if (this.erabiltzaileak == null) {
            this.erabiltzaileak = new ArrayList<>();
        }
        return this.erabiltzaileak;
    }

    // --- KUDEAKETA METODOAK ---


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

    // Getterrak eta setterrak
    public ArrayList<Talde> getTaldeGuztiak() {
        return taldeGuztiak;
    }

    public ArrayList<Denboraldia> getDenboraldiak() {
        return denboraldiak;
    }
    public void setDenboraldiak(ArrayList<Denboraldia> denboraldiak) {
        this.denboraldiak = denboraldiak;
    }

    /**
     * Federazioan une honetan dagoen azken denboraldia itzultzen du.
     *
     * @return azken denboraldia, edo {@code null} denboraldiik ez badaude
     */
    public Denboraldia getUnekoDenboraldia() {
        if (denboraldiak != null && !denboraldiak.isEmpty()) {
            return denboraldiak.get(denboraldiak.size() - 1);
        }
        return null;
    }
}