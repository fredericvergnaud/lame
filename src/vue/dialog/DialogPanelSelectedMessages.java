package vue.dialog;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class DialogPanelSelectedMessages extends JPanel {
	private static final long serialVersionUID = 1L;
	private JPanel panelChk;
	private JRadioButton radioExport, radioDelete;
	private ResourceBundle ressourcesTabMessages;

	public DialogPanelSelectedMessages(ResourceBundle ressourcesTabMessages) {
		this.ressourcesTabMessages = ressourcesTabMessages;
		createGui();
	}

	public void createGui() {
		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLabel.setOpaque(true);
		JLabel label = new JLabel(ressourcesTabMessages.getString("txt_VoulezVous")+" : ");

		panelLabel.add(label);

		panelChk = new JPanel();
		panelChk.setLayout(new GridLayout(2, 1));
		panelChk.setOpaque(true);

		radioExport = new JRadioButton(ressourcesTabMessages.getString("txt_CreerListe"));
		radioExport.setSelected(true);

		radioDelete = new JRadioButton(ressourcesTabMessages.getString("txt_SupprimerSelection"));
		radioDelete.setSelected(false);

		ButtonGroup group = new ButtonGroup();
		group.add(radioExport);
		group.add(radioDelete);

		panelChk.add(radioExport);
		panelChk.add(radioDelete);

		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(panelLabel);
		add(panelChk);
	}

	public JRadioButton getRadioExport() {
		return radioExport;
	}

	public void setRadioExport(JRadioButton radioExport) {
		this.radioExport = radioExport;
	}

	public JRadioButton getRadioDelete() {
		return radioDelete;
	}

	public void setRadioDelete(JRadioButton radioDelete) {
		this.radioDelete = radioDelete;
	}

}
