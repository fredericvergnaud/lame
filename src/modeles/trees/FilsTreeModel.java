package modeles.trees;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;
import java.util.TreeMap;

import modeles.MessageModel;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import comparators.MapIdMessagesDateComparator;
import comparators.MapIdMessagesExpediteurComparator;
import comparators.MapIdMessagesSujetComparator;

public class FilsTreeModel extends DefaultTreeTableModel {

	private Map<String, DefaultMutableTreeTableNode> namesToNodes = new HashMap<String, DefaultMutableTreeTableNode>();
	private Map<String, MessageModel> mapIdMessages;
	private ResourceBundle filsListe;
	private int sortType;
	private boolean ascendant;

	public FilsTreeModel(ResourceBundle filsListe) {
		this.filsListe = filsListe;
	}

	public FilsTreeModel(Map<String, MessageModel> mapIdMessages, ResourceBundle filsListe, int sortType, boolean ascendant) {
		this.mapIdMessages = mapIdMessages;
		this.filsListe = filsListe;
		this.sortType = sortType;
		this.ascendant = ascendant;
		initModel();
	}

	private void initModel() {
//		System.out.println("avant sorting, mapIdMessages size = "+mapIdMessages.size());
		Comparator<Object> comparator;
		if (sortType == 0) {
			comparator = new MapIdMessagesSujetComparator(mapIdMessages, ascendant);
		} else if (sortType == 1) {
			comparator = new MapIdMessagesExpediteurComparator(mapIdMessages, ascendant);
		} else
			comparator = new MapIdMessagesDateComparator(mapIdMessages, ascendant);

		TreeMap<String, MessageModel> sortedMap = new TreeMap<String, MessageModel>(comparator);
		sortedMap.putAll(mapIdMessages);
//		System.out.println("apres sorting, sortedMap size = "+sortedMap.size());
		// ROOT
		MessageModel rootMF = new MessageModel();
		rootMF.setSujet("sujet root");
		rootMF.setExpediteur("exp. root");
		rootMF.setDateUS(new Date());
		rootMF.setIdentifiant("root");
		rootMF.setInReplyTo("");
		DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode(rootMF);
		namesToNodes.put("root", rootNode);
		// mapIdentifiantMF.put("root", rootMF);
		setRoot(new DefaultMutableTreeTableNode(rootMF));
		// AJOUT DES DONNEES
		for (Entry<String, MessageModel> e : sortedMap.entrySet()) {
			MessageModel message = e.getValue();
			String inReplyTo = message.getInReplyTo();
			String inReplyToRegroupe = message.getInReplyToRegroupe();
//			System.out.println("Message id "+message.getIdentifiant()+" | inReplyTo = "+message.getInReplyTo()+" | inReplyToRegroupe = "+message.getInReplyToRegroupe());
			if (inReplyTo.equals("") && inReplyToRegroupe != null)
				inReplyTo = message.getInReplyToRegroupe();
			// System.out.println("message identifiant = " +
			// mf.getIdentifiant());
			// System.out.println("	inReplyTo = " + inReplyTo);

			if (inReplyTo.equals("") && !message.getIdentifiant().equals("root"))
				addNodeTo(message.getIdentifiant(), "root");
			else {
				if (mapIdMessages.containsKey(inReplyTo)) {
					// System.out.println("recherche de " + inReplyTo
					// + " : identifiant trouve");
					addNodeTo(message.getIdentifiant(), mapIdMessages.get(inReplyTo).getIdentifiant());
				} else {
					addNodeTo(message.getIdentifiant(), "root");
				}
			}
			// System.out.println();
		}
	}

	@Override
	public int getColumnCount() {
		return 3;
	}
	
	@Override
	public Object getValueAt(Object node, int column) {
		Object res = "n/a";
		Object userObject = ((DefaultMutableTreeTableNode) node).getUserObject();
		if (userObject instanceof MessageModel) {
			MessageModel mf = (MessageModel) userObject;
			switch (column) {
			case 0:
				res = mf.getSujet();
				break;
			case 1:
				res = mf.getExpediteur();
				break;
			case 2:
				res = mf.getDateUS();
				break;
			}
		}
		return res;
	}

	@Override
	public String getColumnName(int column) {
		String res = "";
		switch (column) {
		case 0:
			res = filsListe.getString("txt_Sujet");
			break;
		case 1:
			res = filsListe.getString("txt_Expediteur");
			break;
		case 2:
			res = filsListe.getString("txt_Date");
			break;

		}
		return res;
	}

	@Override
	public boolean isCellEditable(Object node, int column) {
		return false;
	}

	@Override
	public Class<?> getColumnClass(int column) {
		switch (column) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return Date.class;
		default:
			return super.getColumnClass(column);
		}
	}

	private DefaultMutableTreeTableNode getNode(String identifiant) {
		DefaultMutableTreeTableNode node = (namesToNodes.get(identifiant));
		if (node == null) {
			DefaultMutableTreeTableNode newNode = new DefaultMutableTreeTableNode(mapIdMessages.get(identifiant));
			namesToNodes.put(identifiant, newNode);
			return newNode;
		} else {
			return node;
		}
	}

	public void addNodeTo(String childNodeIdentifiant, String parentNodeIdentifiant) {
		DefaultMutableTreeTableNode parentNode = getNode(parentNodeIdentifiant);
		DefaultMutableTreeTableNode childNode = getNode(childNodeIdentifiant);
		if (parentNodeIdentifiant.equals("root") && !childNodeIdentifiant.equals("root"))
			((DefaultMutableTreeTableNode) getRoot()).add(childNode);
		else
			parentNode.add(childNode);
	}

	public int getRowCount() {
		return mapIdMessages.size();
	}
}