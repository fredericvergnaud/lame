package comparators;

import java.util.Comparator;
import modeles.MessageModel;

public class MessageIdentifiantComparator implements Comparator<MessageModel> {

	@Override
	public int compare(MessageModel m1, MessageModel m2) {
		String id1 = m1.getIdentifiant();
		String id2 = m2.getIdentifiant();
		if (!id1.equals(null) && !id2.equals(null)) {
			return id1.compareTo(id2);
		} else {
			return 1;
		}

	}

}
