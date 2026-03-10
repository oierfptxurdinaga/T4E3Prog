package utils;

import java.io.*;
import java.util.ArrayList;
import model.*;

public class DatuKarga {

    // Fitxategien bideak (Rutas de los archivos)
    private static final String ERABILTZAILE_PATH = "src/data/erabiltzaileak.ser";
    // Orain Federazioa fitxategira apuntatzen dugu (DatuKargatzailea-n jarri duzun izen bera)
    private static final String FEDERAZIOA_PATH = "src/data/federazioa.ser";

    /**
     * Erabiltzaileak .ser fitxategitik kargatzen ditu.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Erabiltzaile> kargatuErabiltzaileak() {
        ArrayList<Erabiltzaile> zerrenda = new ArrayList<>();
        File fitxategia = new File(ERABILTZAILE_PATH);

        if (fitxategia.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fitxategia))) {
                zerrenda = (ArrayList<Erabiltzaile>) ois.readObject();
                // System.out.println("Erabiltzaileak zuzen kargatu dira.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Errorea erabiltzaileak kargatzean: " + e.getMessage());
            }
        } else {
            System.out.println("Ez da 'erabiltzaileak.ser' aurkitu.");
        }
        return zerrenda;
    }

    /**
     * Federazioa (eta bere barruan denboraldiak/taldeak) kargatzen ditu.
     * Fitxategia existitzen ez bada, 'null' itzultzen du.
     */
    public static Federazioa kargatuFederazioa() {
        Federazioa federazioa = null;
        File fitxategia = new File(FEDERAZIOA_PATH);

        if (fitxategia.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fitxategia))) {
                federazioa = (Federazioa) ois.readObject();
                // System.out.println("Federazioa eta datuak zuzen kargatu dira.");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Errorea Federazioa kargatzean: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Ez da 'federazioa.ser' aurkitu. DatuKargatzailea exekutatu duzu?");
        }
        return federazioa;
    }

    /**
     * Federazioaren egoera gordetzen du fitxategian (.ser).
     * Metodo hau deitu behar da denboraldi berri bat sortzen denean.
     */
    public static void gordeFederazioa(Federazioa federazioa) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FEDERAZIOA_PATH))) {
            oos.writeObject(federazioa);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}