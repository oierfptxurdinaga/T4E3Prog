package model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Erabiltzaile orokor bat irudikatzen duen klase abstraktua.
 * Administratzaile, epaile eta presidente profilen oinarrizko atributuak definitzen ditu.
 */
@Entity
public abstract class Erabiltzaile implements Serializable {

    /** Serializaziorako bertsioa. */
    private static final long serialVersionUID = 1L;

    /** Erabiltzailearen izena (ID nagusia). */
    @Id
    protected String erabiltzaile;

    /** Erabiltzailearen pasahitza. */
    protected String pasahitza;

    /** Eraikitzaile hutsa. */
    public Erabiltzaile() {}

    /**
     * Erabiltzaile berri bat sortzen du.
     *
     * @param erabiltzaile erabiltzailearen izena
     * @param pasahitza    erabiltzailearen pasahitza
     */
    public Erabiltzaile(String erabiltzaile, String pasahitza) {
        this.erabiltzaile = erabiltzaile;
        this.pasahitza = pasahitza;
    }

    /**
     * Erabiltzailearen izena itzultzen du.
     *
     * @return erabiltzaile-izena
     */
    public String getErabiltzaile() { return this.erabiltzaile; }

    /**
     * Pasahitza itzultzen du.
     *
     * @return pasahitza
     */
    public String getPasahitza() { return this.pasahitza; }

    /**
     * Erabiltzaile-izena ezartzen du.
     *
     * @param erabiltzaile erabiltzaile-izen berria
     */
    public abstract void setErabiltzaile(String erabiltzaile);

    /**
     * Pasahitza ezartzen du.
     *
     * @param pasahitza pasahitz berria
     */
    public abstract void setPasahitza(String pasahitza);
}
