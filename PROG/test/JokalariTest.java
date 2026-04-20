import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Jokalari;

class JokalariTest {

	private Jokalari j;

	@BeforeEach
	void setUp() throws Exception {
		j = new Jokalari();
	}

	@Test
	void IzenaTest() {
		j.setIzena("kaixo");
		assertEquals(j.getIzena(),"kaixo");
	}

	@Test
	void AbizenaTest() {
		j.setAbizena("kaixo");
		assertEquals(j.getAbizena(),"kaixo");
	}

	@Test
	void JaiotzeUrtearteaTest() {
		j.setJaiotzeUrtea(2000);
		assertEquals(j.getJaiotzeUrtea(),2000);
	}

	@Test
	void DortsalaTest() {
		j.setDortsala(1);
		assertEquals(j.getDortsala(),1);
	}

	@Test
	void PosisioaTest() {
		j.setPosizio("Aurrelari");
		assertEquals(j.getPosizio(),"Aurrelari");
	}

	@Test
	void AktiboaDagoTest() {
		j.setAktiboaDago(false);
		assertFalse(j.isAktiboaDago());
	}

	@Test
	void getIrudiaUrlTest() {
		j.setIzena("kaixo");
		assertEquals(j.getIrudiaUrl(2025),"https://api.dicebear.com/7.x/avataaars/png?seed=kaixo2025");
	}
}


