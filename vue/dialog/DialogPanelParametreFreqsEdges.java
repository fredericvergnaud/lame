package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ResourceBundle;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class DialogPanelParametreFreqsEdges extends JPanel {

	private static final long serialVersionUID = 1L;
	private int freq_max, freq_inf, freq_sup;
	private ResourceBundle bundleOperationsListe;
	private JTextArea txtFreqInf, txtFreqSup;

	public DialogPanelParametreFreqsEdges(ResourceBundle bundleOperationsListe,
			int freq_max, int freq_inf, int freq_sup) {
		this.bundleOperationsListe = bundleOperationsListe;
		this.freq_max = freq_max;
		this.freq_inf = freq_inf;
		this.freq_sup = freq_sup;
		createGui();
	}

	public void createGui() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
		setMinimumSize(new Dimension(250, 100));
		setMaximumSize(new Dimension(250, 100));
		setPreferredSize(new Dimension(250, 100));

		JPanel panelLabelIntervalleEdgesFreqs = new JPanel();
		panelLabelIntervalleEdgesFreqs
				.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLabelIntervalleEdgesFreqs.setOpaque(true);
		panelLabelIntervalleEdgesFreqs.setMinimumSize(new Dimension(250, 30));
		JLabel labIntervalleEdgesFreqs = new JLabel(
				"Fréquences de arcs à afficher :");
		panelLabelIntervalleEdgesFreqs.add(labIntervalleEdgesFreqs);
		JLabel labIntervalleEdgesFreqs2 = new JLabel("(Min = -1 / Max = "
				+ (freq_max + 1) + ")");
		panelLabelIntervalleEdgesFreqs.add(labIntervalleEdgesFreqs2);
		add(panelLabelIntervalleEdgesFreqs);

		JPanel panelIntervalleEdgesFreqs = new JPanel();
		panelIntervalleEdgesFreqs.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelIntervalleEdgesFreqs.setOpaque(true);

		JLabel labSup = new JLabel(" > ");
		JLabel labAndOr = new JLabel(" AND / OR ");
		JLabel labInf = new JLabel(" < ");
		txtFreqInf = new JTextArea();
		txtFreqInf.setPreferredSize(new Dimension(40, 30));
		txtFreqSup = new JTextArea();
		txtFreqSup.setPreferredSize(new Dimension(40, 30));

		if (freq_inf != 0 || freq_sup != 0) {
			if (freq_inf != 0)
				txtFreqInf.setText(String.valueOf(freq_inf));
			if (freq_sup != 0)
				txtFreqSup.setText(String.valueOf(freq_sup));
		}

		panelIntervalleEdgesFreqs.add(labSup);
		panelIntervalleEdgesFreqs.add(txtFreqInf);
		panelIntervalleEdgesFreqs.add(labAndOr);
		panelIntervalleEdgesFreqs.add(labInf);
		panelIntervalleEdgesFreqs.add(txtFreqSup);
		add(panelIntervalleEdgesFreqs);
	}

	public JTextArea getTxtFreqInf() {
		return txtFreqInf;
	}

	public JTextArea getTxtFreqSup() {
		return txtFreqSup;
	}

	public JPanel getPanel() {
		return this;
	}
}
