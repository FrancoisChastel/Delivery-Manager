package model;

import java.io.File;
import java.io.IOException;

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

public class XmlParser {
	
	/**
	 * Method used to parse Well-formed XML File
	 */
	public static Graph<Section, MapNode> xmlMapParser() {
	      
			Graph<Section, MapNode>  graph = new Graph<Section, MapNode>();
			try {	     	  
	    	    final File fXmlFile = new File("plan.xml");
	    		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    		Document document = dBuilder.parse(fXmlFile);
	    		
	    		
	    		DocumentTraversal traversal = (DocumentTraversal) document;
	    		
	    		NodeIterator iterator = traversal.createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);
	    		iterator.nextNode();
	    		ArrayList <MapNode> nodeList= new ArrayList<MapNode>();
	    		ArrayList <Section> sectionList= new ArrayList<Section>();
	    		Node n;
	    		while((n = iterator.nextNode()) != null)
	    		{
	    			Element elem = (Element) n;
	    			switch(((Element) n).getTagName()){	    				
	    				case "noeud":
	    					
	    					Long id = Long.parseLong(elem.getAttribute("id"));
	    					Long x   = Long.parseLong(elem.getAttribute("x"));
	    					Long y   = Long.parseLong(elem.getAttribute("y"));
	    					MapNode mapNode  = new MapNode(id,x,y);
	    					graph.add(mapNode);
	    					nodeList.add(mapNode);
	    					
	    	    			break;
	    				case "troncon":
	    					Long idDestination = Long.parseLong(elem.getAttribute("destination"));
	    					Long idOrigin      = Long.parseLong(elem.getAttribute("origine"));
	    					int idOriginTemp   = Integer.parseInt(elem.getAttribute("origine"));
	    					int idDestinationTemp = Integer.parseInt(elem.getAttribute("destination"));
	    					Long longueur    = Long.parseLong(elem.getAttribute("longueur"));
	    					String nomRue    = elem.getAttribute("nomRue");
	    					Long vitesse     = Long.parseLong(elem.getAttribute("vitesse"));
	    					Section section  = new Section(idOrigin,idDestination,nomRue,longueur,vitesse);
	    	
	    					
	    					MapNode origin = nodeList.get(idOriginTemp);
	    					MapNode destination = nodeList.get(idDestinationTemp);
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

		return graph; 
	}

}
