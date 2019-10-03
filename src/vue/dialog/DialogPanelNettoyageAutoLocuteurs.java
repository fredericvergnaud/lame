package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DialogPanelNettoyageAutoLocuteurs extends JPanel {

	private static final long serialVersionUID = 1L;
	private Map<String, List<String>> mapLocuteursDansMessageAModifier;
	private JPanel mainPanelChk;
	private ResourceBundle bundleOperationsListe;

	public DialogPanelNettoyageAutoLocuteurs(
			Map<String, List<String>> mapAffichage,
			ResourceBundle bundleOperationsListe) {
		this.mapLocuteursDansMessageAModifier = mapAffichage;
		this.bundleOperationsListe = bundleOperationsListe;
		createGui();
	}

	public void createGui() {
		JPanel panelTxt = new JPanel();
		panelTxt.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelTxt.setOpaque(true);
		JLabel labelTxt = new JLabel();
		labelTxt.setText(bundleOperationsListe
				.getString("txt_NettoyageAutoLocuteursRapprochements"));

		panelTxt.add(labelTxt);

		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLabel.setOpaque(true);
		JLabel label = new JLabel(
				bundleOperationsListe.getString("txt_CocherModifications")
						+ " : ");

		panelTxt.setLayout(new BoxLayout(panelTxt, BoxLayout.Y_AXIS));
		panelLabel.setLayout(new BoxLayout(panelLabel, BoxLayout.Y_AXIS));

		panelLabel.add(label);

		mainPanelChk = new JPanel();
		mainPanelChk.setName("mainPanelChk");
		GridBagLayout gbl = new GridBagLayout();
		mainPanelChk.setLayout(gbl);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanelChk.setOpaque(true);
		mainPanelChk.setAlignmentY(Component.LEFT_ALIGNMENT);
		int y = 0;
		for (Entry<String, List<String>> e : mapLocuteursDansMessageAModifier
				.entrySet()) {
			String nomAGarder = e.getKey();
			String[] tabNomAGarder = nomAGarder.split("\\$\\$\\$");
			String justNomAGarder = tabNomAGarder[0];
			JTextField newNom = new JTextField(justNomAGarder);
			newNom.setHorizontalAlignment(SwingConstants.LEFT);
			newNom.setPreferredSize(new Dimension(200, 30));
			newNom.setMinimumSize(new Dimension(200, 30));
			newNom.setMaximumSize(new Dimension(200, 30));
			List<String> listNomsASupprimer = e.getValue();
			Set<String> setNomsASupprimer = new HashSet<String>();
			for (String s : listNomsASupprimer) {
				String[] tabNomASupprimer = s.split("\\$\\$\\$");
				String nomASupprimer = tabNomASupprimer[0];
				setNomsASupprimer.add(nomASupprimer);
			}
			String sBox = " " + bundleOperationsListe.getString("txt_Pour")
					+ " [";
			for (String s : setNomsASupprimer) {
				sBox += s + "; ";
			}
			sBox = sBox.substring(0, sBox.length() - 2) + "]";
			JLabel labBox = new JLabel(sBox);
			JCheckBox box = new JCheckBox();
			box.setName(nomAGarder);
			box.setSelected(true);
			JPanel panelChk = new JPanel();
			panelChk.setName(nomAGarder);
			panelChk.setLayout(new BoxLayout(panelChk, BoxLayout.X_AXIS));
			panelChk.setAlignmentY(Component.LEFT_ALIGNMENT);
			panelChk.add(box);
			panelChk.add(newNom);
			panelChk.add(labBox);
			gbc.gridx = 0;
			gbc.gridy = y;
			mainPanelChk.add(panelChk, gbc);
			y++;
		}

		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(430, 330));

		JScrollPane scroll = new JScrollPane(mainPanelChk);
		scroll.setAlignmentY(Component.LEFT_ALIGNMENT);
		add(panelTxt);
		add(panelLabel);
		add(scroll);
	}

	public JPanel getMainPanelChk() {
		return mainPanelChk;
	}
}
