package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TaldeDAO {
	private Connection cn;

	public TaldeDAO(Connection cn) {
		this.cn = cn;
	}
	
	public boolean aldatuArmarria (String armarria, int idTaldea) {
		String sql = "UPDATE taldeak SET ezkutua = ? "
				+ "WHERE id_taldea = ?";
		try (PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setString(1, armarria);
			ps.setInt(2, idTaldea);


			int aldatutakoErrenkadak = ps.executeUpdate();
			return aldatutakoErrenkadak > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}
}
