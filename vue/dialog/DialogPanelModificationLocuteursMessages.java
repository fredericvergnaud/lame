package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class DialogPanelModificationLocuteursMessages extends JPanel {

	private static final long serialVersionUID = 1L;
	private String ancienNom;
	private JTextArea textNouveauNom;
	private ButtonGroup group;
	private int colSelect;
	private ResourceBundle bundleOperationsListe;

	public DialogPanelModificationLocuteursMessages(String ancienNom,
			int colSelect, ResourceBundle bundleOperationsListe) {
		this.ancienNom = ancienNom;
		this.colSelect = colSelect;
		this.bundleOperationsListe = bundleOperationsListe;
		System.out.println("colSelect = " + colSelect);
	}

	public JPanel getPanel() {
		GridBagLayout l1 = new GridBagLayout();
		JPanel panel1 = new JPanel();
		panel1.setLayout(l1);
		GridBagConstraints c1 = new GridBagConstraints();
		panel1.setAlignmentY(Component.LEFT_ALIGNMENT);

		// JLabel labelNouveauNom = new JLabel("Nouveau nom : "),
		// validerNomSelon = new JLabel(
		// "Valider le nom ci-dessus : "), nul = new JLabel(" "), ou = new
		// JLabel(
		// "OU"), validerLigne = new JLabel("Valider la ligne ["
		// + ancienNom + "][" + mailSelect + "][" + profilSelect + "]["
		// + groupPostSelect + "]");
		JLabel labelNouveauNom = new JLabel(
				bundleOperationsListe.getString("txt_NouveauNom") + " : "), validerNomSelon = new JLabel(
				bundleOperationsListe.getString("txt_ValiderNom") + " : "), ou = new JLabel(
				bundleOperationsListe.getString("txt_Ou")), validerLigne = new JLabel(
				bundleOperationsListe.getString("txt_ValiderNomLigne") + " : ");
		validerNomSelon.setFont(new Font("sansserif", Font.PLAIN, 12));
		validerLigne.setFont(new Font("sansserif", Font.PLAIN, 12));

		textNouveauNom = new JTextArea();
		textNouveauNom.setText(ancienNom);
		// if (ancienNom != null) {
		// textNouveauNom.setText((String) table.getValueAt(row, 1));
		// textNouveauNom.setCaretPosition(0);
		// }
		textNouveauNom.setPreferredSize(new Dimension(200, 30));
		textNouveauNom.setMinimumSize(new Dimension(200, 30));
		textNouveauNom.setMaximumSize(new Dimension(200, 30));

		c1.gridx = 0;
		c1.gridy = 0;
		panel1.add(labelNouveauNom, c1);
		c1.gridx = 1;
		c1.gridy = 0;
		panel1.add(textNouveauNom, c1);

		GridBagLayout l2 = new GridBagLayout();
		JPanel panel2 = new JPanel();
		panel2.setLayout(l2);
		GridBagConstraints c2 = new GridBagConstraints();
		panel2.setAlignmentY(Component.LEFT_ALIGNMENT);

		c2.gridx = 0;
		c2.gridy = 0;
		panel2.add(validerNomSelon, c2);

		GridBagLayout l3 = new GridBagLayout();
		JPanel panel3 = new JPanel();
		panel3.setLayout(l3);
		GridBagConstraints c3 = new GridBagConstraints();
		panel3.setAlignmentY(Component.LEFT_ALIGNMENT);

		int numButtons = 8;
		JRadioButton[] radioButtons = new JRadioButton[numButtons];
		group = new ButtonGroup();

		final String nom_seul = "nom_seul";
		final String meme_group = "meme_group";
		final String meme_profil = "meme_profil";
		final String meme_mail = "meme_mail";

		radioButtons[0] = new JRadioButton(
				bundleOperationsListe.getString("txt_LocuteursMemeNom"));
		radioButtons[0].setActionCommand(nom_seul);
		radioButtons[1] = new JRadioButton(
				bundleOperationsListe.getString("txt_LocuteursMemeGp"));
		radioButtons[1].setActionCommand(meme_group);
		radioButtons[2] = new JRadioButton(
				bundleOperationsListe.getString("txt_LocuteursMemeP"));
		radioButtons[2].setActionCommand(meme_profil);
		radioButtons[3] = new JRadioButton(
				bundleOperationsListe.getString("txt_LocuteursMemeMail"));
		radioButtons[3].setActionCommand(meme_mail);

		c3.gridx = 0;
		c3.gridy = 0;
		if (colSelect == 0 || colSelect == 1)
			panel3.add(radioButtons[0], c3);
		// c3.gridx = 0;
		// c3.gridy = 1;
		else if (colSelect == 2)
			panel3.add(radioButtons[1], c3);
		// c3.gridx = 0;
		// c3.gridy = 2;
		else if (colSelect == 3)
			panel3.add(radioButtons[2], c3);
		// c3.gridx = 0;
		// c3.gridy = 3;
		else
			panel3.add(radioButtons[3], c3);

		GridBagLayout l4 = new GridBagLayout();
		JPanel panel4 = new JPanel();
		panel4.setLayout(l4);
		GridBagConstraints c4 = new GridBagConstraints();
		panel4.setAlignmentY(Component.LEFT_ALIGNMENT);

		c4.gridx = 0;
		c4.gridy = 0;
		panel4.add(ou, c4);

		GridBagLayout l5 = new GridBagLayout();
		JPanel panel5 = new JPanel();
		panel5.setLayout(l5);
		GridBagConstraints c5 = new GridBagConstraints();
		panel5.setAlignmentY(Component.LEFT_ALIGNMENT);

		c5.gridx = 0;
		c5.gridy = 0;
		panel5.add(validerLigne, c5);

		GridBagLayout l6 = new GridBagLayout();
		JPanel panel6 = new JPanel();
		panel6.setLayout(l6);
		GridBagConstraints c6 = new GridBagConstraints();
		panel6.setAlignmentY(Component.LEFT_ALIGNMENT);

		final String ligne_nom_seul = "ligne_nom_seul";
		final String ligne_meme_group = "ligne_meme_group";
		final String ligne_meme_profil = "ligne_meme_profil";
		final String ligne_meme_mail = "ligne_meme_mail";

		radioButtons[4] = new JRadioButton(
				bundleOperationsListe.getString("txt_LocuteursMemeNom"));
		radioButtons[4].setActionCommand(ligne_nom_seul);
		radioButtons[5] = new JRadioButton(
				bundleOperationsListe.getString("txt_LocuteursMemeGp"));
		radioButtons[5].setActionCommand(ligne_meme_group);
		radioButtons[6] = new JRadioButton(
				bundleOperationsListe.getString("txt_LocuteursMemeP"));
		radioButtons[6].setActionCommand(ligne_meme_profil);
		radioButtons[7] = new JRadioButton(
				bundleOperationsListe.getString("txt_LocuteursMemeMail"));
		radioButtons[7].setActionCommand(ligne_meme_mail);

		for (int i = 0; i < numButtons; i++) {
			group.add(radioButtons[i]);
		}

		c6.gridx = 0;
		c6.gridy = 0;
		if (colSelect == 0 || colSelect == 1) {
			panel6.add(radioButtons[4], c6);
			radioButtons[4].setSelected(true);
		}
		// c6.gridx = 0;
		// c6.gridy = 1;
		else if (colSelect == 2) {
			panel6.add(radioButtons[5], c6);
			radioButtons[5].setSelected(true);
		}
		// c6.gridx = 0;
		// c6.gridy = 2;
		else if (colSelect == 3) {
			panel6.add(radioButtons[6], c6);
			radioButtons[6].setSelected(true);
		}
		// c6.gridx = 0;
		// c6.gridy = 3;
		else {
			panel6.add(radioButtons[7], c6);
			radioButtons[7].setSelected(true);
		}

		add(panel1);
		add(panel2);
		add(panel3);
		add(panel4);
		add(panel5);
		add(panel6);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
		setPreferredSize(new Dimension(400, 200));
		setMinimumSize(new Dimension(400, 200));
		setMaximumSize(new Dimension(400, 200));

		return this;
	}

	public String getNewNom() {
		return textNouveauNom.getText();
	}

	public String getAncienNom() {
		return ancienNom;
	}

	public String getSelection() {
		return group.getSelection().getActionCommand();
	}

}
