package utils;

import java.util.ArrayList;
import java.util.Collections;

import model.Jardunaldi;
import model.Partidua;
import model.Talde;

public class PartiduKudeatzailea {

    /**
     * Algoritmo "Round Robin" para generar liga de ida y vuelta
     */
    public static ArrayList<Jardunaldi> sortuEgutegia(ArrayList<Talde> taldeak) {
        ArrayList<Jardunaldi> egutegia = new ArrayList<>();

        // 1. Copiamos la lista para no modificar la original y la barajamos (Aleatorio)
        ArrayList<Talde> kopia = new ArrayList<>(taldeak);
        Collections.shuffle(kopia); // <-- ESTO LO HACE ALEATORIO

        int taldeKop = kopia.size();

        // Si es impar, añadimos un equipo "fantasma" (el que juegue contra él descansa)
        if (taldeKop % 2 != 0) {
            kopia.add(new Talde(0,"Deskantsua", null, null, null, null, false, null, 0));
            taldeKop++;
        }

        int jardunaldiKop = taldeKop - 1; // Jornadas por vuelta (N - 1)
        int partiduakJardunaldiko = taldeKop / 2;

        // --- JOANEKOA (IDA) ---
        for (int i = 0; i < jardunaldiKop; i++) {
            Jardunaldi j = new Jardunaldi(i + 1); // Jornada 1, 2, 3...

            for (int k = 0; k < partiduakJardunaldiko; k++) {
                // Algoritmo cíclico
                Talde etxekoa = kopia.get(k);
                Talde kanpokoa = kopia.get(taldeKop - 1 - k);

                // Ignoramos partidos contra el equipo fantasma (descanso)
                if (!etxekoa.getIzena().equals("Deskantsua") && !kanpokoa.getIzena().equals("Deskantsua")) {
                    // Creamos el partido (Goles a -1 indica no jugado)
                    j.addPartidua(new Partidua(etxekoa, kanpokoa, -1, -1));
                }
            }
            egutegia.add(j);

            // Rotar los equipos (dejando el primero fijo)
            Talde azkena = kopia.remove(kopia.size() - 1);
            kopia.add(1, azkena);
        }

        // --- ITZULIKOA (VUELTA) ---
        // Simplemente copiamos las jornadas de la ida pero invirtiendo local/visitante
        ArrayList<Jardunaldi> itzulikoak = new ArrayList<>();
        for (int i = 0; i < jardunaldiKop; i++) {
            Jardunaldi jIda = egutegia.get(i);
            Jardunaldi jVuelta = new Jardunaldi(jardunaldiKop + i + 1); // Jornada N, N+1...

            for (Partidua p : jIda.getPartiduak()) {
                // Invertimos: El de fuera ahora juega en casa
                jVuelta.addPartidua(new Partidua(p.getKanpokoTaldea(), p.getEtxekoTaldea(), -1, -1));
            }
            itzulikoak.add(jVuelta);
        }

        egutegia.addAll(itzulikoak); // Unimos ida y vuelta
        return egutegia;
    }
}