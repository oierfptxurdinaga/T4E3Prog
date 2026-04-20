package model;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * Futbol jokalari bat irudikatzen duen klasea.
 * Izena, abizena, jaiotze urtea, dortsal zenbakia, posizioa eta egoera gordetzen ditu.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Jokalari implements Serializable {

    /** Serializaziorako bertsioa. */
    private static final long serialVersionUID = 1L;

    /** Datu-base IDa (XML-n ez da gordetzen). */
    @XmlTransient
    private int id;

    /** Jokalariak daukan dortsal zenbakia. */
    @XmlAttribute(name = "Dortsala")
    private int dortsala;

    /** Jokalariak izena. */
    @XmlElement(name = "Izena")
    private String izena;

    /** Jokalariak abizena. */
    @XmlElement(name = "Abizena")
    private String abizena;

    /** Jaiotze urtea. */
    @XmlElement(name = "Jaiotze_urtea")
    private int jaiotzeUrtea;

    /** Jokalariak posizioa (atzelaria, erdilarria...). */
    @XmlElement(name = "Posizioa")
    private String posizio;

    /** Jokalaria aktibo dagoen ala ez. */
    @XmlElement(name = "Aktiboa_dago")
    private boolean aktiboaDago;

    /** Jokalariak argazkiaren bidea. */
    @XmlElement(name = "Argazkia")
    private String argazkia;

    /**
     * Jokalari berri bat sortzen du datu guztiekin.
     *
     * @param id           datu-base IDa
     * @param izena        izena
     * @param abizena      abizena
     * @param jaiotseUrtea jaiotze urtea
     * @param dortsala     dortsal zenbakia
     * @param posizio      posizioa
     * @param aktiboaDago  aktibo dagoen ala ez
     * @param argazkia     argazkiaren bidea
     */
    public Jokalari(int id, String izena, String abizena, int jaiotseUrtea, int dortsala, String posizio,
            boolean aktiboaDago, String argazkia) {
        this.id = id;
        this.izena = izena;
        this.abizena = abizena;
        this.jaiotzeUrtea = jaiotseUrtea;
        this.dortsala = dortsala;
        this.posizio = posizio;
        this.aktiboaDago = aktiboaDago;
        this.argazkia = argazkia;
    }

    /** Eraikitzaile hutsa. */
    public Jokalari() { super(); }

    /**
     * Datu-base IDa itzultzen du.
     *
     * @return id
     */
    public int getId() { return id; }

    /**
     * Datu-base IDa ezartzen du.
     *
     * @param id id berria
     */
    public void setId(int id) { this.id = id; }

    /**
     * Jokalariak izena itzultzen du.
     *
     * @return izena
     */
    public String getIzena() { return izena; }

    /**
     * Jokalariak izena ezartzen du.
     *
     * @param izena izen berria
     */
    public void setIzena(String izena) { this.izena = izena; }

    /**
     * Jokalariak abizena itzultzen du.
     *
     * @return abizena
     */
    public String getAbizena() { return abizena; }

    /**
     * Jokalariak abizena ezartzen du.
     *
     * @param abizena abizen berria
     */
    public void setAbizena(String abizena) { this.abizena = abizena; }

    /**
     * Jaiotze urtea itzultzen du.
     *
     * @return jaiotze urtea
     */
    public int getJaiotzeUrtea() { return jaiotzeUrtea; }

    /**
     * Jaiotze urtea ezartzen du.
     *
     * @param adina jaiotze urte berria
     */
    public void setJaiotzeUrtea(int adina) { this.jaiotzeUrtea = adina; }

    /**
     * Dortsal zenbakia itzultzen du.
     *
     * @return dortsala
     */
    public int getDortsala() { return dortsala; }

    /**
     * Dortsal zenbakia ezartzen du.
     *
     * @param dortsala dortsal berria
     */
    public void setDortsala(int dortsala) { this.dortsala = dortsala; }

    /**
     * Posizioa itzultzen du.
     *
     * @return posizioa
     */
    public String getPosizio() { return posizio; }

    /**
     * Posizioa ezartzen du.
     *
     * @param posizio posizio berria
     */
    public void setPosizio(String posizio) { this.posizio = posizio; }

    /**
     * Jokalaria aktibo dagoen ala ez itzultzen du.
     *
     * @return true aktibo badago
     */
    public boolean isAktiboaDago() { return aktiboaDago; }

    /**
     * Jokalariak egoera aktibo/ez aktibo ezartzen du.
     *
     * @param aktiboaDago egoera berria
     */
    public void setAktiboaDago(boolean aktiboaDago) { this.aktiboaDago = aktiboaDago; }

    /**
     * Argazkiaren bidea itzultzen du.
     *
     * @return argazkiaren bidea
     */
    public String getArgazkia() { return argazkia; }

    /**
     * Argazkiaren bidea ezartzen du.
     *
     * @param argazkia bide berria
     */
    public void setArgazkia(String argazkia) { this.argazkia = argazkia; }

    /**
     * Jokalari honen kopia berri bat sortzen du.
     *
     * @return jokalariak kopia
     */
    public Jokalari kopiatu() {
        return new Jokalari(this.id, this.izena, this.abizena, this.jaiotzeUrtea,
                this.dortsala, this.posizio, this.aktiboaDago, this.argazkia);
    }

    /**
     * Jokalariak irudi automatikoa sortzen du Dicebear API erabiliz.
     *
     * @param urtea urtea, irudiaren seed-era gehitzeko
     * @return irudiaren URL-a
     */
    public String getIrudiaUrl(int urtea) {
        String seed = this.izena.replaceAll(" ", "") + urtea;
        return "https://api.dicebear.com/7.x/avataaars/png?seed=" + seed;
    }
}
