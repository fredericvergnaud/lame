package controleurs.operations.liste;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.SortedSet;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import modeles.ConversationModel;
import modeles.LocuteurModel;
import modeles.tableaux.TabConversationsModel;
import modeles.tableaux.TabLocuteursModel;
import controleurs.vuesabstraites.ProjetView;
import extra.CustomJFileChooser;
import extra.Pdf;

public class ExportToPDF {

	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private String nomProjet, repertoireProjet;
	private DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	// private String[] columnNamesLocuteurs, columnNamesConversations;
	private String nom;
	private SortedSet<LocuteurModel> setLocuteurs;
	private SortedSet<ConversationModel> setConversations;

	public ExportToPDF(ProjetView activitesView, ResourceBundle bundleOperationsListe, String nomProjet, String repertoireProjet, String nom, SortedSet<LocuteurModel> setLocuteurs,
			SortedSet<ConversationModel> setConversations) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.nomProjet = nomProjet;
		this.repertoireProjet = repertoireProjet;
		// this.columnNamesLocuteurs = new String[] {
		// bundleOperationsListe.getString("txt_Nom"),
		// bundleOperationsListe.getString("txt_Debut"),
		// bundleOperationsListe.getString("txt_Fin"),
		// "<html><center>"
		// + bundleOperationsListe
		// .getString("txt_DureeParticipation") + "<br>("
		// + bundleOperationsListe.getString("txt_Jours")
		// + ")</br></center></html>",
		// "<html><center>"
		// + bundleOperationsListe.getString("txt_Nombre")
		// + "<br>"
		// + bundleOperationsListe.getString("txt_MessagesMin")
		// + "</br></center></html>",
		// bundleOperationsListe.getString("txt_Intensite"),
		// "<html><center>"
		// + bundleOperationsListe
		// .getString("txt_LocuteurDominant")
		// + "</br></center></html>",
		// "<html><center>"
		// + bundleOperationsListe.getString("txt_Nombre")
		// + "<br>"
		// + bundleOperationsListe.getString("txt_Conversations")
		// + "</br></center></html>",
		// "<html><center>"
		// + bundleOperationsListe.getString("txt_Nombre") + " "
		// + bundleOperationsListe.getString("txt_SCLances")
		// + "</br></center></html>",
		// "<html><center>"
		// + bundleOperationsListe.getString("txt_Nombre")
		// + "<br>" + bundleOperationsListe.getString("txt_SC")
		// + "</br></center></html>",
		// "<html><center>"
		// + bundleOperationsListe.getString("txt_Nombre") + " "
		// + bundleOperationsListe.getString("txt_MessagesSC")
		// + "</br></center></html>",
		// "<html><center>"
		// + bundleOperationsListe
		// .getString("txt_PourcentTotalMessages")
		// + "</br></center></html>" };
		// this.columnNamesConversations = new String[] {
		// bundleOperationsListe.getString("txt_Conversation"),
		// bundleOperationsListe.getString("txt_SujetPremierMessage"),
		// bundleOperationsListe.getString("txt_NumeroPremierMessage"),
		// bundleOperationsListe.getString("txt_Debut"),
		// bundleOperationsListe.getString("txt_Fin"),
		// bundleOperationsListe.getString("txt_Duree"),
		// "<html><center>"
		// + bundleOperationsListe.getString("txt_Nombre")
		// + "<br>"
		// + bundleOperationsListe.getString("txt_MessagesMin")
		// + "</br></center></html>",
		// "<html><center>"
		// + bundleOperationsListe.getString("txt_Nombre") + " "
		// + bundleOperationsListe.getString("txt_Locuteurs")
		// + "</br></center></html>",
		// bundleOperationsListe.getString("txt_SC"),
		// bundleOperationsListe.getString("txt_Lanceur") };
		this.nom = nom;
		this.setConversations = setConversations;
		this.setLocuteurs = setLocuteurs;
	}

	public void exportLocuteursToPdf(String locuteursTxtToPdf) {
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_LocuteursPDF") + " - " + bundleOperationsListe.getString("txt_Patientez"));
		activitesView.getProgressBar().setIndeterminate(true);
		activitesView.getProgressBar().setStringPainted(false);
		activitesView.updateProgress();

		TabLocuteursModel model = new TabLocuteursModel(setLocuteurs, bundleOperationsListe);
		String[] headers = new String[model.getColumnCount()];
		for (int i = 0; i < model.getColumnCount(); i++)
			headers[i] = model.getColumnName(i);
		CustomJFileChooser customChooser = new CustomJFileChooser("tabLocuteursToPdf", bundleOperationsListe, repertoireProjet, false);
		customChooser.show();
		if (customChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION && customChooser.getTxtFile() != null) {
			String txtFile = customChooser.getTxtFile();
			File f = new File(txtFile);
			if (!f.exists()) {
				createNewLocuteursPdf(txtFile, locuteursTxtToPdf, headers, model);
			} else {
				int diag = JOptionPane.showOptionDialog(null, bundleOperationsListe.getString("txt_EcraserFichierExistant") + " ?", bundleOperationsListe.getString("txt_ExporterVers"),
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (diag == JOptionPane.OK_OPTION) {
					createNewLocuteursPdf(txtFile, locuteursTxtToPdf, headers, model);
				} else
					exportLocuteursToPdf(locuteursTxtToPdf);
			}
		} else {
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_LocuteursPDF"));
			activitesView.getProgressBar().setIndeterminate(false);
			activitesView.getProgressBar().setStringPainted(true);
		}
	}

	private void createNewLocuteursPdf(String monFichier, String locuteursTxtToPdf, String[] headers, TabLocuteursModel model) {
		Pdf pdf = new Pdf(monFichier, bundleOperationsListe.getString("txt_DocumentGenere") + " " + format.format(new Date()) + "\nProjet : " + nomProjet + "\nListe : " + nom + "\n\n"
				+ bundleOperationsListe.getString("txt_StatsLocuteurs") + "\n\n", locuteursTxtToPdf, bundleOperationsListe.getString("txt_TabLocuteurs"), headers, model);
		pdf.createPdf();
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_LocuteursPDF"));
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_LocuteursPDF") + " " + bundleOperationsListe.getString("txt_Accomplie") + "\n");
		activitesView.getProgressBar().setIndeterminate(false);
		activitesView.getProgressBar().setStringPainted(true);
	}

	public void exportConversationsToPdf(String conversationsTxtToPdf) {
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_ConversationsPDF") + " - " + bundleOperationsListe.getString("txt_Patientez"));
		activitesView.getProgressBar().setIndeterminate(true);
		activitesView.getProgressBar().setStringPainted(false);
		activitesView.updateProgress();

		TabConversationsModel model = new TabConversationsModel(setConversations, bundleOperationsListe);
		String[] headers = new String[model.getColumnCount()];

		for (int i = 0; i < model.getColumnCount(); i++)
			headers[i] = model.getColumnName(i);

		CustomJFileChooser customChooser = new CustomJFileChooser("tabConversationsToPdf", bundleOperationsListe, repertoireProjet, false);
		customChooser.show();
		if (customChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION && customChooser.getTxtFile() != null) {
			String txtFile = new String(customChooser.getTxtFile());
			File f = new File(txtFile);
			if (!f.exists()) {
				createNewConversationsPdf(txtFile, conversationsTxtToPdf, headers, model);
			} else {
				int diag = JOptionPane.showOptionDialog(null, bundleOperationsListe.getString("txt_EcraserFichierExistant") + " ?", bundleOperationsListe.getString("txt_ExporterVers"),
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (diag == JOptionPane.OK_OPTION) {
					createNewConversationsPdf(txtFile, conversationsTxtToPdf, headers, model);
				} else
					exportConversationsToPdf(conversationsTxtToPdf);
			}
		} else {
			activitesView.setLabelProgress(bundleOperationsListe.getString("txt_ConversationsPDF"));
			activitesView.getProgressBar().setIndeterminate(false);
			activitesView.getProgressBar().setStringPainted(true);
		}
	}

	private void createNewConversationsPdf(String monFichier, String conversationsTxtToPdf, String[] headers, TabConversationsModel model) {
		Pdf pdf = new Pdf(monFichier, bundleOperationsListe.getString("txt_DocumentGenere") + " " + format.format(new Date()) + "\nProjet : " + nomProjet + "\nListe : " + nom + "\n\n"
				+ bundleOperationsListe.getString("txt_StatsConversations") + "\n\n", conversationsTxtToPdf, bundleOperationsListe.getString("txt_TabConversations"), headers, model);
		pdf.createPdf();
		activitesView.setLabelProgress(bundleOperationsListe.getString("txt_ConversationsPDF"));
		activitesView.appendTxtArea(bundleOperationsListe.getString("txt_ConversationsPDF") + " " + bundleOperationsListe.getString("txt_Accomplie") + "\n");
		activitesView.getProgressBar().setIndeterminate(false);
		activitesView.getProgressBar().setStringPainted(true);
	}

}
