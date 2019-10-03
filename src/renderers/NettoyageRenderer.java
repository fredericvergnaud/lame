package renderers;

import java.awt.Color;
import java.awt.Component;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class NettoyageRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = 1L;
	Map<String, String> map;

	public NettoyageRenderer(Map<String, String> map) {
		this.map = map;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int col) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, col);
		for (Entry<String, String> entry : map.entrySet()) {
			String s = entry.getKey();
			if (value.equals(s)) {
				String[] tabColors = entry.getValue().split(",");
				setBackground(new Color(Integer.parseInt(tabColors[0]),
						Integer.parseInt(tabColors[1]),
						Integer.parseInt(tabColors[2]),
						Integer.parseInt(tabColors[3])));
				// setForeground(Color.white);
				break;
			}
		}
		return this;
	}
}