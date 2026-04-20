import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Jardunaldi;
import model.Partidua;
import model.Talde;

class JardunaldiTest {
	private Talde t1;
	private Talde t2;
	private Jardunaldi j;
	private Jardunaldi j2;
	private Partidua p;
	private Partidua p2;
	private ArrayList<Partidua> pl;


	@BeforeEach
	void setUp() throws Exception {
		j = new Jardunaldi(1);
		t1 = new Talde();
		t2 = new Talde();
		pl = new ArrayList<>();
		p = new Partidua(t1, t2);
		p2 = new Partidua(t2, t1, 2, 2);
		j2 = new Jardunaldi(0, pl, 0);
	}
	@Test
	void getJardunaldiZbkTest() {
		j.setJardunaldiZbk(2);
		assertEquals(2, j.getJardunaldiZbk());
	}
	@Test
	void getPartiduakTest() {
		pl.add(p);
		pl.add(p2);
		j.setPartiduak(pl);
		assertEquals(pl, j.getPartiduak());
	}
	@Test
	void addPartiduakTest() {
		j.addPartidua(p);
		assertNotNull(j.getPartiduak());
		j.setPartiduak(null);
		j.addPartidua(p2);
		assertNotNull(j.getPartiduak());
	}

}
