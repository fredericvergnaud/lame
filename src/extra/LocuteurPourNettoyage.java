package extra;

public class LocuteurPourNettoyage {
	String ancienNom, nouveauNom, groupPost, profil, mail;

	public LocuteurPourNettoyage(String ancienNom, String nouveauNom,
			String groupPost, String profil, String mail) {
		this.ancienNom = ancienNom;
		this.nouveauNom = nouveauNom;
		this.groupPost = groupPost;
		this.profil = profil;
		this.mail = mail;
	}

	public String getAncienNom() {
		return ancienNom;
	}

	public String getNouveauNom() {
		return nouveauNom;
	}

	public String getGroupPost() {
		return groupPost;
	}

	public String getProfil() {
		return profil;
	}

	public String getMail() {
		return mail;
	}

	public void setAncienNom(String s) {
		this.ancienNom = s;
	}

	public void setNouveauNom(String s) {
		this.nouveauNom = s;
	}

	public void setGroupPost(String s) {
		this.groupPost = s;
	}

	public void setProfil(String s) {
		this.profil = s;
	}

	public void setMail(String s) {
		this.mail = s;
	}
}