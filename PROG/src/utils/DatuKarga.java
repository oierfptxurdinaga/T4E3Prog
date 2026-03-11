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
			String sqlTaldeak = "SELECT id_taldea, izena, ezkutua, futbol_zelaia, hiria, aktiboa_dago FROM Taldeak";
			try (PreparedStatement ps = conn.prepareStatement(sqlTaldeak); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Talde t = new Talde(rs.getString("izena"), rs.getString("ezkutua"), rs.getString("futbol_zelaia"),
							new ArrayList<>(), rs.getString("hiria"), rs.getBoolean("aktiboa_dago"));
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

			// --- 3. Denboraldiko taldeak (DenboraldiTalde) sortu ---
			String sqlParticipantes = "SELECT denboraldia_urtea, talde_id FROM Denboraldi_Taldeak"; // <-- Sin
																									// 'aktiboa_dago'
			try (PreparedStatement ps = conn.prepareStatement(sqlParticipantes); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int urtea = rs.getInt("denboraldia_urtea");
					int taldeId = rs.getInt("talde_id");

					Talde t = mapaTaldeak.get(taldeId);
					Denboraldia d = mapaDenboraldiak.get(urtea);

					if (d != null && t != null) {
						// Usamos el estado activo del equipo directamente
						DenboraldiTalde dt = new DenboraldiTalde(t, t.isAktiboaDago());
						d.gehituDenboraldiTaldea(dt);

						mapaDenboraldiTaldeak.put(urtea + "-" + taldeId, dt);
					}
				}
			}

			// --- 4. Jokalariak kargatu (Historial berriaren arabera) ---
			// Adi: 'denboraldi_jokalariak' taula erabiltzen dugu jokalaria urte bakoitzeko
			// taldeari lotzeko
			String sqlJok = "SELECT dj.denboraldia_urtea, dj.talde_id, j.izena, j.abizena, j.jaiotze_urtea, j.dortsala, j.posizioa, j.aktiboa "
					+ "FROM denboraldi_jokalariak dj " + "JOIN Jokalariak j ON dj.jokalari_id = j.id_jokalaria";

			try (PreparedStatement ps = conn.prepareStatement(sqlJok); ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Jokalari j = new Jokalari(rs.getString("izena"), rs.getString("abizena"),
							rs.getInt("jaiotze_urtea"), rs.getInt("dortsala"), rs.getString("posizioa"),
							rs.getBoolean("aktiboa"));

					int urtea = rs.getInt("denboraldia_urtea");
					int taldeId = rs.getInt("talde_id");

					// Urte horretako DenboraldiTalde objektua bilatu
					DenboraldiTalde dt = mapaDenboraldiTaldeak.get(urtea + "-" + taldeId);

					if (dt != null) {
						// SOLUCIÓN: Sacamos el Talde de la caja (DenboraldiTalde) y le metemos el
						// jugador
						dt.getTalde().sartuJokalaria(j);
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

					// Talde baseak lortu partidu-objektua sortzeko
					Talde etxe = mapaTaldeak.get(etxeId);
					Talde kanpo = mapaTaldeak.get(kanpoId);

					if (etxe != null && kanpo != null) {
						Partidua p = new Partidua(etxe, kanpo);
						p.setEtxekoGolak(golE);
						p.setKanpokoGolak(golK);

						Denboraldia d = mapaDenboraldiak.get(urtea);
						if (d != null) {
							d.gehituPartiduaJardunaldira(rs.getInt("jardunaldia_zenbakia"), p);

							// ESTATISTIKAK EGUNERATU: sailkapena kargatzean prest egon dadin
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