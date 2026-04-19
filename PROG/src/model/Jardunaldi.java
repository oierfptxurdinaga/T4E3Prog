package model;

import java.io.Serializable;
import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * Denboraldi bateko jardunaldi bat irudikatzen duen klasea.
 * Jardunaldiak zenbaki baten bidez identifikatzen dira eta haien partiduak gordetzen ditu.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Jardunaldi implements Serializable {

    /** Serializaziorako bertsioa. */
    private static final long serialVersionUID = 1L;

    /** Jardunaldiaren datu-base IDa. */
    @XmlTransient
    private int id;

    /** Jardunaldiaren zenbakia (1, 2, 3...). */
    @XmlAttribute(name = "zenbakia")
    private int jardunaldiZbk;

    /** Jardunaldiko partiduen zerrenda. */
    @XmlElement(name = "Partidua")
    private ArrayList<Partidua> partiduak;

    /** JAXB-rako eraikitzaile hutsa. */
    public Jardunaldi() {}

    /**
     * Jardunaldi berri bat sortzen du zenbaki, partida zerrenda eta ID batekin.
     *
     * @param jardunaldiZbk jardunaldiaren zenbakia
     * @param partiduak     partiduen zerrenda
     * @param id            datu-base IDa
     */
    public Jardunaldi(int jardunaldiZbk, ArrayList<Partidua> partiduak, int id) {
        this.id = id;
        this.jardunaldiZbk = jardunaldiZbk;
        this.partiduak = partiduak;
    }

    /**
     * Jardunaldi berri bat sortzen du zenbaki batekin.
     * Partida zerrenda hutsik sortzen da.
     *
     * @param jardunaldiZbk jardunaldiaren zenbakia
     */
    public Jardunaldi(int jardunaldiZbk) {
        this.jardunaldiZbk = jardunaldiZbk;
        this.partiduak = new ArrayList<>();
    }

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
     * Jardunaldiaren zenbakia itzultzen du.
     *
     * @return jardunaldiaren zenbakia
     */
    public int getJardunaldiZbk() { return jardunaldiZbk; }

    /**
     * Jardunaldiaren zenbakia ezartzen du.
     *
     * @param jardunaldiZbk zenbaki berria
     */
    public void setJardunaldiZbk(int jardunaldiZbk) { this.jardunaldiZbk = jardunaldiZbk; }

    /**
     * Jardunaldiko partiduen zerrenda itzultzen du.
     *
     * @return partiduen zerrenda
     */
    public ArrayList<Partidua> getPartiduak() { return partiduak; }

    /**
     * Jardunaldiko partiduen zerrenda ezartzen du.
     *
     * @param partiduak partida berrien zerrenda
     */
    public void setPartiduak(ArrayList<Partidua> partiduak) { this.partiduak = partiduak; }

    /**
     * Partida berri bat jardunaldian gehitzen du.
     * Zerrenda null bada, automatikoki sortzen da.
     *
     * @param p gehitu nahi den partida
     */
    public void addPartidua(Partidua p) {
        if (this.partiduak == null) {
            this.partiduak = new ArrayList<>();
        }
        this.partiduak.add(p);
    }
}
