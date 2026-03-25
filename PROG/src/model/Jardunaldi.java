package model;

import java.io.Serializable;
import java.util.ArrayList;
import jakarta.xml.bind.annotation.*;

/**
 * Liga edo denboraldi bateko jardunaldi bat irudikatzen duen klasea.
 * 
 * Jardunaldiak zenbaki baten bidez identifikatzen dira eta barnean
 * partida guztiak gordetzen ditu.
 * 
 * Partidak gehitzeko eta kudeatzeko metodoak eskaintzen ditu.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Jardunaldi implements Serializable{

	/** Objektuaren bertsioa serializazioan kontrolatzeko identifikatzailea */
	private static final long serialVersionUID = 1L;
	
	private int id;
	@XmlAttribute(name = "zenbakia")
	private int jardunaldiZbk;
	
	@XmlElement(name = "Partidua")
	private ArrayList<Partidua> partiduak;
	
	public Jardunaldi() {}
	
	public Jardunaldi(int jardunaldiZbk, ArrayList<Partidua> partiduak, int id) {
		this.id = id;
		this.jardunaldiZbk = jardunaldiZbk;
		this.partiduak = partiduak;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Jardunaldi(int jardunaldiZbk) {
        this.jardunaldiZbk = jardunaldiZbk;
        this.partiduak = new ArrayList<>(); 
    }
	
	//getters and setters
	public int getJardunaldiZbk() {
		return jardunaldiZbk;
	}
	public void setJardunaldiZbk(int jardunaldiZbk) {
		this.jardunaldiZbk = jardunaldiZbk;
	}
	public ArrayList<Partidua> getPartiduak() {
		return partiduak;
	}
	public void setPartiduak(ArrayList<Partidua> partiduak) {
		this.partiduak = partiduak;
	}

	/**
     * Partida berri bat jardunaldian gehitzen du.
     * 
     * Barneko lista automatikoki sortzen da {@code null} bada.
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
