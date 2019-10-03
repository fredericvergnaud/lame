package comparators;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

import modeles.MessageModel;

public class MessageDateUsComparator implements Comparator<MessageModel> {

	@Override
	public int compare(MessageModel m1, MessageModel m2) {
		Date dateUs1 = m1.getDateUS();
		Date dateUs2 = m2.getDateUS();
		if (dateUs1.after(dateUs2)) {
			return 1;
		} else if (dateUs1.equals(dateUs2)) {
			boolean parsable = true;
			int id1 = 0, id2 = 0;
			try {
				id1 = Integer.parseInt(m1.getIdentifiant());
				id2 = Integer.parseInt(m2.getIdentifiant());
			} catch (NumberFormatException e) {
				parsable = false;
			}
			if (parsable)
				if (id1 > id2)
					return 1;
				else
					return -1;
			else
				return -1;
		} else
			return -1;

	}

}
