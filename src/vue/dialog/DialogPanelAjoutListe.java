package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DialogPanelAjoutListe extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtNom;
	private ResourceBundle bundleProjetController;
	private String oldName;

	public DialogPanelAjoutListe(ResourceBundle bundleProjetController, String oldName) {
		this.bundleProjetController = bundleProjetController;
		this.oldName = oldName;
		createGui();
	}

	public void createGui() {
		JPanel panelNom = new JPanel();
		panelNom.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelNom.setOpaque(true);
		JLabel labelNom = new JLabel(
				bundleProjetController.getString("txt_Nom") + " : ");
		txtNom = new JTextField(oldName);
		txtNom.setPreferredSize(new Dimension(150, 30));

		panelNom.add(labelNom);
		panelNom.add(txtNom);

		add(panelNom);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
	}

	public String getNomListe() {
		return txtNom.getText();
	}
}
