package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogKudeatzailea {

    // Nombre del archivo donde se guardará todo
    private static final String LOG_FITXATEGIA = "src/data/actividad.log";
    private static final DateTimeFormatter FORMATUA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Guarda un mensaje de información normal (INFO)
     */
    public static void gehituLog(String mezua) {
        idatzi("INFO", mezua);
    }

    /**
     * Guarda un mensaje de error (ERROR)
     */
    public static void gehituErrorea(String mezua) {
        idatzi("ERROR", mezua);
    }

    private static void idatzi(String mota, String mezua) {
        // Usamos try-with-resources para asegurar que se cierra el fichero
        // El 'true' en FileWriter es para hacer APPEND (no borrar lo anterior)
        try (FileWriter fw = new FileWriter(LOG_FITXATEGIA, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            String ordua = LocalDateTime.now().format(FORMATUA);
            // Formato: [2024-01-20 14:30:00] [INFO] Mensaje...
            out.println("[" + ordua + "] [" + mota + "] " + mezua);

        } catch (IOException e) {
            // Si falla el log, lo sacamos por consola como último recurso
            System.err.println("Errorea log-a idaztean: " + e.getMessage());
        }
    }
}