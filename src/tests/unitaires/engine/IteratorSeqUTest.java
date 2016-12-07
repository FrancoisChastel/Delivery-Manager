package tests.unitaires.engine;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.engine.IteratorSeq;

public class IteratorSeqUTest {

	IteratorSeq iter;
	IteratorSeq iter2;
	
	
	@Before
	public void setUp() {
		Collection<Integer> nonVus = new ArrayList<>();
		int sommetCrt = 4;
		iter2 = new IteratorSeq(nonVus, sommetCrt);
		nonVus.add(1);
		nonVus.add(2);
		nonVus.add(3);
		nonVus.add(4);
		nonVus.add(5);
		
		iter = new IteratorSeq(nonVus, sommetCrt);
	}
	
	@After
	public void tearDown() {
		
	}
	
	
	/**
	 * Test hasNext() when true
	 */
	@Test
	public void hasNextTest1() {
		assertEquals(true, iter.hasNext());
	}
	
	/**
	 * Test hasNext() when false
	 */
	@Test
	public void hasNextTest2() {
		assertEquals(false, iter2.hasNext());
	}
	
	/**
	 * Test next() when possible
	 */
	@Test
	public void nextTest1() {
		assertEquals(5==iter.next(),true);
	}

	
	/**
	 * Test next() when impossible
	 */
	@Test
	public void nextTest2() {
		   try {
			   iter.next();
		    } catch (Exception e) {
		    	assertEquals(e instanceof NoSuchElementException, true);
		    }
	}
}
