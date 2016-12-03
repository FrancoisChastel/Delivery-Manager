package TraceRoute;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import model.deliverymanager.DeliveryManager;
import model.graph.MapNode;

public abstract class HtmlGenerator {
	public static void generateHtml(List <Instruction> instructions,DeliveryManager deliveryManager,File fileHtml) 
	{
		if (fileHtml.exists()) {
			System.out.println("Le fichier existe deja");
			fileHtml.delete();
		}
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileWriter = new FileWriter(fileHtml);
			//
			bufferedWriter = new BufferedWriter(fileWriter);

			String htmlPage = "<!DOCTYPE html >\n" + "<html>" + "<head>"
					+ "<link href='http://fonts.googleapis.com/icon?family=Material+Icons' rel='stylesheet'>"
					+ "<link type='text/css' rel='stylesheet' href='css/materialize.min.css'  media='screen,projection'/>"
					+ "<meta name='viewport' content='width=device-width, initial-scale=1.0'/>" + "</head>"
					+ "<body class='light-blue lighten-5'>" + "<div class='container'>" + "<div class='row'>"
					+ "<h1>Feuille de Route</h1>" + "</div>" + "<div class='row'>" + "<ul class='collection'>";
			bufferedWriter.write(htmlPage);
			
			//Update to next Version
			//MapNode entrepot = deliveryManager.getDeliveryOrders().get(key)
			MapNode entrepot = new MapNode(0,0,0);
		
			
			bufferedWriter.append("<li class='collection-item avatar'>"
								+ 	"<img src='images/delivery-truck.png' class='circle green lighten-3'>"
								+ 	"<span class='title'>Dirigez vous vers l'entrepôt aux coordonées GPS suivante : (" + entrepot.getX() + "," + entrepot.getY() + ")</span>"
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
					direction = "Tournez à la " + ((instruction.getIndex() == 1) ? "1ère à gauche"
							: instruction.getIndex() + "ème à gauche");
					image = "images/turn-left.png";
					break;
				case RIGHT:
					direction = "Tournez à la " + ((instruction.getIndex() == 1) ? "1ère à droite"
							: instruction.getIndex() + "ème à droite");
					image = "images/turn-right.png";
					break;
				case TURNAROUND:
					direction = "Faite demi-tour";
					image = "images/turn-around.png";
					break;
				default:
					break;
				}

				// Write bloc <li> Direction
				bufferedWriter.append("<li class='collection-item avatar'>" + "<img src='" + image
						+ "' class='circle green lighten-3'>" + "<span class='title'>" + direction + " direction " +instruction.getRoad() +"</span>"
						+ "<p>Continuez pendant " + instruction.getLength() + " mètres jusqu'à l'intersection " + instruction.getIdDestination() + "</p>" + "</li>");
				
				if(instruction.isDestinationIsDeliveryPoint() && instruction.getIdDestination() != entrepot.getidNode())
				{
					// Write bloc <li> Delivery
					bufferedWriter.append("<li class='collection-item avatar'>"
							+ 	"<img src='images/box.png' class='circle green lighten-3'>"
							+ 	"<span class='title'>Livrez le colis avec l'id : " + instruction.getIdDestination() +"</span>"
							+ "</li>");
				}
			}

			// Write bloc <li> Delivery
			bufferedWriter.append("<li class='collection-item avatar'>"
					+ 	"<img src='images/racing-flag.png' class='circle green lighten-3'>"
					+ 	"<span class='title'>Vous avez fini votre tournée</span>"
					+ "</li>");
			
			// Bottom Html
			bufferedWriter.append("</ul></div></div></body></html>");

			System.out.println("Html Created");
			bufferedWriter.flush();
			fileWriter.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {

				bufferedWriter.close();
				fileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
	}
}
