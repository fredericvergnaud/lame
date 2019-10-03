package modeles.evenements;

import java.util.EventObject;
import modeles.ListeModel;

public class ProjetListeAddedEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private ListeModel newListe;
	private int newNbreListes;

	public ProjetListeAddedEvent(Object source, ListeModel newListe, int newNbreListes) {
		super(source);
		this.newListe = newListe;
		this.newNbreListes = newNbreListes;
	}

	public ListeModel getNewListe() {
		return newListe;
	}
	
	public int getNewNbreListes() {
		return newNbreListes;
	}
}
