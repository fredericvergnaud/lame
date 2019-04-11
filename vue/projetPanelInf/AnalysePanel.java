package vue.projetPanelInf;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import comparators.ConversationNbreLocuteursComparator;
import comparators.ConversationNbreMessagesComparator;
import renderers.CustomPieChartBoxPanel;
import renderers.CustomStatBoxPanel;
import renderers.RoundedPanel;
import vue.ToolBar;
import modeles.ConversationModel;
import modeles.evenements.ListeChangedEvent;
import controleurs.ListeController;
import controleurs.vuesabstraites.ListeView;

public class AnalysePanel extends ListeView {

	private DecimalFormat df = new DecimalFormat("0.00");
	private JPanel panel, cards;
	private JScrollPane scrollPanel;
	private LamePanel panelVide;
	private String locuteursTxtToPdf = null;
	private ResourceBundle ressourcesAnalyseListe;
	private CardLayout cl;
	private CustomStatBoxPanel boxa, boxb, boxc, boxd, boxe, boxf, boxg;
	private CustomPieChartBoxPanel chart1, chart2, chart3, chart4, chart5, chart6, chart7, chart8, chart9, chart10;

	public AnalysePanel(ListeController listeController, ResourceBundle ressourcesAnalyseListe) {
		super(listeController);
		this.ressourcesAnalyseListe = ressourcesAnalyseListe;
	}

