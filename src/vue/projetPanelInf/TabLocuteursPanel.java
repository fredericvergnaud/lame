package vue.projetPanelInf;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.RowFilter.ComparisonType;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import renderers.LocuteursRenderer;
import renderers.LocuteursForumRenderer;
import modeles.LocuteurModel;
import modeles.evenements.ListeChangedEvent;
import modeles.tableaux.TabLocuteursModel;
import modeles.tableaux.TabLocuteursForumModel;
import controleurs.ListeController;
import controleurs.vuesabstraites.ListeView;

public class TabLocuteursPanel extends ListeView implements ActionListener {

	private JScrollPane scrollTabLocuteurs;
	private DecimalFormat df = new DecimalFormat("0.00");
	private JPanel cards, panel, panelTab, panelFiltre;
	private LamePanel panelVide;
	private JTable tableLocuteurs;
	private String locuteursTxtToPdf = null;
	private JTextField filterText;
	private TableRowSorter<TabLocuteursModel> sorterLocuteurs;
	private TableRowSorter<TabLocuteursForumModel> sorterLocuteursForum;
	private JComboBox typesFiltre;
	private ResourceBundle ressourcesTabLocuteurs;
	private boolean fromForum;
	private String identifiantLocuteurToShow;
	private Set<LocuteurModel> setLocuteurs;

	// private String[] columnNames;

