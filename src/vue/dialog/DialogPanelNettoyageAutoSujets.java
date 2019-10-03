package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class DialogPanelNettoyageAutoSujets extends JPanel {

	private static final long serialVersionUID = 1L;
	private Map<String, Integer> mapMotsDansSujetAEnlever;
	private JPanel panelChk;
	private ResourceBundle bundleOperationsListe;

	public DialogPanelNettoyageAutoSujets(
			Map<String, Integer> mapMotsDansSujetAEnlever,
			ResourceBundle bundleOperationsListe) {
		this.mapMotsDansSujetAEnlever = mapMotsDansSujetAEnlever;
		this.bundleOperationsListe = bundleOperationsListe;
		createGui();
	}

	public void createGui() {
		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLabel.setOpaque(true);
		JLabel label = new JLabel(
				bundleOperationsListe.getString("txt_CocherMots") + " : ");

		panelLabel.add(label);

		panelChk = new JPanel();
		panelChk.setLayout(new GridLayout(mapMotsDansSujetAEnlever.size(), 1));
		panelChk.setOpaque(true);

		for (Entry<String, Integer> e : mapMotsDansSujetAEnlever.entrySet()) {
			String mot = e.getKey();
			int nbre = e.getValue();
			JCheckBox box = new JCheckBox(mot + " ("
					+ bundleOperationsListe.getString("txt_Cite") + " " + nbre
					+ " " + bundleOperationsListe.getString("txt_Fois") + ")");
			box.setName(mot);
			box.setSelected(true);
			box.setPreferredSize(new Dimension(2000, 27));
			panelChk.add(box);
		}

		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setSize(450, 330);

		JScrollPane scroll = new JScrollPane(panelChk);
		scroll.setPreferredSize(new Dimension(430, 310));
		scroll.setMinimumSize(new Dimension(430, 310));
		scroll.setMaximumSize(new Dimension(430, 310));
		add(panelLabel);
		add(scroll);
	}
}
