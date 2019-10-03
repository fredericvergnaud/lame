package vue.projetPanelInf;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import javax.swing.BoxLayout;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import renderers.CustomPieChartBoxPanel;
import renderers.CustomStatBoxPanel;
import renderers.SujetsCollectifsRenderer;
import modeles.ConversationModel;
import modeles.LocuteurModel;
import modeles.evenements.ListeChangedEvent;
import modeles.tableaux.TabConversationsForumModel;
import modeles.tableaux.TabConversationsModel;
import controleurs.ListeController;
import controleurs.vuesabstraites.ListeView;

public class TabConversationsPanel extends ListeView implements ActionListener {

	private JScrollPane scrollTabConversations;
	private JPanel cards, panel, panelFiltre, panelTab;
	private LamePanel panelVide;
	private DecimalFormat df = new DecimalFormat("0.00");
	private JTable tableConversations;
	private GridBagConstraints cHaut;
	private Set<ConversationModel> setConversations;
	private JTextField filterText;
	private TableRowSorter<TabConversationsModel> sorterConversations;
	private TableRowSorter<TabConversationsForumModel> sorterConversationsForum;
	private JComboBox typesFiltre;
	private ResourceBundle ressourcesTabConversations;
	private String conversationsTxtToPdf;
	private boolean fromForum;
	private Set<LocuteurModel> setLocuteurs;
	private CustomStatBoxPanel box1, box2;
	private CardLayout cl;
	private CustomPieChartBoxPanel box3, box4, box5, box6, box7, box8, box9, box11;

	public TabConversationsPanel(ListeController listeController, ResourceBundle ressourcesTabConversations) {
		super(listeController);
		this.ressourcesTabConversations = ressourcesTabConversations;
	}

