package model.traceroute;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import model.Tour;
import model.deliverymanager.DeliveryPoint;
import model.graph.MapNode;

/**
 * 
 * @author Christopher
 *
 */
public abstract class HtmlGenerator {
	
	private final static DateFormat df = new SimpleDateFormat("HH:mm");
	
	/**
	 * This method write all instructions in a well-formed html file passed in parameters
	 * @param entrepot : MapNode of entrepot to get X,Y,ID
	 * @param tour : To get List of Delivery Point 
	 * @param instructions : Instructions to print on HTML file
	 * @param fileHtml : File to be written
	 * @throws IOException : File isn't unreachable or is protected
	 */
	public static void generateHtml(MapNode entrepot,Tour tour,List <Instruction> instructions,File fileHtml) throws IOException 
	{
		if (fileHtml.exists()) {
			System.out.println("Le fichier existe deja");
			fileHtml.delete();
		}
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileWriter = new FileWriter(fileHtml);
			bufferedWriter = new BufferedWriter(fileWriter);

			// Print Header and the beginning of body
			String htmlPage = "<!DOCTYPE html >\n" + "<html>" + "<head>"
					+ "<link href='http://fonts.googleapis.com/icon?family=Material+Icons' rel='stylesheet'>"
					+ "<link type='text/css' rel='stylesheet' href='css/materialize.min.css'  media='screen,projection'/>"
					+ "<meta name='viewport' content='width=device-width, initial-scale=1.0'/><meta http-equiv='content-type' content='text/html; charset=utf-8' />" + "</head>"
					+ "<body class='light-blue lighten-5'>" + "<div class='container'>" + "<div class='row'>"
					+ "<h1>Feuille de Route</h1>" + "</div>" + "<div class='row'>" + "<ul class='collection'>";
			bufferedWriter.write(htmlPage);
		
			// Create first LI with all informations about storeAddress
			bufferedWriter.append("<li class='collection-item avatar'>"
								+ 	"<img src='images/delivery-truck.png' class='circle green lighten-3'>"
								+ 	"<span class='title'>Dirigez vous vers l'entrepot aux coordonnees GPS suivante : (" + entrepot.getX() + "," + entrepot.getY() + ")</span>"
								+ 	"<p>Chargez le camion et positionnez vous vers le nord</p>"
								+ "</li>");
			
			String direction = null;
			String image = null;
			
					
			for (Instruction instruction : instructions) {
				// Get Direction and Image from this instruction
				switch (instruction.getDirection()) {
				case STRAIGHT:
					direction = "Allez tout droit";
					image = "images/straight-ahead.png";
					break;
				case LEFT:
					if(instruction.isUniqueOutgoingDestination())
					{
						direction = "Poursuivez";
					}
					else
					{
						if(instruction.isUniqueOutgoingDestinationInItsArea())
						{
							direction = "Tournez a gauche";
						}
						else
						{
							direction = "Tournez a la " + ((instruction.getIndex() == 1) ? "1ere a gauche": instruction.getIndex() + "eme a gauche");			
						}
					}
					image = "images/turn-left.png";
					break;
				case RIGHT:
					if(instruction.isUniqueOutgoingDestination())
					{
						direction = "Poursuivez";
					}
					else
					{
						if(instruction.isUniqueOutgoingDestinationInItsArea())
						{
							direction = "Tournez a droite";
						}
						else
						{
							direction = "Tournez a la " + ((instruction.getIndex() == 1) ? "1ere a droite": instruction.getIndex() + "eme a droite");			
						}
					}
					image = "images/turn-right.png";
					break;
				case TURNAROUND:
					direction = "Faite demi-tour";
					image = "images/turn-around.png";
					break;
				default:
					break;
				}
				
				
				// Write bloc <li> next direction with all informations 
				bufferedWriter.append("<li class='collection-item avatar'>" + "<img src='" + image
						+ "' class='circle green lighten-3'>" + "<span class='title'>" + direction + " sur " +instruction.getRoad() +"</span>"
						+ "<p>Continuez pendant " + instruction.getLength() + " metres" + instruction.getIdDestination()+ "</p>" + "</li>");
				
				// Test if the destination is a DeliveryPoint, in this case add an other LI with informations about delivery 
				if(instruction.isDestinationIsDeliveryPoint() && instruction.getIdDestination() != entrepot.getidNode())
				{
					
					Date arrivingDate = null;
					Date leavingDate = null;
					
					// Get DeliveryPoint ArrivingDate and LeavingDate
					for(DeliveryPoint deliveryPoint: tour.getDeliveryPoints())
					{
						if(deliveryPoint.getMapNodeId() == instruction.getIdDestination())
						{
							arrivingDate = deliveryPoint.getArrivingDate();
							leavingDate = deliveryPoint.getLeavingDate();
						}		
					}
					// Write bloc <li> Delivery
					bufferedWriter.append("<li class='collection-item avatar'>"
							+ 	"<img src='images/box.png' class='circle green lighten-3'>"
							+ 	"<span class='title'>Vous etes arrive au point de livraison "+ instruction.getIdDestination() +"</span>"
							+ 	"<p>Effectuez la livraison entre : "+ df.format(arrivingDate) + " et " + df.format(leavingDate) +"</p>"
							+ "</li>");
				}
			}

			// Write bloc <li> Finish Tour
			bufferedWriter.append("<li class='collection-item avatar'>"
					+ 	"<img src='images/racing-flag.png' class='circle green lighten-3'>"
					+ 	"<span class='title'>Vous avez fini votre tournee</span>"
					+ "</li>");
			
			// Add Bottom Html
			bufferedWriter.append("</ul></div></div></body></html>");

			// Flush Buffer and FileWriter
			bufferedWriter.flush();
			fileWriter.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// Send Exception to the model
			throw e;
		} finally {
			try {

				bufferedWriter.close();
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// Send Exception to the model
				throw e;
			}

		}
		
	}
}
