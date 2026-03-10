import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.*;

import java.util.ArrayList;

class ErabiltzaieEpaileTest {
	
	private ErabiltzaileEpaile e;
	
	private Talde T1;
	private Talde T2;
	private Denboraldia d;
	private Jardunaldi j;
	private Partidua p;
	
	
	@BeforeEach
    void setUp() throws Exception {
        e = new ErabiltzaileEpaile("kaixo", "kaixo123");

        T1 = new Talde();
        T1.setIzena("Talde1");

        T2 = new Talde();
        T2.setIzena("Talde2");

        p = new Partidua(T1, T2);

        ArrayList<Partidua> partiduenLista = new ArrayList<>();
        partiduenLista.add(p);

        j = new Jardunaldi(1, partiduenLista);

        ArrayList<Jardunaldi> jardunaldiak = new ArrayList<>();
        jardunaldiak.add(j);

        d = new Denboraldia(2025);
        d.setLigakoJardunaldi(jardunaldiak);
    }

    @Test
    void getErabiltzaileTest() {
        assertEquals("kaixo", e.getErabiltzaile());
    }

    @Test
    void setErabiltzaileTest() {
        e.setErabiltzaile("agur");
        assertEquals("agur", e.getErabiltzaile());
    }

    @Test
    void getPasahitzaTest() {
        assertEquals("kaixo123", e.getPasahitza());
    }

    @Test
    void setPasahitzaTest() {
        e.setPasahitza("agur123");
        assertEquals("agur123", e.getPasahitza());
    }

    @Test
    void sartuEmaitzaTest() {
        e.sartuEmaitza(d, T1, T2, 3, 2);

        assertEquals(3, p.getEtxekoGolak());
        assertEquals(2, p.getKanpokoGolak());
    }

    @Test
    void sartuEmaitzaNoExisteTest() {
        Talde T3 = new Talde();
        T3.setIzena("Talde3");

        e.sartuEmaitza(d, T3, T2, 5, 1);

        assertEquals(-1, p.getEtxekoGolak());
        assertEquals(-1, p.getKanpokoGolak());
    }

    @Test
    void sartuEmaitzaPartidoDuplicadoTest() {
        Partidua p2 = new Partidua(T1, T2);
        j.addPartidua(p2);

        e.sartuEmaitza(d, T1, T2, 1, 1);

        assertEquals(1, p.getEtxekoGolak());
        assertEquals(1, p.getKanpokoGolak());

        assertEquals(-1, p2.getEtxekoGolak());
        assertEquals(-1, p2.getKanpokoGolak());
    }
}


