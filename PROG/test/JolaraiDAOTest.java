import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import dao.JokalariDAO;
import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JokalariDAOTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private MockedStatic<DBConnection> mockedDBConnection;

    @BeforeEach
    void setUp() throws SQLException {
        // Inicializamos los mocks antes de cada test
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);

        // Configuramos la conexión para que devuelva nuestro PreparedStatement mockeado
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Mockeamos el método estático de la conexión a la BD
        mockedDBConnection = mockStatic(DBConnection.class);
        mockedDBConnection.when(DBConnection::obtenerConexion).thenReturn(mockConnection);
    }

    @AfterEach
    void tearDown() {
        // Es muy importante cerrar el mock estático después de cada test
        // para evitar que interfiera con otros tests.
        mockedDBConnection.close();
    }

    @Test
    void testAldatuJokalariarenTaldeaDB_Exito() throws SQLException {
        // Arrange: Simulamos que el executeUpdate afecta a 1 fila
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        
        int jokalariId = 10;
        int taldeBerriaId = 5;

        // Act
        boolean result = JokalariDAO.aldatuJokalariarenTaldeaDB(jokalariId, taldeBerriaId);

        // Assert
        assertTrue(result, "Debería devolver true si se actualizó una fila");
        
        // Verificamos que los parámetros se asignaron en el orden correcto
        verify(mockPreparedStatement).setInt(1, taldeBerriaId);
        verify(mockPreparedStatement).setInt(2, jokalariId);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testAldatuJokalariarenTaldeaDB_Fallo_NoExisteJokalari() throws SQLException {
        // Arrange: Simulamos que el executeUpdate afecta a 0 filas (el jokalari no existe)
        when(mockPreparedStatement.executeUpdate()).thenReturn(0);

        // Act
        boolean result = JokalariDAO.aldatuJokalariarenTaldeaDB(99, 5);

        // Assert
        assertFalse(result, "Debería devolver false si no se actualizó ninguna fila");
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    void testAldatuJokalariarenTaldeaDB_Excepcion() throws SQLException {
        // Arrange: Simulamos que ocurre un error en la base de datos
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Error simulado de base de datos"));

        // Act
        boolean result = JokalariDAO.aldatuJokalariarenTaldeaDB(10, 5);

        // Assert
        assertFalse(result, "Debería devolver false si ocurre una SQLException");
    }
}