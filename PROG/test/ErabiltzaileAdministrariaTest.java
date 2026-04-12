import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Erabiltzaile;
import model.ErabiltzaileAdministraria;
import model.Jokalari;
import model.Talde;

class ErabiltzaieAdministrariaTest {

	private Erabiltzaile e;
	private Jokalari j1;
	private Jokalari j2;
	private Talde T1;
	private Talde T2;

	@BeforeEach
	void setUp() throws Exception {
		e = new ErabiltzaileAdministraria("kaixo","kaixo123");

		j1 = new Jokalari();
		j2 = new Jokalari();
		T1 = new Talde();
		T2 = new Talde();

		T1.sartuJokalaria(j1);
		T2.sartuJokalaria(j2);
	}

	@Test
	void getErabiltzaileTest() {
		assertEquals(e.getErabiltzaile(),"kaixo");
	}

	@Test
	void getPasahitzaTest() {
		assertEquals(e.getPasahitza(),"kaixo123");
	}

	@Test
	void setErabiltzaileTest() {
		e.setErabiltzaile("Agur");
		assertEquals(e.getErabiltzaile(),"Agur");
	}

	@Test
	void setPasahitzaTest() {
		e.setPasahitza("Agur123");
		assertEquals(e.getPasahitza(),"Agur123");
	}

	@Test
	void aldatuJokalariakTest1() {
		((ErabiltzaileAdministraria) e).aldatuJokalariak(j1,T1,T2,false);

		assertEquals(T2.getJokalariak().getLast(),j1);
	}

	@Test
	void aldatuJokalariakTest2() {
		((ErabiltzaileAdministraria) e).aldatuJokalariak(j1,T1,T2,true);

		assertEquals(T1.getJokalariak().getFirst(),j1);
		assertNotEquals(T2.getJokalariak().getLast(),j1);
	}

	@Test
	void aldatuJokalariakTest3() {
		((ErabiltzaileAdministraria) e).aldatuJokalariak(j2,T1,T2,false);

		assertEquals(T2.getJokalariak().getFirst(),j2);
		assertNotEquals(T1.getJokalariak().getLast(),j2);
	}
}


