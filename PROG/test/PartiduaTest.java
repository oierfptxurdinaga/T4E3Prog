import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;

import java.util.ArrayList;

class PartiduaTest {
	
	private Partidua partidu;
	private Talde talde1;
	private Talde talde2;
	
	
	@BeforeEach
	void setUp() throws Exception {
		talde1= new Talde(0, "Talde1", "", "", null, "Hiria1", true, null, 0);
		talde2= new Talde(0, "Talde2", "", "", null, "Hiria1", true, null, 0);
		partidu = new Partidua(talde1,talde2);
	}
	
	@Test
	void jokatutaDagoTestFalse() {
		assertFalse(partidu.jokatutaDago());
	}
	
	@Test
	void setEtxekoTaldea() {
		partidu.setEtxekoTaldea(talde2);
		assertEquals(partidu.getEtxekoTaldea(),talde2);
	}
	
	@Test
	void setKanpokoTaldea() {
		partidu.setKanpokoTaldea(talde1);
		assertEquals(partidu.getKanpokoTaldea(),talde1);
	}
	
	@Test
	void setKanpokoetxekoTest() {
		partidu.setEtxekoGolak(1);
		assertEquals(partidu.getEtxekoGolak(),1);
	}
	
	@Test
	void setKanpokoGolakTest() {
		partidu.setKanpokoGolak(1);
		assertEquals(partidu.getKanpokoGolak(),1);
	}
	
	@Test
	void jokatutaDagoTestTrue() {
		partidu.setEtxekoGolak(1);
		partidu.setKanpokoGolak(1);
		assertTrue(partidu.jokatutaDago());
	}
	
	@Test
	void jokatutaDagoTest3() {
		partidu.setKanpokoGolak(1);
		assertFalse(partidu.jokatutaDago());
	}
	
	@Test
	void jokatutaDagoTest4() {
		partidu.setEtxekoGolak(1);
		assertFalse(partidu.jokatutaDago());
	}
}


