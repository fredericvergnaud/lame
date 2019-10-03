package renderers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;

public class DateRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;
	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public DateRenderer() {
		super();
	}

	@Override
	public void setValue(Object value) {
		if (formatter == null) {
			formatter = DateFormat.getDateInstance();
		} else {
			if (value instanceof Date)
				setText((value == null) ? "" : formatter.format(value));
		}
	}
}