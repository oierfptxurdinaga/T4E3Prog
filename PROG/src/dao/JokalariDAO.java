package dao; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import db.DBConnection;

public class JokalariDAO {

    public static boolean aldatuJokalariarenTaldeaDB(int jokalariId, int taldeBerriaId) {
        String sql = "UPDATE jokalariak SET talde_id = ? WHERE id_jokalaria = ?";

        try (Connection cn = DBConnection.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            
            ps.setInt(1, taldeBerriaId);
            ps.setInt(2, jokalariId);
            
            int aldatutakoErrenkadak = ps.executeUpdate();
            return aldatutakoErrenkadak > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}