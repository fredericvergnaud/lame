package vue.projetPanelInf;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.PatternSyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import renderers.DateRenderer;
//import renderers.TabMessagesRenderer;
import comparators.MessageDateUsComparator;
import modeles.MessageModel;
import modeles.evenements.ListeChangedEvent;
import modeles.tableaux.TabMessagesForumModel;
import modeles.tableaux.TabMessagesModel;
import controleurs.ListeController;
import controleurs.vuesabstraites.ListeView;

public class _MessagesPanel extends ListeView implements ActionListener {

	private JScrollPane scrollListMessage;
	private JLabel labNbreMessages, txtNbreMessages, labDureeSuivi, txtDureeSuivi;
	private JPanel cards, panelVide, panel, panelBas, panelFiltre;
	private JButton bExportMess, bSelect;
	// private DialogPanelExportMessages exportPanel;
	// private DialogPanelSelectedMessages selectPanel;
	private JTable tableMessages;
	private TableColumn tcol;
	// public ListeController listeController;
	private JTextField filterText;
	private TableRowSorter<TabMessagesModel> sorterMessages;
	private TableRowSorter<TabMessagesForumModel> sorterMessagesForum;
	private JComboBox typesFiltre;
	private String identifiantMessageToShow;
	// private ListeController listeController;
	private ResourceBundle messagesListe;
	private boolean fromForum;

	public _MessagesPanel(ListeController listeController, ResourceBundle messagesListe) {
		super(listeController);
		// this.listeController = listeController;
		this.messagesListe = messagesListe;
	}

	public JPanel getPanel() {
		panel = new JPanel();
		panel.setOpaque(true);
		panel.setAlignmentY(Component.LEFT_ALIGNMENT);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(1250, 850));
		panel.setBorder(new TitledBorder(messagesListe.getString("txt_Messages")));
		panel.setName("panel plein");

		int largeurLabel = 200;
		int hauteurLabel = 15;
		int largeurTxtLabel = 750;
		int hauteurTxtLabel = 15;
		Dimension dimLabel = new Dimension(largeurLabel, hauteurLabel);
		Dimension dimLabelTxt = new Dimension(largeurTxtLabel, hauteurTxtLabel);
		Font gras = new Font("sansserif", Font.BOLD, 12);
		Font normal = new Font("sansserif", Font.PLAIN, 12);

		GridBagLayout lHaut = new GridBagLayout();
		JPanel panelHaut = new JPanel();
		panelHaut.setPreferredSize(new Dimension(1250, 50));
		panelHaut.setMinimumSize(new Dimension(1250, 50));
		panelHaut.setMaximumSize(new Dimension(1250, 50));
		panelHaut.setLayout(lHaut);
		GridBagConstraints cHaut = new GridBagConstraints();
		panelHaut.setAlignmentY(Component.LEFT_ALIGNMENT);

		JPanel panelGauche = new JPanel();
		panelGauche.setLayout(new BoxLayout(panelGauche, BoxLayout.Y_AXIS));
		panelGauche.setPreferredSize(new Dimension(950, 50));
		panelGauche.setAlignmentY(Component.LEFT_ALIGNMENT);

		JPanel panelDroite = new JPanel();
		panelDroite.setLayout(new BoxLayout(panelDroite, BoxLayout.Y_AXIS));
		panelDroite.setPreferredSize(new Dimension(300, 50));
		panelDroite.setAlignmentY(Component.LEFT_ALIGNMENT);

		GridBagLayout l0 = new GridBagLayout();
		JPanel panel0 = new JPanel();
		panel0.setLayout(l0);
		GridBagConstraints c0 = new GridBagConstraints();

		labNbreMessages = new JLabel("", SwingConstants.LEFT);
		labNbreMessages.setPreferredSize(dimLabel);
		labNbreMessages.setFont(normal);
		txtNbreMessages = new JLabel("", SwingConstants.LEFT);
		txtNbreMessages.setPreferredSize(dimLabelTxt);
		txtNbreMessages.setFont(gras);

