/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.va.ascent.demo.service.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gov.va.ascent.framework.messages.MessageSeverity;

/**
 *
 * @author rthota
 */
public class DemoServiceExceptionTest {
	DemoServiceException instance;
	
	@Before
	public void setUp(){
        instance = new DemoServiceException(MessageSeverity.ERROR, "EVSS-7001", "Invalid Pid");
	}
    /**
     * Test of getSeverity method, of class DemoServiceException.
     */
    @Test
    public void testGetSeverity() {
        System.out.println("getSeverity");
        MessageSeverity expResult = MessageSeverity.ERROR;
        MessageSeverity result = instance.getSeverity();
        assertEquals(expResult, result);
    }


    /**
     * Test of getKey method, of class DemoServiceException.
     */
    @Test
    public void testGetKey() {
        System.out.println("getKey");
        String expResult = "EVSS-7001";
        String result = instance.getKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of setKey method, of class DemoServiceException.
     */


    /**
     * Test of getMessage method, of class DemoServiceException.
     */
    @Test
    public void testGetMessage() {
        System.out.println("getMessage");
        String expResult = "Invalid Pid";
        String result = instance.getMessage();
        assertEquals(expResult, result);

    }

}
