package comparators;

import java.io.Serializable;
import java.util.Comparator;
import modeles.ConversationModel;

public class ConversationIdComparator implements Comparator<ConversationModel>,
		Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public int compare(ConversationModel c1, ConversationModel c2) {
		int n1 = c1.getId();
		int n2 = c2.getId();
		return n1 > n2 ? +1 : n1 < n2 ? -1 : 0;
	}

}
