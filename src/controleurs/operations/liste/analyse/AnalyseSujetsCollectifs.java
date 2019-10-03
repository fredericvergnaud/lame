package controleurs.operations.liste.analyse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import controleurs.vuesabstraites.ProjetView;
import modeles.ConversationModel;
import modeles.LocuteurModel;

public class AnalyseSujetsCollectifs {

	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private float nbreMoyenMessagesConversation, nbreMoyenLocuteursDifferentsSujet;
	private float paramSujetsCollectifs;
	private int nbreMessages, nbreLocuteursDominants;
	private SortedSet<ConversationModel> setConversations;
	private boolean paramLocuteursSC;
	private SortedSet<LocuteurModel> setLocuteurs;
	private String locuteurDominant;

	private int nbreSC, nbreMessagesSC, nbreMessagesLocuteursDominantsSC, nbreMessagesLDSC, nbreMessagesLocuteursDominantsSaufPremierSC, nbrePLSC, nbreLocuteursDominantsSC, nbreMessagesPLSC,
			nbreLanceursSC, nbreLocuteursDominantsLanceursSC, nbreSCLancesLD, nbreSCLancesLocuteursDominantsSaufPremier, nbreSCLancesPL;
	private float pourcentSC, pourcentMessagesSC, pourcentMessagesLocuteursDominantsSC, pourcentMessagesLDSC, pourcentMessagesLocuteursDominantsSaufPremierSC, pourcentPLSC, pourcentMessagesPLSC,
			pourcentLanceursSC, pourcentLocuteursDominantsLanceursSC1, pourcentLocuteursDominantsLanceursSC2, pourcentSCLancesLD, pourcentSCLancesLocuteursDominantsSaufPremier, pourcentSCLancesPL,
			pourcentLocuteursDominantsSC;

