package comparators;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import modeles.TopicModel;

public class MapTopicUrlTopicNumComparator implements Comparator<Object> {
	
	private Map<String, TopicModel> base;

	public MapTopicUrlTopicNumComparator(Map<String, TopicModel> base) {
		this.base = base;
	}

	@Override
	public int compare(Object a, Object b) {
		int idA = base.get(a).getId();
		int idB = base.get(b).getId();
//		System.out.println("Comparator : id a = " + idA + " | id b = " + idB);
		if (idA > idB)
			return 1;
		else if (idA == idB)
			return 0;
		else
			return -1;
	}
}
