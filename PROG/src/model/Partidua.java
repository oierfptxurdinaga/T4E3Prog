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
 * Liga edo denboraldi bateko partida bat irudikatzen duen klasea.
 *
 * Partida batek etxeko taldea eta kanpoko taldea ditu,
 * eta bien artean lortutako gol kopuruak gordetzen ditu.
 *
 * Gol kopuruaren balioen arabera, partida jokatu den ala ez
 * zehaztu daiteke.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Partidua implements Serializable {

	/** Objektuaren bertsioa serializazioan kontrolatzeko identifikatzailea */
	private static final long serialVersionUID = 1L;
	@XmlTransient private Talde etxekoTaldea;
	@XmlTransient private Talde kanpokoTaldea;
	@XmlTransient private int etxekoGolak;
	@XmlTransient private int kanpokoGolak;

	/**
     * Golik gabeko partida berri bat sortzen du.
     *
     * Hasieran golak {@code -1} balioarekin ezartzen dira,
     * partida oraindik jokatu ez dela adierazteko.
     *
     * @param etxekoTaldea etxean jokatzen duen taldea
     * @param kanpokoTaldea kanpoan jokatzen duen taldea
     */
	public Partidua(Talde etxekoTaldea, Talde kanpokoTaldea) {
		this.etxekoTaldea = etxekoTaldea;
		this.kanpokoTaldea = kanpokoTaldea;
		this.etxekoGolak = -1;
		this.kanpokoGolak = -1;
	}

	public Partidua() {}

	@XmlElement(name = "EtxekoTaldea")
    public String getEtxekoIzenaXML() { return etxekoTaldea.getIzena(); }

    @XmlElement(name = "EtxekoEzkutua")
    @XmlJavaTypeAdapter(RutaEzkutuaAdapter.class)
    public String getEtxekoEzkutuaXML() { return etxekoTaldea.getEzkutua(); }

    @XmlElement(name = "KanpokoTaldea")
    public String getKanpokoIzenaXML() { return kanpokoTaldea.getIzena(); }

    @XmlElement(name = "KanpokoEzkutua")
    @XmlJavaTypeAdapter(RutaEzkutuaAdapter.class)
    public String getKanpokoEzkutuaXML() { return kanpokoTaldea.getEzkutua(); }

    // Si NO se ha jugado, añade el atributo egoera="JokatuGabe"
    @XmlAttribute(name = "egoera")
    public String getEgoeraXML() {
        return jokatutaDago() ? null : "JokatuGabe";
    }

    // Si SÍ se ha jugado, crea la etiqueta <Emaitza>
    @XmlElement(name = "Emaitza")
    public EmaitzaXML getEmaitzaXML() {
        return jokatutaDago() ? new EmaitzaXML(etxekoGolak, kanpokoGolak) : null;
    }

    // --- Subclase para formatear el resultado ---
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class EmaitzaXML {
        @XmlAttribute(name = "etxekoGolak")
        public int etxekoGolak;

        @XmlAttribute(name = "kanpokoGolak")
        public int kanpokoGolak;

        public EmaitzaXML() {}
        public EmaitzaXML(int e, int k) {
            this.etxekoGolak = e;
            this.kanpokoGolak = k;
        }
    }

	/**
     * Emaitzarekin hasieratutako partida bat sortzen du.
     *
     * @param etxekoTaldea etxeko taldea
     * @param kanpokoTaldea kanpoko taldea
     * @param etxekoGolak etxeko taldeak sartutako gol kopurua
     * @param kanpokoGolak kanpoko taldeak sartutako gol kopurua
     */
	public Partidua(Talde etxekoTaldea, Talde kanpokoTaldea, int etxekoGolak, int kanpokoGolak) {
		this.etxekoTaldea = etxekoTaldea;
		this.kanpokoTaldea = kanpokoTaldea;
		this.etxekoGolak = etxekoGolak;
		this.kanpokoGolak = kanpokoGolak;
	}

	/**
     * Partida jokatu den ala ez adierazten du.
     *
     * Bi taldeetako golak balioz ezarrita badaude,
     * partida jokatu dela ulertzen da.
     *
     * @return {@code true} partida jokatu bada; bestela {@code false}
     */
	public boolean jokatutaDago() {
		return this.etxekoGolak != -1 && this.kanpokoGolak != -1;
	}

	// Getterrak eta setterrak
	public Talde getEtxekoTaldea() {
		return etxekoTaldea;
	}

	public void setEtxekoTaldea(Talde etxekoTaldea) {
		this.etxekoTaldea = etxekoTaldea;
	}

	public Talde getKanpokoTaldea() {
		return kanpokoTaldea;
	}

	public void setKanpokoTaldea(Talde kanpokoTaldea) {
		this.kanpokoTaldea = kanpokoTaldea;
	}

	public int getEtxekoGolak() {
		return etxekoGolak;
	}

	public void setEtxekoGolak(int etxekoGolak) {
		this.etxekoGolak = etxekoGolak;
	}

	public int getKanpokoGolak() {
		return kanpokoGolak;
	}

	public void setKanpokoGolak(int kanpokoGolak) {
		this.kanpokoGolak = kanpokoGolak;
	}
}
