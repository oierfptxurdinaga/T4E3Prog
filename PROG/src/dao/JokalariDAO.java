package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DBConnection;

public class JokalariDAO {
	
	 private Connection cn;

	    public JokalariDAO(Connection cn) {
	        this.cn = cn;
	    }

    public boolean aldatuJokalariarenTaldeaDB(int jokalariId, int taldeBerriaId) {
        String sql = "UPDATE jokalariak SET id_taldea = ? WHERE id_jokalaria = ?";

        try (PreparedStatement ps = this.cn.prepareStatement(sql)) {

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