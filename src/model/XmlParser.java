package model;

import java.io.File;
import java.io.IOException;
import java.sql.Time;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class XmlParser {
	
	Graph<MapNode, Section>  graph = new Graph<MapNode, Section>();
	ArrayList <MapNode> nodeList= new ArrayList<MapNode>();
	ArrayList <Section> sectionList= new ArrayList<Section>();
	
	ArrayList <Delivery> deliveries = new ArrayList<>();
	
	DeliveryOrder delOrder;
	
	public XmlParser(File currentFile) {
		xmlMapParser(currentFile);
	}
	/**
	 * Method used to parse Well-formed XML File
	 */
	public void xmlMapParser(File currentFile) {
	      
			try {	     	  
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
	    		
	      
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
		    // Error from DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    	e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			// Error from Document doc = dBuilder.parse(fXmlFile);
			e.printStackTrace();
		}
	}
	
	public void xmlDeliveriesParser(File currentFile)
	{
		try {	     	  
    	    final File fXmlFile = currentFile;
    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    		Document document = dBuilder.parse(fXmlFile);
    		
    		
    		DocumentTraversal traversal = (DocumentTraversal) document;
    		
    		NodeIterator iterator = traversal.createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);
    		iterator.nextNode();
    		
    		Node n;
    		
    		MapNode entrepotNode = null;
    		String heureDepart;
    		int idDelivery = 0;
    		while((n = iterator.nextNode()) != null)
    		{
    			Element elem = (Element) n;
    			switch(((Element) n).getTagName()){	    				
    				case "entrepot":
    					
    					int entrepotNodeId = Integer.parseInt(elem.getAttribute("adresse"));
    					MapNode entrepotNodeTemp = new MapNode(entrepotNodeId,0,0);
    					entrepotNode = graph.getNodeById(entrepotNodeTemp);
    					heureDepart   = elem.getAttribute("heureDepart");
    	    			break;
    				case "livraison":
    					int idNode = Integer.parseInt(elem.getAttribute("adresse"));
    					int duree = Integer.parseInt(elem.getAttribute("duree"));
    					MapNode no = new MapNode(idNode,0,0);
    					no = graph.getNodeById(no);
    					deliveries.add(new Delivery(idDelivery++,no,duree));
    					
    	    			break;
    	    		default:break;
    			}
    		}
    		delOrder = new DeliveryOrder(0,entrepotNode,new Time(0),deliveries);
    		
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParserConfigurationException e) {
		// TODO Auto-generated catch block
	    // Error from DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    	e.printStackTrace();
	} catch (SAXException e) {
		// TODO Auto-generated catch block
		// Error from Document doc = dBuilder.parse(fXmlFile);
		e.printStackTrace();
	}
	}
	
	public Graph<MapNode, Section> getGraph() {
		return graph;
	}
	public void setGraph(Graph<MapNode, Section> graph) {
		this.graph = graph;
	}
	public ArrayList<MapNode> getNodeList() {
		return nodeList;
	}
	public void setNodeList(ArrayList<MapNode> nodeList) {
		this.nodeList = nodeList;
	}
	public ArrayList<Section> getSectionList() {
		return sectionList;
	}
	public void setSectionList(ArrayList<Section> sectionList) {
		this.sectionList = sectionList;
	}
	public DeliveryOrder getDelOrder() {
		return delOrder;
	}
	public void setDelOrder(DeliveryOrder delOrder) {
		this.delOrder = delOrder;
	}
	
	
	
	

}
