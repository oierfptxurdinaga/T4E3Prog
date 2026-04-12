import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Sailkapena;
import model.Talde;

class SailkapenaTest {

	private Sailkapena S;
	private Talde T1;
	private Talde T2;
	private ArrayList<Talde> lT;
	private ArrayList<Integer> Pt;


	@BeforeEach
	void setUp() {
	    T1 = new Talde();
	    T1.setIzena("Equipo 1");

	    T2 = new Talde();
	    T2.setIzena("Equipo 2");

	    lT = new ArrayList<>();
	    lT.add(T1);
	    lT.add(T2);

	    Pt = new ArrayList<>();
	    Pt.add(5);
	    Pt.add(10);

	    S = new Sailkapena(lT, Pt);
	}

	@Test
	void SailkapenaConstructor1Test() {
		Sailkapena tS = new Sailkapena();
		 assertEquals(tS.getTaldeak().size(),0);
		 assertEquals(tS.getPuntuak().size(),0);
	}

	@Test
	void SailkapenaConstructor2Test() {
		 assertEquals(S.getTaldeak().get(1),T2);
		 assertEquals(S.getPuntuak().get(1),10);
	}

	@Test
	void setPuntuakTest() {
		ArrayList tPt = new ArrayList<Integer>();
		tPt.add(3);
		S.setPuntuak(tPt);
		assertEquals(S.getPuntuak(),tPt);
	}

	@Test
	void setTaldeakTest() {
		ArrayList tT = new ArrayList<>();
		S.setTaldeak(tT);
		assertEquals(S.getTaldeak(),tT);
	}

	@Test
	void gehituTaldeaTest() {
	    Talde T3 = new Talde();
	    T3.setIzena("Equipo 3");

	    int sizeAntes = S.getTaldeak().size();

	    S.gehituTaldea(T3);

	    assertEquals(sizeAntes + 1, S.getTaldeak().size());
	    assertEquals(T3, S.getTaldeak().get(sizeAntes));

	    assertEquals(sizeAntes + 1, S.getPuntuak().size());
	    assertEquals(0, S.getPuntuak().get(sizeAntes));
	}
	@Test
	void gehituTaldeaConListasNull() {
	    Sailkapena sNull = new Sailkapena(null, null);

	    Talde t = new Talde();
	    t.setIzena("Equipo X");

	    sNull.gehituTaldea(t);

	    assertNotNull(sNull.getTaldeak());
	    assertNotNull(sNull.getPuntuak());

	    assertEquals(1, sNull.getTaldeak().size());
	    assertEquals(t, sNull.getTaldeak().get(0));

	    assertEquals(1, sNull.getPuntuak().size());
	    assertEquals(0, sNull.getPuntuak().get(0));
	}

	@Test
	void SailkapenaOrdenatuTest1() {
		S.SailkapenaOrdenatu(S);
		assertEquals(S.getTaldeak().getLast(),T1);
		assertEquals(S.getPuntuak().getLast(),5);
	}

	@Test
	void SailkapenaOrdenatuTest2() {
		Talde T3 = new Talde();
		S.gehituTaldea(T3);
		S.SailkapenaOrdenatu(S);
		assertEquals(S.getTaldeak().getLast(),T3);
		assertEquals(S.getPuntuak().getLast(),0);
	}
}


