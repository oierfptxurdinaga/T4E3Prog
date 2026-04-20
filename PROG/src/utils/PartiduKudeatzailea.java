package utils;

import java.util.ArrayList;
import java.util.Collections;

import model.Jardunaldi;
import model.Partidua;
import model.Talde;

public class PartiduKudeatzailea {

    public static ArrayList<Jardunaldi> sortuEgutegia(ArrayList<Talde> taldeak) {
        ArrayList<Jardunaldi> egutegia = new ArrayList<>();
        ArrayList<Talde> kopia = new ArrayList<>(taldeak);
        Collections.shuffle(kopia);

        int taldeKop = kopia.size();
        if (taldeKop % 2 != 0) {
            kopia.add(new Talde(0,"Deskantsua", null, null, null, null, false, null, 0));
            taldeKop++;
        }

        int jardunaldiKop = taldeKop - 1;
        int partiduakJardunaldiko = taldeKop / 2;

        for (int i = 0; i < jardunaldiKop; i++) {
            Jardunaldi j = new Jardunaldi(i + 1);

            for (int k = 0; k < partiduakJardunaldiko; k++) {
                Talde etxekoa = kopia.get(k);
                Talde kanpokoa = kopia.get(taldeKop - 1 - k);

                if (!etxekoa.getIzena().equals("Deskantsua") && !kanpokoa.getIzena().equals("Deskantsua")) {
                    j.addPartidua(new Partidua(etxekoa, kanpokoa, -1, -1));
                }
            }
            egutegia.add(j);

            Talde azkena = kopia.remove(kopia.size() - 1);
            kopia.add(1, azkena);
        }

        ArrayList<Jardunaldi> itzulikoak = new ArrayList<>();
        for (int i = 0; i < jardunaldiKop; i++) {
            Jardunaldi jIda = egutegia.get(i);
            Jardunaldi jVuelta = new Jardunaldi(jardunaldiKop + i + 1); 

            for (Partidua p : jIda.getPartiduak()) {
                jVuelta.addPartidua(new Partidua(p.getKanpokoTaldea(), p.getEtxekoTaldea(), -1, -1));
            }
            itzulikoak.add(jVuelta);
        }

        egutegia.addAll(itzulikoak);
        return egutegia;
    }
}