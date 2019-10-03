package renderers;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class LocuteursForumRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private DecimalFormat df = new DecimalFormat("0.00");
	private ResourceBundle ressourcesTabLocuteurs;

	public LocuteursForumRenderer(ResourceBundle ressourcesTabLocuteurs) {
		this.ressourcesTabLocuteurs = ressourcesTabLocuteurs;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		Boolean ld = (Boolean) table.getValueAt(row, 11);

		if (ld)
			setForeground(new Color(156, 54, 102)); // rouge
		else
			setForeground(new Color(1, 113, 188)); // bleu

		switch (col) {
		case 0:
		case 1:
			setHorizontalAlignment(LEFT);
			break;
		case 2:
		case 3:
		case 4:
		case 5:
		case 12:
			setHorizontalAlignment(CENTER);
			break;
		case 6:
		case 7:
			setHorizontalAlignment(CENTER);
			setText((value == null) ? "" : formatter.format(value));
			break;
		case 8:
		case 9:
			setHorizontalAlignment(CENTER);
			break;
		case 10:
			setText((value == null) ? "" : df.format(value));
			setHorizontalAlignment(CENTER);
			break;
		case 11:
			setHorizontalAlignment(CENTER);
			setText((value.equals(true)) ? ressourcesTabLocuteurs.getString("txt_Oui") : "∅");
			break;
		case 13:
		case 14:
		case 15:
			break;

		case 16:
			setHorizontalAlignment(CENTER);
			setText((value == null) ? "" : df.format(value));
			break;
		default:
			return null;
		}

		if (isSelected)
			setForeground(Color.white);

		return this;
	}

}