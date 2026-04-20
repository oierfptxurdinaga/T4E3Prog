import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Erabiltzaile;
import model.ErabiltzailePresi;


class ErabiltzaiePresiTest {

	private Erabiltzaile erabiltzaile;


	@BeforeEach
	void setUp() throws Exception {
		erabiltzaile = new ErabiltzailePresi("kaixo","kaixo123");
	}

	@Test
	void getErabiltzaileTest() {
		assertEquals(erabiltzaile.getErabiltzaile(),"kaixo");
	}

	@Test
	void getPasahitzaTest() {
		assertEquals(erabiltzaile.getPasahitza(),"kaixo123");
	}

	@Test
	void setErabiltzaileTest() {
		erabiltzaile.setErabiltzaile("agur");
		assertEquals(erabiltzaile.getErabiltzaile(),"agur");
	}

	@Test
	void setPashitzaTest() {
		erabiltzaile.setPasahitza("agur123");
		assertEquals(erabiltzaile.getPasahitza(),"agur123");
	}
}


