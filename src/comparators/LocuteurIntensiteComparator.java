package comparators;

import java.io.Serializable;
import java.util.Comparator;
import modeles.LocuteurModel;

public class LocuteurIntensiteComparator implements Comparator<LocuteurModel>,
		Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(LocuteurModel l1, LocuteurModel l2) {
		float f1 = l1.getIntensite();
		float f2 = l2.getIntensite();
		return f1 > f2 ? -1 : f1 < f2 ? +1 : +1;
	}

}
