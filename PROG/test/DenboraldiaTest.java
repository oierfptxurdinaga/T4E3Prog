import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;

import java.util.ArrayList;

class DenboraldiaTest {
	private Denboraldia denboraldia;
	private Talde t1, t2;
	private Jardunaldi j1, j2;
	private Partidua p1, p2;
	
	@BeforeEach
	void setUp() throws Exception {
		denboraldia = new Denboraldia(2002);
		
		t1 = new Talde("Talde1", "", "", null, "Hiria1", true);
        t2 = new Talde("Talde2", "", "", null, "Hiria2", true);

        p1 = new Partidua(t1, t2); //Hau ez da jokatu horaindik
        p2 = new Partidua(t1, t2, 2, 1); //Hau jokatu da

        j1 = new Jardunaldi(1);
	}
	
	@Test
    void gehituTaldeaEtaAddJardunaldiaTest() {
        assertEquals(0, denboraldia.getLigakoTaldeak().size());
        assertEquals(0, denboraldia.getLigakoJardunaldi().size());
        
        denboraldia.gehituTaldea(t1);
        denboraldia.addJardunaldia(j1);

        assertEquals(1, denboraldia.getLigakoTaldeak().size());
        assertEquals(t1, denboraldia.getLigakoTaldeak().get(0));
        assertEquals(1, denboraldia.getLigakoJardunaldi().size());
        assertEquals(j1, denboraldia.getLigakoJardunaldi().get(0));
    }
	
	@Test
	void isHasiDaEtaAmaitutaTest() {
		assertFalse(denboraldia.isHasiDa());
		assertFalse(denboraldia.isAmaituta());
		
		j1.addPartidua(p1); //Partidu hau ez da horaindik jokatu
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
		ArrayList<Talde> taldeakArrayList = new ArrayList<Talde>();
		taldeakArrayList.add(t1);
		taldeakArrayList.add(t2);
		denboraldia.setLigakoTaldeak(taldeakArrayList);
		assertEquals(taldeakArrayList, denboraldia.getLigakoTaldeak());
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