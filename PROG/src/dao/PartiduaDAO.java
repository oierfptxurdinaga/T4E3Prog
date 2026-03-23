package dao; // Zure paketearen izena

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import db.DBConnection;

public class PartiduaDAO {

    public static boolean eguneratuEmaitzaDB(int jardunaldiaId, int etxekoId, int kanpokoId, int etxekoGolak, int kanpokoGolak) {
        String sql = "UPDATE partiduak SET etxeko_golak = ?, kanpoko_golak = ? "
                   + "WHERE id_jardunaldi = ? AND etxeko_taldea_id = ? AND kanpoko_taldea_id = ?";

        try (Connection cn = DBConnection.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, etxekoGolak);
            ps.setInt(2, kanpokoGolak);
            ps.setInt(3, jardunaldiaId);
            ps.setInt(4, etxekoId);
            ps.setInt(5, kanpokoId);
            
            int aldatutakoErrenkadak = ps.executeUpdate();
            return aldatutakoErrenkadak > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}