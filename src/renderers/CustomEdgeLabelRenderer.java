package renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;

import edu.uci.ics.jung.visualization.renderers.DefaultEdgeLabelRenderer;

public class CustomEdgeLabelRenderer extends DefaultEdgeLabelRenderer {
	protected Color unpickedEdgeLabelColor = Color.BLACK;

	public CustomEdgeLabelRenderer(Color unpickedEdgeLabelColor,
			Color pickedEdgeLabelColor) {
		super(pickedEdgeLabelColor);
		this.unpickedEdgeLabelColor = unpickedEdgeLabelColor;
	}

	@Override
	public <E> Component getEdgeLabelRendererComponent(JComponent vv,
			Object value, Font font, boolean isSelected, E Edge) {
		super.setForeground(unpickedEdgeLabelColor);
		if (isSelected) {
			setForeground(pickedEdgeLabelColor);			
		}
//		super.setBackground(vv.getBackground());
		
		if (font != null) {
			setFont(font);
		} else {
			setFont(vv.getFont());
		}
		setIcon(null);
		setBorder(noFocusBorder);
		setValue(value);
		return this;
	}
}