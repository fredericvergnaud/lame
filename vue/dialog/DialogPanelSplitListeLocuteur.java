package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import modeles.LocuteurModel;

public class DialogPanelSplitListeLocuteur extends JPanel {

	private static final long serialVersionUID = 1L;
	private JComboBox locuteursListe;
	private ResourceBundle bundleProjetController;
	private SortedSet<LocuteurModel> setLocuteurs;

	public DialogPanelSplitListeLocuteur(ResourceBundle bundleProjetController, SortedSet<LocuteurModel> setLocuteurs) {
		this.bundleProjetController = bundleProjetController;
		this.setLocuteurs = setLocuteurs;
		createGui();
	}

	public void createGui() {
		JPanel panelLocuteur = new JPanel();
		panelLocuteur.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLocuteur.setOpaque(true);
		JLabel labelLocuteur = new JLabel(bundleProjetController.getString("txt_DecoupageListeSelon") + " : ");
		List<LocuteurModel> listLocuteurs = new ArrayList<LocuteurModel>(setLocuteurs);
		// String[] locuteurs = new String[setLocuteurs.size() + 1];
		// locuteurs[0] = "";
		// for (int i = 1; i < listLocuteurs.size() +1; i++)
		// locuteurs[i] = listLocuteurs.get(i).getNom();
		String[] locuteurs = new String[setLocuteurs.size() + 1];
		for (int i = 0; i < listLocuteurs.size(); i++)
			locuteurs[i] = listLocuteurs.get(i).getNom();
		locuteurs[setLocuteurs.size()] = "";
		Arrays.sort(locuteurs);
		locuteursListe = new JComboBox(locuteurs);
		locuteursListe.setPreferredSize(new Dimension(150, 30));

		panelLocuteur.add(labelLocuteur);
		panelLocuteur.add(locuteursListe);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);

		add(panelLocuteur);

	}

	public String getParamSplit2() {
		return locuteursListe.getSelectedItem().toString();
	}

}
