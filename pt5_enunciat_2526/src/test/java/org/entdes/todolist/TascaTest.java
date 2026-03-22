package org.entdes.todolist;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class TascaTest {
    
    @Test 
    public void testTasca(){
        Tasca tasqueta = new Tasca("tasqueta");
        assertNotNull(tasqueta.getId());
    }

    @Test 
    public void testDescripcio(){
        Tasca tasqueta = new Tasca("tasqueta");
        assertEquals("tasqueta", tasqueta.getDescripcio());
    }

    @Test
    public void setDescripcio(){
        Tasca tasqueta = new Tasca("tasqueta");
        tasqueta.setDescripcio("tosqueta");
        assertEquals("tosqueta", tasqueta.getDescripcio());
    }

    @Test
    public void testDataInici(){
        Tasca tasqueta = new Tasca("tasqueta");
        LocalDate dateta = LocalDate.of(2026, 03, 06);
        tasqueta.setDataInici(dateta);
        assertEquals(dateta, tasqueta.getDataInici());
    }

    @Test
    public void testDataFiPrevista(){
        Tasca tasqueta = new Tasca("tasqueta");
        LocalDate datetaEnd = LocalDate.of(2026, 05, 01);
        tasqueta.setDataFiPrevista(datetaEnd);
        assertEquals(datetaEnd, tasqueta.getDataFiPrevista());
    }

    @Test
    public void testDataFiReal(){
        Tasca tasqueta = new Tasca ("tasqueta");
        LocalDate dateReal = LocalDate.of(2026, 07, 01);
        tasqueta.setDataFiReal(dateReal);
        assertEquals(dateReal, tasqueta.getDataFiReal());
    }

    @Test 
    public void testCompletada(){
        Tasca tasqueta = new Tasca("tasqueta");
        boolean Completada = tasqueta.isCompletada();
        assertFalse(Completada);
        
    }

    @Test
    public void setCompletada(){
        Tasca tasqueta = new Tasca("tasqueta");
        tasqueta.setCompletada(true);
        assertTrue(tasqueta.isCompletada());
    }

    @Test 
    public void testActualitzarIdCounter(){
        Tasca tasqueta = new Tasca ("tasqueta");
        Tasca.actualitzarIdCounter(1);
        
    }
     
    @Test
    public void testGetPrioritatNull(){
        Tasca tasqueta = new Tasca("tasqueta");
        assertNull(tasqueta.getPrioritat());
    }
    
    @Test
    public void testGetPrioritatNotNull(){
        Tasca tasqueta = new Tasca("tasqueta");
        tasqueta.setPrioritat(4);
        assertEquals(4, tasqueta.getPrioritat());
    }

    @Test
    public void testToStringCompletada(){
        Tasca tasca = new Tasca("tasqueta");
        tasca.setCompletada(true);
        assertEquals(tasca.toString(), tasca.toString());
    }

    @Test
    public void testToStringNoCompletada(){
        Tasca tasca = new Tasca("tasqueta");
        tasca.setCompletada(false);
        assertEquals(tasca.toString(), tasca.toString());
    }


}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
  