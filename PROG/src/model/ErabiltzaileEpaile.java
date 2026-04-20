package model;

import javax.persistence.Entity;

/**
 * Epaile motako erabiltzailea.
 * Jokatutako partiduen emaitzak sisteman sartzeko gaitasuna du.
 */
@Entity
public class ErabiltzaileEpaile extends Erabiltzaile {

    /** Serializaziorako bertsioa. */
    private static final long serialVersionUID = 1L;

    /**
     * Epaile erabiltzaile berri bat sortzen du.
     *
     * @param erabiltzaile erabiltzaile-izena
     * @param pasahitza    pasahitza
     */
    public ErabiltzaileEpaile(String erabiltzaile, String pasahitza) {
        super(erabiltzaile, pasahitza);
    }

    /** Eraikitzaile hutsa. */
    public ErabiltzaileEpaile() {}

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
     * Partida baten emaitza sisteman erregistratzen du.
     * Taldeak eta denboraldia bilatuta, dagokion partiduaren golak ezartzen ditu.
     *
     * @param denboraldia   emaitza sartu nahi den denboraldia
     * @param etxekoTaldea  etxean jokatzen duen taldea
     * @param kanpokoTaldea kanpoan jokatzen duen taldea
     * @param etxekoGolak   etxeko taldeak sartutako golak
     * @param kanpokoGolak  kanpoko taldeak sartutako golak
     */
    public void sartuEmaitza(Denboraldia denboraldia, Talde etxekoTaldea, Talde kanpokoTaldea,
            int etxekoGolak, int kanpokoGolak) {
        for (Jardunaldi element : denboraldia.getLigakoJardunaldi()) {
            for (Partidua element2 : element.getPartiduak()) {
                if (element2.getEtxekoTaldea().equals(etxekoTaldea)
                        && element2.getKanpokoTaldea().equals(kanpokoTaldea)) {
                    element2.setEtxekoGolak(etxekoGolak);
                    element2.setKanpokoGolak(kanpokoGolak);
                    return;
                }
            }
        }
    }
}
