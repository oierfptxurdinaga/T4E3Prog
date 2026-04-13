package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import db.DBConnection;

/**
 * JokalariDAO klasea Jokalariekin DBren aurka lan egiteko.
 */
public class JokalariDAO {
	/** Connection DBrekin */
	 private Connection cn;

		/**
		 * DAOari connection bat pasatzen diogu, DBrekin lan egiteko.
		 * 
		 * @param cn
		 */
	    public JokalariDAO(Connection cn) {
	        this.cn = cn;
	    }
 /**
  * Jokalariak talde batetik bestera mugitzen duen metodoa
  * @param jokalariId Ze jokalari mugitzen ari da
  * @param taldeBerriaId Ze taldera mugitzen ari den
  * @return
  */
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