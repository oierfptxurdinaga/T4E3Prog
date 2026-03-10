package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import utils.RutaEzkutuaAdapter;
/**
 * Liga edo federazio bateko talde bat irudikatzen duen klasea.
 * 
 * Talde batek bere identitatea (izena, hiria, zelaia, ezkutua) eta
 * jokalarien zerrenda gordetzen ditu, baita denboraldi batean
 * aktiboa dagoen ala ez adierazten duen egoera ere.
 * 
 * Klase honek jokalariak gehitzeko, ezkutua aldatzeko eta
 * taldearen kopia sakona sortzeko funtzionalitateak eskaintzen ditu.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Talde implements Serializable{
	
	/** Objektuaren bertsioa serializazioan kontrolatzeko identifikatzailea */
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "Izena")
	private String izena;
	
	@XmlElement(name = "Ezkutua")
    @XmlJavaTypeAdapter(RutaEzkutuaAdapter.class)
	private String ezkutua;
	
	@XmlElement(name = "Futbol_zelaia")
	private String futbolZelaia;
	
	@XmlElementWrapper(name = "Jokalariak")
    @XmlElement(name = "Jokalari")
	private ArrayList<Jokalari> jokalariak;
	
	@XmlElement(name = "Hiria")
	private String hiria;
	private boolean aktiboaDago;
	
	public Talde(String izena, String eskutua, String futbolZelaia, ArrayList<Jokalari> jokalariak, String hiria, boolean aktiboaDago) {
		this.izena = izena;
		this.ezkutua = eskutua;
		this.futbolZelaia = futbolZelaia;
		this.jokalariak = jokalariak;
		this.hiria = hiria;
		this.aktiboaDago = aktiboaDago;
	}
	
	/**
     * Beste talde baten kopiatik talde berri bat sortzen du.
     *
     * @param taldea kopiatu nahi den taldea
     */
	public Talde(Talde taldea) {
		this.izena = taldea.izena;
		this.ezkutua = taldea.ezkutua;
		this.futbolZelaia = taldea.futbolZelaia;
		this.jokalariak = taldea.jokalariak;
		this.hiria = taldea.hiria;
		this.aktiboaDago = taldea.aktiboaDago;
	}
	public Talde() {
		
	}
	// Getterrak eta setterrak
	
	public String getIzena() {
		return izena;
	}

	public void setIzena(String izena) {
		this.izena = izena;
	}

	public String getEzkutua() {
		return ezkutua;
	}

	public void setEzkutua(String eskutua) {
		this.ezkutua = eskutua;
	}

	public String getFutbolZelaia() {
		return futbolZelaia;
	}

	public void setFutbolZelaia(String futbolZelaia) {
		this.futbolZelaia = futbolZelaia;
	}

	public ArrayList<Jokalari> getJokalariak() {
		return jokalariak;
	}

	public void setJokalariak(ArrayList<Jokalari> jokalariak) {
		this.jokalariak = jokalariak;
	}

	public String getHiria() {
		return hiria;
	}

	public void setHiria(String hiria) {
		this.hiria = hiria;
	}

	public boolean isAktiboaDago() {
		return aktiboaDago;
	}

	public void setAktiboaDago(boolean aktiboaDago) {
		this.aktiboaDago = aktiboaDago;
	}
	
	/**
     * Taldearen ezkutua aldatzen du.
     * 
     * Ezkutu berria egungoa ez bada soilik eguneratzen da.
     *
     * @param ezkutua ezkutu berriaren identifikatzailea edo bidea
     */
	public void aldatuEzkutua(String ezkutua) {
		if(this.ezkutua!=ezkutua) {
			this.ezkutua=ezkutua;
		}
	}
	
	/**
     * Jokalari berri bat taldean sartzen du.
     * 
     * Jokalari zerrenda hutsik badago, automatikoki hasieratzen da.
     * 
     * @param j gehitu nahi den jokalaria
     */
	public void sartuJokalaria (Jokalari j) {
		if (this.jokalariak == null) {
            this.jokalariak = new ArrayList<>();
        }
        this.jokalariak.add(j);
	}
	@Override
	public String toString() {
	    return this.izena; 
	}
	@Override
	public int hashCode() {
		return Objects.hash(izena);
	}
	
	/**
     * Bi talde berdinak diren konparatzen du.
     * 
     * Taldeak berdintzat hartzen dira izen bera badute.
     * 
     * @param obj konparatu beharreko objektua
     * @return {@code true} berdinak badira; bestela {@code false}
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Talde other = (Talde) obj;
		return Objects.equals(izena, other.izena);
	}

	/**
     * Talde honen kopia sakona sortzen du.
     * 
     * Jokalari guztiak ere banan-banan kopiatzen dira,
     * jatorrizko taldearekin erreferentziak partekatu gabe.
     * 
     * @return taldearen kopia berria
     */
	public Talde kopiatu() {
	    // 1. Sortu zerrenda berri eta huts bat jokalarientzat
	    ArrayList<Jokalari> jokalariKopiak = new ArrayList<>();
	    
	    // 2. Jatorrizko taldeko jokalari bakoitza kopiatu eta zerrenda berrira gehitu
	    if (this.jokalariak != null) {
	        for (Jokalari j : this.jokalariak) {
	            jokalariKopiak.add(j.kopiatu()); // HEMEN DAGO GAKOA
	        }
	    }
	    
	    // 3. Talde berria itzuli, jokalari zerrenda BERRIAREKIN
	    return new Talde(
	        this.izena,
	        this.ezkutua,
	        this.futbolZelaia,
	        jokalariKopiak,
	        this.hiria,
	        this.aktiboaDago
	    );
	}
}
