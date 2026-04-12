package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import db.DBConnection;
import model.DenboraldiTalde;
import model.Denboraldia;
import model.Federazioa;
import model.Jardunaldi;
import model.Jokalari;
import model.Partidua;
import model.Talde;

public class DatuKarga {

	public static Federazioa kargatuFederazioaDB() {
		Federazioa federazioa = new Federazioa();

		// Mapak erlazioak mantentzeko
		Map<Integer, Talde> mapaTaldeak = new HashMap<>();
		Map<Integer, Denboraldia> mapaDenboraldiak = new HashMap<>();
		Map<String, DenboraldiTalde> mapaDenboraldiTaldeak = new HashMap<>();

		try (Connection conn = DBConnection.obtenerConexion()) {
			if (conn == null) {
				return federazioa;
			}

			// --- 1. Talde originalak kargatu (datu estatikoak) ---
			String sqlTaldeak = "SELECT id_taldea, izena, ezkutua, futbol_zelaia, hiria, aktiboa_dago, informazioa, sorrera_urtea FROM Taldeak";
			try (PreparedStatement ps = conn.prepareStatement(sqlTaldeak); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Talde t = new Talde(rs.getInt("id_taldea"), rs.getString("izena"), rs.getString("ezkutua"),
							rs.getString("futbol_zelaia"), new ArrayList<>(), rs.getString("hiria"),
							rs.getBoolean("aktiboa_dago"), rs.getString("informazioa"), rs.getInt("sorrera_urtea"));
					int id = rs.getInt("id_taldea");
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
			String sqlParticipantes = "SELECT denboraldia_urtea, id_taldea FROM Denboraldi_Taldeak";
			try (PreparedStatement ps = conn.prepareStatement(sqlParticipantes); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int urtea = rs.getInt("denboraldia_urtea");
					int taldeId = rs.getInt("id_taldea");

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
			String sqlJok = "SELECT id_jokalaria, id_taldea, izena, abizena, jaiotze_urtea, dortsala, posizioa, aktiboa, irudia FROM Jokalariak";

			try (PreparedStatement ps = conn.prepareStatement(sqlJok); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Jokalari j = new Jokalari(rs.getInt("id_jokalaria"), rs.getString("izena"), rs.getString("abizena"),
							rs.getInt("jaiotze_urtea"), rs.getInt("dortsala"), rs.getString("posizioa"),
							rs.getBoolean("aktiboa"), rs.getString("irudia"));

					int taldeId = rs.getInt("id_taldea");
					Talde t = mapaTaldeak.get(taldeId);
					if (t != null) {
						t.sartuJokalaria(j);
					}
				}
			}

			// --- 5. Partiduak eta sailkapena eguneratu ---
			String sqlPartiduak = "SELECT p.id_jardunaldia, p.etxeko_taldea_id, p.kanpoko_taldea_id, "
					+ "p.etxeko_golak, p.kanpoko_golak, " + "j.zenbakia AS jardunaldia_zenbakia, j.denboraldia_urtea "
					+ "FROM Partiduak p " + "JOIN Jardunaldiak j ON p.id_jardunaldia = j.id_jardunaldia";

			try (PreparedStatement ps = conn.prepareStatement(sqlPartiduak); ResultSet rs = ps.executeQuery()) {
			    while (rs.next()) {
			        int urtea = rs.getInt("denboraldia_urtea");
			        int etxeId = rs.getInt("etxeko_taldea_id");
			        int kanpoId = rs.getInt("kanpoko_taldea_id");
			        int golE = rs.getInt("etxeko_golak");
			        int golK = rs.getInt("kanpoko_golak");
			        int idJardunaldi = rs.getInt("id_jardunaldia");
			        int jardunaldiZenbakia = rs.getInt("jardunaldia_zenbakia");

			        Talde etxe = mapaTaldeak.get(etxeId);
			        Talde kanpo = mapaTaldeak.get(kanpoId);

			        if (etxe != null && kanpo != null) {
			            Partidua p = new Partidua(etxe, kanpo);
			            p.setEtxekoGolak(golE);
			            p.setKanpokoGolak(golK);

			            Denboraldia d = mapaDenboraldiak.get(urtea);
			            if (d != null) {
			                // 1. Partidua jardunaldian sartu (zure metodoaren arabera)
			                d.gehituPartiduaJardunaldira(jardunaldiZenbakia, p);

			                // 2. GAKOA: Jardunaldi horri bere ID errealak esleitu
			                // Suposatuz d.getJardunaldiak() metodoak zenbakiaren arabera bilatzeko aukera ematen duela:
			                Jardunaldi jard = d.getJardunaldiID(jardunaldiZenbakia);
			                if (jard != null) {
			                    jard.setId(idJardunaldi); // <--- Ziurtatu zure Jardunaldia modeloan setId(int id) duzula
			                }

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