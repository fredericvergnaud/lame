package modeles;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.event.EventListenerList;
import javax.swing.tree.DefaultMutableTreeNode;

import modeles.ecouteurs.ListeListener;
import modeles.evenements.ListeChangedEvent;
import comparators.ConversationIdComparator;
import comparators.LocuteurIdComparator;

public class ListeModel extends DefaultMutableTreeNode implements Serializable {

	private static final long serialVersionUID = 1L;
	private int numero, nbreMessages, paramLevenshtein, paramJours, paramMessages, nbreLocuteurs, nbreLocuteursUnSeulMessage, nbreConversations, nbreLocuteursDominants, nbrePL,
			nbreMessagesLocuteursDominants, nbreMessagesLD, nbreMessagesLocuteursDominantsSaufPremier, nbreLocuteursDominants3PremiersMois, nbreSC, nbreMessagesSC, nbreMessagesLDSC,
			nbreMessagesLocuteursDominantsSC, nbreMessagesLocuteursDominantsSaufPremierSC, nbrePLSC, nbreLocuteursDominantsSC, nbreMessagesPLSC, nbreLanceursSC, nbreLocuteursDominantsLanceursSC,
			nbreSCLancesLD, nbreSCLancesLocuteursDominantsSaufPremier, nbreSCLancesPL, typeParamRegroupementMessages;
	private float nbreMoyenMessagesLocuteurMois, nbreMoyenLocuteursDifferentsSujet, nbreMoyenMessagesConversation, paramLocuteursDominants, moyenneIntensite, pourcentLocuteursDominantsSaufPremier,
			pourcentMessagesLD, pourcentLocuteursDominants, pourcentPL, pourcentMessagesLocuteursDominants, pourcentMessagesLocuteursDominantsSaufPremier, pourcentLocuteursDominants3PremiersMois,
			dureeMoyenneParticipationLocuteursDominants, ecartTypeParticipationLocuteursDominants, pourcentParticipationLocuteursDominants, paramSujetsCollectifs, pourcentSC, pourcentMessagesSC,
			pourcentMessagesLDSC, pourcentMessagesLocuteursDominantsSC, pourcentMessagesLocuteursDominantsSaufPremierSC, pourcentPLSC, pourcentLocuteursDominantsSC, pourcentMessagesPLSC,
			pourcentLanceursSC, pourcentLocuteursDominantsLanceursSC1, pourcentLocuteursDominantsLanceursSC2, pourcentSCLancesLD, pourcentSCLancesLocuteursDominantsSaufPremier, pourcentSCLancesPL;
	private String nom, sParamRegroupementMessages, dateAjout, dureeSuivi, locuteurDominant, identifiantToShow;
	private SortedSet<ConversationModel> setConversations;
	private SortedSet<LocuteurModel> setLocuteurs;
	private Date debut, fin;
	private boolean paramLocuteursSC = true, paramNoRegroupementMessages = false;
	private Map<String, MessageModel> mapIdMessage;

	private EventListenerList listeners;

	public ListeModel() {
	}

	public ListeModel(int numero) {
		listeners = new EventListenerList();
		init();
		// System.out.println("ListeModel - constructeur - apres init(): numero = "+getNumero());
		this.numero = numero;
	}

	@Override
	public String toString() {
		return "(" + numero + ") " + getNom();
	}

	// // // // // // // // LISTENERS + CONTROLLERS // // // // // // // // //

	public EventListenerList getListeners() {
		return listeners;
	}

	public void addListeListener(ListeListener listeListener) {
		listeners.add(ListeListener.class, listeListener);
	}

	public void removeListeListener(ListeListener listeListener) {
		listeners.remove(ListeListener.class, listeListener);
	}

	public void fireListeChanged() {
		System.out.println("ListeModel - fireListeChanged : " + listeners.getListenerCount() + " listeners");
		System.out.println("ListeModel - fireListeChanged : liste selected = " + getNom());
		ListeListener[] listenerList = listeners.getListeners(ListeListener.class);

		for (ListeListener listener : listenerList) {
			listener.listeChanged(new ListeChangedEvent(this, getNom(), getNumero(), getDateAjout(), getMapIdMessage(), getNbreMessages(), getDebut(), getFin(), getDureeSuivi(),
					getIdentifiantToShow(), getSetConversations(), getSetLocuteurs(), getNbreSC(), getNbreMessagesSC(), getNbreLanceursSC(), getNbrePLSC(), getNbreMessagesPLSC(), getNbreSCLancesPL(),
					getNbreLocuteursDominants(), getNbreMessagesLDSC(), getNbreSCLancesLD(), getNbreMessagesLocuteursDominantsSC(), getNbreMessagesLocuteursDominantsSaufPremierSC(),
					getNbreSCLancesLocuteursDominantsSaufPremier(), getNbreLocuteursDominantsLanceursSC(), getNbreMoyenLocuteursDifferentsSujet(), getNbreMoyenMessagesConversation(),
					getParamSujetsCollectifs(), getPourcentSC(), getPourcentMessagesSC(), getPourcentLanceursSC(), getPourcentPLSC(), getPourcentMessagesPLSC(), getPourcentSCLancesPL(),
					getPourcentMessagesLDSC(), getPourcentSCLancesLD(),
					getPourcentMessagesLocuteursDominantsSC(),
					getPourcentMessagesLocuteursDominantsSaufPremierSC(),
					getPourcentSCLancesLocuteursDominantsSaufPremier(),
					getPourcentLocuteursDominantsLanceursSC1(),
					getPourcentLocuteursDominantsLanceursSC2(),
					getParamLocuteursSC(),
					getsParamRegroupementMessages(),
					getNbreLocuteurs(),
					getNbreLocuteursUnSeulMessage(),
					// getNbreLocuteursDominants(),
					getNbrePL(),
					getNbreMessagesLD(),
					getNbreMessagesLocuteursDominants(),
					getNbreMessagesLocuteursDominantsSaufPremier(),
					// getNbreSCLancesLD(),
					// getNbreMessagesLocuteursDominantsSC(),
					getNbreLocuteursDominants3PremiersMois(), getNbreMoyenMessagesLocuteurMois(), getParamLocuteursDominants(), getMoyenneIntensite(), getPourcentLocuteursDominants(),
					getPourcentPL(), getPourcentMessagesLD(), getPourcentMessagesLocuteursDominants(), getPourcentLocuteursDominantsSaufPremier(), getPourcentMessagesLocuteursDominantsSaufPremier(),
					getPourcentLocuteursDominants3PremiersMois(), getDureeMoyenneParticipationLocuteursDominants(), getEcartTypeParticipationLocuteursDominants(),
					getPourcentParticipationLocuteursDominants(), getLocuteurDominant(), getParamJours(), getParamMessages(), getParamLevenshtein(), getParamNoRegroupementMessages(),
					getNbreLocuteursDominantsSC(), getPourcentLocuteursDominantsSC()));
		}

	}

	// public void setSelected() {
	// System.out.println("ListeModel - setSelected : OK");
	// fireListeChanged();
	// }

	// // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // CREATION LISTE // // // // // // // // // // //

	public void creerListe(String nom
	// , String type
	) {
		setNom(nom);
		// setType(type);
		setDateAjout(new Date());
		// System.out.println("ListeModel - creerListe : newListe = "+getNom()+" | numéro = "+getNumero()+" | type = "+getType()+" | date = "+getDateAjout());
	}

