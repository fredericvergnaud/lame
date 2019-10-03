package modeles.evenements;

import java.util.EventObject;
import java.util.Set;

import modeles.ListeModel;

public class ProjetChangedEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private String newNom;
	private int newCumulMessages;
	private String newRepertoire;
	private int nbreListes;
	private Set<ListeModel> setListes;
	private ListeModel listeSelected;

	public ProjetChangedEvent(Object source, String newNom,
			int newCumulMessages, String newRepertoire, int newNbreListes,
			Set<ListeModel> setListes, ListeModel listeSelected) {
		super(source);
		this.newNom = newNom;
		this.newCumulMessages = newCumulMessages;
		this.newRepertoire = newRepertoire;
		this.nbreListes = newNbreListes;
		this.setListes = setListes;
		this.listeSelected = listeSelected;
	}

	public String getNewNom() {
		return newNom;
	}

	public int getNewCumulMessages() {
		return newCumulMessages;
	}

	public String getNewRepertoire() {
		return newRepertoire;
	}

	public int getNewNbreListes() {
		return nbreListes;
	}

	public Set<ListeModel> getSetListes() {
		return setListes;
	}
	
	public ListeModel getListeSelected() {
		return listeSelected;
	}
}
