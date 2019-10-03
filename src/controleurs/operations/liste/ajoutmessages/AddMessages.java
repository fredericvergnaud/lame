package controleurs.operations.liste.ajoutmessages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JOptionPane;

import modeles.MessageModel;
import vue.dialog.DialogPanelChoixTypeMessagesListe;
import controleurs.operations.liste.ajoutmessages.bal.AddMessagesFromBalLocale;
import controleurs.operations.liste.ajoutmessages.extractify.AddMessagesFromExtractify;
import controleurs.operations.liste.ajoutmessages.forum.AddMessagesForum;
import controleurs.vuesabstraites.ProjetView;

public class AddMessages {

	private ResourceBundle bundleOperationsListe;
//	private ProjetView activitesView;
//	private Map<String, MessageModel> mapIdMessages;
//	private String repertoire;
//	private int numMessageEnCoursListe;
	private int typeMessagesListeSelected;

	public AddMessages(ResourceBundle bundleOperationsListe
//			, ProjetView activitesView, String repertoire,
//			int numMessageEnCoursListe
			) {
		super();
		this.bundleOperationsListe = bundleOperationsListe;
//		this.activitesView = activitesView;
//		this.repertoire = repertoire;
//		this.numMessageEnCoursListe = numMessageEnCoursListe;
	}

	public void displayDialog() {
		DialogPanelChoixTypeMessagesListe optPanel = new DialogPanelChoixTypeMessagesListe(bundleOperationsListe);
		int result = JOptionPane.showOptionDialog(null, optPanel, bundleOperationsListe.getString("txt_AjoutMessages"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			typeMessagesListeSelected = optPanel.getTypeMessagesListeSelected();
			if (typeMessagesListeSelected != 0) {
				setTypeMessagesListeSelected(typeMessagesListeSelected);				
			} else {
				JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ChoixTypeMessages") + ".",
						bundleOperationsListe.getString("txt_AjoutMessages"), JOptionPane.INFORMATION_MESSAGE);
				displayDialog();
				return;
			}
		}
	}

//	public Map<String, MessageModel> getMapIdMessages() {
//		return mapIdMessages;
//	}
//
//	public void setMapIdMessages(Map<String, MessageModel> mapIdMessages) {
//		this.mapIdMessages = mapIdMessages;
//	}

	public int getTypeMessagesListeSelected() {
		return typeMessagesListeSelected;
	}

	public void setTypeMessagesListeSelected(int typeMessagesListeSelected) {
		this.typeMessagesListeSelected = typeMessagesListeSelected;
	}
}
