package modeles.evenements;

import java.util.Date;
import java.util.EventObject;
import java.util.Map;
import java.util.Set;

import modeles.ConversationModel;
import modeles.LocuteurModel;
import modeles.MessageModel;

public class ListeChangedEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	// INFOS
	private String newNom;
	private int newNumero, newNbreMessages;
	private String newDateAjout;
	private Map<String, MessageModel> newMapIdMessages;
	private String newDureeSuivi, newIdentifiantToShow;
	private Date debut, fin;
	private Set<ConversationModel> newSetConversations;
	private Set<LocuteurModel> newSetLocuteurs;
	private int newParamJours, newParamMessages, newParamLevenshtein;
	private boolean newParamLocuteursSC, newParamNoRegroupementMessages;
	// STATS SUR CONVERSATIONS
	private int newNbreSC, newNbreMessagesSC, newNbreLanceursSC, newNbrePLSC, newNbreLocuteursDominantsSC, newNbreMessagesPLSC, newNbreSCLancesPL, newNbreLocuteursDominants, newNbreMessagesLDSC,
			newNbreSCLancesLD, newNbreMessagesLocuteursDominantsSC, newNbreMessagesLocuteursDominantsSaufPremierSC, newNbreSCLancesLocuteursDominantsSaufPremier, newNbreLocuteursDominantsLanceursSC;
	private String newSParamConversations;
	private Float newNbreMoyenLocuteursDifferentsSujet, newNbreMoyenMessagesConversation, newParamSujetsCollectifs, newPourcentSC, newPourcentMessagesSC, newPourcentLanceursSC, newPourcentPLSC,
			newPourcentMessagesPLSC, newPourcentSCLancesPL, newPourcentMessagesLDSC, newPourcentSCLancesLD, newPourcentMessagesLocuteursDominantsSC,
			newPourcentMessagesLocuteursDominantsSaufPremierSC, newPourcentSCLancesLocuteursDominantsSaufPremier, newPourcentLocuteursDominantsLanceursSC1, newPourcentLocuteursDominantsLanceursSC2,
			newPourcentLocuteursDominantsSC;
	// STATS SUR LOCUTEURS
	private int newNbreLocuteurs, newNbreLocuteursUnSeulMessage, newNbrePL, newNbreMessagesLD, newNbreMessagesLocuteursDominants, newNbreMessagesLocuteursDominantsSaufPremier,
			newNbreLocuteursDominants3PremiersMois;
	private String newLocuteurDominant;
	private Float newParamLocuteursDominants, newMoyenneIntensite, newNbreMoyenMessagesLocuteurMois, newPourcentLocuteursDominants, newPourcentPL, newPourcentMessagesLD,
			newPourcentMessagesLocuteursDominants, newPourcentLocuteursDominantsSaufPremier, newPourcentMessagesLocuteursDominantsSaufPremier, newPourcentLocuteursDominants3PremiersMois,
			newDureeMoyenneParticipationLocuteursDominants, newEcartTypeParticipationLocuteursDominants, newPourcentParticipationLocuteursDominants;

	public ListeChangedEvent(
			Object source,
			String newNom,
			int newNumero,
			String newDateAjout,
			Map<String, MessageModel> newMapIdMessages,
			int newNbreMessages,
			Date debut,
			Date fin,
			String newDureeSuivi,
			String newIdentifiantToShow,
			//
			Set<ConversationModel> newSetConversations, Set<LocuteurModel> newSetLocuteurs, int newNbreSC, int newNbreMessagesSC, int newNbreLanceursSC, int newNbrePLSC, int newNbreMessagesPLSC,
			int newNbreSCLancesPL, int newNbreLocuteursDominants, int newNbreMessagesLDSC, int newNbreSCLancesLD, int newNbreMessagesLocuteursDominantsSC,
			int newNbreMessagesLocuteursDominantsSaufPremierSC, int newNbreSCLancesLocuteursDominantsSaufPremier, int newNbreLocuteursDominantsLanceursSC, float newNbreMoyenLocuteursDifferentsSujet,
			float newNbreMoyenMessagesConversation, float newParamSujetsCollectifs, float newPourcentSC, float newPourcentMessagesSC, float newPourcentLanceursSC, float newPourcentPLSC,
			float newPourcentMessagesPLSC, float newPourcentSCLancesPL, float newPourcentMessagesLDSC, float newPourcentSCLancesLD, float newPourcentMessagesLocuteursDominantsSC,
			float newPourcentMessagesLocuteursDominantsSaufPremierSC, float newPourcentSCLancesLocuteursDominantsSaufPremier, float newPourcentLocuteursDominantsLanceursSC1,
			float newPourcentLocuteursDominantsLanceursSC2, boolean newParamLocuteursSC,
			String newSParamConversations,
			int newNbreLocuteurs,
			int newNbreLocuteursUnSeulMessage,
			// int newNbreLocuteursDominants,
			int newNbrePL,
			int newNbreMessagesLD,
			int newNbreMessagesLocuteursDominants,
			int newNbreMessagesLocuteursDominantsSaufPremier,
			// int newNbreSCLancesLD, int newNbreMessagesLocuteursDominantsSC,
			int newNbreLocuteursDominants3PremiersMois, float newNbreMoyenMessagesLocuteurMois, float newParamLocuteursDominants, float newMoyenneIntensite, float newPourcentLocuteursDominants,
			float newPourcentPL, float newPourcentMessagesLD, float newPourcentMessagesLocuteursDominants, float newPourcentLocuteursDominantsSaufPremier,
			float newPourcentMessagesLocuteursDominantsSaufPremier, float newPourcentLocuteursDominants3PremiersMois, float newDureeMoyenneParticipationLocuteursDominants,
			float newEcartTypeParticipationLocuteursDominants, float newPourcentParticipationLocuteursDominants, String newLocuteurDominant, int newParamJours, int newParamMessages,
			int newParamLevenshtein, boolean newParamNoRegroupementMessages, int newNbreLocuteursDominantsSC, float newPourcentLocuteursDominantsSC) {
		super(source);
		this.newNom = newNom;
		this.newNumero = newNumero;
		this.newDateAjout = newDateAjout;
		this.newMapIdMessages = newMapIdMessages;
		this.newNbreMessages = newNbreMessages;
		this.debut = debut;
		this.fin = fin;
		this.newDureeSuivi = newDureeSuivi;
		this.newIdentifiantToShow = newIdentifiantToShow;
		this.newSetConversations = newSetConversations;
		this.newSetLocuteurs = newSetLocuteurs;
		this.newNbreSC = newNbreSC;
		this.newNbreMessagesSC = newNbreMessagesSC;
		this.newNbreLanceursSC = newNbreLanceursSC;
		this.newNbrePLSC = newNbrePLSC;
		this.newNbreMessagesPLSC = newNbreMessagesPLSC;
		this.newNbreSCLancesPL = newNbreSCLancesPL;
		this.newNbreLocuteursDominants = newNbreLocuteursDominants;
		this.newNbreMessagesLDSC = newNbreMessagesLDSC;
		this.newNbreSCLancesLD = newNbreSCLancesLD;
		this.newNbreMessagesLocuteursDominantsSC = newNbreMessagesLocuteursDominantsSC;
		this.newNbreMessagesLocuteursDominantsSaufPremierSC = newNbreMessagesLocuteursDominantsSaufPremierSC;
		this.newNbreSCLancesLocuteursDominantsSaufPremier = newNbreSCLancesLocuteursDominantsSaufPremier;
		this.newNbreLocuteursDominantsLanceursSC = newNbreLocuteursDominantsLanceursSC;
		this.newNbreMoyenLocuteursDifferentsSujet = newNbreMoyenLocuteursDifferentsSujet;
		this.newNbreMoyenMessagesConversation = newNbreMoyenMessagesConversation;
		this.newParamSujetsCollectifs = newParamSujetsCollectifs;
		this.newPourcentSC = newPourcentSC;
		this.newPourcentMessagesSC = newPourcentMessagesSC;
		this.newPourcentLanceursSC = newPourcentLanceursSC;
		this.newPourcentPLSC = newPourcentPLSC;
		this.newPourcentMessagesPLSC = newPourcentMessagesPLSC;
		this.newPourcentSCLancesPL = newPourcentSCLancesPL;
		this.newPourcentMessagesLDSC = newPourcentMessagesLDSC;
		this.newPourcentSCLancesLD = newPourcentSCLancesLD;
		this.newPourcentMessagesLocuteursDominantsSC = newPourcentMessagesLocuteursDominantsSC;
		this.newPourcentMessagesLocuteursDominantsSaufPremierSC = newPourcentMessagesLocuteursDominantsSaufPremierSC;
		this.newPourcentSCLancesLocuteursDominantsSaufPremier = newPourcentSCLancesLocuteursDominantsSaufPremier;
		this.newPourcentLocuteursDominantsLanceursSC1 = newPourcentLocuteursDominantsLanceursSC1;
		this.newPourcentLocuteursDominantsLanceursSC2 = newPourcentLocuteursDominantsLanceursSC2;
		this.newSParamConversations = newSParamConversations;
		this.newNbreLocuteurs = newNbreLocuteurs;
		this.newNbreLocuteursUnSeulMessage = newNbreLocuteursUnSeulMessage;
		this.newNbreMoyenMessagesLocuteurMois = newNbreMoyenMessagesLocuteurMois;
		this.newNbreLocuteursDominants = newNbreLocuteursDominants;
		this.newNbrePL = newNbrePL;
		this.newNbreMessagesLD = newNbreMessagesLD;
		this.newNbreLocuteursDominants = newNbreLocuteursDominants;
		this.newNbreMessagesLocuteursDominants = newNbreMessagesLocuteursDominants;
		this.newNbreMessagesLocuteursDominantsSaufPremier = newNbreMessagesLocuteursDominantsSaufPremier;
		this.newNbreLocuteursDominants3PremiersMois = newNbreLocuteursDominants3PremiersMois;
		this.newParamLocuteursDominants = newParamLocuteursDominants;
		this.newMoyenneIntensite = newMoyenneIntensite;
		this.newPourcentLocuteursDominants = newPourcentLocuteursDominants;
		this.newPourcentPL = newPourcentPL;
		this.newPourcentMessagesLD = newPourcentMessagesLD;
		this.newPourcentMessagesLocuteursDominants = newPourcentMessagesLocuteursDominants;
		this.newPourcentLocuteursDominantsSaufPremier = newPourcentLocuteursDominantsSaufPremier;
		this.newPourcentMessagesLocuteursDominantsSaufPremier = newPourcentMessagesLocuteursDominantsSaufPremier;
		this.newPourcentLocuteursDominants3PremiersMois = newPourcentLocuteursDominants3PremiersMois;
		this.newDureeMoyenneParticipationLocuteursDominants = newDureeMoyenneParticipationLocuteursDominants;
		this.newEcartTypeParticipationLocuteursDominants = newEcartTypeParticipationLocuteursDominants;
		this.newPourcentParticipationLocuteursDominants = newPourcentParticipationLocuteursDominants;
		this.newLocuteurDominant = newLocuteurDominant;
		this.newParamJours = newParamJours;
		this.newParamMessages = newParamMessages;
		this.newParamLevenshtein = newParamLevenshtein;
		this.newParamLocuteursSC = newParamLocuteursSC;
		this.newParamNoRegroupementMessages = newParamNoRegroupementMessages;
		this.newNbreLocuteursDominantsSC = newNbreLocuteursDominantsSC;
		this.newPourcentLocuteursDominantsSC = newPourcentLocuteursDominantsSC;
	}

	public String getNewNom() {
		return newNom;
	}

	public int getNewNumero() {
		return newNumero;
	}

	public int getNewNbreMessages() {
		return newNbreMessages;
	}

	public String getNewDateAjout() {
		return newDateAjout;
	}

	public Map<String, MessageModel> getNewMapIdMessages() {
		return newMapIdMessages;
	}

	public String getNewDureeSuivi() {
		return newDureeSuivi;
	}

	public String getNewIdentifiantToShow() {
		return newIdentifiantToShow;
	}

	public Date getDebut() {
		return debut;
	}

	public Date getFin() {
		return fin;
	}

	public Set<ConversationModel> getNewSetConversations() {
		return newSetConversations;
	}

	public Set<LocuteurModel> getNewSetLocuteurs() {
		return newSetLocuteurs;
	}

	public int getNewNbreSC() {
		return newNbreSC;
	}

	public int getNewNbreMessagesSC() {
		return newNbreMessagesSC;
	}

	public int getNewNbreLanceursSC() {
		return newNbreLanceursSC;
	}

	public int getNewNbrePLSC() {
		return newNbrePLSC;
	}

	public int getNewNbreMessagesPLSC() {
		return newNbreMessagesPLSC;
	}

	public int getNewNbreSCLancesPL() {
		return newNbreSCLancesPL;
	}

	public int getNewNbreLocuteursDominants() {
		return newNbreLocuteursDominants;
	}

	public int getNewNbreMessagesLDSC() {
		return newNbreMessagesLDSC;
	}

	public int getNewNbreSCLancesLD() {
		return newNbreSCLancesLD;
	}

	public int getNewNbreMessagesLocuteursDominantsSC() {
		return newNbreMessagesLocuteursDominantsSC;
	}

	public int getNewNbreMessagesLocuteursDominantsSaufPremierSC() {
		return newNbreMessagesLocuteursDominantsSaufPremierSC;
	}

	public int getNewNbreSCLancesLocuteursDominantsSaufPremier() {
		return newNbreSCLancesLocuteursDominantsSaufPremier;
	}

	public int getNewNbreLocuteursDominantsLanceursSC() {
		return newNbreLocuteursDominantsLanceursSC;
	}

	public String getNewSParamConversations() {
		return newSParamConversations;
	}

	public Float getNewNbreMoyenLocuteursDifferentsSujet() {
		return newNbreMoyenLocuteursDifferentsSujet;
	}

	public Float getNewNbreMoyenMessagesConversation() {
		return newNbreMoyenMessagesConversation;
	}

	public Float getNewParamSujetsCollectifs() {
		return newParamSujetsCollectifs;
	}

	public Float getNewPourcentSC() {
		return newPourcentSC;
	}

	public Float getNewPourcentMessagesSC() {
		return newPourcentMessagesSC;
	}

	public Float getNewPourcentLanceursSC() {
		return newPourcentLanceursSC;
	}

	public Float getNewPourcentPLSC() {
		return newPourcentPLSC;
	}

	public Float getNewPourcentMessagesPLSC() {
		return newPourcentMessagesPLSC;
	}

	public Float getNewPourcentSCLancesPL() {
		return newPourcentSCLancesPL;
	}

	public Float getNewPourcentMessagesLDSC() {
		return newPourcentMessagesLDSC;
	}

	public Float getNewPourcentSCLancesLD() {
		return newPourcentSCLancesLD;
	}

	public Float getNewPourcentMessagesLocuteursDominantsSC() {
		return newPourcentMessagesLocuteursDominantsSC;
	}

	public Float getNewPourcentMessagesLocuteursDominantsSaufPremierSC() {
		return newPourcentMessagesLocuteursDominantsSaufPremierSC;
	}

	public Float getNewPourcentSCLancesLocuteursDominantsSaufPremier() {
		return newPourcentSCLancesLocuteursDominantsSaufPremier;
	}

	public Float getNewPourcentLocuteursDominantsLanceursSC1() {
		return newPourcentLocuteursDominantsLanceursSC1;
	}

	public Float getNewPourcentLocuteursDominantsLanceursSC2() {
		return newPourcentLocuteursDominantsLanceursSC2;
	}

	public boolean getNewParamLocuteursSC() {
		return newParamLocuteursSC;
	}

	public int getNewNbreLocuteurs() {
		return newNbreLocuteurs;
	}

	public int getNewNbreLocuteursUnSeulMessage() {
		return newNbreLocuteursUnSeulMessage;
	}

	public int getNewNbrePL() {
		return newNbrePL;
	}

	public int getNewNbreMessagesLD() {
		return newNbreMessagesLD;
	}

	public int getNewNbreMessagesLocuteursDominants() {
		return newNbreMessagesLocuteursDominants;
	}

	public int getNewNbreMessagesLocuteursDominantsSaufPremier() {
		return newNbreMessagesLocuteursDominantsSaufPremier;
	}

	public int getNewNbreLocuteursDominants3PremiersMois() {
		return newNbreLocuteursDominants3PremiersMois;
	}

	public String getNewLocuteurDominant() {
		return newLocuteurDominant;
	}

	public Float getNewParamLocuteursDominants() {
		return newParamLocuteursDominants;
	}

	public Float getNewMoyenneIntensite() {
		return newMoyenneIntensite;
	}

	public Float getNewNbreMoyenMessagesLocuteurMois() {
		return newNbreMoyenMessagesLocuteurMois;
	}

	public Float getNewPourcentLocuteursDominants() {
		return newPourcentLocuteursDominants;
	}

	public Float getNewPourcentPL() {
		return newPourcentPL;
	}

	public Float getNewPourcentMessagesLD() {
		return newPourcentMessagesLD;
	}

	public Float getNewPourcentMessagesLocuteursDominants() {
		return newPourcentMessagesLocuteursDominants;
	}

	public Float getNewPourcentLocuteursDominantsSaufPremier() {
		return newPourcentLocuteursDominantsSaufPremier;
	}

	public Float getNewPourcentMessagesLocuteursDominantsSaufPremier() {
		return newPourcentMessagesLocuteursDominantsSaufPremier;
	}

	public Float getNewPourcentLocuteursDominants3PremiersMois() {
		return newPourcentLocuteursDominants3PremiersMois;
	}

	public Float getNewDureeMoyenneParticipationLocuteursDominants() {
		return newDureeMoyenneParticipationLocuteursDominants;
	}

	public Float getNewEcartTypeParticipationLocuteursDominants() {
		return newEcartTypeParticipationLocuteursDominants;
	}

	public Float getNewPourcentParticipationLocuteursDominants() {
		return newPourcentParticipationLocuteursDominants;
	}

	public int getNewParamJours() {
		return newParamJours;
	}

	public int getNewParamMessages() {
		return newParamMessages;
	}

	public int getNewParamLevenshtein() {
		return newParamLevenshtein;
	}

	public boolean getNewParamNoRegroupementMessages() {
		return newParamNoRegroupementMessages;
	}

	public int getNewNbreLocuteursDominantsSC() {
		return newNbreLocuteursDominantsSC;
	}

	public Float getNewPourcentLocuteursDominantsSC() {
		return newPourcentLocuteursDominantsSC;
	}
}
