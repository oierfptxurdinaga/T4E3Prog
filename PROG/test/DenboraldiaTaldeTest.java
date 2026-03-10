import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;

import java.util.ArrayList;

class DenboraldiTaldeTest {
	
	private DenboraldiTalde dT;
	private Talde t;
	
	@BeforeEach
	void setUp() throws Exception {
		t = new Talde();
		dT = new DenboraldiTalde(t,true);
	}
	
	@Test
	void emaitzakEguneratu() {
		dT.emaitzakEguneratu(1, 1);
		assertEquals(dT.getPJ(), 1);
		assertEquals(dT.getG(), 0);
		assertEquals(dT.getE(), 1);
		assertEquals(dT.getP(), 0);
		assertEquals(dT.getGF(), 1);
		assertEquals(dT.getGC(), 1);
		assertEquals(dT.getDG(), 0);
		assertEquals(dT.getPts(), 1);
	}
	
	@Test
	void getTaldeTest() {
		assertEquals(dT.getTalde(),t);
	}
	
	@Test
	void AktiboaTest() {
		assertEquals(dT.isAktiboa(),true);
		dT.setAktiboa(false);
		assertEquals(dT.isAktiboa(),false);
	}
}


