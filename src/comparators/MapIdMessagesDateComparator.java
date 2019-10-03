package comparators;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import modeles.MessageModel;

public class MapIdMessagesDateComparator implements Comparator<Object> {

	private Map<String, MessageModel> map;
	private boolean ascendant;

	public MapIdMessagesDateComparator(Map<String, MessageModel> map, boolean ascendant) {
		this.map = map;
		this.ascendant = ascendant;
	}

	@Override
	public int compare(Object a, Object b) {
		Date dateA = map.get(a).getDateUS();
		Date dateB = map.get(b).getDateUS();
		if (ascendant) {
			if (!dateA.equals(dateB))
				return dateA.compareTo(dateB);
			else
				return 1;
		}
		else {
			if (!dateA.equals(dateB))
				return dateB.compareTo(dateA);
			else
				return -1;
		}
			
		// if (dateA != null && dateB != null) {
		// if (dateA.after(dateB) || dateA.equals(dateB)) {
		// return 1;
		// } else {
		// return -1;
		// }
		// } else
		// return -1;
	}
}
