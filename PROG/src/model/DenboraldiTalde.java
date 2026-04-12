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
 *
 * Talde bakoitzari denboraldi honetan jokatutako partida kopurua, irabaziak,
 * berdinketak, galduak, gol kopuruak eta puntuazioa gordetzen ditu.
 *
 * Estatistika hauek {@link #emaitzakEguneratu(int, int)} metodoaren bidez
 * eguneratzen dira partiduen emaitzen arabera.
 */

@XmlAccessorType(XmlAccessType.NONE)
public class DenboraldiTalde {
	@XmlTransient
    private Talde talde;      // Talde originala
	@XmlElement(name = "Aktiboa_dago")
    private boolean aktiboa;   // Denboraldi honetan partizipatzen du
    private ArrayList<Jokalari> jokalariak;
    private int JP;            // Jokatutako partidak
    private int I;             // Irabazitak
    private int B;             // Berdinketak
    private int G;             // Galdutak
    private int GA;            // Golak alde
    private int GK;            // Golak kontra
    private int GD;            // Gol diferentzia
    private int PT;           // Puntuak

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

    @XmlElement(name = "Izena")
    public String getTaldeIzena() {
        return (talde != null) ? talde.getIzena() : null;
    }

    @XmlElement(name = "Ezkutua")
    @XmlJavaTypeAdapter(RutaEzkutuaAdapter.class)
    public String getTaldeEzkutua() {
        return (talde != null) ? talde.getEzkutua() : null;
    }

    @XmlElement(name = "Futbol_zelaia")
    public String getTaldeZelaia() {
        return (talde != null) ? talde.getFutbolZelaia() : null;
    }

    @XmlElement(name = "Hiria")
    public String getTaldeHiria() {
        return (talde != null) ? talde.getHiria() : null;
    }

    @XmlElement(name = "Informazioa")
    public String getTaldeInformazioa() {
        return (talde != null) ? talde.getInformazioa() : null;
    }

    @XmlElement(name = "Sorrera_data")
    public Integer getTaldeSorreraData() {
        return (talde != null) ? talde.getSorreraData() : null;
    }

    /**
     * Partida baten emaitzak eguneratzen ditu talde honen estatistiketan.
     * <p>
     * Golak alde eta kontra kontuan hartuta,
     * jokatutako partidak, irabaziak, berdinketak, galduak,
     * golak alde/kontra, gol-diferentzia eta puntuazioa eguneratzen dira.
     * </p>
     *
     * @param golAlde taldeak sartutako gol kopurua
     * @param golAurka aurkariak sartutako gol kopurua
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
    public void sartuJokalaria(Jokalari j) {
        this.jokalariak.add(j);
    }

    // Getterrak eta setterrak
    public Talde getTalde() { return talde; }
    public boolean isAktiboa() { return aktiboa; }
    public void setAktiboa(boolean aktiboa) { this.aktiboa = aktiboa; }

    public int getPJ() { return JP; }
    public int getG() { return I; }
    public int getE() { return B; }
    public int getP() { return G; }
    public int getGF() { return GA; }
    public int getGC() { return GK; }
    public int getDG() { return GD; }
    public int getPts() { return PT; }
}