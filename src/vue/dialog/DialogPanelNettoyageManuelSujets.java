package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DialogPanelNettoyageManuelSujets extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextArea jtaMotsASuppr;
	private ResourceBundle bundleOperationsListe;

	public DialogPanelNettoyageManuelSujets(ResourceBundle bundleOperationsListe) {
		this.bundleOperationsListe = bundleOperationsListe;
		createGui();
	}

	public JTextArea getJtaMotsASuppr() {
		return jtaMotsASuppr;
	}

	public void createGui() {
		GridBagLayout layout = new GridBagLayout();
		JPanel panelMotsSujet = new JPanel();
		panelMotsSujet.setLayout(layout);
		GridBagConstraints c = new GridBagConstraints();

		JLabel labelMotsSujet = new JLabel(
				bundleOperationsListe.getString("txt_MotsSujetsASupprimer")
						+ " : \n");

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		panelMotsSujet.add(labelMotsSujet, c);

		jtaMotsASuppr = new JTextArea();
		JScrollPane scrollMotsASuppr = new JScrollPane(jtaMotsASuppr);
		scrollMotsASuppr.setPreferredSize(new Dimension(317, 100));

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		panelMotsSujet.add(scrollMotsASuppr, c);

		add(panelMotsSujet);
		setSize(375, 200);
		setLayout(new FlowLayout());
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
	}
}
