package renderers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PieLabelLinkStyle;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

public class CustomPieChartBoxPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private GridBagConstraints gbc;
	private String boxTitle;
	private JPanel graphPanel;
	private JFreeChart chart;
	private JLabel titleLabel;
	private ChartPanel chartPanel;
	private Dimension panelDim = new Dimension(290, 265);
	private ResourceBundle ressourcesAnalyseListe;

	public CustomPieChartBoxPanel(String boxTitle, ResourceBundle ressourcesAnalyseListe) {
		super();
		this.boxTitle = boxTitle;
		this.ressourcesAnalyseListe = ressourcesAnalyseListe;
		createChartPanel();
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

	private void createChartPanel() {
		// setBounds(10, 10, 100, 30);
		setOpaque(false);
		setMinimumSize(new Dimension(291, 265));
		setMaximumSize(new Dimension(291, 265));
		setPreferredSize(new Dimension(291, 265));
		setAlignmentY(Component.TOP_ALIGNMENT);
		setAlignmentX(Component.LEFT_ALIGNMENT);

		setBackground(new Color(248, 248, 248));

		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);
		gbc = new GridBagConstraints();

		titleLabel = new JLabel(boxTitle, SwingConstants.CENTER);
		Border outsideBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		Border insideBorder = BorderFactory.createEmptyBorder(0, 5, 0, 5);
		titleLabel.setBorder(BorderFactory.createCompoundBorder(outsideBorder, insideBorder));
		titleLabel.setBackground(new Color(205, 155, 117));
		titleLabel.setForeground(Color.DARK_GRAY);
		titleLabel.setVerticalAlignment(SwingConstants.CENTER);
		titleLabel.setPreferredSize(new Dimension(291, 45));
		titleLabel.setMinimumSize(new Dimension(291, 45));
		titleLabel.setMaximumSize(new Dimension(291, 45));
		titleLabel.setFont(new Font("sansserif", Font.PLAIN, 11));

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.NORTH;
		add(titleLabel, gbc);

		graphPanel = new JPanel();
		graphPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		graphPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		graphPanel.setOpaque(true);
		graphPanel.setBackground(Color.WHITE);
		graphPanel.setPreferredSize(new Dimension(291, 220));
		graphPanel.setMinimumSize(new Dimension(291, 220));
		graphPanel.setMaximumSize(new Dimension(291, 220));
		graphPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.LIGHT_GRAY));
		// graphPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.anchor = GridBagConstraints.SOUTH;
		add(graphPanel, gbc);
	}

	public GridBagConstraints getGbc() {
		return gbc;
	}

	public JPanel getGraphPanel() {
		return graphPanel;
	}

	public void createPieChart(Map<String, Double> mapData) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		for (Entry<String, Double> entry : mapData.entrySet())
			// System.out.println(entry.getKey()+" | "+entry.getValue());
			dataset.setValue(entry.getKey(), entry.getValue());
		chart = ChartFactory.createPieChart("", dataset);
		chart.getLegend().setFrame(BlockBorder.NONE);
		applyRendererToChart();
	}

	public void addChartToChartPanel() {
		chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(286, 200));
		chartPanel.setMinimumSize(new Dimension(286, 200));
		chartPanel.setMaximumSize(new Dimension(286, 200));
		graphPanel.add(chartPanel);
	}

	private void applyRendererToChart() {
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setBackgroundPaint(Color.WHITE);
		plot.setShadowPaint(null);
		plot.setInteriorGap(0.15);
		plot.setOutlineVisible(false);
		plot.setNoDataMessage("∅");

		// SECTIONS
		// plot.setSectionPaint("Others", createGradientPaint(new Color(200,
		// 200, 255), Color.BLUE));
		plot.setSectionPaint(ressourcesAnalyseListe.getString("txt_LocuteursDominantsUneLigne"), new Color(156, 54, 102));
		plot.setSectionPaint(ressourcesAnalyseListe.getString("txt_LocuteurDominantUneLigne"), new Color(193, 31, 81));
		plot.setSectionPaint(ressourcesAnalyseListe.getString("txt_PetitsLocuteurs"), new Color(1, 113, 188));
		plot.setSectionPaint(ressourcesAnalyseListe.getString("txt_SujetsCollectifsUneLigne"), new Color(129, 73, 173));
		plot.setSectionPaint(ressourcesAnalyseListe.getString("txt_AutresConversations"), new Color(41, 116, 128));
		plot.setSectionPaint(ressourcesAnalyseListe.getString("txt_LanceursSC"), new Color(167, 213, 129));
		plot.setSectionPaint(ressourcesAnalyseListe.getString("txt_NonLanceursSC"), new Color(61, 165, 182));
		// plot.setSectionPaint("Nokia", createGradientPaint(new Color(200, 255,
		// 200), Color.YELLOW));
		plot.setBaseSectionOutlinePaint(new Color(248, 248, 248));
		plot.setSectionOutlinesVisible(true);
		plot.setBaseSectionOutlineStroke(new BasicStroke(0.5f));

		// SECTIONS LABELS
		plot.setLabelFont(new Font("Arial Unicode MS", 0, 13));
		// Lien
		plot.setLabelLinkStyle(PieLabelLinkStyle.STANDARD);
		plot.setLabelLinkStroke(new BasicStroke(0.5f));
		plot.setLabelLinkPaint(Color.LIGHT_GRAY);
		plot.setLabelOutlineStroke(null);
		plot.setLabelShadowPaint(Color.WHITE);
		plot.setLabelBackgroundPaint(Color.WHITE);
		// Rotation
		plot.setDirection(Rotation.CLOCKWISE);
		// Texte label
		PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{2}\n\n( {1} )", new DecimalFormat("0"), new DecimalFormat("0.00%"));
		plot.setLabelGenerator(gen);

		// LEGENDE
		plot.setLegendItemShape(new Rectangle(12, 12));
	}

	// private static RadialGradientPaint createGradientPaint(Color c1, Color
	// c2) {
	// Point2D center = new Point2D.Float(0, 0);
	// float radius = 1;
	// float[] dist = { 0.0f, 1.0f };
	// return new RadialGradientPaint(center, radius, dist, new Color[] { c1, c2
	// });
	// }

	public void resize(int boxWidth, int boxHeight, int titleLabelWidth, int titleLabelHeight, int chartPanelWidth, int chartPanelHeight, int graphPanelWidth, int graphPanelHeight) {
		setPreferredSize(new Dimension(boxWidth, boxHeight));
		setMinimumSize(new Dimension(boxWidth, boxHeight));
		setMaximumSize(new Dimension(boxWidth, boxHeight));
		titleLabel.setPreferredSize(new Dimension(titleLabelWidth, titleLabelHeight));
		titleLabel.setMinimumSize(new Dimension(titleLabelWidth, titleLabelHeight));
		titleLabel.setMaximumSize(new Dimension(titleLabelWidth, titleLabelHeight));
		chartPanel.setPreferredSize(new Dimension(chartPanelWidth, chartPanelHeight));
		chartPanel.setMinimumSize(new Dimension(chartPanelWidth, chartPanelHeight));
		chartPanel.setMaximumSize(new Dimension(chartPanelWidth, chartPanelHeight));
		graphPanel.setPreferredSize(new Dimension(graphPanelWidth, graphPanelHeight));
		graphPanel.setMinimumSize(new Dimension(graphPanelWidth, graphPanelHeight));
		graphPanel.setMaximumSize(new Dimension(graphPanelWidth, graphPanelHeight));
	}

}
