package vue.projetPanelInf;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import modeles.ConversationModel;
import modeles.LocuteurModel;
import modeles.MessageModel;
import modeles.evenements.ListeChangedEvent;
import modeles.trees.FilsTreeModel;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import charts.ChartNbreMessagesDate;
import renderers.CustomMultiXYPlotBoxPanel;
import renderers.CustomStatBoxPanel;
import renderers.DateRenderer;
import renderers.RoundedPanel;
import vue.ToolBar;
import vue.dialog.DialogPanelInfoConversation;
import vue.dialog.DialogPanelInfoLocuteur;
import controleurs.ListeController;
import controleurs.vuesabstraites.ListeView;

public class FilsListePanel extends ListeView implements TreeSelectionListener, ItemListener {

	private JLabel labFrom, txtFrom, labSujet, txtSujet, labDate, txtDate;
	private CustomStatBoxPanel box1, box2, box3, box4, box5, box6, box7, box8, box9;
	private CustomMultiXYPlotBoxPanel chart1;
	private JPanel cards, panel, panelGaucheHaut, panelGaucheBas, panelChk, panelGraphs;
	private LamePanel panelVide;
	private JXTreeTable xTreeTable;
	private DateFormat format = new SimpleDateFormat("dd/MM/yyyy H:mm");
	private JTextPane detailMF;
	private JScrollPane scrollFils, scrollDetailMF;
	private JCheckBox chkOriginalMessage;
	private MessageModel messageSelected;
	private StyledDocument doc;
	private JSplitPane scrollSplitPane;
	private FilsTreeModel filsTreeModel;
	private TreePath treePathToSelect;
	private ResourceBundle filsListe;
	private int colIndexSorted = 999;
	private boolean colIndexSortedAsc, colIndexSortedDesc;
	private Map<String, MessageModel> mapIdMessages;
	private Object nodeSelected;
	private Font gras, normal;
	private CardLayout cl;
	private JButton bMois;
	private DecimalFormat df = new DecimalFormat("0.00");

	public FilsListePanel(ListeController listeController, ResourceBundle filsListe) {
		super(listeController);
		format.setLenient(true);
		this.filsListe = filsListe;
	}

