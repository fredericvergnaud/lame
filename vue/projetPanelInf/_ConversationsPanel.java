package vue.projetPanelInf;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import renderers.CustomPieChartBoxPanel;
import renderers.CustomStatBoxPanel;
import modeles.ConversationModel;
import modeles.LocuteurModel;
import modeles.evenements.ListeChangedEvent;
import modeles.tableaux.TabConversationsForumModel;
import modeles.tableaux.TabConversationsModel;
import controleurs.ListeController;
import controleurs.vuesabstraites.ListeView;

public class _ConversationsPanel extends ListeView implements ActionListener {

	private JScrollPane scrollTabConversations;
	private JLabel labT1, txtT1, labT2, txtT2, labT3, txtT3, labParamCalculListe, txtParamCalculListe, labNbreConversations, txtNbreConversations, labNbreMoyenLocuteursSujet,
			txtNbreMoyenLocuteursSujet, labNbreMoyenMessagesConversation, txtNbreMoyenMessagesConversation, labParamCalculSC1, txtParamCalculSC1, labParamCalculSC2, txtParamCalculSC2, labNbreSC,
			txtNbreSC, labNbreMessagesSC, txtNbreMessagesSC, labNbreMessagesLDSC, txtNbreMessagesLDSC, labNbreMessagesLocuteursDominantsSC, txtNbreMessagesLocuteursDominantsSC,
			labNbreMessagesLocuteursDominantsSaufPremierSC, txtNbreMessagesLocuteursDominantsSaufPremierSC, labNbreMessagesPLSC, txtNbreMessagesPLSC, labNbrePLSC, txtNbrePLSC,
			labNbreLocuteursDominantsLanceursSC, labNbreSCLancesLD, labNbreSCLancesLocuteursDominantsSaufPremier, labNbreSCLancesPL, labNbreLanceursSC, txtNbreLanceursSC,
			txtNbreLocuteursDominantsLanceursSC, txtNbreSCLancesLD, txtNbreSCLancesLocuteursDominantsSaufPremier, txtNbreSCLancesPL;
	private JPanel panelVide, cards, panelBas, panel, panelFiltre;
	private DecimalFormat df = new DecimalFormat("0.00");
	private JTable tableConversations;
	private GridBagConstraints cHaut;
	private Set<ConversationModel> setConversations;
	private JTextField filterText;
	private TableRowSorter<TabConversationsModel> sorterConversations;
	private TableRowSorter<TabConversationsForumModel> sorterConversationsForum;
	private JComboBox typesFiltre;
	private ResourceBundle conversationsListe;
	private String conversationsTxtToPdf;
	private boolean fromForum;
	private Set<LocuteurModel> setLocuteurs;
	private CustomStatBoxPanel box1, box2;
	private CardLayout cl;
	private CustomPieChartBoxPanel box3, box4, box5, box6, box7, box8, box9, box11;

	public _ConversationsPanel(ListeController listeController, ResourceBundle conversationsListe) {
		super(listeController);
		this.conversationsListe = conversationsListe;
	}

