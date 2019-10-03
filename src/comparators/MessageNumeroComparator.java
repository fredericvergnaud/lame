package comparators;

import java.util.Comparator;
import modeles.MessageModel;

public class MessageNumeroComparator implements Comparator<MessageModel> {

	@Override
	public int compare(MessageModel m1, MessageModel m2) {
		int numero1 = Integer.parseInt(m1.getNumero());
		int numero2 = Integer.parseInt(m2.getNumero());

		return numero1 > numero2 ? +1 : numero1 < numero2 ? -1 : 0;	
	}

}
