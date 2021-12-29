package task;

import java.util.Stack;

import util.*;

public class Routenplanung {

	public static void main(String[] args) {
		List<Hotel> tempListHotels = new List<Hotel>();
		List<ArbitraryTree<Hotel>> tempListTrees = new List<ArbitraryTree<Hotel>>();
		
		//Der Einfachheit merken, welche Knoten in welcher Tiefe auftauchen:
		List<ArbitraryTree<Hotel>> depth1 = new List<ArbitraryTree<Hotel>>();
		List<ArbitraryTree<Hotel>> depth2 = new List<ArbitraryTree<Hotel>>();
		List<ArbitraryTree<Hotel>> depth3 = new List<ArbitraryTree<Hotel>>();
		List<ArbitraryTree<Hotel>> depth4 = new List<ArbitraryTree<Hotel>>();
		
		List<ArbitraryTree<Hotel>> bl�tter = new List<ArbitraryTree<Hotel>>();
		
		Datenanalyse d = new Datenanalyse();
		ArbitraryTree<Hotel> start = new ArbitraryTree<Hotel>(new Hotel(0, Double.MAX_VALUE)); //Startwurzel des Baumes, Tiefe 0
		
		//Baum aufbauen, m�gliche Starthotels als Array in Liste �berf�hren und als Nachfolger des Startknotens festlegen, maximale Tiefe von 4, da nur 4 hotels besucht werden k�nnen, um innerhalb von 5 Tagen am Ziel zu sein.
		//Wichtig ist, dass die Hotels mehrmals im Baum auftauchen. Daher m�ssen nebenbei die Bl�tter gespeichert werden, die rechtzeitig zum Ziel f�hren.
		//Tiefe 1:
		tempListHotels = d.convertArrToList(d.getFirstHotels());
		
		tempListHotels.toFirst();
		tempListTrees.toFirst();
		while(tempListHotels.hasAccess()) {
			ArbitraryTree<Hotel> tree = new ArbitraryTree<Hotel>(tempListHotels.getContent());
			tempListTrees.append(tree);
			depth1.append(tree);
			tempListHotels.next();
		}
		
		start.setSuccessors(tempListTrees);
		
		
		//Tiefe 2:
		depth1.toFirst();
		while(depth1.hasAccess()) {
			tempListTrees = null;
			tempListHotels = null;
			
			if(depth1.getContent().getContent().kannLetztesSein() == false) {
				tempListHotels = d.convertArrToList(d.getPossibleNextHotels(depth1.getContent().getContent()));
				tempListHotels.toFirst();
				tempListTrees = new List<ArbitraryTree<Hotel>>();
				
				while(tempListHotels.hasAccess()) {
					ArbitraryTree<Hotel> tree = new ArbitraryTree<Hotel>(tempListHotels.getContent());
					tempListTrees.append(tree);
					depth2.append(tree);
					tempListHotels.next();
				}
				depth1.getContent().setSuccessors(tempListTrees);
			}else if(depth1.getContent().getContent().kannLetztesSein() == true){
				bl�tter.append(depth1.getContent());
			}
			
			depth1.next();
		}
		depth1.toFirst();
		while(!depth1.isEmpty()) {
			depth1.remove();
		}
		depth1 = null;
		
		
		//Tiefe 3:
		depth2.toFirst();
		while(depth2.hasAccess()) {
			tempListTrees = null;
			tempListHotels = null;
			
			if(depth2.getContent().getContent().kannLetztesSein() == false) {
				tempListHotels = d.convertArrToList(d.getPossibleNextHotels(depth2.getContent().getContent()));
				tempListHotels.toFirst();
				tempListTrees = new List<ArbitraryTree<Hotel>>();
				
				while(tempListHotels.hasAccess()) {
					ArbitraryTree<Hotel> tree = new ArbitraryTree<Hotel>(tempListHotels.getContent());
					tempListTrees.append(tree);
					depth3.append(tree);
					tempListHotels.next();
				}
				depth2.getContent().setSuccessors(tempListTrees);
			}else if(depth2.getContent().getContent().kannLetztesSein() == false){
				bl�tter.append(depth2.getContent());
			}			
			depth2.next();
		}
		depth2.toFirst();
		while(!depth2.isEmpty()) {
			depth2.remove();
		}
		depth2 = null;
		
		//Tiefe 4:
		depth3.toFirst();
		while(depth3.hasAccess()) {
			tempListTrees = null;
			tempListHotels = null;
			
			if(depth3.getContent().getContent().kannLetztesSein() == false) {
				tempListHotels = d.convertArrToList(d.getPossibleNextHotels(depth3.getContent().getContent()));
				tempListHotels.toFirst();
				tempListTrees = new List<ArbitraryTree<Hotel>>();
				
				while(tempListHotels.hasAccess()) {
					ArbitraryTree<Hotel> tree = new ArbitraryTree<Hotel>(tempListHotels.getContent());
					tempListTrees.append(tree);
					depth4.append(tree);
					tempListHotels.next();
				}
				depth3.getContent().setSuccessors(tempListTrees);
			}else if(depth3.getContent().getContent().kannLetztesSein() == true){
				bl�tter.append(depth3.getContent());
			}			
			depth3.next();
		}
		depth3.toFirst();
		while(!depth3.isEmpty()) {
			depth3.remove();
		}
		depth3 = null;
		
		//Durchsuchen von depth4 nach fertigen Routen. Sie sind fertig, wenn man vom Inhaltsobjekt des Knotens direkt zum Ziel kommt.
		depth4.toFirst();
		while(depth4.hasAccess()) {
			if(depth4.getContent().getContent().kannLetztesSein() == true) bl�tter.append(depth4.getContent());
			depth4.next();
		}
		depth4.toFirst();
		while(!depth4.isEmpty()) {
			depth4.remove();
		}
		depth4 = null;
		
		
		//Jetzt wird geschaut, welche Route wie gut ist. Um das Arbeiten zu vereinfachen, wird die Liste in einen Array �berf�hrt.
		bl�tter.toFirst();
		int i = 0;
		while(bl�tter.hasAccess()) {
			i++;
			bl�tter.next();
		}
		bl�tter.toFirst();
		
		int j = 0;
		
		ArbitraryTree<Hotel>[] arrBl�tter = (ArbitraryTree<Hotel>[]) new ArbitraryTree[i];
		while(bl�tter.hasAccess()) {
			arrBl�tter[j] = bl�tter.getContent();
			bl�tter.next();
			j++;
		}
		
		
		//Vom Blatt aus m�ssen nun die Vorg�nger identifiziert werden, um die Qualit�t dieser Route zu bewerten.
		ArbitraryTree<Hotel> vorg�nger;
		int indexBesteBewertung = 0;
		double besteBewertung = 0;
		for(int k = 0; k<arrBl�tter.length; k++) {
			vorg�nger = arrBl�tter[k].getPredecessor();
			double bewertung = arrBl�tter[k].getContent().getBewertung();
			while(vorg�nger!=null) {
				if(vorg�nger.getContent().getBewertung()<bewertung) bewertung = vorg�nger.getContent().getBewertung();
				vorg�nger = vorg�nger.getPredecessor();
			}
			if(bewertung>besteBewertung) {
				besteBewertung = bewertung;
				indexBesteBewertung = k;
			}
		}
		
		
		//Nun wurde ein m�glicher, nach der Aufgabenstellung, idealer Weg gefunden.
		//Jetzt wird mit Hilfe eines Stacks vom Blatt ausgehend die Route aus den Vorg�ngern ermittelt.
		//Der Stack korrigiert dabei die verkehrte Reihenfolge beim Auslesen.
		Stack<Hotel> stackHotel = new Stack<Hotel>();
		ArbitraryTree<Hotel> blatt = null;
		if(arrBl�tter.length>0) blatt = arrBl�tter[indexBesteBewertung];
		else {
			System.out.println("Es gibt keine funktionierende Route.");
			System.exit(0);
		}
		
		stackHotel.push(blatt.getContent());
		
		vorg�nger = blatt.getPredecessor();
		while(vorg�nger!=null) {
			stackHotel.push(vorg�nger.getContent());
			vorg�nger = vorg�nger.getPredecessor();
		}
		
		//Startwurzel entfernen:
		stackHotel.pop();
		
		//Somit kann jetzt im finalen Schritt die Route ausgelesen werden, wobei der Stack geleert wird.
		System.out.print("Start --> ");
		while(!stackHotel.isEmpty()) {
			System.out.print("Hotel bei " + stackHotel.peek().getEntfernung() + "min mit Bewertung " + stackHotel.peek().getBewertung() + " --> ");
			stackHotel.pop();
		}
		System.out.println("Ziel");
		System.out.println("Schlechteste Bewertung: " + besteBewertung);
	}	
}