	public JPanel getPanel() {
		panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setOpaque(true);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setPreferredSize(new Dimension(1300, 1150));
		panel.setMinimumSize(new Dimension(1300, 1150));
		panel.setMaximumSize(new Dimension(1300, 1150));
		panel.setBorder(new TitledBorder(""));
		panel.setName("PANEL_PLEIN");
		GridBagConstraints gbc = new GridBagConstraints();

		// BOXES
		JPanel panel1 = new JPanel(new GridBagLayout());
		panel1.setBorder(new TitledBorder(""));
		panel1.setAlignmentY(Component.TOP_ALIGNMENT);
		panel1.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel1.setPreferredSize(new Dimension(1300, 130));
		panel1.setMinimumSize(new Dimension(1300, 130));
		panel1.setMaximumSize(new Dimension(1300, 130));
		GridBagConstraints gbc1 = new GridBagConstraints();

		// PARAM SC 1
		box1 = new CustomStatBoxPanel("<html><center>" + conversationsListe.getString("txt_ParamCalculSC1") + "</center></html>", null);

		gbc1.gridx = 0;
		gbc1.gridy = 0;
		gbc1.weightx = 1.0;
		gbc1.anchor = GridBagConstraints.NORTHWEST;
		panel1.add(box1, gbc1);

		box2 = new CustomStatBoxPanel("<html><center>" + conversationsListe.getString("txt_ParamCalculSC2") + "</center></html>", null);

		gbc1.gridx = 1;
		gbc1.gridy = 0;
		gbc1.weightx = 10.0;
		gbc1.anchor = GridBagConstraints.NORTHWEST;
		panel1.add(box2, gbc1);

		// PANEL GRAPHS 1

		JPanel panel2 = new JPanel(new GridBagLayout());
		panel2.setBorder(new TitledBorder(""));
		panel2.setAlignmentY(Component.TOP_ALIGNMENT);
		panel2.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel2.setPreferredSize(new Dimension(1300, 280));
		panel2.setMinimumSize(new Dimension(1300, 280));
		panel2.setMaximumSize(new Dimension(1300, 280));
		GridBagConstraints gbc2 = new GridBagConstraints();

		// GRAPH 1 : Répartition scs / non scs

		box3 = new CustomPieChartBoxPanel("<html><center>Répartition des conversations</center></html>", conversationsListe);

		gbc2.gridx = 0;
		gbc2.gridy = 0;
		gbc2.weightx = 1.0;
		gbc2.anchor = GridBagConstraints.NORTHWEST;
		panel2.add(box3, gbc2);

		// GRAPH 2 : Répartition messages scs / non scs

		box4 = new CustomPieChartBoxPanel("<html><center>Répartition des messages</center></html>", conversationsListe);

		gbc2.gridx = 1;
		gbc2.gridy = 0;
		gbc2.weightx = 1.0;
		gbc2.anchor = GridBagConstraints.NORTHWEST;
		panel2.add(box4, gbc2);

		// GRAPH 3 : Répartition lanceurs scs / non lanceurs scs

		box5 = new CustomPieChartBoxPanel("<html><center>Répartition des locuteurs</center></html>", conversationsListe);

		gbc2.gridx = 2;
		gbc2.gridy = 0;
		gbc2.weightx = 10.0;
		gbc2.anchor = GridBagConstraints.NORTHWEST;
		panel2.add(box5, gbc2);

		// PANEL GRAPHS 2

		JPanel panel3 = new JPanel(new GridBagLayout());
		panel3.setBorder(new TitledBorder(""));
		panel3.setAlignmentY(Component.TOP_ALIGNMENT);
		panel3.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel3.setPreferredSize(new Dimension(1300, 280));
		panel3.setMinimumSize(new Dimension(1300, 280));
		panel3.setMaximumSize(new Dimension(1300, 280));
		GridBagConstraints gbc3 = new GridBagConstraints();

		// int largeurLabel = 600;
		// int hauteurLabel = 15;
		// int largeurTxtLabel = 650;
		// int hauteurTxtLabel = 15;
		// Dimension dimLabel = new Dimension(largeurLabel, hauteurLabel);
		// Dimension dimLabelTxt = new Dimension(largeurTxtLabel,
		// hauteurTxtLabel);
		// Font gras = new Font("sansserif", Font.BOLD, 12);
		// Font normal = new Font("sansserif", Font.PLAIN, 12);
		//
		// GridBagLayout lHaut = new GridBagLayout();
		// JPanel panelHaut = new JPanel();
		// panelHaut.setPreferredSize(new Dimension(1250, 366));
		// panelHaut.setMinimumSize(new Dimension(1250, 366));
		// panelHaut.setMaximumSize(new Dimension(1250, 366));
		// panelHaut.setLayout(lHaut);
		// cHaut = new GridBagConstraints();
		// panelHaut.setAlignmentY(Component.LEFT_ALIGNMENT);
		//
		// JPanel panelGauche = new JPanel();
		// panelGauche.setLayout(new BoxLayout(panelGauche, BoxLayout.Y_AXIS));
		// panelGauche.setPreferredSize(new Dimension(1250, 366));
		// panelGauche.setMinimumSize(new Dimension(1250, 366));
		// panelGauche.setMaximumSize(new Dimension(1250, 366));
		// panelGauche.setAlignmentY(Component.LEFT_ALIGNMENT);
		//
		// GridBagLayout l1 = new GridBagLayout();
		// JPanel panel1 = new JPanel();
		// panel1.setLayout(l1);
		// GridBagConstraints c1 = new GridBagConstraints();
		//
		// labParamCalculListe = new JLabel("", SwingConstants.LEFT);
		// labParamCalculListe.setPreferredSize(dimLabel);
		// labParamCalculListe.setFont(normal);
		// txtParamCalculListe = new JLabel("", SwingConstants.LEFT);
		// txtParamCalculListe.setPreferredSize(dimLabelTxt);
		// txtParamCalculListe.setFont(gras);
		//
		// c1.gridx = 0;
		// c1.gridy = 0;
		// panel1.add(labParamCalculListe, c1);
		// c1.gridx = 1;
		// c1.gridy = 0;
		// panel1.add(txtParamCalculListe, c1);
		//
		// panelGauche.add(panel1);
		//
		// GridBagLayout l2 = new GridBagLayout();
		// JPanel panel2 = new JPanel();
		// panel2.setLayout(l2);
		// GridBagConstraints c2 = new GridBagConstraints();
		//
		// labNbreConversations = new JLabel("", SwingConstants.LEFT);
		// labNbreConversations.setPreferredSize(dimLabel);
		// labNbreConversations.setFont(normal);
		// txtNbreConversations = new JLabel("", SwingConstants.LEFT);
		// txtNbreConversations.setPreferredSize(dimLabelTxt);
		// txtNbreConversations.setFont(gras);
		//
		// c2.gridx = 0;
		// c2.gridy = 0;
		// panel2.add(labNbreConversations, c2);
		// c2.gridx = 1;
		// c2.gridy = 0;
		// panel2.add(txtNbreConversations, c2);
		//
		// panelGauche.add(panel2);
		//
		// GridBagLayout l18 = new GridBagLayout();
		// JPanel panel18 = new JPanel();
		// panel18.setLayout(l18);
		// GridBagConstraints c18 = new GridBagConstraints();
		//
		// labNbreMoyenLocuteursSujet = new JLabel("", SwingConstants.LEFT);
		// labNbreMoyenLocuteursSujet.setPreferredSize(dimLabel);
		// labNbreMoyenLocuteursSujet.setFont(normal);
		// txtNbreMoyenLocuteursSujet = new JLabel("", SwingConstants.LEFT);
		// txtNbreMoyenLocuteursSujet.setPreferredSize(dimLabelTxt);
		// txtNbreMoyenLocuteursSujet.setFont(gras);
		//
		// c18.gridx = 0;
		// c18.gridy = 0;
		// panel18.add(labNbreMoyenLocuteursSujet, c18);
		// c18.gridx = 1;
		// c18.gridy = 0;
		// panel18.add(txtNbreMoyenLocuteursSujet, c18);
		//
		// panelGauche.add(panel18);
		//
		// GridBagLayout l19 = new GridBagLayout();
		// JPanel panel19 = new JPanel();
		// panel19.setLayout(l19);
		// GridBagConstraints c19 = new GridBagConstraints();
		//
		// labNbreMoyenMessagesConversation = new JLabel("",
		// SwingConstants.LEFT);
		// labNbreMoyenMessagesConversation.setPreferredSize(dimLabel);
		// labNbreMoyenMessagesConversation.setFont(normal);
		// txtNbreMoyenMessagesConversation = new JLabel("",
		// SwingConstants.LEFT);
		// txtNbreMoyenMessagesConversation.setPreferredSize(dimLabelTxt);
		// txtNbreMoyenMessagesConversation.setFont(gras);
		//
		// c19.gridx = 0;
		// c19.gridy = 0;
		// panel19.add(labNbreMoyenMessagesConversation, c19);
		// c19.gridx = 1;
		// c19.gridy = 0;
		// panel19.add(txtNbreMoyenMessagesConversation, c19);
		//
		// panelGauche.add(panel19);
		//
		// // SC
		// GridBagLayout lT1 = new GridBagLayout();
		// JPanel panelT1 = new JPanel();
		// panelT1.setLayout(lT1);
		// GridBagConstraints cT1 = new GridBagConstraints();
		//
		// labT1 = new JLabel("", SwingConstants.LEFT);
		// labT1.setPreferredSize(new Dimension(largeurLabel, 30));
		// labT1.setFont(gras);
		// labT1.setForeground(new Color(159, 150, 244));
		// txtT1 = new JLabel("", SwingConstants.LEFT);
		// txtT1.setPreferredSize(new Dimension(largeurTxtLabel, 30));
		// txtT1.setFont(gras);
		//
		// cT1.gridx = 0;
		// cT1.gridy = 0;
		// panelT1.add(labT1, cT1);
		// cT1.gridx = 1;
		// cT1.gridy = 0;
		// panelT1.add(txtT1, cT1);
		//
		// panelGauche.add(panelT1);
		//
		// GridBagLayout l10 = new GridBagLayout();
		// JPanel panel10 = new JPanel();
		// panel10.setLayout(l10);
		// GridBagConstraints c10 = new GridBagConstraints();
		//
		// labParamCalculSC1 = new JLabel("", SwingConstants.LEFT);
		// labParamCalculSC1.setPreferredSize(dimLabel);
		// labParamCalculSC1.setFont(normal);
		// txtParamCalculSC1 = new JLabel("", SwingConstants.LEFT);
		// txtParamCalculSC1.setPreferredSize(dimLabelTxt);
		// txtParamCalculSC1.setFont(gras);
		//
		// c10.gridx = 0;
		// c10.gridy = 0;
		// panel10.add(labParamCalculSC1, c10);
		// c10.gridx = 10;
		// c10.gridy = 0;
		// panel10.add(txtParamCalculSC1, c10);
		//
		// panelGauche.add(panel10);
		//
		// GridBagLayout l11 = new GridBagLayout();
		// JPanel panel11 = new JPanel();
		// panel11.setLayout(l11);
		// GridBagConstraints c11 = new GridBagConstraints();
		//
		// labParamCalculSC2 = new JLabel("", SwingConstants.LEFT);
		// labParamCalculSC2.setPreferredSize(dimLabel);
		// labParamCalculSC2.setFont(normal);
		// txtParamCalculSC2 = new JLabel("", SwingConstants.LEFT);
		// txtParamCalculSC2.setPreferredSize(dimLabelTxt);
		// txtParamCalculSC2.setFont(gras);
		//
		// c11.gridx = 0;
		// c11.gridy = 0;
		// panel11.add(labParamCalculSC2, c11);
		// c11.gridx = 1;
		// c11.gridy = 0;
		// panel11.add(txtParamCalculSC2, c11);
		//
		// panelGauche.add(panel11);
		//
		// GridBagLayout l3 = new GridBagLayout();
		// JPanel panel3 = new JPanel();
		// panel3.setLayout(l3);
		// GridBagConstraints c3 = new GridBagConstraints();
		//
		// labNbreSC = new JLabel("", SwingConstants.LEFT);
		// labNbreSC.setPreferredSize(dimLabel);
		// labNbreSC.setFont(normal);
		// txtNbreSC = new JLabel("", SwingConstants.LEFT);
		// txtNbreSC.setPreferredSize(dimLabelTxt);
		// txtNbreSC.setFont(gras);
		//
		// c3.gridx = 0;
		// c3.gridy = 0;
		// panel3.add(labNbreSC, c3);
		// c3.gridx = 1;
		// c3.gridy = 0;
		// panel3.add(txtNbreSC, c3);
		//
		// panelGauche.add(panel3);
		//
		// GridBagLayout l4 = new GridBagLayout();
		// JPanel panel4 = new JPanel();
		// panel4.setLayout(l4);
		// GridBagConstraints c4 = new GridBagConstraints();
		//
		// labNbreMessagesSC = new JLabel("", SwingConstants.LEFT);
		// labNbreMessagesSC.setPreferredSize(dimLabel);
		// labNbreMessagesSC.setFont(normal);
		// txtNbreMessagesSC = new JLabel("", SwingConstants.LEFT);
		// txtNbreMessagesSC.setPreferredSize(dimLabelTxt);
		// txtNbreMessagesSC.setFont(gras);
		//
		// c4.gridx = 0;
		// c4.gridy = 0;
		// panel4.add(labNbreMessagesSC, c4);
		// c4.gridx = 1;
		// c4.gridy = 0;
		// panel4.add(txtNbreMessagesSC, c4);
		//
		// panelGauche.add(panel4);
		//
		// GridBagLayout l9 = new GridBagLayout();
		// JPanel panel9 = new JPanel();
		// panel9.setLayout(l9);
		// GridBagConstraints c9 = new GridBagConstraints();
		//
		// labNbreLanceursSC = new JLabel("", SwingConstants.LEFT);
		// labNbreLanceursSC.setPreferredSize(dimLabel);
		// labNbreLanceursSC.setFont(normal);
		// txtNbreLanceursSC = new JLabel("", SwingConstants.LEFT);
		// txtNbreLanceursSC.setPreferredSize(dimLabelTxt);
		// txtNbreLanceursSC.setFont(gras);
		//
		// c9.gridx = 0;
		// c9.gridy = 0;
		// panel9.add(labNbreLanceursSC, c9);
		// c9.gridx = 1;
		// c9.gridy = 0;
		// panel9.add(txtNbreLanceursSC, c9);
		//
		// panelGauche.add(panel9);
		//
		// GridBagLayout lT3 = new GridBagLayout();
		// JPanel panelT3 = new JPanel();
		// panelT3.setLayout(lT3);
		// GridBagConstraints cT3 = new GridBagConstraints();
		//
		// // PL DANS SC
		// labT3 = new JLabel("", SwingConstants.LEFT);
		// labT3.setPreferredSize(new Dimension(largeurLabel, 30));
		// labT3.setFont(gras);
		// labT3.setForeground(new Color(0, 116, 0));
		// txtT3 = new JLabel("", SwingConstants.LEFT);
		// txtT3.setPreferredSize(new Dimension(largeurTxtLabel, 30));
		// txtT3.setFont(gras);
		//
		// cT3.gridx = 0;
		// cT3.gridy = 0;
		// panelT3.add(labT3, cT3);
		// cT3.gridx = 1;
		// cT3.gridy = 0;
		// panelT3.add(txtT3, cT3);
		//
		// panelGauche.add(panelT3);
		//
		// GridBagLayout l16 = new GridBagLayout();
		// JPanel panel16 = new JPanel();
		// panel16.setLayout(l16);
		// GridBagConstraints c16 = new GridBagConstraints();
		//
		// labNbrePLSC = new JLabel("", SwingConstants.LEFT);
		// labNbrePLSC.setPreferredSize(dimLabel);
		// labNbrePLSC.setFont(normal);
		// txtNbrePLSC = new JLabel("", SwingConstants.LEFT);
		// txtNbrePLSC.setPreferredSize(dimLabelTxt);
		// txtNbrePLSC.setFont(gras);
		//
		// c16.gridx = 0;
		// c16.gridy = 0;
		// panel16.add(labNbrePLSC, c16);
		// c16.gridx = 1;
		// c16.gridy = 0;
		// panel16.add(txtNbrePLSC, c16);
		//
		// panelGauche.add(panel16);
		//
		// GridBagLayout l8 = new GridBagLayout();
		// JPanel panel8 = new JPanel();
		// panel8.setLayout(l8);
		// GridBagConstraints c8 = new GridBagConstraints();
		//
		// labNbreMessagesPLSC = new JLabel("", SwingConstants.LEFT);
		// labNbreMessagesPLSC.setPreferredSize(dimLabel);
		// labNbreMessagesPLSC.setFont(normal);
		// txtNbreMessagesPLSC = new JLabel("", SwingConstants.LEFT);
		// txtNbreMessagesPLSC.setPreferredSize(dimLabelTxt);
		// txtNbreMessagesPLSC.setFont(gras);
		//
		// c8.gridx = 0;
		// c8.gridy = 0;
		// panel8.add(labNbreMessagesPLSC, c8);
		// c8.gridx = 1;
		// c8.gridy = 0;
		// panel8.add(txtNbreMessagesPLSC, c8);
		//
		// panelGauche.add(panel8);
		//
		// GridBagLayout l13 = new GridBagLayout();
		// JPanel panel13 = new JPanel();
		// panel13.setLayout(l13);
		// GridBagConstraints c13 = new GridBagConstraints();
		//
		// labNbreSCLancesPL = new JLabel("", SwingConstants.LEFT);
		// labNbreSCLancesPL.setPreferredSize(dimLabel);
		// labNbreSCLancesPL.setFont(normal);
		// txtNbreSCLancesPL = new JLabel("", SwingConstants.LEFT);
		// txtNbreSCLancesPL.setPreferredSize(dimLabelTxt);
		// txtNbreSCLancesPL.setFont(gras);
		//
		// c13.gridx = 0;
		// c13.gridy = 0;
		// panel13.add(labNbreSCLancesPL, c13);
		// c13.gridx = 1;
		// c13.gridy = 0;
		// panel13.add(txtNbreSCLancesPL, c13);
		//
		// panelGauche.add(panel13);
		//
		// // LD DANS SC
		// GridBagLayout lT2 = new GridBagLayout();
		// JPanel panelT2 = new JPanel();
		// panelT2.setLayout(lT2);
		// GridBagConstraints cT2 = new GridBagConstraints();
		//
		// labT2 = new JLabel("", SwingConstants.LEFT);
		// labT2.setPreferredSize(new Dimension(largeurLabel, 30));
		// labT2.setFont(gras);
		// labT2.setForeground(new Color(164, 0, 0));
		// txtT2 = new JLabel("", SwingConstants.LEFT);
		// txtT2.setPreferredSize(new Dimension(largeurTxtLabel, 30));
		// txtT2.setFont(gras);
		//
		// cT2.gridx = 0;
		// cT2.gridy = 0;
		// panelT2.add(labT2, cT2);
		// cT2.gridx = 1;
		// cT2.gridy = 0;
		// panelT2.add(txtT2, cT2);
		//
		// panelGauche.add(panelT2);
		//
		// // // LD
		// GridBagLayout l5 = new GridBagLayout();
		// JPanel panel5 = new JPanel();
		// panel5.setLayout(l5);
		// GridBagConstraints c5 = new GridBagConstraints();
		//
		// labNbreMessagesLDSC = new JLabel("", SwingConstants.LEFT);
		// labNbreMessagesLDSC.setPreferredSize(dimLabel);
		// labNbreMessagesLDSC.setFont(normal);
		// txtNbreMessagesLDSC = new JLabel("", SwingConstants.LEFT);
		// txtNbreMessagesLDSC.setPreferredSize(dimLabelTxt);
		// txtNbreMessagesLDSC.setFont(gras);
		//
		// c5.gridx = 0;
		// c5.gridy = 0;
		// panel5.add(labNbreMessagesLDSC, c5);
		// c5.gridx = 1;
		// c5.gridy = 0;
		// panel5.add(txtNbreMessagesLDSC, c5);
		//
		// panelGauche.add(panel5);
		//
		// GridBagLayout l14 = new GridBagLayout();
		// JPanel panel14 = new JPanel();
		// panel14.setLayout(l14);
		// GridBagConstraints c14 = new GridBagConstraints();
		//
		// labNbreSCLancesLD = new JLabel("", SwingConstants.LEFT);
		// labNbreSCLancesLD.setPreferredSize(dimLabel);
		// labNbreSCLancesLD.setFont(normal);
		// txtNbreSCLancesLD = new JLabel("", SwingConstants.LEFT);
		// txtNbreSCLancesLD.setPreferredSize(dimLabelTxt);
		// txtNbreSCLancesLD.setFont(gras);
		//
		// c14.gridx = 0;
		// c14.gridy = 0;
		// panel14.add(labNbreSCLancesLD, c14);
		// c14.gridx = 1;
		// c14.gridy = 0;
		// panel14.add(txtNbreSCLancesLD, c14);
		//
		// panelGauche.add(panel14);
		//
		// // // Locuteurs dominants
		//
		// GridBagLayout l6 = new GridBagLayout();
		// JPanel panel6 = new JPanel();
		// panel6.setLayout(l6);
		// GridBagConstraints c6 = new GridBagConstraints();
		//
		// labNbreMessagesLocuteursDominantsSC = new JLabel("",
		// SwingConstants.LEFT);
		// labNbreMessagesLocuteursDominantsSC.setPreferredSize(dimLabel);
		// labNbreMessagesLocuteursDominantsSC.setFont(normal);
		// txtNbreMessagesLocuteursDominantsSC = new JLabel("",
		// SwingConstants.LEFT);
		// txtNbreMessagesLocuteursDominantsSC.setPreferredSize(dimLabelTxt);
		// txtNbreMessagesLocuteursDominantsSC.setFont(gras);
		//
		// c6.gridx = 0;
		// c6.gridy = 0;
		// panel6.add(labNbreMessagesLocuteursDominantsSC, c6);
		// c6.gridx = 1;
		// c6.gridy = 0;
		// panel6.add(txtNbreMessagesLocuteursDominantsSC, c6);
		//
		// panelGauche.add(panel6);
		//
		// GridBagLayout l7 = new GridBagLayout();
		// JPanel panel7 = new JPanel();
		// panel7.setLayout(l7);
		// GridBagConstraints c7 = new GridBagConstraints();
		//
		// labNbreMessagesLocuteursDominantsSaufPremierSC = new JLabel("",
		// SwingConstants.LEFT);
		// labNbreMessagesLocuteursDominantsSaufPremierSC.setPreferredSize(dimLabel);
		// labNbreMessagesLocuteursDominantsSaufPremierSC.setFont(normal);
		// txtNbreMessagesLocuteursDominantsSaufPremierSC = new JLabel("",
		// SwingConstants.LEFT);
		// txtNbreMessagesLocuteursDominantsSaufPremierSC.setPreferredSize(dimLabelTxt);
		// txtNbreMessagesLocuteursDominantsSaufPremierSC.setFont(gras);
		//
		// c7.gridx = 0;
		// c7.gridy = 0;
		// panel7.add(labNbreMessagesLocuteursDominantsSaufPremierSC, c7);
		// c7.gridx = 1;
		// c7.gridy = 0;
		// panel7.add(txtNbreMessagesLocuteursDominantsSaufPremierSC, c7);
		//
		// panelGauche.add(panel7);
		//
		// GridBagLayout l12 = new GridBagLayout();
		// JPanel panel12 = new JPanel();
		// panel12.setLayout(l12);
		// GridBagConstraints c12 = new GridBagConstraints();
		//
		// labNbreLocuteursDominantsLanceursSC = new JLabel("",
		// SwingConstants.LEFT);
		// labNbreLocuteursDominantsLanceursSC.setPreferredSize(dimLabel);
		// labNbreLocuteursDominantsLanceursSC.setFont(normal);
		// txtNbreLocuteursDominantsLanceursSC = new JLabel("",
		// SwingConstants.LEFT);
		// txtNbreLocuteursDominantsLanceursSC.setPreferredSize(dimLabelTxt);
		// txtNbreLocuteursDominantsLanceursSC.setFont(gras);
		//
		// c12.gridx = 0;
		// c12.gridy = 0;
		// panel12.add(labNbreLocuteursDominantsLanceursSC, c12);
		// c12.gridx = 1;
		// c12.gridy = 0;
		// panel12.add(txtNbreLocuteursDominantsLanceursSC, c12);
		//
		// panelGauche.add(panel12);
		//
		// GridBagLayout l15 = new GridBagLayout();
		// JPanel panel15 = new JPanel();
		// panel15.setLayout(l15);
		// GridBagConstraints c15 = new GridBagConstraints();
		//
		// labNbreSCLancesLocuteursDominantsSaufPremier = new JLabel("",
		// SwingConstants.LEFT);
		// labNbreSCLancesLocuteursDominantsSaufPremier.setPreferredSize(dimLabel);
		// labNbreSCLancesLocuteursDominantsSaufPremier.setFont(normal);
		// txtNbreSCLancesLocuteursDominantsSaufPremier = new JLabel("",
		// SwingConstants.LEFT);
		// txtNbreSCLancesLocuteursDominantsSaufPremier.setPreferredSize(dimLabelTxt);
		// txtNbreSCLancesLocuteursDominantsSaufPremier.setFont(gras);
		//
		// c15.gridx = 0;
		// c15.gridy = 0;
		// panel15.add(labNbreSCLancesLocuteursDominantsSaufPremier, c15);
		// c15.gridx = 1;
		// c15.gridy = 0;
		// panel15.add(txtNbreSCLancesLocuteursDominantsSaufPremier, c15);
		//
		// panelGauche.add(panel15);
		//
		// cHaut.gridx = 0;
		// cHaut.gridy = 0;
		// panelHaut.add(panelGauche, cHaut);
		//
		// // JTABLE
		// tableConversations = new JTable(new TabConversationsModel(new
		// HashSet<ConversationModel>(), conversationsListe));
		// scrollTabConversations = new JScrollPane(tableConversations);
		//
		// panelBas = new JPanel();
		// panelBas.setLayout(new BoxLayout(panelBas, BoxLayout.Y_AXIS));
		// panelBas.setPreferredSize(new Dimension(1250, 805));
		// panelBas.setAlignmentY(Component.LEFT_ALIGNMENT);
		//
		// panel.add(panelHaut);
		// panel.add(panelBas);
		//
		// panelFiltre = new JPanel();
		// panelFiltre.setLayout(new BoxLayout(panelFiltre, BoxLayout.X_AXIS));
		// panelFiltre.setAlignmentY(Component.RIGHT_ALIGNMENT);
		//
		// JLabel labFiltre = new
		// JLabel(conversationsListe.getString("txt_Filtre") + " : ",
		// SwingConstants.LEFT);
		// panelFiltre.add(labFiltre);
		//
		// String[] filterTypes = {
		// conversationsListe.getString("txt_FiltreDuree"),
		// conversationsListe.getString("txt_FiltreNbreMessages"),
		// conversationsListe.getString("txt_FiltreNbreLocuteurs"),
		// conversationsListe.getString("txt_FiltreLanceur") };
		// typesFiltre = new JComboBox(filterTypes);
		// typesFiltre.setSelectedIndex(1);
		// typesFiltre.addActionListener(this);
		// panelFiltre.add(typesFiltre);
		//
		// filterText = new JTextField();
		// filterText.getDocument().addDocumentListener(new DocumentListener() {
		// @Override
		// public void changedUpdate(DocumentEvent e) {
		// if (!fromForum)
		// newFilterConversations();
		// else
		// newFilterConversationsForum();
		// }
		//
		// @Override
		// public void insertUpdate(DocumentEvent e) {
		// if (!fromForum)
		// newFilterConversations();
		// else
		// newFilterConversationsForum();
		// }
		//
		// @Override
		// public void removeUpdate(DocumentEvent e) {
		// if (!fromForum)
		// newFilterConversations();
		// else
		// newFilterConversationsForum();
		// }
		// });
		// labFiltre.setLabelFor(filterText);
		// panelFiltre.add(filterText);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(panel1, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(panel2, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1.0;
		gbc.weighty = 7.0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(panel3, gbc);

		// CardLayout
		panelVide = new JPanel();
		panelVide.setName("PANEL_VIDE");
		panelVide.setBorder(new TitledBorder(conversationsListe.getString("txt_Conversations")));
		cards = new JPanel(new CardLayout());
		cards.add(panel, panel.getName());
		cards.add(panelVide, panelVide.getName());
		cl = (CardLayout) (cards.getLayout());

		return cards;
	}

