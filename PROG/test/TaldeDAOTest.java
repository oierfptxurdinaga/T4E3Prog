import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.TaldeDAO;
import db.DBConnectionTest;

public class TaldeDAOTest {
	private Connection connTest;
	private TaldeDAO taldeDAO;

	@BeforeEach
	void setUp() throws SQLException {
		connTest = DBConnectionTest.obtenerConexion();
		taldeDAO = new TaldeDAO(connTest);
		connTest.setAutoCommit(false);
	}

	@AfterEach
	void tearDown() throws SQLException {
		if (connTest != null && !connTest.isClosed()) {
			connTest.rollback();
			connTest.close();
		}
	}

	@Test
	void testAldatuTaldeArmarria() {
		String armarriBerria = "froga.png";
		int idTalde = 1;
		boolean ondoAldatuta = taldeDAO.aldatuArmarria(armarriBerria, idTalde);
		assertTrue(ondoAldatuta, "Taldea aldatu behar zen datu basean");
	}
}
