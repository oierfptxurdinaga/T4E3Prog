package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogKudeatzailea {

    private static final String LOG_FITXATEGIA = "src/data/aktibitatea.log";
    private static final DateTimeFormatter FORMATUA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public static void gehituLog(String mezua) {
        idatzi("INFO", mezua);
    }

    public static void gehituErrorea(String mezua) {
        idatzi("ERROR", mezua);
    }

    private static void idatzi(String mota, String mezua) {

        try (FileWriter fw = new FileWriter(LOG_FITXATEGIA, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            String ordua = LocalDateTime.now().format(FORMATUA);
            out.println("[" + ordua + "] [" + mota + "] " + mezua);

        } catch (IOException e) {
            System.err.println("Errorea log-a idaztean: " + e.getMessage());
        }
    }
}