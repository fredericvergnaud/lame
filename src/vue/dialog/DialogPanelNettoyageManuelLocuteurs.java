package vue.dialog;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import renderers.NettoyageRenderer;
import extra.LocuteurPourNettoyage;
import extra.nettoyage.NomLocuteurCellEditor;

//import extra.nettoyage.StringActionTableCellEditorNettoyageLocuteur;

import modeles.LocuteurModel;
import modeles.MessageModel;
import modeles.tableaux.TabNettoyageLocuteursMessagesModel;

public class DialogPanelNettoyageManuelLocuteurs extends JPanel {

	private static final long serialVersionUID = 1L;
	private List<MessageModel> listMessages;
	private List<LocuteurModel> listLocuteurs = new ArrayList<LocuteurModel>();
	private JTable tableLocuteurs;
	private int nbreModifsTotal = 0;
	private int oldCol, col = 0;
	private ResourceBundle bundleOperationsListe;

	public DialogPanelNettoyageManuelLocuteurs(List<MessageModel> listMessages, ResourceBundle bundleOperationsListe, int nbreModifs) {
		this.listMessages = listMessages;
		this.bundleOperationsListe = bundleOperationsListe;
		this.nbreModifsTotal = nbreModifs;
		createGui();
	}

	public JTable getTableLocuteurs() {
		return tableLocuteurs;
	}

	public void createListLocuteurs() {
		listLocuteurs.clear();
		Map<String, Set<String>> mapNomXGP = new HashMap<String, Set<String>>(), mapNomXP = new HashMap<String, Set<String>>(), mapNomXM = new HashMap<String, Set<String>>();
		Map<String, Set<String>> mapNomXIdMessages = new HashMap<String, Set<String>>();
		for (MessageModel message : listMessages) {
			String idM = message.getIdentifiant();
			String nomL = message.getExpediteur();
			String mailL = message.getMail();
			String profilL = message.getProfilYahoo();
			String gPL = message.getGroupPostYahoo();
			if (!mapNomXIdMessages.containsKey(nomL)) {
				mapNomXIdMessages.put(nomL, new HashSet<String>());
				mapNomXIdMessages.get(nomL).add(idM);
				mapNomXP.put(nomL, new HashSet<String>());
				mapNomXP.get(nomL).add(profilL);
				mapNomXGP.put(nomL, new HashSet<String>());
				mapNomXGP.get(nomL).add(gPL);
				mapNomXM.put(nomL, new HashSet<String>());
				mapNomXM.get(nomL).add(mailL);
			} else {
				mapNomXIdMessages.get(nomL).add(idM);
				mapNomXP.get(nomL).add(profilL);
				mapNomXGP.get(nomL).add(gPL);
				mapNomXM.get(nomL).add(mailL);
			}
		}

		for (Entry<String, Set<String>> e1 : mapNomXIdMessages.entrySet()) {
			String nomLocuteur = e1.getKey();
			// Set<Integer> setIdMessages = e1.getValue();
			LocuteurModel newLocuteur = new LocuteurModel();
			newLocuteur.setNom(nomLocuteur);
			// newLocuteur.setSetIdMessages(setIdMessages);

			for (Entry<String, Set<String>> e2 : mapNomXGP.entrySet()) {
				String nomLocuteur2 = e2.getKey();
				if (nomLocuteur2.equals(nomLocuteur)) {
					Set<String> setGroupPosts = e2.getValue();
					newLocuteur.setSetGroupPosts(setGroupPosts);
				}
			}
			for (Entry<String, Set<String>> e2 : mapNomXP.entrySet()) {
				String nomLocuteur2 = e2.getKey();
				if (nomLocuteur2.equals(nomLocuteur)) {
					Set<String> setProfils = e2.getValue();
					newLocuteur.setSetProfils(setProfils);
				}
			}
			for (Entry<String, Set<String>> e2 : mapNomXM.entrySet()) {
				String nomLocuteur2 = e2.getKey();
				if (nomLocuteur2.equals(nomLocuteur)) {
					Set<String> setMails = e2.getValue();
					newLocuteur.setSetMails(setMails);
				}
			}
			listLocuteurs.add(newLocuteur);
		}
		// AFFICHAGE DES LOCUTEURS
		// for (LocuteurModel locuteur : listLocuteurs) {
		// System.out.println(locuteur.getNom() + " - "
		// + locuteur.getSetGroupPosts().size() + " groupPosts - "
		// + locuteur.getSetProfils().size() + " profils - "
		// + locuteur.getSetMails().size() + " mails - "
		// + locuteur.getNbreMessages() + " messages");
		// for (String groupPost : locuteur.getSetGroupPosts())
		// System.out.println("== GP = " + groupPost);
		// for (String profil : locuteur.getSetProfils())
		// System.out.println("== P = " + profil);
		// for (String mail : locuteur.getSetMails())
		// System.out.println("== M = " + mail);
		// }
	}

