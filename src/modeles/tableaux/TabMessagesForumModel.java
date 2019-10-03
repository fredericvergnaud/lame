package modeles.tableaux;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;

import modeles.MessageModel;

public class TabMessagesForumModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columnNames;
	@SuppressWarnings("rawtypes")
	private Class[] types;
	private List<MessageModel> listMessages;
	
	public TabMessagesForumModel(List<MessageModel> listMessages, ResourceBundle messagesListe) {
		super();
		this.listMessages = new ArrayList<MessageModel>(listMessages);
		this.types = new Class[] { String.class, Date.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Integer.class, String.class, Integer.class };
		this.columnNames = new String[] { messagesListe.getString("txt_IdMessage"), messagesListe.getString("txt_Date"), messagesListe.getString("txt_Locuteur"),
				messagesListe.getString("txt_MailLocuteur"), messagesListe.getString("txt_ProfilYahoo"), messagesListe.getString("txt_GPYahoo"), messagesListe.getString("txt_IdGoogle"), messagesListe.getString("txt_Forum"),
				messagesListe.getString("txt_Sujet"), messagesListe.getString("txt_NumDansTopic"), messagesListe.getString("txt_SujetTronque") };
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
		return listMessages.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return listMessages.get(row).getIdentifiant();
		case 1:
			return listMessages.get(row).getDateUS();
		case 2:
			return listMessages.get(row).getExpediteur();
		case 3:
			return listMessages.get(row).getMail();
		case 4:
			return listMessages.get(row).getProfilYahoo();
		case 5:
			return listMessages.get(row).getGroupPostYahoo();
		case 6:
			return listMessages.get(row).getIdGoogle();
		case 7:
			return listMessages.get(row).getFName();
		case 8:
			return listMessages.get(row).getSujet();
		case 9:
			return listMessages.get(row).getfNumDansConversation();
		case 10:
			return listMessages.get(row).getSujetTronque();
		default:
			return null;
		}
	}

	public void addMessage(MessageModel message) {
		listMessages.add(message);

		fireTableRowsInserted(listMessages.size() - 1, listMessages.size() - 1);
	}

	public void removeMessage(int row) {
		listMessages.remove(row);

		fireTableRowsDeleted(row, row);
	}
}
