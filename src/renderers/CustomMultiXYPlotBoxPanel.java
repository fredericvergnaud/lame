package renderers;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CustomMultiXYPlotBoxPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private GridBagConstraints gbc;
	private String boxTitle;
	private JPanel graphPanel;
	private ImageIcon icon;

	public CustomMultiXYPlotBoxPanel(String boxTitle, ImageIcon icon) {
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
		setMinimumSize(new Dimension(630, 360));
		setMaximumSize(new Dimension(630, 360));
		setPreferredSize(new Dimension(630, 360));
		setAlignmentY(Component.TOP_ALIGNMENT);

		setBackground(new Color(248, 248, 248));
		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		gbc = new GridBagConstraints();

		JPanel titlePanel = new JPanel();
		FlowLayout titlePanelLayout = new FlowLayout(FlowLayout.LEFT);
		titlePanelLayout.setVgap(0);
		titlePanelLayout.setHgap(0);
		titlePanel.setLayout(titlePanelLayout);
		// titlePanel.setPreferredSize(new Dimension(320, 40));
		// titlePanel.setMinimumSize(new Dimension(320, 40));
		// titlePanel.setMaximumSize(new Dimension(320, 40));
		titlePanel.setBackground(new Color(248, 248, 248));
		titlePanel.setForeground(Color.DARK_GRAY);
		titlePanel.setAlignmentY(Component.CENTER_ALIGNMENT);
		titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		titlePanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
		// Border outsideBorder = BorderFactory.createEmptyBorder(0,0,0,0);
		// Border insideBorder = BorderFactory.createEmptyBorder(10,5,0,5);
		// titlePanel.setBorder(BorderFactory.createCompoundBorder(outsideBorder,insideBorder));

		JLabel titleLabel = new JLabel(boxTitle, icon, SwingConstants.LEFT);
		titleLabel.setIconTextGap(10);
		titleLabel.setFont(new Font("sansserif", Font.PLAIN, 11));
		titleLabel.setPreferredSize(new Dimension(300, 45));
		titleLabel.setMinimumSize(new Dimension(300, 45));
		titleLabel.setMaximumSize(new Dimension(300, 45));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(0,5,0,0);
		add(titleLabel, gbc);

		graphPanel = new JPanel();
		CardLayout layoutGraphPanel = new CardLayout(0, 0);
		graphPanel.setLayout(layoutGraphPanel);
		graphPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		graphPanel.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		graphPanel.setOpaque(true);
		graphPanel.setBackground(Color.WHITE);
		graphPanel.setPreferredSize(new Dimension(620, 315));
		graphPanel.setMinimumSize(new Dimension(620, 315));
		graphPanel.setMaximumSize(new Dimension(620, 315));
		graphPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.LIGHT_GRAY));

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0,0,0,0);
		gbc.anchor = GridBagConstraints.SOUTH;
		add(graphPanel, gbc);
	}

	public GridBagConstraints getGbc() {
		return gbc;
	}

	public JPanel getGraphPanel() {
		return graphPanel;
	}

}
