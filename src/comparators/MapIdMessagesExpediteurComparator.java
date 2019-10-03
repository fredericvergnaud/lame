package comparators;

import java.util.Comparator;
import java.util.Map;

import modeles.MessageModel;

public class MapIdMessagesExpediteurComparator implements Comparator<Object> {

	private Map<String, MessageModel> map;
	private boolean ascendant;

	public MapIdMessagesExpediteurComparator(Map<String, MessageModel> map, boolean ascendant) {
		this.map = map;
		this.ascendant = ascendant;
	}

	@Override
	public int compare(Object a, Object b) {
		String expA = map.get(a).getExpediteur();
		String expB = map.get(b).getExpediteur();
		if (expA.equals(expB))
			return -1;
		else {
			if (ascendant)
				return expA.compareToIgnoreCase(expB);
			else
				return expB.compareToIgnoreCase(expA);

		}
	}
}
