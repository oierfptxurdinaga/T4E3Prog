package utils;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import db.DBConnection;
import model.*;

public class DatuKarga {




    public static Federazioa kargatuFederazioaDB() {
        Federazioa federazioa = new Federazioa();
        // Mapak erlazioak mantentzeko
        Map<Integer, Talde> mapaTaldeak = new HashMap<>();
        Map<Integer, Denboraldia> mapaDenboraldiak = new HashMap<>();

        try (Connection conn = DBConnection.obtenerConexion()) {
            if (conn == null) return federazioa;

            // --- 1. Taldeak kargatu ---
            String sqlTaldeak = "SELECT id_taldea, izena, ezkutua, futbol_zelaia, hiria, aktiboa_dago FROM Taldeak";
            try (PreparedStatement ps = conn.prepareStatement(sqlTaldeak); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Talde t = new Talde(
                        rs.getString("izena"),
                        rs.getString("ezkutua"),
                        rs.getString("futbol_zelaia"),
                        new ArrayList<>(), // Lista de jugadores vacía por ahora
                        rs.getString("hiria"),
                        rs.getBoolean("aktiboa_dago")
                    );
                    int id = rs.getInt("id_taldea");
                    mapaTaldeak.put(id, t);
                    federazioa.gehituTaldea(t);
                }
            }

            // --- 2. Jokalariak kargatu eta taldeari asignatu ---
            String sqlJok = "SELECT izena, abizena, jaiotze_urtea, dortsala, posizioa, aktiboa, talde_id FROM Jokalariak";
            try (PreparedStatement ps = conn.prepareStatement(sqlJok); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Jokalari j = new Jokalari(
                        rs.getString("izena"), rs.getString("abizena"),
                        rs.getInt("jaiotze_urtea"), rs.getInt("dortsala"),
                        rs.getString("posizioa"), rs.getBoolean("aktiboa")
                    );
                    Talde t = mapaTaldeak.get(rs.getInt("talde_id"));
                    if (t != null) t.sartuJokalaria(j);
                }
            }

            // --- 3. Denboraldiak kargatu ---
            String sqlDenb = "SELECT urtea FROM Denboraldiak";
            try (PreparedStatement ps = conn.prepareStatement(sqlDenb); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int urtea = rs.getInt("urtea"); 
                    Denboraldia d = new Denboraldia(urtea); 
                    mapaDenboraldiak.put(urtea, d); 
                    federazioa.gehituDenboraldia(d);
                }
            }
            
         // --- 3.5. ASIGNAR EQUIPOS A SUS TEMPORADAS ---
            String sqlParticipantes = "SELECT denboraldia_urtea, talde_id FROM Denboraldi_Taldeak";
            try (PreparedStatement ps = conn.prepareStatement(sqlParticipantes); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int urtea = rs.getInt("denboraldia_urtea");
                    int taldeId = rs.getInt("talde_id");
                    
                    // Rescatamos la temporada y el equipo de nuestros mapas
                    Denboraldia d = mapaDenboraldiak.get(urtea);
                    Talde t = mapaTaldeak.get(taldeId);
                    
                    // Si existen los dos, metemos el equipo en la temporada correspondiente
                    if (d != null && t != null) {
                        d.gehituTaldea(t); // Usamos el método que ya tenías en Denboraldia.java
                    }
                }
            }
            
            
         // --- 4. CARGAR PARTIDOS Y JORNADAS ---
         // JOIN bat egiten dugu Jardunaldiak taularekin, denboraldiaren urtea eta jardunaldiaren zenbakia lortzeko
         String sqlPartiduak = "SELECT p.etxeko_taldea_id, p.kanpoko_taldea_id, " +
                               "p.etxeko_golak, p.kanpoko_golak, " +
                               "j.zenbakia AS jardunaldia_zenbakia, j.denboraldia_urtea " +
                               "FROM Partiduak p " +
                               "JOIN Jardunaldiak j ON p.jardunaldia_id = j.id_jardunaldia";

         try (PreparedStatement ps = conn.prepareStatement(sqlPartiduak); ResultSet rs = ps.executeQuery()) {
             while (rs.next()) {
                 // Zure taulako zutabeen izen zehatzak erabiltzen ditugu
                 Talde etxe = mapaTaldeak.get(rs.getInt("etxeko_taldea_id"));
                 Talde kanpo = mapaTaldeak.get(rs.getInt("kanpoko_taldea_id"));
                 
                 if (etxe != null && kanpo != null) {
                     Partidua p = new Partidua(etxe, kanpo);
                     p.setEtxekoGolak(rs.getInt("etxeko_golak"));
                     p.setKanpokoGolak(rs.getInt("kanpoko_golak"));
                     
                     // Jokatuta dagoen ala ez deduzitu dezakegu: golak -1 ez badira, jokatu da
                     // p.setAmaituta(rs.getInt("etxeko_golak") != -1);

                     // Jardunaldiak taulatik lortutako urtearekin Denboraldia bilatu
                     Denboraldia d = mapaDenboraldiak.get(rs.getInt("denboraldia_urtea"));
                     
                     if (d != null) {
                         int numJardunaldi = rs.getInt("jardunaldia_zenbakia");
                         // Jardunaldia existitzen dela ziurtatu eta partidua gehitu
                         d.gehituPartiduaJardunaldira(numJardunaldi, p);
                     }
                 }
             }
         }
         
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return federazioa;
    }
    
}