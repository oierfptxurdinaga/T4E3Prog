import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.DenboraldiTalde;
import model.Denboraldia;
import model.Jardunaldi;
import model.Partidua;
import model.Talde;

class DenboraldiaTest {
	private Denboraldia denboraldia;
	private Talde t1, t2;
	private Jardunaldi j1, j2;
	private Partidua p1, p2;

	@BeforeEach
	void setUp() throws Exception {
		denboraldia = new Denboraldia(2002);

		t1 = new Talde(0, "Talde1", "", "", null, "Hiria1", true, null, 0);
        t2 = new Talde(0, "Talde2", "", "", null, "Hiria2", true, null, 0);

        p1 = new Partidua(t1, t2); //Hau ez da jokatu oraindik
        p2 = new Partidua(t1, t2, 2, 1); //Hau jokatu da

        j1 = new Jardunaldi(1);
	}

	
	@Test
	void getJardunaldiIDNullNegTest() {
		int zenbakia = -5;
		assertNull(denboraldia.getJardunaldiID(zenbakia));
	}
	
	@Test
	void getJardunaldiIDNullOverTest() {
		int zenbakia = 100;
		assertNull(denboraldia.getJardunaldiID(zenbakia));
	}
	
	@Test
	void getJardunaldiIDTest() {
		j1.addPartidua(p1);
		j1.addPartidua(p2);
		denboraldia.addJardunaldia(j1);
		int zenbakia = 1;
		assertEquals(denboraldia.getJardunaldiID(zenbakia),j1);
	}
	@Test
	void gehituDenboraldiTaldeNullTest() {
		DenboraldiTalde dt1 = new DenboraldiTalde(t1, false);
		denboraldia.setLigakoTaldeak(null);
		denboraldia.gehituDenboraldiTaldea(dt1);
		assertNotNull(denboraldia.getLigakoTaldeak());
	}
	
	@Test
    void gehituTaldeaEtaAddJardunaldiaTest() {
        assertEquals(0, denboraldia.getLigakoTaldeak().size());
        assertEquals(0, denboraldia.getLigakoJardunaldi().size());

        denboraldia.gehituTaldea(t1); // Honek automatikoki DenboraldiTalde sortzen du barruan
        denboraldia.addJardunaldia(j1);

        assertEquals(1, denboraldia.getLigakoTaldeak().size());
        // ALDAKETA: getLigakoTaldeak()-ek DenboraldiTalde itzultzen du, beraz .getTalde() atera behar dugu
        assertEquals(t1, denboraldia.getLigakoTaldeak().get(0).getTalde());

        assertEquals(1, denboraldia.getLigakoJardunaldi().size());
        assertEquals(j1, denboraldia.getLigakoJardunaldi().get(0));
    }

	@Test
	void isHasiDaEtaAmaitutaTest() {
		assertFalse(denboraldia.isHasiDa());
		assertFalse(denboraldia.isAmaituta());

		j1.addPartidua(p1); //Partidu hau ez da oraindik jokatu
		denboraldia.addJardunaldia(j1);

		assertFalse(denboraldia.isHasiDa()); //Partidua jokatu ez bada denboraldia ez da hasi
		assertFalse(denboraldia.isAmaituta());

		j1.getPartiduak().clear(); //Jokatu ez den partidua kentzen dut, denboraldi bat dituen partidu guztiak jokatu direnean amaituta dagoelako
		j1.addPartidua(p2); //Partidu hau BAI jokatu da

		assertTrue(denboraldia.isHasiDa());
		assertTrue(denboraldia.isAmaituta());
	}

	@Test
	void testIsHasiDa_Null() {
        denboraldia.setLigakoJardunaldi(null);
        assertFalse(denboraldia.isHasiDa());
	}

	@Test
    void toStringTest() {
        assertEquals("2002", denboraldia.toString());
    }

	@Test
	void getUrteaTest() {
		assertEquals(denboraldia.getUrtea(), 2002);
	}

	@Test
	void setLigakoTaldeakTest() {
		// ALDAKETA: Zerrenda orain DenboraldiTalde motakoa izan behar da
		ArrayList<DenboraldiTalde> dtArrayList = new ArrayList<>();
		dtArrayList.add(new DenboraldiTalde(t1, true));
		dtArrayList.add(new DenboraldiTalde(t2, true));

		denboraldia.setLigakoTaldeak(dtArrayList);
		assertEquals(dtArrayList, denboraldia.getLigakoTaldeak());
	}

	@Test
	void isDenboraldiaHasiDaTest() {
		assertFalse(denboraldia.isDenboraldiaHasiDa());
	}

	@Test
	void getSailkapenaTest() {
		denboraldia.gehituTaldea(t1);
		denboraldia.gehituTaldea(t2);
		j1.addPartidua(p1);
		j1.addPartidua(p2);
		denboraldia.addJardunaldia(j1);

		ArrayList<DenboraldiTalde> tDenboraldiTaldeak = denboraldia.getSailkapena();
		assertNotNull(tDenboraldiTaldeak);
		assertEquals(2, tDenboraldiTaldeak.size());
	}

	@Test
	void getSailkapenaTest_Null() {
		denboraldia.setLigakoTaldeak(null);
		denboraldia.setLigakoJardunaldi(null);
		ArrayList<DenboraldiTalde> emaitzArrayList = denboraldia.getSailkapena();

		assertNotNull(emaitzArrayList);
		assertTrue(emaitzArrayList.isEmpty());
	}
}