	public JPanel getPanel() {
		int largeurLabel = 70;
		int hauteurLabel = 15;
		int largeurTxtLabel = 530;
		int hauteurTxtLabel = 15;
		Dimension dimLabel = new Dimension(largeurLabel, hauteurLabel);
		Dimension dimLabelTxt = new Dimension(largeurTxtLabel, hauteurTxtLabel);
		gras = new Font("sansserif", Font.BOLD, 12);
		normal = new Font("sansserif", Font.PLAIN, 12);

		panel = new JPanel();
		panel.setOpaque(true);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setPreferredSize(new Dimension(1300, 750));
		panel.setMinimumSize(new Dimension(1300, 750));
		panel.setMaximumSize(new Dimension(1300, 750));
		panel.setBackground(Color.WHITE);
		// panel.setBorder(new TitledBorder(""));
		panel.setName("PANEL_PLEIN");

		RoundedPanel panelGauche = new RoundedPanel();
		panelGauche.setPreferredSize(new Dimension(645, 750));
		panelGauche.setMinimumSize(new Dimension(645, 750));
		panelGauche.setMaximumSize(new Dimension(645, 750));
		panelGauche.setBackground(new Color(248, 248, 248));
		GridBagConstraints gbcGauche = new GridBagConstraints();

		RoundedPanel panelDroite = new RoundedPanel();
		panelDroite.setPreferredSize(new Dimension(665, 750));
		panelDroite.setMinimumSize(new Dimension(665, 750));
		panelDroite.setMaximumSize(new Dimension(665, 750));
		panelDroite.setAlignmentY(Component.TOP_ALIGNMENT);
		panelDroite.setAlignmentX(Component.CENTER_ALIGNMENT);

		panelGaucheHaut = new JPanel(new GridBagLayout());
		panelGaucheHaut.setPreferredSize(new Dimension(620, 50));
		panelGaucheHaut.setMinimumSize(new Dimension(620, 50));
		panelGaucheHaut.setMaximumSize(new Dimension(620, 50));
		panelGaucheHaut.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelGaucheHaut.setAlignmentY(Component.TOP_ALIGNMENT);
		panelGaucheHaut.setBackground(new Color(248, 248, 248));

		panelGaucheBas = new JPanel();
		panelGaucheBas.setLayout(new BoxLayout(panelGaucheBas, BoxLayout.Y_AXIS));
		panelGaucheBas.setPreferredSize(new Dimension(620, 670));
		panelGaucheBas.setMinimumSize(new Dimension(620, 670));
		panelGaucheBas.setMaximumSize(new Dimension(620, 670));
		panelGaucheBas.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelGaucheBas.setBackground(new Color(248, 248, 248));

		GridBagConstraints cHaut = new GridBagConstraints();

		JPanel panel1 = new JPanel(new GridBagLayout());
		panel1.setBackground(new Color(248, 248, 248));
		GridBagConstraints c1 = new GridBagConstraints();

		labFrom = new JLabel("", SwingConstants.LEFT);
		labFrom.setPreferredSize(dimLabel);
		labFrom.setMinimumSize(dimLabel);
		labFrom.setPreferredSize(dimLabel);
		labFrom.setFont(normal);
		txtFrom = new JLabel("", SwingConstants.LEFT);
		txtFrom.setPreferredSize(dimLabelTxt);
		txtFrom.setMinimumSize(dimLabelTxt);
		txtFrom.setMaximumSize(dimLabelTxt);
		txtFrom.setFont(gras);

		c1.gridx = 0;
		c1.gridy = 0;
		panel1.add(labFrom, c1);
		c1.gridx = 1;
		c1.gridy = 0;
		panel1.add(txtFrom, c1);

		GridBagLayout l2 = new GridBagLayout();
		JPanel panel2 = new JPanel();
		panel2.setLayout(l2);
		panel2.setBackground(new Color(248, 248, 248));
		GridBagConstraints c2 = new GridBagConstraints();

		labSujet = new JLabel("", SwingConstants.LEFT);
		labSujet.setPreferredSize(dimLabel);
		labSujet.setMinimumSize(dimLabel);
		labSujet.setMaximumSize(dimLabel);
		labSujet.setFont(normal);
		txtSujet = new JLabel("", SwingConstants.LEFT);
		txtSujet.setPreferredSize(dimLabelTxt);
		txtSujet.setMinimumSize(dimLabelTxt);
		txtSujet.setMaximumSize(dimLabelTxt);
		txtSujet.setFont(gras);

		c2.gridx = 0;
		c2.gridy = 0;
		panel2.add(labSujet, c2);
		c2.gridx = 1;
		c2.gridy = 0;
		panel2.add(txtSujet, c2);

		GridBagLayout l3 = new GridBagLayout();
		JPanel panel3 = new JPanel();
		panel3.setLayout(l3);
		panel3.setBackground(new Color(248, 248, 248));
		GridBagConstraints c3 = new GridBagConstraints();

		labDate = new JLabel("", SwingConstants.LEFT);
		labDate.setPreferredSize(dimLabel);
		labDate.setMinimumSize(dimLabel);
		labDate.setMaximumSize(dimLabel);
		labDate.setFont(normal);
		txtDate = new JLabel("", SwingConstants.LEFT);
		txtDate.setPreferredSize(dimLabelTxt);
		txtDate.setMinimumSize(dimLabelTxt);
		txtDate.setMaximumSize(dimLabelTxt);
		txtDate.setFont(gras);

		c3.gridx = 0;
		c3.gridy = 0;
		panel3.add(labDate, c3);
		c3.gridx = 1;
		c3.gridy = 0;
		panel3.add(txtDate, c3);

		cHaut.gridx = 0;
		cHaut.gridy = 0;
		panelGaucheHaut.add(panel1, cHaut);
		cHaut.gridx = 0;
		cHaut.gridy = 1;
		panelGaucheHaut.add(panel2, cHaut);
		cHaut.gridx = 0;
		cHaut.gridy = 2;
		panelGaucheHaut.add(panel3, cHaut);

		// JXTREETABLE
		xTreeTable = new JXTreeTable(new FilsTreeModel(filsListe));
		xTreeTable.addTreeSelectionListener(this);
		xTreeTable.setRootVisible(false);
		xTreeTable.setDefaultRenderer(Date.class, new DateRenderer());

		// HEADERS : renderer et mouse listener (pour sorting)

		final TableCellRenderer r = xTreeTable.getTableHeader().getDefaultRenderer();
		TableCellRenderer wrapper = new TableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component comp = r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (comp instanceof JLabel) {
					JLabel label = (JLabel) comp;
					label.setIcon(getSortIcon(table, column));
				}
				return comp;
			}

