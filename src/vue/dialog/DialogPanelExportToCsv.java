package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import extra.ListTransferHandler;
import modeles.LocuteurModel;

public class DialogPanelExportToCsv extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTextArea txtALocuteursSelectionnes;
	private JTextField txtFDate1, txtFDate2, txtFNbreLocuteurs1, txtFNbreLocuteurs2, txtFNbreMessages1, txtFNbreMessages2, txtFNumerosMessages, txtFMotsSujet, txtFMotsCorps;
	private JCheckBox chkEtOuMotsSujet, chkEtOuMotsCorps, chkNonIntegrationMessageOriginal;
	private Set<LocuteurModel> setLocuteurs;
	private ResourceBundle bundleOperationsListe;
	private JRadioButton bExporterTout;

	public DialogPanelExportToCsv(Set<LocuteurModel> setLocuteurs, ResourceBundle bundleOperationsListe) {
		this.setLocuteurs = setLocuteurs;
		this.bundleOperationsListe = bundleOperationsListe;
		createGui();
	}

	public void createGui() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setPreferredSize(new Dimension(700, 630));

		// EXPORTER TOUT
		JPanel panelExporterTout = new JPanel();
		panelExporterTout.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelExporterTout.setOpaque(true);
		panelExporterTout.setMinimumSize(new Dimension(700, 30));
		bExporterTout = new JRadioButton(" Exporter tout ");
		panelExporterTout.add(bExporterTout);

		// SELON MESSAGES

		GridBagLayout lM = new GridBagLayout();
		JPanel panelMessages = new JPanel();
		panelMessages.setLayout(lM);
		panelMessages.setBorder(BorderFactory.createTitledBorder(bundleOperationsListe.getString("txt_SelectionSelonMessages")));
		panelMessages.setPreferredSize(new Dimension(700, 550));
		GridBagConstraints cM = new GridBagConstraints();

		// // Locuteurs

		GridBagLayout layout1 = new GridBagLayout();
		JPanel panelLocuteurs = new JPanel();
		panelLocuteurs.setLayout(layout1);
		panelLocuteurs.setBorder(BorderFactory.createTitledBorder(bundleOperationsListe.getString("txt_LocuteursSeparesPar")));
		panelLocuteurs.setPreferredSize(new Dimension(680, 200));
		panelLocuteurs.setMinimumSize(new Dimension(680, 200));
		panelLocuteurs.setMaximumSize(new Dimension(680, 200));
		GridBagConstraints c1 = new GridBagConstraints();

		// // // Locuteurs sélectionnés

		txtALocuteursSelectionnes = new JTextArea();
		txtALocuteursSelectionnes.setDragEnabled(true);
		JScrollPane scrollLocuteursSelectionnes = new JScrollPane(txtALocuteursSelectionnes);
		scrollLocuteursSelectionnes.setPreferredSize(new Dimension(310, 150));

		c1.gridx = 0;
		c1.gridy = 0;
		panelLocuteurs.add(scrollLocuteursSelectionnes, c1);

		// // // Flèche

		JLabel labelFleche = new JLabel("<=");
		labelFleche.setPreferredSize(new Dimension(20, 150));
		c1.gridx = 1;
		c1.gridy = 0;
		panelLocuteurs.add(labelFleche, c1);

		// // // Liste des locuteurs

		List<String> listNomL = new ArrayList<String>();
		for (LocuteurModel locuteur : setLocuteurs)
			listNomL.add(locuteur.getNom());
		Collections.sort(listNomL);
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for (String nomL : listNomL)
			listModel.addElement(nomL);
		JList<String> jListLocuteurs = new JList<String>(listModel);
		jListLocuteurs.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		jListLocuteurs.setDragEnabled(true);
		jListLocuteurs.setTransferHandler(new ListTransferHandler());

		final JScrollPane scrollListeLocuteurs = new JScrollPane(jListLocuteurs);
		scrollListeLocuteurs.setPreferredSize(new Dimension(310, 150));

		c1.gridx = 2;
		c1.gridy = 0;
		panelLocuteurs.add(scrollListeLocuteurs, c1);

		cM.gridx = 0;
		cM.gridy = 0;
		panelMessages.add(panelLocuteurs, cM);

		// // Date

		GridBagLayout layout2 = new GridBagLayout();
		JPanel panelDate = new JPanel();
		panelDate.setBorder(BorderFactory.createTitledBorder("Date (JJ/MM/AAAA)"));
		panelDate.setAlignmentY(Component.LEFT_ALIGNMENT);
		panelDate.setPreferredSize(new Dimension(680, 60));
		panelDate.setMinimumSize(new Dimension(680, 60));
		panelDate.setMaximumSize(new Dimension(680, 60));
		panelDate.setLayout(layout2);
		GridBagConstraints c2 = new GridBagConstraints();

		JLabel labelDate1 = new JLabel(bundleOperationsListe.getString("txt_Du") + " : ");

		c2.gridx = 0;
		c2.gridy = 0;
		panelDate.add(labelDate1, c2);

		txtFDate1 = new JTextField();
		txtFDate1.setPreferredSize(new Dimension(120, 27));
		txtFDate1.setMinimumSize(new Dimension(120, 27));
		txtFDate1.setMaximumSize(new Dimension(120, 27));

		c2.gridx = 1;
		c2.gridy = 0;
		panelDate.add(txtFDate1, c2);

		JLabel labelDate2 = new JLabel(" " + bundleOperationsListe.getString("txt_InclusAu") + " : ");

		c2.gridx = 2;
		c2.gridy = 0;
		panelDate.add(labelDate2, c2);

		txtFDate2 = new JTextField();
		txtFDate2.setPreferredSize(new Dimension(120, 27));
		txtFDate2.setMinimumSize(new Dimension(120, 27));
		txtFDate2.setMaximumSize(new Dimension(120, 27));

		c2.gridx = 3;
		c2.gridy = 0;
		panelDate.add(txtFDate2, c2);

		JLabel labelDate3 = new JLabel(" " + bundleOperationsListe.getString("txt_Inclus"));
		labelDate3.setPreferredSize(new Dimension(300, 27));
		labelDate3.setMinimumSize(new Dimension(300, 27));
		labelDate3.setMaximumSize(new Dimension(300, 27));

		c2.gridx = 4;
		c2.gridy = 0;
		panelDate.add(labelDate3, c2);

		cM.gridx = 0;
		cM.gridy = 1;
		panelMessages.add(panelDate, cM);

		// // Numeros de messages

		GridBagLayout layout7 = new GridBagLayout();
		JPanel panelNumeroMessages = new JPanel();
		panelNumeroMessages.setAlignmentY(Component.LEFT_ALIGNMENT);
		panelNumeroMessages.setBorder(BorderFactory.createTitledBorder(bundleOperationsListe.getString("txt_NumerosMessagesSepares")));
		panelNumeroMessages.setPreferredSize(new Dimension(680, 60));
		panelNumeroMessages.setMinimumSize(new Dimension(680, 60));
		panelNumeroMessages.setMaximumSize(new Dimension(680, 60));
		panelNumeroMessages.setLayout(layout7);
		GridBagConstraints c7 = new GridBagConstraints();

		JLabel labelNumeroMessages = new JLabel(bundleOperationsListe.getString("txt_Numeros") + " : ");

		c7.gridx = 0;
		c7.gridy = 1;
		panelNumeroMessages.add(labelNumeroMessages, c7);

		txtFNumerosMessages = new JTextField();
		txtFNumerosMessages.setPreferredSize(new Dimension(560, 27));
		txtFNumerosMessages.setMinimumSize(new Dimension(560, 27));
		txtFNumerosMessages.setMaximumSize(new Dimension(560, 27));

		c7.gridx = 1;
		c7.gridy = 1;
		panelNumeroMessages.add(txtFNumerosMessages, c7);

		cM.gridx = 0;
		cM.gridy = 2;
		panelMessages.add(panelNumeroMessages, cM);

		// // Mots dans le sujet

		GridBagLayout layout5 = new GridBagLayout();
		JPanel panelMotsSujet = new JPanel();
		panelMotsSujet.setAlignmentY(Component.LEFT_ALIGNMENT);
		panelMotsSujet.setBorder(BorderFactory.createTitledBorder(bundleOperationsListe.getString("txt_MotsSujetSepares")));
		panelMotsSujet.setPreferredSize(new Dimension(680, 60));
		panelMotsSujet.setMinimumSize(new Dimension(680, 60));
		panelMotsSujet.setMaximumSize(new Dimension(680, 60));
		panelMotsSujet.setLayout(layout5);
		GridBagConstraints c5 = new GridBagConstraints();

		JLabel labelMotsSujet = new JLabel(bundleOperationsListe.getString("txt_Mots") + " : ");

		c5.gridx = 0;
		c5.gridy = 0;
		panelMotsSujet.add(labelMotsSujet, c5);

		txtFMotsSujet = new JTextField();
		txtFMotsSujet.setPreferredSize(new Dimension(532, 27));
		txtFMotsSujet.setMinimumSize(new Dimension(532, 27));
		txtFMotsSujet.setMaximumSize(new Dimension(532, 27));

		c5.gridx = 1;
		c5.gridy = 0;
		panelMotsSujet.add(txtFMotsSujet, c5);

		chkEtOuMotsSujet = new JCheckBox(";=" + bundleOperationsListe.getString("txt_Et"), false);

		c5.gridx = 2;
		c5.gridy = 0;
		panelMotsSujet.add(chkEtOuMotsSujet, c5);

		cM.gridx = 0;
		cM.gridy = 3;
		panelMessages.add(panelMotsSujet, cM);

		// // Corps de messages

		GridBagLayout layout6 = new GridBagLayout();
		JPanel panelMotsCorps = new JPanel();
		panelMotsCorps.setAlignmentY(Component.LEFT_ALIGNMENT);
		panelMotsCorps.setPreferredSize(new Dimension(680, 80));
		panelMotsCorps.setMinimumSize(new Dimension(680, 80));
		panelMotsCorps.setMaximumSize(new Dimension(680, 80));
		panelMotsCorps.setBorder(BorderFactory.createTitledBorder(bundleOperationsListe.getString("txt_MotsCorpsSepares")));
		panelMotsCorps.setLayout(layout6);
		GridBagConstraints c6 = new GridBagConstraints();

		// // // Mots dans corps de messages

		JLabel labelMotsCorps = new JLabel(bundleOperationsListe.getString("txt_Mots") + " : ");

		c6.gridx = 0;
		c6.gridy = 0;
		panelMotsCorps.add(labelMotsCorps, c6);

		txtFMotsCorps = new JTextField();
		txtFMotsCorps.setPreferredSize(new Dimension(532, 27));
		txtFMotsCorps.setMinimumSize(new Dimension(532, 27));
		txtFMotsCorps.setMaximumSize(new Dimension(532, 27));

		c6.gridx = 1;
		c6.gridy = 0;
		panelMotsCorps.add(txtFMotsCorps, c6);

		chkEtOuMotsCorps = new JCheckBox(";=" + bundleOperationsListe.getString("txt_Et"), false);

		c6.gridx = 2;
		c6.gridy = 0;
		panelMotsCorps.add(chkEtOuMotsCorps, c6);

		chkNonIntegrationMessageOriginal = new JCheckBox(bundleOperationsListe.getString("txt_MessageOriginal"), true);

		c6.gridx = 1;
		c6.gridy = 1;
		panelMotsCorps.add(chkNonIntegrationMessageOriginal, c6);

		cM.gridx = 0;
		cM.gridy = 4;
		panelMessages.add(panelMotsCorps, cM);

		// CONVERSATIONS

		GridBagLayout lC = new GridBagLayout();
		JPanel panelConversations = new JPanel();
		panelConversations.setLayout(lC);
		panelConversations.setBorder(BorderFactory.createTitledBorder(bundleOperationsListe.getString("txt_SelectConv")));
		panelConversations.setPreferredSize(new Dimension(700, 100));
		panelConversations.setMinimumSize(new Dimension(700, 100));
		panelConversations.setMaximumSize(new Dimension(700, 100));
		GridBagConstraints cC = new GridBagConstraints();

		// // Nombre de locuteurs dans conversations

		GridBagLayout lC1 = new GridBagLayout();
		JPanel panelC1 = new JPanel();
		panelC1.setLayout(lC1);
		panelC1.setPreferredSize(new Dimension(680, 30));
		panelC1.setMinimumSize(new Dimension(680, 30));
		panelC1.setMaximumSize(new Dimension(680, 30));
		GridBagConstraints cC1 = new GridBagConstraints();

		JLabel labelNbreLocuteurs = new JLabel(bundleOperationsListe.getString("txt_NbreLocuteursSupEgal") + " : ");

		cC1.gridx = 0;
		cC1.gridy = 0;
		panelC1.add(labelNbreLocuteurs, cC1);

		txtFNbreLocuteurs1 = new JTextField();
		txtFNbreLocuteurs1.setPreferredSize(new Dimension(130, 27));

		cC1.gridx = 1;
		cC1.gridy = 0;
		panelC1.add(txtFNbreLocuteurs1, cC1);

		JLabel labelNbreLocuteurs2 = new JLabel(" " + bundleOperationsListe.getString("txt_InferieurEgal") + " : ");

		cC1.gridx = 2;
		cC1.gridy = 0;
		panelC1.add(labelNbreLocuteurs2, cC1);

		txtFNbreLocuteurs2 = new JTextField();
		txtFNbreLocuteurs2.setPreferredSize(new Dimension(130, 27));

		cC1.gridx = 3;
		cC1.gridy = 0;
		panelC1.add(txtFNbreLocuteurs2, cC1);

		cC.gridx = 0;
		cC.gridy = 0;
		panelConversations.add(panelC1, cC);

		// // Nombre de messages dans conversations

		GridBagLayout lC2 = new GridBagLayout();
		JPanel panelC2 = new JPanel();
		panelC2.setLayout(lC2);
		panelC2.setPreferredSize(new Dimension(680, 30));
		panelC2.setMinimumSize(new Dimension(680, 30));
		panelC2.setMaximumSize(new Dimension(680, 30));
		GridBagConstraints cC2 = new GridBagConstraints();

		JLabel labelNbreMessages = new JLabel(bundleOperationsListe.getString("txt_NbreMessagesSupEgal") + " : ");

		cC2.gridx = 0;
		cC2.gridy = 0;
		panelC2.add(labelNbreMessages, cC2);

		txtFNbreMessages1 = new JTextField();
		txtFNbreMessages1.setPreferredSize(new Dimension(127, 27));

		cC2.gridx = 1;
		cC2.gridy = 0;
		panelC2.add(txtFNbreMessages1, cC2);

		JLabel labelNbreMessages2 = new JLabel(" " + bundleOperationsListe.getString("txt_InferieurEgal") + " : ");

		cC2.gridx = 2;
		cC2.gridy = 0;
		panelC2.add(labelNbreMessages2, cC2);

		txtFNbreMessages2 = new JTextField();
		txtFNbreMessages2.setPreferredSize(new Dimension(127, 27));

		cC2.gridx = 3;
		cC2.gridy = 0;
		panelC2.add(txtFNbreMessages2, cC2);

		cC.gridx = 0;
		cC.gridy = 1;
		panelConversations.add(panelC2, cC);

		bExporterTout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JRadioButton source = (JRadioButton) e.getSource();
				if (source.isSelected()) {
					txtALocuteursSelectionnes.setEnabled(false);
					txtFDate1.setEnabled(false);
					txtFDate2.setEnabled(false);
					txtFNbreLocuteurs1.setEnabled(false);
					txtFNbreLocuteurs2.setEnabled(false);
					txtFNbreMessages1.setEnabled(false);
					txtFNbreMessages2.setEnabled(false);
					txtFNumerosMessages.setEnabled(false);
					txtFMotsSujet.setEnabled(false);
					txtFMotsCorps.setEnabled(false);
					chkEtOuMotsSujet.setEnabled(false);
					chkEtOuMotsCorps.setEnabled(false);
					chkNonIntegrationMessageOriginal.setEnabled(false);
				} else {
					txtALocuteursSelectionnes.setEnabled(true);
					txtFDate1.setEnabled(true);
					txtFDate2.setEnabled(true);
					txtFNbreLocuteurs1.setEnabled(true);
					txtFNbreLocuteurs2.setEnabled(true);
					txtFNbreMessages1.setEnabled(true);
					txtFNbreMessages2.setEnabled(true);
					txtFNumerosMessages.setEnabled(true);
					txtFMotsSujet.setEnabled(true);
					txtFMotsCorps.setEnabled(true);
					chkEtOuMotsSujet.setEnabled(true);
					chkEtOuMotsCorps.setEnabled(true);
					chkNonIntegrationMessageOriginal.setEnabled(true);
				}
			}
		});
		
		bExporterTout.setSelected(true);		
		txtALocuteursSelectionnes.setEnabled(false);
		txtFDate1.setEnabled(false);
		txtFDate2.setEnabled(false);
		txtFNbreLocuteurs1.setEnabled(false);
		txtFNbreLocuteurs2.setEnabled(false);
		txtFNbreMessages1.setEnabled(false);
		txtFNbreMessages2.setEnabled(false);
		txtFNumerosMessages.setEnabled(false);
		txtFMotsSujet.setEnabled(false);
		txtFMotsCorps.setEnabled(false);
		chkEtOuMotsSujet.setEnabled(false);
		chkEtOuMotsCorps.setEnabled(false);
		chkNonIntegrationMessageOriginal.setEnabled(false);

		add(panelExporterTout);
		add(panelMessages);
		add(panelConversations);
	}
	
	public boolean getExporterToutIsSelected() {
		return bExporterTout.isSelected();
	}

	public JTextArea getTxtALocuteursSelectionnes() {
		return txtALocuteursSelectionnes;
	}

	public void setTxtALocuteursSelectionnes(JTextArea txtALocuteursSelectionnes) {
		this.txtALocuteursSelectionnes = txtALocuteursSelectionnes;
	}

	public JTextField getTxtFDate1() {
		return txtFDate1;
	}

	public void setTxtFDate1(JTextField txtFDate1) {
		this.txtFDate1 = txtFDate1;
	}

	public JTextField getTxtFDate2() {
		return txtFDate2;
	}

	public void setTxtFDate2(JTextField txtFDate2) {
		this.txtFDate2 = txtFDate2;
	}

	public JTextField getTxtFNbreLocuteurs1() {
		return txtFNbreLocuteurs1;
	}

	public void setTxtFNbreLocuteurs1(JTextField txtFNbreLocuteurs1) {
		this.txtFNbreLocuteurs1 = txtFNbreLocuteurs1;
	}

	public JTextField getTxtFNbreLocuteurs2() {
		return txtFNbreLocuteurs2;
	}

	public void setTxtFNbreLocuteurs2(JTextField txtFNbreLocuteurs2) {
		this.txtFNbreLocuteurs2 = txtFNbreLocuteurs2;
	}

	public JTextField getTxtFNbreMessages1() {
		return txtFNbreMessages1;
	}

	public void setTxtFNbreMessages1(JTextField txtFNbreMessages1) {
		this.txtFNbreMessages1 = txtFNbreMessages1;
	}

	public JTextField getTxtFNbreMessages2() {
		return txtFNbreMessages2;
	}

	public void setTxtFNbreMessages2(JTextField txtFNbreMessages2) {
		this.txtFNbreMessages2 = txtFNbreMessages2;
	}

	public JTextField getTxtFNumerosMessages() {
		return txtFNumerosMessages;
	}

	public void setTxtFNumerosMessages(JTextField txtFNumerosMessages) {
		this.txtFNumerosMessages = txtFNumerosMessages;
	}

	public JTextField getTxtFMotsSujet() {
		return txtFMotsSujet;
	}

	public void setTxtFMotsSujet(JTextField txtFMotsSujet) {
		this.txtFMotsSujet = txtFMotsSujet;
	}

	public JTextField getTxtFMotsCorps() {
		return txtFMotsCorps;
	}

	public void setTxtFMotsCorps(JTextField txtFMotsCorps) {
		this.txtFMotsCorps = txtFMotsCorps;
	}

	public JCheckBox getChkEtOuMotsSujet() {
		return chkEtOuMotsSujet;
	}

	public void setChkEtOuMotsSujet(JCheckBox chkEtOuMotsSujet) {
		this.chkEtOuMotsSujet = chkEtOuMotsSujet;
	}

	public JCheckBox getChkEtOuMotsCorps() {
		return chkEtOuMotsCorps;
	}

	public void setChkEtOuMotsCorps(JCheckBox chkEtOuMotsCorps) {
		this.chkEtOuMotsCorps = chkEtOuMotsCorps;
	}

	public JCheckBox getChkNonIntegrationMessageOriginal() {
		return chkNonIntegrationMessageOriginal;
	}

	public void setChkNonIntegrationMessageOriginal(JCheckBox chkNonIntegrationMessageOriginal) {
		this.chkNonIntegrationMessageOriginal = chkNonIntegrationMessageOriginal;
	}

}
