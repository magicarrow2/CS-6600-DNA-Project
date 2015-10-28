/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stickermodeljava;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jared
 */
public class ConsoleIOTest {
    
    public ConsoleIOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of get3SatFromInputFile method, of class ConsoleIO.
     */
    @Test
    public void testGet3SatFromInputFile() throws Exception {
        System.out.println("get3SatFromInputFile");
        Sat expResult = null;
        Sat result = ConsoleIO.get3SatFromInputFile();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of get3SatFromString method, of class ConsoleIO.
     */
    @Test
    public void testGet3SatFromString() throws Exception {
        System.out.println("get3SatFromString");
        Sat expResult = null;
        Sat result = ConsoleIO.get3SatFromString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of displayHelp method, of class ConsoleIO.
     */
    @Test
    public void testDisplayHelp() {
        System.out.println("displayHelp");
        ConsoleIO.displayHelp();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
