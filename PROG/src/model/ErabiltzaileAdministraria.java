package model;

import javax.persistence.Entity;
import javax.swing.JOptionPane;

/**
 * Administratzaile motako erabiltzailea.
 * Jokalariak talde batetik bestera transferitzeko gaitasuna du.
 */
@Entity
public class ErabiltzaileAdministraria extends Erabiltzaile {

    /** Serializaziorako bertsioa. */
    private static final long serialVersionUID = 1L;

    /**
     * Administratzaile erabiltzaile berri bat sortzen du.
     *
     * @param erabiltzaile erabiltzaile-izena
     * @param pasahitza    pasahitza
     */
    public ErabiltzaileAdministraria(String erabiltzaile, String pasahitza) {
        super(erabiltzaile, pasahitza);
    }

    /** Eraikitzaile hutsa. */
    public ErabiltzaileAdministraria() {
        super();
    }

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

    /**
     * Jokalari bat talde batetik bestera aldatzen du.
     * Denboraldia hasi bada, ez da aldaketarik onartzen.
     *
     * @param jokalari         aldatu nahi den jokalaria
     * @param taldeZaharra     jokalariak dagoen taldea
     * @param taldeBerria      jokalaria joan behar den taldea
     * @param denboraldiaHasiDa denboraldia hasita dagoen ala ez
     */
    public void aldatuJokalariak(Jokalari jokalari, Talde taldeZaharra, Talde taldeBerria, boolean denboraldiaHasiDa) {
        if (!denboraldiaHasiDa) {
            if (taldeZaharra.getJokalariak().contains(jokalari)) {
                taldeZaharra.getJokalariak().remove(jokalari);
                taldeBerria.getJokalariak().add(jokalari);
            } else {
                JOptionPane.showMessageDialog(null, "Jokalari hori ez dago talde horretan", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ezin dituzu jokalariak aldatu denboraldia hasi delako", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
}
