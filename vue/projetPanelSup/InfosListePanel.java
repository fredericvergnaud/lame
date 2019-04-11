package vue.projetPanelSup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import renderers.RoundedPanel;
import modeles.evenements.ListeChangedEvent;
import controleurs.ListeController;
import controleurs.vuesabstraites.ListeView;

public class InfosListePanel extends ListeView {

	private JPanel panelHaut;
	private JLabel txtNumListe, txtNomListe, txtDateAjoutListe, txtNbreFichiersListe, labNumListe, labNomListe, labDateAjoutListe, labNbreFichiersListe;
	private RoundedPanel panel;
	private ResourceBundle infosListe;
//	private Color panelBackgroundColor = new Color(239,235,231);

	public InfosListePanel(ListeController controller, ResourceBundle infosListe) {
		super(controller);
		this.infosListe = infosListe;
	}

	public JPanel getPanel() {
		panel = new RoundedPanel();
		panel.setPreferredSize(new Dimension(300, 100));
		panel.setMaximumSize(new Dimension(300, 100));
		panel.setMinimumSize(new Dimension(300, 100));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0,10,0,10);
		gbc.ipadx = 10;
		
		Font gras = new Font("sansserif", Font.BOLD, 12);
		Font normal = new Font("sansserif", Font.PLAIN, 12);
	
		JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel1.setBackground(Color.WHITE);
	
		labNumListe = new JLabel("", SwingConstants.LEFT);
		labNumListe.setFont(gras);
		txtNumListe = new JLabel("", SwingConstants.LEFT);
		txtNumListe.setFont(normal);
	
		panel1.add(labNumListe);
		panel1.add(txtNumListe);
	
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(panel1, gbc);
	
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.WHITE);
	
		labNomListe = new JLabel("", SwingConstants.LEFT);
		labNomListe.setFont(gras);
		txtNomListe = new JLabel("", SwingConstants.LEFT);
		txtNomListe.setFont(normal);
	
		panel2.add(labNomListe);
		panel2.add(txtNomListe);
	
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(panel2, gbc);
	
		JPanel panel3 = new JPanel();
		panel3.setBackground(Color.WHITE);
	
		labDateAjoutListe = new JLabel("", SwingConstants.LEFT);
		labDateAjoutListe.setFont(gras);
		txtDateAjoutListe = new JLabel("", SwingConstants.LEFT);
		txtDateAjoutListe.setFont(normal);
	
		panel3.add(labDateAjoutListe);
		panel3.add(txtDateAjoutListe);
	
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(panel3, gbc);
	
		JPanel panel4 = new JPanel();
		panel4.setBackground(Color.WHITE);
	
		labNbreFichiersListe = new JLabel("", SwingConstants.LEFT);
		labNbreFichiersListe.setFont(gras);
		txtNbreFichiersListe = new JLabel("", SwingConstants.LEFT);
		txtNbreFichiersListe.setFont(normal);
	
		panel4.add(labNbreFichiersListe);
		panel4.add(txtNbreFichiersListe);
	
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(panel4, gbc);
	
		return panel;
	}

	@Override
	public JPanel getInfosListePanel() {
		return getPanel();
	}

	@Override
	public void listeChanged(ListeChangedEvent event) {
		// System.out.println("InfoListePanel - listeChanged : num listeSelected = "+event.getNewNumero());
		if (event.getNewNumero() != 0) {
			labNumListe.setText(" "+infosListe.getString("txt_Numero") + " : ");
			txtNumListe.setText(String.valueOf(event.getNewNumero()));
			labNomListe.setText(infosListe.getString("txt_Nom") + " : ");
			txtNomListe.setText(event.getNewNom());
			labDateAjoutListe.setText(infosListe.getString("txt_DateAjout") + " : ");
			txtDateAjoutListe.setText(event.getNewDateAjout());
			labNbreFichiersListe.setText(infosListe.getString("txt_NbreMessages") + " : ");
			txtNbreFichiersListe.setText(String.valueOf(event.getNewMapIdMessages().size()));
		} else {
			labNumListe.setText("");
			txtNumListe.setText("");
			labNomListe.setText("");
			txtNomListe.setText("");
			labDateAjoutListe.setText("");
			txtDateAjoutListe.setText("");
			labNbreFichiersListe.setText("");
			txtNbreFichiersListe.setText("");
		}
	}

	@Override
	public JPanel getFilsListePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getTabConversationsPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getConversationsTxtToPdf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getAnalysePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocuteursTxtToPdf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getTabMessagesPanel() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public JPanel getTabLocuteursPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIdentifiantMessageToShow(String identifiantMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public JToolBar getToolBar() {
		// TODO Auto-generated method stub
		return null;
	}

}
