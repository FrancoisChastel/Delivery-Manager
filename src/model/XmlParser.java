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
	public static Graph<MapNode, Section> xmlMapParser() {
	      
			Graph<MapNode, Section>  graph = new Graph<MapNode, Section>();
			ArrayList <MapNode> nodeList= new ArrayList<MapNode>();
    		ArrayList <Section> sectionList= new ArrayList<Section>();
			try {	     	  
	    	    final File fXmlFile = new File("src/plan5x5.xml");
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
		System.out.println(nodeList.size());
		System.out.println(sectionList.size());
		return graph; 
	}

}
