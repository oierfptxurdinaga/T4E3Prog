import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;

import java.util.ArrayList;

class TaldeTest {
	private Talde t;
	private Jokalari j;
	
	@BeforeEach
	void setUp() throws Exception {
		t = new Talde("Talde1", "", "", null, "Hiria1", true, null, 0);
		j = new Jokalari("Markel", "Abascal", 2002, 13, "Delantero", true);
	}
	
	@Test
	void sartuJokalariaTest() {
		assertNull(t.getJokalariak());
		t.sartuJokalaria(j);
		assertEquals(1, t.getJokalariak().size());
		assertEquals(j, t.getJokalariak().get(0));
	}
	@Test
	void sartuJokalariaMultipleTest() {
	    Jokalari j2 = new Jokalari("Iker", "Lopez", 2001, 7, "Defensa", true);

	    t.sartuJokalaria(j);
	    t.sartuJokalaria(j2);

	    assertEquals(2, t.getJokalariak().size());
	    assertEquals(j, t.getJokalariak().get(0));
	    assertEquals(j2, t.getJokalariak().get(1));
	}
	@Test
	void sartuJokalariaNullTest() {
	    t.sartuJokalaria(null);

	    assertNotNull(t.getJokalariak());
	    assertEquals(1, t.getJokalariak().size());
	    assertNull(t.getJokalariak().get(0));
	}
	
	@Test
	void aldatuEzkutua() {
		t.setEzkutua("Beltza");
        assertEquals("Beltza", t.getEzkutua());
        t.aldatuEzkutua("Gorria");
        assertEquals("Gorria", t.getEzkutua());
	}
	@Test
	void taldeKopiaTest() {
		Talde tKopiaTalde = new Talde(t);
		assertEquals(t, tKopiaTalde);
	}
	@Test
	void setGetIzenaTest() {
		t.setIzena("Txurdinaga");
		assertEquals(t.getIzena(), "Txurdinaga");
	}
	@Test
	void setGetZelaiaTest() {
		t.setFutbolZelaia("Txurdinaga");
		assertEquals(t.getFutbolZelaia(), "Txurdinaga");
	}
	@Test
	void setGetHiriaTest() {
		t.setHiria("Bilbao");
		assertEquals(t.getHiria(), "Bilbao");
	}
	@Test
	void setJokalariakTest(){
		ArrayList<Jokalari> jokalariTest = new ArrayList<Jokalari>();
		jokalariTest.add(j);
		t.setJokalariak(jokalariTest);
		assertEquals(jokalariTest, t.getJokalariak());
	}
	
	@Test
	void setGetAktiboaDagoTest(){
		t.setAktiboaDago(false);
		assertFalse(t.isAktiboaDago());
	}
	@Test
	void eskutuBerdinaTest() {
		t.setEzkutua("Txurdinaga.png");
		t.aldatuEzkutua("Txurdinaga.png");
		assertEquals("Txurdinaga.png", t.getEzkutua());
	}
	@Test
	void equalsTest() {
		assertTrue(t.equals(t));
		assertFalse(t.equals(null));
		assertFalse(t.equals("String"));
		
		Talde desberdinaTalde = new Talde();
	    desberdinaTalde.setIzena("Real Sociedad");
	    assertFalse(t.equals(desberdinaTalde));
	}
	@Test
	void kopiatuTest() {
		t.setJokalariak(new ArrayList<>());
		t.getJokalariak().add(j);
		Talde kopiaTalde = t.kopiatu();
		assertNotSame(t, kopiaTalde);
		assertEquals(t.getIzena(), kopiaTalde.getIzena());
		assertEquals(1, kopiaTalde.getJokalariak().size());
		
		t.setJokalariak(null);
		Talde nullTalde = t.kopiatu();
		assertNotNull(nullTalde);
		assertNotNull(nullTalde.getJokalariak());
		assertTrue(nullTalde.getJokalariak().isEmpty());
	}
	
	@Test
	void toStringTest() {
		assertEquals(t.toString(),"Talde1");
	}
	
	@Test
	void hashCodeIgualParaMismoIzena() {
	    Talde t2 = new Talde();
	    t2.setIzena("Talde1");

	    assertEquals(t.hashCode(), t2.hashCode());
	}
}


