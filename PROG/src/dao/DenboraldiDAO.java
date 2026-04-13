package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.DenboraldiTalde;
import model.Denboraldia;
import model.Jardunaldi;
import model.Jokalari;
import model.Partidua;
import model.Talde;

public class DenboraldiDAO {
    private Connection cn;
    public DenboraldiDAO(Connection cn) {
        this.cn = cn;
    }

    public boolean txertatuDenboraldiaOsoa(Denboraldia denboraldia) {
        String sqlDenboraldia = "INSERT INTO denboraldiak (urtea) VALUES (?)";
        String sqlDenbTaldeak = "INSERT INTO denboraldi_taldeak (denboraldia_urtea, id_taldea) VALUES (?, ?)";
        String sqlDenbJokalariak = "INSERT INTO denboraldi_jokalariak (denboraldia_urtea, id_taldea, id_jokalari) VALUES (?, ?, ?)";
        String sqlJardunaldia = "INSERT INTO jardunaldiak (zenbakia, denboraldia_urtea) VALUES (?, ?)";
        String sqlPartidua = "INSERT INTO partiduak (id_jardunaldia, etxeko_taldea_id, kanpoko_taldea_id, etxeko_golak, kanpoko_golak) VALUES (?, ?, ?, ?, ?)";
        try {
            this.cn.setAutoCommit(false); 

            try (PreparedStatement psDenb = this.cn.prepareStatement(sqlDenboraldia)) {
                psDenb.setInt(1, denboraldia.getUrtea());
                psDenb.executeUpdate();
            }

            try (PreparedStatement psDTaldeak = this.cn.prepareStatement(sqlDenbTaldeak);
                 PreparedStatement psDJokalariak = this.cn.prepareStatement(sqlDenbJokalariak)) {

                for (DenboraldiTalde dt : denboraldia.getLigakoTaldeak()) {
                    Talde taldea = dt.getTalde();
                    int idTaldea = taldea.getId();

                    psDTaldeak.setInt(1, denboraldia.getUrtea());
                    psDTaldeak.setInt(2, idTaldea);
                    psDTaldeak.addBatch();

                    for (Jokalari jokalari : taldea.getJokalariak()) {
                        psDJokalariak.setInt(1, denboraldia.getUrtea());
                        psDJokalariak.setInt(2, idTaldea);
                        psDJokalariak.setInt(3, jokalari.getId());
                        psDJokalariak.addBatch();
                    }
                }

                psDTaldeak.executeBatch();
                psDJokalariak.executeBatch();
            }

            try (PreparedStatement psJard = this.cn.prepareStatement(sqlJardunaldia, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psPart = this.cn.prepareStatement(sqlPartidua)) {

                for (Jardunaldi jardunaldia : denboraldia.getLigakoJardunaldi()) {
                    psJard.setInt(1, jardunaldia.getJardunaldiZbk());
                    psJard.setInt(2, denboraldia.getUrtea());
                    psJard.executeUpdate(); 

                    try (ResultSet rs = psJard.getGeneratedKeys()) {
                        if (rs.next()) {
                            int idJardunaldiaSartuta = rs.getInt(1);
                            for (Partidua partidua : jardunaldia.getPartiduak()) {
                                psPart.setInt(1, idJardunaldiaSartuta);
                                psPart.setInt(2, partidua.getEtxekoTaldea().getId());
                                psPart.setInt(3, partidua.getKanpokoTaldea().getId());
                                psPart.setInt(4, partidua.getEtxekoGolak());
                                psPart.setInt(5, partidua.getKanpokoGolak());
                                psPart.addBatch();
                            }
                        }
                    }
                }
                psPart.executeBatch();
            }

            this.cn.commit();
            return true;

        } catch (SQLException e) {
            if (this.cn != null) {
                try {
                    this.cn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (this.cn != null) {
                    this.cn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}