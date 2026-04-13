package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import utils.RutaEzkutuaAdapter;

/**
 * Futbol talde bat irudikatzen duen klasea.
 * Taldearen identitatea (izena, hiria, zelaia, ezkutua) eta
 * jokalarien zerrenda gordetzen ditu.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Talde implements Serializable {

    /** Serializaziorako bertsioa. */
    private static final long serialVersionUID = 1L;

    /** Datu-base IDa (XML-n ez da gordetzen). */
    @XmlTransient
    private int id;

    /** Taldearen izena. */
    @XmlElement(name = "Izena")
    private String izena;

    /** Taldearen ezkutuaren bidea. */
    @XmlElement(name = "Ezkutua")
    @XmlJavaTypeAdapter(RutaEzkutuaAdapter.class)
    private String ezkutua;

    /** Taldeak jokatzen duen estadioaren izena. */
    @XmlElement(name = "Futbol_zelaia")
    private String futbolZelaia;

    /** Taldearen jokalarien zerrenda. */
    @XmlElementWrapper(name = "Jokalariak")
    @XmlElement(name = "Jokalari")
    private ArrayList<Jokalari> jokalariak;

    /** Taldeak dagoen hiria. */
    @XmlElement(name = "Hiria")
    private String hiria;

    /** Taldeari buruzko informazio orokorra. */
    @XmlElement(name = "Informazioa")
    private String informazioa;

    /** Taldea sortu zen urtea. */
    @XmlElement(name = "Sorrera_data")
    private int sorreraData;

    /** Taldea uneko denboraldian aktibo dagoen ala ez. */
    @XmlElement(name = "Aktiboa_dago")
    private boolean aktiboaDago;

    /**
     * Talde berri bat sortzen du datu guztiekin.
     *
     * @param id           datu-base IDa
     * @param izena        taldearen izena
     * @param eskutua      ezkutuaren bidea
     * @param futbolZelaia estadioaren izena
     * @param jokalariak   jokalarien zerrenda
     * @param hiria        taldeak dagoen hiria
     * @param aktiboaDago  aktibo dagoen ala ez
     * @param informazioa  taldearen informazioa
     * @param sorreraData  sorrera urtea
     */
    public Talde(int id, String izena, String eskutua, String futbolZelaia, ArrayList<Jokalari> jokalariak,
            String hiria, boolean aktiboaDago, String informazioa, int sorreraData) {
        this.id = id;
        this.izena = izena;
        this.ezkutua = eskutua;
        this.futbolZelaia = futbolZelaia;
        this.jokalariak = jokalariak;
        this.hiria = hiria;
        this.aktiboaDago = aktiboaDago;
        this.informazioa = informazioa;
        this.sorreraData = sorreraData;
    }

    /**
     * Talde baten kopia sortzen du.
     *
     * @param taldea kopiatu nahi den taldea
     */
    public Talde(Talde taldea) {
        this.id = taldea.id;
        this.izena = taldea.izena;
        this.ezkutua = taldea.ezkutua;
        this.futbolZelaia = taldea.futbolZelaia;
        this.jokalariak = taldea.jokalariak;
        this.hiria = taldea.hiria;
        this.aktiboaDago = taldea.aktiboaDago;
    }

    /** Eraikitzaile hutsa. */
    public Talde() {}

    /**
     * Taldearen izena itzultzen du.
     *
     * @return izena
     */
    public String getIzena() { return izena; }

    /**
     * Taldearen izena ezartzen du.
     *
     * @param izena izen berria
     */
    public void setIzena(String izena) { this.izena = izena; }

    /**
     * Ezkutuaren bidea itzultzen du.
     *
     * @return ezkutuaren bidea
     */
    public String getEzkutua() { return ezkutua; }

    /**
     * Ezkutuaren bidea ezartzen du.
     *
     * @param eskutua bide berria
     */
    public void setEzkutua(String eskutua) { this.ezkutua = eskutua; }

    /**
     * Estadioaren izena itzultzen du.
     *
     * @return estadioaren izena
     */
    public String getFutbolZelaia() { return futbolZelaia; }

    /**
     * Estadioaren izena ezartzen du.
     *
     * @param futbolZelaia estadio berriaren izena
     */
    public void setFutbolZelaia(String futbolZelaia) { this.futbolZelaia = futbolZelaia; }

    /**
     * Jokalarien zerrenda itzultzen du.
     *
     * @return jokalarien zerrenda
     */
    public ArrayList<Jokalari> getJokalariak() { return jokalariak; }

    /**
     * Jokalarien zerrenda ezartzen du.
     *
     * @param jokalariak jokalari berrien zerrenda
     */
    public void setJokalariak(ArrayList<Jokalari> jokalariak) { this.jokalariak = jokalariak; }

    /**
     * Taldeak dagoen hiria itzultzen du.
     *
     * @return hiria
     */
    public String getHiria() { return hiria; }

    /**
     * Taldeak dagoen hiria ezartzen du.
     *
     * @param hiria hiri berria
     */
    public void setHiria(String hiria) { this.hiria = hiria; }

    /**
     * Taldea aktibo dagoen ala ez itzultzen du.
     *
     * @return true aktibo badago
     */
    public boolean isAktiboaDago() { return aktiboaDago; }

    /**
     * Taldearen egoera aktibo/ez aktibo ezartzen du.
     *
     * @param aktiboaDago egoera berria
     */
    public void setAktiboaDago(boolean aktiboaDago) { this.aktiboaDago = aktiboaDago; }

    /**
     * Taldearen informazioa itzultzen du.
     *
     * @return informazioa
     */
    public String getInformazioa() { return informazioa; }

    /**
     * Taldearen informazioa ezartzen du.
     *
     * @param informazioa informazio berria
     */
    public void setInformazioa(String informazioa) { this.informazioa = informazioa; }

    /**
     * Sorrera data itzultzen du.
     *
     * @return sorrera urtea
     */
    public int getSorreraData() { return sorreraData; }

    /**
     * Sorrera data ezartzen du.
     *
     * @param sorreraData sorrera urte berria
     */
    public void setSorreraData(int sorreraData) { this.sorreraData = sorreraData; }

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
     * Taldearen ezkutua aldatzen du, aldaketa beharrezkoa bada soilik.
     *
     * @param ezkutua ezkutu berriaren bidea
     */
    public void aldatuEzkutua(String ezkutua) {
        if (this.ezkutua != ezkutua) {
            this.ezkutua = ezkutua;
        }
    }

    /**
     * Jokalari berri bat taldean gehitzen du.
     * Zerrenda null bada, automatikoki sortzen da.
     *
     * @param j gehitu nahi den jokalaria
     */
    public void sartuJokalaria(Jokalari j) {
        if (this.jokalariak == null) {
            this.jokalariak = new ArrayList<>();
        }
        this.jokalariak.add(j);
    }

    /**
     * Taldearen izena itzultzen du kate gisa.
     *
     * @return taldearen izena
     */
    @Override
    public String toString() { return this.izena; }

    /** {@inheritDoc} */
    @Override
    public int hashCode() { return Objects.hash(izena); }

    /**
     * Bi talde berdinak diren egiaztatzen du izenaren arabera.
     *
     * @param obj konparatu nahi den objektua
     * @return true izenak berdinak badira
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if ((obj == null) || (getClass() != obj.getClass())) return false;
        Talde other = (Talde) obj;
        return Objects.equals(izena, other.izena);
    }

    /**
     * Talde honen kopia sakona sortzen du.
     * Jokalari guztiak ere banan-banan kopiatzen dira.
     *
     * @return taldearen kopia berria
     */
    public Talde kopiatu() {
        ArrayList<Jokalari> jokalariKopiak = new ArrayList<>();
        if (this.jokalariak != null) {
            for (Jokalari j : this.jokalariak) {
                jokalariKopiak.add(j.kopiatu());
            }
        }
        return new Talde(this.id, this.izena, this.ezkutua, this.futbolZelaia,
                jokalariKopiak, this.hiria, this.aktiboaDago, this.informazioa, this.sorreraData);
    }
}