			private Icon getSortIcon(JTable table, int column) {
				if (column == colIndexSorted)
					if (colIndexSortedAsc)
						return UIManager.getIcon("Table.ascendingSortIcon");
					else
						return UIManager.getIcon("Table.descendingSortIcon");
				else
					return null;
			}
		};

		xTreeTable.getTableHeader().setDefaultRenderer(wrapper);
		xTreeTable.getTableHeader().addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int colIndexClicked = xTreeTable.getColumnModel().getColumnIndexAtX(e.getX());

				if (colIndexClicked != colIndexSorted) {
					colIndexSorted = colIndexClicked;
					colIndexSortedAsc = true;
					colIndexSortedDesc = false;
				} else {
					if (colIndexSortedAsc) {
						colIndexSortedAsc = false;
						colIndexSortedDesc = true;
					} else {
						colIndexSortedAsc = true;
						colIndexSortedDesc = false;
					}
				}
				filsTreeModel = new FilsTreeModel(mapIdMessages, filsListe, colIndexClicked, colIndexSortedAsc);
				xTreeTable.setTreeTableModel(filsTreeModel);
				applyRenderer();
				if (xTreeTable.getSelectedRow() != 0 && messageSelected != null)
					setIdentifiantMessageToShow(messageSelected.getIdentifiant());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

		// CELL SELECTION
		xTreeTable.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int colViewClicked = xTreeTable.getSelectedColumn();
					if (nodeSelected instanceof DefaultMutableTreeTableNode) {
						Object userObject = ((DefaultMutableTreeTableNode) nodeSelected).getUserObject();
						if (userObject instanceof MessageModel) {
							messageSelected = (MessageModel) userObject;
							System.out.println("FilsListePanel - clicked cell listener : message id = " + messageSelected.getIdentifiant() + " | conversation id = "
									+ messageSelected.getIdConversation() + " | locuteur id = " + messageSelected.getIdLocuteur());
							Object[] options = { "OK" };
							if (colViewClicked == 0) {
								Set<ConversationModel> setConversations = getListeController().getListeSelected().getSetConversations();
								for (ConversationModel conversation : setConversations)
									if (conversation.getId() == messageSelected.getIdConversation()) {
										DialogPanelInfoConversation optPanel = new DialogPanelInfoConversation(conversation);
										JOptionPane.showOptionDialog(null, optPanel, "Conversation numéro " + conversation.getId(), JOptionPane.CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
												options, options[0]);
										break;
									}
							} else if (colViewClicked == 1) {
								Set<LocuteurModel> setLocuteurs = getListeController().getSetLocuteurs();
								for (LocuteurModel locuteur : setLocuteurs)
									if (locuteur.getId() == messageSelected.getIdLocuteur()) {
										DialogPanelInfoLocuteur optPanel = new DialogPanelInfoLocuteur(locuteur);
										JOptionPane.showOptionDialog(null, optPanel, "Locuteur " + locuteur.getNom(), JOptionPane.CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options,
												options[0]);
										break;
									}
							}
						}
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});

		scrollFils = new JScrollPane();

		detailMF = new JTextPane();
		detailMF.setContentType("text/html");
		doc = detailMF.getStyledDocument();
		addStylesToDocument(doc);
		detailMF.setEditable(false);

		scrollDetailMF = new JScrollPane();

		// SPLITPANE BAS
		scrollSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollFils, scrollDetailMF);
		scrollSplitPane.setDividerLocation(300);
		scrollSplitPane.setOneTouchExpandable(true);
		scrollSplitPane.setBackground(new Color(248, 248, 248));

		panelGaucheBas.add(scrollSplitPane);

		panelChk = new JPanel();
		panelChk.setLayout(new BoxLayout(panelChk, BoxLayout.X_AXIS));
		panelChk.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelChk.setBackground(new Color(248, 248, 248));

		chkOriginalMessage = new JCheckBox(filsListe.getString("txt_EnleverMessageOrigine"));
		chkOriginalMessage.addItemListener(this);
		chkOriginalMessage.setSelected(false);

		panelChk.add(chkOriginalMessage);

		gbcGauche.gridx = 0;
		gbcGauche.gridy = 0;
		gbcGauche.weightx = 1.0;
		gbcGauche.anchor = GridBagConstraints.NORTH;
		panelGauche.add(panelGaucheHaut, gbcGauche);
		gbcGauche.gridx = 0;
		gbcGauche.gridy = 1;
		gbcGauche.weightx = 10.0;
		gbcGauche.anchor = GridBagConstraints.NORTH;
		panelGauche.add(panelGaucheBas, gbcGauche);
		gbcGauche.gridx = 0;
		gbcGauche.gridy = 2;
		gbcGauche.weightx = 1.0;
		gbcGauche.anchor = GridBagConstraints.NORTH;
		panelGauche.add(panelChk, gbcGauche);

		// PANEL DROITE

		// BOXES
		JPanel panelBoxes = new JPanel();
		panelBoxes.setLayout(new GridBagLayout());
		panelBoxes.setOpaque(true);
		panelBoxes.setAlignmentY(Component.TOP_ALIGNMENT);
		panelBoxes.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelBoxes.setBackground(Color.WHITE);
		panelBoxes.setPreferredSize(new Dimension(630, 735));
		panelBoxes.setMinimumSize(new Dimension(630, 735));
		panelBoxes.setMaximumSize(new Dimension(630, 735));
		GridBagConstraints gbc = new GridBagConstraints();
		// gbc.fill = GridBagConstraints.NONE;

		JPanel panel4 = new JPanel(new GridBagLayout());
		panel4.setBackground(Color.WHITE);
		panel4.setAlignmentY(Component.TOP_ALIGNMENT);
		panel4.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel4.setPreferredSize(new Dimension(480, 120));
		panel4.setMinimumSize(new Dimension(480, 120));
		panel4.setMaximumSize(new Dimension(480, 120));
		GridBagConstraints gbc4 = new GridBagConstraints();

		// NOMBRE DE MESSAGES
		box1 = new CustomStatBoxPanel("<html><center>" + filsListe.getString("txt_Messages") + "</center></html>", new ImageIcon(ToolBar.class.getResource("/images/icones/email_22.png")));

		gbc4.gridx = 0;
		gbc4.gridy = 0;
		gbc4.weightx = 1.0;
		gbc4.anchor = GridBagConstraints.WEST;
		panel4.add(box1, gbc4);

		box2 = new CustomStatBoxPanel("<html><center>" + filsListe.getString("txt_Suivi") + "</center></html>", new ImageIcon(ToolBar.class.getResource("/images/icones/duree_22.png")));
		box2.getTxtLabel().setFont(new Font("sansserif", Font.BOLD, 13));
		box2.resize(300, 105, 300, 45, 300, 60);

		gbc4.gridx = 1;
		gbc4.gridy = 0;
		gbc4.weightx = 1.0;
		gbc4.anchor = GridBagConstraints.WEST;
		panel4.add(box2, gbc4);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		panelBoxes.add(panel4, gbc);

		// BOUTONS

		JPanel panelButtons = new JPanel(new GridBagLayout());
		panelButtons.setBackground(Color.WHITE);
		panelButtons.setAlignmentY(Component.TOP_ALIGNMENT);
		panelButtons.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelButtons.setPreferredSize(new Dimension(130, 240));
		panelButtons.setMinimumSize(new Dimension(130, 240));
		panelButtons.setMaximumSize(new Dimension(130, 240));
		GridBagConstraints gbcButtons = new GridBagConstraints();
		gbcButtons.insets = new Insets(15, 0, 15, 0);

		final JButton bTabMessages = new JButton("<html><center>" + filsListe.getString("txt_TableauMessages2Lignes") + "</center></html>");
		bTabMessages.setPreferredSize(new Dimension(130, 50));
		bTabMessages.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frameTabMessages = new JFrame();
				frameTabMessages.setLayout(new BorderLayout());
				frameTabMessages.setContentPane(getListeController().getPanelTabMessagesListeView().getTabMessagesPanel());
				frameTabMessages.setSize(new Dimension(700, 400));
				frameTabMessages.setTitle(filsListe.getString("txt_TableauMessages"));

				frameTabMessages.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent windowEvent) {
						bTabMessages.setEnabled(true);
					}
				});
				bTabMessages.setEnabled(false);
				frameTabMessages.setVisible(true);
			}
		});

		final JButton bTabLocuteurs = new JButton("<html><center>" + filsListe.getString("txt_TableauLocuteurs2Lignes") + "</center></html>");
		bTabLocuteurs.setPreferredSize(new Dimension(130, 50));
		bTabLocuteurs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frameTabLocuteurs = new JFrame();
				frameTabLocuteurs.setLayout(new BorderLayout());
				frameTabLocuteurs.setContentPane(getListeController().getPanelTabLocuteursListeView().getTabLocuteursPanel());
				frameTabLocuteurs.setSize(new Dimension(700, 400));
				frameTabLocuteurs.setTitle(filsListe.getString("txt_TableauLocuteurs"));

				frameTabLocuteurs.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent windowEvent) {
						bTabLocuteurs.setEnabled(true);
					}
				});
				bTabLocuteurs.setEnabled(false);
				frameTabLocuteurs.setVisible(true);
			}
		});

		final JButton bTabConversations = new JButton("<html><center>" + filsListe.getString("txt_TableauConversations2Lignes") + "</center></html>");
		bTabConversations.setPreferredSize(new Dimension(130, 50));
		bTabConversations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame frameTabConversations = new JFrame();
				frameTabConversations.setLayout(new BorderLayout());
				frameTabConversations.setContentPane(getListeController().getPanelTabConversationsListeView().getTabConversationsPanel());
				frameTabConversations.setSize(new Dimension(700, 400));
				frameTabConversations.setTitle(filsListe.getString("txt_TableauConversations"));

				frameTabConversations.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent windowEvent) {
						bTabConversations.setEnabled(true);
					}
				});
				bTabConversations.setEnabled(false);
				frameTabConversations.setVisible(true);
			}
		});

		gbcButtons.gridx = 0;
		gbcButtons.gridy = 0;
		gbcButtons.weightx = 1.0;
		gbcButtons.weighty = 1.0;
		gbcButtons.anchor = GridBagConstraints.NORTH;
		panelButtons.add(bTabMessages, gbcButtons);

		gbcButtons.gridx = 0;
		gbcButtons.gridy = 1;
		gbcButtons.weightx = 1.0;
		gbcButtons.weighty = 1.0;
		gbcButtons.anchor = GridBagConstraints.CENTER;
		panelButtons.add(bTabLocuteurs, gbcButtons);

		gbcButtons.gridx = 0;
		gbcButtons.gridy = 2;
		gbcButtons.weightx = 1.0;
		gbcButtons.weighty = 1.0;
		gbcButtons.anchor = GridBagConstraints.SOUTH;
		panelButtons.add(bTabConversations, gbcButtons);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 10.0;
		gbc.gridheight = 2;
		gbc.anchor = GridBagConstraints.NORTH;
		panelBoxes.add(panelButtons, gbc);

		JPanel panel5 = new JPanel(new GridBagLayout());
		panel5.setBackground(Color.WHITE);
		panel5.setAlignmentY(Component.TOP_ALIGNMENT);
		panel5.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel5.setPreferredSize(new Dimension(480, 120));
		panel5.setMinimumSize(new Dimension(480, 120));
		panel5.setMaximumSize(new Dimension(480, 120));
		GridBagConstraints gbc5 = new GridBagConstraints();

		// NOMBRE DE LOCUTEURS
		box3 = new CustomStatBoxPanel(filsListe.getString("txt_NbreLocuteurs"), new ImageIcon(ToolBar.class.getResource("/images/icones/locuteurs_22.png")));

		gbc5.gridx = 0;
		gbc5.gridy = 0;
		gbc5.weightx = 1.0;
		gbc5.anchor = GridBagConstraints.WEST;
		panel5.add(box3, gbc5);

		box4 = new CustomStatBoxPanel("<html><center>" + filsListe.getString("txt_NbreMoyenMessagesLocuteursMois") + "</center></html>", new ImageIcon(
				ToolBar.class.getResource("/images/icones/email_locuteurs_22.png")));
		box4.resize(300, 105, 300, 45, 300, 60);

		gbc5.gridx = 1;
		gbc5.gridy = 0;
		gbc5.weightx = 1.0;
		gbc5.anchor = GridBagConstraints.WEST;
		panel5.add(box4, gbc5);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.NORTH;
		panelBoxes.add(panel5, gbc);

		// CONVERSATIONS
		JPanel panel6 = new JPanel(new GridBagLayout());
		panel6.setBackground(Color.WHITE);
		panel6.setAlignmentY(Component.TOP_ALIGNMENT);
		panel6.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel6.setPreferredSize(new Dimension(630, 125));
		panel6.setMinimumSize(new Dimension(630, 125));
		panel6.setMaximumSize(new Dimension(630, 125));
		GridBagConstraints gbc6 = new GridBagConstraints();

		// PARAMETRE REGROUPEMENT DES MESSAGES
		box5 = new CustomStatBoxPanel("<html><center>" + filsListe.getString("txt_ParamRegroupement") + "</center></html>", new ImageIcon(ToolBar.class.getResource("/images/icones/grouping_22.png")));
		box5.getTxtLabel().setFont(new Font("sansserif", Font.BOLD, 10));
		box5.resize(210, 105, 210, 45, 210, 60);

		gbc6.gridx = 0;
		gbc6.gridy = 0;
		gbc6.weightx = 1.0;
		gbc6.anchor = GridBagConstraints.WEST;
		panel6.add(box5, gbc6);

		// NBRE DE CONVERSATIONS
		box6 = new CustomStatBoxPanel("<html><center>" + filsListe.getString("txt_NbreConversations") + "</center></html>", new ImageIcon(
				ToolBar.class.getResource("/images/icones/conversations_22.png")));

		gbc6.gridx = 1;
		gbc6.gridy = 0;
		gbc6.weightx = 1.0;
		gbc6.anchor = GridBagConstraints.WEST;
		panel6.add(box6, gbc6);

		// NBRE MOYEN DE LOCUTEURS PAR CONVERSATIONS
		box7 = new CustomStatBoxPanel("<html><center>" + filsListe.getString("txt_NbreMoyenLocuteursSujet") + "</center></html>", new ImageIcon(
				ToolBar.class.getResource("/images/icones/conversations_locuteurs_22.png")));

		gbc6.gridx = 2;
		gbc6.gridy = 0;
		gbc6.weightx = 1.0;
		gbc6.anchor = GridBagConstraints.WEST;
		panel6.add(box7, gbc6);

		// NBRE MOYEN DE MESSAGES PAR CONVERSATIONS
		box8 = new CustomStatBoxPanel("<html><center>" + filsListe.getString("txt_NbreMoyenMessagesSujet") + "</center></html>", new ImageIcon(
				ToolBar.class.getResource("/images/icones/conversations_messages_22.png")));

		gbc6.gridx = 3;
		gbc6.gridy = 0;
		gbc6.weightx = 1.0;
		gbc6.anchor = GridBagConstraints.WEST;
		panel6.add(box8, gbc6);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1.0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		panelBoxes.add(panel6, gbc);

		// EVOLUTION DES MESSAGES
		JPanel panel7 = new JPanel(new GridBagLayout());
		panel7.setBackground(Color.WHITE);
		panel7.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		panel7.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel7.setPreferredSize(new Dimension(620, 360));
		panel7.setMinimumSize(new Dimension(620, 360));
		panel7.setMaximumSize(new Dimension(620, 360));
		GridBagConstraints gbc7 = new GridBagConstraints();
		// panel7.setBorder(new TitledBorder(""));

		// BOUTONS
		final JButton bJours = new JButton(filsListe.getString("txt_ParJours"));
		bJours.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout clGraphs = (CardLayout) (panelGraphs.getLayout());
				JPanel p = getChartPanel(1);
				((FlowLayout) p.getLayout()).setVgap(0);
				panelGraphs.add(p, "PANEL_GRAPH_JOURS");
				clGraphs.show(panelGraphs, "PANEL_GRAPH_JOURS");
			}
		});
		bMois = new JButton(filsListe.getString("txt_ParMois"));
		bMois.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout clGraphs = (CardLayout) (panelGraphs.getLayout());
				JPanel p = getChartPanel(2);
				((FlowLayout) p.getLayout()).setVgap(0);
				panelGraphs.add(p, "PANEL_GRAPH_MOIS");
				clGraphs.show(panelGraphs, "PANEL_GRAPH_MOIS");
				// bMois.requestFocus();
			}
		});
		final JButton bAnnee = new JButton(filsListe.getString("txt_ParAnnees"));

		bAnnee.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout clGraphs = (CardLayout) (panelGraphs.getLayout());
				JPanel p = getChartPanel(3);
				((FlowLayout) p.getLayout()).setVgap(0);
				panelGraphs.add(p, "PANEL_GRAPH_ANNEE");
				clGraphs.show(panelGraphs, "PANEL_GRAPH_ANNEE");
				// bAnnee.requestFocus();
			}
		});

		chart1 = new CustomMultiXYPlotBoxPanel("<html><center>" + filsListe.getString("txt_EvolutionMessages") + "</center></html>", new ImageIcon(
				ToolBar.class.getResource("/images/icones/evolution_messages_22.png")));
		GridBagConstraints gbcGraphPanel = chart1.getGbc();
		panelGraphs = chart1.getGraphPanel();
		panelGraphs.add(new JPanel(), "PANEL_GRAPH_JOURS");
		panelGraphs.add(new JPanel(), "PANEL_GRAPH_MOIS");
		panelGraphs.add(new JPanel(), "PANEL_GRAPH_ANNEES");

		JPanel panelButtonsEvolutionMessages = new JPanel(new GridBagLayout());
		panelButtonsEvolutionMessages.setBackground(new Color(248, 248, 248));
		panelButtonsEvolutionMessages.setAlignmentY(Component.CENTER_ALIGNMENT);
		panelButtonsEvolutionMessages.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelButtonsEvolutionMessages.setPreferredSize(new Dimension(300, 40));
		panelButtonsEvolutionMessages.setMinimumSize(new Dimension(300, 40));
		panelButtonsEvolutionMessages.setMaximumSize(new Dimension(300, 40));
		GridBagConstraints gbcButtonsEvolutionMessages = new GridBagConstraints();

		gbcButtonsEvolutionMessages.gridx = 0;
		gbcButtonsEvolutionMessages.gridy = 0;
		gbcButtonsEvolutionMessages.weightx = 1.0;
		gbcButtonsEvolutionMessages.anchor = GridBagConstraints.WEST;
		panelButtonsEvolutionMessages.add(bJours, gbcButtonsEvolutionMessages);

		gbcButtonsEvolutionMessages.gridx = 1;
		gbcButtonsEvolutionMessages.gridy = 0;
		gbcButtonsEvolutionMessages.weightx = 1.0;
		gbcButtonsEvolutionMessages.anchor = GridBagConstraints.WEST;
		panelButtonsEvolutionMessages.add(bMois, gbcButtonsEvolutionMessages);

		gbcButtonsEvolutionMessages.gridx = 2;
		gbcButtonsEvolutionMessages.gridy = 0;
		gbcButtonsEvolutionMessages.weightx = 1.0;
		gbcButtonsEvolutionMessages.anchor = GridBagConstraints.WEST;
		panelButtonsEvolutionMessages.add(bAnnee, gbcButtonsEvolutionMessages);

		gbcGraphPanel.gridx = 1;
		gbcGraphPanel.gridy = 0;
		gbcGraphPanel.anchor = GridBagConstraints.WEST;
		chart1.add(panelButtonsEvolutionMessages, gbcGraphPanel);

		gbc7.gridx = 0;
		gbc7.gridy = 0;
		gbc7.weightx = 1.0;
		// gbc7.insets = new Insets(20, 0, 0, 0);
		gbc7.anchor = GridBagConstraints.SOUTH;
		panel7.add(chart1, gbc7);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, 2, 5, 0);
		gbc.weighty = 10.0;
		gbc.anchor = GridBagConstraints.SOUTHWEST;
		panelBoxes.add(panel7, gbc);

		panelDroite.add(panelBoxes);

		panel.add(panelGauche);
		panel.add(panelDroite);

		// SCROLL PANEL
		JScrollPane scrollPanel = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPanel.setBorder(null);

		// CardLayout
		panelVide = new LamePanel();
		panelVide.setName("PANEL_VIDE");
		// panelVide.setBorder(new
		// TitledBorder(filsListe.getString("txt_StructureListe")));
		cards = new JPanel(new CardLayout());
		cards.add(scrollPanel, panel.getName());
		cards.add(panelVide.getPanel(), panelVide.getName());
		cl = (CardLayout) (cards.getLayout());
		cl.show(cards, "PANEL_VIDE");
		// cl.show(cards, "PANEL_PLEIN");
		return cards;
	}

	private JPanel getChartPanel(int typeGraph) {
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout());
		panel1.setAlignmentY(Component.CENTER_ALIGNMENT);

		ChartNbreMessagesDate chartsNbreMessagesDate = new ChartNbreMessagesDate(mapIdMessages, typeGraph, filsListe);
		panel1.add(chartsNbreMessagesDate.getNbreMessagesAnneeChartPanel());

		return panel1;
	}

	private void applyRenderer() {
		xTreeTable.setOpaque(true);
		xTreeTable.setBackground(Color.WHITE);

		TableColumn colSujet = xTreeTable.getColumnModel().getColumn(0);
		TableColumn colExpediteur = xTreeTable.getColumnModel().getColumn(1);
		TableColumn colDate = xTreeTable.getColumnModel().getColumn(2);

		int widthColSujet = 400;
		int widthColExpediteur = 100;
		int widthColDate = 75;

		colSujet.setPreferredWidth(widthColSujet);
		colExpediteur.setPreferredWidth(widthColExpediteur);
		colDate.setPreferredWidth(widthColDate);

		ImageIcon leafIcon = new ImageIcon(FilsListePanel.class.getResource("/images/icones/messages_tree_15.png"));
		xTreeTable.setLeafIcon(leafIcon);

		// ColorHighlighter hl = new ColorHighlighter(new
		// MyHighlightPredicate(), Color.yellow,Color.blue, Color.red,
		// Color.green);
		// xTreeTable.addHighlighter(hl);
	}

	// private class MyHighlightPredicate implements HighlightPredicate {
	//
	// public boolean isHighlighted(Component renderer, ComponentAdapter
	// adapter) {
	// int rowIndex = adapter.row;
	// TreePath path = xTreeTable.getPathForRow(rowIndex);
	// DefaultMutableTreeTableNode node = (DefaultMutableTreeTableNode)
	// path.getLastPathComponent();
	// return node.getUserObject() instanceof MessageModel;
	// }
	// }

	@Override
	public JPanel getFilsListePanel() {
		return getPanel();
	}

	@Override
	public JPanel getTabMessagesPanel() {
		return null;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// System.out.println("FilsListePanel - valueChanged : messageSelected = "+messageSelected+" | treePathToSelect = "+treePathToSelect+" | xTreeTable.getSelectedRowCount() = "+xTreeTable.getSelectedRowCount()+" | nodeSelected = "+nodeSelected);
		nodeSelected = e.getPath().getLastPathComponent();
		if (nodeSelected instanceof DefaultMutableTreeTableNode) {
			Object userObject = ((DefaultMutableTreeTableNode) nodeSelected).getUserObject();
			if (userObject instanceof MessageModel) {
				messageSelected = (MessageModel) userObject;
				labFrom.setText(filsListe.getString("txt_De") + " : ");
				labSujet.setText(filsListe.getString("txt_Sujet") + " : ");
				labDate.setText(filsListe.getString("txt_Date") + " : ");
				txtFrom.setText(messageSelected.getExpediteur());
				txtSujet.setText(messageSelected.getSujet());
				txtDate.setText(format.format(messageSelected.getDateUS()));
				boolean isSsOriginalMessage = chkOriginalMessage.isSelected();
				showText(messageSelected, isSsOriginalMessage);
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (null != messageSelected) {
			Object source = e.getItemSelectable();
			if (source == chkOriginalMessage) {
				showText(messageSelected, true);
			} else
				showText(messageSelected, false);
			if (e.getStateChange() == ItemEvent.DESELECTED)
				showText(messageSelected, false);
		}
	}

	private void showText(MessageModel messageSelected, boolean isSsOriginalMessage) {
		String newCorpsMessage = messageSelected.getCorps();
		if (isSsOriginalMessage)
			newCorpsMessage = messageSelected.getSsOriginalMessage(newCorpsMessage);
		setNewStyledDoc(newCorpsMessage);
		// System.out.println("FilsListePanel - showText : newCorpsMessage = \n"+newCorpsMessage);
	}

	public StyledDocument getNewStyledDoc(String corpsMessage) {
		StyledDocument newDoc = getNewDocumentWithStyles(messageSelected.getCorpsToTab(corpsMessage));
		return newDoc;
	}

	private void setNewStyledDoc(String corpsMessage) {
		detailMF.setText("");
		StyledDocument newDoc = getNewStyledDoc(corpsMessage);
		detailMF.setStyledDocument(newDoc);
		detailMF.setCaretPosition(0);
	}

	private StyledDocument getNewDocumentWithStyles(Map<String, String> mapStringStyles) {
		StyledDocument doc = detailMF.getStyledDocument();
		List<String> listString = new ArrayList<String>(mapStringStyles.keySet());
		List<String> listStyles = new ArrayList<String>(mapStringStyles.values());

		String[] initString = listString.toArray(new String[0]);
		String[] initStyles = listStyles.toArray(new String[0]);

		try {
			for (int i = 0; i < initString.length; i++) {
				doc.insertString(doc.getLength(), initString[i], doc.getStyle(initStyles[i]));
			}
		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text into text pane.");
		}
		return doc;
	}

	protected void addStylesToDocument(StyledDocument doc) {
		Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "SansSerif");

		Style s1 = doc.addStyle("blue", regular);
		StyleConstants.setForeground(s1, Color.BLUE);
		StyleConstants.setFontSize(s1, 14);
		StyleConstants.setItalic(s1, true);

		Style s2 = doc.addStyle("green", regular);
		StyleConstants.setForeground(s2, new Color(0, 116, 0));
		StyleConstants.setFontSize(s2, 14);
		StyleConstants.setItalic(s2, true);

		Style s3 = doc.addStyle("red", regular);
		StyleConstants.setForeground(s3, new Color(164, 0, 0));
		StyleConstants.setFontSize(s3, 14);
		StyleConstants.setItalic(s3, true);

		regular = doc.addStyle("regular", def);
		StyleConstants.setFontSize(regular, 14);
	}

	@Override
	public void setIdentifiantMessageToShow(String identifiantMessage) {
		// System.out.println("FilsListePanel - setIdentifiantMessageToShow : identifiantMessage = "
		// + identifiantMessage);
		TreeNode root = filsTreeModel.getRoot();
		findPathToSelect(root, identifiantMessage);
		if (treePathToSelect != null) {
			TreeSelectionModel sm = xTreeTable.getTreeSelectionModel();
			sm.setSelectionPath(treePathToSelect);
			xTreeTable.scrollPathToVisible(treePathToSelect);
		}
	}

	private void findPathToSelect(TreeNode r, String identifiant) {
		TreeNode root = r;
		for (int i = 0; i < root.getChildCount(); i++) {
			Object node = root.getChildAt(i);
			Object userObject = ((DefaultMutableTreeTableNode) node).getUserObject();
			if (userObject instanceof MessageModel) {
				messageSelected = (MessageModel) userObject;
				if (messageSelected.getIdentifiant().equals(identifiant)) {
					treePathToSelect = convertTreeNodeToTreePath((TreeNode) node);
					// System.out
					// .println(treePathToSelect.toString()
					// + " : rang "
					// + i
					// + " | "
					// + ((MessageModel) ((DefaultMutableTreeTableNode) node)
					// .getUserObject()).getExpediteur()
					// + " | " + ((TreeNode) node).isLeaf()
					// + " | "
					// + treeTable.isCollapsed(treePathToSelect)
					// + " | " + ((TreeNode) node).getChildCount()
					// + " enfants");
					break;
				}
			}

			if (((TreeNode) node).getChildCount() > 0) {
				// System.out.println("--- ENFANTS : ");
				findPathToSelect((TreeNode) node, identifiant);
				// System.out.println("--------------");
			}
		}
	}

	private TreePath convertTreeNodeToTreePath(TreeNode node) {
		List<TreeNode> list = new ArrayList<TreeNode>();
		while (node != null) {
			list.add(node);
			node = node.getParent();
		}
		Collections.reverse(list);
		return new TreePath(list.toArray());
	}

	@Override
	public void listeChanged(ListeChangedEvent event) {
		nodeSelected = null;
		messageSelected = null;
		treePathToSelect = null;
		// System.out.println("FilsListePanel - listeChanged : messageSelected = "+messageSelected+" | treePathToSelect = "+treePathToSelect+" | xTreeTable.getSelectedRowCount() = "+xTreeTable.getSelectedRowCount()+" | nodeSelected = "+nodeSelected);
		if (event.getNewNumero() != 0 && event.getNewMapIdMessages().size() != 0) {
			mapIdMessages = event.getNewMapIdMessages();
			colIndexSorted = 999;
			cl.show(cards, "PANEL_PLEIN");
			filsTreeModel = new FilsTreeModel(event.getNewMapIdMessages(), filsListe, 2, true);
			xTreeTable.setTreeTableModel(filsTreeModel);
			labFrom.setText("");
			labSujet.setText("");
			labDate.setText("");
			txtFrom.setText("");
			txtSujet.setText("");
			txtDate.setText("");
			detailMF.setText("");

			box1.getTxtLabel().setText(String.valueOf(event.getNewNbreMessages()));
			String dureeSuivi = event.getNewDureeSuivi();
			// System.out.println("dureeSuivi = " + dureeSuivi);
			if (dureeSuivi != null && dureeSuivi.indexOf("(") != -1) {
				DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
				DateTime dtDebut = new DateTime(event.getDebut());
				DateTime dtFin = new DateTime(event.getFin());
				Period period = new Period(dtDebut, dtFin, PeriodType.yearMonthDayTime());
				box2.getTxtLabel().setText(
						"<html><center>" + fmt.print(dtDebut) + " &raquo; " + fmt.print(dtFin) + "<br><font color=#c1c1c1>" + period.getYears() + " " + filsListe.getString("txt_Annees") + ", "
								+ period.getMonths() + " " + filsListe.getString("txt_Mois") + ", " + period.getDays() + " " + filsListe.getString("txt_Jours") + ", " + period.getHours() + " "
								+ filsListe.getString("txt_Heures") + "</font></center></html>");
			}
			box3.getTxtLabel().setText(String.valueOf(event.getNewNbreLocuteurs()));
			box4.getTxtLabel().setText(
					"<html><center>" + String.valueOf(df.format(event.getNewNbreMoyenMessagesLocuteurMois())) + "<br><font style=color:#c1c1c1;font-size:9px>"
							+ String.valueOf(event.getNewNbreLocuteursUnSeulMessage()) + " " + filsListe.getString("txt_NbreLocuteursUnSeulMessage") + "</font></center></html>");
			box5.getTxtLabel().setText("<html><center>" + event.getNewSParamConversations() + "</center></html>");
			box6.getTxtLabel().setText(String.valueOf(event.getNewSetConversations().size()));
			box7.getTxtLabel().setText(df.format(event.getNewNbreMoyenLocuteursDifferentsSujet()));
			box8.getTxtLabel().setText(df.format(event.getNewNbreMoyenMessagesConversation()));

			scrollFils.setViewportView(xTreeTable);
			scrollDetailMF.setViewportView(detailMF);
			applyRenderer();
			bMois.doClick();
			// bMois.requestFocus();
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
	public JPanel getTabLocuteursPanel() {
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
}
