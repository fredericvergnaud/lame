package modeles;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.event.EventListenerList;

import comparators.ListeNumeroComparator;
import modeles.ecouteurs.ProjetListener;
import modeles.evenements.ProjetChangedEvent;
import modeles.evenements.ProjetListeAddedEvent;

public class ProjetModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nom;
	private transient String repertoire;
	private SortedSet<ListeModel> setListes;
	private EventListenerList listeners;
	private ListeModel listeSelected = null;
	private ListeModel newListe = null;
	// private ListeController listeController;
	private int cumulMessages = 0;

	public ProjetModel() {
		listeners = new EventListenerList();
		setListes = new TreeSet<ListeModel>(new ListeNumeroComparator());
	}

	// // // // // // // // NOUVEAU // // // // // // // // // // // // // // //
	public void createNewProjet(String nom, String repertoire) {
		this.nom = nom;
		this.repertoire = repertoire;
		File rep = new File(repertoire);
		rep.mkdir();
		fireProjetChanged();
	}
	
	public void setNom(String nomProjet) {
		this.nom = nomProjet;
	}
	
	public void setRepertoire(String nomRepertoire){
		this.repertoire = nomRepertoire;
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // OUVERTURE/ // // // // // // // // // // // // // //

	public void setTranscientRepertoire(String repertoire) {
		this.repertoire = repertoire;
		fireProjetChanged();
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // SAUVEGARDE // // // // // // // // // // // // // //

	public void save() {
		try {
			FileOutputStream f = new FileOutputStream(getRepertoire() + "/"
					+ getNom() + ".lame");
			ObjectOutputStream s = new ObjectOutputStream(f);
			s.writeObject(this);
			s.flush();
			s.close();
		} catch (IOException e) {
			System.out.println("Projet/SaveProjet : erreur de sauvegarde : "
					+ e);
			e.printStackTrace();
		}
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // LISTE // // // // // // // // // // // // // // //

	public void setListeSelected(int numListeSelected) {
		for (ListeModel liste : setListes) {
			int existingNumListe = liste.getNumero();
			if (numListeSelected == existingNumListe) {
				this.listeSelected = liste;
				break;
			}
		}
		System.out.println("ProjetModel - setListeSelected : nomListe = "
				+ listeSelected.getNom());
	}

	public void setListeSelected(ListeModel listeSelected) {
		this.listeSelected = listeSelected;
	}

	public ListeModel getListeSelected() {
		return listeSelected;
	}

	// public ListeModel getNewListe() {
	// return newListe;
	// }

	public ListeModel getListe(int numListeSelect) {
		ListeModel listeToFind = null;
		for (ListeModel liste : setListes) {
			int numListe = liste.getNumero();
			if (numListe == numListeSelect) {
				listeToFind = liste;
				break;
			}
		}
		return listeToFind;
	}

	public void addListe(String nom) {
		int numNewListe;
		if (!setListes.isEmpty()) {
			numNewListe = setListes.last().getNumero() + 1;
		} else
			numNewListe = 1;
		// System.out.println("ProjetModel - addListe : newListe = "+nom+" | numero = "+numNewListe);
		ListeModel newListe = new ListeModel(numNewListe);
		newListe.creerListe(nom);
		setListes.add(newListe);
		// System.out.println("ProjetModel - addListe : newListe = "+newListe);
		// setNewCumul();
		setNewListe(newListe);
		// setListeSelected(newListe);
		fireProjetListeAdded();
	}
	
	public void addListe(String nom, Map<String, MessageModel> mapMessages) {
		int numNewListe;
		if (!setListes.isEmpty()) {
			numNewListe = setListes.last().getNumero() + 1;
		} else
			numNewListe = 1;
		// System.out.println("ProjetModel - addListe : newListe = "+nom+" | numero = "+numNewListe);
		ListeModel newListe = new ListeModel(numNewListe);
		newListe.creerListe(nom);
		newListe.addMapIdMessages(mapMessages);
		setListes.add(newListe);
		// System.out.println("ProjetModel - addListe : newListe = "+newListe);
		// setNewCumul();
		setNewListe(newListe);
		// setListeSelected(newListe);
		fireProjetListeAdded();
	}
	
	public void addListe(ListeModel liste){
		setListes.add(liste);		
	}

	public void removeListe(ListeModel listeToRemove) {
		if (setListes.contains(listeToRemove)) {
			listeToRemove.initBeforeRemove(listeToRemove.getNumero());
			setListes.remove(listeToRemove);
		}
		setNewCumul();
		//fireProjetChanged();
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // LISTENERS + CONTROLLERS // // // // // // // // //

	public void addProjetListener(ProjetListener listener) {
		listeners.add(ProjetListener.class, listener);
	}

	// public void fireProjetCreated() {
	// ProjetListener[] listenerList = listeners
	// .getListeners(ProjetListener.class);
	// for (ProjetListener listener : listenerList) {
	// listener.projetCreated(new ProjetCreatedEvent(this, getNom(),
	// getRepertoire()));
	// }
	// save();
	// }

	public void fireProjetChanged() {
		System.out.println("ProjetModel - fireProjetChanged : listeSelected = "+getListeSelected());
		ProjetListener[] listenerList = listeners
				.getListeners(ProjetListener.class);
		for (ProjetListener listener : listenerList) {
			listener.projetChanged(new ProjetChangedEvent(this, getNom(),
					getCumulMessages(), getRepertoire(), getNbreListes(),
					getSetListes(), getListeSelected()));
		}
		save();
	}

	public void fireProjetListeAdded() {
		ProjetListener[] listenerList = listeners
				.getListeners(ProjetListener.class);
		for (ProjetListener listener : listenerList) {
			listener.projetListeAdded(new ProjetListeAddedEvent(this,
					getNewListe(), getNbreListes()));
		}
		save();
	}

	// public void fireProjetListeSelected() {
	// System.out
	// .println("projetModel - fireProjetListeSelected : listeSelected = "
	// + getListeSelected().getNom());
	// ProjetListener[] listenerList = listeners
	// .getListeners(ProjetListener.class);
	// for (ProjetListener listener : listenerList) {
	// listener.projetListeSelected(new ProjetListeSelectedEvent(this,
	// getListeSelected()));
	// }
	// }

	public void removeInfosProjetListener(ProjetListener l) {
		listeners.remove(ProjetListener.class, l);
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // GETTER + SETTER // // // // // // // // // // // //

	public Set<ListeModel> getSetListes() {
		return setListes;
	}

	public String getNom() {
		return nom;
	}

	public String getRepertoire() {
		return repertoire;
	}

	public int getNbreListes() {
		return setListes.size();
	}
	
	// public void setNewListe(ListeModel newListe) {
	// this.newListe = newListe;
	// }

	public void setNewCumul() {
		//System.out.println("ProjetModel - setNewCumul : listeSelected = "+listeSelected.getNom());
		cumulMessages = 0;
		for (ListeModel liste : setListes) {
			cumulMessages += liste.getNbreMessages();
		}
		// System.out.println("cumulfichiers dans projet = " + cumulFichier);
		// System.out.println("cumulmessages dans projet = " + cumulMessages);
		// newListe = null;
		fireProjetChanged();
	}

	public int getCumulMessages() {
		return cumulMessages;
	}

	public ListeModel getNewListe() {
		return newListe;
	}

	public void setNewListe(ListeModel newListe) {
		this.newListe = newListe;
		setListeSelected(this.newListe);
	}

	// // // // // // // // // // // // // // // // // // // // // // // // //

}
