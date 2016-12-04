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
import model.deliverymanager.Delivery;
import model.deliverymanager.DeliveryOrder;
import model.graph.Graph;
import model.graph.MapNode;
import model.graph.Section;

import java.util.ArrayList;
import java.util.Date;

public abstract class XmlParser {
	
	/**
	 * Method used to parse Map File XML. It modifies the graph,nodeList,sectionList passed in parameter
	 * @param graph
	 * @param nodeList
	 * @param sectionList
	 * @param currentFile
	 * @throws Exception
	 */
	public static void xmlMapParser(Graph <MapNode, Section> graph,ArrayList <MapNode> nodeList,ArrayList <Section> sectionList,File currentFile) throws Exception{

		graph.emptyGraph();
		nodeList.clear();
		sectionList.clear();
	    final File fXmlFile = currentFile;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document document = dBuilder.parse(fXmlFile);
		
		
		DocumentTraversal traversal = (DocumentTraversal) document;
		
		NodeIterator iterator = traversal.createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);
		iterator.nextNode();
		
		Node n;
		//int nodesCreated = 0;
		//int edgesCreated = 0;
		
		
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
					//nodesCreated++;
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
					//edgesCreated++;
	    			break;
	    		default:break;
			}
		}
		//model.getController().getLogger().write("Nodes created : "+nodesCreated+" - Edges created : "+edgesCreated);	

	}
	
	/**
	 * Method used to parse delivery File XML. It modifies the graph passed in parameter
	 * @param currentFile file to parse
	 * @param graph graph that will be modified in the method.
	 */
	public static DeliveryOrder xmlDeliveriesParser(Graph <MapNode, Section> graph,File currentFile) throws Exception
	{

 	  
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
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		
		while((n = iterator.nextNode()) != null)
		{
			Element elem = (Element) n;
			switch(((Element) n).getTagName()){	    				
				case "entrepot":
					
					int entrepotNodeId = Integer.parseInt(elem.getAttribute("adresse"));
					// Initialization of the entrepotNode object
					entrepotNode = graph.getNodeById(entrepotNodeId);
					heureDepart   = elem.getAttribute("heureDepart");
	    			break;
				case "livraison":
					int idNode = Integer.parseInt(elem.getAttribute("adresse"));
					int duree = Integer.parseInt(elem.getAttribute("duree"));
					// This node is instanciated only for get the real node from the graph
					MapNode no = graph.getNodeById(idNode);
					
					String debutString;
					String finString;
					try
					{
						debutString=elem.getAttribute("debutPlage");
						finString=elem.getAttribute("finPlage");
						
						Date debut = formatter.parse(debutString);
						Date fin = formatter.parse(finString);
						deliveries.add(new Delivery(idDelivery++,no,duree,debut,fin));
					}
					catch(Exception e )
					{
						// This line create the corresponding delivery
						deliveries.add(new Delivery(idDelivery++,no,duree));
					}		
	    			break;
	    		default:break;
			}
		}
		
		// Create the deliveryOrder
		return new DeliveryOrder(0,entrepotNode, formatter.parse(heureDepart),deliveries);

			
	}
	
}
