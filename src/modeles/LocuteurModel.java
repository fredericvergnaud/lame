package modeles;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class LocuteurModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private String nom;
	private Date dateDebut, dateFin;
	private float intensite, pourcentMessagesSC;
	private int duree, nbreMessages, nbreConversations, nbreSujetsCollectifs,
			nbreSujetsCollectifsLances, nbreMessagesSC;
	private Set<String> setGroupPosts = new HashSet<String>();
	private Set<String> setProfils = new HashSet<String>();
	private Set<String> setMails = new HashSet<String>();
	private boolean ld, lanceurSC;
	// Attributs forum
	private String fRole;
	private double fStars;
	// Stats
	private int fStatNbrePosts;
	private int fStatActivity;
	private String fStatPosition;
	private Date fStatDateRegistrered;
	private String fStatEMail;
	private String fStatWebsite;
	private String fStatGender;
	private int fStatAge;
	private String fStatLocation;
	private String fStatSignature;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getDuree() {
		return duree;
	}

	public void setDuree(int duree) {
		this.duree = duree;
	}

	public int getNbreMessages() {
		return nbreMessages;
	}

	public void setNbreMessages(int nbreMessages) {
		this.nbreMessages = nbreMessages;
	}

	public int getNbreConversations() {
		return nbreConversations;
	}

	public void setNbreConversations(int nbreConversations) {
		this.nbreConversations = nbreConversations;
	}

	public int getNbreSujetsCollectifs() {
		return nbreSujetsCollectifs;
	}

	public void setNbreSujetsCollectifs(int nbreSujetsCollectifs) {
		this.nbreSujetsCollectifs = nbreSujetsCollectifs;
	}

	public int getNbreSujetsCollectifsLances() {
		return nbreSujetsCollectifsLances;
	}

	public void setNbreSujetsCollectifsLances(int nbreSujetsCollectifsLances) {
		this.nbreSujetsCollectifsLances = nbreSujetsCollectifsLances;
	}

	public Set<String> getSetGroupPosts() {
		return setGroupPosts;
	}

	public void setSetGroupPosts(Set<String> setGroupPosts) {
		this.setGroupPosts = setGroupPosts;
	}

	public Set<String> getSetProfils() {
		return setProfils;
	}

	public void setSetProfils(Set<String> setProfils) {
		this.setProfils = setProfils;
	}

	public Set<String> getSetMails() {
		return setMails;
	}

	public void setSetMails(Set<String> setMails) {
		this.setMails = setMails;
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

	public float getIntensite() {
		return intensite;
	}

	public void setIntensite(float intensite) {
		this.intensite = intensite;
	}

	public float getPourcentMessagesSC() {
		return pourcentMessagesSC;
	}

	public void setPourcentMessagesSC(float pourcentMessagesSC) {
		this.pourcentMessagesSC = pourcentMessagesSC;
	}

	public int getNbreMessagesSC() {
		return nbreMessagesSC;
	}

	public void setNbreMessagesSC(int nbreMessagesSC) {
		this.nbreMessagesSC = nbreMessagesSC;
	}

	public boolean isLd() {
		return ld;
	}

	public void setLd(boolean ld) {
		this.ld = ld;
	}

	public boolean isLanceurSc() {
		return lanceurSC;
	}

	public void setLanceurSc(boolean lanceurSC) {
		this.lanceurSC = lanceurSC;
	}

	public boolean isLanceurSC() {
		return lanceurSC;
	}

	public void setLanceurSC(boolean lanceurSC) {
		this.lanceurSC = lanceurSC;
	}

	public String getfRole() {
		return fRole;
	}

	public void setfRole(String fRole) {
		this.fRole = fRole;
	}

	public String getfStatPosition() {
		return fStatPosition;
	}

	public void setfStatPosition(String fStatPosition) {
		this.fStatPosition = fStatPosition;
	}

	public double getfStars() {
		return fStars;
	}

	public void setfStars(double fStars) {
		this.fStars = fStars;
	}

	public int getfStatNbrePosts() {
		return fStatNbrePosts;
	}

	public void setfStatsNbrePosts(int fStatNbrePosts) {
		this.fStatNbrePosts = fStatNbrePosts;
	}

	public Date getfStatDateRegistrered() {
		return fStatDateRegistrered;
	}

	public void setfStatDateRegistrered(Date fStatDateRegistrered) {
		this.fStatDateRegistrered = fStatDateRegistrered;
	}

	public int getfStatActivity() {
		return fStatActivity;
	}

	public void setfStatActivity(int fStatActivity) {
		this.fStatActivity = fStatActivity;
	}

	public String getfStatEMail() {
		return fStatEMail;
	}

	public void setfStatEMail(String fStatEMail) {
		this.fStatEMail = fStatEMail;
	}

	public String getfStatWebsite() {
		return fStatWebsite;
	}

	public void setfStatWebsite(String fStatWebsite) {
		this.fStatWebsite = fStatWebsite;
	}

	public String getfStatGender() {
		return fStatGender;
	}

	public void setfStatGender(String fStatGender) {
		this.fStatGender = fStatGender;
	}

	public int getfStatAge() {
		return fStatAge;
	}

	public void setfStatAge(int fStatAge) {
		this.fStatAge = fStatAge;
	}

	public String getfStatLocation() {
		return fStatLocation;
	}

	public void setfStatLocation(String fStatLocation) {
		this.fStatLocation = fStatLocation;
	}

	public String getfStatSignature() {
		return fStatSignature;
	}

	public void setfStatSignature(String fStatSignature) {
		this.fStatSignature = fStatSignature;
	}
	
	
}
