package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DialogPanelParametreCalculLD extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtParametreLD;
	private float paramLD;
	private ResourceBundle bundleOperationsListe;

	public DialogPanelParametreCalculLD(float paramLD,
			ResourceBundle bundleOperationsListe) {
		this.paramLD = paramLD;
		this.bundleOperationsListe = bundleOperationsListe;
	}

	public JPanel getPanel() {
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel1.setOpaque(true);
		JLabel labParamLD = new JLabel(
				bundleOperationsListe.getString("txt_ParamSelectionLD") + " : ");
		txtParametreLD = new JTextField();
		txtParametreLD.setText(String.valueOf(paramLD));
		txtParametreLD.setPreferredSize(new Dimension(40, 30));

		panel1.add(labParamLD);
		panel1.add(txtParametreLD);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);

		add(panel1);
		return this;
	}

	public JTextField getTxtParamLD() {
		return txtParametreLD;
	}

}