	public void createGui() {
		createListLocuteurs();
		// JTABLE
		String[] columnNames = { bundleOperationsListe.getString("txt_AncienNom"), bundleOperationsListe.getString("txt_NouveauNom"), bundleOperationsListe.getString("txt_GPYahoo"),
				bundleOperationsListe.getString("txt_ProfilYahoo"), bundleOperationsListe.getString("txt_Mail") };
		TabNettoyageLocuteursMessagesModel model = new TabNettoyageLocuteursMessagesModel(listLocuteurs, columnNames);
		tableLocuteurs = new JTable(model);
		tableLocuteurs.setRowSelectionAllowed(true);
		tableLocuteurs.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tableLocuteurs.setFillsViewportHeight(true);
		tableLocuteurs.setAutoCreateRowSorter(true);
		// TableRowSorter<TabNettoyageLocuteursMessagesModel> sorter = new
		// TableRowSorter<TabNettoyageLocuteursMessagesModel>(model);
		// tableLocuteurs.setRowSorter(sorter);
		// sorter.setSortsOnUpdates(true);
		// COLONNE 1 RENDERER
		// JTextField textField = new JTextField();
		// textField.setBorder(BorderFactory.createEmptyBorder());
		// DefaultCellEditor editor = new DefaultCellEditor(textField);
		// editor.setClickCountToStart(1);
		// tableLocuteurs.getColumn(tableLocuteurs.getColumnName(1))
		// .setCellEditor(
		// new StringActionTableCellEditorNettoyageLocuteur(
		// editor, listMessages));

		tableLocuteurs.setDefaultEditor(String.class, new NomLocuteurCellEditor(listMessages, nbreModifsTotal, col, bundleOperationsListe));

		final Map<String, String> mapNoms = new HashMap<String, String>();
		final Map<String, String> mapGp = new HashMap<String, String>();
		final Map<String, String> mapP = new HashMap<String, String>();
		final Map<String, String> mapM = new HashMap<String, String>();
		for (int i = 0; i < model.getRowCount(); i++) {
			String nom = (String) tableLocuteurs.getValueAt(i, 1);
			String gp = (String) tableLocuteurs.getValueAt(i, 2);
			String p = (String) tableLocuteurs.getValueAt(i, 3);
			String m = (String) tableLocuteurs.getValueAt(i, 4);
			String a = String.valueOf(0 + (int) (Math.random() * ((255 - 0) + 1)));
			String rgba = "61,61,61," + a;

			if (!mapNoms.containsKey(nom)) {
				mapNoms.put(nom, rgba);
			}
			if (!mapGp.containsKey(gp)) {
				mapGp.put(gp, rgba);
			}
			if (!mapP.containsKey(p)) {
				mapP.put(p, rgba);
			}
			if (!mapM.containsKey(m)) {
				mapM.put(m, rgba);
			}
		}

		final JTableHeader header = tableLocuteurs.getTableHeader();
		header.setReorderingAllowed(false);
		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// int oldCol = 0;
				col = header.columnAtPoint(e.getPoint());
				System.out.println("old = " + oldCol + " | new = " + col);
				// System.out.printf("click cursor = %d%n", header.getCursor()
				// .getType());
				if (header.getCursor().getType() == Cursor.E_RESIZE_CURSOR)
					e.consume();
				else {
					// System.out.printf("sorting column %d%n", col);
					NettoyageRenderer renderer;
					if (col == 1 || col == 0)
						renderer = new NettoyageRenderer(mapNoms);
					else if (col == 2)
						renderer = new NettoyageRenderer(mapGp);
					else if (col == 3)
						renderer = new NettoyageRenderer(mapP);
					else
						renderer = new NettoyageRenderer(mapM);
					tableLocuteurs.getColumn(tableLocuteurs.getColumnName(col)).setCellRenderer(renderer);
					tableLocuteurs.setDefaultEditor(String.class, new NomLocuteurCellEditor(listMessages, nbreModifsTotal, col, bundleOperationsListe));
					if (col != oldCol) {
						tableLocuteurs.getColumn(tableLocuteurs.getColumnName(oldCol)).setCellRenderer(new DefaultTableCellRenderer());
					}
					oldCol = col;
				}
			}
		});
		// Disable auto resizing
		tableLocuteurs.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// LARGEUR DES COLONNES
		TableColumn colNomOld = tableLocuteurs.getColumnModel().getColumn(0);
		TableColumn colNomNew = tableLocuteurs.getColumnModel().getColumn(1);
		TableColumn colGp = tableLocuteurs.getColumnModel().getColumn(2);
		TableColumn colP = tableLocuteurs.getColumnModel().getColumn(3);
		TableColumn colM = tableLocuteurs.getColumnModel().getColumn(4);

		int widthColNomOld = 150;
		int widthColNomNew = 150;
		int widthColGp = 100;
		int widthColP = 100;
		int widthColM = 250;

		colNomOld.setPreferredWidth(widthColNomOld);
		colNomNew.setPreferredWidth(widthColNomNew);
		colGp.setPreferredWidth(widthColGp);
		colP.setPreferredWidth(widthColP);
		colM.setPreferredWidth(widthColM);

		JScrollPane scrollTabLocuteurs = new JScrollPane(tableLocuteurs);
		scrollTabLocuteurs.setPreferredSize(new Dimension(740, 440));

		setLayout(new FlowLayout());
		add(scrollTabLocuteurs);
		setPreferredSize(new Dimension(740, 450));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
	}

	public int getNbreModifsTotal() {
		NomLocuteurCellEditor editor = (NomLocuteurCellEditor) tableLocuteurs.getDefaultEditor(String.class);
		nbreModifsTotal = editor.getNbreModifsTotal();
		return nbreModifsTotal;
	}
	
	public Set<LocuteurPourNettoyage> getNewListLocuteurs() {
		TabNettoyageLocuteursMessagesModel model = (TabNettoyageLocuteursMessagesModel) tableLocuteurs.getModel();
		return model.getSetLocuteursPourNettoyage();
	}
}