		c0.gridx = 0;
		c0.gridy = 0;
		panel0.add(labNbreMessages, c0);
		c0.gridx = 1;
		c0.gridy = 0;
		panel0.add(txtNbreMessages, c0);

		panelGauche.add(panel0);

		GridBagLayout l1 = new GridBagLayout();
		JPanel panel1 = new JPanel();
		panel1.setLayout(l1);
		GridBagConstraints c1 = new GridBagConstraints();

		labDureeSuivi = new JLabel("", SwingConstants.LEFT);
		labDureeSuivi.setPreferredSize(dimLabel);
		labDureeSuivi.setFont(normal);
		txtDureeSuivi = new JLabel("", SwingConstants.LEFT);
		txtDureeSuivi.setPreferredSize(dimLabelTxt);
		txtDureeSuivi.setFont(gras);

		c1.gridx = 0;
		c1.gridy = 0;
		panel1.add(labDureeSuivi, c1);
		c1.gridx = 1;
		c1.gridy = 0;
		panel1.add(txtDureeSuivi, c1);

		panelGauche.add(panel1);

		// BOUTONS
		int largeurBouton = 100;
		int hauteurBouton = 25;

		GridBagLayout l8 = new GridBagLayout();
		JPanel panel8 = new JPanel();
		panel8.setLayout(l8);
		GridBagConstraints c8 = new GridBagConstraints();

		bExportMess = new JButton("Exporter");
		bExportMess.setVisible(false);
		bExportMess.setPreferredSize(new Dimension(largeurBouton, hauteurBouton));
		// bExportMess.addActionListener(this);
		c8.gridx = 1;
		c8.gridy = 0;
		// panel8.add(bExportMess, c8);

		bSelect = new JButton("Sélection");
		bSelect.setVisible(false);
		bSelect.setPreferredSize(new Dimension(largeurBouton, hauteurBouton));

		// bSelect.addActionListener(this
		// new ActionListener() {
		// @Override
		// public void actionPerformed(ActionEvent e) {
		// // int[] tabSelection = tableMessages.getSelectedRows();
		// // if (tabSelection.length > 0) {
		// // selectPanel = new DialogPanelSelectedMessages();
		// // int result = JOptionPane.showOptionDialog(null,
		// // selectPanel, "S�lection de messages",
		// // JOptionPane.OK_CANCEL_OPTION,
		// // JOptionPane.QUESTION_MESSAGE, null, null, null);
		// // if (result == JOptionPane.OK_OPTION) {
		// // if (selectPanel.getRadioExport().isSelected())
		// // exportMessages(tabSelection);
		// // else
		// // deleteMessages(tabSelection);
		// // }
		// // } else
		// // JOptionPane.showMessageDialog(null,
		// // "Veuillez s�lectionner des messages.", "Erreur",
		// // JOptionPane.ERROR_MESSAGE);
		// }
		// }
		// );
		c8.gridx = 1;
		c8.gridy = 1;
		// panel8.add(bSelect, c8);

		panelDroite.add(panel8);

		cHaut.gridx = 0;
		cHaut.gridy = 0;
		panelHaut.add(panelGauche, cHaut);
		cHaut.gridx = 1;
		cHaut.gridy = 0;
		panelHaut.add(panelDroite, cHaut);

		tableMessages = new JTable(new TabMessagesModel(new ArrayList<MessageModel>(), messagesListe));
		scrollListMessage = new JScrollPane(tableMessages);

		panelBas = new JPanel();
		panelBas.setLayout(new BoxLayout(panelBas, BoxLayout.Y_AXIS));
		panelBas.setPreferredSize(new Dimension(1250, 805));
		panelBas.setAlignmentY(Component.LEFT_ALIGNMENT);

		panel.add(panelHaut);
		panel.add(panelBas);

		panelFiltre = new JPanel();
		panelFiltre.setLayout(new BoxLayout(panelFiltre, BoxLayout.X_AXIS));
		panelFiltre.setAlignmentY(Component.RIGHT_ALIGNMENT);
		panelFiltre.setMaximumSize(new Dimension(1250, 30));

