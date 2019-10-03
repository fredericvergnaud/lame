package modeles.trees;

import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import modeles.ListeModel;

public class ListesTreeModel extends DefaultTreeModel {

	private static final long serialVersionUID = 1L;
	
	public ListesTreeModel() {
		super(null);
	}

	public ListesTreeModel(String nomProjet, Set<ListeModel> setListes) {
		super(null);
		setRoot(new DefaultMutableTreeNode(nomProjet));
		for (ListeModel liste : setListes) {
			insertNodeInto(liste, (MutableTreeNode) root, root.getChildCount());			
		}	
	}

	public void addNewListe(int numListe, ListeModel newListe) {
		insertNodeInto(newListe, (MutableTreeNode) root, root.getChildCount());
	}
	
	
}