	public JPanel getConversationsPanel() {
		return getPanel();
	}

	@Override
	public String getConversationsTxtToPdf() {
		return conversationsTxtToPdf;
	}

	public void applyRenderer() {
		// GENERAL
		tableConversations.setRowSelectionAllowed(true);
		tableConversations.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableConversations.setFillsViewportHeight(true);
		tableConversations.setAutoCreateRowSorter(true);

//		tableConversations.setDefaultRenderer(Integer.class, new SujetsCollectifsRenderer(setConversations));
//		tableConversations.setDefaultRenderer(Date.class, new SujetsCollectifsRenderer(setConversations));
//		tableConversations.setDefaultRenderer(String.class, new SujetsCollectifsRenderer(setConversations));
//		tableConversations.setDefaultRenderer(Boolean.class, new SujetsCollectifsRenderer(setConversations));

		// HEADERS
		// // Hauteur
		tableConversations.getTableHeader().setPreferredSize(new Dimension(tableConversations.getColumnModel().getTotalColumnWidth(), 50));
		// // Centrage
		((JLabel) tableConversations.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		((JLabel) tableConversations.getTableHeader().getDefaultRenderer()).setVerticalAlignment(SwingConstants.TOP);

		// SujetsCollectifsRenderer : StringRenderer + DateRenderer +
		// FloatRenderer +
		// IntegerRenderer

		if (!fromForum) {
			// Sorter
			sorterConversations = new TableRowSorter<TabConversationsModel>((TabConversationsModel) tableConversations.getModel());
			tableConversations.setRowSorter(sorterConversations);

			// LANCEURS
			TableColumn colLanceur = tableConversations.getColumnModel().getColumn(9);
//			colLanceur.setCellRenderer(new SujetsCollectifsLanceursRenderer(setLocuteurs));

			// LARGEUR DES COLONNES
			// Disable auto resizing
			// tableConversations.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			TableColumn colId = tableConversations.getColumnModel().getColumn(0);
			TableColumn colSujetMessage1 = tableConversations.getColumnModel().getColumn(1);
			TableColumn colNumeroMessage1 = tableConversations.getColumnModel().getColumn(2);
			TableColumn colDebut = tableConversations.getColumnModel().getColumn(3);
			TableColumn colFin = tableConversations.getColumnModel().getColumn(4);
			TableColumn colDuree = tableConversations.getColumnModel().getColumn(5);
			TableColumn colNbreMessages = tableConversations.getColumnModel().getColumn(6);
			TableColumn colNbreLoc = tableConversations.getColumnModel().getColumn(7);

			int widthColId = 100;
			int widthColSujetMessage1 = 450;
			int widthColNumeroMessage1 = 100;
			int widthColDebut = 100;
			int widthColFin = 100;
			int widthColDuree = 50;
			int widthColNbreMessages = 100;
			int widthColNbreLoc = 80;
			int widthColLanceur = 280;

			colId.setPreferredWidth(widthColId);
			colSujetMessage1.setPreferredWidth(widthColSujetMessage1);
			colNumeroMessage1.setPreferredWidth(widthColNumeroMessage1);
			colDebut.setPreferredWidth(widthColDebut);
			colFin.setPreferredWidth(widthColFin);
			colDuree.setPreferredWidth(widthColDuree);
			colNbreMessages.setPreferredWidth(widthColNbreMessages);
			colNbreLoc.setPreferredWidth(widthColNbreLoc);
			colLanceur.setPreferredWidth(widthColLanceur);
		} else {
			// Sorter
			sorterConversationsForum = new TableRowSorter<TabConversationsForumModel>((TabConversationsForumModel) tableConversations.getModel());
			tableConversations.setRowSorter(sorterConversationsForum);

			// LANCEURS
			TableColumn colLanceur = tableConversations.getColumnModel().getColumn(9);
			// colLanceur.setCellRenderer(new SujetsCollectifsLanceursRenderer(
			// setLocuteurs));

			// LARGEUR DES COLONNES
			// Disable auto resizing
			// tableConversations.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

			TableColumn colId = tableConversations.getColumnModel().getColumn(0);
			TableColumn colTopic = tableConversations.getColumnModel().getColumn(1);
			TableColumn colNbreVues = tableConversations.getColumnModel().getColumn(2);
			TableColumn colDebut = tableConversations.getColumnModel().getColumn(3);
			TableColumn colFin = tableConversations.getColumnModel().getColumn(4);
			TableColumn colDuree = tableConversations.getColumnModel().getColumn(5);
			TableColumn colNbreMessages = tableConversations.getColumnModel().getColumn(6);
			TableColumn colNbreLoc = tableConversations.getColumnModel().getColumn(7);

			int widthColId = 100;
			int widthColTopic = 450;
			int widthColNbreVues = 100;
			int widthColDebut = 100;
			int widthColFin = 100;
			int widthColDuree = 50;
			int widthColNbreMessages = 100;
			int widthColNbreLoc = 80;
			int widthColLanceur = 280;

			colId.setPreferredWidth(widthColId);
			colTopic.setPreferredWidth(widthColTopic);
			colNbreVues.setPreferredWidth(widthColNbreVues);
			colDebut.setPreferredWidth(widthColDebut);
			colFin.setPreferredWidth(widthColFin);
			colDuree.setPreferredWidth(widthColDuree);
			colNbreMessages.setPreferredWidth(widthColNbreMessages);
			colNbreLoc.setPreferredWidth(widthColNbreLoc);
			colLanceur.setPreferredWidth(widthColLanceur);
		}
	}

	private void newFilterConversations() {
		RowFilter<TabConversationsModel, Object> rf = null;
		try {
			String typeFiltre = (String) typesFiltre.getSelectedItem();
			int numCol;
			if (typeFiltre.equals(conversationsListe.getString("txt_FiltreLanceur"))) {
				rf = RowFilter.regexFilter("(?i)" + filterText.getText(), 9);
			} else {
				if (typeFiltre.equals(conversationsListe.getString("txt_FiltreDuree")))
					numCol = 5;
				else if (typeFiltre.equals(conversationsListe.getString("txt_FiltreNbreMessages")))
					numCol = 6;
				else
					numCol = 7;
				if (!filterText.getText().equals(""))
					try {
						rf = RowFilter.numberFilter(ComparisonType.AFTER, Float.valueOf(filterText.getText()), numCol);
					} catch (NumberFormatException nfe) {
						System.out.println("Ce n'est pas un nombre");
					}
			}

		} catch (PatternSyntaxException e) {
			return;
		}
		sorterConversations.setRowFilter(rf);
	}

	private void newFilterConversationsForum() {
		RowFilter<TabConversationsForumModel, Object> rf = null;
		try {
			String typeFiltre = (String) typesFiltre.getSelectedItem();
			int numCol;
			if (typeFiltre.equals(conversationsListe.getString("txt_FiltreLanceur"))) {
				rf = RowFilter.regexFilter("(?i)" + filterText.getText(), 9);
			} else {
				if (typeFiltre.equals(conversationsListe.getString("txt_FiltreDuree")))
					numCol = 5;
				else if (typeFiltre.equals(conversationsListe.getString("txt_FiltreNbreMessages")))
					numCol = 6;
				else
					numCol = 7;
				if (!filterText.getText().equals(""))
					try {
						rf = RowFilter.numberFilter(ComparisonType.AFTER, Float.valueOf(filterText.getText()), numCol);
					} catch (NumberFormatException nfe) {
						System.out.println("Ce n'est pas un nombre");
					}
			}

		} catch (PatternSyntaxException e) {
			return;
		}
		sorterConversationsForum.setRowFilter(rf);
	}

	private void newFilterConversations(String typeFiltre) {
		RowFilter<TabConversationsModel, Object> rf = null;
		try {
			int numCol;
			if (typeFiltre.equals(conversationsListe.getString("txt_FiltreLanceur"))) {
				rf = RowFilter.regexFilter("(?i)" + filterText.getText(), 9);
			} else {
				if (typeFiltre.equals(conversationsListe.getString("txt_FiltreDuree")))
					numCol = 5;
				else if (typeFiltre.equals(conversationsListe.getString("txt_FiltreNbreMessages")))
					numCol = 6;
				else
					numCol = 7;
				if (!filterText.getText().equals(""))
					try {
						rf = RowFilter.numberFilter(ComparisonType.AFTER, Float.valueOf(filterText.getText()), numCol);
					} catch (NumberFormatException nfe) {
						System.out.println("Ce n'est pas un nombre");
					}
			}

		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		sorterConversations.setRowFilter(rf);
	}

	private void newFilterConversationsForum(String typeFiltre) {
		RowFilter<TabConversationsForumModel, Object> rf = null;
		try {
			int numCol;
			if (typeFiltre.equals(conversationsListe.getString("txt_FiltreLanceur"))) {
				rf = RowFilter.regexFilter("(?i)" + filterText.getText(), 9);
			} else {
				if (typeFiltre.equals(conversationsListe.getString("txt_FiltreDuree")))
					numCol = 5;
				else if (typeFiltre.equals(conversationsListe.getString("txt_FiltreNbreMessages")))
					numCol = 6;
				else
					numCol = 7;
				if (!filterText.getText().equals(""))
					try {
						rf = RowFilter.numberFilter(ComparisonType.AFTER, Float.valueOf(filterText.getText()), numCol);
					} catch (NumberFormatException nfe) {
						System.out.println("Ce n'est pas un nombre");
					}
			}

		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		sorterConversationsForum.setRowFilter(rf);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		String typeFiltre = (String) cb.getSelectedItem();
		if (!fromForum)
			newFilterConversations(typeFiltre);
		else
			newFilterConversationsForum(typeFiltre);

	}

	@Override
	public void listeChanged(ListeChangedEvent event) {
		Map<String, Double> mapData;
		if (event.getNewNbrePL() > 0) {
			// box1.getTxtLabel().setText(String.valueOf(event.getNewParamSujetsCollectifs()));
			// box2.getTxtLabel().setText(String.valueOf(event.getNewParamLocuteursSC()));
			//
			// // Data Graph répartition conversations entre SCs / non SCs
			// mapData = new HashMap<String, Double>();
			// if (event.getNewNbreSC() > 0)
			// mapData.put("Sujets collectifs", (double) event.getNewNbreSC());
			// if (event.getNewSetConversations().size() - event.getNewNbreSC()
			// > 0)
			// mapData.put("Autres conversations", (double)
			// event.getNewSetConversations().size() - event.getNewNbreSC());
			// box3.getGraphPanel().removeAll();
			// box3.createPieChart(mapData);
			//
			// // Data Graph répartition messages entre SCs / non SCs
			// mapData = new HashMap<String, Double>();
			// if (event.getNewNbreMessagesSC() > 0)
			// mapData.put("Sujets collectifs", (double)
			// event.getNewNbreMessagesSC());
			// if (event.getNewNbreMessages() - event.getNewNbreMessagesSC() >
			// 0)
			// mapData.put("Autres conversations", (double)
			// event.getNewNbreMessages() - event.getNewNbreMessagesSC());
			// box4.getGraphPanel().removeAll();
			// box4.createPieChart(mapData);
			//
			// // Data Graph répartition messages entre lanceurs SCs / non
			// lanceurs
			// // SCs
			// mapData = new HashMap<String, Double>();
			// if (event.getNewNbreLanceursSC() > 0)
			// mapData.put("Lanceurs de sujets collectifs", (double)
			// event.getNewNbreLanceursSC());
			// if (event.getNewNbreLocuteurs() - event.getNewNbreLanceursSC() >
			// 0)
			// mapData.put("Non lanceurs de sujets collectifs", (double)
			// event.getNewNbreLocuteurs() - event.getNewNbreLanceursSC());
			// box5.getGraphPanel().removeAll();
			// box5.createPieChart(mapData);

			// setConversations = event.getNewSetConversations();
			// setLocuteurs = event.getNewSetLocuteurs();
			//
			// // LABELS
			// labParamCalculListe.setText(conversationsListe.getString("txt_ParamRegroupement")
			// + " : ");
			// txtParamCalculListe.setText(event.getNewSParamConversations());
			// labNbreConversations.setText(conversationsListe.getString("txt_NbreConversations")
			// + " : ");
			// txtNbreConversations.setText(String.valueOf(setConversations.size()));
			// labNbreMoyenLocuteursSujet.setText(conversationsListe.getString("txt_NbreMoyenLocuteursSujet")
			// + " : ");
			// txtNbreMoyenLocuteursSujet.setText(df.format(event.getNewNbreMoyenLocuteursDifferentsSujet()));
			// labNbreMoyenMessagesConversation.setText(conversationsListe.getString("txt_NbreMoyenMessagesSujet")
			// + " : ");
			// txtNbreMoyenMessagesConversation.setText(df.format(event.getNewNbreMoyenMessagesConversation()));
			//
			// labParamCalculSC1.setText(conversationsListe.getString("txt_ParamCalculSC1")
			// + " : ");
			// labParamCalculSC2.setText(conversationsListe.getString("txt_ParamCalculSC2")
			// + " : ");
			// labNbreSC.setText(conversationsListe.getString("txt_NbreSC") +
			// " : ");
			// labNbreMessagesSC.setText(conversationsListe.getString("txt_NbreMessagesSC")
			// + " : ");
			// labNbreLanceursSC.setText(conversationsListe.getString("txt_NbreLanceursSC")
			// + " : ");
			// labNbrePLSC.setText(conversationsListe.getString("txt_NbrePLSC")
			// + " : ");
			// labNbreMessagesPLSC.setText(conversationsListe.getString("txt_NbreMessagesPLSC")
			// + " : ");
			// labNbreSCLancesPL.setText(conversationsListe.getString("txt_NbreSCLancesPL")
			// + " : ");
			// labNbreMessagesLDSC.setText(conversationsListe.getString("txt_NbreMessagesLDSC")
			// + " : ");
			// labNbreSCLancesLD.setText(conversationsListe.getString("txt_NbreSCLancesLD")
			// + " : ");
			// labNbreMessagesLocuteursDominantsSC.setText(conversationsListe.getString("txt_NbreMessagesLocuteursDominantsSC")
			// + " : ");
			// labNbreMessagesLocuteursDominantsSaufPremierSC.setText(conversationsListe.getString("txt_NbreMessagesLocuteursDominantsSaufPremierSC")
			// + " : ");
			// labNbreSCLancesLocuteursDominantsSaufPremier.setText(conversationsListe.getString("txt_NbreSCLancesLocuteursDominantsSaufPremier")
			// + " : ");
			// labNbreLocuteursDominantsLanceursSC.setText(conversationsListe.getString("txt_NbreLocuteursDominantsLanceursSC")
			// + " : ");
			//
			// // PDF
			// conversationsTxtToPdf = labParamCalculListe.getText() +
			// txtParamCalculListe.getText() + "\n" +
			// labNbreConversations.getText() + txtNbreConversations.getText() +
			// "\n"
			// + labNbreMoyenLocuteursSujet.getText() +
			// txtNbreMoyenLocuteursSujet.getText() + "\n" +
			// labNbreMoyenMessagesConversation.getText() +
			// txtNbreMoyenMessagesConversation.getText()
			// + "\n";
			//
			// if (event.getNewParamSujetsCollectifs() != 0) {
			// txtParamCalculSC1.setText(conversationsListe.getString("txt_NbreMessagesSuperieurA")
			// + " "
			// + (int) (event.getNewParamSujetsCollectifs() *
			// event.getNewNbreMoyenMessagesConversation() + 0.5) + " (" +
			// df.format(event.getNewParamSujetsCollectifs()) + " X "
			// + df.format(event.getNewNbreMoyenMessagesConversation()) + " (" +
			// conversationsListe.getString("txt_NbreMoyenMessagesConversation")
			// + "))");
			//
			// if (event.getNewParamLocuteursSC() == true) {
			// txtParamCalculSC2.setText(conversationsListe.getString("txt_NbreLocuteursSuperieurA")
			// + " "
			// + (int) (event.getNewParamSujetsCollectifs() *
			// event.getNewNbreMoyenLocuteursDifferentsSujet() + 0.5) + " (" +
			// df.format(event.getNewParamSujetsCollectifs()) + " X "
			// + df.format(event.getNewNbreMoyenLocuteursDifferentsSujet()) +
			// " (" +
			// conversationsListe.getString("txt_NbreMoyenLocuteursConversation")
			// + "))");
			// } else {
			// txtParamCalculSC2.setText(conversationsListe.getString("txt_NonPrisCompte"));
			// }
			// txtNbreSC.setText(String.valueOf(event.getNewNbreSC()) + " (" +
			// String.valueOf(df.format(event.getNewPourcentSC())) +
			// conversationsListe.getString("txt_PourcentSujets") + ")");
			//
			// if (event.getNewNbreSC() > 0) {
			// // LABELS SC
			// labT1.setText("<html><br>" +
			// conversationsListe.getString("txt_SujetsCollectifs") +
			// " &rsaquo;&rsaquo;&rsaquo;</br></html>");
			// txtNbreMessagesSC.setText(String.valueOf(event.getNewNbreMessagesSC())
			// + " (" +
			// String.valueOf(df.format(event.getNewPourcentMessagesSC()))
			// + conversationsListe.getString("txt_PourcentMessages") + ")");
			// txtNbreLanceursSC.setText(String.valueOf(event.getNewNbreLanceursSC())
			// + " (" +
			// String.valueOf(df.format(event.getNewPourcentLanceursSC()))
			// + conversationsListe.getString("txt_PourcentLocuteurs") + ")");
			//
			// // SC et PL
			// labT3.setText("<html><br>" +
			// conversationsListe.getString("txt_PLdansSC") +
			// " &rsaquo;&rsaquo;&rsaquo;</br></html>");
			// txtNbrePLSC.setText(String.valueOf(event.getNewNbrePLSC()) + " ("
			// + String.valueOf(df.format(event.getNewPourcentPLSC())) +
			// conversationsListe.getString("txt_PourcentPL") + ")");
			// txtNbreMessagesPLSC.setText(String.valueOf(event.getNewNbreMessagesPLSC())
			// + " (" +
			// String.valueOf(df.format(event.getNewPourcentMessagesPLSC()))
			// + conversationsListe.getString("txt_PourcentMessagesSC") + ")");
			// txtNbreSCLancesPL.setText(String.valueOf(event.getNewNbreSCLancesPL())
			// + " (" +
			// String.valueOf(df.format(event.getNewPourcentSCLancesPL()))
			// + conversationsListe.getString("txt_PourcentSC") + ")");
			// if (event.getNewNbreLocuteursDominants() > 0) {
			// // SC ET LOCUTEURS DOMINANTS
			// labT2.setText("<html><br>" +
			// conversationsListe.getString("txt_LocuteursDominantsdansSC") +
			// " &rsaquo;&rsaquo;&rsaquo;</br></html>");
			// // // ld
			// txtNbreMessagesLDSC.setText(String.valueOf(event.getNewNbreMessagesLDSC())
			// + " (" +
			// String.valueOf(df.format(event.getNewPourcentMessagesLDSC()))
			// + conversationsListe.getString("txt_PourcentMessagesSC") + ")");
			// txtNbreSCLancesLD.setText(String.valueOf(event.getNewNbreSCLancesLD())
			// + " (" +
			// String.valueOf(df.format(event.getNewPourcentSCLancesLD()))
			// + conversationsListe.getString("txt_PourcentSC") + ")");
			// // // locuteurs dominants
			// txtNbreMessagesLocuteursDominantsSC.setText(String.valueOf(event.getNewNbreMessagesLocuteursDominantsSC())
			// + " ("
			// +
			// String.valueOf(df.format(event.getNewPourcentMessagesLocuteursDominantsSC()))
			// + conversationsListe.getString("txt_PourcentMessagesSC") + ")");
			// txtNbreMessagesLocuteursDominantsSaufPremierSC.setText(String.valueOf(event.getNewNbreMessagesLocuteursDominantsSaufPremierSC())
			// + " ("
			// +
			// String.valueOf(df.format(event.getNewPourcentMessagesLocuteursDominantsSaufPremierSC()))
			// + conversationsListe.getString("txt_PourcentMessagesSC") + ")");
			// txtNbreLocuteursDominantsLanceursSC.setText(String.valueOf(event.getNewNbreLocuteursDominantsLanceursSC())
			// + " ("
			// +
			// String.valueOf(df.format(event.getNewPourcentLocuteursDominantsLanceursSC1()))
			// + conversationsListe.getString("txt_PourcentLanceurs") + " | "
			// +
			// String.valueOf(df.format(event.getNewPourcentLocuteursDominantsLanceursSC2()))
			// + conversationsListe.getString("txt_PourcentLocuteursDominants")
			// + "))");
			// txtNbreSCLancesLocuteursDominantsSaufPremier.setText(String.valueOf(event.getNewNbreSCLancesLocuteursDominantsSaufPremier())
			// + " ("
			// +
			// String.valueOf(df.format(event.getNewPourcentSCLancesLocuteursDominantsSaufPremier()))
			// + conversationsListe.getString("txt_PourcentSC") + ")");
			//
			// } else {
			// labT2.setText("<html><br>" +
			// conversationsListe.getString("txt_LocuteursDominantsdansSC") +
			// " &rsaquo;&rsaquo;&rsaquo; : " +
			// conversationsListe.getString("txt_NonTrouveMin")
			// + "</br></html>");
			// txtNbreMessagesLDSC.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreSCLancesLD.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreMessagesLocuteursDominantsSC.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreMessagesLocuteursDominantsSaufPremierSC.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreSCLancesLocuteursDominantsSaufPremier.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreLocuteursDominantsLanceursSC.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// }
			// } else {
			// labT1.setText("<html><br>" +
			// conversationsListe.getString("txt_SujetsCollectifs") +
			// " &rsaquo;&rsaquo;&rsaquo; : " +
			// conversationsListe.getString("txt_AucunSC") + "</br></html>");
			// txtNbreMessagesSC.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreLanceursSC.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// labT3.setText("<html><br>Petits locuteurs dans Sujets Collectifs &rsaquo;&rsaquo;&rsaquo; : "
			// + conversationsListe.getString("txt_NonTrouveMin") +
			// "</br></html>");
			// txtNbrePLSC.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreMessagesPLSC.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreSCLancesPL.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// labT2.setText("<html><br>Locuteurs dominants dans Sujets Collectifs &rsaquo;&rsaquo;&rsaquo; : "
			// + conversationsListe.getString("txt_NonTrouveMin") +
			// "</br></html>");
			// txtNbreMessagesLDSC.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreSCLancesLD.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreMessagesLocuteursDominantsSC.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreMessagesLocuteursDominantsSaufPremierSC.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreSCLancesLocuteursDominantsSaufPremier.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// txtNbreLocuteursDominantsLanceursSC.setText(conversationsListe.getString("txt_NonTrouveMaj"));
			// }
			// } else {
			// txtParamCalculSC1.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// txtParamCalculSC2.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// txtNbreSC.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// labT1.setText("<html><br>" +
			// conversationsListe.getString("txt_SujetsCollectifs") +
			// " &rsaquo;&rsaquo;&rsaquo; : " +
			// conversationsListe.getString("txt_NonCalculeMin") +
			// "</br></html>");
			// txtNbreMessagesSC.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// txtNbreLanceursSC.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// labT3.setText("<html><br>" +
			// conversationsListe.getString("txt_PLdansSC") +
			// " &rsaquo;&rsaquo;&rsaquo; : " +
			// conversationsListe.getString("txt_NonCalculeMin") +
			// "</br></html>");
			// txtNbrePLSC.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// txtNbreMessagesPLSC.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// txtNbreSCLancesPL.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// labT2.setText("<html><br>" +
			// conversationsListe.getString("txt_LocuteursDominantsdansSC") +
			// " &rsaquo;&rsaquo;&rsaquo; : " +
			// conversationsListe.getString("txt_NonCalculeMin")
			// + "</br></html>");
			// txtNbreMessagesLDSC.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// txtNbreSCLancesLD.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// txtNbreMessagesLocuteursDominantsSC.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// txtNbreMessagesLocuteursDominantsSaufPremierSC.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// txtNbreSCLancesLocuteursDominantsSaufPremier.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// txtNbreLocuteursDominantsLanceursSC.setText(conversationsListe.getString("txt_NonCalculeMaj"));
			// }
			//
			// // PDF
			// conversationsTxtToPdf += labParamCalculSC1.getText() +
			// txtParamCalculSC1.getText() + "\n" + labParamCalculSC2.getText()
			// + txtParamCalculSC2.getText() + "\n" + labNbreSC.getText()
			// + txtNbreSC.getText() + "\n" + labNbreMessagesSC.getText() +
			// txtNbreMessagesSC.getText() + "\n" + labNbreLanceursSC.getText()
			// + txtNbreLanceursSC.getText() + "\n"
			// + labNbrePLSC.getText() + txtNbrePLSC.getText() + "\n" +
			// labNbreMessagesPLSC.getText() + txtNbreMessagesPLSC.getText() +
			// "\n" + labNbreSCLancesPL.getText()
			// + txtNbreSCLancesPL.getText() + "\n" +
			// labNbreMessagesLDSC.getText() + txtNbreMessagesLDSC.getText() +
			// "\n" + labNbreSCLancesLD.getText() + txtNbreSCLancesLD.getText()
			// + "\n"
			// + labNbreMessagesLocuteursDominantsSC.getText() +
			// txtNbreMessagesLocuteursDominantsSC.getText() + "\n" +
			// labNbreMessagesLocuteursDominantsSaufPremierSC.getText()
			// + txtNbreMessagesLocuteursDominantsSaufPremierSC.getText() + "\n"
			// + labNbreLocuteursDominantsLanceursSC.getText() +
			// txtNbreLocuteursDominantsLanceursSC.getText() + "\n"
			// + labNbreSCLancesLocuteursDominantsSaufPremier.getText() +
			// txtNbreSCLancesLocuteursDominantsSaufPremier.getText() + "\n";
			//
			// // JTABLE
			// // TEST SUR fNbreVues POUR SAVOIR SI IL Y A DES CONVERSATIONS
			// VENANT
			// // DE
			// // FORUM
			// fromForum = false;
			// for (ConversationModel conversation : setConversations) {
			// int fNbreVues = conversation.getfNbreVues();
			// //
			// System.out.println("LocuteursPanel - listeChanged : fQualiteLocuteur = "+fQualiteLocuteur);
			// if (fNbreVues != 0) {
			// fromForum = true;
			// break;
			// }
			// }
			// if (!fromForum)
			// tableConversations = new JTable(new
			// TabConversationsModel(setConversations, conversationsListe));
			// else
			// tableConversations = new JTable(new
			// TabConversationsForumModel(setConversations,
			// conversationsListe));
			//
			// scrollTabConversations.setViewportView(tableConversations);
			// applyRenderer();
			// panelBas.add(scrollTabConversations);
			// panel.add(panelFiltre);
			cl.show(cards, "panel plein");
		} else {
			cl.show(cards, "panel vide");
		}
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

	public JPanel getLocuteursPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocuteursTxtToPdf() {
		// TODO Auto-generated method stub
		return null;
	}

	public JPanel getMessagesPanel() {
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

	@Override
	public JPanel getTabConversationsPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getTabLocuteursPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getAnalysePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getTabMessagesPanel() {
		// TODO Auto-generated method stub
		return null;
	}
}