	public JPanel getPanel() {
		panel = new JPanel();
		panel.setOpaque(true);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(700, 350));
		panel.setMinimumSize(new Dimension(700, 350));
		panel.setMaximumSize(new Dimension(6500, 350));
		panel.setBorder(new TitledBorder(""));
		panel.setName("PANEL_PLEIN");
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);

		panelTab = new JPanel();
		BoxLayout boxLayoutTab = new BoxLayout(panelTab, BoxLayout.Y_AXIS);
		panelTab.setLayout(boxLayoutTab);
		panelTab.setPreferredSize(new Dimension(650, 300));
		panelTab.setMinimumSize(new Dimension(650, 300));
		panelTab.setMaximumSize(new Dimension(6500, 3000));
		panelTab.setAlignmentY(Component.TOP_ALIGNMENT);

		tableConversations = new JTable(new TabConversationsModel(new HashSet<ConversationModel>(), ressourcesTabConversations));
		scrollTabConversations = new JScrollPane(tableConversations);
		scrollTabConversations.setPreferredSize(new Dimension(650, 300));
		scrollTabConversations.setMinimumSize(new Dimension(650, 300));
		scrollTabConversations.setMaximumSize(new Dimension(6500, 3000));
		panelTab.add(scrollTabConversations);

		panelFiltre = new JPanel();
		panelFiltre.setLayout(new BoxLayout(panelFiltre, BoxLayout.X_AXIS));
		panelFiltre.setAlignmentY(Component.TOP_ALIGNMENT);
		panelFiltre.setPreferredSize(new Dimension(700, 30));
		panelFiltre.setMinimumSize(new Dimension(700, 30));
		panelFiltre.setMaximumSize(new Dimension(7000, 30));

		JLabel labFiltre = new JLabel(ressourcesTabConversations.getString("txt_Filtre") + " : ", SwingConstants.LEFT);
		panelFiltre.add(labFiltre);

		String[] filterTypes = { ressourcesTabConversations.getString("txt_FiltreDuree"), ressourcesTabConversations.getString("txt_FiltreNbreMessages"),
				ressourcesTabConversations.getString("txt_FiltreNbreLocuteurs"), ressourcesTabConversations.getString("txt_FiltreLanceur") };
		typesFiltre = new JComboBox(filterTypes);
		typesFiltre.setSelectedIndex(1);
		typesFiltre.addActionListener(this);
		panelFiltre.add(typesFiltre);

		filterText = new JTextField();
		filterText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (!fromForum)
					newFilterConversations();
				else
					newFilterConversationsForum();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!fromForum)
					newFilterConversations();
				else
					newFilterConversationsForum();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!fromForum)
					newFilterConversations();
				else
					newFilterConversationsForum();
			}
		});
		labFiltre.setLabelFor(filterText);
		panelFiltre.add(filterText);

		panel.add(panelTab);
		panel.add(panelFiltre);

		// CardLayout
		panelVide = new LamePanel();
		panelVide.setName("PANEL_VIDE");
		cards = new JPanel(new CardLayout());
		cards.add(panel, panel.getName());
		cards.add(panelVide.getPanel(), panelVide.getName());

		// AFFICHAGE DES QUE LE PANEL APPARAIT DANS LA FRAME
		CardLayout cl = (CardLayout) (cards.getLayout());
		if (getListeController() != null) {
			cl.show(cards, "PANEL_PLEIN");

			setConversations = getListeController().getListeSelected().getSetConversations();
			setLocuteurs = getListeController().getSetLocuteurs();
			// FORUM
			fromForum = false;
			for (ConversationModel conversation : setConversations) {
				int fNbreVues = conversation.getfNbreVues();
				if (fNbreVues != 0) {
					fromForum = true;
					break;
				}
			}
			if (!fromForum)
				tableConversations = new JTable(new TabConversationsModel(setConversations, ressourcesTabConversations));
			else
				tableConversations = new JTable(new TabConversationsForumModel(setConversations, ressourcesTabConversations));

			scrollTabConversations.setViewportView(tableConversations);
			applyRenderer();
		}

		return cards;
	}

	@Override
	public JPanel getTabConversationsPanel() {
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
		tableConversations.setOpaque(true);
		tableConversations.setBackground(Color.WHITE);

		tableConversations.setDefaultRenderer(Integer.class, new SujetsCollectifsRenderer(ressourcesTabConversations));
		tableConversations.setDefaultRenderer(Date.class, new SujetsCollectifsRenderer(ressourcesTabConversations));
		tableConversations.setDefaultRenderer(String.class, new SujetsCollectifsRenderer(ressourcesTabConversations));
		tableConversations.setDefaultRenderer(Boolean.class, new SujetsCollectifsRenderer(ressourcesTabConversations));

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
			// colLanceur.setCellRenderer(new
			// SujetsCollectifsLanceursRenderer(setLocuteurs));

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
			if (typeFiltre.equals(ressourcesTabConversations.getString("txt_FiltreLanceur"))) {
				rf = RowFilter.regexFilter("(?i)" + filterText.getText(), 9);
			} else {
				if (typeFiltre.equals(ressourcesTabConversations.getString("txt_FiltreDuree")))
					numCol = 5;
				else if (typeFiltre.equals(ressourcesTabConversations.getString("txt_FiltreNbreMessages")))
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
			if (typeFiltre.equals(ressourcesTabConversations.getString("txt_FiltreLanceur"))) {
				rf = RowFilter.regexFilter("(?i)" + filterText.getText(), 9);
			} else {
				if (typeFiltre.equals(ressourcesTabConversations.getString("txt_FiltreDuree")))
					numCol = 5;
				else if (typeFiltre.equals(ressourcesTabConversations.getString("txt_FiltreNbreMessages")))
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
			if (typeFiltre.equals(ressourcesTabConversations.getString("txt_FiltreLanceur"))) {
				rf = RowFilter.regexFilter("(?i)" + filterText.getText(), 9);
			} else {
				if (typeFiltre.equals(ressourcesTabConversations.getString("txt_FiltreDuree")))
					numCol = 5;
				else if (typeFiltre.equals(ressourcesTabConversations.getString("txt_FiltreNbreMessages")))
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
			if (typeFiltre.equals(ressourcesTabConversations.getString("txt_FiltreLanceur"))) {
				rf = RowFilter.regexFilter("(?i)" + filterText.getText(), 9);
			} else {
				if (typeFiltre.equals(ressourcesTabConversations.getString("txt_FiltreDuree")))
					numCol = 5;
				else if (typeFiltre.equals(ressourcesTabConversations.getString("txt_FiltreNbreMessages")))
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
		CardLayout cl = (CardLayout) (cards.getLayout());
		if (event.getNewNumero() != 0 && event.getNewMapIdMessages().size() != 0) {
			cl.show(cards, "PANEL_PLEIN");

			setConversations = event.getNewSetConversations();
			setLocuteurs = event.getNewSetLocuteurs();
			// FORUM
			fromForum = false;
			for (ConversationModel conversation : setConversations) {
				int fNbreVues = conversation.getfNbreVues();
				if (fNbreVues != 0) {
					fromForum = true;
					break;
				}
			}
			if (!fromForum)
				tableConversations = new JTable(new TabConversationsModel(setConversations, ressourcesTabConversations));
			else
				tableConversations = new JTable(new TabConversationsForumModel(setConversations, ressourcesTabConversations));

			scrollTabConversations.setViewportView(tableConversations);
			applyRenderer();

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
		} else {
			cl.show(cards, "PANEL_VIDE");
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

	@Override
	public JPanel getAnalysePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocuteursTxtToPdf() {
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
