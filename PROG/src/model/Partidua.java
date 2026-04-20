package model;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import utils.RutaEzkutuaAdapter;

/**
 * Denboraldi bateko partida bat irudikatzen duen klasea.
 * Etxeko eta kanpoko taldeak eta haien gol kopuruak gordetzen ditu.
 * Gol balioak -1 badira, partida oraindik jokatu gabe dagoela esan nahi du.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Partidua implements Serializable {

    /** Serializaziorako bertsioa. */
    private static final long serialVersionUID = 1L;

    /** Etxean jokatzen duen taldea (XML-n ez da zuzenean gordetzen). */
    @XmlTransient private Talde etxekoTaldea;

    /** Kanpoan jokatzen duen taldea (XML-n ez da zuzenean gordetzen). */
    @XmlTransient private Talde kanpokoTaldea;

    /** Etxeko taldeak sartutako golak (-1 jokatu gabe badago). */
    @XmlTransient private int etxekoGolak;

    /** Kanpoko taldeak sartutako golak (-1 jokatu gabe badago). */
    @XmlTransient private int kanpokoGolak;

    /**
     * Partida berri bat sortzen du, oraindik jokatu gabe.
     * Golak -1 balioarekin hasieratzen dira.
     *
     * @param etxekoTaldea  etxean jokatzen duen taldea
     * @param kanpokoTaldea kanpoan jokatzen duen taldea
     */
    public Partidua(Talde etxekoTaldea, Talde kanpokoTaldea) {
        this.etxekoTaldea = etxekoTaldea;
        this.kanpokoTaldea = kanpokoTaldea;
        this.etxekoGolak = -1;
        this.kanpokoGolak = -1;
    }

    /** JAXB-rako eraikitzaile hutsa. */
    public Partidua() {}

    /**
     * Etxeko taldearen izena itzultzen du XML serializaziorako.
     *
     * @return etxeko taldearen izena
     */
    @XmlElement(name = "EtxekoTaldea")
    public String getEtxekoIzenaXML() { return etxekoTaldea.getIzena(); }

    /**
     * Etxeko taldearen ezkutuaren bidea itzultzen du XML serializaziorako.
     *
     * @return etxeko taldearen ezkutua
     */
    @XmlElement(name = "EtxekoEzkutua")
    @XmlJavaTypeAdapter(RutaEzkutuaAdapter.class)
    public String getEtxekoEzkutuaXML() { return etxekoTaldea.getEzkutua(); }

    /**
     * Kanpoko taldearen izena itzultzen du XML serializaziorako.
     *
     * @return kanpoko taldearen izena
     */
    @XmlElement(name = "KanpokoTaldea")
    public String getKanpokoIzenaXML() { return kanpokoTaldea.getIzena(); }

    /**
     * Kanpoko taldearen ezkutuaren bidea itzultzen du XML serializaziorako.
     *
     * @return kanpoko taldearen ezkutua
     */
    @XmlElement(name = "KanpokoEzkutua")
    @XmlJavaTypeAdapter(RutaEzkutuaAdapter.class)
    public String getKanpokoEzkutuaXML() { return kanpokoTaldea.getEzkutua(); }

    /**
     * Partida jokatu gabe badago "JokatuGabe" itzultzen du XML atributu gisa.
     *
     * @return "JokatuGabe" edo null jokatu bada
     */
    @XmlAttribute(name = "egoera")
    public String getEgoeraXML() {
        return jokatutaDago() ? null : "JokatuGabe";
    }

    /**
     * Partidaren emaitza XML formatuan itzultzen du, jokatu bada soilik.
     *
     * @return EmaitzaXML objektua, edo null jokatu gabe badago
     */
    @XmlElement(name = "Emaitza")
    public EmaitzaXML getEmaitzaXML() {
        return jokatutaDago() ? new EmaitzaXML(etxekoGolak, kanpokoGolak) : null;
    }

    /**
     * Partidaren emaitza XML formatuan gordetzeko klase laguntzailea.
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class EmaitzaXML {

        /** Etxeko taldeak sartutako golak. */
        @XmlAttribute(name = "etxekoGolak")
        public int etxekoGolak;

        /** Kanpoko taldeak sartutako golak. */
        @XmlAttribute(name = "kanpokoGolak")
        public int kanpokoGolak;

        /** JAXB-rako eraikitzaile hutsa. */
        public EmaitzaXML() {}

        /**
         * Emaitza berri bat sortzen du.
         *
         * @param e etxeko golak
         * @param k kanpoko golak
         */
        public EmaitzaXML(int e, int k) {
            this.etxekoGolak = e;
            this.kanpokoGolak = k;
        }
    }

    /**
     * Emaitzarekin hasieratutako partida bat sortzen du.
     *
     * @param etxekoTaldea  etxeko taldea
     * @param kanpokoTaldea kanpoko taldea
     * @param etxekoGolak   etxeko golak
     * @param kanpokoGolak  kanpoko golak
     */
    public Partidua(Talde etxekoTaldea, Talde kanpokoTaldea, int etxekoGolak, int kanpokoGolak) {
        this.etxekoTaldea = etxekoTaldea;
        this.kanpokoTaldea = kanpokoTaldea;
        this.etxekoGolak = etxekoGolak;
        this.kanpokoGolak = kanpokoGolak;
    }

    /**
     * Partida jokatu den ala ez egiaztatzen du.
     * Bi taldeetan golak -1 ez badira, jokatutatzat hartzen da.
     *
     * @return true partida jokatu bada
     */
    public boolean jokatutaDago() {
        return this.etxekoGolak != -1 && this.kanpokoGolak != -1;
    }

    /**
     * Etxeko taldea itzultzen du.
     *
     * @return etxeko taldea
     */
    public Talde getEtxekoTaldea() { return etxekoTaldea; }

    /**
     * Etxeko taldea ezartzen du.
     *
     * @param etxekoTaldea talde berria
     */
    public void setEtxekoTaldea(Talde etxekoTaldea) { this.etxekoTaldea = etxekoTaldea; }

    /**
     * Kanpoko taldea itzultzen du.
     *
     * @return kanpoko taldea
     */
    public Talde getKanpokoTaldea() { return kanpokoTaldea; }

    /**
     * Kanpoko taldea ezartzen du.
     *
     * @param kanpokoTaldea talde berria
     */
    public void setKanpokoTaldea(Talde kanpokoTaldea) { this.kanpokoTaldea = kanpokoTaldea; }

    /**
     * Etxeko taldeak sartutako golak itzultzen du.
     *
     * @return etxeko golak
     */
    public int getEtxekoGolak() { return etxekoGolak; }

    /**
     * Etxeko taldeak sartutako golak ezartzen du.
     *
     * @param etxekoGolak gol kopuru berria
     */
    public void setEtxekoGolak(int etxekoGolak) { this.etxekoGolak = etxekoGolak; }

    /**
     * Kanpoko taldeak sartutako golak itzultzen du.
     *
     * @return kanpoko golak
     */
    public int getKanpokoGolak() { return kanpokoGolak; }

    /**
     * Kanpoko taldeak sartutako golak ezartzen du.
     *
     * @param kanpokoGolak gol kopuru berria
     */
    public void setKanpokoGolak(int kanpokoGolak) { this.kanpokoGolak = kanpokoGolak; }
}
