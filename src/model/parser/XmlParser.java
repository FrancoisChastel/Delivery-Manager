package model.parser;

import java.io.File;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import model.Model;
import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryOrder;
import model.graph.Graph;
import model.graph.MapNode;
import model.graph.Section;

import java.util.ArrayList;
import java.util.HashMap;

public class XmlParser {
	
	private Model model;
	/**
	 * Default Constructor
	 */
	public XmlParser(Model model) {
		this.model = model;
	}
	
	/**
	 * Method used to parse Well-formed XML File
	 */
	public void xmlMapParser(File currentFile) throws Exception{
		Graph <MapNode, Section> graph	= model.getGraphDeliveryManager().getGraph();  
		ArrayList <MapNode> nodeList	= model.getGraphDeliveryManager().getNodeList();
		ArrayList <Section> sectionList	= model.getGraphDeliveryManager().getSectionList();
	    final File fXmlFile = currentFile;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(fXmlFile);
		
		
		DocumentTraversal traversal = (DocumentTraversal) document;
		
		NodeIterator iterator = traversal.createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);
		iterator.nextNode();
		
		Node n;
		while((n = iterator.nextNode()) != null)
		{
			Element elem = (Element) n;
			switch(((Element) n).getTagName()){	    				
				case "noeud":
					
					int id = Integer.parseInt(elem.getAttribute("id"));
					int x   = Integer.parseInt(elem.getAttribute("x"));
					int y   = Integer.parseInt(elem.getAttribute("y"));
					MapNode mapNode  = new MapNode(id,x,y);
					graph.add(mapNode);
					nodeList.add(mapNode);					
	    			break;
				case "troncon":
					int idDestination = Integer.parseInt(elem.getAttribute("destination"));
					int idOrigin      = Integer.parseInt(elem.getAttribute("origine"));
					int longueur    = Integer.parseInt(elem.getAttribute("longueur"));
					String nomRue    = elem.getAttribute("nomRue");
					int vitesse     = Integer.parseInt(elem.getAttribute("vitesse"));
					Section section  = new Section(idOrigin,idDestination,nomRue,longueur,vitesse);
	
					
					MapNode origin = nodeList.get(idOrigin);
					MapNode destination = nodeList.get(idDestination);
					graph.addDestination(origin, section, destination);
					sectionList.add(section);    					
	    			break;
	    		default:break;
			}
		}
	    		

	}
	
	/**
	 * Méthod used to parse delivery File XML. It modifies the graph passed in parameter
	 * @param currentFile file to parse
	 * @param graph graph that will be modified in the method.
	 */
	public void xmlDeliveriesParser(File currentFile) throws Exception
	{
		Graph <MapNode, Section> graph	= model.getGraphDeliveryManager().getGraph();  
 	  
	    final File fXmlFile = currentFile;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(fXmlFile);
		
		DocumentTraversal traversal = (DocumentTraversal) document;
		
		NodeIterator iterator = traversal.createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);
		iterator.nextNode();
		
		Node n;    		
		MapNode entrepotNode = null;
		String heureDepart = null;
		int idDelivery = 0;
		ArrayList<Delivery> deliveries = new ArrayList<Delivery>();
		
		while((n = iterator.nextNode()) != null)
		{
			Element elem = (Element) n;
			switch(((Element) n).getTagName()){	    				
				case "entrepot":
					
					int entrepotNodeId = Integer.parseInt(elem.getAttribute("adresse"));
					MapNode entrepotNodeTemp = new MapNode(entrepotNodeId,0,0);
					
					// Initialization of the entrepotNode object
					entrepotNode = graph.getNodeById(entrepotNodeTemp);
					heureDepart   = elem.getAttribute("heureDepart");
	    			break;
				case "livraison":
					int idNode = Integer.parseInt(elem.getAttribute("adresse"));
					int duree = Integer.parseInt(elem.getAttribute("duree"));
					
					// This node is instanciated only for get the real node from the graph
					MapNode no = new MapNode(idNode,0,0);
					no = graph.getNodeById(no);
					
					// This line create the corresponding delivery
					deliveries.add(new Delivery(idDelivery++,no,duree));
	    			break;
	    		default:break;
			}
		}
		SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");

		// Create the deliveryOrder
		model.getDeliveryManager().addDeliveryOrder(new DeliveryOrder(0,entrepotNode, parser.parse(heureDepart),deliveries));
		
		
	}
	
}
