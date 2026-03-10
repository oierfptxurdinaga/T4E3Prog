package utils;

import java.io.*;
import java.util.*;
import model.*;

public class DatuKargatzailea {

    public static void main(String[] args) {
        System.out.println("Sistemaren datuak hasieratzen (Denboraldi osoa + Jokalari errealak 12 taldeentzat)...");

        File dataKarpeta = new File("src/data");
        if (!dataKarpeta.exists()) {
            dataKarpeta.mkdirs();
        }

        // 1. FEDERAZIOA SORTU
        Federazioa federazioa = new Federazioa();

        // 2. TALDEAK SORTU
        Map<Integer, Talde> taldeMapa = sortuTaldeak();

        // 3. JOKALARIAK KARGATU (12 TALDEEN DATU ERREALAK)
        kargatuJokalariakErrealak(taldeMapa);

        // 4. TALDE GUZTIAK FEDERAZIOAN GORDE
        for (Talde t : taldeMapa.values()) {
            federazioa.gehituTaldea(t);
        }


        Denboraldia denboraldia = new Denboraldia(2024);

        for (int i = 1; i <= 6; i++) {
            denboraldia.gehituTaldea(taldeMapa.get(i));
        }

        // 6. JARDUNALDIAK KARGATU (10 JARDUNALDI)
        kargatuPartiduakOsoak(denboraldia, taldeMapa);

        federazioa.gehituDenboraldia(denboraldia);

        // 7. ERABILTZAILEAK
        federazioa.getErabiltzaileak().add(new ErabiltzaileAdministraria("admin", "admin"));
        federazioa.getErabiltzaileak().add(new ErabiltzaileEpaile("epaile", "epaile"));
        federazioa.getErabiltzaileak().add(new ErabiltzailePresi("presi", "presi"));

        // 8. DATUAK GORDE
        gordeObjektua(federazioa, "src/data/federazioa.ser");
        
        System.out.println("Datuak ondo sortu eta gorde dira.");
    }

    private static Map<Integer, Talde> sortuTaldeak() {
        Map<Integer, Talde> taldeak = new HashMap<>();
        // ID, Izena, Irudia, Estadioa, JokalariZerrenda, Hiria, Aktibo
        Object[][] datuak = {
            { 1, "Barça Futsal", "/resources/images/barcelona.png", "Palau Blaugrana", new ArrayList<Jokalari>(), "Barcelona", true },
            { 2, "ElPozo Murcia", "/resources/images/elpozo.png", "Palacio de Deportes", new ArrayList<Jokalari>(), "Murcia", true },
            { 3, "Inter Movistar", "/resources/images/inter.png", "Jorge Garbajosa", new ArrayList<Jokalari>(), "Torrejon", true },
            { 4, "Mallorca Palma Futsal", "/resources/images/baleares.png", "Son Moix", new ArrayList<Jokalari>(), "Palma", true },
            { 5, "Jaén Paraíso Interior", "/resources/images/jaen.png", "Olivo Arena", new ArrayList<Jokalari>(), "Jaen", true },
            { 6, "Viña Albali Valdepeñas", "/resources/images/valdepenas.png", "Virgen de la Cabeza", new ArrayList<Jokalari>(), "Valdepeñas", true },
            { 7, "Jimbee Cartagena", "/resources/images/cartagena.png", "Palacio Deportes", new ArrayList<Jokalari>(), "Cartagena", true },
            { 8, "Aspil-Jumpers Ribera", "/resources/images/ribera.png", "Ciudad de Tudela", new ArrayList<Jokalari>(), "Tudela", true },
            { 9, "Industrias Santa Coloma", "/resources/images/industrias.png", "Pavelló Nou", new ArrayList<Jokalari>(), "Sta Coloma", true },
            { 10, "Xota FS", "/resources/images/xota.png", "Anaitasuna", new ArrayList<Jokalari>(), "Pamplona", true },
            { 11, "Córdoba Patrimonio", "/resources/images/cordoba.png", "Vista Alegre", new ArrayList<Jokalari>(), "Córdoba", true },
            { 12, "Noia Portus Apostoli", "/resources/images/noia.png", "Agustín Mourís", new ArrayList<Jokalari>(), "Noia", true }
        };

        for (Object[] d : datuak) {
            Talde t = new Talde((String) d[1], (String) d[2], (String) d[3], (ArrayList<Jokalari>) d[4], (String) d[5], (boolean) d[6]);
            taldeak.put((Integer) d[0], t);
        }
        return taldeak;
    }

