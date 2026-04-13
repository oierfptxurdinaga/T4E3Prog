package model;

import javax.persistence.Entity;

/**
 * Presidente motako erabiltzailea.
 * Sisteman pribilegio gehienak dituen profila da.
 */
@Entity
public class ErabiltzailePresi extends Erabiltzaile {

    /** Serializaziorako bertsioa. */
    private static final long serialVersionUID = 1L;

    /**
     * Presidente erabiltzaile berri bat sortzen du.
     *
     * @param erabiltzaile erabiltzaile-izena
     * @param pasahitza    pasahitza
     */
    public ErabiltzailePresi(String erabiltzaile, String pasahitza) {
        super(erabiltzaile, pasahitza);
    }

    /** Eraikitzaile hutsa. */
    public ErabiltzailePresi() {}

    /**
     * Erabiltzaile-izena itzultzen du.
     *
     * @return erabiltzaile-izena
     */
    @Override
    public String getErabiltzaile() { return erabiltzaile; }

    /**
     * Erabiltzaile-izena ezartzen du.
     *
     * @param erabiltzaile erabiltzaile-izen berria
     */
    @Override
    public void setErabiltzaile(String erabiltzaile) { this.erabiltzaile = erabiltzaile; }

    /**
     * Pasahitza itzultzen du.
     *
     * @return pasahitza
     */
    @Override
    public String getPasahitza() { return pasahitza; }

    /**
     * Pasahitza ezartzen du.
     *
     * @param pasahitza pasahitz berria
     */
    @Override
    public void setPasahitza(String pasahitza) { this.pasahitza = pasahitza; }
}
