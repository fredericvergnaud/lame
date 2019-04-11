package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class DialogPanelChoixTypeMessagesListe extends JPanel {

	private static final long serialVersionUID = 1L;
	private int typeMessagesListeSelected = 0;
	private ResourceBundle bundleProjetController;

	public DialogPanelChoixTypeMessagesListe(
			ResourceBundle bundleProjetController) {
		this.bundleProjetController = bundleProjetController;
		createGui();
	}

	public void createGui() {
		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelLabel.setOpaque(true);
		panelLabel.setMaximumSize(new Dimension(400, 30));
		panelLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		JLabel labChoixType = new JLabel(
				bundleProjetController.getString("txt_ChoixTypeMessages")
						+ " : ");
		panelLabel.add(labChoixType);

		JPanel panelRadios = new JPanel();
		panelRadios.setLayout(new BoxLayout(panelRadios, BoxLayout.Y_AXIS));
		panelRadios.setOpaque(true);
		panelRadios.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelRadios.setMaximumSize(new Dimension(400, 100));
		
		ButtonGroup groupB = new ButtonGroup();

		JRadioButton rbForum = new JRadioButton(
				bundleProjetController.getString("txt_ChoixTypeMessagesForum"));
		rbForum.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				typeMessagesListeSelected = 1;
			}
		});
		rbForum.setAlignmentY(Component.LEFT_ALIGNMENT);

		JRadioButton rbBalLocale = new JRadioButton(
				bundleProjetController
						.getString("txt_ChoixTypeMessagesBalLocale"));
		rbBalLocale.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				typeMessagesListeSelected = 2;
			}
		});
		rbBalLocale.setAlignmentY(Component.LEFT_ALIGNMENT);

		JRadioButton rbBalDistante = new JRadioButton(
				bundleProjetController
						.getString("txt_ChoixTypeMessagesBalDistante"));
		rbBalDistante.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				typeMessagesListeSelected = 3;
			}
		});
		rbBalDistante.setAlignmentY(Component.LEFT_ALIGNMENT);
		rbBalDistante.setEnabled(false);
		
		rbForum.setMaximumSize(new Dimension(400, 30));
		rbBalLocale.setMaximumSize(new Dimension(400, 30));
		rbBalDistante.setMaximumSize(new Dimension(400, 30));

		groupB.add(rbForum);
		groupB.add(rbBalLocale);
		groupB.add(rbBalDistante);

		panelRadios.add(rbForum);
		panelRadios.add(rbBalLocale);
		panelRadios.add(rbBalDistante);

		add(panelLabel);
		add(panelRadios);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setOpaque(true);

		setMaximumSize(new Dimension(400, 140));
		setPreferredSize(new Dimension(400, 140));
	}

	public int getTypeMessagesListeSelected() {
		return typeMessagesListeSelected;
	}
}
