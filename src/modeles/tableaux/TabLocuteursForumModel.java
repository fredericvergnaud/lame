package modeles.tableaux;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import modeles.LocuteurModel;

public class TabLocuteursForumModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("rawtypes")
	private Class[] types;
	private List<LocuteurModel> listLocuteurs;
	private String[] columnNames;

	public TabLocuteursForumModel(Set<LocuteurModel> setLocuteurs, ResourceBundle locuteursListePanel) {
		super();
		this.listLocuteurs = new ArrayList<LocuteurModel>(setLocuteurs);
		// System.out.println("TabLocuteursModelForum - Constructeur : listLocuteurs.size = "+listLocuteurs.size());
		types = new Class[] { Integer.class, String.class, String.class, String.class, Integer.class, Double.class, Date.class, Date.class, Integer.class, Integer.class, Float.class, Boolean.class, Integer.class,
				Integer.class, Integer.class, Integer.class, Float.class };
		columnNames = new String[] { "Id", locuteursListePanel.getString("txt_Nom"), locuteursListePanel.getString("txt_Role"), locuteursListePanel.getString("txt_Qualite"),
				locuteursListePanel.getString("txt_Activite"),
				"<html><center>" + locuteursListePanel.getString("txt_Nombre") + "<br>" + locuteursListePanel.getString("txt_Etoiles") + "</br></center></html>",
				locuteursListePanel.getString("txt_Debut"), locuteursListePanel.getString("txt_Fin"),
				"<html><center>" + locuteursListePanel.getString("txt_DureeParticipation") + "<br>(" + locuteursListePanel.getString("txt_Jours") + ")</br></center></html>",
				"<html><center>" + locuteursListePanel.getString("txt_Nombre") + "<br>" + locuteursListePanel.getString("txt_MessagesMin") + "</br></center></html>",
				locuteursListePanel.getString("txt_Intensite"), "<html><center>" + locuteursListePanel.getString("txt_LocuteurDominant") + "</br></center></html>",
				"<html><center>" + locuteursListePanel.getString("txt_Nombre") + "<br>" + locuteursListePanel.getString("txt_Conversations") + "</br></center></html>",
				"<html><center>" + locuteursListePanel.getString("txt_Nombre") + "<br>" + locuteursListePanel.getString("txt_ParticipationsSC") + "</br></center></html>",
				"<html><center>" + locuteursListePanel.getString("txt_Nombre") + " " + locuteursListePanel.getString("txt_SCLances") + "</br></center></html>",
				"<html><center>" + locuteursListePanel.getString("txt_Nombre") + " " + locuteursListePanel.getString("txt_MessagesSC") + "</br></center></html>",
				"<html><center>" + locuteursListePanel.getString("txt_PourcentTotalMessages") + "</br></center></html>" };

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
		return listLocuteurs.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return listLocuteurs.get(row).getId();
		case 1:
			return listLocuteurs.get(row).getNom();
		case 2:
			return listLocuteurs.get(row).getfRole();
		case 3:
			return listLocuteurs.get(row).getfStatPosition();
		case 4:
			return listLocuteurs.get(row).getfStatActivity();
		case 5:
			return listLocuteurs.get(row).getfStars();
		case 6:
			return listLocuteurs.get(row).getDateDebut();
		case 7:
			return listLocuteurs.get(row).getDateFin();
		case 8:
			return listLocuteurs.get(row).getDuree();
		case 9:
			return listLocuteurs.get(row).getNbreMessages();
		case 10:
			return listLocuteurs.get(row).getIntensite();
		case 11:
			return listLocuteurs.get(row).isLd();
		case 12:
			return listLocuteurs.get(row).getNbreConversations();
		case 13:
			return listLocuteurs.get(row).getNbreSujetsCollectifs();
		case 14:
			return listLocuteurs.get(row).getNbreSujetsCollectifsLances();
		case 15:
			return listLocuteurs.get(row).getNbreMessagesSC();
		case 16:
			return listLocuteurs.get(row).getPourcentMessagesSC();
		default:
			return null;
		}
	}
}
