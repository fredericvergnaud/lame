package vue.projetPanelInf;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.PatternSyntaxException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import vue.dialog.DialogPanelSelectedMessages;
//import renderers.TabMessagesRenderer;
import comparators.MessageDateUsComparator;
import modeles.MessageModel;
import modeles.evenements.ListeChangedEvent;
import modeles.tableaux.TabMessagesForumModel;
import modeles.tableaux.TabMessagesModel;
import controleurs.ListeController;
import controleurs.vuesabstraites.ListeView;

public class TabMessagesPanel extends ListeView implements ActionListener {

	private JScrollPane scrollTabMessage;
	private JPanel cards, panel, panelTab, panelFiltre;
	private LamePanel panelVide;
	private JButton
	// bExportMess,
	bSupprimer;
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
	private ResourceBundle ressourcesTabMessages;
	private boolean fromForum;

	public TabMessagesPanel(ListeController listeController, ResourceBundle ressourcesTabMessages) {
		super(listeController);
		// this.listeController = listeController;
		this.ressourcesTabMessages = ressourcesTabMessages;
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

		tableMessages = new JTable(new TabMessagesModel(new ArrayList<MessageModel>(), ressourcesTabMessages));
		scrollTabMessage = new JScrollPane(tableMessages);
		scrollTabMessage.setPreferredSize(new Dimension(650, 300));
		scrollTabMessage.setMinimumSize(new Dimension(650, 300));
		scrollTabMessage.setMaximumSize(new Dimension(6500, 3000));
		panelTab.add(scrollTabMessage);

		panelFiltre = new JPanel();
		panelFiltre.setLayout(new BoxLayout(panelFiltre, BoxLayout.X_AXIS));
		panelFiltre.setAlignmentY(Component.TOP_ALIGNMENT);
		panelFiltre.setPreferredSize(new Dimension(700, 30));
		panelFiltre.setMinimumSize(new Dimension(700, 30));
		panelFiltre.setMaximumSize(new Dimension(7000, 30));

		JLabel labFiltre = new JLabel(ressourcesTabMessages.getString("txt_Filtre") + " : ", SwingConstants.LEFT);
		panelFiltre.add(labFiltre);

		String[] filterTypes = { ressourcesTabMessages.getString("txt_FiltreDate"), ressourcesTabMessages.getString("txt_FiltreLocuteur"),
				// messagesListe.getString("txt_FiltreMail"),
				ressourcesTabMessages.getString("txt_FiltreSujet")
		// , messagesListe.getString("txt_FiltreNumeroMessage")
		};
		typesFiltre = new JComboBox(filterTypes);
		typesFiltre.setPreferredSize(new Dimension(200, 30));
		typesFiltre.setMinimumSize(new Dimension(200, 30));
		typesFiltre.setMaximumSize(new Dimension(200, 30));
		typesFiltre.setSelectedIndex(1);
		typesFiltre.addActionListener(this);
		panelFiltre.add(typesFiltre);

		filterText = new JTextField();
		// filterText.setPreferredSize(new Dimension(500, 30));
		// filterText.setMinimumSize(new Dimension(500, 30));
		// filterText.setMaximumSize(new Dimension(500, 30));
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

		// BOUTONS SELECTION ET SUPPRESSION

		bSupprimer = new JButton(ressourcesTabMessages.getString("txt_ButtonSelectionMessages"));
		bSupprimer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] tabSelection = tableMessages.getSelectedRows();
				if (tabSelection.length > 0) {
					List<String> listSelectedIdMessages = new ArrayList<String>();
					for (int i = 0; i < tabSelection.length; i++) {
						int selectedViewRow = tabSelection[i];
						// System.out.println("selectedViewRow = " +
						// selectedViewRow);
						int selectedModelRow = tableMessages.convertRowIndexToModel(selectedViewRow);
						// System.out.println("selectedModelRow = "
						// + selectedModelRow);
						// System.out.println("Message id " +
						// tableMessages.getModel().getValueAt(selectedModelRow,
						// 0));
						listSelectedIdMessages.add((String) tableMessages.getModel().getValueAt(selectedModelRow, 0));
					}

					DialogPanelSelectedMessages selectPanel = new DialogPanelSelectedMessages(ressourcesTabMessages);
					int result = JOptionPane.showOptionDialog(null, selectPanel, ressourcesTabMessages.getString("txt_MessagesSelectionnes"), JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE, null, null, null);
					if (result == JOptionPane.OK_OPTION) {
						if (selectPanel.getRadioExport().isSelected()) {
							System.out.println("TabMessagesPanel - getPanel() : Creer liste selected");
							getListeController().getProjetController().notifyProjetCreateListFromSelectedMessages(getListeController().getListeSelected(),listSelectedIdMessages);
						} else {
							int diag = JOptionPane.showOptionDialog(null, ressourcesTabMessages.getString("txt_PoursuivreSuppressionMessages") + " " + tabSelection.length + " messages ?",
									ressourcesTabMessages.getString("txt_SuppressionMessages"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
							if (diag == JOptionPane.YES_OPTION) {
								getListeController().notifyDeleteSelectedMessages(listSelectedIdMessages);
							}
						}
					}
				} else
					JOptionPane.showMessageDialog(null, ressourcesTabMessages.getString("txt_VeuillezSelectionnerMessages"), ressourcesTabMessages.getString("txt_SelectionMessages"),
							JOptionPane.ERROR_MESSAGE);
			}
		});
		panelFiltre.add(bSupprimer);

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
			ArrayList<MessageModel> listMessages = new ArrayList<MessageModel>(getListeController().getListeSelected().getMapIdMessage().values());
			fromForum = false;
			for (MessageModel message : listMessages) {
				String fName = message.getFName();
				// System.out.println("LocuteursPanel - listeChanged : fQualiteLocuteur = "+fQualiteLocuteur);
				if (fName != null && !fName.equals("")) {
					fromForum = true;
					break;
				}
			}

			// BOUTONS
			// bExportMess.setVisible(true);
			// bSelect.setVisible(true);

			// JTABLE
			MessageDateUsComparator comparator = new MessageDateUsComparator();
			Collections.sort(listMessages, comparator);
			if (!fromForum)
				tableMessages = new JTable(new TabMessagesModel(listMessages, ressourcesTabMessages));
			else
				tableMessages = new JTable(new TabMessagesForumModel(listMessages, ressourcesTabMessages));
			scrollTabMessage.setViewportView(tableMessages);
			applyRenderer();
		}
		return cards;
	}

	@Override
	public JPanel getFilsListePanel() {
		return null;
	}

	@Override
	public JPanel getTabMessagesPanel() {
		return getPanel();
	}

	public void applyRenderer() {
		// GENERAL
		tableMessages.setAutoCreateRowSorter(true);
		tableMessages.setRowSelectionAllowed(true);
		tableMessages.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableMessages.setFillsViewportHeight(true);
		tableMessages.setOpaque(true);
		tableMessages.setBackground(Color.WHITE);

		// DateRenderer
		tableMessages.setDefaultRenderer(Date.class, new DateRenderer());
		// tcol = tableMessages.getColumnModel().getColumn(0);
		// tcol.setCellRenderer(new TabMessagesRenderer());

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
			colFName.setMinWidth(widthColFName);
			colSujetMessage.setPreferredWidth(widthColSujetMessage);
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
					getListeController().notifyMessageToShow(identifiantMessageToShow);

				}

			}
		});
	}

	private void newFilterMessages() {
		RowFilter<TabMessagesModel, Object> rf = null;
		try {
			String typeFiltre = (String) typesFiltre.getSelectedItem();
			int numCol;
			if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreDate")))
				numCol = 1;
			else if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreLocuteur")))
				numCol = 2;
			else if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreMail")))
				numCol = 3;
			else if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreSujet")))
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
			if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreDate")))
				numCol = 1;
			else if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreLocuteur")))
				numCol = 2;
			else if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreMail")))
				numCol = 3;
			else if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreSujet")))
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
			if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreDate")))
				numCol = 1;
			else if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreLocuteur")))
				numCol = 2;
			else if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreMail")))
				numCol = 3;
			else if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreSujet")))
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
			if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreDate")))
				numCol = 1;
			else if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreLocuteur")))
				numCol = 2;
			else if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreMail")))
				numCol = 3;
			else if (typeFiltre.equals(ressourcesTabMessages.getString("txt_FiltreSujet")))
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

	@Override
	public void setIdentifiantMessageToShow(String identifiantMessage) {
		// TODO Auto-generated method stub
	}

	@Override
	public void listeChanged(ListeChangedEvent event) {
		// System.out.println("TabMessagesPanel - listeChanged : event.getNewNumeroListe() = "
		// + event.getNewNumero());
		CardLayout cl = (CardLayout) (cards.getLayout());
		if (event.getNewNumero() != 0 && event.getNewMapIdMessages().size() != 0) {
			cl.show(cards, "PANEL_PLEIN");
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

			// BOUTONS
			// bExportMess.setVisible(true);
			// bSelect.setVisible(true);

			// JTABLE
			MessageDateUsComparator comparator = new MessageDateUsComparator();
			Collections.sort(listMessages, comparator);
			if (!fromForum)
				tableMessages = new JTable(new TabMessagesModel(listMessages, ressourcesTabMessages));
			else
				tableMessages = new JTable(new TabMessagesForumModel(listMessages, ressourcesTabMessages));
			scrollTabMessage.setViewportView(tableMessages);
			applyRenderer();
		} else {
			cl.show(cards, "PANEL_VIDE");
		}
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
	public JToolBar getToolBar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getTabLocuteursPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
