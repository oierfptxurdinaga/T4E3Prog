import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dao.PartiduaDAO;
import db.DBConnectionTest;

public class PartiduaDAOTest {
	private Connection connTest;
	private PartiduaDAO partiduaDAO;
	
	@BeforeEach
	void setUp() throws SQLException {
		connTest = DBConnectionTest.obtenerConexion();
		partiduaDAO = new PartiduaDAO(connTest);
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
	void eguneratuEmaitzaDBTestOndo() {
		boolean ondoAldatuta = partiduaDAO.eguneratuEmaitzaDB(1, 1, 6, 10, 10);
		assertTrue(ondoAldatuta, "Partidua aldatu behar zen datu basean");
	}
	@Test
	void eguneratuEmaitzaDBTestTxarto() {
		boolean ondoAldatuta = partiduaDAO.eguneratuEmaitzaDB(0, -5, -5, -5, 0);
		assertFalse(ondoAldatuta);
	}
}
