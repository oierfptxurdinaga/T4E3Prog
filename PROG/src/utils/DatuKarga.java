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
		// GAKOA: Urtea-TaldeId konbinazio bakoitzeko DenboraldiTalde objektua
		// gordetzeko
		Map<String, DenboraldiTalde> mapaDenboraldiTaldeak = new HashMap<>();

		try (Connection conn = DBConnection.obtenerConexion()) {
			if (conn == null)
				return federazioa;

			// --- 1. Talde originalak kargatu (datu estatikoak) ---
			String sqlTaldeak = "SELECT talde_id, izena, ezkutua, futbol_zelaia, hiria, aktiboa_dago, informazioa, sorrera_urtea FROM Taldeak";
			try (PreparedStatement ps = conn.prepareStatement(sqlTaldeak); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Talde t = new Talde(rs.getString("izena"), rs.getString("ezkutua"), rs.getString("futbol_zelaia"),
							new ArrayList<>(), rs.getString("hiria"), rs.getBoolean("aktiboa_dago"), rs.getString("informazioa"), rs.getInt("sorrera_urtea"));
					int id = rs.getInt("talde_id");
					mapaTaldeak.put(id, t);
					federazioa.gehituTaldea(t);
				}
			}

			// --- 2. Denboraldiak kargatu ---
			String sqlDenb = "SELECT urtea FROM Denboraldiak";
			try (PreparedStatement ps = conn.prepareStatement(sqlDenb); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int urtea = rs.getInt("urtea");
					Denboraldia d = new Denboraldia(urtea);
					mapaDenboraldiak.put(urtea, d);
					federazioa.gehituDenboraldia(d);
				}
			}

			// --- 3. DENBORALDIKO TALDEAK ---
			String sqlParticipantes = "SELECT denboraldia_urtea, talde_id FROM Denboraldi_Taldeak"; 
			try (PreparedStatement ps = conn.prepareStatement(sqlParticipantes); ResultSet rs = ps.executeQuery()) {
			    while (rs.next()) {
			        int urtea = rs.getInt("denboraldia_urtea");
			        int taldeId = rs.getInt("talde_id");

			        Talde t = mapaTaldeak.get(taldeId);
			        Denboraldia d = mapaDenboraldiak.get(urtea);

			        if (d != null && t != null) {
			            DenboraldiTalde dt = new DenboraldiTalde(t, t.isAktiboaDago());	            
			            mapaDenboraldiTaldeak.put(urtea + "-" + taldeId, dt);
			            d.gehituDenboraldiTaldea(dt); 
			        }
			    }
			}
			
		// --- 4. JOKALARIAK ---
			String sqlJok = "SELECT talde_id, izena, abizena, jaiotze_urtea, dortsala, posizioa, aktiboa FROM Jokalariak";

			try (PreparedStatement ps = conn.prepareStatement(sqlJok); ResultSet rs = ps.executeQuery()) {
			    while (rs.next()) {
			        Jokalari j = new Jokalari(
			                rs.getString("izena"), 
			                rs.getString("abizena"),
			                rs.getInt("jaiotze_urtea"), 
			                rs.getInt("dortsala"), 
			                rs.getString("posizioa"),
			                rs.getBoolean("aktiboa")
			        );

			        int taldeId = rs.getInt("talde_id");
			        
			        // Talde "Masterra" bilatu mapan eta jokalaria sartu
			        Talde t = mapaTaldeak.get(taldeId);
			        if (t != null) {
			            t.sartuJokalaria(j);
			        }
			    }
			}

			// --- 5. Partiduak eta sailkapena eguneratu ---
			String sqlPartiduak = "SELECT p.etxeko_taldea_id, p.kanpoko_taldea_id, "
					+ "p.etxeko_golak, p.kanpoko_golak, " + "j.zenbakia AS jardunaldia_zenbakia, j.denboraldia_urtea "
					+ "FROM Partiduak p " + "JOIN Jardunaldiak j ON p.jardunaldia_id = j.id_jardunaldia";

			try (PreparedStatement ps = conn.prepareStatement(sqlPartiduak); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int urtea = rs.getInt("denboraldia_urtea");
					int etxeId = rs.getInt("etxeko_taldea_id");
					int kanpoId = rs.getInt("kanpoko_taldea_id");
					int golE = rs.getInt("etxeko_golak");
					int golK = rs.getInt("kanpoko_golak");
					Talde etxe = mapaTaldeak.get(etxeId);
					Talde kanpo = mapaTaldeak.get(kanpoId);

					if (etxe != null && kanpo != null) {
						Partidua p = new Partidua(etxe, kanpo);
						p.setEtxekoGolak(golE);
						p.setKanpokoGolak(golK);

						Denboraldia d = mapaDenboraldiak.get(urtea);
						if (d != null) {
							d.gehituPartiduaJardunaldira(rs.getInt("jardunaldia_zenbakia"), p);
							DenboraldiTalde dtEtxe = mapaDenboraldiTaldeak.get(urtea + "-" + etxeId);
							DenboraldiTalde dtKanpo = mapaDenboraldiTaldeak.get(urtea + "-" + kanpoId);

							if (dtEtxe != null && dtKanpo != null && golE != -1) {
								dtEtxe.emaitzakEguneratu(golE, golK);
								dtKanpo.emaitzakEguneratu(golK, golE);
							}
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