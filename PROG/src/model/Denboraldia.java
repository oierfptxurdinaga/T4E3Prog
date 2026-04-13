package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

/**
 * Liga bateko denboraldi bat irudikatzen duen klasea.
 * Denboraldiak urte bati lotuta daude eta ligako taldeak eta jardunaldiak gordetzen ditu.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Denboraldia implements Serializable {

    /** Serializaziorako bertsioa. */
    private static final long serialVersionUID = 1L;

    /** Denboraldiaren urtea. */
    @XmlAttribute(name = "urtea")
    private int urtea;

    /** Denboraldian parte hartzen duten taldeen zerrenda. */
    @XmlElementWrapper(name = "DenboraldikoTaldeak")
    @XmlElement(name = "Talde")
    private ArrayList<DenboraldiTalde> ligakoTaldeak;

    /** Denboraldiko jardunaldien zerrenda. */
    @XmlElementWrapper(name = "Jardunaldiak")
    @XmlElement(name = "Jardunaldi")
    private ArrayList<Jardunaldi> ligakoJardunaldi;

    /** JAXB-rako eraikitzaile hutsa. */
    public Denboraldia() {}

    /**
     * Denboraldi berri bat sortzen du urte batekin.
     *
     * @param urtea denboraldiaren urtea
     */
    public Denboraldia(int urtea) {
        this.urtea = urtea;
        this.ligakoTaldeak = new ArrayList<>();
        this.ligakoJardunaldi = new ArrayList<>();
    }

    /**
     * Denboraldiaren urtea itzultzen du.
     *
     * @return urtea
     */
    public int getUrtea() { return urtea; }

    /**
     * Denboraldiko taldeen zerrenda itzultzen du.
     *
     * @return taldeen zerrenda
     */
    public ArrayList<DenboraldiTalde> getLigakoTaldeak() { return ligakoTaldeak; }

    /**
     * Denboraldiko taldeen zerrenda ezartzen du.
     *
     * @param ligakoTaldeak talde berrien zerrenda
     */
    public void setLigakoTaldeak(ArrayList<DenboraldiTalde> ligakoTaldeak) { this.ligakoTaldeak = ligakoTaldeak; }

    /**
     * Denboraldiko jardunaldien zerrenda itzultzen du.
     *
     * @return jardunaldien zerrenda
     */
    public ArrayList<Jardunaldi> getLigakoJardunaldi() { return ligakoJardunaldi; }

    /**
     * Denboraldiko jardunaldien zerrenda ezartzen du.
     *
     * @param ligakoJardunaldi jardunaldi berrien zerrenda
     */
    public void setLigakoJardunaldi(ArrayList<Jardunaldi> ligakoJardunaldi) { this.ligakoJardunaldi = ligakoJardunaldi; }

    /**
     * Jardunaldi bat zerrendara gehitzen du.
     *
     * @param j gehitu nahi den jardunaldia
     */
    public void addJardunaldia(Jardunaldi j) { this.ligakoJardunaldi.add(j); }

    /**
     * DenboraldiTalde bat zerrendara gehitzen du.
     * Zerrenda null bada, automatikoki sortzen da.
     *
     * @param dt gehitu nahi den denboraldi-taldea
     */
    public void gehituDenboraldiTaldea(DenboraldiTalde dt) {
        if (this.ligakoTaldeak == null) {
            this.ligakoTaldeak = new ArrayList<>();
        }
        this.ligakoTaldeak.add(dt);
    }

    /**
     * Talde arrunt bat DenboraldiTalde bihurtu eta zerrendara gehitzen du.
     *
     * @param t gehitu nahi den taldea
     */
    public void gehituTaldea(Talde t) {
        this.gehituDenboraldiTaldea(new DenboraldiTalde(t, true));
    }

    /**
     * Denboraldia hasita dagoen edo ez egiaztatzen du.
     * Gutxienez partida bat jokatu bada, hasitatzat jotzen da.
     *
     * @return true denboraldia hasita badago
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
     * Denboraldia amaituta dagoen edo ez egiaztatzen du.
     * Partida guztiak jokatu badira, amaitutzat jotzen da.
     *
     * @return true denboraldia amaituta badago
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
     * isHasiDa()-ren baliokidea, PanelAdmin-ekin bateragarritasunerako.
     *
     * @return true denboraldia hasita badago
     */
    public boolean isDenboraldiaHasiDa() {
        return isHasiDa();
    }

    /**
     * Zenbaki baten arabera jardunaldia itzultzen du.
     *
     * @param zenbakia jardunaldiaren zenbakia (1etik aurrera)
     * @return jardunaldia, edo null ez bada existitzen
     */
    public Jardunaldi getJardunaldiID(int zenbakia) {
        if (zenbakia > 0 && zenbakia <= ligakoJardunaldi.size()) {
            return ligakoJardunaldi.get(zenbakia - 1);
        }
        return null;
    }

    /**
     * Denboraldiaren urtea kate gisa itzultzen du.
     *
     * @return urtea String formatuan
     */
    @Override
    public String toString() {
        return String.valueOf(urtea);
    }

    /**
     * Jokatutako partiduen emaitzetan oinarrituta sailkapena kalkulatzen du.
     *
     * @return taldeen sailkapena DenboraldiTalde zerrenda gisa
     */
    public ArrayList<DenboraldiTalde> getSailkapena() {
        Map<String, DenboraldiTalde> statsMap = new HashMap<>();

        if (this.ligakoTaldeak != null) {
            for (DenboraldiTalde dt : this.ligakoTaldeak) {
                Talde t = dt.getTalde();
                statsMap.put(t.getIzena().trim(), new DenboraldiTalde(t, true));
            }
        }

        if (this.ligakoJardunaldi != null) {
            for (Jardunaldi j : this.ligakoJardunaldi) {
                if (j.getPartiduak() != null) {
                    for (Partidua p : j.getPartiduak()) {
                        if (!p.jokatutaDago()) {
							continue;
						}

                        String localNom = p.getEtxekoTaldea().getIzena().trim();
                        String visitNom = p.getKanpokoTaldea().getIzena().trim();

                        DenboraldiTalde sLocal = statsMap.get(localNom);
                        DenboraldiTalde sVisit = statsMap.get(visitNom);

                        if (sLocal != null && sVisit != null) {
                            sLocal.emaitzakEguneratu(p.getEtxekoGolak(), p.getKanpokoGolak());
                            sVisit.emaitzakEguneratu(p.getKanpokoGolak(), p.getEtxekoGolak());
                        }
                    }
                }
            }
        }
        return new ArrayList<>(statsMap.values());
    }

    /**
     * Partida bat jardunaldi jakin batean gehitzen du.
     * Jardunaldia existitzen ez bada, automatikoki sortzen da.
     *
     * @param jardunaldiZenbakia partida gehitu nahi den jardunaldiaren zenbakia
     * @param p gehitu nahi den partida
     */
    public void gehituPartiduaJardunaldira(int jardunaldiZenbakia, Partidua p) {
        if (this.ligakoJardunaldi == null) {
            this.ligakoJardunaldi = new ArrayList<>();
        }

        Jardunaldi aurkitutakoa = null;

        for (Jardunaldi j : this.ligakoJardunaldi) {
            if (j.getJardunaldiZbk() == jardunaldiZenbakia) {
                aurkitutakoa = j;
                break;
            }
        }

        if (aurkitutakoa == null) {
            aurkitutakoa = new Jardunaldi(jardunaldiZenbakia);
            this.ligakoJardunaldi.add(aurkitutakoa);
        }

        aurkitutakoa.getPartiduak().add(p);
    }
}
