package comparators;

import java.util.Comparator;
import java.util.Map;

public class MapStringIntegerComparator implements Comparator<Object> {
	Map<String, Integer> base;

	public MapStringIntegerComparator(Map<String, Integer> base) {
		this.base = base;
	}

	@Override
	public int compare(Object a, Object b) {
		if (base.get(a) < base.get(b)) {
			return 1;
		} else if (base.get(a) == base.get(b)) {
			return ((String) a).compareTo((String) b);
		} else {
			return -1;
		}
	}
}
