package modeles.tableaux;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;
import extra.LocuteurPourNettoyage;

import modeles.LocuteurModel;

public class TabNettoyageLocuteursMessagesModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private List<LocuteurPourNettoyage> listLocuteursPourNettoyage = new ArrayList<LocuteurPourNettoyage>();
	private String[] columnNames;
	@SuppressWarnings("rawtypes")
	private Class[] types = new Class[] { String.class, String.class,
			String.class, String.class, String.class };

	public TabNettoyageLocuteursMessagesModel(
			List<LocuteurModel> listLocuteurs, String[] columnNames) {
		super();
		this.columnNames = columnNames;
		for (LocuteurModel locuteur : listLocuteurs) {
			Set<String> tabGPs = locuteur.getSetGroupPosts();
			Set<String> tabPs = locuteur.getSetProfils();
			Set<String> tabMails = locuteur.getSetMails();
			for (String gP : tabGPs) {
				for (String p : tabPs) {
					for (String m : tabMails) {
						this.listLocuteursPourNettoyage
								.add(new LocuteurPourNettoyage(locuteur
										.getNom(), locuteur.getNom(), gP, p, m));
					}
				}
			}
		}
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
		return listLocuteursPourNettoyage.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return listLocuteursPourNettoyage.get(row).getAncienNom();
		case 1:
			return listLocuteursPourNettoyage.get(row).getNouveauNom();
		case 2:
			return listLocuteursPourNettoyage.get(row).getGroupPost();
		case 3:
			return listLocuteursPourNettoyage.get(row).getProfil();
		case 4:
			return listLocuteursPourNettoyage.get(row).getMail();
		default:
			return null;
		}
	}

	public void addLocuteur(LocuteurPourNettoyage locuteurPourNettoyage) {
		listLocuteursPourNettoyage.add(locuteurPourNettoyage);
		fireTableRowsInserted(listLocuteursPourNettoyage.size() - 1,
				listLocuteursPourNettoyage.size() - 1);
	}

	public void removeLocuteur(int row) {
		listLocuteursPourNettoyage.remove(row);
		fireTableRowsDeleted(row, row);
	}

	@Override
	public void setValueAt(Object aValue, int row, int col) {
		if (aValue != null) {
			LocuteurPourNettoyage locuteurPourNettoyage = listLocuteursPourNettoyage
					.get(row);
			switch (col) {
			case 0:
				locuteurPourNettoyage.setAncienNom((String) aValue);
				break;
			case 1:
				locuteurPourNettoyage.setNouveauNom((String) aValue);
				break;
			case 2:
				locuteurPourNettoyage.setGroupPost((String) aValue);
				break;
			case 3:
				locuteurPourNettoyage.setProfil((String) aValue);
				break;
			case 4:
				locuteurPourNettoyage.setMail((String) aValue);
				break;
			}
			fireTableDataChanged();
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		if (col == 1)
			return true;
		else
			return false;
	}

	public Set<LocuteurPourNettoyage> getSetLocuteursPourNettoyage() {
		Set<LocuteurPourNettoyage> setLocuteursPourNettoyage = new HashSet<LocuteurPourNettoyage>(
				listLocuteursPourNettoyage);
		return setLocuteursPourNettoyage;
	}
}
