import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import db.DBConnectionTest;
import dao.JokalariDAO;

class JokalariDAOTest {
	private Connection connTest;
	private JokalariDAO jokalariDAO;

	@BeforeEach
	void setUp() throws SQLException {
		connTest = DBConnectionTest.obtenerConexion();
		jokalariDAO = new JokalariDAO(connTest);
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
	void testAldatuJokalariarenTaldea() {
		int idJokalari = 1;
		int idTaldeBerria = 2;
		boolean ondoAldatuta = jokalariDAO.aldatuJokalariarenTaldeaDB(idJokalari, idTaldeBerria);
		assertTrue(ondoAldatuta, "Jokalaria aldatu behar zen datu basean");
	}
}