	public void exportListe(ListeModel listeToExport) {
		listeners = new EventListenerList();
		init();
		numero = 1;
		nom = listeToExport.getNom();
		setDateAjout(new Date());
		nbreMessages = listeToExport.getNbreMessages();

		// PARAMETRES
		paramNoRegroupementMessages = listeToExport.getParamNoRegroupementMessages();
		sParamRegroupementMessages = listeToExport.getsParamRegroupementMessages();
		typeParamRegroupementMessages = listeToExport.getTypeParamRegroupementMessages();
		paramJours = listeToExport.getParamJours();
		paramMessages = listeToExport.getParamMessages();
		paramLevenshtein = listeToExport.getParamLevenshtein();
		paramLocuteursDominants = listeToExport.getParamLocuteursDominants();
		paramSujetsCollectifs = listeToExport.getParamSujetsCollectifs();
		paramLocuteursSC = listeToExport.getParamLocuteursSC();

		mapIdMessage = listeToExport.getMapIdMessage();

		nbreLocuteurs = listeToExport.getNbreLocuteurs();
		nbreLocuteursUnSeulMessage = listeToExport.getNbreLocuteursUnSeulMessage();
		nbreConversations = listeToExport.getNbreConversations();
		nbreLocuteursDominants = listeToExport.getNbreLocuteursDominants();
		nbrePL = listeToExport.getNbrePL();
		nbreMessagesLocuteursDominants = listeToExport.getNbreMessagesLocuteursDominants();
		nbreMessagesLD = listeToExport.getNbreMessagesLD();
		nbreMessagesLocuteursDominantsSaufPremier = listeToExport.getNbreMessagesLocuteursDominantsSaufPremier();
		nbreLocuteursDominants3PremiersMois = listeToExport.getNbreLocuteursDominants3PremiersMois();
		nbreSC = listeToExport.getNbreSC();
		nbreMessagesSC = listeToExport.getNbreMessagesSC();
		nbreMessagesLDSC = listeToExport.getNbreMessagesLDSC();
		nbreMessagesLocuteursDominantsSC = listeToExport.getNbreMessagesLocuteursDominantsSC();
		nbreMessagesLocuteursDominantsSaufPremierSC = listeToExport.getNbreMessagesLocuteursDominantsSaufPremierSC();
		nbrePLSC = listeToExport.getNbrePLSC();
		nbrePLSC = listeToExport.getNbreLocuteursDominantsSC();
		nbreMessagesPLSC = listeToExport.getNbreMessagesPLSC();
		nbreLanceursSC = listeToExport.getNbreLanceursSC();
		nbreLocuteursDominantsLanceursSC = listeToExport.getNbreLocuteursDominantsLanceursSC();
		nbreSCLancesLD = listeToExport.getNbreSCLancesLD();
		nbreSCLancesLocuteursDominantsSaufPremier = listeToExport.getNbreSCLancesLocuteursDominantsSaufPremier();
		nbreSCLancesPL = listeToExport.getNbreSCLancesPL();
		nbreMoyenMessagesLocuteurMois = listeToExport.getNbreMoyenMessagesLocuteurMois();
		nbreMoyenLocuteursDifferentsSujet = listeToExport.getNbreMoyenLocuteursDifferentsSujet();
		nbreMoyenMessagesConversation = listeToExport.getNbreMoyenMessagesConversation();
		moyenneIntensite = listeToExport.getMoyenneIntensite();
		pourcentLocuteursDominantsSaufPremier = listeToExport.getPourcentLocuteursDominantsSaufPremier();
		pourcentMessagesLD = listeToExport.getPourcentMessagesLD();
		pourcentLocuteursDominants = listeToExport.getPourcentLocuteursDominants();
		pourcentPL = listeToExport.getPourcentPL();
		pourcentMessagesLocuteursDominants = listeToExport.getPourcentMessagesLocuteursDominants();
		pourcentMessagesLocuteursDominantsSaufPremier = listeToExport.getPourcentMessagesLocuteursDominantsSaufPremier();
		pourcentLocuteursDominants3PremiersMois = listeToExport.getPourcentLocuteursDominants3PremiersMois();
		dureeMoyenneParticipationLocuteursDominants = listeToExport.getDureeMoyenneParticipationLocuteursDominants();
		ecartTypeParticipationLocuteursDominants = listeToExport.getEcartTypeParticipationLocuteursDominants();
		pourcentParticipationLocuteursDominants = listeToExport.getPourcentParticipationLocuteursDominants();
		pourcentSC = listeToExport.getPourcentSC();
		pourcentMessagesSC = listeToExport.getPourcentMessagesSC();
		pourcentMessagesLDSC = listeToExport.getPourcentMessagesLDSC();
		pourcentMessagesLocuteursDominantsSC = listeToExport.getPourcentMessagesLocuteursDominantsSC();
		pourcentMessagesLocuteursDominantsSaufPremierSC = listeToExport.getPourcentMessagesLocuteursDominantsSaufPremierSC();
		pourcentPLSC = listeToExport.getPourcentPLSC();
		pourcentLocuteursDominantsSC = listeToExport.getPourcentLocuteursDominantsSC();
		pourcentMessagesPLSC = listeToExport.getPourcentMessagesPLSC();
		pourcentLanceursSC = listeToExport.getPourcentLanceursSC();
		pourcentLocuteursDominantsLanceursSC1 = listeToExport.getPourcentLocuteursDominantsLanceursSC1();
		pourcentLocuteursDominantsLanceursSC2 = listeToExport.getPourcentLocuteursDominantsLanceursSC2();
		pourcentSCLancesLD = listeToExport.getPourcentSCLancesLD();
		pourcentSCLancesLocuteursDominantsSaufPremier = listeToExport.getPourcentSCLancesLocuteursDominantsSaufPremier();
		pourcentSCLancesPL = listeToExport.getPourcentSCLancesPL();
		dureeSuivi = listeToExport.getDureeSuivi();
		locuteurDominant = listeToExport.getLocuteurDominant();
		setConversations = listeToExport.getSetConversations();
		setLocuteurs = listeToExport.getSetLocuteurs();
	}

	// // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // AJOUT MESSAGES // // // // // // // // // // //

	public void addMapIdMessages(Map<String, MessageModel> newMapIdMessages) {
		// System.out.println("ListeModel - addMapIdMessages : listeSelected = "+getNom());
		int oldNbreMessages = nbreMessages;
		mapIdMessage.putAll(newMapIdMessages);
		setNewCumul();
		if (getNbreMessages() != oldNbreMessages)
			initAfterNewMapIdMessages();
		fireListeChanged();
		// System.out.println("ListeModel - addMapIdMessages - Après fireListeChanged : listeSelected = "+getNom());
	}

	public void deleteMessages(List<String> listIdMessagesToDelete) {
		// System.out.println("ListeModel - addMapIdMessages : listeSelected = "+getNom());
		int oldNbreMessages = nbreMessages;
		for (String idMessageToDelete : listIdMessagesToDelete)
			mapIdMessage.remove(idMessageToDelete);
		setNewCumul();
		if (getNbreMessages() != oldNbreMessages)
			initAfterNewMapIdMessages();
		fireListeChanged();
		// System.out.println("ListeModel - addMapIdMessages - Après fireListeChanged : listeSelected = "+getNom());
	}

	// // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // GETTER + SETTER // // // // // // // // // // //

	// // // // // // // // GETTER + SETTER SUR INFOS LISTE // // // // //

	public void setNewCumul() {
		this.nbreMessages = mapIdMessage.size();
	}

	public void setNom(final String nom) {
		this.nom = nom;
		fireListeChanged();
	}

	public String getNom() {
		return nom;
	}

	public String getDateAjout() {
		return dateAjout.toString();
	}

