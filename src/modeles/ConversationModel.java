package modeles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversationModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<String> listSujetsTronques = new ArrayList<String>();
	private Map<String, Integer> mapLocuteurNbreMessages = new HashMap<String, Integer>();
	private String sujetPremierMessage, lanceur, numeroPremierMessage;
	private int id, nbreMessages, duree, nbreLocuteurs;
	private Date dateDebut, dateFin;
	private boolean sc;
	// Attributs forum
	private String fName;
	private int fNbreVues;

	public ConversationModel() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<String, Integer> getMapLocuteurNbreMessages() {
		return mapLocuteurNbreMessages;
	}

	public void setMapLocuteurNbreMessages(
			Map<String, Integer> mapLocuteurNbreMessages) {
		this.mapLocuteurNbreMessages = mapLocuteurNbreMessages;
	}

	public String getSujetPremierMessage() {
		return sujetPremierMessage;
	}

	public void setSujetPremierMessage(String sujetPremierMessage) {
		this.sujetPremierMessage = sujetPremierMessage;
	}

	public String getNumeroPremierMessage() {
		return numeroPremierMessage;
	}

	public void setNumeroPremierMessage(String numeroPremierMessage) {
		this.numeroPremierMessage = numeroPremierMessage;
	}

	public String getLanceur() {
		return lanceur;
	}

	public void setLanceur(String lanceur) {
		this.lanceur = lanceur;
	}

	public int getNbreMessages() {
		return nbreMessages;
	}

	public void setNbreMessages(int nbreMessages) {
		this.nbreMessages = nbreMessages;
	}

	public int getDuree() {
		return duree;
	}

	public void setDuree(int duree) {
		this.duree = duree;
	}

	public int getNbreLocuteurs() {
		return nbreLocuteurs;
	}

	public void setNbreLocuteurs(int nbreLocuteurs) {
		this.nbreLocuteurs = nbreLocuteurs;
	}

	public Date getDateDebut() {
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	public Date getDateFin() {
		return dateFin;
	}

	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	public List<String> getListSujetsTronques() {
		return listSujetsTronques;
	}

	public void setListSujetsTronques(List<String> listSujetsTronques) {
		this.listSujetsTronques = listSujetsTronques;
	}

	public boolean isSc() {
		return sc;
	}

	public void setSc(boolean sc) {
		this.sc = sc;
	}

	public void addSujetTronque(String sujetTronque) {
		this.listSujetsTronques.add(sujetTronque);
	}

	public int getfNbreVues() {
		return fNbreVues;
	}

	public void setfNbreVues(int fNbreVues) {
		this.fNbreVues = fNbreVues;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}	
}
