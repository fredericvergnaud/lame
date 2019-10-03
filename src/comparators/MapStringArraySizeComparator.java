package comparators;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MapStringArraySizeComparator implements Comparator<Object> {
	Map<String, TreeSet<Integer>> base;

	public MapStringArraySizeComparator(Map<String, TreeSet<Integer>> base) {
		this.base = base;
	}

	@Override
	public int compare(Object a, Object b) {
		int numPagesSizeA = base.get(a).size();
		int numPagesSizeB = base.get(b).size();
		if (numPagesSizeA == numPagesSizeB && numPagesSizeA == 1) {
			int numPageA = base.get(a).first();
			int numPageB = base.get(b).first();
			if (numPageA < numPageB)
				return 1;
			else
				return -1;
		} else if (numPagesSizeA < numPagesSizeB || numPagesSizeA == numPagesSizeB)
			return 1;
		else
			return -1;
	}
}
