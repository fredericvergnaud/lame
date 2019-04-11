package vue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingWorker;

import modeles.evenements.ListeChangedEvent;
import controleurs.ListeController;
import controleurs.vuesabstraites.ListeView;

public class ToolBar extends ListeView implements ActionListener {

	// private ProjetController projetController;
	JButton bExportListe, bExportLocuteursToPdf, bAddMessages, bParametres;

	public ToolBar(ListeController listeController) {
		super(listeController);
	}

	// CREATION DE LA BARRE DE MENU
	@Override
	public JToolBar getToolBar() {

		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		toolbar.setBorderPainted(false);

		// Liste 2 CSV
		bExportListe = new JButton(new ImageIcon(ToolBar.class.getResource("/images/icones/liste_2_csv.png")));
		bExportListe.setFocusPainted(false);
		bExportListe.setRolloverEnabled(false);
		bExportListe.addActionListener(this);
		bExportListe.setEnabled(false);

//		// locuteurs 2 PDF
//		bExportLocuteursToPdf = new JButton(new ImageIcon(ToolBar.class.getResource("/images/icones/locuteurs_2_pdf.png")));
//		bExportLocuteursToPdf.setFocusPainted(false);
//		bExportLocuteursToPdf.setRolloverEnabled(false);
//		bExportLocuteursToPdf.addActionListener(this);
//		bExportLocuteursToPdf.setEnabled(false);

		// conversations 2 PDF
		bAddMessages = new JButton(new ImageIcon(ToolBar.class.getResource("/images/icones/add_messages.png")));
		bAddMessages.setFocusPainted(false);
		bAddMessages.setRolloverEnabled(false);
		bAddMessages.addActionListener(this);
		bAddMessages.setEnabled(false);

		// Parametres
		bParametres = new JButton(new ImageIcon(ToolBar.class.getResource("/images/icones/parametres.png")));
		bParametres.setFocusPainted(false);
		bParametres.setRolloverEnabled(false);
		bParametres.addActionListener(this);
		bParametres.setEnabled(false);

		toolbar.add(bAddMessages);
		toolbar.add(bParametres);
		toolbar.add(bExportListe);
		//toolbar.add(bExportLocuteursToPdf);
		

		return toolbar;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		// EXPORT MESSAGES
		if (source == bExportListe) {
			new SwingWorker<Object, Object>() {
				@Override
				public Object doInBackground() {
					getListeController().notifyExportToCSV();
					return null;
				}

				@Override
				public void done() {
				}
			}.execute();
		}

//		else if (source == bExportLocuteursToPdf) {
//			getListeController().notifyExportLocuteursToPdf();
//		}

		else if (source == bAddMessages) {
			getListeController().notifyAddMessages(getListeController().getProjetController().getRepertoire());
		}

		else if (source == bParametres) {
			getListeController().notifyAnalyseData();
		}
	}

	// @Override
	// public void projetCreated(ProjetCreatedEvent event) {
	// }
	//
	// @Override
	// public void projetListeAdded(ProjetListeAddedEvent event) {
	// }
	//
	// @Override
	// public void projetListeSelected(ProjetListeSelectedEvent event) {
	// //
	// System.out.println("LISTE SELECTED : listeController = "+listeController);
	// listeController = getProjetController().getListeController();
	// if (event.getListeSelected().getNumero() != 0
	// && event.getListeSelected().getNbreMessages() > 0) {
	// bExportMessages.setEnabled(true);
	// bParametres.setEnabled(true);
	// } else {
	// bExportMessages.setEnabled(false);
	// }
	// if (event.getListeSelected().getNumero() != 0
	// && event.getListeSelected().getNbreLocuteurs() > 0) {
	// bExportLocuteursToPdf.setEnabled(true);
	// bExportConversationsToPdf.setEnabled(true);
	// bParametres.setEnabled(true);
	// } else {
	// bExportLocuteursToPdf.setEnabled(false);
	// bExportConversationsToPdf.setEnabled(false);
	// bParametres.setEnabled(false);
	// }
	// }

	@Override
	public void listeChanged(ListeChangedEvent event) {
		// System.out.println("ToolBar - listeChanged : nom listeSelected = "
		// + event.getNewNom());
		// System.out.println("ToolBar - listeChanged : nbreMessages = "
		// + event.getNewNbreMessages());
		// System.out.println("ToolBar - listeChanged : numero = "
		// + event.getNewNumero());
		// System.out.println("ToolBar - listeChanged : nbreLocuteurs = "
		// + event.getNewNbreLocuteurs());

		if (event.getNewSetLocuteurs().size() > 0 && event.getNewSetConversations().size() > 0)
			bExportListe.setEnabled(true);
		else
			bExportListe.setEnabled(false);

//		if (event.getNewSetLocuteurs().size() > 0)
//			bExportLocuteursToPdf.setEnabled(true);
//		else
//			bExportLocuteursToPdf.setEnabled(false);

		if (event.getNewNumero() != 0)
			bAddMessages.setEnabled(true);
		else
			bAddMessages.setEnabled(false);

		if (event.getNewMapIdMessages().size() > 0)
			bParametres.setEnabled(true);
		else
			bParametres.setEnabled(false);
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
	public JPanel getInfosListePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getFilsListePanel() {
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
	public void setIdentifiantMessageToShow(String identifiantMessage) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public JPanel getTabLocuteursPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}
