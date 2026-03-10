import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Denboraldia;
import model.Federazioa;
import model.Jokalari;
import model.Talde;

class FederazioaTest {
		private Federazioa f;
		private Denboraldia d;
		private Talde t;
		
		@BeforeEach
		void setUp() throws Exception {
			f = new Federazioa();
			d = new Denboraldia(2025);
			t = new Talde();
		}
	@Test
	void gehituTaldeaTest() {
		f.gehituTaldea(t);
		ArrayList<Talde> taldeakArrayList = f.getTaldeGuztiak();
		assertEquals(t, taldeakArrayList.get(0));
		f.gehituTaldea(t);
		assertEquals(1, taldeakArrayList.size());
	}
	@Test
	void getDenboraldiak () {
		f.gehituDenboraldia(d);
		ArrayList<Denboraldia> denboraldiaTest = f.getDenboraldiak();
		assertEquals(d, denboraldiaTest.get(0));
	}
	@Test
	void getUnekoDenboraldiaTest() {
		f.gehituDenboraldia(d);
		assertEquals(d, f.getUnekoDenboraldia());
		
		Federazioa fHutsa = new Federazioa();
		assertNull(fHutsa.getUnekoDenboraldia());
		
		Federazioa fNull = new Federazioa();
	    fNull.setDenboraldiak(null);
	    assertNull(fNull.getUnekoDenboraldia());
	}
}
