package renderers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class CustomStatBoxPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel titleLabel, txtLabel;
	private String boxTitle;
	private ImageIcon icon;

	public CustomStatBoxPanel(String boxTitle, ImageIcon icon) {
		super();
		this.boxTitle = boxTitle;
		this.icon = icon;
		applyRenderer();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension arcs = new Dimension(10, 10);
		int width = getWidth();
		int height = getHeight();
		Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Draws the rounded opaque panel with borders.
		graphics.setColor(getBackground());
		graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);// paint
																						// background
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height);// paint
																						// border

	}

	public void applyRenderer() {
		// setBounds(10, 10, 100, 30);
		setOpaque(false);
		setPreferredSize(new Dimension(130, 105));
		setMaximumSize(new Dimension(130, 105));
		setMinimumSize(new Dimension(130, 105));
		setAlignmentY(Component.CENTER_ALIGNMENT);
		setAlignmentX(Component.LEFT_ALIGNMENT);

		setBackground(new Color(248, 248, 248));

		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();

		titleLabel = new JLabel(boxTitle, icon, SwingConstants.LEFT);
		titleLabel.setIconTextGap(10);
		Border outsideBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		Border insideBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		titleLabel.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
		titleLabel.setBackground(new Color(205, 155, 117));
		titleLabel.setForeground(Color.DARK_GRAY);
		titleLabel.setVerticalAlignment(SwingConstants.CENTER);
		titleLabel.setPreferredSize(new Dimension(130, 45));
		titleLabel.setMinimumSize(new Dimension(130, 45));
		titleLabel.setMaximumSize(new Dimension(130, 45));
		titleLabel.setFont(new Font("sansserif", Font.PLAIN, 11));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.NORTH;
		add(titleLabel, gbc);

		txtLabel = new JLabel("200", SwingConstants.CENTER);
		txtLabel.setBackground(Color.WHITE);
		txtLabel.setOpaque(true);
		txtLabel.setVerticalAlignment(SwingConstants.CENTER);
		txtLabel.setPreferredSize(new Dimension(130, 60));
		txtLabel.setMinimumSize(new Dimension(130, 60));
		txtLabel.setMaximumSize(new Dimension(130, 60));
		txtLabel.setFont(new Font("sansserif", Font.BOLD, 20));
		txtLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.LIGHT_GRAY));

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.SOUTH;
		add(txtLabel, gbc);
	}

	public JLabel getTxtLabel() {
		return txtLabel;
	}

	public void resize(int boxWidth, int boxHeight, int titleLabelWidth, int titleLabelHeight, int txtLabelWidth, int txtLabelHeight) {
		setPreferredSize(new Dimension(boxWidth, boxHeight));
		setMinimumSize(new Dimension(boxWidth, boxHeight));
		setMaximumSize(new Dimension(boxWidth, boxHeight));
		titleLabel.setPreferredSize(new Dimension(titleLabelWidth, titleLabelHeight));
		titleLabel.setMinimumSize(new Dimension(titleLabelWidth, titleLabelHeight));
		titleLabel.setMaximumSize(new Dimension(titleLabelWidth, titleLabelHeight));
		txtLabel.setPreferredSize(new Dimension(txtLabelWidth, txtLabelHeight));
		txtLabel.setMinimumSize(new Dimension(txtLabelWidth, txtLabelHeight));
		txtLabel.setMaximumSize(new Dimension(txtLabelWidth, txtLabelHeight));
	}
}
