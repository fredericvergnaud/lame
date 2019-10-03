package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DialogPanelAjoutTopic extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField topicTitle;
	private ResourceBundle bundleOperationsListe;

	public DialogPanelAjoutTopic(ResourceBundle bundleOperationsListe) {
		this.bundleOperationsListe = bundleOperationsListe;
		createGui();
	}

	public void createGui() {
		JPanel panelTitreTopic = new JPanel();
		panelTitreTopic.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelTitreTopic.setOpaque(true);
		JLabel labelTitreTopic = new JLabel(bundleOperationsListe.getString("txt_TitreTopic") + " : ");
		topicTitle = new JTextField("");
		topicTitle.setPreferredSize(new Dimension(150, 30));

		panelTitreTopic.add(labelTitreTopic);
		panelTitreTopic.add(topicTitle);

		add(panelTitreTopic);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
	}

	public String getTopicTitle() {
		return topicTitle.getText();
	}
}