	public void setDateAjout(Date dateAjout) {
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy H:mm");
		this.dateAjout = format.format(dateAjout);
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getNbreMessages() {
		return nbreMessages;
	}

	public String getIdentifiantToShow() {
		return identifiantToShow;
	}

	// // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // GETTER + SETTER SUR MAP ID MESSAGES // // // //

	public Map<String, MessageModel> getMapIdMessage() {
		return mapIdMessage;
	}

	public String getDureeSuivi() {
		return dureeSuivi;
	}

	public Date getDebut() {
		return debut;
	}

	public Date getFin() {
		return fin;
	}

	public void setCalculSurMessages(Date debut, Date fin, String dureeSuivi) {
		this.debut = debut;
		this.fin = fin;
		this.dureeSuivi = dureeSuivi;
		fireListeChanged();
	}

	// // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // GETTER + SETTER SUR LOCUTEURS // // // // // //

	public SortedSet<LocuteurModel> getSetLocuteurs() {
		return setLocuteurs;
	}

	public void setSetLocuteurs(SortedSet<LocuteurModel> setLocuteurs) {
		this.setLocuteurs = setLocuteurs;
		fireListeChanged();
	}

	public int getNbreLocuteurs() {
		return nbreLocuteurs;
	}

	public int getNbreLocuteursUnSeulMessage() {
		return nbreLocuteursUnSeulMessage;
	}

	public float getNbreMoyenMessagesLocuteurMois() {
		return nbreMoyenMessagesLocuteurMois;
	}

	public void setCalculSurLocuteurs(int nbreLocuteurs, int nbreLocuteursUnSeulMessage, float nbreMoyenMessagesLocuteurMois) {
		this.nbreLocuteurs = nbreLocuteurs;
		this.nbreLocuteursUnSeulMessage = nbreLocuteursUnSeulMessage;
		this.nbreMoyenMessagesLocuteurMois = nbreMoyenMessagesLocuteurMois;
		fireListeChanged();
	}

	// LOCUTEURS DOMINANTS

	public void setCalculLocuteursDominants(float moyenneIntensite, float pourcentLocuteursDominants3PremiersMois, float dureeMoyenneParticipationLocuteursDominants,
			float ecartTypeParticipationLocuteursDominants, float pourcentParticipationLocuteursDominants, float pourcentPL, float pourcentMessagesLD, float pourcentLocuteursDominants,
			float pourcentMessagesLocuteursDominants, float pourcentLocuteursDominantsSaufPremier, float pourcentMessagesLocuteursDominantsSaufPremier, int nbreLocuteursDominants3PremiersMois,
			int nbrePL, int nbreMessagesLocuteursDominants, int nbreMessagesLD, int nbreLocuteursDominants, int nbreMessagesLocuteursDominantsSaufPremier, String locuteurDominant) {

		this.moyenneIntensite = moyenneIntensite;
		this.pourcentLocuteursDominants3PremiersMois = pourcentLocuteursDominants3PremiersMois;
		this.dureeMoyenneParticipationLocuteursDominants = dureeMoyenneParticipationLocuteursDominants;
		this.ecartTypeParticipationLocuteursDominants = ecartTypeParticipationLocuteursDominants;
		this.pourcentParticipationLocuteursDominants = pourcentParticipationLocuteursDominants;
		this.pourcentPL = pourcentPL;
		this.pourcentMessagesLD = pourcentMessagesLD;
		this.pourcentLocuteursDominants = pourcentLocuteursDominants;
		this.pourcentMessagesLocuteursDominants = pourcentMessagesLocuteursDominants;
		this.pourcentLocuteursDominantsSaufPremier = pourcentLocuteursDominantsSaufPremier;
		this.pourcentMessagesLocuteursDominantsSaufPremier = pourcentMessagesLocuteursDominantsSaufPremier;
		this.nbreLocuteursDominants3PremiersMois = nbreLocuteursDominants3PremiersMois;
		this.nbrePL = nbrePL;
		this.nbreMessagesLocuteursDominants = nbreMessagesLocuteursDominants;
		this.nbreMessagesLD = nbreMessagesLD;
		this.nbreLocuteursDominants = nbreLocuteursDominants;
		this.nbreMessagesLocuteursDominantsSaufPremier = nbreMessagesLocuteursDominantsSaufPremier;
		this.locuteurDominant = locuteurDominant;
		fireListeChanged();
	}

	public int getNbreLocuteursDominants() {
		return nbreLocuteursDominants;
	}

	public int getNbrePL() {
		return nbrePL;
	}

	public int getNbreMessagesLocuteursDominants() {
		return nbreMessagesLocuteursDominants;
	}

	public int getNbreMessagesLocuteursDominantsSaufPremier() {
		return nbreMessagesLocuteursDominantsSaufPremier;
	}

	public float getPourcentLocuteursDominantsSaufPremier() {
		return pourcentLocuteursDominantsSaufPremier;
	}

	public String getLocuteurDominant() {
		return locuteurDominant;
	}

	public float getPourcentMessagesLD() {
		return pourcentMessagesLD;
	}

	public float getPourcentLocuteursDominants() {
		return pourcentLocuteursDominants;
	}

	public float getPourcentMessagesLocuteursDominants() {
		return pourcentMessagesLocuteursDominants;
	}

	public float getPourcentMessagesLocuteursDominantsSaufPremier() {
		return pourcentMessagesLocuteursDominantsSaufPremier;
	}

	public int getNbreLocuteursDominants3PremiersMois() {
		return nbreLocuteursDominants3PremiersMois;
	}

	public float getPourcentLocuteursDominants3PremiersMois() {
		return pourcentLocuteursDominants3PremiersMois;
	}

	public float getDureeMoyenneParticipationLocuteursDominants() {
		return dureeMoyenneParticipationLocuteursDominants;
	}

	public float getEcartTypeParticipationLocuteursDominants() {
		return ecartTypeParticipationLocuteursDominants;
	}

	public float getPourcentParticipationLocuteursDominants() {
		return pourcentParticipationLocuteursDominants;
	}

	public float getMoyenneIntensite() {
		return moyenneIntensite;
	}

	public int getNbreMessagesLD() {
		return nbreMessagesLD;
	}

	public float getPourcentPL() {
		return pourcentPL;
	}

	// // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // GETTER + SETTER SUR CONVERSATIONS // // // // //

	public SortedSet<ConversationModel> getSetConversations() {
		return setConversations;
	}

	public void setSetConversations(SortedSet<ConversationModel> setConversations) {
		this.setConversations = setConversations;
		fireListeChanged();
	}

	public void setCalculSurConversations(int nbreConversations, float nbreMoyenMessagesConversation, float nbreMoyenLocuteursDifferentsSujet) {
		this.nbreConversations = nbreConversations;
		this.nbreMoyenMessagesConversation = nbreMoyenMessagesConversation;
		this.nbreMoyenLocuteursDifferentsSujet = nbreMoyenLocuteursDifferentsSujet;
		fireListeChanged();
	}

	public int getNbreConversations() {
		return nbreConversations;
	}

	public float getNbreMoyenLocuteursDifferentsSujet() {
		return nbreMoyenLocuteursDifferentsSujet;
	}

	public float getNbreMoyenMessagesConversation() {
		return nbreMoyenMessagesConversation;
	}

	// SUJETS COLLECTIFS

	public void setCalculSujetsCollectifs(int nbreSC, int nbreMessagesSC, int nbreMessagesLocuteursDominantsSC, int nbreMessagesLDSC, int nbreMessagesLocuteursDominantsSaufPremierSC, int nbrePLSC,
			int nbreMessagesPLSC, int nbreLanceursSC, int nbreLocuteursDominantsLanceursSC, int nbreSCLancesLD, int nbreSCLancesLocuteursDominantsSaufPremier, int nbreSCLancesPL, float pourcentSC,
			float pourcentMessagesSC, float pourcentMessagesLocuteursDominantsSC, float pourcentMessagesLDSC, float pourcentMessagesLocuteursDominantsSaufPremierSC, float pourcentPLSC,
			float pourcentMessagesPLSC, float pourcentLanceursSC, float pourcentLocuteursDominantsLanceursSC1, float pourcentLocuteursDominantsLanceursSC2, float pourcentSCLancesLD,
			float pourcentSCLancesLocuteursDominantsSaufPremier, float pourcentSCLancesPL, int nbreLocuteursDominantsSC, float pourcentLocuteursDominantsSC) {

		this.nbreSC = nbreSC;
		this.nbreMessagesSC = nbreMessagesSC;
		this.nbreMessagesLocuteursDominantsSC = nbreMessagesLocuteursDominantsSC;
		this.nbreMessagesLDSC = nbreMessagesLDSC;
		this.nbreMessagesLocuteursDominantsSaufPremierSC = nbreMessagesLocuteursDominantsSaufPremierSC;
		this.nbrePLSC = nbrePLSC;
		this.nbreMessagesPLSC = nbreMessagesPLSC;
		this.nbreLanceursSC = nbreLanceursSC;
		this.nbreLocuteursDominantsLanceursSC = nbreLocuteursDominantsLanceursSC;
		this.nbreSCLancesLD = nbreSCLancesLD;
		this.nbreSCLancesLocuteursDominantsSaufPremier = nbreSCLancesLocuteursDominantsSaufPremier;
		this.nbreSCLancesPL = nbreSCLancesPL;
		this.pourcentSC = pourcentSC;
		this.pourcentMessagesSC = pourcentMessagesSC;
		this.pourcentMessagesLocuteursDominantsSC = pourcentMessagesLocuteursDominantsSC;
		this.pourcentMessagesLDSC = pourcentMessagesLDSC;
		this.pourcentMessagesLocuteursDominantsSaufPremierSC = pourcentMessagesLocuteursDominantsSaufPremierSC;
		this.pourcentPLSC = pourcentPLSC;
		this.pourcentMessagesPLSC = pourcentMessagesPLSC;
		this.pourcentLanceursSC = pourcentLanceursSC;
		this.pourcentLocuteursDominantsLanceursSC2 = pourcentLocuteursDominantsLanceursSC1;
		this.pourcentLocuteursDominantsLanceursSC1 = pourcentLocuteursDominantsLanceursSC2;
		this.pourcentSCLancesLD = pourcentSCLancesLD;
		this.pourcentSCLancesLocuteursDominantsSaufPremier = pourcentSCLancesLocuteursDominantsSaufPremier;
		this.pourcentSCLancesPL = pourcentSCLancesPL;
		this.nbreLocuteursDominantsSC = nbreLocuteursDominantsSC;
		this.pourcentLocuteursDominantsSC = pourcentLocuteursDominantsSC;
		fireListeChanged();
	}

	public int getNbreMessagesSC() {
		return nbreMessagesSC;
	}

	public float getPourcentMessagesLocuteursDominantsSC() {
		return pourcentMessagesLocuteursDominantsSC;
	}

	public float getPourcentMessagesLocuteursDominantsSaufPremierSC() {
		return pourcentMessagesLocuteursDominantsSaufPremierSC;
	}

	public float getPourcentMessagesPLSC() {
		return pourcentMessagesPLSC;
	}

	public int getNbreMessagesPLSC() {
		return nbreMessagesPLSC;
	}

	public float getPourcentSC() {
		return pourcentSC;
	}

	public float getPourcentMessagesSC() {
		return pourcentMessagesSC;
	}

	public float getPourcentMessagesLDSC() {
		return pourcentMessagesLDSC;
	}

	public int getNbreMessagesLDSC() {
		return nbreMessagesLDSC;
	}

	public int getNbreMessagesLocuteursDominantsSC() {
		return nbreMessagesLocuteursDominantsSC;
	}

	public int getNbreMessagesLocuteursDominantsSaufPremierSC() {
		return nbreMessagesLocuteursDominantsSaufPremierSC;
	}

	public int getNbrePLSC() {
		return nbrePLSC;
	}

	public int getNbreLocuteursDominantsSC() {
		return nbreLocuteursDominantsSC;
	}

	public float getPourcentPLSC() {
		return pourcentPLSC;
	}

	public float getPourcentLocuteursDominantsSC() {
		return pourcentLocuteursDominantsSC;
	}

	public int getNbreSC() {
		return nbreSC;
	}

	public int getNbreLanceursSC() {
		return nbreLanceursSC;
	}

	public int getNbreLocuteursDominantsLanceursSC() {
		return nbreLocuteursDominantsLanceursSC;
	}

	public int getNbreSCLancesLD() {
		return nbreSCLancesLD;
	}

	public int getNbreSCLancesLocuteursDominantsSaufPremier() {
		return nbreSCLancesLocuteursDominantsSaufPremier;
	}

	public int getNbreSCLancesPL() {
		return nbreSCLancesPL;
	}

	public float getPourcentLanceursSC() {
		return pourcentLanceursSC;
	}

	public float getPourcentLocuteursDominantsLanceursSC1() {
		return pourcentLocuteursDominantsLanceursSC1;
	}

	public float getPourcentLocuteursDominantsLanceursSC2() {
		return pourcentLocuteursDominantsLanceursSC2;
	}

	public float getPourcentSCLancesLD() {
		return pourcentSCLancesLD;
	}

	public float getPourcentSCLancesLocuteursDominantsSaufPremier() {
		return pourcentSCLancesLocuteursDominantsSaufPremier;
	}

	public float getPourcentSCLancesPL() {
		return pourcentSCLancesPL;
	}

	// // // // // // // // // // // // // // // // // // // // // // // //

	// // // // // // // // GETTER + SETTER SUR PARAMETRES // // // // // //

	public boolean getParamNoRegroupementMessages() {
		return paramNoRegroupementMessages;
	}

	public void setParamNoRegroupementMessages(boolean paramNoRegroupementMessages) {
		this.paramNoRegroupementMessages = paramNoRegroupementMessages;
	}

	public void setParametresAnalyse(float paramLocuteursDominants, float paramSujetsCollectifs, boolean isLocuteursSC) {
		this.paramLocuteursDominants = paramLocuteursDominants;
		this.paramSujetsCollectifs = paramSujetsCollectifs;
		this.paramLocuteursSC = isLocuteursSC;
		fireListeChanged();
	}

	public void setParametresRegroupement(int paramJours, int paramMessages, int paramLevenshtein) {
		this.paramJours = paramJours;
		this.paramMessages = paramMessages;
		this.paramLevenshtein = paramLevenshtein;
		fireListeChanged();
	}

	public String getsParamRegroupementMessages() {
		return sParamRegroupementMessages;
	}

	public void setsParamRegroupementMessages(String sParamRegroupementMessages) {
		this.sParamRegroupementMessages = sParamRegroupementMessages;
		fireListeChanged();
	}

	public float getParamLocuteursDominants() {
		return paramLocuteursDominants;
	}

	public int getParamLevenshtein() {
		return paramLevenshtein;
	}

	public int getParamJours() {
		return paramJours;
	}

	public int getParamMessages() {
		return paramMessages;
	}

	public float getParamSujetsCollectifs() {
		return paramSujetsCollectifs;
	}

	public boolean getParamLocuteursSC() {
		return paramLocuteursSC;
	}

	public int getTypeParamRegroupementMessages() {
		return typeParamRegroupementMessages;
	}

	public void setTypeParamRegroupementMessages(int typeParamRegroupementMessages) {
		this.typeParamRegroupementMessages = typeParamRegroupementMessages;
		fireListeChanged();
	}

	// // // // // // // // // // // // // // // // // // // // // // // //

	// public void addListLocuteurs(List<Locuteur> listLocuteurs) {
	// this.listLocuteurs.addAll(listLocuteurs);
	// }
	//
	// public void addListConversations(List<Conversation> listConversations) {
	// this.listConversations.addAll(listConversations);
	// }

	//
	// public void addMessages(List<Message> listMessages, AvancementAction AA)
	// {
	// AA.setStep(1);
	// AA.setLabelAction("Ajout des messages");
	// addListMessages(listMessages);
	// AA.updateProgress();
	// AA.appendTxtArea("Ajout des messages \n");
	// if (getListMessages().size() != 0) {
	// // DIALOGUE PARAMETRES GLOBAUX
	// dialogueParamGlobaux(AA);
	// }
	// }
	//
	// public void addMessagesToSplitList(List<Message> listMessages,
	// AvancementAction AA) {
	// AA.setStep(1);
	// AA.setLabelAction("Ajout des messages");
	// addListMessages(listMessages);
	// AA.updateProgress();
	// AA.appendTxtArea("Ajout des messages \n");
	// }
	//

	//
	// public void addMessages(ListeAvancementAction AA) {
	//
	// }

	//
	// public void dialogueParamGlobaux(AvancementAction AA) {
	// // DIALOGUE DEMANDE PARAMETRES
	// DialogPanelCalculTout panelTout = new DialogPanelCalculTout(
	// getParamJours(), getParamMessages(), getParamLevenshtein(),
	// getParamLocuteursDominants(), getParamSujetsCollectifs(),
	// getParamLocuteursSC());
	// int result = JOptionPane.showOptionDialog(null, panelTout,
	// "Analyse des messages", JOptionPane.OK_CANCEL_OPTION,
	// JOptionPane.PLAIN_MESSAGE, null, null, null);
	// if (result == JOptionPane.OK_OPTION) {
	// int paramJours = Integer.parseInt(panelTout.getTxtParametreJours()
	// .getText());
	// int paramMessages = Integer.parseInt(panelTout
	// .getTxtParametreMessages().getText());
	// int paramLevenshtein = Integer.parseInt(panelTout
	// .getTxtParametreLevenshtein().getText());
	// String sParamLD = panelTout.getTxtParametreLD().getText();
	// if (sParamLD.indexOf(",") != -1)
	// sParamLD = sParamLD.replace(",", ".");
	// float paramLD = Float.parseFloat(sParamLD);
	// String sParamSC = panelTout.getTxtParametreSC().getText();
	// if (sParamSC.indexOf(",") != -1)
	// sParamSC = sParamSC.replace(",", ".");
	// float paramSC = Float.parseFloat(sParamSC);
	// if ((paramJours != -1 && paramMessages != -1)
	// || (paramJours == -1 && paramMessages == -1)) {
	// JOptionPane.showMessageDialog(null,
	// "Veuillez indiquer un paramètre jours OU messages.",
	// "Erreur", JOptionPane.ERROR_MESSAGE);
	// dialogueParamGlobaux(AA);
	// return;
	// } else if (paramLevenshtein >= 15) {
	// JOptionPane
	// .showMessageDialog(
	// null,
	// "Veuillez indiquer un paramètre de \nDistance de Levenshtein strictement inférieur à 15.",
	// "Erreur", JOptionPane.ERROR_MESSAGE);
	// dialogueParamGlobaux(AA);
	// return;
	// } else if (paramLD <= 0) {
	// JOptionPane
	// .showMessageDialog(
	// null,
	// "Veuillez indiquer un paramètre de sélection\n des locuteurs dominants strictement supérieur à 0.",
	// "Erreur", JOptionPane.ERROR_MESSAGE);
	// dialogueParamGlobaux(AA);
	// return;
	// } else if (paramSC <= 0) {
	// JOptionPane
	// .showMessageDialog(
	// null,
	// "Veuillez indiquer un paramètre de sélection\n des sujets collectifs strictement supérieur à 0.",
	// "Erreur", JOptionPane.ERROR_MESSAGE);
	// dialogueParamGlobaux(AA);
	// return;
	// } else {
	// setParamJours(paramJours);
	// setParamMessages(paramMessages);
	// setParamLevenshtein(paramLevenshtein);
	// setParamLocuteursDominants(paramLD);
	// setParamSujetsCollectifs(paramSC);
	// boolean paramLocuteursSC = panelTout.getChkParametreSC()
	// .isSelected();
	// setParamLocuteursSC(paramLocuteursSC);
	// calcule(AA);
	// }
	// }
	// }
	//
	//
	//
	// public void delMessages(List<Message> listMToDelete, AvancementAction AA)
	// {
	// AA.setStep(1);
	// AA.setLabelAction("Suppression des messages");
	// listMessages.removeAll(listMToDelete);
	// setNbreMessages(listMessages.size());
	// setNbreFichiers(nbreFichiers - listMToDelete.size());
	// AA.updateProgress();
	// AA.appendTxtArea("Suppression des messages \n");
	// if (listMessages.size() != 0)
	// calcule(AA);
	// AA.reset();
	// }
	//
	// public void questionNettoyage(AvancementAction AA) {
	// AA.setLabelAction("Nettoyage");
	// System.out.println("question nettoyage");
	// int diag = JOptionPane.showOptionDialog(null,
	// "Voulez-vous nettoyer les locuteurs de messages?",
	// "Nettoyage des locuteurs", JOptionPane.YES_NO_OPTION,
	// JOptionPane.PLAIN_MESSAGE, null, null, null);
	// if (diag == JOptionPane.NO_OPTION) {
	// System.out.println("pas de nettoyage des locuteurs");
	// AA.appendTxtArea("Pas de nettoyage des locuteurs \n");
	// int diag2 = JOptionPane.showOptionDialog(null,
	// "Voulez-vous nettoyer les sujets de messages ?",
	// "Nettoyage des sujets", JOptionPane.YES_NO_OPTION,
	// JOptionPane.PLAIN_MESSAGE, null, null, null);
	// if (diag2 == JOptionPane.NO_OPTION) {
	// System.out.println("pas de nettoyage des sujets");
	// AA.appendTxtArea("Pas de nettoyage des sujets \n");
	// preCalcul(AA);
	// } else {
	// System.out.println("nettoyage des sujets");
	// modeNettoyageSujets(AA);
	// preCalcul(AA);
	// }
	// } else {
	// System.out.println("nettoyage des locuteurs");
	// modeNettoyageLocuteurs(AA);
	// int diag2 = JOptionPane.showOptionDialog(null,
	// "Voulez-vous nettoyer les sujets de messages ?",
	// "Nettoyage des sujets", JOptionPane.YES_NO_OPTION,
	// JOptionPane.PLAIN_MESSAGE, null, null, null);
	// if (diag2 == JOptionPane.NO_OPTION) {
	// System.out.println("pas de nettoyage des sujets");
	// AA.appendTxtArea("Pas de nettoyage des sujets \n");
	// preCalcul(AA);
	// } else {
	// System.out.println("nettoyage des sujets");
	// modeNettoyageSujets(AA);
	// preCalcul(AA);
	// }
	// }
	// }
	//
	// public void modeNettoyageLocuteurs(AvancementAction AA) {
	// DialogPanelNettoyage optPanel = new DialogPanelNettoyage();
	// int result = JOptionPane.showOptionDialog(null, optPanel,
	// "Nettoyage des locuteurs", JOptionPane.OK_CANCEL_OPTION,
	// JOptionPane.QUESTION_MESSAGE, null, null, null);
	// if (result == JOptionPane.OK_OPTION) {
	// String mode = optPanel.getListModeNettoyage().getSelectedItem()
	// .toString();
	// NettoyageLocuteurs nl = new NettoyageLocuteurs(this, AA);
	// if (mode.equals("Automatique"))
	// nl.nettoyageAutoLocuteurs();
	// else
	// nl.nettoyageManuelLocuteurs();
	//
	// }
	// }
	//
	// public void modeNettoyageSujets(AvancementAction AA) {
	// DialogPanelNettoyage optPanel = new DialogPanelNettoyage();
	// int result = JOptionPane.showOptionDialog(null, optPanel,
	// "Nettoyage des sujets", JOptionPane.OK_CANCEL_OPTION,
	// JOptionPane.QUESTION_MESSAGE, null, null, null);
	// if (result == JOptionPane.OK_OPTION) {
	// String mode = optPanel.getListModeNettoyage().getSelectedItem()
	// .toString();
	// NettoyageSujets ns = new NettoyageSujets(this, AA);
	// if (mode.equals("Automatique"))
	// ns.nettoyageAutoSujets();
	// else
	// ns.nettoyageManuelSujets();
	// }
	// }
	//
	// public void initAnalyse(AvancementAction AA) {
	// AA.setStep(1);
	// AA.setLabelAction("Initialisation de l'analyse");
	// initLocuteursLD(AA);
	// initLocuteursSC(AA);
	// initSC(AA);
	// setNbrePL(0);
	// setNbreLocuteursDominants(0);
	// setNbreMessagesLocuteursDominants(0);
	// setNbreMessagesLD(0);
	// setNbreMessagesLocuteursDominantsSaufPremier(0);
	// setNbreLocuteursDominants3PremiersMois(0);
	// setNbreSC(0);
	// setNbreMessagesSC(0);
	// setNbreMessagesLDSC(0);
	// setNbreMessagesLocuteursDominantsSC(0);
	// setNbreMessagesLocuteursDominantsSaufPremierSC(0);
	// setNbrePLSC(0);
	// setNbreMessagesPLSC(0);
	// setNbreLanceursSC(0);
	// setNbreLocuteursDominantsLanceursSC(0);
	// setNbreSCLancesLD(0);
	// setNbreSCLancesLocuteursDominantsSaufPremier(0);
	// setNbreSCLancesPL(0);
	// setParamLocuteursDominants(0);
	// setMoyenneIntensite(0);
	// setPourcentLocuteursDominantsSaufPremier(0);
	// setPourcentMessagesLD(0);
	// setPourcentLocuteursDominants(0);
	// setPourcentPL(0);
	// setPourcentMessagesLocuteursDominants(0);
	// setPourcentMessagesLocuteursDominantsSaufPremier(0);
	// setPourcentLocuteursDominants3PremiersMois(0);
	// setDureeMoyenneParticipationLocuteursDominants(0);
	// setEcartTypeParticipationLocuteursDominants(0);
	// setPourcentParticipationLocuteursDominants(0);
	// setParamSujetsCollectifs(0);
	// setPourcentSC(0);
	// setPourcentMessagesSC(0);
	// setPourcentMessagesLDSC(0);
	// setPourcentMessagesLocuteursDominantsSC(0);
	// setPourcentMessagesLocuteursDominantsSaufPremierSC(0);
	// setPourcentPLSC(0);
	// setPourcentMessagesPLSC(0);
	// setPourcentLanceursSC(0);
	// setPourcentLocuteursDominantsLanceursSC1(0);
	// setPourcentLocuteursDominantsLanceursSC2(0);
	// setPourcentSCLancesLD(0);
	// setPourcentSCLancesLocuteursDominantsSaufPremier(0);
	// setPourcentSCLancesPL(0);
	// AA.updateProgress();
	// AA.appendTxtArea("Initialisation de l'analyse\n");
	// AA.reset();
	// }
	//
	// public void questionAnalyse(AvancementAction AA) {
	// AA.setLabelAction("Analyse de la liste");
	// int diag3 = JOptionPane.showOptionDialog(null,
	// "Voulez-vous lancer l'analyse de la liste ?",
	// "Analyse de liste", JOptionPane.YES_NO_OPTION,
	// JOptionPane.PLAIN_MESSAGE, null, null, null);
	// if (diag3 == JOptionPane.OK_OPTION) {
	// analyse(AA, getParamLocuteursDominants(),
	// getParamSujetsCollectifs());
	// } else {
	// AA.appendTxtArea("Pas d'analyse de la liste\n");
	// initAnalyse(AA);
	// }
	// }
	//
	// public void preCalcul(AvancementAction AA) {
	// AA.setLabelAction("Analyse de la structure de la liste");
	// // calculSuivi(AA);
	// // calculListLocuteurs(AA);
	// questionParametresCalculConversations(AA);
	// questionAnalyse(AA);
	// }
	//
	// public void analyse(AvancementAction AA, float paramLD, float paramSC) {
	// initAnalyse(AA);
	// setParamLocuteursDominants(paramLD);
	// setParamSujetsCollectifs(paramSC);
	// System.out.println("analyse - paramLocuteursSC = "
	// + getParamLocuteursSC());
	// System.out.println("analyse - paramSC = " + getParamSujetsCollectifs());
	// if (listConversations.size() == 0)
	// questionParametresCalculConversations(AA);
	// AA.setLabelAction("Analyse de la liste");
	// // calculLocuteursDominants(AA, getParamLocuteursDominants());
	// questionPriseEnCompteLocuteursSC(AA, getParamSujetsCollectifs());
	// }
	//
	// private void questionParametresCalculConversations(AvancementAction AA) {
	// // Initialisation du calcul des Conversations
	// // Ouverture boite de dialogue avec les paramètres de calcul des
	// // Conversations :
	// // nombre de jours ou de messages entre deux sujets tronqués identiques
	// // Tests sur le contenu des labels du Dialogue
	// // Si les 2 paramètres jours et messages sont différents de -1 OU
	// égaux
	// // à -1 : return
	// // Sinon : calculConversations
	// AA.setLabelAction("Paramétrage du calcul des conversations");
	// DialogPanelParametreCalculConversations panelParamConv = new
	// DialogPanelParametreCalculConversations();
	// int result = JOptionPane.showOptionDialog(null, panelParamConv,
	// "Calcul des conversations", JOptionPane.OK_CANCEL_OPTION,
	// JOptionPane.PLAIN_MESSAGE, null, null, null);
	// // CalculConversations cc = new CalculConversations(this);
	// // if (result == JOptionPane.OK_OPTION) {
	// // int paramJours = Integer.parseInt(panelParamConv
	// // .getTextParametreJours().getText());
	// // int paramMessages = Integer.parseInt(panelParamConv
	// // .getTextParametreMessages().getText());
	// // int paramLevenshtein = Integer.parseInt(panelParamConv
	// // .getTextParametreLevenshtein().getText());
	// // if ((paramJours != -1 && paramMessages != -1)
	// // || (paramJours == -1 && paramMessages == -1)) {
	// // JOptionPane.showMessageDialog(null,
	// // "Veuillez indiquer un paramètre jours OU messages.",
	// // "Erreur", JOptionPane.ERROR_MESSAGE);
	// // questionParametresCalculConversations(AA);
	// // return;
	// // } else {
	// //
	// // cc.calculListConversations(AA, paramJours, paramMessages,
	// // paramLevenshtein);
	// // }
	// // } else
	// // cc.calculListConversations(AA, 30, -1, 1);
	// }
	//
	// private void initLocuteursLD(AvancementAction AA) {
	// // Initialisation du Tableau des Locuteurs Dominants
	// // On passe le champs LD à FALSE
	// AA.setStep(listLocuteurs.size());
	// AA.setLabelAction("Initialisation : locuteurs / dominance");
	// for (Locuteur locuteur : listLocuteurs) {
	// AA.updateProgress();
	// // LD
	// locuteur.setLd(false);
	// }
	// AA.appendTxtArea("Initialisation : locuteurs / dominance\n");
	// AA.reset();
	// }
	//
	// private void initLocuteursSC(AvancementAction AA) {
	// AA.setStep(listLocuteurs.size());
	// AA.setLabelAction("Initialisation : locuteurs / sujets collectifs");
	// for (Locuteur locuteur : listLocuteurs) {
	// AA.updateProgress();
	// // LANCEMENTS
	// locuteur.setLanceurSc(false);
	// locuteur.setNbreSujetsCollectifsLances(0);
	// // PARTICIPATION A SC
	// locuteur.setNbreSujetsCollectifs(0);
	// locuteur.setNbreMessagesSC(0);
	// locuteur.setPourcentMessagesSC(0);
	// }
	// AA.appendTxtArea("Initialisation : locuteurs / sujets collectifs\n");
	// AA.reset();
	// }
	//
	// private void initSC(AvancementAction AA) {
	// AA.setStep(listConversations.size());
	// AA.setLabelAction("Initialisation : Sujets collectifs");
	// for (Conversation conversation : listConversations) {
	// AA.updateProgress();
	// conversation.setSc(false);
	// }
	// AA.appendTxtArea("Initialisation : Sujets collectifs\n");
	// AA.reset();
	// }
	//
	// private void questionPriseEnCompteLocuteursSC(AvancementAction AA,
	// float paramSujetsCollectifs) {
	// int result = JOptionPane
	// .showOptionDialog(
	// null,
	// "Voulez-vous prendre en compte le nombre de locuteurs différents\n"
	// + "par conversation dans le calcul des sujets collectifs ?",
	// "Calcul des sujets collectifs",
	// JOptionPane.YES_NO_OPTION,
	// JOptionPane.INFORMATION_MESSAGE, null, null, null);
	// if (result == JOptionPane.YES_OPTION) {
	// setParamLocuteursSC(true);
	// } else
	// setParamLocuteursSC(false);
	// System.out
	// .println("questionPriseEnCompteLocuteursSC - paramLocuteursSC = "
	// + paramLocuteursSC);
	// System.out.println("questionPriseEnCompteLocuteursSC - paramSC = "
	// + paramSujetsCollectifs);
	// // calculSujetsCollectifs(AA, getParamLocuteursSC(),
	// // getParamSujetsCollectifs());
	// }
	//
	// public void calcule(AvancementAction AA) {
	// // CALCUL SUR TABLEAU DES MESSAGES
	// CalculSurTableauMessages calculTabMessages = new
	// CalculSurTableauMessages(
	// this, AA);
	// // CALCUL FILS DISCUSSION
	// setMapFils(calculTabMessages.getMapFils());
	// // AUTRES CALCULS
	// calculTabMessages.calcule();
	//
	// // CREATION DU TABLEAU DES LOCUTEURS
	// CreationTableauLocuteurs creationTabLocuteurs = new
	// CreationTableauLocuteurs(
	// getListMessages(), AA);
	// List<Locuteur> newListLocuteurs = creationTabLocuteurs
	// .getListLocuteurs();
	// // AJOUT A LA LISTE DES LOCUTEURS
	// setListLocuteurs(newListLocuteurs);
	// // CALCUL SUR LE TABLEAU DES LOCUTEURS
	// CalculSurTableauLocuteurs calculTabLocuteurs = new
	// CalculSurTableauLocuteurs(
	// this, AA);
	// calculTabLocuteurs.calcule();
	// // CREATION DU TABLEAU DES CONVERSATIONS
	// CreationTableauConversations creationTabConversations = new
	// CreationTableauConversations(
	// this, AA, getParamJours(), getParamMessages(),
	// getParamLevenshtein());
	// List<Conversation> newListConversations = creationTabConversations
	// .getListConversations();
	// // AJOUT A LA LISTE DES CONVERSATIONS
	// setListConversations(newListConversations);
	// // CALCUL SUR TABLEAU CONVERSATIONS
	// CalculSurTableauConversations calculTabConversations = new
	// CalculSurTableauConversations(
	// this, AA);
	// calculTabConversations.calcule();
	// // MAJ DU TABLEAU DES LOCUTEURS SELON TABLEAU CONVERSATIONS
	// calculTabLocuteurs.miseAJourNbreConversations();
	// // CALCUL LOCUTEURS DOMINANTS
	// CalculStats cs = new CalculStats(this, AA);
	// cs.calculLocuteursDominants();
	// // CALCUL SUJETS COLLECTIFS
	// cs.calculeSujetsCollectifs();
	//
	// // DialogPanelCalculTout panelTout = new DialogPanelCalculTout(
	// // getTypeParamConversations(), getParamConversations(),
	// // getParamLevenshtein(), getParamLocuteursDominants(),
	// // getParamSujetsCollectifs(), getParamLocuteursSC());
	// // int result = JOptionPane.showOptionDialog(null, panelTout,
	// // "Analyse des messages", JOptionPane.OK_CANCEL_OPTION,
	// // JOptionPane.PLAIN_MESSAGE, null, null, null);
	// // if (result == JOptionPane.OK_OPTION) {
	// // int paramJours = Integer.parseInt(panelTout.getTxtParametreJours()
	// // .getText());
	// // int paramMessages = Integer.parseInt(panelTout
	// // .getTxtParametreMessages().getText());
	// // if ((paramJours != -1 && paramMessages != -1)
	// // || (paramJours == -1 && paramMessages == -1)) {
	// // JOptionPane.showMessageDialog(null,
	// // "Veuillez indiquer un paramètre jours OU messages.",
	// // "Erreur", JOptionPane.ERROR_MESSAGE);
	// // calcule(AA);
	// // return;
	// // } else {
	// // AA.setLabelAction("Analyse de la structure de la liste");
	// // // calculSuivi(AA);
	// // // calculListLocuteurs(AA);
	// // // String typeParamConv = panelTout.getTypeParamConversations();
	// // // int paramLevenshtein = Integer.parseInt(panelTout
	// // // .getTxtParametreLevenshtein().getText());
	// // // CalculConversations cc = new CalculConversations(this);
	// // // if (typeParamConv.equals("jours")) {
	// // // cc.calculListConversations(AA, paramJours, -1,
	// // // paramLevenshtein);
	// // // } else {
	// // // cc.calculListConversations(AA, -1, paramMessages,
	// // // paramLevenshtein);
	// // // }
	// // initAnalyse(AA);
	// // AA.setLabelAction("Analyse de la liste");
	// // String sParamLD = panelTout.getTxtParametreLD().getText();
	// // if (sParamLD.indexOf(",") != -1)
	// // sParamLD = sParamLD.replace(",", ".");
	// // float paramLD = Float.parseFloat(sParamLD);
	// // setParamLocuteursDominants(paramLD);
	// // calculLocuteursDominants(AA, getParamLocuteursDominants());
	// // boolean paramLocuteursSC = panelTout.getChkParametreSC()
	// // .isSelected();
	// // String sParamSC = panelTout.getTxtParametreSC().getText();
	// // if (sParamSC.indexOf(",") != -1)
	// // sParamSC = sParamSC.replace(",", ".");
	// // float paramSC = Float.parseFloat(sParamSC);
	// // setParamLocuteursSC(paramLocuteursSC);
	// // setParamSujetsCollectifs(paramSC);
	// // System.out.println("calculTout - paramLocuteursSC = "
	// // + getParamLocuteursSC());
	// // calculSujetsCollectifs(AA, getParamLocuteursSC(),
	// // getParamSujetsCollectifs());
	// // }
	// // }
	// }
	public void init() {
		// INFOS LISTE
		numero = 0;
		nom = "";
		dateAjout = "";
		nbreMessages = 0;

		// PARAMETRES
		paramNoRegroupementMessages = false;
		sParamRegroupementMessages = "∅";
		typeParamRegroupementMessages = 0;
		paramJours = 30;
		paramMessages = -1;
		paramLevenshtein = 1;
		paramLocuteursDominants = 2;
		paramSujetsCollectifs = 2;
		paramLocuteursSC = true;

		mapIdMessage = new HashMap<String, MessageModel>();

		nbreLocuteurs = 0;
		nbreLocuteursUnSeulMessage = 0;
		nbreConversations = 0;
		nbreLocuteursDominants = 0;
		nbrePL = 0;
		nbreMessagesLocuteursDominants = 0;
		nbreMessagesLD = 0;
		nbreMessagesLocuteursDominantsSaufPremier = 0;
		nbreLocuteursDominants3PremiersMois = 0;
		nbreSC = 0;
		nbreMessagesSC = 0;
		nbreMessagesLDSC = 0;
		nbreMessagesLocuteursDominantsSC = 0;
		nbreMessagesLocuteursDominantsSaufPremierSC = 0;
		nbrePLSC = 0;
		nbreLocuteursDominantsSC = 0;
		nbreMessagesPLSC = 0;
		nbreLanceursSC = 0;
		nbreLocuteursDominantsLanceursSC = 0;
		nbreSCLancesLD = 0;
		nbreSCLancesLocuteursDominantsSaufPremier = 0;
		nbreSCLancesPL = 0;
		nbreMoyenMessagesLocuteurMois = 0;
		nbreMoyenLocuteursDifferentsSujet = 0;
		nbreMoyenMessagesConversation = 0;
		moyenneIntensite = 0;
		pourcentLocuteursDominantsSaufPremier = 0;
		pourcentMessagesLD = 0;
		pourcentLocuteursDominants = 0;
		pourcentPL = 0;
		pourcentMessagesLocuteursDominants = 0;
		pourcentMessagesLocuteursDominantsSaufPremier = 0;
		pourcentLocuteursDominants3PremiersMois = 0;
		dureeMoyenneParticipationLocuteursDominants = 0;
		ecartTypeParticipationLocuteursDominants = 0;
		pourcentParticipationLocuteursDominants = 0;
		pourcentSC = 0;
		pourcentMessagesSC = 0;
		pourcentMessagesLDSC = 0;
		pourcentMessagesLocuteursDominantsSC = 0;
		pourcentMessagesLocuteursDominantsSaufPremierSC = 0;
		pourcentPLSC = 0;
		pourcentLocuteursDominantsSC = 0;
		pourcentMessagesPLSC = 0;
		pourcentLanceursSC = 0;
		pourcentLocuteursDominantsLanceursSC1 = 0;
		pourcentLocuteursDominantsLanceursSC2 = 0;
		pourcentSCLancesLD = 0;
		pourcentSCLancesLocuteursDominantsSaufPremier = 0;
		pourcentSCLancesPL = 0;
		dureeSuivi = "∅";
		locuteurDominant = "∅";
		setConversations = new TreeSet<ConversationModel>(new ConversationIdComparator());
		setLocuteurs = new TreeSet<LocuteurModel>(new LocuteurIdComparator());

		fireListeChanged();

	}

	public void initAfterNewMapIdMessages() {
		nbreLocuteurs = 0;
		nbreLocuteursUnSeulMessage = 0;
		nbreConversations = 0;
		nbreLocuteursDominants = 0;
		nbrePL = 0;
		nbreMessagesLocuteursDominants = 0;
		nbreMessagesLD = 0;
		nbreMessagesLocuteursDominantsSaufPremier = 0;
		nbreLocuteursDominants3PremiersMois = 0;
		nbreSC = 0;
		nbreMessagesSC = 0;
		nbreMessagesLDSC = 0;
		nbreMessagesLocuteursDominantsSC = 0;
		nbreMessagesLocuteursDominantsSaufPremierSC = 0;
		nbrePLSC = 0;
		nbreLocuteursDominantsSC = 0;
		nbreMessagesPLSC = 0;
		nbreLanceursSC = 0;
		nbreLocuteursDominantsLanceursSC = 0;
		nbreSCLancesLD = 0;
		nbreSCLancesLocuteursDominantsSaufPremier = 0;
		nbreSCLancesPL = 0;
		nbreMoyenMessagesLocuteurMois = 0;
		nbreMoyenLocuteursDifferentsSujet = 0;
		nbreMoyenMessagesConversation = 0;
		moyenneIntensite = 0;
		pourcentLocuteursDominantsSaufPremier = 0;
		pourcentMessagesLD = 0;
		pourcentLocuteursDominants = 0;
		pourcentPL = 0;
		pourcentMessagesLocuteursDominants = 0;
		pourcentMessagesLocuteursDominantsSaufPremier = 0;
		pourcentLocuteursDominants3PremiersMois = 0;
		dureeMoyenneParticipationLocuteursDominants = 0;
		ecartTypeParticipationLocuteursDominants = 0;
		pourcentParticipationLocuteursDominants = 0;
		pourcentSC = 0;
		pourcentMessagesSC = 0;
		pourcentMessagesLDSC = 0;
		pourcentMessagesLocuteursDominantsSC = 0;
		pourcentMessagesLocuteursDominantsSaufPremierSC = 0;
		pourcentPLSC = 0;
		pourcentLocuteursDominantsSC = 0;
		pourcentMessagesPLSC = 0;
		pourcentLanceursSC = 0;
		pourcentLocuteursDominantsLanceursSC1 = 0;
		pourcentLocuteursDominantsLanceursSC2 = 0;
		pourcentSCLancesLD = 0;
		pourcentSCLancesLocuteursDominantsSaufPremier = 0;
		pourcentSCLancesPL = 0;
		dureeSuivi = "∅";
		locuteurDominant = "∅";
		setConversations = new TreeSet<ConversationModel>(new ConversationIdComparator());
		setLocuteurs = new TreeSet<LocuteurModel>(new LocuteurIdComparator());

		fireListeChanged();
	}

	public void initBeforeRemove(int numero) {
		// INFOS LISTE
		this.numero = numero;
		nom = "";
		dateAjout = "";
		nbreMessages = 0;

		// PARAMETRES
		paramNoRegroupementMessages = false;
		sParamRegroupementMessages = "∅";
		typeParamRegroupementMessages = 0;
		paramJours = 0;
		paramMessages = 0;
		paramLevenshtein = 0;
		paramLocuteursDominants = 0;
		paramSujetsCollectifs = 0;
		paramLocuteursSC = true;

		mapIdMessage = null;

		nbreLocuteurs = 0;
		nbreLocuteursUnSeulMessage = 0;
		nbreConversations = 0;
		nbreLocuteursDominants = 0;
		nbrePL = 0;
		nbreMessagesLocuteursDominants = 0;
		nbreMessagesLD = 0;
		nbreMessagesLocuteursDominantsSaufPremier = 0;
		nbreLocuteursDominants3PremiersMois = 0;
		nbreSC = 0;
		nbreMessagesSC = 0;
		nbreMessagesLDSC = 0;
		nbreMessagesLocuteursDominantsSC = 0;
		nbreMessagesLocuteursDominantsSaufPremierSC = 0;
		nbrePLSC = 0;
		nbreLocuteursDominantsSC = 0;
		nbreMessagesPLSC = 0;
		nbreLanceursSC = 0;
		nbreLocuteursDominantsLanceursSC = 0;
		nbreSCLancesLD = 0;
		nbreSCLancesLocuteursDominantsSaufPremier = 0;
		nbreSCLancesPL = 0;
		nbreMoyenMessagesLocuteurMois = 0;
		nbreMoyenLocuteursDifferentsSujet = 0;
		nbreMoyenMessagesConversation = 0;
		moyenneIntensite = 0;
		pourcentLocuteursDominantsSaufPremier = 0;
		pourcentMessagesLD = 0;
		pourcentLocuteursDominants = 0;
		pourcentPL = 0;
		pourcentMessagesLocuteursDominants = 0;
		pourcentMessagesLocuteursDominantsSaufPremier = 0;
		pourcentLocuteursDominants3PremiersMois = 0;
		dureeMoyenneParticipationLocuteursDominants = 0;
		ecartTypeParticipationLocuteursDominants = 0;
		pourcentParticipationLocuteursDominants = 0;
		pourcentSC = 0;
		pourcentMessagesSC = 0;
		pourcentMessagesLDSC = 0;
		pourcentMessagesLocuteursDominantsSC = 0;
		pourcentMessagesLocuteursDominantsSaufPremierSC = 0;
		pourcentPLSC = 0;
		pourcentLocuteursDominantsSC = 0;
		pourcentMessagesPLSC = 0;
		pourcentLanceursSC = 0;
		pourcentLocuteursDominantsLanceursSC1 = 0;
		pourcentLocuteursDominantsLanceursSC2 = 0;
		pourcentSCLancesLD = 0;
		pourcentSCLancesLocuteursDominantsSaufPremier = 0;
		pourcentSCLancesPL = 0;
		dureeSuivi = "∅";
		locuteurDominant = "∅";
		setConversations = null;
		setLocuteurs = null;
	}

	public void initBeforeStat() {
		// // INFOS LISTE
		// this.numero = numero;
		// nom = "";
		// dateAjout = "";
		// nbreMessages = 0;
		//
		// // PARAMETRES
		// sParamRegroupementMessages = "";
		// typeParamConversations = 0;
		// paramJours = 0;
		// paramMessages = 0;
		// paramLevenshtein = 0;
		// paramLocuteursDominants = 0;
		// paramSujetsCollectifs = 0;
		// paramLocuteursSC = true;

		// mapIdMessage = null;

		// nbreLocuteurs = 0;
		// nbreLocuteursUnSeulMessage = 0;
		// nbreConversations = 0;
		nbreLocuteursDominants = 0;
		nbrePL = 0;
		nbreMessagesLocuteursDominants = 0;
		nbreMessagesLD = 0;
		nbreMessagesLocuteursDominantsSaufPremier = 0;
		nbreLocuteursDominants3PremiersMois = 0;
		nbreSC = 0;
		nbreMessagesSC = 0;
		nbreMessagesLDSC = 0;
		nbreMessagesLocuteursDominantsSC = 0;
		nbreMessagesLocuteursDominantsSaufPremierSC = 0;
		nbrePLSC = 0;
		nbreLocuteursDominantsSC = 0;
		nbreMessagesPLSC = 0;
		nbreLanceursSC = 0;
		nbreLocuteursDominantsLanceursSC = 0;
		nbreSCLancesLD = 0;
		nbreSCLancesLocuteursDominantsSaufPremier = 0;
		nbreSCLancesPL = 0;
		// nbreMoyenMessagesLocuteurMois = 0;
		// nbreMoyenLocuteursDifferentsSujet = 0;
		// nbreMoyenMessagesConversation = 0;
		moyenneIntensite = 0;
		pourcentLocuteursDominantsSaufPremier = 0;
		pourcentMessagesLD = 0;
		pourcentLocuteursDominants = 0;
		pourcentPL = 0;
		pourcentMessagesLocuteursDominants = 0;
		pourcentMessagesLocuteursDominantsSaufPremier = 0;
		pourcentLocuteursDominants3PremiersMois = 0;
		dureeMoyenneParticipationLocuteursDominants = 0;
		ecartTypeParticipationLocuteursDominants = 0;
		pourcentParticipationLocuteursDominants = 0;
		pourcentSC = 0;
		pourcentMessagesSC = 0;
		pourcentMessagesLDSC = 0;
		pourcentMessagesLocuteursDominantsSC = 0;
		pourcentMessagesLocuteursDominantsSaufPremierSC = 0;
		pourcentPLSC = 0;
		pourcentLocuteursDominantsSC = 0;
		pourcentMessagesPLSC = 0;
		pourcentLanceursSC = 0;
		pourcentLocuteursDominantsLanceursSC1 = 0;
		pourcentLocuteursDominantsLanceursSC2 = 0;
		pourcentSCLancesLD = 0;
		pourcentSCLancesLocuteursDominantsSaufPremier = 0;
		pourcentSCLancesPL = 0;
		// dureeSuivi = "non calculée";
		locuteurDominant = "∅";

		for (ConversationModel conversation : setConversations)
			conversation.setSc(false);

		for (LocuteurModel locuteur : setLocuteurs) {
			locuteur.setPourcentMessagesSC(0);
			locuteur.setNbreSujetsCollectifs(0);
			locuteur.setNbreSujetsCollectifsLances(0);
			locuteur.setNbreMessagesSC(0);
			locuteur.setLd(false);
			locuteur.setLanceurSc(false);
		}
	}

}
