package controleurs.vuesabstraites;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import controleurs.ProjetController;

import modeles.ecouteurs.ProjetListener;

public abstract class ProjetView implements ProjetListener {
	private ProjetController projetController = null;

	public ProjetView(ProjetController projetController) {
		super();
		// System.out.println("projetview : mon controller est "+controller.toString());
		this.projetController = projetController;
	}

	public final ProjetController getProjetController() {
		return projetController;
	}

	public abstract JPanel getInfosProjetPanel();

	public abstract JPanel getListesListePanel();

	public abstract JMenuBar getMenuBar();

	public abstract void setLabelProgress(String string);

	public abstract void showActivites();

	public abstract void updateProgress();

	public abstract void setStepProgress(int step);

	public abstract void close();

	public abstract void resetProgress();

	public abstract void appendTxtArea(String txt);

	public abstract JProgressBar getProgressBar();

	
}
