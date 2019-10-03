package controleurs.operations.liste;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import vue.dialog.DialogPanelNettoyageAutoSujets;
import vue.dialog.DialogPanelNettoyageManuelSujets;
import comparators.MapStringIntegerComparator;
import controleurs.vuesabstraites.ProjetView;
import modeles.MessageModel;

public class NettoyageSujets {

	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private List<MessageModel> listMessages;

	int nbreModifs = 0;

	public NettoyageSujets(ProjetView activitesView, ResourceBundle bundleOperationsListe, List<MessageModel> listMessages) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.listMessages = listMessages;
	}

	public void nettoyageAutoSujets() {
		activitesView.resetProgress();
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_NettoyageSujets") + " - " + bundleOperationsListe.getString("txt_Patientez"));
		activitesView.getProgressBar().setIndeterminate(true);
		activitesView.getProgressBar().setStringPainted(false);
		activitesView.updateProgress();
		Map<String, Integer> mapMotsDansSujetAEnlever = new HashMap<String, Integer>();
		for (MessageModel message : listMessages) {
			String sujet = message.getSujet();
			Pattern p = Pattern.compile("\\[.*?\\]");
			Matcher m = p.matcher(sujet);
			while (m.find()) {
				// System.out.println(m.group());
				String aEnlever = m.group();
				if (!mapMotsDansSujetAEnlever.containsKey(aEnlever)) {
					int nbre = new Integer(1);
					mapMotsDansSujetAEnlever.put(aEnlever, nbre);
				} else {
					int nbre = mapMotsDansSujetAEnlever.get(aEnlever);
					mapMotsDansSujetAEnlever.put(aEnlever, nbre + 1);
				}
			}
		}

		// SUPPRESSION DES MOTS CITES UNE SEULE FOIS + TRI
		// for (Iterator<Entry<String, Integer>> i = mapMotsDansSujetAEnlever
		// .entrySet().iterator(); i.hasNext();) {
		// Entry<String, Integer> entry = i.next();
		// if (entry.getValue() ==1) {
		// String o = entry.getKey();
		// i.remove();
		// System.out
		// .println("mot supprimé de mapMotsDansSujetAEnlever : "
		// + o);
		// }
		// }
		if (mapMotsDansSujetAEnlever.size() > 0) {
			MapStringIntegerComparator msic = new MapStringIntegerComparator(mapMotsDansSujetAEnlever);
			Map<String, Integer> mapMotsDansSujetAEnleverTrie = new TreeMap<String, Integer>(msic);
			mapMotsDansSujetAEnleverTrie.putAll(mapMotsDansSujetAEnlever);

			// AFFICHAGE MAP
			// System.out.println("MAP : ");
			// for (Entry<String, Integer> e :
			// mapMotsDansSujetAEnlever.entrySet())
			// System.out.println(e.getKey() + " a été cité " + e.getValue()
			// + " fois");

			// AFFICHAGE MAP TRIEE
			// System.out.println("MAP TRIEE : ");
			// for (Entry<String, Integer> e :
			// mapMotsDansSujetAEnleverTrie.entrySet())
			// System.out.println(e.getKey() + " a été cité " + e.getValue()
			// + " fois");

			DialogPanelNettoyageAutoSujets optPanel = new DialogPanelNettoyageAutoSujets(mapMotsDansSujetAEnleverTrie, bundleOperationsListe);
			int result = JOptionPane.showOptionDialog(null, optPanel, bundleOperationsListe.getString("txt_NettoyageSujets"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null,
					null);
			if (result == JOptionPane.OK_OPTION) {
				JPanel panelChk = (JPanel) ((JScrollPane) optPanel.getComponent(1)).getViewport().getView();
				Component[] tabComp = panelChk.getComponents();
				for (int i = 0; i < tabComp.length; i++) {
					JCheckBox box = (JCheckBox) tabComp[i];
					if (box.isSelected()) {
						String motASuppr = tabComp[i].getName();
						// System.out.println(motASuppr + " est selectionne");
						for (MessageModel message : listMessages) {
							String sujet = message.getSujet();
							if (sujet.indexOf(motASuppr) != -1) {
								sujet = sujet.replace(motASuppr, "");
								sujet = sujet.trim();
								nbreModifs++;
							}
							if (sujet.indexOf("[") != -1 && sujet.indexOf("]") == -1) {
								// System.out.println("sujet = " + sujet);
								sujet = sujet.replace("[", "");
								sujet = sujet.trim();
								// System.out.println("sujet nettoye = " +
								// sujet);
							}
							message.setSujet(sujet);
							CalculSujetTronque cst = new CalculSujetTronque(sujet);
							message.setSujetTronque(cst.getSujetTronque());

						}
					}
				}
			}
		} else
			JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_NettoyageAutomatiqueImpossible"), bundleOperationsListe.getString("txt_NettoyageSujets"),
					JOptionPane.INFORMATION_MESSAGE);
	}

	public void nettoyageManuelSujets() {
		activitesView.resetProgress();
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_NettoyageSujets") + " - " + bundleOperationsListe.getString("txt_Patientez"));
		activitesView.getProgressBar().setIndeterminate(true);
		activitesView.getProgressBar().setStringPainted(false);
		activitesView.updateProgress();
		DialogPanelNettoyageManuelSujets optPanel = new DialogPanelNettoyageManuelSujets(bundleOperationsListe);
		String[] options = new String[2];
		options[1] = bundleOperationsListe.getString("txt_ModifierSujets");
		options[0] = bundleOperationsListe.getString("txt_Annuler");
		int result = JOptionPane.showOptionDialog(null, optPanel, bundleOperationsListe.getString("txt_NettoyageSujets"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
				null);
		if (result == 1) {
			JTextArea jtaMotsASuppr = optPanel.getJtaMotsASuppr();
			String[] tabMotsASuppr;
			if (!jtaMotsASuppr.getText().equals("")) {
				tabMotsASuppr = jtaMotsASuppr.getText().replace("\n", "").split(";");
				int nbreMotsASuppr = tabMotsASuppr.length;
				System.out.println("nbre de mots a suppr = " + nbreMotsASuppr);
				for (int i = 0; i < tabMotsASuppr.length; i++) {
					String motASuppr = tabMotsASuppr[i];
					for (MessageModel message : listMessages) {
						String sujet = message.getSujet();
						if (sujet.indexOf(motASuppr) != -1) {
							sujet = sujet.replace(motASuppr, "").trim();
							message.setSujet(sujet);
							CalculSujetTronque cst = new CalculSujetTronque(sujet);
							message.setSujetTronque(cst.getSujetTronque());
							nbreModifs++;
						}
					}
				}
				jtaMotsASuppr.setText("");
			} else {
				JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ChoisirAuMoinsUnMot"), bundleOperationsListe.getString("txt_Erreur"), JOptionPane.ERROR_MESSAGE);
				nettoyageManuelSujets();
			}
		} else
			return;
	}

	public int getNbreModifs() {
		return nbreModifs;
	}
}
