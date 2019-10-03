package renderers;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class SujetsCollectifsRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	private DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	private ResourceBundle ressourcesTabConversations;

	public SujetsCollectifsRenderer(ResourceBundle ressourcesTabConversations) {
		this.ressourcesTabConversations = ressourcesTabConversations;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

		Boolean sc = (Boolean) table.getValueAt(row, 8);

		if (sc)
			setForeground(new Color(129, 73, 173));
		else
			setForeground(new Color(41, 116, 128));

		setHorizontalAlignment(CENTER);		
		
		switch (col) {
		case 0:
		case 2:
		case 5:
		case 6:
		case 7:
			break;
		case 1:
		case 9:
			setHorizontalAlignment(LEFT);
			break;
		case 3:
		case 4:
			setText((value == null) ? "" : formatter.format(value));
			break;
		case 8:
			setText((value.equals(true)) ? ressourcesTabConversations.getString("txt_Oui") : "∅");
			break;
		default:
			return null;
		}

		if (isSelected)
			setForeground(Color.white);

		return this;
	}
}