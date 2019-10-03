package comparators;

import java.io.Serializable;
import java.util.Comparator;
import modeles.ListeModel;

public class ListeNumeroComparator implements Comparator<ListeModel>,
		Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(ListeModel l1, ListeModel l2) {
		int n1 = l1.getNumero();
		int n2 = l2.getNumero();
		return n1 > n2 ? +1 : n1 < n2 ? -1 : 0;
	}

}