	public TabLocuteursPanel(ListeController listeController, ResourceBundle ressourcesTabLocuteurs) {
		super(listeController);
		this.ressourcesTabLocuteurs = ressourcesTabLocuteurs;
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

		tableLocuteurs = new JTable(new TabLocuteursModel(new HashSet<LocuteurModel>(), ressourcesTabLocuteurs));
		scrollTabLocuteurs = new JScrollPane(tableLocuteurs);
		scrollTabLocuteurs.setPreferredSize(new Dimension(650, 300));
		scrollTabLocuteurs.setMinimumSize(new Dimension(650, 300));
		scrollTabLocuteurs.setMaximumSize(new Dimension(6500, 3000));
		panelTab.add(scrollTabLocuteurs);

		panelFiltre = new JPanel();
		panelFiltre.setLayout(new BoxLayout(panelFiltre, BoxLayout.X_AXIS));
		panelFiltre.setAlignmentY(Component.TOP_ALIGNMENT);
		panelFiltre.setPreferredSize(new Dimension(700, 30));
		panelFiltre.setMinimumSize(new Dimension(700, 30));
		panelFiltre.setMaximumSize(new Dimension(7000, 30));

		JLabel labFiltre = new JLabel(ressourcesTabLocuteurs.getString("txt_Filtre") + " : ", SwingConstants.LEFT);
		panelFiltre.add(labFiltre);

		String[] filterTypes = { ressourcesTabLocuteurs.getString("txt_FiltreParticipation"), ressourcesTabLocuteurs.getString("txt_FiltreMessages"),
				ressourcesTabLocuteurs.getString("txt_FiltreConversations"), ressourcesTabLocuteurs.getString("txt_FiltreSClances"), ressourcesTabLocuteurs.getString("txt_FiltreSC"),
				ressourcesTabLocuteurs.getString("txt_FiltreMessagesSC") };
		typesFiltre = new JComboBox(filterTypes);
		typesFiltre.setSelectedIndex(1);
		typesFiltre.addActionListener(this);
		panelFiltre.add(typesFiltre);

		filterText = new JTextField();
		filterText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (!fromForum)
					newFilterLocuteurs();
				else
					newFilterLocuteursForum();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!fromForum)
					newFilterLocuteurs();
				else
					newFilterLocuteursForum();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!fromForum)
					newFilterLocuteurs();
				else
					newFilterLocuteursForum();
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
			setLocuteurs = getListeController().getSetLocuteurs();
			fromForum = false;
			for (LocuteurModel locuteur : setLocuteurs) {
				Date fStatDateRegistered = locuteur.getfStatDateRegistrered();
				// System.out.println("LocuteursPanel - listeChanged : fStatDateRegistered = "
				// + fStatDateRegistered);
				if (fStatDateRegistered != null) {
					fromForum = true;
					break;
				}
			}
			if (!fromForum)
				tableLocuteurs = new JTable(new TabLocuteursModel(setLocuteurs, ressourcesTabLocuteurs));
			else
				tableLocuteurs = new JTable(new TabLocuteursForumModel(setLocuteurs, ressourcesTabLocuteurs));
			scrollTabLocuteurs.setViewportView(tableLocuteurs);
			applyRenderer();
		}
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

	public void applyRenderer() {
		// GENERAL
		tableLocuteurs.setRowSelectionAllowed(true);
		tableLocuteurs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableLocuteurs.setFillsViewportHeight(true);
		tableLocuteurs.setAutoCreateRowSorter(true);
		tableLocuteurs.setOpaque(true);
		tableLocuteurs.setBackground(Color.WHITE);
		// HEADERS
		// Hauteur
		tableLocuteurs.getTableHeader().setPreferredSize(new Dimension(tableLocuteurs.getColumnModel().getTotalColumnWidth(), 65));
		// Centrage
		((JLabel) tableLocuteurs.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		((JLabel) tableLocuteurs.getTableHeader().getDefaultRenderer()).setVerticalAlignment(SwingConstants.TOP);

		if (!fromForum) {
			// Sorter
			sorterLocuteurs = new TableRowSorter<TabLocuteursModel>((TabLocuteursModel) tableLocuteurs.getModel());
			tableLocuteurs.setRowSorter(sorterLocuteurs);
			// Renderer
			tableLocuteurs.setDefaultRenderer(Date.class, new LocuteursRenderer(ressourcesTabLocuteurs));
			tableLocuteurs.setDefaultRenderer(String.class, new LocuteursRenderer(ressourcesTabLocuteurs));
			tableLocuteurs.setDefaultRenderer(Float.class, new LocuteursRenderer(ressourcesTabLocuteurs));
			tableLocuteurs.setDefaultRenderer(Integer.class, new LocuteursRenderer(ressourcesTabLocuteurs));
			tableLocuteurs.setDefaultRenderer(Boolean.class, new LocuteursRenderer(ressourcesTabLocuteurs));
			// Largeurs
			TableColumn colId = tableLocuteurs.getColumnModel().getColumn(0);
			TableColumn colNom = tableLocuteurs.getColumnModel().getColumn(1);
			TableColumn colDebut = tableLocuteurs.getColumnModel().getColumn(2);
			TableColumn colFin = tableLocuteurs.getColumnModel().getColumn(3);
			TableColumn colDuree = tableLocuteurs.getColumnModel().getColumn(4);
			TableColumn colNbreMessages = tableLocuteurs.getColumnModel().getColumn(5);
			TableColumn colIntensite = tableLocuteurs.getColumnModel().getColumn(6);
			TableColumn colLD = tableLocuteurs.getColumnModel().getColumn(7);
			TableColumn colNbreConversations = tableLocuteurs.getColumnModel().getColumn(8);
			TableColumn colNbreSCLances = tableLocuteurs.getColumnModel().getColumn(9);
			TableColumn colNbreSC = tableLocuteurs.getColumnModel().getColumn(10);
			TableColumn colNbreMessagesSC = tableLocuteurs.getColumnModel().getColumn(11);
			TableColumn colPourcentMessages = tableLocuteurs.getColumnModel().getColumn(12);

			int widthColId = 0;
			int widthColNom = 200;
			int widthColDebut = 130;
			int widthColFin = 130;
			int widthColDuree = 130;
			int widthColNbreMessages = 150;
			int widthColIntensite = 100;
			int widthColLD = 100;
			int widthColNbreConversations = 130;
			int widthColNbreSC = 130;
			int widthColNbreSCLances = 150;
			int widthColNbreMessagesSC = 200;
			int widthColPourcentMessages = 180;

			colId.setPreferredWidth(widthColId);
			colId.setMinWidth(widthColId);
			colId.setMaxWidth(widthColId);
			colNom.setPreferredWidth(widthColNom);
			colNom.setMinWidth(widthColNom);
			colNom.setMaxWidth(widthColNom);
			colDebut.setPreferredWidth(widthColDebut);
			colFin.setPreferredWidth(widthColFin);
			colDuree.setPreferredWidth(widthColDuree);
			colNbreMessages.setPreferredWidth(widthColNbreMessages);
			colIntensite.setPreferredWidth(widthColIntensite);
			colLD.setPreferredWidth(widthColLD);
			colNbreConversations.setPreferredWidth(widthColNbreConversations);
			colNbreSC.setPreferredWidth(widthColNbreSC);
			colNbreSCLances.setPreferredWidth(widthColNbreSCLances);
			colNbreMessagesSC.setPreferredWidth(widthColNbreMessagesSC);
			colPourcentMessages.setPreferredWidth(widthColPourcentMessages);
		} else {
			sorterLocuteursForum = new TableRowSorter<TabLocuteursForumModel>((TabLocuteursForumModel) tableLocuteurs.getModel());
			tableLocuteurs.setRowSorter(sorterLocuteursForum);
			tableLocuteurs.setDefaultRenderer(Date.class, new LocuteursForumRenderer(ressourcesTabLocuteurs));
			tableLocuteurs.setDefaultRenderer(String.class, new LocuteursForumRenderer(ressourcesTabLocuteurs));
			tableLocuteurs.setDefaultRenderer(Float.class, new LocuteursForumRenderer(ressourcesTabLocuteurs));
			tableLocuteurs.setDefaultRenderer(Double.class, new LocuteursForumRenderer(ressourcesTabLocuteurs));
			tableLocuteurs.setDefaultRenderer(Integer.class, new LocuteursForumRenderer(ressourcesTabLocuteurs));
			tableLocuteurs.setDefaultRenderer(Boolean.class, new LocuteursForumRenderer(ressourcesTabLocuteurs));
			// Largeurs
			TableColumn colId = tableLocuteurs.getColumnModel().getColumn(0);
			TableColumn colNom = tableLocuteurs.getColumnModel().getColumn(1);
			TableColumn colRole = tableLocuteurs.getColumnModel().getColumn(2);
			TableColumn colQualite = tableLocuteurs.getColumnModel().getColumn(3);
			TableColumn colActivite = tableLocuteurs.getColumnModel().getColumn(4);
			TableColumn colEtoiles = tableLocuteurs.getColumnModel().getColumn(5);
			TableColumn colDebut = tableLocuteurs.getColumnModel().getColumn(6);
			TableColumn colFin = tableLocuteurs.getColumnModel().getColumn(7);
			TableColumn colDuree = tableLocuteurs.getColumnModel().getColumn(8);
			TableColumn colNbreMessages = tableLocuteurs.getColumnModel().getColumn(9);
			TableColumn colIntensite = tableLocuteurs.getColumnModel().getColumn(10);
			TableColumn colLD = tableLocuteurs.getColumnModel().getColumn(11);
			TableColumn colNbreConversations = tableLocuteurs.getColumnModel().getColumn(12);
			TableColumn colNbreSCLances = tableLocuteurs.getColumnModel().getColumn(13);
			TableColumn colNbreSC = tableLocuteurs.getColumnModel().getColumn(14);
			TableColumn colNbreMessagesSC = tableLocuteurs.getColumnModel().getColumn(15);
			TableColumn colPourcentMessages = tableLocuteurs.getColumnModel().getColumn(16);

			int widthColId = 0;
			int widthColNom = 200;
			int widthColRole = 100;
			int widthColQualite = 100;
			int widthColActivite = 100;
			int widthColEtoiles = 100;
			int widthColDebut = 175;
			int widthColFin = 175;
			int widthColDuree = 130;
			int widthColNbreMessages = 150;
			int widthColIntensite = 100;
			int widthColLD = 100;
			int widthColNbreConversations = 130;
			int widthColNbreSC = 200;
			int widthColNbreSCLances = 200;
			int widthColNbreMessagesSC = 200;
			int widthColPourcentMessages = 180;

			colId.setPreferredWidth(widthColId);
			colId.setMinWidth(widthColId);
			colId.setMaxWidth(widthColId);
			colNom.setPreferredWidth(widthColNom);
			colNom.setMinWidth(widthColNom);
			colNom.setMaxWidth(widthColNom);
			colRole.setPreferredWidth(widthColRole);
			colQualite.setPreferredWidth(widthColQualite);
			colActivite.setPreferredWidth(widthColActivite);
			colEtoiles.setPreferredWidth(widthColEtoiles);
			colDebut.setPreferredWidth(widthColDebut);
			colFin.setPreferredWidth(widthColFin);
			colDuree.setPreferredWidth(widthColDuree);
			colNbreMessages.setPreferredWidth(widthColNbreMessages);
			colIntensite.setPreferredWidth(widthColIntensite);
			colLD.setPreferredWidth(widthColLD);
			colNbreConversations.setPreferredWidth(widthColNbreConversations);
			colNbreSC.setPreferredWidth(widthColNbreSC);
			colNbreSCLances.setPreferredWidth(widthColNbreSCLances);
			colNbreMessagesSC.setPreferredWidth(widthColNbreMessagesSC);
			colPourcentMessages.setPreferredWidth(widthColPourcentMessages);
		}

		// Mouses listeners
		tableLocuteurs.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JTable target = (JTable) e.getSource();
					int row = target.getSelectedRow();
					identifiantLocuteurToShow = String.valueOf(target.getValueAt(row, 0));
					System.out.println("LocuteursPanel - mouseClicked : identifiant locuteur to show = " + identifiantLocuteurToShow);
					System.out.println("LocuteursPanel - mouseClicked : ListeController = " + getListeController());
					getListeController().notifyLocuteurToShow(identifiantLocuteurToShow);

					// identifiantToShowChanged(new
					// IdentifiantToShowChangedEvent(event.getSource(),
					// event.getNewNumeroListe(), identifiantToShow));
					// System.out.println(panel.getParent().getParent().toString());
					// JTabbedPane jtp = (JTabbedPane)
					// panel.getParent().getParent();
					// jtp.setSelectedIndex(0);
					// System.out.println("nom composant = "+jtp.getComponent(0).getComponentAt(0,
					// 0).getName());
					// getListeController().notifyMessageToShow(numeroToShow,identifiantToShow);

				}

			}
		});
	}

	private void newFilterLocuteurs() {
		RowFilter<TabLocuteursModel, Object> rf = null;
		try {
			String typeFiltre = (String) typesFiltre.getSelectedItem();
			int numCol;
			// if (typeFiltre.equals("Par Locuteur dominant")) {
			// String input = "(?i)" + filterText.getText();
			// String output;
			// if (input.equals("oui"))
			// output = "true";
			// else
			// output = "false";
			// rf = RowFilter.regexFilter(output, 6);
			// } else {
			if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreParticipation")))
				numCol = 4;
			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreMessages")))
				numCol = 5;
			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreConversations")))
				numCol = 8;
			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreSClances")))
				numCol = 9;
			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreSC")))
				numCol = 10;
			else {
				numCol = 11;
			}
			if (!filterText.getText().equals(""))
				try {
					rf = RowFilter.numberFilter(ComparisonType.AFTER, Float.valueOf(filterText.getText()), numCol);
				} catch (NumberFormatException nfe) {
					System.out.println("Ce n'est pas un nombre");

				}
			// }

		} catch (PatternSyntaxException e) {
			return;
		}
		sorterLocuteurs.setRowFilter(rf);
	}

	private void newFilterLocuteursForum() {
		RowFilter<TabLocuteursForumModel, Object> rf = null;
		try {
			String typeFiltre = (String) typesFiltre.getSelectedItem();
			int numCol;
			// if (typeFiltre.equals("Par Locuteur dominant")) {
			// String input = "(?i)" + filterText.getText();
			// String output;
			// if (input.equals("oui"))
			// output = "true";
			// else
			// output = "false";
			// rf = RowFilter.regexFilter(output, 6);
			// } else {
			if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreParticipation")))
				numCol = 8;

			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreMessages")))
				numCol = 9;

			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreConversations")))
				numCol = 12;

			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreSClances")))
				numCol = 13;

			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreSC")))
				numCol = 14;

			else {
				numCol = 15;
			}
			if (!filterText.getText().equals(""))
				try {
					rf = RowFilter.numberFilter(ComparisonType.AFTER, Float.valueOf(filterText.getText()), numCol);
				} catch (NumberFormatException nfe) {
					System.out.println("Ce n'est pas un nombre");

				}
			// }

		} catch (PatternSyntaxException e) {
			return;
		}
		sorterLocuteursForum.setRowFilter(rf);
	}

	private void newFilterLocuteurs(String typeFiltre) {
		RowFilter<TabLocuteursModel, Object> rf = null;
		try {
			int numCol;
			// if (typeFiltre.equals("Par Locuteur dominant")) {
			// String input = "(?i)" + filterText.getText();
			// String output;
			// if (input.equals("oui"))
			// output = "true";
			// else
			// output = "false";
			// rf = RowFilter.regexFilter(output, 6);
			// } else {
			if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreParticipation")))
				numCol = 4;
			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreMessages")))
				numCol = 5;
			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreConversations")))
				numCol = 8;
			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreSClances")))
				numCol = 9;
			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreSC")))
				numCol = 10;
			else
				numCol = 11;
			if (!filterText.getText().equals(""))
				try {
					rf = RowFilter.numberFilter(ComparisonType.AFTER, Float.valueOf(filterText.getText()), numCol);
				} catch (NumberFormatException nfe) {
					System.out.println("Ce n'est pas un nombre");

				}
			// }

		} catch (PatternSyntaxException e) {
			return;
		}
		sorterLocuteurs.setRowFilter(rf);
	}

	private void newFilterLocuteursForum(String typeFiltre) {
		RowFilter<TabLocuteursForumModel, Object> rf = null;
		try {
			int numCol;
			// if (typeFiltre.equals("Par Locuteur dominant")) {
			// String input = "(?i)" + filterText.getText();
			// String output;
			// if (input.equals("oui"))
			// output = "true";
			// else
			// output = "false";
			// rf = RowFilter.regexFilter(output, 6);
			// } else {
			if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreParticipation")))
				numCol = 8;
			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreMessages")))
				numCol = 9;
			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreConversations")))
				numCol = 12;
			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreSClances")))
				numCol = 13;
			else if (typeFiltre.equals(ressourcesTabLocuteurs.getString("txt_FiltreSC")))
				numCol = 14;
			else
				numCol = 11;
			if (!filterText.getText().equals(""))
				try {
					rf = RowFilter.numberFilter(ComparisonType.AFTER, Float.valueOf(filterText.getText()), numCol);
				} catch (NumberFormatException nfe) {
					System.out.println("Ce n'est pas un nombre");

				}
			// }

		} catch (PatternSyntaxException e) {
			return;
		}
		sorterLocuteursForum.setRowFilter(rf);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JComboBox cb = (JComboBox) e.getSource();
		String typeFiltre = (String) cb.getSelectedItem();
		if (!fromForum)
			newFilterLocuteurs(typeFiltre);
		else
			newFilterLocuteursForum(typeFiltre);
	}

	@Override
	public void listeChanged(ListeChangedEvent event) {
		// System.out.println("TabLocuteursPanel - listeChanged : event.getNewNumeroListe() = "
		// + event.getNewNumero());
		CardLayout cl = (CardLayout) (cards.getLayout());
		if (event.getNewNumero() != 0 && event.getNewMapIdMessages().size() != 0) {
			cl.show(cards, "PANEL_PLEIN");
			setLocuteurs = event.getNewSetLocuteurs();
			fromForum = false;
			for (LocuteurModel locuteur : setLocuteurs) {
				Date fStatDateRegistered = locuteur.getfStatDateRegistrered();
				// System.out.println("LocuteursPanel - listeChanged : fStatDateRegistered = "
				// + fStatDateRegistered);
				if (fStatDateRegistered != null) {
					fromForum = true;
					break;
				}
			}
			if (!fromForum)
				tableLocuteurs = new JTable(new TabLocuteursModel(setLocuteurs, ressourcesTabLocuteurs));
			else
				tableLocuteurs = new JTable(new TabLocuteursForumModel(setLocuteurs, ressourcesTabLocuteurs));

			scrollTabLocuteurs.setViewportView(tableLocuteurs);
			applyRenderer();

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
		return getPanel();
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
