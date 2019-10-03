package controleurs.operations.liste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;

import modeles.MessageModel;

import comparators.MessageDateUsComparator;
import controleurs.vuesabstraites.ProjetView;

public class CalculSurMessages {

	private Date debut;
	private Date fin;
	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private ArrayList<MessageModel> listMessages;

	public CalculSurMessages(ProjetView activitesView, ResourceBundle bundleOperationsListe,
			ArrayList<MessageModel> listMessages) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.listMessages = listMessages;
	}

	public void calculeSuivi() {
		// DUREE SUIVI
		activitesView.setStepProgress(1);
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_CalculSuivi"));
		Comparator<MessageModel> byDateUs = new MessageDateUsComparator();
		Collections.sort(listMessages, byDateUs);
		System.out.println("projetController : nom de la liste : "
				+ activitesView.getProjetController().getListeSelected().getNom()
				+ " |  listeController : nom de la liste : "
				+ activitesView.getProjetController().getListeController().getListeSelected().getNom()
				+ " | listMessages size = " + listMessages.size());
		Date debut = listMessages.get(0).getDateUS();
		setDebut(debut);
		Date fin = listMessages.get(listMessages.size() - 1).getDateUS();
		setFin(fin);
	}

	public void calculeNumerotation() {
		// ATTRIBUTION NUMERO
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_CalculNumerotation"));
		String firstMessageNum = listMessages.get(0).getNumero();
		String firstMessageId = listMessages.get(0).getIdentifiant();
		System.out.println("firstMessageNum = " + firstMessageNum + " | firstMessageId = " + firstMessageId);
		if (firstMessageNum.equals("1")) {
			int numero = 1;
			for (MessageModel message : listMessages) {
				String identifiant = message.getIdentifiant();
				System.out.println("identifiant = " + identifiant);
				int intIdentifiant = 0;
				try {
					intIdentifiant = Integer.parseInt(identifiant);
				} catch (NumberFormatException e) {
				}
				if (intIdentifiant != 0) {
					System.out.println("identifiant trouvé : " + intIdentifiant);
					message.setNumero(String.valueOf(intIdentifiant));
				} else {
					System.out.println("intIdentifiant à 0 : on attribue le numéro" + numero);
					message.setNumero(String.valueOf(numero));
					numero++;
				}
			}
		}
	}

	public Date getDebut() {
		return debut;
	}

	public void setDebut(Date debut) {
		// System.out.println("CalculSurMessages : setDebut = "+debut);
		this.debut = debut;
	}

	public Date getFin() {
		return fin;
	}

	public void setFin(Date fin) {
		// System.out.println("CalculSurMessages : setFin = "+fin);
		this.fin = fin;
	}

}
