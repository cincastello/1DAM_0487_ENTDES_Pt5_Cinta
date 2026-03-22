/*package org.entdes.todolist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GestorTasquesMockTests {

    @Mock
    private INotificador notificadorMock;

    @Mock
    private Tasca tascaMock;

    @Mock
    private List<Tasca> llistaTasquesMock;

    @Spy
    private ConsoleNotificador notificadorSpy;

    @Captor
    private ArgumentCaptor<String> missatgeCaptor;

    @TempDir
    File tempDir;

    private GestorTasques gestor;
    private GestorTasques gestorAmbSpy;

    @BeforeEach
    void setUp() {
        gestor = new GestorTasques(notificadorMock);
        gestorAmbSpy = new GestorTasques(notificadorSpy);
    }

    @Test
    void testMockNotificadorRetornaTrueQuanCridat() throws Exception {
        when(notificadorMock.notificar(anyString())).thenReturn(true);

        int id = gestor.afegirTasca("Tasca mock", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), 3);

        assertTrue(id > 0);
        verify(notificadorMock, times(1)).notificar(anyString());
    }

    @Test
    void testMockNotificadorRetornaFalseLlencaExcepcio() {
        when(notificadorMock.notificar(anyString())).thenReturn(false);

        Exception exception = assertThrows(Exception.class, () -> {
            gestor.afegirTasca("Tasca que falla", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), 3);
        });

        assertTrue(exception.getMessage().contains("notificació"));
        verify(notificadorMock, times(1)).notificar(anyString());
    }

    @Test
    void testMockNotificadorVerificaMissatgeCorrecte() throws Exception {
        when(notificadorMock.notificar(anyString())).thenReturn(true);
        String descripcioEsperada = "Tasca importantíssima";

        gestor.afegirTasca(descripcioEsperada, LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), 5);

        verify(notificadorMock).notificar(missatgeCaptor.capture());
        String missatgeCapturat = missatgeCaptor.getValue();
        
        assertTrue(missatgeCapturat.contains(descripcioEsperada));
        assertTrue(missatgeCapturat.contains("creada"));
        assertTrue(missatgeCapturat.contains("prioritat 5"));
    }

    @Test
    void testMockNotificadorEsCridatEnModificarTasca() throws Exception {
        when(notificadorMock.notificar(anyString())).thenReturn(true);
        
        int id = gestor.afegirTasca("Original", LocalDate.now().plusDays(1), null, 2);

        gestor.modificarTasca(id, "Modificada", false, LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), 4);

        verify(notificadorMock, times(2)).notificar(anyString());
    }

    @Test
    void testMockNotificadorEsCridatEnCompletarTasca() throws Exception {
        when(notificadorMock.notificar(anyString())).thenReturn(true);
        
        int id = gestor.afegirTasca("Tasca a completar", LocalDate.now().plusDays(1), null, 2);

        gestor.marcarCompletada(id);

        verify(notificadorMock, times(2)).notificar(anyString());
        verify(notificadorMock, times(1)).notificar(
            argThat(missatge -> missatge.contains("completada"))
        );
    }

    @Test
    void testMockNotificadorNoEsCridatSiValidacioFalla() {
        when(notificadorMock.notificar(anyString())).thenReturn(true);

        assertThrows(Exception.class, () -> {
            gestor.afegirTasca("", LocalDate.now().plusDays(1), 
                              LocalDate.now().plusDays(5), 3);
        });

        verify(notificadorMock, never()).notificar(anyString());
    }

    @Test
    void testSpyNotificadorComportamentReal() throws Exception {
        int id = gestorAmbSpy.afegirTasca("Tasca", LocalDate.now().plusDays(1), LocalDate.now().plusDays(5), 3);
        
        assertTrue(id > 0);
        verify(notificadorSpy, times(1)).notificar(anyString());
    }

    @Test
    void testSpyNotificadorCapturaMissatges() throws Exception {
        String descripcio = "Captura";
        
        gestorAmbSpy.afegirTasca(descripcio, LocalDate.now().plusDays(1), null, 1);

        verify(notificadorSpy).notificar(argThat(missatge -> 
            missatge.contains(descripcio) && missatge.contains("creada")
        ));
    }

    @Test
    void testSpyNotificadorMultipleInteraccions() throws Exception {
        int id = gestorAmbSpy.afegirTasca("Primera", LocalDate.now().plusDays(1), null, 2);
        
        gestorAmbSpy.modificarTasca(id, "Segona", false, LocalDate.now().plusDays(1), null, 3);
        
        gestorAmbSpy.marcarCompletada(id);

        verify(notificadorSpy, times(3)).notificar(anyString());
    }

    @Test
    void testMockTascaComportament() {
        when(tascaMock.getId()).thenReturn(999);
        when(tascaMock.getDescripcio()).thenReturn("Mock Tasca");
        when(tascaMock.getPrioritat()).thenReturn(5);
        when(tascaMock.isCompletada()).thenReturn(false);
        when(tascaMock.getDataInici()).thenReturn(LocalDate.now());
        when(tascaMock.getDataFiPrevista()).thenReturn(LocalDate.now().plusDays(10));

        assertEquals(999, tascaMock.getId());
        assertEquals("Mock Tasca", tascaMock.getDescripcio());
        assertEquals(5, tascaMock.getPrioritat());
        assertFalse(tascaMock.isCompletada());
    }

    @Test
    void testMockTascaModificacio() {
        when(tascaMock.isCompletada()).thenReturn(false);
        doNothing().when(tascaMock).setCompletada(true);
        
        tascaMock.setCompletada(true);
        
        verify(tascaMock, times(1)).setCompletada(true);
    }

    @Test
    void testMockTascaComparacio() throws Exception {
        Tasca tascaReal = new Tasca("Real");
        when(tascaMock.getDescripcio()).thenReturn("Mock");
        when(tascaMock.getPrioritat()).thenReturn(3);

        assertNotEquals(tascaReal.getDescripcio(), tascaMock.getDescripcio());
        assertEquals(tascaReal.getPrioritat(), tascaMock.getPrioritat());
    }

    @Test
    void testMockLlistaTasques() {
        List<Tasca> llistaMock = mock(List.class);
        when(llistaMock.size()).thenReturn(5);
        when(llistaMock.get(0)).thenReturn(tascaMock);
        
        assertEquals(5, llistaMock.size());
        assertEquals(tascaMock, llistaMock.get(0));
    }

    @Test
    void testMockLlistaAmbComportament() {
        List<Tasca> llistaMock = mock(List.class);
        when(llistaMock.isEmpty()).thenReturn(false);
        when(llistaMock.contains(any())).thenReturn(true);
        
        assertFalse(llistaMock.isEmpty());
        assertTrue(llistaMock.contains(tascaMock));
    }



    @Test
    void testMockValidacioDescripcioBuida() {
        assertThrows(Exception.class, () -> {
            gestor.afegirTasca("", LocalDate.now().plusDays(1), null, 1);
        });
        verify(notificadorMock, never()).notificar(anyString());
    }

    @Test
    void testMockValidacioDescripcioNull() {
        assertThrows(Exception.class, () -> {
            gestor.afegirTasca(null, LocalDate.now().plusDays(1), null, 1);
        });
        verify(notificadorMock, never()).notificar(anyString());
    }

    @Test
    void testMockValidacioDataFiAnteriorDataInici() {
        LocalDate dataInici = LocalDate.of(2025, 3, 10);
        LocalDate dataFiPrevista = LocalDate.of(2024, 4, 2);
        
        assertThrows(Exception.class, () -> {
            gestor.afegirTasca("Tasca", dataInici, dataFiPrevista, 1);
        });
        verify(notificadorMock, never()).notificar(anyString());
    }

    @Test
    void testMockValidacioDataIniciAnteriorActual() {
        LocalDate dataInici = LocalDate.now().minusDays(1);
        LocalDate dataFiPrevista = LocalDate.now().plusDays(10);
        
        assertThrows(Exception.class, () -> {
            gestor.afegirTasca("Tasca", dataInici, dataFiPrevista, 1);
        });
        verify(notificadorMock, never()).notificar(anyString());
    }

    @Test
    void testMockValidacioPrioritatMenorRang() throws Exception {
        int id = gestor.afegirTasca("Tasca vàlida", 
                                   LocalDate.now().plusDays(1), 
                                   null, 1);
        
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(id, "Modificada", false, 
                                 LocalDate.now().plusDays(1), 
                                 null, 0);
        });
        
        assertEquals("La prioritat ha de ser un valor entre 1 i 5", exception.getMessage());
    }

    @Test
    void testMockValidacioPrioritatMajorRang() throws Exception {
        int id = gestor.afegirTasca("Tasca vàlida", 
                                   LocalDate.now().plusDays(1), 
                                   null, 5);
        
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.modificarTasca(id, "Modificada", false, 
                                 LocalDate.now().plusDays(1), 
                                 null, 6);
        });
        
        assertEquals("La prioritat ha de ser un valor entre 1 i 5", exception.getMessage());
    }

    @Test
    void testMockEliminarTascaNoExistent() {
        assertThrows(Exception.class, () -> {
            gestor.eliminarTasca(999);
        });
    }

    @Test
    void testMockObtenirTascaNoExistent() {
        assertThrows(Exception.class, () -> {
            gestor.obtenirTasca(999);
        });
    }

    @Test
    void testMockModificarTascaNoExistent() {
        assertThrows(Exception.class, () -> {
            gestor.modificarTasca(999, "Modificada", false, 
                                 LocalDate.now().plusDays(1), 
                                 null, 3);
        });
    }

    @Test
    void testMockMarcarCompletadaTascaNoExistent() throws Exception {
        gestor.afegirTasca("Tasca real", LocalDate.now().plusDays(1), null, 2);
        
        Exception exception = assertThrows(Exception.class, () -> {
            gestor.marcarCompletada(999);
        });
        
        assertEquals("La tasca no existeix", exception.getMessage());
    }

    @Test
    void testMockValidacioTascaDuplicada() throws Exception {
        String descripcio = "Tasca duplicada";
        LocalDate dataInici = LocalDate.of(2027, 3, 9);
        LocalDate dataFi = LocalDate.of(2027, 3, 10);
        
        gestor.afegirTasca(descripcio, dataInici, dataFi, 1);
        
        assertThrows(Exception.class, () -> {
            gestor.afegirTasca(descripcio, dataInici, dataFi, 2);
        });
    }

    @Test
    void testMockEncadenatNotificador() throws Exception {
        when(notificadorMock.notificar(anyString()))
            .thenReturn(true)
            .thenReturn(false);
        
        int id = gestor.afegirTasca("Primera", 
                                   LocalDate.now().plusDays(1), 
                                   null, 2);
        
        assertThrows(Exception.class, () -> {
            gestor.afegirTasca("Segona", 
                              LocalDate.now().plusDays(1), 
                              null, 3);
        });
        
        verify(notificadorMock, times(2)).notificar(anyString());
    }

    @Test
    void testMockAmbArgumentMatchersComplexos() throws Exception {
        when(notificadorMock.notificar(argThat(missatge -> 
            missatge != null && 
            missatge.contains("Tasca") &&
            missatge.length() > 10
        ))).thenReturn(true);
        
        int id = gestor.afegirTasca("Tasca amb matcher", 
                                   LocalDate.now().plusDays(1), 
                                   null, 4);
        
        assertTrue(id > 0);
        verify(notificadorMock, times(1)).notificar(anyString());
    }

    @Test
    void testMockVerificaOrdreCrides() throws Exception {
        when(notificadorMock.notificar(anyString())).thenReturn(true);
        
        int id = gestor.afegirTasca("Ordre", 
                                   LocalDate.now().plusDays(1), 
                                   null, 2);
        
        gestor.marcarCompletada(id);
        
        InOrder inOrder = inOrder(notificadorMock);
        inOrder.verify(notificadorMock).notificar(contains("creada"));
        inOrder.verify(notificadorMock).notificar(contains("completada"));
    }

    @Test
    void testMockAmbRespostaPersonalitzada() throws Exception {
        when(notificadorMock.notificar(anyString())).thenAnswer(invocation -> {
            String missatge = invocation.getArgument(0);
            if (missatge.contains("urgent")) {
                return true;
            }
            return false;
        });
        
        int id = gestor.afegirTasca("Tasca urgent", 
                                   LocalDate.now().plusDays(1), 
                                   null, 5);
        
        assertTrue(id > 0);
        verify(notificadorMock, times(1)).notificar(contains("urgent"));
    }

    @Test
    void testMockLlistarTasquesAmbFiltre() throws Exception {
        Tasca tasca1 = mock(Tasca.class);
        Tasca tasca2 = mock(Tasca.class);
        
        when(tasca1.getDescripcio()).thenReturn("Comprar pa");
        when(tasca2.getDescripcio()).thenReturn("Llegir llibre");
        when(tasca1.isCompletada()).thenReturn(true);
        when(tasca2.isCompletada()).thenReturn(false);
        
        List<Tasca> tasquesMock = new ArrayList<>();
        tasquesMock.add(tasca1);
        tasquesMock.add(tasca2);
        
        GestorTasques gestorMock = mock(GestorTasques.class);
        when(gestorMock.llistarTasquesPerDescripcio("pa")).thenReturn(List.of(tasca1));
        when(gestorMock.llistarTasquesPerComplecio(true)).thenReturn(List.of(tasca1));
        
        List<Tasca> resultatDescripcio = gestorMock.llistarTasquesPerDescripcio("pa");
        List<Tasca> resultatComplecio = gestorMock.llistarTasquesPerComplecio(true);
        
        assertEquals(1, resultatDescripcio.size());
        assertEquals("Comprar pa", resultatDescripcio.get(0).getDescripcio());
        assertEquals(1, resultatComplecio.size());
        assertTrue(resultatComplecio.get(0).isCompletada());
    }

    @Test
    void testMockNotificadorPerformance() throws Exception {
        when(notificadorMock.notificar(anyString())).thenReturn(true);
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            gestor.afegirTasca("Tasca " + i, 
                              LocalDate.now().plusDays(1), 
                              null, 
                              (i % 5) + 1);
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        assertTrue(duration < 5000, "Hauria de processar 100 tasques en menys de 5 segons");
        verify(notificadorMock, times(100)).notificar(anyString());
    }
}
    */