package charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.swing.JPanel;
import modeles.MessageModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.RectangleInsets;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ChartNbreMessagesDate {

	private Map<String, MessageModel> mapIdMessages;
	private Font font10 = new Font("SansSerif", Font.PLAIN, 10);
	private Font font12 = new Font("SansSerif", Font.PLAIN, 12);
	private String sFormatDate, txt;
	private ResourceBundle filsListe;
	
	public ChartNbreMessagesDate(Map<String, MessageModel> mapIdMessages, int typeGraph, ResourceBundle filsListe) {
		this.mapIdMessages = mapIdMessages;
		this.filsListe = filsListe;
		if (typeGraph == 1) {
			sFormatDate = "dd MMM yyyy";
			txt = " "+filsListe.getString("txt_ParJoursMin");
		}
		else if (typeGraph == 2) {
			sFormatDate = "MMM yyyy";
			txt = " "+filsListe.getString("txt_ParMoisMin");
		}
		else {
			sFormatDate = "yyyy";
			txt = " "+filsListe.getString("txt_ParAnneesMin");
		}

	}
	
	static {
		XYBarRenderer.setDefaultBarPainter(new StandardXYBarPainter());
	}

	private IntervalXYDataset getNbreMessagesAnnees() {
		List<MessageModel> listMessages = new ArrayList<MessageModel>(mapIdMessages.values());
		TreeMap<String, Integer> treeMapDateMessageModel = new TreeMap<String, Integer>();

		for (MessageModel message : listMessages) {
			DateTime date = new DateTime(message.getDateUS());
			String sDate = date.toString(DateTimeFormat.forPattern(sFormatDate));
			if (!treeMapDateMessageModel.containsKey(sDate)) {
				int nbreMessages = 1;
				treeMapDateMessageModel.put(sDate, nbreMessages);
			} else {
				int nbreMessages = treeMapDateMessageModel.get(sDate);
				nbreMessages++;
				treeMapDateMessageModel.put(sDate, nbreMessages);
			}
		}

		TimeSeries serie = new TimeSeries(filsListe.getString("txt_NombreMessages")+txt);
		for (Entry<String, Integer> entry : treeMapDateMessageModel.entrySet()) {
			// System.out.println(entry.getKey() + " : " + entry.getValue() +
			// " messages");
			DateTimeFormatter fmt = DateTimeFormat.forPattern(sFormatDate);
			DateTime date = fmt.parseDateTime(entry.getKey());
			Day day = new Day(date.getDayOfMonth(), date.getMonthOfYear(), date.getYear());
			serie.add(day, entry.getValue());
		}
		TimeSeriesCollection dataset = new TimeSeriesCollection();
		dataset.addSeries(serie);

		return dataset;
	}

	public JPanel getNbreMessagesAnneeChartPanel() {
		IntervalXYDataset dataset = getNbreMessagesAnnees();

		JFreeChart chart = ChartFactory.createXYBarChart("", "", true, "", dataset);
		chartApplyRender(chart);

		XYPlot plot = (XYPlot) chart.getPlot();
		xyLineChartApplyRender(plot);

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanelApplyRender(chartPanel);

		return chartPanel;
	}

	private void xyLineChartApplyRender(XYPlot plot) {

		plot.setBackgroundPaint(Color.WHITE);
		plot.setDomainCrosshairVisible(true);
		plot.setRangeCrosshairVisible(true);

		NumberFormat numberFormat = NumberFormat.getNumberInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(sFormatDate);
		StandardXYToolTipGenerator ttG = new StandardXYToolTipGenerator("{1} : {2}" + " messages", dateFormat, numberFormat);
		XYBarRenderer renderer = new XYBarRenderer();
		renderer.setBaseToolTipGenerator(ttG);
		renderer.setShadowVisible(false);
		renderer.setSeriesPaint(0, new Color(1, 113, 188));
		
		// renderer.setBaseToolTipGenerator(new StandardXYToolTipGenerator(){
		// public String generateToolTip(XYDataset dataset, int series, int
		// item) {
		// double y =dataset.getYValue(series, item);
		// Date x = (Date) dataset.getXValue(series, item);
		// return x+" m, "+y+" %";
		// }
		// });
		plot.setRenderer(renderer);
		// XYItemRenderer r = plot.getRenderer();
		// r.setBaseToolTipGenerator(new StandardXYToolTipGenerator(){
		// public String generateToolTip(XYDataset dataset, int series, int
		// item) {
		// double y =dataset.getYValue(series, item);
		// double x = dataset.getXValue(series, item);
		// return x+" m, "+y+" %";
		// }
		// });
		// NumberFormat format = NumberFormat.getNumberInstance();
		// format.setMaximumFractionDigits(2); // etc.
		// StandardXYToolTipGenerator ttG = new
		// StandardXYToolTipGenerator("{1},{2}", format, format);
		// renderer.setBaseToolTipGenerator(ttG);
		// plot.setRenderer(new XYSplineRenderer());
		// if (r instanceof XYLineAndShapeRenderer) {
		// XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
		// renderer.setBaseShapesVisible(true);
		// renderer.setBaseShapesFilled(true);
		// renderer.setDrawSeriesLineAsPath(true);
		// NumberFormat format = NumberFormat.getNumberInstance();
		// format.setMaximumFractionDigits(2); // etc.
		// StandardXYToolTipGenerator ttG =
		// new StandardXYToolTipGenerator("{1},{2}", format, format);
		// renderer.setBaseToolTipGenerator(ttG);
		// }
		//

		DateAxis xAxis = (DateAxis) plot.getDomainAxis();
		xAxis.setDateFormatOverride(new SimpleDateFormat(sFormatDate));
		xAxis.setTickLabelFont(font10);
		xAxis.setLabelFont(font12);

		NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
		yAxis.setTickLabelFont(font10);
		yAxis.setLabelFont(font12);
		yAxis.setAutoRangeStickyZero(false);
	}

	private void chartApplyRender(JFreeChart chart) {
		chart.setPadding(new RectangleInsets(0, 0, 10, 0));
		chart.getLegend().setFrame(BlockBorder.NONE);

	}

	private void chartPanelApplyRender(ChartPanel chartPanel) {
		chartPanel.setPreferredSize(new Dimension(609, 320));
		chartPanel.setMinimumSize(new Dimension(609, 320));
		chartPanel.setMaximumSize(new Dimension(609, 320));

		// chartPanel.setOpaque(true);
		// chartPanel.setBackground(new Color(205, 155, 117));
		chartPanel.setFillZoomRectangle(true);
		chartPanel.setMouseWheelEnabled(true);
		chartPanel.setDomainZoomable(true);
		chartPanel.setRangeZoomable(false);
		chartPanel.setInitialDelay(0);

	}
}