		JLabel labFiltre = new JLabel(messagesListe.getString("txt_Filtre") + " : ", SwingConstants.LEFT);
		panelFiltre.add(labFiltre);

		String[] filterTypes = { messagesListe.getString("txt_FiltreDate"), messagesListe.getString("txt_FiltreLocuteur"),
				// messagesListe.getString("txt_FiltreMail"),
				messagesListe.getString("txt_FiltreSujet")
				//, messagesListe.getString("txt_FiltreNumeroMessage") 
				};
		typesFiltre = new JComboBox(filterTypes);
		typesFiltre.setSelectedIndex(1);
		typesFiltre.addActionListener(this);
		panelFiltre.add(typesFiltre);

		filterText = new JTextField();
		filterText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (!fromForum)
					newFilterMessages();
				else
					newFilterMessagesForum();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!fromForum)
					newFilterMessages();
				else
					newFilterMessagesForum();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!fromForum)
					newFilterMessages();
				else
					newFilterMessagesForum();
			}
		});
		labFiltre.setLabelFor(filterText);
		panelFiltre.add(filterText);

		// CardLayout
		panelVide = new JPanel();
		panelVide.setName("panel vide");
		panelVide.setBorder(new TitledBorder(messagesListe.getString("txt_Messages")));
		cards = new JPanel(new CardLayout());
		cards.add(panel, panel.getName());
		cards.add(panelVide, panelVide.getName());

		return cards;
	}

	@Override
	public JPanel getFilsListePanel() {
		return null;
	}

	// @Override
	// public void mapIdMessagesListeChanged(MapIdMessagesListeChangedEvent
	// event) {
	// CardLayout cl = (CardLayout) (cards.getLayout());
	// System.out.println("MessagesPanel - mapIdMessagesListeChanged : event.getNewNumeroListe() = "+event.getNewNumeroListe());
	// if (event.getNewNumeroListe() != 0
	// && event.getNewMapIdMessages().size() != 0) {
	// cl.show(cards, "panel plein");
	// // LABELS
	// labNbreMessages.setText(messagesListe
	// .getString("txt_NbreMessagesExtraits") + " : ");
	// txtNbreMessages.setText(String.valueOf(event.getNewNbreMessages()));
	//
	// labDureeSuivi.setText(messagesListe.getString("txt_DureeSuivi")
	// + " : ");
	// txtDureeSuivi.setText(event.getNewDureeSuivi());
	//
	// // BOUTONS
	// bExportMess.setVisible(true);
	// bSelect.setVisible(true);
	//
	// // JTABLE
	// ArrayList<MessageModel> listMessages = new ArrayList<MessageModel>(
	// event.getNewMapIdMessages().values());
	// MessageDateUsComparator comparator = new MessageDateUsComparator();
	// Collections.sort(listMessages, comparator);
	// tableMessages = new JTable(new TabMessagesModel(listMessages,
	// columnNames));
	// scrollListMessage.setViewportView(tableMessages);
	// applyRenderer();
	// panelBas.add(scrollListMessage);
	// panel.add(panelFiltre);
	// } else {
	// cl.show(cards, "panel vide");
	// }
	// }

	public JPanel getMessagesPanel() {
		return getPanel();
	}

	public void applyRenderer() {
		// GENERAL
		tableMessages.setAutoCreateRowSorter(true);
		tableMessages.setRowSelectionAllowed(true);
		tableMessages.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableMessages.setFillsViewportHeight(true);
		tableMessages.setOpaque(true);

		// DateRenderer
		tableMessages.setDefaultRenderer(Date.class, new DateRenderer());
//		tcol = tableMessages.getColumnModel().getColumn(0);
//		tcol.setCellRenderer(new TabMessagesRenderer());

		if (!fromForum) {
			// Sorter
			sorterMessages = new TableRowSorter<TabMessagesModel>((TabMessagesModel) tableMessages.getModel());
			tableMessages.setRowSorter(sorterMessages);
			// Largeurs colonnes
			TableColumn colIdMessage = tableMessages.getColumnModel().getColumn(0);
			TableColumn colDatePost = tableMessages.getColumnModel().getColumn(1);
			TableColumn colNomLocuteur = tableMessages.getColumnModel().getColumn(2);
			TableColumn colMailLocuteur = tableMessages.getColumnModel().getColumn(3);
			TableColumn colProfilYahooLocuteur = tableMessages.getColumnModel().getColumn(4);
			TableColumn colGroupPostYahooLocuteur = tableMessages.getColumnModel().getColumn(5);
			TableColumn colIdGoogleLocuteur = tableMessages.getColumnModel().getColumn(6);
			TableColumn colSujetMessage = tableMessages.getColumnModel().getColumn(7);
			TableColumn colSujetMessageTronque = tableMessages.getColumnModel().getColumn(8);
			TableColumn colNumeroMessage = tableMessages.getColumnModel().getColumn(9);

			int widthColIdMessage = 0;
			int widthColDatePost = 120;
			int widthColNomLocuteur = 100;
			int widthColMailLocuteur = 0;
			int widthColProfilYahooLocuteur = 0;
			int widthColGroupPostYahooLocuteur = 0;
			int widthColIdGoogleLocuteur = 0;
			int widthColSujetMessage = 390;
			int widthColSujetMessageTronque = 0;
			int widthColNumeroMessage = 80;

			colIdMessage.setPreferredWidth(widthColIdMessage);
			colIdMessage.setMinWidth(widthColIdMessage);
			colIdMessage.setMaxWidth(widthColIdMessage);
			colDatePost.setPreferredWidth(widthColDatePost);
			colNomLocuteur.setPreferredWidth(widthColNomLocuteur);
			colMailLocuteur.setPreferredWidth(widthColMailLocuteur);
			colMailLocuteur.setMinWidth(widthColMailLocuteur);
			colMailLocuteur.setMaxWidth(widthColMailLocuteur);
			colProfilYahooLocuteur.setPreferredWidth(widthColProfilYahooLocuteur);
			colProfilYahooLocuteur.setMinWidth(widthColProfilYahooLocuteur);
			colProfilYahooLocuteur.setMaxWidth(widthColProfilYahooLocuteur);
			colGroupPostYahooLocuteur.setPreferredWidth(widthColGroupPostYahooLocuteur);
			colGroupPostYahooLocuteur.setMinWidth(widthColGroupPostYahooLocuteur);
			colGroupPostYahooLocuteur.setMaxWidth(widthColGroupPostYahooLocuteur);
			colIdGoogleLocuteur.setPreferredWidth(widthColIdGoogleLocuteur);
			colIdGoogleLocuteur.setMinWidth(widthColIdGoogleLocuteur);
			colIdGoogleLocuteur.setMaxWidth(widthColIdGoogleLocuteur);
			colSujetMessage.setPreferredWidth(widthColSujetMessage);
			colSujetMessageTronque.setPreferredWidth(widthColSujetMessageTronque);
			colSujetMessageTronque.setMinWidth(widthColSujetMessageTronque);
			colSujetMessageTronque.setMaxWidth(widthColSujetMessageTronque);
			colNumeroMessage.setPreferredWidth(widthColNumeroMessage);
		} else {
			sorterMessagesForum = new TableRowSorter<TabMessagesForumModel>((TabMessagesForumModel) tableMessages.getModel());
			tableMessages.setRowSorter(sorterMessagesForum);
			// Largeurs colonnes
			TableColumn colIdMessage = tableMessages.getColumnModel().getColumn(0);
			TableColumn colDatePost = tableMessages.getColumnModel().getColumn(1);
			TableColumn colNomLocuteur = tableMessages.getColumnModel().getColumn(2);
			TableColumn colMailLocuteur = tableMessages.getColumnModel().getColumn(3);
			TableColumn colProfilYahooLocuteur = tableMessages.getColumnModel().getColumn(4);
			TableColumn colGroupPostYahooLocuteur = tableMessages.getColumnModel().getColumn(5);
			TableColumn colIdGoogleLocuteur = tableMessages.getColumnModel().getColumn(6);
			TableColumn colFName = tableMessages.getColumnModel().getColumn(7);
			TableColumn colSujetMessage = tableMessages.getColumnModel().getColumn(8);
			TableColumn colFNumDansConversation = tableMessages.getColumnModel().getColumn(9);
			TableColumn colSujetMessageTronque = tableMessages.getColumnModel().getColumn(10);
			
			int widthColIdMessage = 0;
			int widthColDatePost = 120;
			int widthColNomLocuteur = 100;
			int widthColMailLocuteur = 0;
			int widthColProfilYahooLocuteur = 0;
			int widthColGroupPostYahooLocuteur = 0;
			int widthColIdGoogleLocuteur = 0;
			int widthColFName = 150;
			int widthColSujetMessage = 390;
			int widthColFNumDansConversation = 50;
			int widthColSujetMessageTronque = 0;
			
			colIdMessage.setPreferredWidth(widthColIdMessage);
			colIdMessage.setMinWidth(widthColIdMessage);
			colIdMessage.setMaxWidth(widthColIdMessage);
			colDatePost.setPreferredWidth(widthColDatePost);
			colNomLocuteur.setPreferredWidth(widthColNomLocuteur);
			colMailLocuteur.setPreferredWidth(widthColMailLocuteur);
			colMailLocuteur.setMinWidth(widthColMailLocuteur);
			colMailLocuteur.setMaxWidth(widthColMailLocuteur);
			colProfilYahooLocuteur.setPreferredWidth(widthColProfilYahooLocuteur);
			colProfilYahooLocuteur.setMinWidth(widthColProfilYahooLocuteur);
			colProfilYahooLocuteur.setMaxWidth(widthColProfilYahooLocuteur);
			colGroupPostYahooLocuteur.setPreferredWidth(widthColGroupPostYahooLocuteur);
			colGroupPostYahooLocuteur.setMinWidth(widthColGroupPostYahooLocuteur);
			colGroupPostYahooLocuteur.setMaxWidth(widthColGroupPostYahooLocuteur);
			colIdGoogleLocuteur.setPreferredWidth(widthColIdGoogleLocuteur);
			colIdGoogleLocuteur.setMinWidth(widthColIdGoogleLocuteur);
			colIdGoogleLocuteur.setMaxWidth(widthColIdGoogleLocuteur);
			colFName.setPreferredWidth(widthColFName);
			colFName.setPreferredWidth(widthColFName);
			colFName.setMinWidth(widthColFName);
			colSujetMessage.setPreferredWidth(widthColSujetMessage);
			colFNumDansConversation.setPreferredWidth(widthColFNumDansConversation);
			colFNumDansConversation.setPreferredWidth(widthColFNumDansConversation);
			colFNumDansConversation.setMinWidth(widthColFNumDansConversation);
			colSujetMessageTronque.setPreferredWidth(widthColSujetMessageTronque);
			colSujetMessageTronque.setMinWidth(widthColSujetMessageTronque);
			colSujetMessageTronque.setMaxWidth(widthColSujetMessageTronque);			
		}

		// tableMessages.setColumnModel(columnModel);

		// TabMessagesRenderer columnModel = new
		// TabMessagesRenderer(tableMessages);
		// tableMessages.setColumnModel(columnModel);

		// TableRowSorter<TableModel> sorter = new
		// TableRowSorter<TableModel>(
		// tableMessages.getModel());
		// tableMessages.setRowSorter(sorter);
		// // RENDERER BOUTON POUR LA COLONNE 11
		// JButtonEditorVisualiserCorpsMessage jbevcm = new
		// JButtonEditorVisualiserCorpsMessage(
		// new JCheckBox(), projet);
		// tableMessages.getTableHeader().getColumnModel().getColumn(11)
		// .setCellEditor(jbevcm);
		// JButtonRendererVisualiserCorpsMessage jbrvcm = new
		// JButtonRendererVisualiserCorpsMessage();
		// tableMessages.getTableHeader().getColumnModel().getColumn(11)
		// .setCellRenderer(jbrvcm);
		//
		// // RENDERER BOUTON POUR LA COLONNE 12
		// JButtonEditor jbe = new JButtonEditor(new JCheckBox(), projet);
		// tableMessages.getTableHeader().getColumnModel().getColumn(12)
		// .setCellEditor(jbe);
		// JButtonRenderer jbr = new JButtonRenderer();
		// tableMessages.getTableHeader().getColumnModel().getColumn(12)
		// .setCellRenderer(jbr);

		// LARGEUR DES COLONNES
		// Disable auto resizing
		// tableMessages.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// Mouses listeners
		tableMessages.addMouseListener(new MouseListener() {

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
					identifiantMessageToShow = (String) target.getValueAt(row, 0);
//					System.out.println("MessagesPanel - mouseClicked : identifiant to show = " + identifiantMessageToShow);
//					System.out.println("MessagesPanel - mouseClicked : ListeController = " + getListeController());
					getListeController().notifyMessageToShow(identifiantMessageToShow);

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

	private void newFilterMessages() {
		RowFilter<TabMessagesModel, Object> rf = null;
		try {
			String typeFiltre = (String) typesFiltre.getSelectedItem();
			int numCol;
			if (typeFiltre.equals(messagesListe.getString("txt_FiltreDate")))
				numCol = 1;
			else if (typeFiltre.equals(messagesListe.getString("txt_FiltreLocuteur")))
				numCol = 2;
			else if (typeFiltre.equals(messagesListe.getString("txt_FiltreMail")))
				numCol = 3;
			else if (typeFiltre.equals(messagesListe.getString("txt_FiltreSujet")))
				numCol = 7;
			else
				numCol = 9;
			rf = RowFilter.regexFilter("(?i)" + filterText.getText(), numCol);
		} catch (PatternSyntaxException e) {
			return;
		}
		sorterMessages.setRowFilter(rf);
	}

	private void newFilterMessagesForum() {
		RowFilter<TabMessagesForumModel, Object> rf = null;
		try {
			String typeFiltre = (String) typesFiltre.getSelectedItem();
			int numCol;
			if (typeFiltre.equals(messagesListe.getString("txt_FiltreDate")))
				numCol = 1;
			else if (typeFiltre.equals(messagesListe.getString("txt_FiltreLocuteur")))
				numCol = 2;
			else if (typeFiltre.equals(messagesListe.getString("txt_FiltreMail")))
				numCol = 3;
			else if (typeFiltre.equals(messagesListe.getString("txt_FiltreSujet")))
				numCol = 8;
			else
				numCol = 10;
			rf = RowFilter.regexFilter("(?i)" + filterText.getText(), numCol);
		} catch (PatternSyntaxException e) {
			return;
		}
		sorterMessagesForum.setRowFilter(rf);
	}

	private void newFilterMessages(String typeFiltre) {
		RowFilter<TabMessagesModel, Object> rf = null;
		try {
			int numCol;
			if (typeFiltre.equals(messagesListe.getString("txt_FiltreDate")))
				numCol = 1;
			else if (typeFiltre.equals(messagesListe.getString("txt_FiltreLocuteur")))
				numCol = 2;
			else if (typeFiltre.equals(messagesListe.getString("txt_FiltreMail")))
				numCol = 3;
			else if (typeFiltre.equals(messagesListe.getString("txt_FiltreSujet")))
				numCol = 6;
			else
				numCol = 8;
			rf = RowFilter.regexFilter("(?i)" + filterText.getText(), numCol);
		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		sorterMessages.setRowFilter(rf);
	}

	private void newFilterMessagesForum(String typeFiltre) {
		RowFilter<TabMessagesForumModel, Object> rf = null;
		try {
			int numCol;
			if (typeFiltre.equals(messagesListe.getString("txt_FiltreDate")))
				numCol = 1;
			else if (typeFiltre.equals(messagesListe.getString("txt_FiltreLocuteur")))
				numCol = 2;
			else if (typeFiltre.equals(messagesListe.getString("txt_FiltreMail")))
				numCol = 3;
			else if (typeFiltre.equals(messagesListe.getString("txt_FiltreSujet")))
				numCol = 7;
			else
				numCol = 9;
			rf = RowFilter.regexFilter("(?i)" + filterText.getText(), numCol);
		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		sorterMessagesForum.setRowFilter(rf);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Object source = e.getSource();
		// if (source == bExportMess) {
		// System.out.println("export message sélectionné");
		// // System.out.println("nbre messages = "
		// // + listeController.getNbreMessages());
		// // exportPanel = new DialogPanelExportMessages(
		// // getListeController().getSetLocuteurs());
		// // int result = JOptionPane.showOptionDialog(null, exportPanel,
		// // "Exportation de messages",
		// // JOptionPane.OK_CANCEL_OPTION,
		// // JOptionPane.QUESTION_MESSAGE, null, null, null);
		// // if (result == JOptionPane.OK_OPTION) {
		// // exportMessagesCritered();
		// // }
		// }
		JComboBox cb = (JComboBox) e.getSource();
		String typeFiltre = (String) cb.getSelectedItem();
		if (!fromForum)
			newFilterMessages(typeFiltre);
		else
			newFilterMessagesForum(typeFiltre);

	}

	// @Override
	// public void setListeController(ListeController listeController) {
	// System.out
	// .println("MessagesPanel - setListeController : mon controleur passe à "
	// + listeController);
	// this.listeController = listeController;

	// }

	@Override
	public void setIdentifiantMessageToShow(String identifiantMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void listeChanged(ListeChangedEvent event) {
		CardLayout cl = (CardLayout) (cards.getLayout());
		// System.out
		// .println("MessagesPanel - listeChanged : event.getNewNumeroListe() = "
		// + event.getNewNumero());
		if (event.getNewNumero() != 0 && event.getNewMapIdMessages().size() != 0) {
			cl.show(cards, "panel plein");
			ArrayList<MessageModel> listMessages = new ArrayList<MessageModel>(event.getNewMapIdMessages().values());
			fromForum = false;
			for (MessageModel message : listMessages) {
				String fName = message.getFName();
				// System.out.println("LocuteursPanel - listeChanged : fQualiteLocuteur = "+fQualiteLocuteur);
				if (fName != null && !fName.equals("")) {
					fromForum = true;
					break;
				}
			}

			// LABELS
			labNbreMessages.setText(messagesListe.getString("txt_NbreMessagesExtraits") + " : ");
			txtNbreMessages.setText(String.valueOf(event.getNewNbreMessages()));

			labDureeSuivi.setText(messagesListe.getString("txt_DureeSuivi") + " : ");
			txtDureeSuivi.setText(event.getNewDureeSuivi());

			// BOUTONS
			bExportMess.setVisible(true);
			bSelect.setVisible(true);

			// JTABLE
			MessageDateUsComparator comparator = new MessageDateUsComparator();
			Collections.sort(listMessages, comparator);
			if (!fromForum)
				tableMessages = new JTable(new TabMessagesModel(listMessages, messagesListe));
			else
				tableMessages = new JTable(new TabMessagesForumModel(listMessages, messagesListe));
			scrollListMessage.setViewportView(tableMessages);
			applyRenderer();
			panelBas.add(scrollListMessage);
			panel.add(panelFiltre);
		} else {
			cl.show(cards, "panel vide");
		}

	}

	public JPanel getConversationsPanel() {
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

	public JPanel getLocuteursPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocuteursTxtToPdf() {
		// TODO Auto-generated method stub
		return null;
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
