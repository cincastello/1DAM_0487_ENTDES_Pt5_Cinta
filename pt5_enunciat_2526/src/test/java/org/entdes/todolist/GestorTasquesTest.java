package org.entdes.todolist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

public class GestorTasquesTest {
    
    @Test
    public void TestGesTorTasquesNotificador() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        gestor.afegirTasca("llegir", null, null, null);
        assertEquals(1, gestor.getNombreTasques());
    }

    @Test
    public void testDescripcioBuit() {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        assertThrows(Exception.class, () -> {
        gestor.afegirTasca("", null, null, null);
    });
        
    }

    @Test
    public void testDescripcioBuitNull() {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        assertThrows(Exception.class, () -> {
        gestor.afegirTasca(null, null, null, 1);
    });
    }

    @Test
    public void testDataFiAnteriorDataInici() {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        LocalDate dataInici = LocalDate.of(2025, 3, 10);
        LocalDate dataFiPrevista = LocalDate.of(2024, 4, 2);
        assertThrows(Exception.class, () -> {
        gestor.afegirTasca("Tasqueta", dataInici, dataFiPrevista, 1);
        });
    }

    @Test
    public void testDataIniciAnteriorActual() {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        LocalDate dataInici = LocalDate.now().minusDays(1);
        LocalDate dataFiPrevista = LocalDate.of(2026, 2, 8);
        assertThrows(Exception.class, () -> {
            gestor.afegirTasca("Tasca", dataInici, dataFiPrevista, 1);
        });
    }

    @Test
    public void testNoNotifica(){
        INotificador noFunciona = new INotificador(){
        @Override
        public boolean notificar(String missatge) {
            System.out.println("[NOTIFICACIÓ] " + missatge);
            return false;
        }
        };
        GestorTasques gestor = new GestorTasques(noFunciona);
        assertThrows(Exception.class, () -> {
            gestor.afegirTasca("Tasqueta", LocalDate.of(2025, 2, 5), LocalDate.of(2026, 2, 4), 1);
        });
    }
    
    @Test
    public void testEliminarTasca() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id = gestor.afegirTasca("comprar", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 1);
        gestor.eliminarTasca(id);
        assertEquals(0, gestor.getNombreTasques());
    }

    @Test
    public void testEliminarTascaNoExistent() {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());    
        assertThrows(Exception.class, () -> {
            gestor.eliminarTasca(1);
        });
    }

    @Test
    public void testMarcarCompletada() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id = gestor.afegirTasca("comprar", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 1);
        gestor.marcarCompletada(id);
        assertEquals(1, gestor.getNombreTasques());
        List<Tasca> tasquetes = gestor.llistarTasques();        
        assertEquals(1, tasquetes.size());
        assertTrue(tasquetes.get(0).isCompletada());
    }

    @Test
    public void testTascaModificadaNull() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id = gestor.afegirTasca("comprar", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 1);
        try {
            gestor.marcarCompletada(464);
        } catch (Exception e) {
            assertEquals("La tasca no existeix", e.getMessage());
        }
        List<Tasca> tasques = gestor.llistarTasques();
        assertFalse(tasques.get(0).isCompletada());
    }

    @Test
    public void testModificarTascaNul() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        gestor.afegirTasca("Llegir més", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 1);
        try {
        gestor.modificarTasca(1, null, false, LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 1);
        } catch(Exception e) {
            assertEquals("La descripció no pot estar buida.", e.getMessage());
        }
    }

    @Test
    public void testModificarTascaBuit() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        gestor.afegirTasca("LLegir", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 1);
        try {
        gestor.modificarTasca(1, "", false, LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 1);
        } catch(Exception e) {
            assertEquals("La descripció no pot estar buida.", e.getMessage());
        }
    }

    @Test
    public void testDataFiAnteriorDataIniciModificada() throws Exception {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        gestor.afegirTasca("LLegir", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 1);
        LocalDate dataInici = LocalDate.of(2025, 3, 10);
        LocalDate dataFiPrevista = LocalDate.of(2024, 4, 2);
        assertThrows(Exception.class, () -> {
        gestor.modificarTasca(1, "Jugar", false, dataInici, dataFiPrevista, 1);
    });
    }

    /* @Test
    public void testPrioritatNull() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        gestor.afegirTasca("Llegir", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 3);
        assertDoesNotThrow(() -> 
        gestor.modificarTasca(1, "LLegir més", false, LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), null));
    }
        */

    @Test
    public void testPrioritatRangsMenors() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        gestor.afegirTasca("LLegir", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 1);
        Exception exception = assertThrows(Exception.class, () -> {
        gestor.modificarTasca(1, "LLegir", false, LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 0);
    });
        assertEquals("La prioritat ha de ser un valor entre 1 i 5", exception.getMessage());
    }

    @Test
    public void testPrioritatRangsMajors() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        gestor.afegirTasca("LLegir", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 1);
        Exception exception = assertThrows(Exception.class, () -> {
        gestor.modificarTasca(1, "LLegir", false, LocalDate.of(2026, 4, 5), LocalDate.of(2026, 8, 4), 6);
    });
        assertEquals("La prioritat ha de ser un valor entre 1 i 5", exception.getMessage());
    }

    @Test
    void testModificarTasca() throws Exception {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id = gestor.afegirTasca(
                "Tasca original",
                LocalDate.now().plusDays(1),
                null,
                2
        );

        gestor.modificarTasca(
                id,
                "Tasca modificada",
                true,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                4
        );

        Tasca t = gestor.obtenirTasca(id);

        assertEquals("Tasca modificada", t.getDescripcio());
        assertTrue(t.isCompletada());
        assertEquals(4, t.getPrioritat());
    }

    @Test
    void testObtenirTasca() throws Exception {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id = gestor.afegirTasca(
                "Consultar tasca",
                LocalDate.now().plusDays(1),
                null,
                2
        );

        Tasca t = gestor.obtenirTasca(id);

        assertEquals("Consultar tasca", t.getDescripcio());
    }

    @Test
    void testObtenirTascaNoExisteix() {
        assertThrows(Exception.class, () -> {
            GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
            gestor.obtenirTasca(200);
        });
    }

    @Test
    void testLlistarTasques() throws Exception {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        gestor.afegirTasca("Tasca 1", LocalDate.now().plusDays(1), null, 1);
        gestor.afegirTasca("Tasca 2", LocalDate.now().plusDays(1), null, 2);

        List<Tasca> tasques = gestor.llistarTasques();

        assertEquals(2, tasques.size());
    }

    @Test
    void testLlistarTasquesPerDescripcio() throws Exception {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        gestor.afegirTasca("Comprar pa", LocalDate.now().plusDays(1), null, 1);
        gestor.afegirTasca("Comprar aigua", LocalDate.now().plusDays(1), null, 1);

        List<Tasca> resultat = gestor.llistarTasquesPerDescripcio("pa");

        assertEquals(1, resultat.size());
        assertTrue(resultat.get(0).getDescripcio().contains("pa"));
    }

    @Test
    void testLlistarTasquesPerComplecio() throws Exception {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id = gestor.afegirTasca("Tasca feta", LocalDate.now().plusDays(1), null, 1);

        gestor.marcarCompletada(id);

        List<Tasca> completades = gestor.llistarTasquesPerComplecio(true);

        assertEquals(1, completades.size());
        assertTrue(completades.get(0).isCompletada());
    }

    @Test
    void testGetNombreTasques() throws Exception {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        gestor.afegirTasca("Tasca 1", LocalDate.now().plusDays(1), null, 1);
        gestor.afegirTasca("Tasca 2", LocalDate.now().plusDays(1), null, 2);

        assertEquals(2, gestor.getNombreTasques());
    }

    @Test
    public void testModificarTascaFalsa() throws Exception{
    GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
    int id = gestor.afegirTasca("Llegir", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
    gestor.obtenirTasca(id).setCompletada(true);
    gestor.modificarTasca(id, "Comprar", null, LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
    assertEquals(null, gestor.obtenirTasca(id).getDataFiReal());
}

    @Test
    public void testobtenirTasca() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id = gestor.afegirTasca("Llegir", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
        assertEquals(gestor.obtenirTasca(id), gestor.obtenirTasca(id));
    }

    @Test
    public void testobtenirTascaFalse() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id = gestor.afegirTasca("Llegir", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
        assertThrows(Exception.class, () -> gestor.obtenirTasca(2));
    }

    @Test
    public void testvalidarNoExisteixTasca() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id = gestor.afegirTasca("Llegir", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
        assertThrows(Exception.class, () ->
        gestor.afegirTasca("Llegir", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 2)
        );
    }

    @Test
    public void testvalidarSIExisteixTascaIncorrecta() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id = gestor.afegirTasca("Llegir", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
        assertThrows(Exception.class, () ->
            gestor.modificarTasca(1, "Llegir", false, LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1)
        );
    }

    @Test
    public void testllistarTasques() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id1 = gestor.afegirTasca("Llegir", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
        int id2 = gestor.afegirTasca("Llegir més", LocalDate.of(2027, 3, 10), LocalDate.of(2027, 4, 10), 2);
        int id3 = gestor.afegirTasca("Comprar", LocalDate.of(2027, 3, 11), LocalDate.of(2027, 4, 10), 3);
        assertEquals(3, gestor.llistarTasques().size());
    }

    @Test
    public void testllistarTasquesDescripcio() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id1 = gestor.afegirTasca("Llegir", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
        int id2 = gestor.afegirTasca("Llegir més", LocalDate.of(2027, 3, 10), LocalDate.of(2027, 4, 10), 2);
        int id3 = gestor.afegirTasca("Comprar", LocalDate.of(2027, 3, 11), LocalDate.of(2027, 4, 10), 3);
        List<Tasca> resultat = gestor.llistarTasquesPerDescripcio("Llegir més");
        assertEquals(id2, resultat.get(0).getId());
    }

    @Test
    public void testGuardar() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id1 = gestor.afegirTasca("Llegir", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
        File fitxer = new File("tasques.dat");
        gestor.guardar();
        assertTrue(fitxer.exists());
}

    @Test
    public void testcarregarFitxerCorrecte() throws Exception {
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id1 = gestor.afegirTasca("Llegir", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
        gestor.guardar();

        GestorTasques gestor2 = new GestorTasques(new ConsoleNotificador());
        gestor2.carregar();

        assertEquals(1, gestor2.getNombreTasques());
        assertEquals("Llegir", gestor2.llistarTasques().get(0).getDescripcio());
    }

    @Test
    public void testllistarTasquesPerComplecio() throws Exception{
        GestorTasques gestor = new GestorTasques(new ConsoleNotificador());
        int id1 = gestor.afegirTasca("Llegir", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
        gestor.modificarTasca(id1, "Llegir", true, LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
        int id2 = gestor.afegirTasca("Comprar", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
        gestor.modificarTasca(id2, "Comprar", true, LocalDate.of(2027, 3, 9), LocalDate.of(2027, 4, 10), 2);
        int id3 = gestor.afegirTasca("Dormir", LocalDate.of(2027, 3, 9), LocalDate.of(2027, 3, 10), 1);
        gestor.modificarTasca(id3, "Dormir", true, LocalDate.of(2027, 3, 9), LocalDate.of(2027, 4, 11), 3);
        List<Tasca> resultat = gestor.llistarTasquesPerComplecio(true);
        assertEquals(id1, resultat.get(0).getId());
    }

}
