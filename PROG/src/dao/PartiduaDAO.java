package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Partiduekin lan egiteko DAOa
 */
public class PartiduaDAO {

	/** Connection DBrekin */
	private Connection cn;

	/**
	 * DAOari connection bat pasatzen diogu, DBrekin lan egiteko.
	 * 
	 * @param cn
	 */
	public PartiduaDAO(Connection cn) {
		this.cn = cn;
	}

	/**
	 * Partidu baten emaitza aldatzen duen metodoa.
	 * @param jardunaldiId
	 * @param etxekoId
	 * @param kanpokoId
	 * @param etxekoGolak
	 * @param kanpokoGolak
	 * @return
	 */
	public boolean eguneratuEmaitzaDB(int jardunaldiId, int etxekoId, int kanpokoId, int etxekoGolak,
			int kanpokoGolak) {
		String sql = "UPDATE partiduak SET etxeko_golak = ?, kanpoko_golak = ? "
				+ "WHERE id_jardunaldia = ? AND etxeko_taldea_id = ? AND kanpoko_taldea_id = ?";

		try (PreparedStatement ps = cn.prepareStatement(sql)) {

			ps.setInt(1, etxekoGolak);
			ps.setInt(2, kanpokoGolak);
			ps.setInt(3, jardunaldiId);
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