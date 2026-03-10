package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap; // <--- Hau gehitu
import java.util.Map;     // <--- Hau gehitu
import jakarta.xml.bind.annotation.*;

/**
 * Liga bateko denboraldi bat irudikatzen duen klasea.
 *
 * Denboraldiak urte bati lotuta daude, eta barnean
 * ligako taldeak eta jardunaldiak kudeatzen ditu.
 * Partiduen egoeraren arabera, denboraldia hasita
 * edo amaituta dagoen zehaztu daiteke.
 */

@XmlAccessorType(XmlAccessType.FIELD)
public class Denboraldia implements Serializable {

	/** Objektuaren bertsioa serializazioan kontrolatzeko identifikatzailea */
    private static final long serialVersionUID = 1L;
    
    @XmlAttribute(name = "urtea")
    private int urtea;
    
    @XmlElementWrapper(name = "DenboraldikoTaldeak")
    @XmlElement(name = "Talde")
    private ArrayList<Talde> ligakoTaldeak;
    
    @XmlElementWrapper(name = "Jardunaldiak")
    @XmlElement(name = "Jardunaldi")
    private ArrayList<Jardunaldi> ligakoJardunaldi;
    
    public Denboraldia() {}

    public Denboraldia(int urtea) {
        this.urtea = urtea;
        this.ligakoTaldeak = new ArrayList<>();
        this.ligakoJardunaldi = new ArrayList<>();
    }

    // Getterrak eta setterrak
    public int getUrtea() { return urtea; }
    public ArrayList<Talde> getLigakoTaldeak() { return ligakoTaldeak; }
    public void setLigakoTaldeak(ArrayList<Talde> ligakoTaldeak) { this.ligakoTaldeak = ligakoTaldeak; }
    public ArrayList<Jardunaldi> getLigakoJardunaldi() { return ligakoJardunaldi; }
    public void setLigakoJardunaldi(ArrayList<Jardunaldi> ligakoJardunaldi) { this.ligakoJardunaldi = ligakoJardunaldi; }
    
    public void addJardunaldia(Jardunaldi j) { this.ligakoJardunaldi.add(j); }
    public void gehituTaldea(Talde t) { this.ligakoTaldeak.add(t); }

    /**
     * Denboraldia hasita dagoen ala ez adierazten du.
     *
     * Gutxienez partida bat jokatu bada, denboraldia
     * hasitzat jotzen da, jardunaldia osorik amaitu
     * ez bada ere.
     *
     * @return {@code true} denboraldia hasita badago; bestela {@code false}
     */
    public boolean isHasiDa() {
        if (this.ligakoJardunaldi == null || this.ligakoJardunaldi.isEmpty()) {
            return false;
        }
        for (Jardunaldi j : this.ligakoJardunaldi) {
            if (j.getPartiduak() != null) {
                for (Partidua p : j.getPartiduak()) {
                    if (p.jokatutaDago()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Denboraldia amaituta dagoen ala ez adierazten du.
     *
     * Jardunaldi guztietako partida guztiak jokatu badira,
     * denboraldia amaitutzat hartzen da.
     *
     * @return {@code true} denboraldia amaituta badago; bestela {@code false}
     */
    public boolean isAmaituta() {
        if (this.ligakoJardunaldi == null || this.ligakoJardunaldi.isEmpty()) {
            return false;
        }
        for (Jardunaldi j : this.ligakoJardunaldi) {
            if (j.getPartiduak() != null) {
                for (Partidua p : j.getPartiduak()) {
                    if (!p.jokatutaDago()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    /**
     * PanelAdmin-ekin bateragarritasuna mantentzeko erabilitako metodoa.
     *
     * @return {@link #isHasiDa()} metodoaren emaitza
     */
    public boolean isDenboraldiaHasiDa() {
        return isHasiDa();
    }

    @Override
    public String toString() {
        return String.valueOf(urtea);
    }
    
    /**
     * Uneko sailkapena kalkulatzen du jokatutako partiduen emaitzetan oinarrituta.
     *
     * Talde bakoitzerako estatistikak hasieratzen dira eta
     * jokatutako partida bakoitzaren emaitzen arabera
     * eguneratzen dira.
     *
     * @return denboraldiko sailkapena adierazten duen zerrenda
     */
    public ArrayList<DenboraldiTalde> getSailkapena() {
        Map<String, DenboraldiTalde> statsMap = new HashMap<>();

        // 1. Taldeak hasieratu (0 puntu)
        if (this.ligakoTaldeak != null) {
            for (Talde t : this.ligakoTaldeak) {
                statsMap.put(t.getIzena().trim(), new DenboraldiTalde(t, true));
            }
        }

        // 2. Partiduak prozesatu eta puntuak batu
        if (this.ligakoJardunaldi != null) {
            for (Jardunaldi j : this.ligakoJardunaldi) {
                if (j.getPartiduak() != null) {
                    for (Partidua p : j.getPartiduak()) {
                        // Jokatu gabe badago, hurrengoa
                        if (!p.jokatutaDago()) continue;

                        String localNom = p.getEtxekoTaldea().getIzena().trim();
                        String visitNom = p.getKanpokoTaldea().getIzena().trim();

                        DenboraldiTalde sLocal = statsMap.get(localNom);
                        DenboraldiTalde sVisit = statsMap.get(visitNom);

                        if (sLocal != null && sVisit != null) {
                            // DenboraldiTalde klaseko metodoa erabili datuak eguneratzeko
                            sLocal.emaitzakEguneratu(p.getEtxekoGolak(), p.getKanpokoGolak());
                            sVisit.emaitzakEguneratu(p.getKanpokoGolak(), p.getEtxekoGolak());
                        }
                    }
                }
            }
        }
        // Zerrenda itzuli
        return new ArrayList<>(statsMap.values());
    }
}