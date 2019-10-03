package controleurs.vuesabstraites;

import javax.swing.JPanel;
import javax.swing.JToolBar;

import controleurs.ListeController;
import modeles.ecouteurs.ListeListener;

public abstract class ListeView implements ListeListener {

	private ListeController listeController = null;

	public ListeView(ListeController listeController) {
		super();
		this.listeController = listeController;
	}

	public ListeController getListeController() {
		return listeController;
	}

	public abstract JPanel getTabConversationsPanel();
	
	public abstract JPanel getTabLocuteursPanel();

	public abstract String getConversationsTxtToPdf();

	public abstract JPanel getInfosListePanel();

	public abstract JPanel getFilsListePanel();

	public abstract JPanel getAnalysePanel();

	public abstract String getLocuteursTxtToPdf();

	public abstract JPanel getTabMessagesPanel();

	public abstract void setIdentifiantMessageToShow(String identifiantMessage);

	public void setListeController(ListeController listeController) {
		this.listeController = listeController;
	}
	
	public abstract JToolBar getToolBar();
	

}
