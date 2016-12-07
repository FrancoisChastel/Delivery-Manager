package tests.unitaires.engine;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.engine.Pair;
import org.junit.Test;

public class PairUTest {

	
	@Before
	public void setUp() {
		int a = 4;
		int b = 6;
		Pair c = new Pair(a,b);
	}
	
	@After
	public void tearDown() {
		
	}

	/**
	 * Test hash code with different Pairs
	 */
	@Test
	public void hashCodeTest1() {
		Pair a = new Pair (5,2);
		Pair b = new Pair (3,9);
		assertEquals(a.hashCode()==b.hashCode(), false);
	}
	
	/**
	 * Test equals when true
	 */
	@Test
	public void equalsTest1() {
		Pair a = new Pair (5,2);
		Pair b = new Pair (5,2);
		assertEquals(a.equals(b), true);
	}

	/**
	 * Test equals when false
	 */
	@Test
	public void equalsTest2() {
		Pair a = new Pair (5,2);
		Pair b = new Pair (5,4);
		assertEquals(a.equals(b), false);
	}
	
	/**
	 * Test toString
	 */
	@Test
	public void toStringTest1() {
		Pair a = new Pair (5,2);
		assertEquals(a.toString() instanceof String, true);
	}
	
	/**
	 * Test getFirst
	 */
	@Test
	public void getFirstTest1() {
		Pair a = new Pair (5,2);
		assertEquals(a.getFirst(), 5);
	}

	/**
	 * Test getSecond
	 */
	@Test
	public void getSecondTest1() {
		Pair a = new Pair (5,2);
		assertEquals(a.getSecond(), 2);
	}
	
	/**
	 * Test setFirst
	 */
	@Test
	public void setFirstTest1() {
		Pair a = new Pair (5,2);
		a.setFirst(4);
		assertEquals(a.getFirst(), 4);
	}
	
	/**
	 * Test setSecond
	 */
	@Test
	public void setSecondTest1() {
		Pair a = new Pair (5,2);
		a.setSecond(4);
		assertEquals(a.getSecond(), 4);
	}
}
