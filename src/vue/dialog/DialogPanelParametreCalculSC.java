package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DialogPanelParametreCalculSC extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtParametreSC;
	private float paramSC;
	private JCheckBox chkParametreSC;
	private boolean isLocuteursSC;
	private ResourceBundle bundleOperationsListe;

	public DialogPanelParametreCalculSC(float paramSC, boolean isLocuteursSC,
			ResourceBundle bundleOperationsListe) {
		this.paramSC = paramSC;
		this.isLocuteursSC = isLocuteursSC;
		this.bundleOperationsListe = bundleOperationsListe;
	}

	public JPanel getPanel() {
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel2.setOpaque(true);
		JLabel labParamSC = new JLabel(
				bundleOperationsListe.getString("txt_ParamSelectionSC") + " : ");
		txtParametreSC = new JTextField();
		txtParametreSC.setText(String.valueOf(paramSC));
		txtParametreSC.setPreferredSize(new Dimension(40, 30));

		panel2.add(labParamSC);
		panel2.add(txtParametreSC);

		JPanel panel3 = new JPanel();
		panel3.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel3.setOpaque(true);
		JLabel labParamLocuteursSC = new JLabel(
				bundleOperationsListe.getString("txt_ParamSelectionSCLD")
						+ " : ");
		chkParametreSC = new JCheckBox();
		chkParametreSC.setSelected(isLocuteursSC);

		panel3.add(labParamLocuteursSC);
		panel3.add(chkParametreSC);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);

		add(panel2);
		add(panel3);

		return this;
	}

	public JTextField getTxtParamSC() {
		return txtParametreSC;
	}

	public boolean getChkParametreSC() {
		return chkParametreSC.isSelected();
	}

}