    private static void kargatuJokalariakErrealak(Map<Integer, Talde> taldeMapa) {
        // Formato: { ID_TALDEA, IZENA, ABIZENA, DORTSALA, POSIZIOA }
        Object[][] jokalariak = {
            // --- 1. BARÇA ---
            { 1, "Sergio", "Lozano", 9, "Hegala" }, { 1, "Jean Pierre", "Pito", 10, "Pibota" },
            { 1, "Adolfo", "Fernández", 8, "Hegala" }, { 1, "Dídac", "Plana", 21, "Atezaina" },
            { 1, "Matheus", "Rodrigues", 3, "Hegala" }, { 1, "Miquel", "Feixas", 26, "Atezaina" },
            { 1, "Antonio", "Pérez", 2, "Itxiera" }, { 1, "André", "Coelho", 4, "Itxiera" },
            { 1, "Alex", "Yepes", 11, "Pibota" }, { 1, "Erick", "Mendonça", 99, "Unibertsala" },

            // --- 2. ELPOZO MURCIA ---
            { 2, "Rafa", "Santos", 11, "Pibota" }, { 2, "Felipe", "Valerio", 5, "Itxiera" },
            { 2, "Gadeia", "Fabricio", 13, "Hegala" }, { 2, "Juanjo", "Angosto", 12, "Atezaina" },
            { 2, "Marcel", "Marques", 10, "Hegala" }, { 2, "Edu", "Sousa", 1, "Atezaina" },
            { 2, "Bruno", "Taffy", 9, "Pibota" }, { 2, "Artem", "Niyazov", 96, "Hegala" },
            { 2, "David", "Álvarez", 8, "Hegala" }, { 2, "Ricardo", "Mayor", 4, "Itxiera" },

            // --- 3. INTER MOVISTAR ---
            { 3, "Jesús", "Herrero", 1, "Atezaina" }, { 3, "Raúl", "Gómez", 8, "Hegala" },
            { 3, "Cecilio", "Morales", 2, "Itxiera" }, { 3, "Lucas", "Tripodi", 3, "Hegala" },
            { 3, "Fits", "Rafael", 12, "Pibota" }, { 3, "Javi", "Mínguez", 6, "Hegala" },
            { 3, "Tomás", "Drahovsky", 10, "Pibota" }, { 3, "Jhonatan", "Linhares", 14, "Itxiera" },
            { 3, "Humberto", "Dalmata", 7, "Hegala" }, { 3, "Kaito", "Yamada", 19, "Hegala" },

            // --- 4. PALMA FUTSAL ---
            { 4, "Luan", "Muller", 1, "Atezaina" }, { 4, "Cleber", "Gomes", 10, "Hegala" },
            { 4, "Mario", "Rivillos", 8, "Hegala" }, { 4, "Moslem", "Oladghobad", 9, "Hegala" },
            { 4, "Bruno", "Gomes", 11, "Pibota" }, { 4, "Carlos", "Barrón", 21, "Atezaina" },
            { 4, "Hossein", "Tayebi", 15, "Pibota" }, { 4, "Chaguinha", "Bruno", 2, "Itxiera" },
            { 4, "Fabinho", "Teixeira", 17, "Hegala" }, { 4, "Rómulo", "Alves", 5, "Itxiera" },

            // --- 5. JAEN PARAISO ---
            { 5, "Alan", "Brandi", 10, "Pibota" }, { 5, "Mati", "Rosa", 41, "Pibota" },
            { 5, "Chino", "Javier", 20, "Hegala" }, { 5, "Michel", "Moyano", 21, "Hegala" },
            { 5, "César", "Velasco", 8, "Hegala" }, { 5, "Espindola", "Carlos", 2, "Atezaina" },
            { 5, "Pablo", "Taborda", 14, "Itxiera" }, { 5, "Renato", "Lopes", 12, "Hegala" },
            { 5, "Helder", "Seminario", 29, "Hegala" }, { 5, "Dudú", "Rodríguez", 15, "Atezaina" },

            // --- 6. VIÑA ALBALI VALDEPEÑAS ---
            { 6, "Boyis", "Antonio", 4, "Itxiera" }, { 6, "Pol", "Pacheco", 10, "Hegala" },
            { 6, "Eric", "Martel", 7, "Hegala" }, { 6, "Abassi", "Amin", 11, "Pibota" },
            { 6, "Lolo", "Manuel", 21, "Itxiera" }, { 6, "Marcao", "Marcio", 1, "Atezaina" },
            { 6, "Solano", "Francisco", 9, "Pibota" }, { 6, "Bynho", "Ferraz", 17, "Hegala" },
            { 6, "Eloy", "Rojas", 8, "Hegala" }, { 6, "Nano", "David", 23, "Itxiera" },

            // --- 7. JIMBEE CARTAGENA ---
            { 7, "Mellado", "Miguel", 13, "Hegala" }, { 7, "Lucao", "Lucas", 12, "Hegala" },
            { 7, "Waltinho", "Walter", 11, "Pibota" }, { 7, "Bebe", "Rafael", 4, "Itxiera" },
            { 7, "Chemi", "José", 1, "Atezaina" }, // Jarri dugu Atezaina bezala (nahiz eta SQLn ez zehaztu)
            { 7, "Tomaz", "Braga", 2, "Itxiera" }, { 7, "Pablo", "Ramírez", 9, "Pibota" },
            { 7, "Juanan", "Sánchez", 21, "Pibota" }, { 7, "Motta", "Felipe", 3, "Hegala" },
            { 7, "Darío", "Gil", 16, "Hegala" },

            // --- 8. ASPIL-JUMPERS RIBERA ---
            { 8, "Terry", "Prestjord", 10, "Hegala" }, { 8, "David", "García", 5, "Itxiera" },
            { 8, "Pintinho", "Gabriel", 17, "Hegala" }, { 8, "Carlos", "Bartolomé", 2, "Hegala" },
            { 8, "Nacho", "Gómez", 21, "Itxiera" }, { 8, "Adrián", "Pereira", 1, "Atezaina" },
            { 8, "Uge", "Eugenio", 14, "Hegala" }, { 8, "Gabi", "Vasques", 7, "Hegala" },
            { 8, "Claudino", "Angel", 23, "Hegala" }, { 8, "Petry", "João", 9, "Pibota" },

            // --- 9. INDUSTRIAS SANTA COLOMA ---
            { 9, "Khalid", "Bouzid", 2, "Hegala" }, { 9, "Cardona", "David", 7, "Hegala" },
            { 9, "Verdejo", "Víctor", 10, "Hegala" }, { 9, "Corso", "Sebastián", 5, "Itxiera" },
            { 9, "Povill", "Bernat", 14, "Hegala" }, { 9, "Borja", "Puerta", 1, "Atezaina" },
            { 9, "Uri", "Santos", 9, "Pibota" }, { 9, "Marc", "Tolrà", 4, "Itxiera" },
            { 9, "Nil", "Closas", 6, "Hegala" }, { 9, "Hirata", "Neto", 11, "Pibota" },

            // --- 10. XOTA FS ---
            { 10, "Asier", "Llamas", 1, "Atezaina" }, { 10, "Linhares", "Juninho", 11, "Hegala" },
            { 10, "Geraghty", "Tony", 14, "Hegala" }, { 10, "Fabinho", "Silva", 17, "Hegala" },
            { 10, "Dani", "Zurdo", 10, "Hegala" }, { 10, "Roberto", "Martil", 5, "Itxiera" },
            { 10, "Ion", "Cerviño", 2, "Itxiera" }, { 10, "Leo", "Café", 8, "Hegala" },
            { 10, "Vento", "Alejandro", 21, "Hegala" }, { 10, "Raúl", "Jiménez", 9, "Pibota" },

            // --- 11. CÓRDOBA PATRIMONIO ---
            { 11, "Fabio", "Alvira", 1, "Atezaina" }, { 11, "Zequi", "Ezequiel", 7, "Hegala" },
            { 11, "Perin", "Lucas", 11, "Hegala" }, { 11, "Muhammad", "Osamanmusa", 9, "Pibota" },
            { 11, "Pulinho", "Victor", 10, "Hegala" }, { 11, "Víctor", "Arenas", 12, "Atezaina" },
            { 11, "Mykytiuk", "Mykola", 14, "Itxiera" }, { 11, "Kauê", "Monteiro", 8, "Hegala" },
            { 11, "Arnaldo", "Báez", 5, "Itxiera" }, { 11, "Kenji", "González", 2, "Itxiera" },

            // --- 12. NOIA PORTUS APOSTOLI ---
            { 12, "Henrique", "Rafagnin", 1, "Atezaina" }, { 12, "Power", "Raggiati", 4, "Itxiera" },
            { 12, "Altamirano", "Leandro", 10, "Hegala" }, { 12, "Pirata", "David", 7, "Hegala" },
            { 12, "Edu", "Jabá", 14, "Hegala" }, { 12, "Nico", "Sarmiento", 21, "Atezaina" },
            { 12, "Matheus", "Machado", 11, "Hegala" }, { 12, "Attos", "Mendes", 2, "Itxiera" },
            { 12, "Rufino", "García", 8, "Hegala" }, { 12, "David", "Pazos", 20, "Hegala" }
        };

        for (Object[] d : jokalariak) {
            int taldeId = (int) d[0];
            String izena = (String) d[1];
            String abizena = (String) d[2];
            int dortsala = (int) d[3];
            String posizioa = (String) d[4];

            Talde t = taldeMapa.get(taldeId);
            if (t != null) {
                // Urtea 1995 jarri diogu defektuz denei adibide honetarako
                Jokalari j = new Jokalari(izena, abizena, 1995, dortsala, posizioa, true);
                t.sartuJokalaria(j);
            }
        }
    }


