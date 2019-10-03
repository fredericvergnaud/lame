package controleurs.operations.projet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import modeles.ProjetModel;

public class OpenProjet {

	private ProjetModel openedProjet = null;
	private File file = null;
	private ResourceBundle bundleProjetController;

	public OpenProjet(ResourceBundle bundleProjetController) {
		this.bundleProjetController = bundleProjetController;
	}

	public void displayDialog() {
		// System.out.println("OpenProjet - displayDialog : bundleProjetController = "+bundleProjetController);
		FileFilter objFilter = new FileNameExtensionFilter(bundleProjetController.getString("txt_FichiersLame"), "lame");
		final JFileChooser fc = new JFileChooser();
		fc.setDialogTitle(bundleProjetController.getString("txt_OuvrirProjet"));
		// fc.addChoosableFileFilter(objFilter);
		fc.setFileFilter(objFilter);
		int returnVal = fc.showOpenDialog(fc.getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			if (fc.getSelectedFile().getName().indexOf(".lame") != -1) {
				setFile(fc.getSelectedFile());
				FileInputStream fis;
				ObjectInputStream ois = null;
				try {
					fis = new FileInputStream(file);
					ois = new ObjectInputStream(fis);
					openedProjet = (ProjetModel) ois.readObject();
					ois.close();
				} catch (IOException ioe) {
					JOptionPane.showMessageDialog(null, bundleProjetController.getString("txt_ErreurRestaurationProjet_1"), bundleProjetController.getString("txt_RestaurationProjet"),
							JOptionPane.ERROR_MESSAGE);
					System.out.println("Projet/RestoreProjet : erreur de restauration IOException: " + ioe.getMessage());
				} catch (ClassNotFoundException cnf) {
					JOptionPane.showMessageDialog(null, bundleProjetController.getString("txt_ErreurRestaurationProjet_2"), bundleProjetController.getString("txt_RestaurationProjet"),
							JOptionPane.ERROR_MESSAGE);
					System.out.println("Projet/RestoreProjet : erreur de restauration ClassNotFoundException: " + cnf.getMessage());
				}
				if (openedProjet != null)
					setOpenedProjet(openedProjet);

			} else {
				JOptionPane.showMessageDialog(null, bundleProjetController.getString("txt_FichierInvalide"), "Erreur", JOptionPane.ERROR_MESSAGE);
				displayDialog();
			}
		}
	}

	public ProjetModel getOpenedProjet() {
		return openedProjet;
	}

	public void setOpenedProjet(ProjetModel openedProjet) {
		this.openedProjet = openedProjet;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
