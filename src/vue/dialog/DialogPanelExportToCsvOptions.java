package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class DialogPanelExportToCsvOptions extends JPanel {
	private static final long serialVersionUID = 1L;
	private JCheckBox chkDate, chkLocuteur, chkSujet, chkSujetTronque, chkNumC,
			chkNumM, chkCorpsMessage, chkCorpsMessageSsHTML,
			chkCorpsMessageSsOriginalMessage;
	private ResourceBundle bundleOperationsListe;

	public DialogPanelExportToCsvOptions(ResourceBundle bundleOperationsListe) {
		this.bundleOperationsListe = bundleOperationsListe;
		createGui();
	}

	public void createGui() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setPreferredSize(new Dimension(500, 150));

		// OPTIONS D'EXPORTATION
		GridBagLayout lO = new GridBagLayout();
		JPanel panelOptions = new JPanel();
		panelOptions.setLayout(lO);
		panelOptions.setBorder(BorderFactory
				.createTitledBorder(bundleOperationsListe
						.getString("txt_OptionsExportation")));
		panelOptions.setPreferredSize(new Dimension(500, 150));
		panelOptions.setMinimumSize(new Dimension(500, 150));
		panelOptions.setMaximumSize(new Dimension(500, 150));
		GridBagConstraints cO = new GridBagConstraints();

		// // Panel CHK
		GridBagLayout layout8 = new GridBagLayout();
		JPanel panelChk = new JPanel();
		panelChk.setAlignmentY(Component.LEFT_ALIGNMENT);
		panelChk.setPreferredSize(new Dimension(480, 100));
		panelChk.setMinimumSize(new Dimension(480, 100));
		panelChk.setMaximumSize(new Dimension(480, 100));
		panelChk.setLayout(layout8);
		GridBagConstraints c8 = new GridBagConstraints();

		// // // Export Date
		chkDate = new JCheckBox(
				bundleOperationsListe.getString("txt_ExporterDate"), true);
		chkDate.setPreferredSize(new Dimension(450, 27));
		chkDate.setMinimumSize(new Dimension(450, 27));
		chkDate.setMaximumSize(new Dimension(450, 27));

		c8.gridx = 0;
		c8.gridy = 0;
//		panelChk.add(chkDate, c8);

		// // // Export Locuteur
		chkLocuteur = new JCheckBox(
				bundleOperationsListe.getString("txt_ExporterLocuteur"), true);
		chkLocuteur.setPreferredSize(new Dimension(450, 27));
		chkLocuteur.setMinimumSize(new Dimension(450, 27));
		chkLocuteur.setMaximumSize(new Dimension(450, 27));

		c8.gridx = 0;
		c8.gridy = 1;
//		panelChk.add(chkLocuteur, c8);

		// // // Export Sujet
		chkSujet = new JCheckBox(
				bundleOperationsListe.getString("txt_ExporterSujet"), true);
		chkSujet.setPreferredSize(new Dimension(450, 27));
		chkSujet.setMinimumSize(new Dimension(450, 27));
		chkSujet.setMaximumSize(new Dimension(450, 27));

		c8.gridx = 0;
		c8.gridy = 2;
//		panelChk.add(chkSujet, c8);

		// // // Export Sujet Tronque
		chkSujetTronque = new JCheckBox(
				bundleOperationsListe.getString("txt_ExporterSujetTronque"),
				true);
		chkSujetTronque.setPreferredSize(new Dimension(450, 27));
		chkSujetTronque.setMinimumSize(new Dimension(450, 27));
		chkSujetTronque.setMaximumSize(new Dimension(450, 27));

		c8.gridx = 0;
		c8.gridy = 3;
//		panelChk.add(chkSujetTronque, c8);

		// // // Export Numéro Conversation
		chkNumC = new JCheckBox(
				bundleOperationsListe.getString("txt_ExporterNumConv"), true);
		chkNumC.setPreferredSize(new Dimension(450, 27));
		chkNumC.setMinimumSize(new Dimension(450, 27));
		chkNumC.setMaximumSize(new Dimension(450, 27));

		c8.gridx = 0;
		c8.gridy = 4;
//		panelChk.add(chkNumC, c8);

		// // // Export Numéro Message
		chkNumM = new JCheckBox(
				bundleOperationsListe.getString("txt_ExporterNumMessage"), true);
		chkNumM.setPreferredSize(new Dimension(450, 27));
		chkNumM.setMinimumSize(new Dimension(450, 27));
		chkNumM.setMaximumSize(new Dimension(450, 27));

		c8.gridx = 0;
		c8.gridy = 5;
//		panelChk.add(chkNumM, c8);

		// // // Export du corps de messages
		chkCorpsMessage = new JCheckBox(
				bundleOperationsListe.getString("txt_ExporterCorpsMessage"),
				true);
		chkCorpsMessage.setPreferredSize(new Dimension(450, 27));
		chkCorpsMessage.setMinimumSize(new Dimension(450, 27));
		chkCorpsMessage.setMaximumSize(new Dimension(450, 27));
		chkCorpsMessage.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (chkCorpsMessageSsHTML.isSelected() == true
						|| chkCorpsMessageSsOriginalMessage.isSelected() == true) {
					chkCorpsMessageSsHTML.setSelected(false);
					chkCorpsMessageSsOriginalMessage.setSelected(false);
				}
			}
		});

		c8.gridx = 0;
		c8.gridy = 6;
		panelChk.add(chkCorpsMessage, c8);

		// // // // Suppression du HTML

		chkCorpsMessageSsHTML = new JCheckBox(
				bundleOperationsListe.getString("txt_SupprimerHTML"), true);
		chkCorpsMessageSsHTML.setPreferredSize(new Dimension(450, 27));
		chkCorpsMessageSsHTML.setMinimumSize(new Dimension(450, 27));
		chkCorpsMessageSsHTML.setMaximumSize(new Dimension(450, 27));
		chkCorpsMessageSsHTML.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (chkCorpsMessage.isSelected() == false) {
					chkCorpsMessage.setSelected(true);
				}
			}
		});

		c8.gridx = 0;
		c8.gridy = 7;
		panelChk.add(chkCorpsMessageSsHTML, c8);

		// // // // Nettoyage du corps de message

		chkCorpsMessageSsOriginalMessage = new JCheckBox(
				bundleOperationsListe.getString("txt_SupprimerMessageOrigine"),
				true);
		chkCorpsMessageSsOriginalMessage
				.setPreferredSize(new Dimension(450, 27));
		chkCorpsMessageSsOriginalMessage.setMinimumSize(new Dimension(450, 27));
		chkCorpsMessageSsOriginalMessage.setMaximumSize(new Dimension(450, 27));

		chkCorpsMessageSsOriginalMessage.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (chkCorpsMessage.isSelected() == false) {
					chkCorpsMessage.setSelected(true);
				}
			}
		});

		c8.gridx = 0;
		c8.gridy = 8;
		panelChk.add(chkCorpsMessageSsOriginalMessage, c8);

		cO.gridx = 0;
		cO.gridy = 0;
		panelOptions.add(panelChk, cO);

		add(panelOptions);
	}

	public JCheckBox getChkDate() {
		return chkDate;
	}

	public void setChkDate(JCheckBox chkDate) {
		this.chkDate = chkDate;
	}

	public JCheckBox getChkLocuteur() {
		return chkLocuteur;
	}

	public void setChkLocuteur(JCheckBox chkLocuteur) {
		this.chkLocuteur = chkLocuteur;
	}

	public JCheckBox getChkSujet() {
		return chkSujet;
	}

	public void setChkSujet(JCheckBox chkSujet) {
		this.chkSujet = chkSujet;
	}

	public JCheckBox getChkSujetTronque() {
		return chkSujetTronque;
	}

	public void setChkSujetTronque(JCheckBox chkSujetTronque) {
		this.chkSujetTronque = chkSujetTronque;
	}

	public JCheckBox getChkNumC() {
		return chkNumC;
	}

	public void setChkNumC(JCheckBox chkNumC) {
		this.chkNumC = chkNumC;
	}

	public JCheckBox getChkNumM() {
		return chkNumM;
	}

	public void setChkNumM(JCheckBox chkNumM) {
		this.chkNumM = chkNumM;
	}

	public JCheckBox getChkCorpsMessage() {
		return chkCorpsMessage;
	}

	public void setChkCorpsMessage(JCheckBox chkCorpsMessage) {
		this.chkCorpsMessage = chkCorpsMessage;
	}

	public JCheckBox getChkCorpsMessageSsHTML() {
		return chkCorpsMessageSsHTML;
	}

	public void setChkCorpsMessageSsHTML(JCheckBox chkCorpsMessageSsHTML) {
		this.chkCorpsMessageSsHTML = chkCorpsMessageSsHTML;
	}

	public JCheckBox getChkCorpsMessageSsOriginalMessage() {
		return chkCorpsMessageSsOriginalMessage;
	}

	public void setChkCorpsMessageSsOriginalMessage(
			JCheckBox chkCorpsMessageSsOriginalMessage) {
		this.chkCorpsMessageSsOriginalMessage = chkCorpsMessageSsOriginalMessage;
	}

}
