package vue.dialog;

import java.awt.Component;
import java.awt.Font;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import modeles.ConversationModel;

public class DialogPanelInfoConversation extends JPanel {

	private ConversationModel conversation;
	private ResourceBundle bundleFilsListe;
	
	public DialogPanelInfoConversation(ConversationModel conversation, ResourceBundle bundleFilsListe) {
		this.conversation = conversation;
		this.bundleFilsListe = bundleFilsListe;
		createGui();
	}

	public void createGui() {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Font gras = new Font("sansserif", Font.BOLD, 12);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setAlignmentX(Component.LEFT_ALIGNMENT);

		JPanel panelSujet = new JPanel();
		panelSujet.setLayout(new BoxLayout(panelSujet, BoxLayout.X_AXIS));
		panelSujet.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labSujet = new JLabel(bundleFilsListe.getString("txt_Sujet") + " : ");
		labSujet.setFont(gras);
		JLabel txtSujet = new JLabel(String.valueOf(conversation.getSujetPremierMessage()));
		panelSujet.add(labSujet);
		panelSujet.add(txtSujet);

		add(panelSujet);

		JPanel panelLanceur = new JPanel();
		panelLanceur.setLayout(new BoxLayout(panelLanceur, BoxLayout.X_AXIS));
		panelLanceur.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labLanceur = new JLabel(bundleFilsListe.getString("txt_Lanceur") + " : ");
		labLanceur.setFont(gras);
		JLabel txtLanceur = new JLabel(conversation.getLanceur());
		panelLanceur.add(labLanceur);
		panelLanceur.add(txtLanceur);

		add(panelLanceur);

		JPanel panelDebut = new JPanel();
		panelDebut.setLayout(new BoxLayout(panelDebut, BoxLayout.X_AXIS));
		panelDebut.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labDebut = new JLabel(bundleFilsListe.getString("txt_Debut") + " : ");
		labDebut.setFont(gras);
		JLabel txtDebut = new JLabel((conversation.getDateDebut() == null) ? "" : formatter.format(conversation.getDateDebut()));
		panelDebut.add(labDebut);
		panelDebut.add(txtDebut);

		add(panelDebut);

		JPanel panelFin = new JPanel();
		panelFin.setLayout(new BoxLayout(panelFin, BoxLayout.X_AXIS));
		panelFin.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labFin = new JLabel(bundleFilsListe.getString("txt_Fin") + " : ");
		labFin.setFont(gras);
		JLabel txtFin = new JLabel((conversation.getDateFin() == null) ? "" : formatter.format(conversation.getDateFin()));
		panelFin.add(labFin);
		panelFin.add(txtFin);

		add(panelFin);

		JPanel panelDuree = new JPanel();
		panelDuree.setLayout(new BoxLayout(panelDuree, BoxLayout.X_AXIS));
		panelDuree.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labDuree = new JLabel(bundleFilsListe.getString("txt_DureeJours") + " : ");
		labDuree.setFont(gras);
		JLabel txtDuree = new JLabel(String.valueOf(conversation.getDuree()));
		panelDuree.add(labDuree);
		panelDuree.add(txtDuree);

		add(panelDuree);
		
		JPanel panelMessages = new JPanel();
		panelMessages.setLayout(new BoxLayout(panelMessages, BoxLayout.X_AXIS));
		panelMessages.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labMessages = new JLabel(bundleFilsListe.getString("txt_Messages") + " : ");
		labMessages.setFont(gras);
		JLabel txtMessages = new JLabel(String.valueOf(conversation.getNbreMessages()));
		panelMessages.add(labMessages);
		panelMessages.add(txtMessages);

		add(panelMessages);
		
		JPanel panelLocuteurs = new JPanel();
		panelLocuteurs.setLayout(new BoxLayout(panelLocuteurs, BoxLayout.X_AXIS));
		panelLocuteurs.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labLocuteurs = new JLabel(bundleFilsListe.getString("txt_Locuteurs") + " : ");
		labLocuteurs.setFont(gras);
		JLabel txtLocuteurs = new JLabel(String.valueOf(conversation.getNbreLocuteurs()));
		panelLocuteurs.add(labLocuteurs);
		panelLocuteurs.add(txtLocuteurs);

		add(panelLocuteurs);
		
		JPanel panelSc = new JPanel();
		panelSc.setLayout(new BoxLayout(panelSc, BoxLayout.X_AXIS));
		panelSc.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labSc = new JLabel(bundleFilsListe.getString("txt_ConversationCollective") + " : ");
		labSc.setFont(gras);
		JLabel txtSc = new JLabel((conversation.isSc() == false) ? bundleFilsListe.getString("txt_Non") : bundleFilsListe.getString("txt_Oui"));
		panelSc.add(labSc);
		panelSc.add(txtSc);

		add(panelSc);
		
		// Infos Forum
		if (conversation.getfNbreVues() != 0) {
			JPanel panelVues = new JPanel();
			panelVues.setLayout(new BoxLayout(panelVues, BoxLayout.X_AXIS));
			panelVues.setAlignmentX(Component.LEFT_ALIGNMENT);
			JLabel labVues = new JLabel(bundleFilsListe.getString("txt_Vues") + " : ");
			labVues.setFont(gras);
			JLabel txtVues = new JLabel(String.valueOf(conversation.getfNbreVues()));
			panelVues.add(labVues);
			panelVues.add(txtVues);

			add(panelVues);	
		}

	}
}
