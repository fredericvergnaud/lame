package vue.projetPanelSup;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;

import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import renderers.RoundedPanel;
import modeles.evenements.ProjetChangedEvent;
import modeles.evenements.ProjetListeAddedEvent;
import modeles.trees.ListesTreeModel;
import controleurs.ProjetController;
import controleurs.vuesabstraites.ProjetView;

public class TreeListesProjetPanel extends ProjetView {

	private RoundedPanel panel;
	private JTree treeProjet;
	private int numListeSelected;
	private ListesTreeModel listesTreeModel;
	private JScrollPane scrollTreeListes;
	private ResourceBundle infosListeListesPanel;

	public TreeListesProjetPanel(ProjetController projetController, ResourceBundle infosListeListesPanel) {
		super(projetController);
		this.infosListeListesPanel = infosListeListesPanel;
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
		
		listesTreeModel = new ListesTreeModel();
		treeProjet = new JTree();
		treeProjet.setModel(listesTreeModel);
		TreeCellRenderer cr = treeProjet.getCellRenderer();
		if (cr instanceof DefaultTreeCellRenderer) {
			DefaultTreeCellRenderer dtcr = (DefaultTreeCellRenderer) cr;
			dtcr.setMinimumSize(new Dimension(350, 20));
			dtcr.setPreferredSize(new Dimension(350, 20));
		}
		treeProjet.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		treeProjet.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent tsl) {
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeProjet.getLastSelectedPathComponent();
				// System.out.println("selectedNode = " + selectedNode);
				if (selectedNode != null && selectedNode.isLeaf() && !selectedNode.isRoot()) {
					String sNode = selectedNode.toString();
					String[] tabNode = sNode.split("\\) ");
					numListeSelected = Integer.valueOf(tabNode[0].substring(1));
				} else
					numListeSelected = 0;
				// System.out
				// .println("TreeListesProjetPanel - getPanel - valueChanged : numListeSelected = "
				// + numListeSelected);
				getProjetController().notifyListeSelected(numListeSelected);
			}
		});

		MouseListener ml = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int selRow = treeProjet.getRowForLocation(e.getX(), e.getY());
				if (selRow != -1) {
					if (e.getClickCount() == 2) {
						// System.out.println("double clic : " + selRow + " , "
						// + selPath);
						DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeProjet.getLastSelectedPathComponent();
						// System.out.println("selectedNode = " + selectedNode);
						String oldName;
						if (selectedNode == null || !selectedNode.isLeaf() || selectedNode.isRoot()) {
							numListeSelected = 0;
							oldName = "";
						} else {
							String sNode = selectedNode.toString();
							String[] tabNode = sNode.split("\\) ");
							numListeSelected = Integer.valueOf(tabNode[0].substring(1));
							oldName = tabNode[1].trim();
						}
						// System.out.println("numListeSelected = "
						// + numListeSelected);
						if (numListeSelected != 0) {
							getProjetController().notifyModifyNomListe(oldName);

						}
					}
				}
			}
		};
		treeProjet.addMouseListener(ml);
		// treeProjet.setRootVisible(false);
		scrollTreeListes = new JScrollPane(treeProjet);
		scrollTreeListes.setPreferredSize(new Dimension(400,100));
		scrollTreeListes.setMaximumSize(new Dimension(400,100));
		scrollTreeListes.setMinimumSize(new Dimension(400,100));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		panel.add(scrollTreeListes, gbc);
		return panel;
	}

	public int getRowSelectedNode(Object rootListe, String nomListe, int numeroListe) {
		int row = 0;
		TreeNode root = (TreeNode) rootListe;
		for (int i = 0; i < root.getChildCount(); i++) {
			if (root.getChildAt(i).isLeaf()) {
				String txtNode = root.getChildAt(i).toString();
				String[] tabNode = txtNode.split("\\) ");
				String numeroListeRecherche = tabNode[0];
				numeroListeRecherche = numeroListeRecherche.replace("(", "");
				int iNumeroListeRecherche = Integer.parseInt(numeroListeRecherche);
				String nomListeRecherche = tabNode[1];
				if (numeroListe == iNumeroListeRecherche && nomListe.equals(nomListeRecherche)) {
					row = i + 1;
					break;
				}
			}
		}
		return row;
	}

	@Override
	public JPanel getInfosProjetPanel() {
		return null;
	}

	@Override
	public JMenuBar getMenuBar() {
		return null;
	}

	@Override
	public JPanel getListesListePanel() {
		return getPanel();
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
		listesTreeModel = new ListesTreeModel(getProjetController().getNomProjet(), getProjetController().getSetListes());
		treeProjet.setModel(listesTreeModel);

		if (event.getListeSelected() != null && event.getListeSelected().getNumero() != 0) {
			// treeProjet.setCellRenderer(new listeListesRenderer());
			// System.out
			// .println("TreeListesProjetPanel - projetChanged : listeSelected = "
			// + event.getListeSelected().getNom());
			treeProjet.expandRow(0);
			treeProjet.setSelectionRow(getRowSelectedNode(listesTreeModel.getRoot(), event.getListeSelected().getNom(), event.getListeSelected().getNumero()));

			TreePath treePathSelectedList = treeProjet.getSelectionPath();
			treeProjet.scrollPathToVisible(treePathSelectedList);
			// treeProjet.updateUI();
		}
	}

	@Override
	public void projetListeAdded(ProjetListeAddedEvent event) {
		listesTreeModel.addNewListe(event.getNewListe().getNumero(), event.getNewListe());
		treeProjet.expandRow(0);
		treeProjet.setSelectionRow(getRowSelectedNode(listesTreeModel.getRoot(), event.getNewListe().getNom(), event.getNewListe().getNumero()));
		TreePath treePathSelectedList = treeProjet.getSelectionPath();
		treeProjet.scrollPathToVisible(treePathSelectedList);
		// treeProjet.updateUI();
	}
}
