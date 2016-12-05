package tests.unitaires.parser;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import model.Model;
import model.deliverymanager.DeliveryOrder;
import model.graph.GraphDeliveryManager;
import model.graph.MapNode;
import model.graph.Section;
import model.parser.XmlParser;

public class XmlParserTest {

	private Model model;
	
	@Before
	public void setUp() {
		model = new Model(null);
	}
	
	@After
	public void tearDown() {
		model.resetModel();
	}
	
	
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void MapParserTest1() throws Exception {
		File currentFile = new File("XMLs/plan10x10.xml");
		XmlParser.xmlMapParser(model.getGraphDeliveryManager().getGraph(),
				model.getGraphDeliveryManager().getNodeList(), 
				model.getGraphDeliveryManager().getSectionList(), 
				currentFile);
		assertEquals(model.getGraphDeliveryManager().getNodeList().size(), 100);
	}
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void MapParserTest2() throws Exception {
		File currentFile = new File("XMLs/plan25x25.xml");
		XmlParser.xmlMapParser(model.getGraphDeliveryManager().getGraph(),
				model.getGraphDeliveryManager().getNodeList(), 
				model.getGraphDeliveryManager().getSectionList(), 
				currentFile);
		assertEquals(model.getGraphDeliveryManager().getNodeList().size(), 625);
	}	
	
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void DeliveriesParserTest() throws Exception {
		//Load map in order to load deliveries after
		File currentFile = new File("XMLs/plan10x10.xml");
		XmlParser.xmlMapParser(model.getGraphDeliveryManager().getGraph(),
				model.getGraphDeliveryManager().getNodeList(), 
				model.getGraphDeliveryManager().getSectionList(), 
				currentFile);
		currentFile = new File("XMLs/livraisons10x10-5.xml");
		DeliveryOrder delOrder = XmlParser.xmlDeliveriesParser(model.getGraphDeliveryManager().getGraph(),currentFile);
		//Number of deliveries + warehouse
		assertEquals(delOrder.getDeliveryList().size()+1,5);
	}
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void DeliveriesParserTest2() throws Exception {
		//Load map in order to load deliveries after
		File currentFile = new File("XMLs/plan25x25.xml");
		XmlParser.xmlMapParser(model.getGraphDeliveryManager().getGraph(),
				model.getGraphDeliveryManager().getNodeList(), 
				model.getGraphDeliveryManager().getSectionList(), 
				currentFile);
		currentFile = new File("XMLs/livraisons25x25-19.xml");
		DeliveryOrder delOrder = XmlParser.xmlDeliveriesParser(model.getGraphDeliveryManager().getGraph(),currentFile);
		//Number of deliveries + warehouse
		assertEquals(delOrder.getDeliveryList().size()+1,20);
	}
}
