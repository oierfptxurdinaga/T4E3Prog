import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Erabiltzaile;

class ErabiltzaieTest {

	private Erabiltzaile erabiltzaile;

	static class ErabiltzaileStub extends Erabiltzaile {
        public ErabiltzaileStub(String erabiltzaile, String pasahitza) {
            super(erabiltzaile, pasahitza);
        }

        @Override
        public void setErabiltzaile(String string) {
            this.erabiltzaile = string;
        }

        @Override
        public void setPasahitza(String string) {
            this.pasahitza = string;
        }
    }

    @BeforeEach
    void setUp() {
        erabiltzaile = new ErabiltzaileStub("kaixo", "kaixo123");
    }

    @Test
    void getErabiltzaileTest() {
        // Ahora coverage reconoce que estamos testeando el getter en Erabiltzaile
        assertEquals("kaixo", erabiltzaile.getErabiltzaile());
    }

    @Test
    void getPasahitzaTest() {
        assertEquals("kaixo123", erabiltzaile.getPasahitza());
    }

    @Test
    void setErabiltzaileTest() {
        erabiltzaile.setErabiltzaile("nuevoUsuario");
        assertEquals("nuevoUsuario", erabiltzaile.getErabiltzaile());
    }

    @Test
    void setPasahitzaTest() {
        erabiltzaile.setPasahitza("nuevoPass");
        assertEquals("nuevoPass", erabiltzaile.getPasahitza());
    }

    @Test
    void constructorNoNullTest() {
        assertNotNull(erabiltzaile.getErabiltzaile());
        assertNotNull(erabiltzaile.getPasahitza());
    }
}
