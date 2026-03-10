package model;

import java.io.Serializable;

/**
 * Erabiltzaile orokor bat irudikatzen duen klase abstraktua.
 * Sistema erabiltzen duten profil guztien (Administraria, Epailea, Presidentea)
 * oinarrizko atributuak eta metodoak definitzen ditu.
 * 
 * {@link Serializable} interfazea inplementatzen du, objektuak fitxategietan
 * gorde eta berreskuratu ahal izateko.
 */
public abstract class Erabiltzaile implements Serializable {

	/** Objektuaren bertsioa serializazioan kontrolatzeko identifikatzailea */
	private static final long serialVersionUID = 1L;
	
	protected String erabiltzaile;
    protected String pasahitza;

    /**
     * Erabiltzaile berri bat sortzen du emandako datuekin.
     *
     * @param erabiltzaile erabiltzailearen izena
     * @param pasahitza erabiltzailearen pasahitza
     */
    public Erabiltzaile(String erabiltzaile, String pasahitza) {
        this.erabiltzaile = erabiltzaile;
        this.pasahitza = pasahitza;
    }

    public String getErabiltzaile() { return this.erabiltzaile; }
    public String getPasahitza() { return this.pasahitza; }

	public abstract void setErabiltzaile(String string);

	public abstract void setPasahitza(String string);   
}