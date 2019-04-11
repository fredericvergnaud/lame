package vue.projetPanelSup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import renderers.RoundedPanel;
import modeles.evenements.ProjetChangedEvent;
import modeles.evenements.ProjetListeAddedEvent;
import controleurs.ProjetController;
import controleurs.vuesabstraites.ProjetView;

public class InfosProjetPanel extends ProjetView {

	private RoundedPanel panel;
	private JLabel txtNomProjet;
	private JLabel txtRepertoireProjet, txtNbreListes, txtCumulMessages;
	private ResourceBundle infosProjetPanel;

	public InfosProjetPanel(ProjetController controller, ResourceBundle infosProjetPanel) {
		super(controller);
		this.infosProjetPanel = infosProjetPanel;
	}

	public JPanel getPanel() {
		panel = new RoundedPanel();
		panel.setPreferredSize(new Dimension(300, 100));
		panel.setMaximumSize(new Dimension(300, 100));
		panel.setMinimumSize(new Dimension(300, 100));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0,10,0,10);
		gbc.ipadx = 10;

		Font gras = new Font("sansserif", Font.BOLD, 12);
		Font normal = new Font("sansserif", Font.PLAIN, 12);

		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel1.setBackground(Color.WHITE);

		JLabel labNomProjet = new JLabel(infosProjetPanel.getString("txt_Name") + " : ", SwingConstants.LEFT);
		labNomProjet.setFont(gras);
		txtNomProjet = new JLabel("", SwingConstants.LEFT);
		txtNomProjet.setFont(normal);

		panel1.add(labNomProjet);
		panel1.add(txtNomProjet);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(panel1, gbc);

		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel2.setBackground(Color.WHITE);

		JLabel labRepertoireProjet = new JLabel(infosProjetPanel.getString("txt_RepertoireTravail") + " : ", SwingConstants.LEFT);
		labRepertoireProjet.setFont(gras);
		txtRepertoireProjet = new JLabel("", SwingConstants.LEFT);
		txtRepertoireProjet.setFont(normal);

		panel2.add(labRepertoireProjet);
		panel2.add(txtRepertoireProjet);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(panel2, gbc);

		JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel3.setBackground(Color.WHITE);

		JLabel labNbreListes = new JLabel(infosProjetPanel.getString("txt_NombreListes") + " : ", SwingConstants.LEFT);
		labNbreListes.setFont(gras);
		txtNbreListes = new JLabel("", SwingConstants.LEFT);
		txtNbreListes.setFont(normal);

		panel3.add(labNbreListes);
		panel3.add(txtNbreListes);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(panel3, gbc);

		JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel4.setBackground(Color.WHITE);

		JLabel labNbreMessagesCumules = new JLabel(infosProjetPanel.getString("txt_NombreMessagesCumules") + " : ", SwingConstants.LEFT);
		labNbreMessagesCumules.setFont(gras);
		txtCumulMessages = new JLabel("", SwingConstants.LEFT);
		txtCumulMessages.setFont(normal);

		panel4.add(labNbreMessagesCumules);
		panel4.add(txtCumulMessages);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(panel4, gbc);

		return panel;
	}

	@Override
	public JPanel getInfosProjetPanel() {
		return getPanel();
	}

	@Override
	public JMenuBar getMenuBar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getListesListePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLabelProgress(String string) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showActivites() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateProgress() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStepProgress(int step) {
		// TODO Auto-generated method stub

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetProgress() {
		// TODO Auto-generated method stub

	}

	@Override
	public void appendTxtArea(String txt) {
		// TODO Auto-generated method stub

	}

	@Override
	public JProgressBar getProgressBar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void projetChanged(ProjetChangedEvent event) {
		txtNomProjet.setText(event.getNewNom());
		txtRepertoireProjet.setText(event.getNewRepertoire());
		txtNbreListes.setText(String.valueOf(event.getNewNbreListes()));
		txtCumulMessages.setText(String.valueOf(event.getNewCumulMessages()));
	}

	@Override
	public void projetListeAdded(ProjetListeAddedEvent event) {
		txtNbreListes.setText(String.valueOf(event.getNewNbreListes()));
	}

}