        private static void kargatuPartiduakOsoak(Denboraldia d, Map<Integer, Talde> tMap) {
            // --- IDA (JORNADAS 1-5) ---
            
            // J1: JOKATUTA
            Jardunaldi j1 = new Jardunaldi(1);
            j1.addPartidua(sortuPartidua(tMap, 1, 6, 4, 2)); // Barça vs Valdepeñas
            j1.addPartidua(sortuPartidua(tMap, 2, 5, 3, 3)); // ElPozo vs Jaen
            j1.addPartidua(sortuPartidua(tMap, 3, 4, 5, 4)); // Inter vs Palma
            d.addJardunaldia(j1);

            // J2: JOKATUTA
            Jardunaldi j2 = new Jardunaldi(2);
            j2.addPartidua(sortuPartidua(tMap, 6, 4, 1, 2)); // Valdepeñas vs Palma
            j2.addPartidua(sortuPartidua(tMap, 5, 3, 2, 2)); // Jaen vs Inter
            j2.addPartidua(sortuPartidua(tMap, 1, 2, 3, 1)); // Barça vs ElPozo
            d.addJardunaldia(j2);

            // J3: JOKATUTA
            Jardunaldi j3 = new Jardunaldi(3);
            j3.addPartidua(sortuPartidua(tMap, 2, 6, 5, 0)); // ElPozo vs Valdepeñas
            j3.addPartidua(sortuPartidua(tMap, 3, 1, 2, 4)); // Inter vs Barça
            j3.addPartidua(sortuPartidua(tMap, 4, 5, 1, 1)); // Palma vs Jaen
            d.addJardunaldia(j3);

            // J4: JOKATUTA
            Jardunaldi j4 = new Jardunaldi(4);
            j4.addPartidua(sortuPartidua(tMap, 6, 5, 2, 3)); // Valdepeñas vs Jaen
            j4.addPartidua(sortuPartidua(tMap, 1, 4, 4, 4)); // Barça vs Palma
            j4.addPartidua(sortuPartidua(tMap, 2, 3, 3, 2)); // ElPozo vs Inter
            d.addJardunaldia(j4);

            // J5: JOKATUTA (Lehen itzuliaren amaiera)
            Jardunaldi j5 = new Jardunaldi(5);
            j5.addPartidua(sortuPartidua(tMap, 3, 6, 5, 2)); // Inter vs Valdepeñas
            j5.addPartidua(sortuPartidua(tMap, 4, 2, 3, 3)); // Palma vs ElPozo
            j5.addPartidua(sortuPartidua(tMap, 5, 1, 1, 4)); // Jaen vs Barça
            d.addJardunaldia(j5);

            // --- VUELTA (JORNADAS 6-10) --- 
            // Etxeko/Kanpoko alderantziz eta emaitza berriak
            
            // J6 (Vuelta de J1): JOKATUTA
            Jardunaldi j6 = new Jardunaldi(6);
            j6.addPartidua(sortuPartidua(tMap, 6, 1, 2, 5)); // Valdepeñas vs Barça
            j6.addPartidua(sortuPartidua(tMap, 5, 2, 1, 0)); // Jaen vs ElPozo
            j6.addPartidua(sortuPartidua(tMap, 4, 3, 4, 3)); // Palma vs Inter
            d.addJardunaldia(j6);

            // J7 (Vuelta de J2): JOKATUTA
            Jardunaldi j7 = new Jardunaldi(7);
            j7.addPartidua(sortuPartidua(tMap, 4, 6, 3, 1)); // Palma vs Valdepeñas
            j7.addPartidua(sortuPartidua(tMap, 3, 5, 4, 2)); // Inter vs Jaen
            j7.addPartidua(sortuPartidua(tMap, 2, 1, 2, 2)); // ElPozo vs Barça
            d.addJardunaldia(j7);

            // J8 (Vuelta de J3): JOKATUTA
            Jardunaldi j8 = new Jardunaldi(8);
            j8.addPartidua(sortuPartidua(tMap, 6, 2, 3, 4)); // Valdepeñas vs ElPozo
            j8.addPartidua(sortuPartidua(tMap, 1, 3, 6, 3)); // Barça vs Inter
            j8.addPartidua(sortuPartidua(tMap, 5, 4, 2, 2)); // Jaen vs Palma
            d.addJardunaldia(j8);

            // J9 (Vuelta de J4): JOKATUTA
            Jardunaldi j9 = new Jardunaldi(9);
            j9.addPartidua(sortuPartidua(tMap, 5, 6, 4, 1)); // Jaen vs Valdepeñas
            j9.addPartidua(sortuPartidua(tMap, 4, 1, 2, 3)); // Palma vs Barça
            j9.addPartidua(sortuPartidua(tMap, 3, 2, 1, 1)); // Inter vs ElPozo
            d.addJardunaldia(j9);

            // J10 (Vuelta de J5): JOKATUTA - Denboraldiaren amaiera
            Jardunaldi j10 = new Jardunaldi(10);
            j10.addPartidua(sortuPartidua(tMap, 6, 3, 2, 4)); // Valdepeñas vs Inter
            j10.addPartidua(sortuPartidua(tMap, 2, 4, 3, 2)); // ElPozo vs Palma
            j10.addPartidua(sortuPartidua(tMap, 1, 5, 5, 1)); // Barça vs Jaen
            d.addJardunaldia(j10);
        }

    private static Partidua sortuPartidua(Map<Integer, Talde> map, int idEtxekoa, int idKanpokoa, int golEtxekoa, int golKanpokoa) {
        Talde etxekoa = map.get(idEtxekoa);
        Talde kanpokoa = map.get(idKanpokoa);
        return new Partidua(etxekoa, kanpokoa, golEtxekoa, golKanpokoa);
    }

    private static void gordeObjektua(Object obj, String bidea) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(bidea))) {
            oos.writeObject(obj);
            System.out.println("Gordeta: " + bidea);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}