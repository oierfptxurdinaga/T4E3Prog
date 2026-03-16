package model;

import java.io.Serializable;
import jakarta.xml.bind.annotation.*;

/**
 * Futbol edo kirol jokalari bat irudikatzen duen klasea.
 * 
 * Jokalariak izena, abizena, jaiotze urtea, dortsal zenbakia, posizioa
 * eta denboraldi batean aktiboa dagoen ala ez gordetzen ditu.
 * 
 * Gainera, jokalari baten kopia sortzeko eta irudi automatiko bat sortzeko
 * metodoak eskaintzen ditu.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Jokalari implements Serializable{

	/** Objektuaren bertsioa serializazioan kontrolatzeko identifikatzailea */
	private static final long serialVersionUID = 1L;
	
	@XmlAttribute(name = "Dortsala")
	private int dortsala;
	
	@XmlElement(name = "Izena")
	private String izena;
	
	@XmlElement(name = "Abizena")
	private String abizena;
	
	@XmlElement(name = "Jaiotze_urtea")
	private int jaiotzeUrtea;

	@XmlElement(name = "Posizioa")
	private String posizio;
	
	@XmlElement(name= "Aktiboa_dago")
	private boolean aktiboaDago;
	
	public Jokalari(String izena, String abizena, int jaiotseUrtea, int dortsala, String posizio, boolean aktiboaDago) {
		this.izena = izena;
		this.abizena = abizena;
		this.jaiotzeUrtea = jaiotseUrtea;
		this.dortsala = dortsala;
		this.posizio = posizio;
		this.aktiboaDago = aktiboaDago;
	}
	
	public Jokalari() {
		super();
	}

	// Getterrak eta setterrak
	public String getIzena() {
		return izena;
	}
	public void setIzena(String izena) {
		this.izena = izena;
	}
	public String getAbizena() {
		return abizena;
	}
	public void setAbizena(String abizena) {
		this.abizena = abizena;
	}
	public int getJaiotzeUrtea() {
		return jaiotzeUrtea;
	}
	public void setJaiotzeUrtea(int adina) {
		this.jaiotzeUrtea = adina;
	}
	public int getDortsala() {
		return dortsala;
	}
	public void setDortsala(int dortsala) {
		this.dortsala = dortsala;
	}
	public String getPosizio() {
		return posizio;
	}
	public void setPosizio(String posizio) {
		this.posizio = posizio;
	}
	public boolean isAktiboaDago() {
		return aktiboaDago;
	}
	public void setAktiboaDago(boolean aktiboaDago) {
		this.aktiboaDago = aktiboaDago;
	}
	
	/**
     * Jokalari honen kopia sortzen du.
     * 
     * Joko edo liga kudeaketetan aldagai originala aldatu gabe
     * kopia bat erabiltzeko erabilgarria.
     *
     * @return jokalari honen kopia berria
     */
	public Jokalari kopiatu() {
	    return new Jokalari(
	        this.izena, 
	        this.abizena, 
	        this.jaiotzeUrtea, 
	        this.dortsala, 
	        this.posizio, 
	        this.aktiboaDago
	    );
	}
	
	/**
     * Jokalariaren irudi automatikoaren URL bat sortzen du.
     * 
     * Urte bateko "seed"-aren arabera irudia generatzen da
     * Dicebear API erabiliz.
     *
     * @param urtea urtea, irudiaren seed-era gehitzeko
     * @return jokalariaren irudiaren URL-a
     */
	public String getIrudiaUrl(int urtea) {
	     String seed = this.izena.replaceAll(" ", "") + urtea;
	     return "https://api.dicebear.com/7.x/avataaars/png?seed=" + seed;
	}
}
