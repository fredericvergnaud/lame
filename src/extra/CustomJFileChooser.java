package extra;

import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CustomJFileChooser extends JFileChooser {

	private static final long serialVersionUID = 1L;
	private String type;
	private String repertoireProjet;

	private FileNameExtensionFilter fileFilter;
	private String txtFile;
	private String chooserTitle;
	private boolean folderOnly;

	public CustomJFileChooser(String type, ResourceBundle bundleOperationsListe, String repertoireProjet, boolean folderOnly) {
		super();
		this.type = type;
		this.repertoireProjet = repertoireProjet;
		this.folderOnly = folderOnly;
		System.out.println("répertoire projet = " + repertoireProjet);
		if (type.equals("jpg")) {
			fileFilter = new FileNameExtensionFilter(bundleOperationsListe.getString("txt_FichiersJpg"), "jpg", "jpeg");
			chooserTitle = bundleOperationsListe.getString("txt_EnregistrerJpg");
		} else if (type.equals("net")) {
			fileFilter = new FileNameExtensionFilter(bundleOperationsListe.getString("txt_FichiersNet"), "net");
			chooserTitle = bundleOperationsListe.getString("txt_EnregistrerNet");
		} else if (type.equals("ListeToCsv")) {
			fileFilter = new FileNameExtensionFilter(bundleOperationsListe.getString("txt_FichiersCsv"), "csv");
			chooserTitle = bundleOperationsListe.getString("txt_ListeCsv");
		} else if (type.equals("tabConversationsToPdf")) {
			fileFilter = new FileNameExtensionFilter(bundleOperationsListe.getString("txt_FichiersPdf"), "pdf");
			chooserTitle = bundleOperationsListe.getString("txt_ExportTabConversationsPDFVers");
		} else if (type.equals("tabLocuteursToPdf")) {
			fileFilter = new FileNameExtensionFilter(bundleOperationsListe.getString("txt_FichiersPdf"), "pdf");
			chooserTitle = bundleOperationsListe.getString("txt_ExportTabLocuteursPDFVers");
		} else if (type.equals("ExportListe")) {
			fileFilter = new FileNameExtensionFilter(bundleOperationsListe.getString("txt_FichiersLame"), "lame");
			chooserTitle = bundleOperationsListe.getString("txt_ExportListeVers");
		} else if (type.equals("ImportListe")) {
			fileFilter = new FileNameExtensionFilter(bundleOperationsListe.getString("txt_FichiersLame"), "lame");
			chooserTitle = bundleOperationsListe.getString("txt_ImportListe");
		}

	}

	@Override
	public void show() {
		//System.out.println("CustomJFileChooser - show() : folderOnly = "+folderOnly);
		setDialogTitle(chooserTitle);
		setCurrentDirectory(new File(repertoireProjet));
		if (!folderOnly) {			
			addChoosableFileFilter(fileFilter);
			setFileFilter(fileFilter);
			File file = getSelectedFile();
			if (file != null)
				setTxtFile(getExtension(file, type));
		} else
			setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);		
	}

	public String getExtension(File file, String extension) {
		String txtFile = new String(file.toString());
		if (type.equals("jpg")) {
			if (!(txtFile.endsWith(".jpg") || txtFile.endsWith(".JPG")))
				txtFile = txtFile + ".jpg";
		} else if (type.equals("net")) {
			if (!(txtFile.endsWith(".net") || txtFile.endsWith(".NET")))
				txtFile = txtFile + ".net";
		} else if (type.equals("tabConversationsToPdf") || type.equals("tabLocuteursToPdf")) {
			if (!(txtFile.endsWith(".pdf") || txtFile.endsWith(".PDF")))
				txtFile = txtFile + ".pdf";
		} else if (type.equals("ListeToCsv")) {
			if (!(txtFile.endsWith(".csv") || txtFile.endsWith(".CSV")))
				txtFile = txtFile + ".csv";
		} else if (type.equals("ExportListe")) {
			if (!(txtFile.endsWith(".lame") || txtFile.endsWith(".LAME")))
				txtFile = txtFile + ".lame";
		}
		return txtFile;
	}

	public String getTxtFile() {
		return txtFile;
	}

	public void setTxtFile(String txtFile) {
		this.txtFile = txtFile;
	}	
}
