package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DialogPanelSetUrlToConnect extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtUrl;
	private ResourceBundle bundleOperationsListe;
	private String siteUrl;

	public DialogPanelSetUrlToConnect(ResourceBundle bundleOperationsListe, String siteUrl) {
		this.bundleOperationsListe = bundleOperationsListe;
		this.siteUrl = siteUrl;
		createGui();
	}

	public void createGui() {

		JPanel panelUrl = new JPanel();
		panelUrl.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelUrl.setOpaque(true);
		JLabel labUrl = new JLabel(bundleOperationsListe.getString("txt_UrlToConnect") + " : ");
		txtUrl = new JTextField(siteUrl);
		txtUrl.setPreferredSize(new Dimension(350, 30));
		panelUrl.add(labUrl);
		panelUrl.add(txtUrl);

		add(panelUrl);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
	}

	public String getUrlToConnect() {
		return txtUrl.getText();
	}
}
