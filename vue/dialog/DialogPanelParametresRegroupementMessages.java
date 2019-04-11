package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class DialogPanelParametresRegroupementMessages extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtParametreJours, txtParametreMessages, txtParametreLevenshtein;
	private int paramJours, paramMessages, paramLevenshtein;
	private ResourceBundle bundleOperationsListe;
	private JRadioButton radioNoRegroupement;
	private boolean regroupementMessages;

	public DialogPanelParametresRegroupementMessages(int paramJours, int paramMessages, int paramLevenshtein, boolean regroupementMessages, ResourceBundle bundleOperationsListe) {
		this.paramJours = paramJours;
		this.paramMessages = paramMessages;
		this.paramLevenshtein = paramLevenshtein;
		this.regroupementMessages = regroupementMessages;
		this.bundleOperationsListe = bundleOperationsListe;
	}

	public JPanel getPanel() {
		// System.out.println("DialogPanelParametreCalculConversations - getPanel : enableParamJoursMessagesLevenshtein = "
		// + enableParamJoursMessagesLevenshtein);

		JPanel panelDialogueParamConv = new JPanel();
		panelDialogueParamConv.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelDialogueParamConv.setOpaque(true);

		final JLabel labelParametreJours = new JLabel(bundleOperationsListe.getString("txt_ParamJours") + " : ");
		txtParametreJours = new JTextField(String.valueOf(paramJours));
		txtParametreJours.setPreferredSize(new Dimension(40, 30));

		final JLabel labelParametreMessages = new JLabel("   " + bundleOperationsListe.getString("txt_Ou") + " " + bundleOperationsListe.getString("txt_ParamMessages") + " : ");
		txtParametreMessages = new JTextField(String.valueOf(paramMessages));
		txtParametreMessages.setPreferredSize(new Dimension(40, 30));
		final JLabel labelParametreMessages2 = new JLabel("   (" + bundleOperationsListe.getString("txt_ExclureParam") + ")");

		panelDialogueParamConv.add(labelParametreJours);
		panelDialogueParamConv.add(txtParametreJours);
		panelDialogueParamConv.add(labelParametreMessages);
		panelDialogueParamConv.add(txtParametreMessages);
		panelDialogueParamConv.add(labelParametreMessages2);

		JPanel panelDialogueParamLev = new JPanel();
		panelDialogueParamLev.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelDialogueParamLev.setOpaque(true);
		final JLabel labelParametreLevenshtein = new JLabel(bundleOperationsListe.getString("txt_ParamDistanceLevenshtein") + " : ");
		txtParametreLevenshtein = new JTextField();
		txtParametreLevenshtein.setText(String.valueOf(paramLevenshtein));
		txtParametreLevenshtein.setPreferredSize(new Dimension(40, 30));

		panelDialogueParamLev.add(labelParametreLevenshtein);
		panelDialogueParamLev.add(txtParametreLevenshtein);

		JPanel panelRadioNoRegroupement = new JPanel();
		panelRadioNoRegroupement.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelRadioNoRegroupement.setOpaque(true);
		radioNoRegroupement = new JRadioButton(bundleOperationsListe.getString("txt_ChkNoRegroupement"));
		radioNoRegroupement.setSelected(regroupementMessages);
		radioNoRegroupement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (radioNoRegroupement.isSelected()) {
					txtParametreJours.setEnabled(false);
					txtParametreMessages.setEnabled(false);
					txtParametreLevenshtein.setEnabled(false);
					labelParametreJours.setEnabled(false);
					labelParametreMessages.setEnabled(false);
					labelParametreMessages2.setEnabled(false);
					labelParametreLevenshtein.setEnabled(false);
				} else {
					txtParametreJours.setEnabled(true);
					txtParametreMessages.setEnabled(true);
					txtParametreLevenshtein.setEnabled(true);
					labelParametreJours.setEnabled(true);
					labelParametreMessages.setEnabled(true);
					labelParametreMessages2.setEnabled(true);
					labelParametreLevenshtein.setEnabled(true);
				}
			}
		});
		panelRadioNoRegroupement.add(radioNoRegroupement);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);

		add(panelDialogueParamConv);
		add(panelDialogueParamLev);
		add(panelRadioNoRegroupement);
		
		if (radioNoRegroupement.isSelected()) {
			txtParametreJours.setEnabled(false);
			txtParametreMessages.setEnabled(false);
			txtParametreLevenshtein.setEnabled(false);
			labelParametreJours.setEnabled(false);
			labelParametreMessages.setEnabled(false);
			labelParametreMessages2.setEnabled(false);
			labelParametreLevenshtein.setEnabled(false);
		} else {
			txtParametreJours.setEnabled(true);
			txtParametreMessages.setEnabled(true);
			txtParametreLevenshtein.setEnabled(true);
			labelParametreJours.setEnabled(true);
			labelParametreMessages.setEnabled(true);
			labelParametreMessages2.setEnabled(true);
			labelParametreLevenshtein.setEnabled(true);
		}

		return this;
	}

	public JTextField getTxtParametreJours() {
		return txtParametreJours;
	}

	public JTextField getTxtParametreMessages() {
		return txtParametreMessages;
	}

	public JTextField getTxtParametreLevenshtein() {
		return txtParametreLevenshtein;
	}

	public boolean getParamNoRegroupementMessages() {
		return radioNoRegroupement.isSelected();
	}
}
