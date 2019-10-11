package vue.dialog;

import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import modeles.LocuteurModel;

public class DialogPanelInfoLocuteur extends JPanel {

	private LocuteurModel locuteur;
	private ResourceBundle bundleFilsListe;

	public DialogPanelInfoLocuteur(LocuteurModel locuteur, ResourceBundle bundleFilsListe) {
		this.locuteur = locuteur;
		this.bundleFilsListe = bundleFilsListe;
		createGui();
	}

	public void createGui() {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Font gras = new Font("sansserif", Font.BOLD, 12);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel panelMessages = new JPanel();
		panelMessages.setLayout(new BoxLayout(panelMessages, BoxLayout.X_AXIS));
		panelMessages.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labMessages = new JLabel(bundleFilsListe.getString("txt_Messages") + " : ");
		labMessages.setFont(gras);
		JLabel txtMessages = new JLabel(String.valueOf(locuteur.getNbreMessages()));
		panelMessages.add(labMessages);
		panelMessages.add(txtMessages);

		add(panelMessages);

		JPanel panelConversations = new JPanel();
		panelConversations.setLayout(new BoxLayout(panelConversations, BoxLayout.X_AXIS));
		panelConversations.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labConversations = new JLabel(bundleFilsListe.getString("txt_Conversations") + " : ");
		labConversations.setFont(gras);
		JLabel txtConversations = new JLabel(String.valueOf(locuteur.getNbreConversations()));
		panelConversations.add(labConversations);
		panelConversations.add(txtConversations);

		add(panelConversations);

		JPanel panelDebut = new JPanel();
		panelDebut.setLayout(new BoxLayout(panelDebut, BoxLayout.X_AXIS));
		panelDebut.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labDebut = new JLabel(bundleFilsListe.getString("txt_Debut") + " : ");
		labDebut.setFont(gras);
		JLabel txtDebut = new JLabel((locuteur.getDateDebut() == null) ? "" : formatter.format(locuteur.getDateDebut()));
		panelDebut.add(labDebut);
		panelDebut.add(txtDebut);

		add(panelDebut);

		JPanel panelFin = new JPanel();
		panelFin.setLayout(new BoxLayout(panelFin, BoxLayout.X_AXIS));
		panelFin.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labFin = new JLabel(bundleFilsListe.getString("txt_Fin") + " : ");
		labFin.setFont(gras);
		JLabel txtFin = new JLabel((locuteur.getDateFin() == null) ? "" : formatter.format(locuteur.getDateFin()));
		panelFin.add(labFin);
		panelFin.add(txtFin);

		add(panelFin);

		JPanel panelDuree = new JPanel();
		panelDuree.setLayout(new BoxLayout(panelDuree, BoxLayout.X_AXIS));
		panelDuree.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labDuree = new JLabel(bundleFilsListe.getString("txt_DureeJours") + " : ");
		labDuree.setFont(gras);
		JLabel txtDuree = new JLabel(String.valueOf(locuteur.getDuree()));
		panelDuree.add(labDuree);
		panelDuree.add(txtDuree);

		add(panelDuree);

		JPanel panelConversationsCollectives = new JPanel();
		panelConversationsCollectives.setLayout(new BoxLayout(panelConversationsCollectives, BoxLayout.X_AXIS));
		panelConversationsCollectives.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labConversationsCollectives = new JLabel(bundleFilsListe.getString("txt_ConversationsCollectives") + " : ");
		labConversationsCollectives.setFont(gras);
		JLabel txtConversationsCollectives = new JLabel(String.valueOf(locuteur.getNbreSujetsCollectifs()));
		panelConversationsCollectives.add(labConversationsCollectives);
		panelConversationsCollectives.add(txtConversationsCollectives);

		add(panelConversationsCollectives);

		JPanel panelConversationsCollectivesLancees = new JPanel();
		panelConversationsCollectivesLancees.setLayout(new BoxLayout(panelConversationsCollectivesLancees, BoxLayout.X_AXIS));
		panelConversationsCollectivesLancees.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labConversationsCollectivesLancees = new JLabel(bundleFilsListe.getString("txt_ConversationsCollectivesLancees") + " : ");
		labConversationsCollectivesLancees.setFont(gras);
		JLabel txtConversationsCollectivesLancees = new JLabel(String.valueOf(locuteur.getNbreSujetsCollectifsLances()));
		panelConversationsCollectivesLancees.add(labConversationsCollectivesLancees);
		panelConversationsCollectivesLancees.add(txtConversationsCollectivesLancees);

		add(panelConversationsCollectivesLancees);

		JPanel panelMessagesConversationsCollectives = new JPanel();
		panelMessagesConversationsCollectives.setLayout(new BoxLayout(panelMessagesConversationsCollectives, BoxLayout.X_AXIS));
		panelMessagesConversationsCollectives.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labMessagesConversationsCollectives = new JLabel(bundleFilsListe.getString("txt_MessagesDansConversationsCollectives") + " : ");
		labMessagesConversationsCollectives.setFont(gras);
		JLabel txtMessagesConversationsCollectives = new JLabel(String.valueOf(locuteur.getNbreMessagesSC()));
		panelMessagesConversationsCollectives.add(labMessagesConversationsCollectives);
		panelMessagesConversationsCollectives.add(txtMessagesConversationsCollectives);

		add(panelMessagesConversationsCollectives);

		JPanel panelLd = new JPanel();
		panelLd.setLayout(new BoxLayout(panelLd, BoxLayout.X_AXIS));
		panelLd.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labLd = new JLabel(bundleFilsListe.getString("txt_LocuteurDominant") + " : ");
		labLd.setFont(gras);
		JLabel txtLd = new JLabel((locuteur.isLd() == false) ? bundleFilsListe.getString("txt_Non") : bundleFilsListe.getString("txt_Oui"));
		panelLd.add(labLd);
		panelLd.add(txtLd);

		add(panelLd);

		// Infos Forum
		if (locuteur.getfStatDateRegistrered() != null) {
			JPanel panelRole = new JPanel();
			panelRole.setLayout(new BoxLayout(panelRole, BoxLayout.X_AXIS));
			panelRole.setAlignmentX(Component.LEFT_ALIGNMENT);
			JLabel labRole = new JLabel(bundleFilsListe.getString("txt_Role") + " : ");
			labRole.setFont(gras);
			JLabel txtRole = new JLabel(String.valueOf(locuteur.getfRole()));
			panelRole.add(labRole);
			panelRole.add(txtRole);

			add(panelRole);

			JPanel panelQualite = new JPanel();
			panelQualite.setLayout(new BoxLayout(panelQualite, BoxLayout.X_AXIS));
			panelQualite.setAlignmentX(Component.LEFT_ALIGNMENT);
			JLabel labQualite = new JLabel(bundleFilsListe.getString("txt_Qualite") + " : ");
			labQualite.setFont(gras);
			JLabel txtQualite = new JLabel(String.valueOf(locuteur.getfStatPosition()));
			panelQualite.add(labQualite);
			panelQualite.add(txtQualite);

			add(panelQualite);
			
			JPanel panelActivite = new JPanel();
			panelActivite.setLayout(new BoxLayout(panelActivite, BoxLayout.X_AXIS));
			panelActivite.setAlignmentX(Component.LEFT_ALIGNMENT);
			JLabel labActivite = new JLabel(bundleFilsListe.getString("txt_Activite") + " : ");
			labActivite.setFont(gras);
			JLabel txtActivite = new JLabel(String.valueOf(locuteur.getfStatActivity()));
			panelActivite.add(labActivite);
			panelActivite.add(txtActivite);

			add(panelActivite);
			
			JPanel panelEtoiles = new JPanel();
			panelEtoiles.setLayout(new BoxLayout(panelEtoiles, BoxLayout.X_AXIS));
			panelEtoiles.setAlignmentX(Component.LEFT_ALIGNMENT);
			JLabel labEtoiles = new JLabel(bundleFilsListe.getString("txt_Rang") + " : ");
			labEtoiles.setFont(gras);
			JLabel txtEtoiles = new JLabel(String.valueOf(locuteur.getfStars()));
			panelEtoiles.add(labEtoiles);
			panelEtoiles.add(txtEtoiles);

			add(panelEtoiles);
		}

	}
}
