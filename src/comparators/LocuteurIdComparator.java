package comparators;

import java.io.Serializable;
import java.util.Comparator;
import modeles.LocuteurModel;

public class LocuteurIdComparator implements Comparator<LocuteurModel>,
		Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(LocuteurModel l1, LocuteurModel l2) {
		int n1 = l1.getId();
		int n2 = l2.getId();
		return n1 > n2 ? +1 : n1 < n2 ? -1 : 0;
	}

}
