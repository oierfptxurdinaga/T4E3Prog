package model;

import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import utils.RutaEzkutuaAdapter;

/**
 * Denboraldi jakin bateko talde baten estatistikak gordetzeko klasea.
 * Talde bakoitzaren partida kopurua, irabaziak, berdinketak, galduak,
 * golak eta puntuak gordetzen ditu.
 */
@XmlAccessorType(XmlAccessType.NONE)
public class DenboraldiTalde {

    /** Talde originala (XML-n ez da zuzenean serializatzen). */
    @XmlTransient
    private Talde talde;

    /** Taldea denboraldi honetan aktibo dagoen ala ez. */
    @XmlElement(name = "Aktiboa_dago")
    private boolean aktiboa;

    /** Denboraldi honetako jokalarien zerrenda. */
    private ArrayList<Jokalari> jokalariak;

    /** Jokatutako partidak. */
    private int JP;

    /** Irabazitako partidak. */
    private int I;

    /** Berdindutako partidak. */
    private int B;

    /** Galdutako partidak. */
    private int G;

    /** Golak alde (sartutakoak). */
    private int GA;

    /** Golak kontra (jasotakoak). */
    private int GK;

    /** Gol-diferentzia. */
    private int GD;

    /** Puntu kopurua. */
    private int PT;

    /**
     * DenboraldiTalde berri bat sortzen du.
     *
     * @param talde   oinarrizko taldea
     * @param aktiboa taldea aktibo dagoen ala ez
     */
    public DenboraldiTalde(Talde talde, boolean aktiboa) {
        this.talde = talde;
        this.aktiboa = aktiboa;
        this.jokalariak = new ArrayList<>();
        this.JP = 0;
        this.I = 0;
        this.B = 0;
        this.G = 0;
        this.GA = 0;
        this.GK = 0;
        this.GD = 0;
        this.PT = 0;
    }

    /**
     * Taldearen izena itzultzen du XML serializaziorako.
     *
     * @return taldearen izena
     */
    @XmlElement(name = "Izena")
    public String getTaldeIzena() {
        return (talde != null) ? talde.getIzena() : null;
    }

    /**
     * Taldearen ezkutuaren bidea itzultzen du XML serializaziorako.
     *
     * @return ezkutuaren bidea
     */
    @XmlElement(name = "Ezkutua")
    @XmlJavaTypeAdapter(RutaEzkutuaAdapter.class)
    public String getTaldeEzkutua() {
        return (talde != null) ? talde.getEzkutua() : null;
    }

    /**
     * Taldearen futbol-zelaiaren izena itzultzen du XML serializaziorako.
     *
     * @return futbol-zelaiaren izena
     */
    @XmlElement(name = "Futbol_zelaia")
    public String getTaldeZelaia() {
        return (talde != null) ? talde.getFutbolZelaia() : null;
    }

    /**
     * Taldearen hiria itzultzen du XML serializaziorako.
     *
     * @return hiria
     */
    @XmlElement(name = "Hiria")
    public String getTaldeHiria() {
        return (talde != null) ? talde.getHiria() : null;
    }

    /**
     * Taldearen informazio orokorra itzultzen du XML serializaziorako.
     *
     * @return informazioa
     */
    @XmlElement(name = "Informazioa")
    public String getTaldeInformazioa() {
        return (talde != null) ? talde.getInformazioa() : null;
    }

    /**
     * Taldearen sorrera-data itzultzen du XML serializaziorako.
     *
     * @return sorrera-data
     */
    @XmlElement(name = "Sorrera_data")
    public Integer getTaldeSorreraData() {
        return (talde != null) ? talde.getSorreraData() : null;
    }

    /**
     * Partida baten emaitzak talde honen estatistiketan eguneratzen ditu.
     * Irabazia bada 3 puntu, berdinketa bada 1, galera bada 0.
     *
     * @param golAlde  taldeak sartutako golak
     * @param golAurka aurkariak sartutako golak
     */
    public void emaitzakEguneratu(int golAlde, int golAurka) {
        this.JP++;
        this.GA += golAlde;
        this.GK += golAurka;
        this.GD = GA - GK;

        if (golAlde > golAurka) {
            this.I++;
            this.PT += 3;
        } else if (golAlde == golAurka) {
            this.B++;
            this.PT += 1;
        } else {
            this.G++;
        }
    }

    /**
     * Jokalari bat denboraldi honetako zerrendara gehitzen du.
     *
     * @param j gehitu nahi den jokalaria
     */
    public void sartuJokalaria(Jokalari j) {
        this.jokalariak.add(j);
    }

    /**
     * Talde originala itzultzen du.
     *
     * @return taldea
     */
    public Talde getTalde() { return talde; }

    /**
     * Taldea aktibo dagoen ala ez itzultzen du.
     *
     * @return true aktibo badago
     */
    public boolean isAktiboa() { return aktiboa; }

    /**
     * Taldearen egoera aktibo/ez aktibo ezartzen du.
     *
     * @param aktiboa egoera berria
     */
    public void setAktiboa(boolean aktiboa) { this.aktiboa = aktiboa; }

    /**
     * Jokatutako partida kopurua itzultzen du.
     *
     * @return jokatutako partidak
     */
    public int getPJ() { return JP; }

    /**
     * Irabazitako partida kopurua itzultzen du.
     *
     * @return irabaziak
     */
    public int getG() { return I; }

    /**
     * Berdindutako partida kopurua itzultzen du.
     *
     * @return berdinketak
     */
    public int getE() { return B; }

    /**
     * Galdutako partida kopurua itzultzen du.
     *
     * @return galduak
     */
    public int getP() { return G; }

    /**
     * Sartutako gol kopurua itzultzen du.
     *
     * @return golak alde
     */
    public int getGF() { return GA; }

    /**
     * Jasotako gol kopurua itzultzen du.
     *
     * @return golak kontra
     */
    public int getGC() { return GK; }

    /**
     * Gol-diferentzia itzultzen du.
     *
     * @return gol-diferentzia
     */
    public int getDG() { return GD; }

    /**
     * Puntu kopurua itzultzen du.
     *
     * @return puntuak
     */
    public int getPts() { return PT; }
}