	public JPanel getPanel() {
		panel = new JPanel();
		panel.setOpaque(true);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setPreferredSize(new Dimension(1300, 800));
		panel.setMinimumSize(new Dimension(1300, 800));
		panel.setMaximumSize(new Dimension(1300, 800));
		panel.setBackground(Color.WHITE);
		panel.setName("PANEL_PLEIN");

		RoundedPanel panelGauche = new RoundedPanel();
		panelGauche.setPreferredSize(new Dimension(340, 750));
		panelGauche.setMinimumSize(new Dimension(340, 750));
		panelGauche.setMaximumSize(new Dimension(340, 750));

		JPanel panelBoxesGauche = new JPanel();
		panelBoxesGauche.setLayout(new BoxLayout(panelBoxesGauche, BoxLayout.Y_AXIS));
		panelBoxesGauche.setOpaque(true);
		panelBoxesGauche.setAlignmentY(Component.TOP_ALIGNMENT);
		panelBoxesGauche.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelBoxesGauche.setBackground(Color.WHITE);
		panelBoxesGauche.setPreferredSize(new Dimension(330, 725));
		panelBoxesGauche.setMinimumSize(new Dimension(330, 725));
		panelBoxesGauche.setMaximumSize(new Dimension(330, 725));
		GridBagConstraints gbcGauche = new GridBagConstraints();

		RoundedPanel panelDroite = new RoundedPanel();
		panelDroite.setPreferredSize(new Dimension(950, 750));
		panelDroite.setMinimumSize(new Dimension(950, 750));
		panelDroite.setMaximumSize(new Dimension(950, 750));

		JPanel panelBoxesDroite = new JPanel();
		panelBoxesDroite.setLayout(new BoxLayout(panelBoxesDroite, BoxLayout.Y_AXIS));
		panelBoxesDroite.setOpaque(true);
		panelBoxesDroite.setAlignmentY(Component.TOP_ALIGNMENT);
		panelBoxesDroite.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelBoxesDroite.setBackground(Color.WHITE);
		GridBagConstraints gbcDroite = new GridBagConstraints();

		JPanel panel1 = new JPanel(new GridBagLayout());
		panel1.setPreferredSize(new Dimension(300, 170));
		panel1.setMinimumSize(new Dimension(300, 170));
		panel1.setMaximumSize(new Dimension(300, 170));
		GridBagConstraints gbc1 = new GridBagConstraints();

		// SEUILS DE SELECTIONS
		boxa = new CustomStatBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_SeuilsSelection") + "</center></html>", new ImageIcon(
				ToolBar.class.getResource("/images/icones/parametres_22.png")));
		boxa.resize(300, 165, 300, 45, 300, 120);

		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1.0;
		gbc1.anchor = GridBagConstraints.NORTH;
		panel1.add(boxa, gbc1);

		JPanel panel2 = new JPanel(new GridBagLayout());
		panel2.setPreferredSize(new Dimension(300, 130));
		panel2.setMinimumSize(new Dimension(300, 130));
		panel2.setMaximumSize(new Dimension(300, 130));
		GridBagConstraints gbc2 = new GridBagConstraints();

		// NOMBRE DE LOCUTEURS
		boxb = new CustomStatBoxPanel(ressourcesAnalyseListe.getString("txt_Locuteurs"), new ImageIcon(ToolBar.class.getResource("/images/icones/locuteurs_22.png")));

		gbc2.gridx = 0;
		gbc2.gridy = 0;
		gbc2.weightx = 1.0;
		gbc2.anchor = GridBagConstraints.WEST;
		panel2.add(boxb, gbc2);

		// NOMBRE DE CONVERSATIONS
		boxc = new CustomStatBoxPanel(ressourcesAnalyseListe.getString("txt_Conversations_UC"), new ImageIcon(ToolBar.class.getResource("/images/icones/conversations_22.png")));

		gbc2.gridx = 1;
		gbc2.gridy = 0;
		gbc2.weightx = 1.0;
		gbc2.anchor = GridBagConstraints.EAST;
		panel2.add(boxc, gbc2);

		JPanel panel3 = new JPanel(new GridBagLayout());
		panel3.setPreferredSize(new Dimension(300, 130));
		panel3.setMinimumSize(new Dimension(300, 130));
		panel3.setMaximumSize(new Dimension(300, 130));
		GridBagConstraints gbc3 = new GridBagConstraints();

		// NOMBRE DE LOCUTEURS DOMINANTS
		boxd = new CustomStatBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_LocuteursDominants") + "</center></html>", new ImageIcon(
				ToolBar.class.getResource("/images/icones/locuteurs_dominants_22.png")));

		gbc3.gridx = 0;
		gbc3.gridy = 0;
		gbc3.weightx = 1.0;
		gbc3.anchor = GridBagConstraints.WEST;
		panel3.add(boxd, gbc3);

		// NOMBRE DE SUJETS COLLECTIFS
		boxe = new CustomStatBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_SujetsCollectifs") + "</center></html>", new ImageIcon(
				ToolBar.class.getResource("/images/icones/conversations_collectives_22.png")));

		gbc3.gridx = 1;
		gbc3.gridy = 0;
		gbc3.weightx = 1.0;
		gbc3.anchor = GridBagConstraints.EAST;
		panel3.add(boxe, gbc3);

		JPanel panel4 = new JPanel(new GridBagLayout());
		panel4.setPreferredSize(new Dimension(300, 140));
		panel4.setMinimumSize(new Dimension(300, 140));
		panel4.setMaximumSize(new Dimension(300, 140));
		GridBagConstraints gbc4 = new GridBagConstraints();

		// LOCUTEUR DOMINANT
		boxf = new CustomStatBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_LocuteurDominant") + "</center></html>", new ImageIcon(
				ToolBar.class.getResource("/images/icones/locuteur_dominant_22.png")));
		boxf.resize(300, 135, 300, 45, 300, 90);

		gbc4.gridx = 0;
		gbc4.gridy = 0;
		gbc4.weightx = 1.0;
		gbc4.anchor = GridBagConstraints.CENTER;
		panel4.add(boxf, gbc4);

		JPanel panel5 = new JPanel(new GridBagLayout());
		panel5.setPreferredSize(new Dimension(300, 140));
		panel5.setMinimumSize(new Dimension(300, 140));
		panel5.setMaximumSize(new Dimension(300, 140));
		GridBagConstraints gbc5 = new GridBagConstraints();

		// CONVERSATION COLLECTIVE
		boxg = new CustomStatBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_PlusImportanteConversationCollective") + "</center></html>", new ImageIcon(
				ToolBar.class.getResource("/images/icones/conversation_collective_22.png")));
		boxg.resize(300, 135, 300, 45, 300, 90);

		gbc5.gridx = 0;
		gbc5.gridy = 0;
		gbc5.weightx = 1.0;
		gbc5.anchor = GridBagConstraints.CENTER;
		panel5.add(boxg, gbc5);

		// AJOUT DES PANELS A panelBoxesGauche

		gbcGauche.gridx = 0;
		gbcGauche.gridy = 0;
		gbcGauche.weighty = 1.0;
		gbcGauche.anchor = GridBagConstraints.NORTH;
		panelBoxesGauche.add(panel1, gbcGauche);

		gbcGauche.gridx = 0;
		gbcGauche.gridy = 1;
		gbcGauche.weighty = 1.0;
		gbcGauche.anchor = GridBagConstraints.NORTH;
		panelBoxesGauche.add(panel2, gbcGauche);

		gbcGauche.gridx = 0;
		gbcGauche.gridy = 2;
		gbcGauche.weighty = 1.0;
		gbcGauche.anchor = GridBagConstraints.NORTH;
		panelBoxesGauche.add(panel3, gbcGauche);

		gbcGauche.gridx = 0;
		gbcGauche.gridy = 3;
		gbcGauche.weighty = 1.0;
		gbcGauche.anchor = GridBagConstraints.NORTH;
		panelBoxesGauche.add(panel4, gbcGauche);

		gbcGauche.gridx = 0;
		gbcGauche.gridy = 4;
		gbcGauche.weighty = 1.0;
		gbcGauche.anchor = GridBagConstraints.SOUTH;
		panelBoxesGauche.add(panel5, gbcGauche);

		// AJOUT DE panelBoxesGauche A panelGauche

		panelGauche.add(panelBoxesGauche);

		// PANELS GRAPHS

		JPanel panel6 = new JPanel();
		panel6.setPreferredSize(new Dimension(900, 290));
		panel6.setMinimumSize(new Dimension(900, 290));
		panel6.setMaximumSize(new Dimension(900, 290));
		GridBagConstraints gbc6 = new GridBagConstraints();

		// GRAPH 1 : Répartition locs doms / petits locs

		chart1 = new CustomPieChartBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_TypeLocuteurs") + "</center></html>", ressourcesAnalyseListe);
		// chart1.resize(400, 265, 400, 45, 400, 200);

		gbc6.gridx = 0;
		gbc6.gridy = 0;
		gbc6.weightx = 1.0;
		gbc6.anchor = GridBagConstraints.WEST;
		panel6.add(chart1, gbc6);

		// GRAPH 2 : Répartition scs / non scs

		chart2 = new CustomPieChartBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_TypeConversations") + "</center></html>", ressourcesAnalyseListe);
		// chart2.resize(400, 265, 400, 45, 400, 200);

		gbc6.gridx = 1;
		gbc6.gridy = 0;
		gbc6.weightx = 1.0;
		gbc6.anchor = GridBagConstraints.EAST;
		panel6.add(chart2, gbc6);

		JPanel panel7 = new JPanel();
		panel7.setPreferredSize(new Dimension(900, 290));
		panel7.setMinimumSize(new Dimension(900, 290));
		panel7.setMaximumSize(new Dimension(900, 290));
		GridBagConstraints gbc7 = new GridBagConstraints();

		// GRAPH 3 : Répartition locs doms / petits locs

		chart3 = new CustomPieChartBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_RepartitionMessagesSelonTypesLocuteurs") + "</center></html>", ressourcesAnalyseListe);

		gbc7.gridx = 0;
		gbc7.gridy = 0;
		gbc7.weightx = 1.0;
		gbc7.anchor = GridBagConstraints.WEST;
		panel7.add(chart3, gbc7);

		// GRAPH 4 : Répartition locs doms / petits locs

		chart4 = new CustomPieChartBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_RepartitionMessagesSelonTypesConversations") + "</center></html>", ressourcesAnalyseListe);

		gbc7.gridx = 1;
		gbc7.gridy = 0;
		gbc7.weightx = 1.0;
		gbc7.anchor = GridBagConstraints.EAST;
		panel7.add(chart4, gbc7);

		JPanel panel8 = new JPanel();
		panel8.setPreferredSize(new Dimension(900, 290));
		panel8.setMinimumSize(new Dimension(900, 290));
		panel8.setMaximumSize(new Dimension(900, 290));
		GridBagConstraints gbc8 = new GridBagConstraints();

		// GRAPH 5 : Répartition scs / non scs

		chart5 = new CustomPieChartBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_RepartitionMessagesDansSCSelonTypesLocuteurs") + "</center></html>", ressourcesAnalyseListe);

		gbc8.gridx = 0;
		gbc8.gridy = 0;
		gbc8.weightx = 1.0;
		gbc8.anchor = GridBagConstraints.WEST;
		panel8.add(chart5, gbc8);

		// GRAPH 6 : Répartition petits locuteurs dans SCs

		chart6 = new CustomPieChartBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_ParticipationPLConversations") + "</center></html>", ressourcesAnalyseListe);

		gbc8.gridx = 1;
		gbc8.gridy = 0;
		gbc8.weightx = 1.0;
		gbc8.anchor = GridBagConstraints.CENTER;
		panel8.add(chart6, gbc8);

		// GRAPH 7 : Répartition locuteurs dominants dans SCs

		chart7 = new CustomPieChartBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_ParticipationLDConversations") + "</center></html>", ressourcesAnalyseListe);

		gbc8.gridx = 2;
		gbc8.gridy = 0;
		gbc8.weightx = 1.0;
		gbc8.anchor = GridBagConstraints.EAST;
		panel8.add(chart7, gbc8);

		JPanel panel9 = new JPanel();
		panel9.setPreferredSize(new Dimension(900, 290));
		panel9.setMinimumSize(new Dimension(900, 290));
		panel9.setMaximumSize(new Dimension(900, 290));
		GridBagConstraints gbc9 = new GridBagConstraints();

		// GRAPH 8 : Répartition lanceurs scs / non lanceurs scs

		chart8 = new CustomPieChartBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_RepartitionLocuteursLanceursSC") + "</center></html>", ressourcesAnalyseListe);

		gbc9.gridx = 0;
		gbc9.gridy = 0;
		gbc9.weightx = 1.0;
		gbc9.anchor = GridBagConstraints.WEST;
		panel9.add(chart8, gbc9);

		// GRAPH 9 : Répartition lanceurs de SCs : LsDs / LD / PsLs

		chart9 = new CustomPieChartBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_RepartitionSCSelonTypesLocuteursLanceurs") + "</center></html>", ressourcesAnalyseListe);

		gbc9.gridx = 0;
		gbc9.gridy = 0;
		gbc9.weightx = 1.0;
		gbc9.anchor = GridBagConstraints.CENTER;
		panel9.add(chart9, gbc9);

		// GRAPH 10 : Répartition SCs lancés : LDs / LD / PLs

		chart10 = new CustomPieChartBoxPanel("<html><center>" + ressourcesAnalyseListe.getString("txt_RepartitionLanceursSCSelonType") + "</center></html>", ressourcesAnalyseListe);

		gbc9.gridx = 2;
		gbc9.gridy = 0;
		gbc9.weightx = 1.0;
		gbc9.anchor = GridBagConstraints.WEST;
		panel9.add(chart10, gbc9);

		// AJOUT DES PANELS A panelBoxesDroite

		gbcDroite.gridx = 0;
		gbcDroite.gridy = 0;
		gbcDroite.weighty = 1.0;
		gbcDroite.anchor = GridBagConstraints.NORTH;
		panelBoxesDroite.add(panel6, gbcDroite);

		gbcDroite.gridx = 0;
		gbcDroite.gridy = 1;
		gbcDroite.weighty = 1.0;
		gbcDroite.anchor = GridBagConstraints.NORTH;
		panelBoxesDroite.add(panel7, gbcDroite);

		gbcDroite.gridx = 0;
		gbcDroite.gridy = 2;
		gbcDroite.weighty = 1.0;
		gbcDroite.anchor = GridBagConstraints.NORTH;
		panelBoxesDroite.add(panel8, gbcDroite);

		gbcDroite.gridx = 0;
		gbcDroite.gridy = 3;
		gbcDroite.weighty = 1.0;
		gbcDroite.anchor = GridBagConstraints.NORTH;
		panelBoxesDroite.add(panel9, gbcDroite);

		// Scrollpane panelDroite
		JScrollPane scrollDroite = new JScrollPane(panelBoxesDroite);
		scrollDroite.setPreferredSize(new Dimension(930, 720));
		scrollDroite.setMinimumSize(new Dimension(930, 720));
		scrollDroite.setMaximumSize(new Dimension(930, 720));
		scrollDroite.setBorder(null);

		// AJOUT DE panelBoxesDroite A panelDroite

		panelDroite.add(scrollDroite);

		panel.add(panelGauche);
		panel.add(panelDroite);

		// SCROLL PANEL
		scrollPanel = new JScrollPane(panel);
		scrollPanel.setBorder(null);
		scrollPanel.setName("PANEL_PLEIN");

		// CardLayout
		panelVide = new LamePanel();
		panelVide.setName("PANEL_VIDE");
		cards = new JPanel(new CardLayout());
		cards.add(scrollPanel, scrollPanel.getName());
		cards.add(panelVide.getPanel(), panelVide.getName());
		cl = (CardLayout) (cards.getLayout());
		cl.show(cards, "PANEL_VIDE");
		
		return cards;
	}

	@Override
	public JPanel getAnalysePanel() {
		return getPanel();
	}

	@Override
	public String getLocuteursTxtToPdf() {
		return locuteursTxtToPdf;
	}

	@Override
	public void listeChanged(ListeChangedEvent event) {
		Map<String, Double> mapData;
		if (event.getNewNbrePL() > 0) {
			String paramSC2 = "";
			if (event.getNewParamLocuteursSC() == true) {
				paramSC2 = "<div align=right><font style=font-size:12px;>" + (int) (event.getNewParamSujetsCollectifs() * event.getNewNbreMoyenLocuteursDifferentsSujet() + 0.5) + " "
						+ ressourcesAnalyseListe.getString("txt_locuteurs") + " / " + ressourcesAnalyseListe.getString("txt_conversation") + "</font></div>";
			}
			boxa.getTxtLabel().setText(
					"<html>"
							+ "<div align=left><font style=font-size:9px;color:#c1c1c1>"
							+ ressourcesAnalyseListe.getString("txt_LocuteursDominantsUneLigne")
							+ " :</font></div>"
							+ "<div align=right><font style=font-size:12px;>"
							+ ressourcesAnalyseListe.getString("txt_Intensite")
							+ " "
							+ String.valueOf(df.format(event.getNewParamLocuteursDominants() * event.getNewMoyenneIntensite()) + "</font></div><br>"
									+ "<div align=left><font style=font-size:9px;color:#c1c1c1>" + ressourcesAnalyseListe.getString("txt_SujetsCollectifsUneLigne") + " :</font></div>"
									+ "<div align=right><font style=font-size:12px;>" + (int) (event.getNewParamSujetsCollectifs() * event.getNewNbreMoyenMessagesConversation() + 0.5) + " "
									+ ressourcesAnalyseListe.getString("txt_MessagesMin") + " / " + ressourcesAnalyseListe.getString("txt_conversation") + "</font></div>" + paramSC2 + "</html>"));

			boxb.getTxtLabel().setText(String.valueOf(event.getNewNbreLocuteurs()));
			boxc.getTxtLabel().setText(String.valueOf(event.getNewSetConversations().size()));
			boxd.getTxtLabel().setText(String.valueOf(event.getNewNbreLocuteursDominants()));
			boxe.getTxtLabel().setText(String.valueOf(event.getNewNbreSC()));
			if (event.getNewLocuteurDominant() != null)
				boxf.getTxtLabel().setText(String.valueOf(event.getNewLocuteurDominant()));
			else
				boxf.getTxtLabel().setText("∅");
			List<ConversationModel> listConversations = new ArrayList<ConversationModel>(event.getNewSetConversations());
			Comparator<ConversationModel> byNbreMessages = new ConversationNbreMessagesComparator();
			Collections.sort(listConversations, byNbreMessages);
			Comparator<ConversationModel> byNbreLocuteurs = new ConversationNbreLocuteursComparator();
			Collections.sort(listConversations, byNbreLocuteurs);
			boxg.getTxtLabel().setText("<html><center>" + String.valueOf(listConversations.get(0).getSujetPremierMessage()) + "</center></html>");

			// Répartition des locuteurs : Locs Doms / Petits locs
			mapData = new HashMap<String, Double>();
			if (event.getNewNbreLocuteursDominants() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_LocuteursDominantsUneLigne"), (double) event.getNewNbreLocuteursDominants());
			if (event.getNewNbrePL() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_PetitsLocuteurs"), (double) event.getNewNbrePL());
			chart1.getGraphPanel().removeAll();
			chart1.createPieChart(mapData);
			chart1.addChartToChartPanel();
			chart1.resize(440, 265, 440, 45, 435, 200, 440, 220);

			// Data Graph répartition conversations entre SCs / non SCs
			mapData = new HashMap<String, Double>();
			if (event.getNewNbreSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_SujetsCollectifsUneLigne"), (double) event.getNewNbreSC());
			if (event.getNewSetConversations().size() - event.getNewNbreSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_AutresConversations"), (double) event.getNewSetConversations().size() - event.getNewNbreSC());
			chart2.getGraphPanel().removeAll();
			chart2.createPieChart(mapData);
			chart2.addChartToChartPanel();
			chart2.resize(440, 265, 440, 45, 435, 200, 440, 220);

			// Répartition des messages : LsDs / LD / PLs
			mapData = new HashMap<String, Double>();
			if (event.getNewNbreMessagesLD() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_LocuteurDominantUneLigne"), (double) event.getNewNbreMessagesLD());
			if (event.getNewNbreMessagesLocuteursDominantsSaufPremier() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_LocuteursDominantsUneLigne"), (double) event.getNewNbreMessagesLocuteursDominantsSaufPremier());
			if (event.getNewNbreMessages() - event.getNewNbreMessagesLD() - event.getNewNbreMessagesLocuteursDominantsSaufPremier() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_PetitsLocuteurs"),
						(double) event.getNewNbreMessages() - event.getNewNbreMessagesLD() - event.getNewNbreMessagesLocuteursDominantsSaufPremier());
			chart3.getGraphPanel().removeAll();
			chart3.createPieChart(mapData);
			chart3.addChartToChartPanel();
			chart3.resize(440, 265, 440, 45, 435, 200, 440, 220);

			// Data Graph répartition messages entre SCs / non SCs
			mapData = new HashMap<String, Double>();
			if (event.getNewNbreMessagesSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_SujetsCollectifsUneLigne"), (double) event.getNewNbreMessagesSC());
			if (event.getNewNbreMessages() - event.getNewNbreMessagesSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_AutresConversations"), (double) event.getNewNbreMessages() - event.getNewNbreMessagesSC());
			chart4.getGraphPanel().removeAll();
			chart4.createPieChart(mapData);
			chart4.addChartToChartPanel();
			chart4.resize(440, 265, 440, 45, 435, 200, 440, 220);

			// Répartition des messages dans SCs : LsDs / LD / PLs
			mapData = new HashMap<String, Double>();
			if (event.getNewNbreMessagesLDSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_LocuteurDominantUneLigne"), (double) event.getNewNbreMessagesLDSC());
			if (event.getNewNbreMessagesLocuteursDominantsSaufPremierSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_LocuteursDominantsUneLigne"), (double) event.getNewNbreMessagesLocuteursDominantsSaufPremierSC());
			if (event.getNewNbreMessagesSC() - event.getNewNbreMessagesLDSC() - event.getNewNbreMessagesLocuteursDominantsSaufPremierSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_PetitsLocuteurs"),
						(double) event.getNewNbreMessagesSC() - event.getNewNbreMessagesLDSC() - event.getNewNbreMessagesLocuteursDominantsSaufPremierSC());
			chart5.getGraphPanel().removeAll();
			chart5.createPieChart(mapData);
			chart5.addChartToChartPanel();

			// Répartition PLs dans SCs entre SCs / non SCs
			mapData = new HashMap<String, Double>();
			if (event.getNewNbrePLSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_SujetsCollectifsUneLigne"), (double) event.getNewNbrePLSC());
			if (event.getNewNbrePL() - event.getNewNbrePLSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_AutresConversations"), (double) event.getNewNbrePL() - event.getNewNbrePLSC());
			chart6.getGraphPanel().removeAll();
			chart6.createPieChart(mapData);
			chart6.addChartToChartPanel();

			// Répartition LDs dans SCs entre SCs / non SCs
			mapData = new HashMap<String, Double>();
			if (event.getNewNbreLocuteursDominantsSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_SujetsCollectifsUneLigne"), (double) event.getNewNbreLocuteursDominantsSC());
			if (event.getNewNbreLocuteursDominants() - event.getNewNbreLocuteursDominantsSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_AutresConversations"), (double) event.getNewNbreLocuteursDominants() - event.getNewNbreLocuteursDominantsSC());
			chart7.getGraphPanel().removeAll();
			chart7.createPieChart(mapData);
			chart7.addChartToChartPanel();

			// Répartition entre lanceurs SCs / non lanceurs
			// SCs
			mapData = new HashMap<String, Double>();
			if (event.getNewNbreLanceursSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_LanceursSC"), (double) event.getNewNbreLanceursSC());
			if (event.getNewNbreLocuteurs() - event.getNewNbreLanceursSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_NonLanceursSC"), (double) event.getNewNbreLocuteurs() - event.getNewNbreLanceursSC());
			chart8.getGraphPanel().removeAll();
			chart8.createPieChart(mapData);
			chart8.addChartToChartPanel();

			// Répartition des conversations collectives lancés : LsDs / LD / PL
			mapData = new HashMap<String, Double>();
			if (event.getNewNbreSCLancesLD() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_LocuteurDominantUneLigne"), (double) event.getNewNbreSCLancesLD());
			if (event.getNewNbreSCLancesLocuteursDominantsSaufPremier() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_LocuteursDominantsUneLigne"), (double) event.getNewNbreSCLancesLocuteursDominantsSaufPremier());
			if (event.getNewNbreSC() - event.getNewNbreSCLancesLD() - event.getNewNbreSCLancesLocuteursDominantsSaufPremier() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_PetitsLocuteurs"),
						(double) event.getNewNbreSC() - event.getNewNbreSCLancesLD() - event.getNewNbreSCLancesLocuteursDominantsSaufPremier());
			chart9.getGraphPanel().removeAll();
			chart9.createPieChart(mapData);
			chart9.addChartToChartPanel();

			// Répartition des lanceurs de sujets collectifs : LsDs / LD / PL
			mapData = new HashMap<String, Double>();
			if (event.getNewNbreLocuteursDominantsLanceursSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_LocuteursDominantsUneLigne"), (double) event.getNewNbreLocuteursDominantsLanceursSC());
			if (event.getNewNbreLanceursSC() - event.getNewNbreLocuteursDominantsLanceursSC() > 0)
				mapData.put(ressourcesAnalyseListe.getString("txt_PetitsLocuteurs"), (double) event.getNewNbreLanceursSC() - event.getNewNbreLocuteursDominantsLanceursSC());
			chart10.getGraphPanel().removeAll();
			chart10.createPieChart(mapData);
			chart10.addChartToChartPanel();

			// setLocuteurs = event.getNewSetLocuteurs();
			// labNbreLocuteurs.setText(locuteursListePanel.getString("txt_NbreLocuteurs")
			// + " : ");
			// txtNbreLocuteurs.setText(String.valueOf(event.getNewNbreLocuteurs()));
			// labNbreLocuteursUnSeulMessage.setText(locuteursListePanel.getString("txt_NbreLocuteursUnSeulMessage")
			// + " : ");
			// txtNbreLocuteursUnSeulMessage.setText(String.valueOf(event.getNewNbreLocuteursUnSeulMessage()));
			// labNbreMoyenMessagesLocuteurMois.setText(locuteursListePanel.getString("txt_NbreMoyenMessagesLocuteursMois")
			// + " : ");
			// txtNbreMoyenMessagesLocuteurMois.setText(df.format(event.getNewNbreMoyenMessagesLocuteurMois()));
			// labParamLD.setText(locuteursListePanel.getString("txt_ParamLD") +
			// " : ");
			// labNbreLD.setText(locuteursListePanel.getString("txt_NbreLD") +
			// " : ");
			// labNbrePL.setText(locuteursListePanel.getString("txt_NbrePL") +
			// " : ");
			// labLD.setText(locuteursListePanel.getString("txt_LD") + " : ");
			// labNbreMessagesLD.setText(locuteursListePanel.getString("txt_NbreMessagesLD")
			// + " : ");
			// labPourcentLDSaufPremier.setText(locuteursListePanel.getString("txt_PourcentLDSaufPremier")
			// + " : ");
			// labNbreLocuteursDominants3PremiersMois.setText(locuteursListePanel.getString("txt_NbreLocuteursDominants3PremiersMois")
			// + " : ");
			// labDureeMoyenneMoisParticipationLocuteursDominants.setText(locuteursListePanel.getString("txt_DureeMoyenneMoisParticipationLocuteursDominants")
			// + " : ");
			// // PDF
			// locuteursTxtToPdf = labNbreLocuteurs.getText() +
			// txtNbreLocuteurs.getText() + "\n" +
			// labNbreLocuteursUnSeulMessage.getText() +
			// txtNbreLocuteursUnSeulMessage.getText() + "\n";
			//
			// if (event.getNewParamLocuteursDominants() != 0) {
			// txtParamLD.setText(locuteursListePanel.getString("txt_ParamLD_1")
			// + " " + df.format(event.getNewParamLocuteursDominants() *
			// event.getNewMoyenneIntensite()) + " ("
			// + String.valueOf(event.getNewParamLocuteursDominants()) + " X " +
			// df.format(event.getNewMoyenneIntensite()) + " (" +
			// locuteursListePanel.getString("txt_ParamLD_2") + "))");
			// txtNbreLocuteursDominants.setText(String.valueOf(event.getNewNbreLocuteursDominants())
			// + " (" + df.format(event.getNewPourcentLocuteursDominants())
			// + locuteursListePanel.getString("txt_PourcentLocuteurs") + ")");
			// txtNbrePL.setText(String.valueOf(event.getNewNbrePL()) + " (" +
			// df.format(event.getNewPourcentPL()) +
			// locuteursListePanel.getString("txt_PourcentLocuteurs") + ")");
			// // PDF
			// locuteursTxtToPdf += labParamLD.getText() + txtParamLD.getText()
			// + "\n" + labNbreLD.getText() +
			// txtNbreLocuteursDominants.getText() + "\n" + labNbrePL.getText()
			// + txtNbrePL.getText()
			// + "\n";
			// // LABELS LOCUTEURS DOMINANTS
			// if (event.getNewNbreLocuteursDominants() > 0) {
			// labT1.setText("<html><br>" +
			// locuteursListePanel.getString("txt_LocuteursDominants") +
			// " &rsaquo;&rsaquo;&rsaquo;</br></html>");
			// txtLD.setText(event.getNewLocuteurDominant() + " (" +
			// event.getNewNbreMessagesLD() + " " +
			// locuteursListePanel.getString("txt_MessagesMin") + " - "
			// + df.format(event.getNewPourcentMessagesLD()) +
			// locuteursListePanel.getString("txt_PourcentMessages") + ")");
			// txtNbreMessagesLocuteursDominants.setText(String.valueOf(event.getNewNbreMessagesLocuteursDominants())
			// + " (" +
			// df.format(event.getNewPourcentMessagesLocuteursDominants())
			// + locuteursListePanel.getString("txt_PourcentMessages") + ")");
			// txtPourcentLDSaufPremier.setText(df.format(event.getNewPourcentLocuteursDominantsSaufPremier())
			// + "% (" +
			// String.valueOf(event.getNewNbreMessagesLocuteursDominantsSaufPremier())
			// + locuteursListePanel.getString("txt_MessagesSoit") + " " +
			// df.format(event.getNewPourcentMessagesLocuteursDominantsSaufPremier())
			// + locuteursListePanel.getString("txt_PourcentMessagesSaufLD") +
			// ")");
			// txtNbreLocuteursDominants3PremiersMois.setText(String.valueOf(event.getNewNbreLocuteursDominants3PremiersMois())
			// + " ("
			// +
			// df.format(event.getNewPourcentLocuteursDominants3PremiersMois())
			// + locuteursListePanel.getString("txt_PourcentLocuteursDominants")
			// + ")");
			// txtDureeMoyenneMoisParticipationLocuteursDominants.setText(String.valueOf(df.format(event.getNewDureeMoyenneParticipationLocuteursDominants()))
			// + locuteursListePanel.getString("txt_MoisEcartType") + " = " +
			// df.format(event.getNewEcartTypeParticipationLocuteursDominants())
			// + ") "
			// + locuteursListePanel.getString("txt_Soit") + " " +
			// df.format(event.getNewPourcentParticipationLocuteursDominants())
			// + locuteursListePanel.getString("txt_PourcentDureeSuivi"));
			// // PDF
			// locuteursTxtToPdf += labLD.getText() + txtLD.getText() + "\n" +
			// labNbreMessagesLD.getText() +
			// txtNbreMessagesLocuteursDominants.getText() + "\n"
			// + labPourcentLDSaufPremier.getText() +
			// txtPourcentLDSaufPremier.getText() + "\n" +
			// labNbreLocuteursDominants3PremiersMois.getText()
			// + txtNbreLocuteursDominants3PremiersMois.getText() + "\n" +
			// labDureeMoyenneMoisParticipationLocuteursDominants.getText()
			// + txtDureeMoyenneMoisParticipationLocuteursDominants.getText() +
			// "\n";
			// } else {
			// labT1.setText("<html><br>" +
			// locuteursListePanel.getString("txt_LocuteursDominants") +
			// " &rsaquo;&rsaquo;&rsaquo; : " +
			// locuteursListePanel.getString("txt_AucunLocuteurDominant")
			// + "</br></html>");
			// txtLD.setText(locuteursListePanel.getString("txt_NonTrouveMaj"));
			// txtNbreMessagesLocuteursDominants.setText(locuteursListePanel.getString("txt_NonTrouveMaj"));
			// txtPourcentLDSaufPremier.setText(locuteursListePanel.getString("txt_NonTrouveMaj"));
			// txtNbreLocuteursDominants3PremiersMois.setText(locuteursListePanel.getString("txt_NonTrouveMaj"));
			// txtDureeMoyenneMoisParticipationLocuteursDominants.setText(locuteursListePanel.getString("txt_NonTrouveMaj"));
			// }
			// } else {
			// txtParamLD.setText(locuteursListePanel.getString("txt_NonCalculeMaj"));
			// txtNbreLocuteursDominants.setText(locuteursListePanel.getString("txt_NonCalculeMaj"));
			// txtNbrePL.setText(locuteursListePanel.getString("txt_NonCalculeMaj"));
			// labT1.setText("<html><br>" +
			// locuteursListePanel.getString("txt_LocuteursDominants") +
			// " &rsaquo;&rsaquo;&rsaquo; : " +
			// locuteursListePanel.getString("txt_NonCalculeMin")
			// + "</br></html>");
			// txtLD.setText(locuteursListePanel.getString("txt_NonCalculeMaj"));
			// txtNbreMessagesLocuteursDominants.setText(locuteursListePanel.getString("txt_NonCalculeMaj"));
			// txtPourcentLDSaufPremier.setText(locuteursListePanel.getString("txt_NonCalculeMaj"));
			// txtNbreLocuteursDominants3PremiersMois.setText(locuteursListePanel.getString("txt_NonCalculeMaj"));
			// txtDureeMoyenneMoisParticipationLocuteursDominants.setText(locuteursListePanel.getString("txt_NonCalculeMaj"));
			// }
			// // JTABLE
			// // TEST SUR fQuality POUR SAVOIR SI IL Y A DES LOCUTEURS VENANT
			// DE
			// // FORUM
			// fromForum = false;
			// for (LocuteurModel locuteur : setLocuteurs) {
			// Date fStatDateRegistered = locuteur.getfStatDateRegistrered();
			// //
			// System.out.println("LocuteursPanel - listeChanged : fQualiteLocuteur = "+fQualiteLocuteur);
			// if (fStatDateRegistered != null) {
			// fromForum = true;
			// break;
			// }
			// }
			// if (!fromForum)
			// tableLocuteurs = new JTable(new TabLocuteursModel(setLocuteurs,
			// locuteursListePanel));
			// else
			// tableLocuteurs = new JTable(new
			// TabLocuteursForumModel(setLocuteurs, locuteursListePanel));
			// // tableLocuteurs
			// // .addPropertyChangeListener(new PropertyChangeListener() {
			// //
			// // @Override
			// // public void propertyChange(PropertyChangeEvent evt) {
			// // if ("tableCellEditor".equals(evt.getPropertyName())) {
			// // if (tableLocuteurs.isEditing())
			// // System.out
			// // .println("TABLE : EDITON EN COURS");
			// // else
			// // System.out
			// // .println("TABLE : EDITON ACHEVEE");
			// // }
			// //
			// // }
			// // });
			// scrollTabLocuteurs.setViewportView(tableLocuteurs);
			// applyRenderer();
			// panelBas.add(scrollTabLocuteurs);
			// panel.add(panelFiltre);
			scrollPanel.setViewportView(panel);
			cl.show(cards, "PANEL_PLEIN");
		} else
			cl.show(cards, "PANEL_VIDE");

	}

	@Override
	public JPanel getTabConversationsPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConversationsTxtToPdf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getInfosListePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getFilsListePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getTabMessagesPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getTabLocuteursPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIdentifiantMessageToShow(String identifiantMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public JToolBar getToolBar() {
		// TODO Auto-generated method stub
		return null;
	}
}