	public AnalyseSujetsCollectifs(ProjetView activitesView, ResourceBundle bundleOperationsListe, float nbreMoyenMessagesConversation, float paramSujetsCollectifs,
			float nbreMoyenLocuteursDifferentsSujet, int nbreMessages, SortedSet<ConversationModel> setConversations, boolean paramLocuteursSC, SortedSet<LocuteurModel> setLocuteurs,
			String locuteurDominant, int nbreLocuteursDominants) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.nbreMoyenMessagesConversation = nbreMoyenMessagesConversation;
		this.paramSujetsCollectifs = paramSujetsCollectifs;
		this.nbreMoyenLocuteursDifferentsSujet = nbreMoyenLocuteursDifferentsSujet;
		this.nbreMessages = nbreMessages;
		this.setConversations = setConversations;
		this.paramLocuteursSC = paramLocuteursSC;
		this.setLocuteurs = setLocuteurs;
		this.locuteurDominant = locuteurDominant;
		this.nbreLocuteursDominants = nbreLocuteursDominants;
	}

	public void analyse() {
		activitesView.resetProgress();
		activitesView.setStepProgress(1);
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_CalculSC"));
		// Calcul des parametres :
		// parametre sujet collectif (pas de calcul)
		// parametre Nombre de Messages superieur a (parametre X nombre moyen de
		// messages par conversation) : nbreMoyenMessagesSujetMultiplie
		// parametre Nombre de Locuteurs superieur a (parametre X nombre moyen
		// de locuteurs par conversation) : nbreMoyenLocuteursSujetMultiplie
		int nbreMoyenMessagesSujetMultiplie = (int) (nbreMoyenMessagesConversation * paramSujetsCollectifs + 0.5);
		int nbreMoyenLocuteursConversationMultiplie = (int) (nbreMoyenLocuteursDifferentsSujet * paramSujetsCollectifs + 0.5);
		System.out.println("nbre messages total = " + nbreMessages);
		System.out.println("nbre moyen messages par sujet (" + nbreMoyenMessagesConversation + ") multiplie par le multiplicateur (" + paramSujetsCollectifs + ") +0,5 = "
				+ nbreMoyenMessagesSujetMultiplie);
		System.out.println("nbre moyen locuteurs par conversation (" + nbreMoyenLocuteursDifferentsSujet + ") multiplie par le multiplicateur (" + paramSujetsCollectifs + ") +0,5= "
				+ nbreMoyenLocuteursConversationMultiplie);
		// Creation de :
		// une MAP Nbre Messages X List de Conversations
		// une MAP Nbre Locuteurs X List de Conversations
		// Pour selectionner les Conversations "collectives" selon
		// le parametre oblogatoire "messages" et le parametre supplementaire
		// "locuteurs"
		Map<Integer, List<ConversationModel>> mapNbreMessagesConversations = new TreeMap<Integer, List<ConversationModel>>();
		Map<Integer, List<ConversationModel>> mapNbreLocuteursConversations = new TreeMap<Integer, List<ConversationModel>>();

		for (ConversationModel conversation : setConversations) {
			int nbreMessagesConversation = conversation.getNbreMessages();
			if (!mapNbreMessagesConversations.containsKey(nbreMessagesConversation)) {
				List<ConversationModel> newListConversations = new ArrayList<ConversationModel>();
				newListConversations.add(conversation);
				mapNbreMessagesConversations.put(nbreMessagesConversation, newListConversations);
			} else {
				List<ConversationModel> oldListConversations = mapNbreMessagesConversations.get(nbreMessagesConversation);
				oldListConversations.add(conversation);
				mapNbreMessagesConversations.put(nbreMessagesConversation, oldListConversations);
			}
			int nbreLocuteursConversation = conversation.getNbreLocuteurs();
			if (!mapNbreLocuteursConversations.containsKey(nbreLocuteursConversation)) {
				List<ConversationModel> newListConversations = new ArrayList<ConversationModel>();
				newListConversations.add(conversation);
				mapNbreLocuteursConversations.put(nbreLocuteursConversation, newListConversations);
			} else {
				List<ConversationModel> oldListConversations = mapNbreLocuteursConversations.get(nbreLocuteursConversation);
				oldListConversations.add(conversation);
				mapNbreLocuteursConversations.put(nbreLocuteursConversation, oldListConversations);
			}
		}
		// On ne garde que ce qui est superieur a
		// nbreMoyenMessagesSujetMultiplie
		SortedMap<Integer, List<ConversationModel>> mapNbreMessagesSC = ((TreeMap<Integer, List<ConversationModel>>) mapNbreMessagesConversations).tailMap(nbreMoyenMessagesSujetMultiplie);

		// Creation du Set de sujets collectifs selon le nombre de messages
		List<ConversationModel> listSC = new ArrayList<ConversationModel>();
		for (Entry<Integer, List<ConversationModel>> e : mapNbreMessagesSC.entrySet()) {
			for (ConversationModel conversation : e.getValue()) {
				System.out.println("La conversation " + conversation.getId() + " a " + conversation.getNbreMessages() + " messages : c'est un sujet collectif");
				listSC.add(conversation);
				conversation.setSc(true);
			}
		}

		// Si on prend en compte le paramètre nbre de locuteurs, on retire les
		// conversations n'y répondant pas
		if (paramLocuteursSC == true) {
			System.out.println("Le nombre de locuteurs est pris en compte");
			for (Iterator<ConversationModel> iterator = listSC.iterator(); iterator.hasNext();) {
				ConversationModel sujetCollectifSelonNbreMessages = iterator.next();
				if (sujetCollectifSelonNbreMessages.getNbreLocuteurs() < nbreMoyenLocuteursConversationMultiplie) {
					System.out.println("\tLa conversation " + sujetCollectifSelonNbreMessages.getId() + " a " + sujetCollectifSelonNbreMessages.getNbreLocuteurs()
							+ " locuteurs : on l'enlève car ce n'est pas un sujet collectif");
					sujetCollectifSelonNbreMessages.setSc(false);
					iterator.remove();
				}
			}
		}

		Set<ConversationModel> setSC = new HashSet<ConversationModel>(listSC);
		// Setting des stats sur SC si SetSC n'est pas vide
		if (setSC.size() > 0) {
			// Setting du nbre + % de SC
			int nbreSC = setSC.size();
			setNbreSC(nbreSC);
			int nbreConversations = setConversations.size();
			float pourcentSC = (float) nbreSC / (float) nbreConversations * 100;
			setPourcentSC(pourcentSC);

			// Locuteurs Dominants & Sujets Collectifs
			// Mise a jour du tableau des locuteurs :
			// - nbre messages + % dans SC
			// - nbre SC
			// - lanceur de SC
			// - locuteur dominant LD
			// Creation de :
			// - un Set de petits locuteurs dans SC
			// - un Set de locuteurs dominants dans SC
			int i = 0;
			int j = 0;
			int k = 0;
			Set<String> setPLSC = new HashSet<String>();
			Set<String> setLDSC = new HashSet<String>();
			int nbreLocuteurs = setLocuteurs.size();
			for (ConversationModel sc : setSC) {
				i += sc.getNbreMessages();
				// System.out
				// .println("sc n°" + sc.getId() + " : "
				// + sc.getMapLocuteurNbreMessages().size()
				// + " locuteurs");
				// LD et MESSAGES DANS SC
				Map<String, Integer> mapLocuteursNbreMessagesConversation = sc.getMapLocuteurNbreMessages();
				String nomLanceurSC = sc.getLanceur();
				for (Entry<String, Integer> e : mapLocuteursNbreMessagesConversation.entrySet()) {
					String nomLSC = e.getKey();
					int nbreMessagesLSC = e.getValue();
					for (LocuteurModel locuteur : setLocuteurs) {
						String nomL = locuteur.getNom();
						// NOMBRE SC + NOMBRE MESSAGES DANS SC
						if (nomL.equals(nomLSC)) {
							locuteur.setNbreMessagesSC(locuteur.getNbreMessagesSC() + nbreMessagesLSC);
							locuteur.setPourcentMessagesSC((float) locuteur.getNbreMessagesSC() / (float) nbreMessages * 100);
							locuteur.setNbreSujetsCollectifs(locuteur.getNbreSujetsCollectifs() + 1);
							// Locuteurs Dominants
							if (locuteur.isLd()) {
								// System.out.println("LD " + nomLSC + " : "
								// + e.getValue()
								// + " messages dans cette conversation");
								j += nbreMessagesLSC;
								setLDSC.add(nomLSC);
							} else
								setPLSC.add(nomLSC);
							// LD
							if (nomLSC.equals(locuteurDominant)) {
								// System.out.println("Locuteur Dominant " + ld
								// + " : " + e.getValue()
								// + " messages dans cette conversation");
								k += nbreMessagesLSC;
							}
							// LANCEURS
							if (nomLSC.equals(nomLanceurSC)) {
								locuteur.setLanceurSc(true);
								locuteur.setNbreSujetsCollectifsLances(locuteur.getNbreSujetsCollectifsLances() + 1);
							}
						}
					}
				}
			}
			System.out.println("nbre de locuteurs dominants dans les SC = " + setLDSC.size());
			// Setting nbre + % de petits locuteurs dans SC
			int nbreLocuteursDominantsSC = setLDSC.size();
			setNbreLocuteursDominantsSC(nbreLocuteursDominantsSC);
			float pourcentLocuteursDominantsSC = 0;
			pourcentLocuteursDominantsSC = (float) nbreLocuteursDominantsSC / (float) nbreLocuteursDominants * 100;
			setPourcentLocuteursDominantsSC(pourcentLocuteursDominantsSC);
			System.out.println("nbre Locuteurs Dominants dans SC = " + nbreLocuteursDominantsSC + " (" + pourcentLocuteursDominantsSC + "% des locuteurs dominants)");
			// Setting du nbre + % de messages dans SC
			int nbreMessagesSC = i;
			setNbreMessagesSC(nbreMessagesSC);
			float pourcentMessagesSC = (float) nbreMessagesSC / (float) nbreMessages * 100;
			setPourcentMessagesSC(pourcentMessagesSC);

			// Setting du nbre + % de messages des locuteurs dominants dans SC
			int nbreMessagesLocuteursDominantsSC = j;
			setNbreMessagesLocuteursDominantsSC(nbreMessagesLocuteursDominantsSC);
			float pourcentMessagesLocuteursDominantsSC = (float) nbreMessagesLocuteursDominantsSC / (float) nbreMessagesSC * 100;
			setPourcentMessagesLocuteursDominantsSC(pourcentMessagesLocuteursDominantsSC);

			// Setting du nbre + % de messages du LD dans SC
			int nbreMessagesLDSC = k;
			setNbreMessagesLDSC(nbreMessagesLDSC);
			float pourcentMessagesLDSC = (float) nbreMessagesLDSC / (float) nbreMessagesSC * 100;
			setPourcentMessagesLDSC(pourcentMessagesLDSC);

			// Setting du nbre + % de messages des Locuteurs Dominants sauf
			// Premier dans SC
			int nbreMessagesLocuteursDominantsSaufPremierSC = nbreMessagesLocuteursDominantsSC - nbreMessagesLDSC;
			setNbreMessagesLocuteursDominantsSaufPremierSC(nbreMessagesLocuteursDominantsSaufPremierSC);
			float pourcentMessagesLocuteursDominantsSaufPremierSC = (float) nbreMessagesLocuteursDominantsSaufPremierSC / (float) nbreMessagesSC * 100;
			setPourcentMessagesLocuteursDominantsSaufPremierSC(pourcentMessagesLocuteursDominantsSaufPremierSC);

			System.out.println("nbre messages SC = " + nbreMessagesSC + " (" + pourcentMessagesSC + "%)");
			System.out.println("nbre messages locuteurs dominants dans SC = " + nbreMessagesLocuteursDominantsSC + " (" + pourcentMessagesLocuteursDominantsSC + "%)");
			System.out.println("nbre messages LD = " + k + " (" + pourcentMessagesLDSC + "%)");
			System.out.println("nbre messages locuteurs dominants sauf premier dans SC = " + nbreMessagesLocuteursDominantsSaufPremierSC + " (" + pourcentMessagesLocuteursDominantsSaufPremierSC
					+ "%)");

			// Petits Locuteurs

			// Setting nbre + % de petits locuteurs dans SC
			int nbrePLSC = setPLSC.size();
			setNbrePLSC(nbrePLSC);
			float pourcentPLSC = 0;
			if (nbreLocuteursDominants != nbreLocuteurs)
				pourcentPLSC = (float) nbrePLSC / (float) (nbreLocuteurs - nbreLocuteursDominants) * 100;
			setPourcentPLSC(pourcentPLSC);
			System.out.println("nbre PL dans SC = " + nbrePLSC + " (" + pourcentPLSC + "% des petits locuteurs)");

			// Setting nbre + % de messages des petits locuteurs dans SC
			int nbreMessagesPLSC = nbreMessagesSC - nbreMessagesLocuteursDominantsSC;
			setNbreMessagesPLSC(nbreMessagesPLSC);
			float pourcentMessagesPLSC = (float) nbreMessagesPLSC / (float) nbreMessagesSC * 100;
			setPourcentMessagesPLSC(pourcentMessagesPLSC);
			System.out.println("nbre messages PL dans SC = " + nbreMessagesPLSC + " (" + pourcentMessagesPLSC + "%)");

			// LANCEURS
			int nbreLanceursSC = 0;
			int nbreLocuteursDominantsLanceursSC = 0;
			int nbreSCLancesLD = 0;
			int nbreSCLancesLocuteursDominantsSaufPremier = 0;
			for (LocuteurModel locuteur : setLocuteurs) {
				if (locuteur.isLanceurSc()) {
					System.out.println("lanceur = " + locuteur.getNom() + " | " + locuteur.getNbreSujetsCollectifsLances() + " sc lancés | " + locuteur.getNbreMessagesSC() + " messages dans SC");
					nbreLanceursSC++;
					// Locuteurs dominants et LD
					if (locuteur.isLd()) {
						nbreLocuteursDominantsLanceursSC++;
						if (locuteur.getNom().equals(locuteurDominant)) {
							nbreSCLancesLD = locuteur.getNbreSujetsCollectifsLances();
						} else
							nbreSCLancesLocuteursDominantsSaufPremier += locuteur.getNbreSujetsCollectifsLances();
					}
				}
			}
			System.out.println("nbre de lanceurs : " + nbreLanceursSC + " (" + (float) nbreLanceursSC / (float) nbreLocuteurs * 100 + " des locuteurs)");
			System.out.println("nbre de locuteurs dominants lanceurs de SC = " + nbreLocuteursDominantsLanceursSC + " (" + (float) nbreLocuteursDominantsLanceursSC / (float) nbreLanceursSC * 100
					+ "% des lanceurs de SC | " + (float) nbreLocuteursDominantsLanceursSC / (float) nbreLocuteursDominants * 100 + "% des locuteurs dominants)");
			System.out.println("nbre de SC lances par LD = " + nbreSCLancesLD + " (" + (float) nbreSCLancesLD / (float) nbreSC * 100 + "% des SC)");
			System.out.println("nbre de SC lances par locuteurs dominants sauf LD = " + nbreSCLancesLocuteursDominantsSaufPremier + " (" + (float) nbreSCLancesLocuteursDominantsSaufPremier
					/ (float) nbreSC * 100 + "% des SC)");
			System.out.println("nbreSC = " + nbreSC);
			System.out.println("nbreSCLancesLD = " + nbreSCLancesLD);
			System.out.println("nbreSCLancesLocuteursDominantsSaufPremier = " + nbreSCLancesLocuteursDominantsSaufPremier);
			int nbreSCLancesPL = nbreSC - nbreSCLancesLD - nbreSCLancesLocuteursDominantsSaufPremier;
			System.out.println("nbre de SC lances par les PL = " + nbreSCLancesPL + " (" + (float) nbreSCLancesPL / (float) nbreSC * 100 + "% des SC)");

			// Setting nbre + % de lanceurs de SC
			setNbreLanceursSC(nbreLanceursSC);
			float pourcentLanceursSC = (float) nbreLanceursSC / (float) nbreLocuteurs * 100;
			setPourcentLanceursSC(pourcentLanceursSC);

			// Setting nbre + % de locuteurs dominants lanceurs de SC
			setNbreLocuteursDominantsLanceursSC(nbreLocuteursDominantsLanceursSC);

			// Setting % de locuteurs dominants lanceurs de SC en pourcentage
			// des lanceurs
			float pourcentLocuteursDominantsLanceursSC1 = (float) nbreLocuteursDominantsLanceursSC / (float) nbreLanceursSC * 100;
			setPourcentLocuteursDominantsLanceursSC1(pourcentLocuteursDominantsLanceursSC1);

			// Setting % de locuteurs dominants lanceurs de SC en pourcentage
			// des locuteurs dominants
			float pourcentLocuteursDominantsLanceursSC2 = (float) nbreLocuteursDominantsLanceursSC / (float) nbreLocuteursDominants * 100;
			setPourcentLocuteursDominantsLanceursSC2(pourcentLocuteursDominantsLanceursSC2);

			// Setting nbre + % de SC lances par le locuteur dominant
			setNbreSCLancesLD(nbreSCLancesLD);
			float pourcentSCLancesLD = (float) nbreSCLancesLD / (float) nbreSC * 100;
			setPourcentSCLancesLD(pourcentSCLancesLD);

			// Setting nbre + % de SC lances par les locuteurs dominants sauf le
			// Premier
			setNbreSCLancesLocuteursDominantsSaufPremier(nbreSCLancesLocuteursDominantsSaufPremier);
			float pourcentSCLancesLocuteursDominantsSaufPremier = (float) nbreSCLancesLocuteursDominantsSaufPremier / (float) nbreSC * 100;
			setPourcentSCLancesLocuteursDominantsSaufPremier(pourcentSCLancesLocuteursDominantsSaufPremier);

			// Setting nbre + % de SC lances par les petits locuteurs
			setNbreSCLancesPL(nbreSCLancesPL);
			float pourcentSCLancesPL = (float) nbreSCLancesPL / (float) nbreSC * 100;
			setPourcentSCLancesPL(pourcentSCLancesPL);
		} else
			JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_AucunSCTrouve"), bundleOperationsListe.getString("txt_CalculSC"), JOptionPane.ERROR_MESSAGE);
		activitesView.updateProgress();
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_CalculSC") + " " + bundleOperationsListe.getString("txt_Effectue") + "\n");
	}

	public int getNbreSC() {
		return nbreSC;
	}

	public void setNbreSC(int nbreSC) {
		this.nbreSC = nbreSC;
	}

	public int getNbreMessagesSC() {
		return nbreMessagesSC;
	}

	public void setNbreMessagesSC(int nbreMessagesSC) {
		this.nbreMessagesSC = nbreMessagesSC;
	}

	public int getNbreMessagesLocuteursDominantsSC() {
		return nbreMessagesLocuteursDominantsSC;
	}

	public void setNbreMessagesLocuteursDominantsSC(int nbreMessagesLocuteursDominantsSC) {
		this.nbreMessagesLocuteursDominantsSC = nbreMessagesLocuteursDominantsSC;
	}

	public int getNbreMessagesLDSC() {
		return nbreMessagesLDSC;
	}

	public void setNbreMessagesLDSC(int nbreMessagesLDSC) {
		this.nbreMessagesLDSC = nbreMessagesLDSC;
	}

	public int getNbreMessagesLocuteursDominantsSaufPremierSC() {
		return nbreMessagesLocuteursDominantsSaufPremierSC;
	}

	public void setNbreMessagesLocuteursDominantsSaufPremierSC(int nbreMessagesLocuteursDominantsSaufPremierSC) {
		this.nbreMessagesLocuteursDominantsSaufPremierSC = nbreMessagesLocuteursDominantsSaufPremierSC;
	}

	public int getNbrePLSC() {
		return nbrePLSC;
	}

	public void setNbrePLSC(int nbrePLSC) {
		this.nbrePLSC = nbrePLSC;
	}

	public int getNbreMessagesPLSC() {
		return nbreMessagesPLSC;
	}

	public void setNbreMessagesPLSC(int nbreMessagesPLSC) {
		this.nbreMessagesPLSC = nbreMessagesPLSC;
	}

	public int getNbreLanceursSC() {
		return nbreLanceursSC;
	}

	public void setNbreLanceursSC(int nbreLanceursSC) {
		this.nbreLanceursSC = nbreLanceursSC;
	}

	public int getNbreLocuteursDominantsLanceursSC() {
		return nbreLocuteursDominantsLanceursSC;
	}

	public void setNbreLocuteursDominantsLanceursSC(int nbreLocuteursDominantsLanceursSC) {
		this.nbreLocuteursDominantsLanceursSC = nbreLocuteursDominantsLanceursSC;
	}

	public int getNbreSCLancesLD() {
		return nbreSCLancesLD;
	}

	public void setNbreSCLancesLD(int nbreSCLancesLD) {
		this.nbreSCLancesLD = nbreSCLancesLD;
	}

	public int getNbreSCLancesLocuteursDominantsSaufPremier() {
		return nbreSCLancesLocuteursDominantsSaufPremier;
	}

	public void setNbreSCLancesLocuteursDominantsSaufPremier(int nbreSCLancesLocuteursDominantsSaufPremier) {
		this.nbreSCLancesLocuteursDominantsSaufPremier = nbreSCLancesLocuteursDominantsSaufPremier;
	}

	public int getNbreSCLancesPL() {
		return nbreSCLancesPL;
	}

	public void setNbreSCLancesPL(int nbreSCLancesPL) {
		this.nbreSCLancesPL = nbreSCLancesPL;
	}

	public float getPourcentSC() {
		return pourcentSC;
	}

	public void setPourcentSC(float pourcentSC) {
		this.pourcentSC = pourcentSC;
	}

	public float getPourcentMessagesSC() {
		return pourcentMessagesSC;
	}

	public void setPourcentMessagesSC(float pourcentMessagesSC) {
		this.pourcentMessagesSC = pourcentMessagesSC;
	}

	public float getPourcentMessagesLocuteursDominantsSC() {
		return pourcentMessagesLocuteursDominantsSC;
	}

	public void setPourcentMessagesLocuteursDominantsSC(float pourcentMessagesLocuteursDominantsSC) {
		this.pourcentMessagesLocuteursDominantsSC = pourcentMessagesLocuteursDominantsSC;
	}

	public float getPourcentMessagesLDSC() {
		return pourcentMessagesLDSC;
	}

	public void setPourcentMessagesLDSC(float pourcentMessagesLDSC) {
		this.pourcentMessagesLDSC = pourcentMessagesLDSC;
	}

	public float getPourcentMessagesLocuteursDominantsSaufPremierSC() {
		return pourcentMessagesLocuteursDominantsSaufPremierSC;
	}

	public void setPourcentMessagesLocuteursDominantsSaufPremierSC(float pourcentMessagesLocuteursDominantsSaufPremierSC) {
		this.pourcentMessagesLocuteursDominantsSaufPremierSC = pourcentMessagesLocuteursDominantsSaufPremierSC;
	}

	public float getPourcentPLSC() {
		return pourcentPLSC;
	}

	public void setPourcentPLSC(float pourcentPLSC) {
		this.pourcentPLSC = pourcentPLSC;
	}

	public float getPourcentMessagesPLSC() {
		return pourcentMessagesPLSC;
	}

	public void setPourcentMessagesPLSC(float pourcentMessagesPLSC) {
		this.pourcentMessagesPLSC = pourcentMessagesPLSC;
	}

	public float getPourcentLanceursSC() {
		return pourcentLanceursSC;
	}

	public void setPourcentLanceursSC(float pourcentLanceursSC) {
		this.pourcentLanceursSC = pourcentLanceursSC;
	}

	public float getPourcentLocuteursDominantsLanceursSC1() {
		return pourcentLocuteursDominantsLanceursSC1;
	}

	public void setPourcentLocuteursDominantsLanceursSC1(float pourcentLocuteursDominantsLanceursSC1) {
		this.pourcentLocuteursDominantsLanceursSC1 = pourcentLocuteursDominantsLanceursSC1;
	}

	public float getPourcentLocuteursDominantsLanceursSC2() {
		return pourcentLocuteursDominantsLanceursSC2;
	}

	public void setPourcentLocuteursDominantsLanceursSC2(float pourcentLocuteursDominantsLanceursSC2) {
		this.pourcentLocuteursDominantsLanceursSC2 = pourcentLocuteursDominantsLanceursSC2;
	}

	public float getPourcentSCLancesLD() {
		return pourcentSCLancesLD;
	}

	public void setPourcentSCLancesLD(float pourcentSCLancesLD) {
		this.pourcentSCLancesLD = pourcentSCLancesLD;
	}

	public float getPourcentSCLancesLocuteursDominantsSaufPremier() {
		return pourcentSCLancesLocuteursDominantsSaufPremier;
	}

	public void setPourcentSCLancesLocuteursDominantsSaufPremier(float pourcentSCLancesLocuteursDominantsSaufPremier) {
		this.pourcentSCLancesLocuteursDominantsSaufPremier = pourcentSCLancesLocuteursDominantsSaufPremier;
	}

	public float getPourcentSCLancesPL() {
		return pourcentSCLancesPL;
	}

	public void setPourcentSCLancesPL(float pourcentSCLancesPL) {
		this.pourcentSCLancesPL = pourcentSCLancesPL;
	}

	public int getNbreLocuteursDominantsSC() {
		return nbreLocuteursDominantsSC;
	}

	public void setNbreLocuteursDominantsSC(int nbreLocuteursDominantsSC) {
		this.nbreLocuteursDominantsSC = nbreLocuteursDominantsSC;
	}

	public float getPourcentLocuteursDominantsSC() {
		return pourcentLocuteursDominantsSC;
	}

	public void setPourcentLocuteursDominantsSC(float pourcentLocuteursDominantsSC) {
		this.pourcentLocuteursDominantsSC = pourcentLocuteursDominantsSC;
	}

}
