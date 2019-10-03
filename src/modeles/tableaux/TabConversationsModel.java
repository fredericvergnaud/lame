package modeles.tableaux;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import modeles.ConversationModel;

public class TabConversationsModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnNames;
	@SuppressWarnings("rawtypes")
	private Class[] types;
	private List<ConversationModel> listConversations;
	private ResourceBundle conversationsListe;

	public TabConversationsModel(Set<ConversationModel> setConversations, ResourceBundle conversationsListe) {
		super();
		this.listConversations = new ArrayList<ConversationModel>(setConversations);
		this.conversationsListe = conversationsListe;
		this.types = new Class[] { Integer.class, String.class, String.class, Date.class, Date.class, Integer.class, Integer.class, Integer.class, Boolean.class, String.class };
		this.columnNames = new String[] { conversationsListe.getString("txt_Conversation"), conversationsListe.getString("txt_SujetPremierMessage"),
				"<html>" + conversationsListe.getString("txt_NumeroPremierMessage") + "</br></html>", conversationsListe.getString("txt_Debut"), conversationsListe.getString("txt_Fin"),
				"<html>" + conversationsListe.getString("txt_Duree")+ "</br></html>", "<html>" + conversationsListe.getString("txt_NbreMessages") + "</br></html>",
				"<html>" + conversationsListe.getString("txt_NbreLocuteurs") + "</br></html>", "<html>" + conversationsListe.getString("txt_SC") + "</br></html>",
				conversationsListe.getString("txt_Lanceur") };
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return types[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return listConversations.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return listConversations.get(row).getId();
		case 1:
			return listConversations.get(row).getSujetPremierMessage();
		case 2:
			return listConversations.get(row).getNumeroPremierMessage();
		case 3:
			return listConversations.get(row).getDateDebut();
		case 4:
			return listConversations.get(row).getDateFin();
		case 5:
			return listConversations.get(row).getDuree();
		case 6:
			return listConversations.get(row).getNbreMessages();
		case 7:
			return listConversations.get(row).getNbreLocuteurs();
		case 8:
			return listConversations.get(row).isSc();
		case 9:
			return listConversations.get(row).getLanceur();
		default:
			return null;
		}
	}

	public void addConversation(ConversationModel conversation) {
		listConversations.add(conversation);

		fireTableRowsInserted(listConversations.size() - 1, listConversations.size() - 1);
	}

	public void removeConversation(int row) {
		listConversations.remove(row);

		fireTableRowsDeleted(row, row);
	}
}
